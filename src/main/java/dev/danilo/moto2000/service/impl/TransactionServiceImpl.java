package dev.danilo.moto2000.service.impl;

import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.TransactionItemRequest;
import dev.danilo.moto2000.dto.TransactionRequest;
import dev.danilo.moto2000.entity.Product;
import dev.danilo.moto2000.entity.Supplier;
import dev.danilo.moto2000.entity.Transaction;
import dev.danilo.moto2000.enums.TransactionStatus;
import dev.danilo.moto2000.enums.TransactionType;
import dev.danilo.moto2000.exceptions.NameValueRequiredException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.repository.ProductRepository;
import dev.danilo.moto2000.repository.SupplierRepository;
import dev.danilo.moto2000.repository.TransactionRepository;
import dev.danilo.moto2000.service.ClientService;
import dev.danilo.moto2000.service.TransactionService;
import dev.danilo.moto2000.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final ProductRepository productRepository;

    @Override
    public Response restockInventory(TransactionRequest transactionRequest) {

        List<TransactionItemRequest> items = transactionRequest.getItems();

        int totalProducts = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (TransactionItemRequest item : items) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(() -> new NotFoundException("Produto não encontrado"));

            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantidade inválida para o produto: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);

            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            totalProducts += item.getQuantity();
        }


        Transaction.builder()
                .transactionType(TransactionType.COMPRA)
                .transactionStatus(TransactionStatus.CONCLUÍDO)
                .totalProducts(totalProducts)
                .totalPrice(totalPrice)
                //.products(null) resolver problema no qual tem que ser retornado um set de products mas precisa mostrar quais products e qual quantidade foi, verificar o que pode ser feito.
                .description(transactionRequest.getDescription())
                .build();


        return null;
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
