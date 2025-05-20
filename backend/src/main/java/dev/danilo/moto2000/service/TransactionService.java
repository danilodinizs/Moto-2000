package dev.danilo.moto2000.service;

import dev.danilo.moto2000.dto.CategoryDTO;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.TransactionRequest;
import dev.danilo.moto2000.enums.TransactionStatus;

import java.util.UUID;

public interface TransactionService {

    Response restockInventory(TransactionRequest transactionRequest);
    Response sell(TransactionRequest transactionRequest);
    Response returnToSupplier(TransactionRequest transactionRequest);
    Response getAllTransactions(int page, int size, String searchText);
    Response getTransactionById(UUID id);
    Response getAllTransactionsByMonthAndYear(int month, int year);
    Response updateTransactionStatus(UUID id, TransactionStatus transactionStatus);

}
