package com.prathmesh.expense.service;

import com.prathmesh.expense.dto.TransactionRequest;
import com.prathmesh.expense.entity.Category;
import com.prathmesh.expense.entity.Transaction;
import com.prathmesh.expense.entity.User;
import com.prathmesh.expense.repository.CategoryRepository;
import com.prathmesh.expense.repository.TransactionRepository;
import com.prathmesh.expense.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository) {

        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Transaction createTransaction(
            TransactionRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        Transaction transaction = new Transaction();

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());

        return transactionRepository.save(transaction);
    }

    public java.util.List<Transaction> getMyTransactions(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return transactionRepository.findByUserId(user.getId());
    }

    public Page<Transaction> getMyTransactionsPaginated(
            Pageable pageable,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return transactionRepository.findByUserId(
                user.getId(),
                pageable
        );
    }

    public List<Transaction> getMyTransactionsByType(
            String type,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return transactionRepository.findByUserIdAndType(
                user.getId(),
                type.toUpperCase()
        );
    }

    public List<Transaction> getMyTransactionsByCategory(
            Long categoryId,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return transactionRepository.findByUserIdAndCategoryId(
                user.getId(),
                categoryId
        );
    }

    public List<Transaction> getMyTransactionsByDateRange(
            LocalDate startDate,
            LocalDate endDate,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException(
                    "Start date cannot be after end date"
            );
        }

        return transactionRepository
                .findByUserIdAndTransactionDateBetween(
                        user.getId(),
                        startDate,
                        endDate
                );
    }

    public List<Transaction> filterTransactions(
            String type,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (startDate != null &&
                endDate != null &&
                startDate.isAfter(endDate)) {

            throw new RuntimeException(
                    "Start date cannot be after end date"
            );
        }

        if (type != null) {
            type = type.toUpperCase();
        }

        return transactionRepository.filterTransactions(
                user.getId(),
                type,
                categoryId,
                startDate,
                endDate
        );
    }

    public Transaction updateTransaction(
            Long transactionId,
            TransactionRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not allowed to update this transaction");
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new RuntimeException("Category not found"));

        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());

        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(
            Long transactionId,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You are not allowed to delete this transaction");
        }

        transactionRepository.delete(transaction);
    }
}