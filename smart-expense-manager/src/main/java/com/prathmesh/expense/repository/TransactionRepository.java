package com.prathmesh.expense.repository;

import com.prathmesh.expense.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    Page<Transaction> findByUserId(
            Long userId,
            Pageable pageable
    );

    List<Transaction> findByUserIdAndType(
            Long userId,
            String type
    );

    List<Transaction> findByUserIdAndCategoryId(
            Long userId,
            Long categoryId
    );

    List<Transaction> findByUserIdAndTransactionDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("""
       SELECT COALESCE(SUM(t.amount), 0)
       FROM Transaction t
       WHERE t.user.id = :userId
       AND t.type = 'INCOME'
       """)
    BigDecimal getTotalIncome(@Param("userId") Long userId);

    @Query("""
       SELECT COALESCE(SUM(t.amount), 0)
       FROM Transaction t
       WHERE t.user.id = :userId
       AND t.type = 'EXPENSE'
       """)
    BigDecimal getTotalExpense(@Param("userId") Long userId);

    @Query("""
       SELECT COALESCE(SUM(t.amount), 0)
       FROM Transaction t
       WHERE t.user.id = :userId
       AND t.type = 'EXPENSE'
       AND t.transactionDate BETWEEN :startDate AND :endDate
       """)
    BigDecimal getMonthlyExpense(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
       SELECT t
       FROM Transaction t
       WHERE t.user.id = :userId
       AND (:type IS NULL OR t.type = :type)
       AND (:categoryId IS NULL OR t.category.id = :categoryId)
       AND (:startDate IS NULL OR t.transactionDate >= :startDate)
       AND (:endDate IS NULL OR t.transactionDate <= :endDate)
       ORDER BY t.transactionDate DESC
       """)
    List<Transaction> filterTransactions(
            @Param("userId") Long userId,
            @Param("type") String type,
            @Param("categoryId") Long categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

