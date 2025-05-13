package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.TransactionDTO;
import dev.danilo.moto2000.dto.TransactionItemRequest;
import dev.danilo.moto2000.dto.TransactionRequest;
import dev.danilo.moto2000.entity.Product;
import dev.danilo.moto2000.entity.Supplier;
import dev.danilo.moto2000.entity.Transaction;
import dev.danilo.moto2000.entity.TransactionItem;
import dev.danilo.moto2000.enums.TransactionStatus;
import dev.danilo.moto2000.enums.TransactionType;
import dev.danilo.moto2000.exceptions.NameValueRequiredException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.ProductRepository;
import dev.danilo.moto2000.repository.SupplierRepository;
import dev.danilo.moto2000.repository.TransactionItemRepository;
import dev.danilo.moto2000.repository.TransactionRepository;
import dev.danilo.moto2000.service.ClientService;
import dev.danilo.moto2000.service.TransactionService;
import dev.danilo.moto2000.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    private final ModelMapper mapper;
    private final ClientService clientService;
    private final TransactionItemRepository transactionItemRepository;
    private final ProductRepository productRepository;

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
        return null;
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
