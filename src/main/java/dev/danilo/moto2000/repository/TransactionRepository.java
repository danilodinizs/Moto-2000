package dev.danilo.moto2000.repository;

import dev.danilo.moto2000.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE YEAR(t.createdAt) = :year AND MONTH(t.createdAt) = :month")
    List<Transaction> findAllByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT t FROM Transaction t " +
            "LEFT JOIN t.items i " +
            "LEFT JOIN i.product p " +
            "WHERE (:searchText IS NULL OR " +
            "       LOWER(t.description) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "       LOWER(t.note) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "       LOWER(t.transactionStatus) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "       LOWER(t.transactionType) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "       LOWER(t.transactionPaymentMethod) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "       LOWER(p.name) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "       LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchText, '%')))")
    Page<Transaction> searchTransactions(@Param("searchText") String searchText, Pageable pageable);


}
