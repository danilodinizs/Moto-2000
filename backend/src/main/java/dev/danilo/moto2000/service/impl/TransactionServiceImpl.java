package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
                .note(transactionRequest.getNote())
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
                .client(client)
                .note(transactionRequest.getNote())
                .transactionPaymentMethod(transactionRequest.getTransactionPaymentMethod())
                .description(transactionRequest.getDescription())
                .serviceOrders(soS)
                .items(transactionItems)
                .build();

        for (TransactionItem item : transactionItems) {
            item.setTransaction(transaction);
        }

        // Persiste a transação (os itens serão salvos em cascata)
        repository.save(transaction);

        // Transforma o set de service order em um set de service order dto
        Set<ServiceOrderDTO> sosDTO = new HashSet<>();

        soS.forEach(serviceOrder -> {
            // define a transação de cada so como null para evitar loop
            serviceOrder.setTransaction(null);

            // faz o map de cada so pra dto
            ServiceOrderDTO soDTO = mapper.map(serviceOrder, ServiceOrderDTO.class);

            // cria um set de ids para inserir em cada so os ids do set de produtos
            Set<UUID> ids = new HashSet<>();

            // itera sobre o set de produtos do so e guarda no seti
            serviceOrder.getProducts().forEach(product -> {
                ids.add(product.getId());
            });

            // coloca o set de ids no so
            soDTO.setProductsIds(ids);

            sosDTO.add(soDTO);
        });

        // Coloca o client da transação como null
        TransactionDTO dto = mapper.map(transaction, TransactionDTO.class);
        dto.setClient(null);
        dto.setServiceOrder(sosDTO);

        return Response.builder()
                .status(200)
                .message("Venda concluída com sucesso")
                .transaction(dto)
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequest request) {
        List<TransactionItemRequest> items = request.getItems();

        List<TransactionItem> itemsEntity = new ArrayList<>();

        int totalProducts = 0;

        for (TransactionItemRequest item : items) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado"));

            itemsEntity.add(mapper.map(items, TransactionItem.class));

            totalProducts += item.getQuantity();

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.RETORNO_AO_FORNECEDOR)
                .transactionStatus(TransactionStatus.PROCESSANDO)
                .totalProducts(totalProducts)
                .totalPrice(BigDecimal.ZERO)
                .note(request.getNote())
                .items(itemsEntity)
                .description(request.getDescription())
                .build();

        repository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Processo de retorno ao fornecedor iniciado")
                .build();
    }

    @Override
    public Response getAllTransactions(int page, int size, String searchText) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Transaction> transactionPage = repository.searchTransactions(searchText, pageable);
        List<TransactionDTO> transactionDTOS = new ArrayList<>();

        for (Transaction transaction : transactionPage) {
            transactionDTOS.add(mapper.map(transaction, TransactionDTO.class));
        }

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setClient(null);
            transactionDTO.setItems(null);
            transactionDTO.setServiceOrder(null);
        });

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response getTransactionById(UUID id) {
        Transaction transaction = repository.findById(id).orElseThrow(() -> new NotFoundException("Transação não encontrada"));

        TransactionDTO dto = mapper.map(transaction, TransactionDTO.class);

        if(dto.getClient() != null) {
            dto.getClient().setTransactions(null);
        }

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .transaction(dto)
                .build();
    }

    @Override
    public Response getAllTransactionsByMonthAndYear(int month, int year) {
        List<Transaction> transactions = repository.findAllByMonthAndYear(month, year);

        List<TransactionDTO> transactionDTOS = new ArrayList<>();

        for (Transaction transaction : transactions) {
            transactionDTOS.add(mapper.map(transaction, TransactionDTO.class));
        }

        transactionDTOS.forEach(transactionDTO -> {
            transactionDTO.setClient(null);
            transactionDTO.setItems(null);
            transactionDTO.setServiceOrder(null);
        });

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .transactions(transactionDTOS)
                .build();
    }

    @Override
    public Response updateTransactionStatus(UUID id, TransactionStatus transactionStatus) {
        Transaction transaction = repository.findById(id).orElseThrow(() -> new NotFoundException("Transação não encontrada"));

        transaction.setTransactionStatus(transactionStatus);
        transaction.setUpdatedAt(LocalDateTime.now());

        repository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Status da transação atualizado com sucesso")
                .build();
    }
}
