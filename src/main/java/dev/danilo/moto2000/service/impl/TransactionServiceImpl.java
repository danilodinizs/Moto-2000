package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.TransactionDTO;
import dev.danilo.moto2000.dto.TransactionItemRequest;
import dev.danilo.moto2000.dto.TransactionRequest;
import dev.danilo.moto2000.entity.*;
import dev.danilo.moto2000.enums.TransactionStatus;
import dev.danilo.moto2000.enums.TransactionType;
import dev.danilo.moto2000.exceptions.InvalidDataException;
import dev.danilo.moto2000.exceptions.NameValueRequiredException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.*;
import dev.danilo.moto2000.service.ClientService;
import dev.danilo.moto2000.service.TransactionService;
import dev.danilo.moto2000.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    private final ModelMapper mapper;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    @Transactional
    @Override
    public Response restockInventory(TransactionRequest transactionRequest) {
        // Validação inicial
        if (transactionRequest.getItems() == null || transactionRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("A transação deve conter itens");
        }

        List<TransactionItem> transactionItems = new ArrayList<>();
        int totalProducts = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Processa cada item
        for (TransactionItemRequest itemRequest : transactionRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

            if (itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantidade inválida para o produto: " + product.getName());
            }

            // Atualiza estoque
            product.setStockQuantity(product.getStockQuantity() + itemRequest.getQuantity());
            productRepository.save(product);

            // Calcula totais
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            totalProducts += itemRequest.getQuantity();

            // Cria o item da transação
            TransactionItem transactionItem = TransactionItem.builder()
                    .quantity(itemRequest.getQuantity())
                    .product(product)
                    .build();

            transactionItems.add(transactionItem);
        }

        // Cria a transação
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.COMPRA)
                .transactionStatus(TransactionStatus.CONCLUÍDO)
                .totalProducts(totalProducts)
                .totalPrice(totalPrice)
                .transactionPaymentMethod(transactionRequest.getTransactionPaymentMethod())
                .description(transactionRequest.getDescription())
                .build();

        // Estabelece a relação bidirecional
        transactionItems.forEach(item -> item.setTransaction(transaction));
        transaction.setItems(transactionItems);

        // Persiste a transação (os itens serão salvos em cascata)
        repository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Estoque reabastecido com sucesso")
                .transaction(mapper.map(transaction, TransactionDTO.class))
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {

        // Validação inicial
        if (transactionRequest.getClientId() == null) {
            throw new IllegalArgumentException("O cliente deve ser informado na transação");
        }

        if (transactionRequest.getItems() == null || transactionRequest.getItems().isEmpty()) {
            throw new IllegalArgumentException("A transação deve conter itens");
        }

        Client client = clientRepository.findById(transactionRequest.getClientId()).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        List<TransactionItem> transactionItems = new ArrayList<>();
        int totalProducts = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Processa cada item
        for (TransactionItemRequest itemRequest : transactionRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

            if (itemRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantidade inválida para o produto: " + product.getName());
            }

            // Atualiza estoque
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            // Calcula totais
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            totalProducts += itemRequest.getQuantity();

            // Cria o item da transação
            TransactionItem transactionItem = TransactionItem.builder()
                    .quantity(itemRequest.getQuantity())
                    .product(product)
                    .build();

            transactionItems.add(transactionItem);
        }


        Set<ServiceOrder> soS = new HashSet<>();

        if (transactionRequest.getServiceOrderIds() != null) {

            Set<UUID> serviceOrderIds = new HashSet<>(transactionRequest.getServiceOrderIds());

            for (UUID soId : serviceOrderIds) {
                ServiceOrder so = serviceOrderRepository.findById(soId).orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada com o ID: " + soId));

                if(so.getTransaction() != null) {
                    Response response = new Response();
                    response.setStatus(409);
                    response.setMessage("Essa ordem de serviço já está fechada: " + so.getId());
                    throw new InvalidDataException(response);
                }

                Set<Product> products = new HashSet<>(so.getProducts());

                for (Product product : products) {
                    if (product.getStockQuantity() < 1) {
                        throw new IllegalStateException("Produto " + product.getName() + " não possui estoque suficiente");
                    }

                    product.setStockQuantity(product.getStockQuantity() - 1);
                    totalPrice = totalPrice.add(product.getPrice());
                    totalProducts += 1;

                    productRepository.save(product);
                }

                if (so.getLaborCost() != null) {
                    totalPrice = totalPrice.add(so.getLaborCost());
                }

                soS.add(so);
            }
        }

        // Cria a transação
        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.VENDA)
                .transactionStatus(TransactionStatus.CONCLUÍDO)
                .totalProducts(totalProducts)
                .totalPrice(totalPrice)
                .transactionPaymentMethod(transactionRequest.getTransactionPaymentMethod())
                .description(transactionRequest.getDescription())
                .serviceOrders(soS)
                .build();

        // Persiste a transação (os itens serão salvos em cascata)
        repository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Venda concluída com sucesso")
                .transaction(mapper.map(transaction, TransactionDTO.class))
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {
        return null;
    }

    @Override
    public Response getAllTransactions(int page, int size, String searchText) {
        return null;
    }

    @Override
    public Response getTransactionById(UUID id) {
        return null;
    }

    @Override
    public Response getAllTransactionsByMonthAndYear(int month, int year) {
        return null;
    }

    @Override
    public Response updateTransactionStatus(UUID id, TransactionStatus transactionStatus) {
        return null;
    }
}
