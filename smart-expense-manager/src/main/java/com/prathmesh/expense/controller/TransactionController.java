package com.prathmesh.expense.controller;

import com.prathmesh.expense.dto.TransactionRequest;
import com.prathmesh.expense.entity.Transaction;
import com.prathmesh.expense.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/api/transactions")
@Tag(
        name = "Transaction APIs",
        description = "Create, view, update and delete income and expense transactions"
)
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {

        return transactionService.createTransaction(
                request,
                authentication
        );

    }

    @GetMapping
    public java.util.List<Transaction> getMyTransactions(
            Authentication authentication) {

        return transactionService.getMyTransactions(authentication);
    }
    @GetMapping("/filter")
    public java.util.List<Transaction> getTransactionsByType(
            @RequestParam String type,
            Authentication authentication) {

        return transactionService.getMyTransactionsByType(
                type,
                authentication
        );
    }

    @GetMapping("/filter/category")
    public java.util.List<Transaction> getTransactionsByCategory(
            @RequestParam Long categoryId,
            Authentication authentication) {

        return transactionService.getMyTransactionsByCategory(
                categoryId,
                authentication
        );
    }

    @GetMapping("/filter/date")
    public java.util.List<Transaction> getTransactionsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            Authentication authentication) {

        return transactionService.getMyTransactionsByDateRange(
                startDate,
                endDate,
                authentication
        );
    }

    @GetMapping("/filter/advanced")
    public java.util.List<Transaction> filterTransactions(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Authentication authentication) {

        return transactionService.filterTransactions(
                type,
                categoryId,
                startDate,
                endDate,
                authentication
        );
    }

    @GetMapping("/page")
    public Page<Transaction> getMyTransactionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            Authentication authentication) {

        org.springframework.data.domain.Sort.Direction sortDirection =
                direction.equalsIgnoreCase("asc")
                        ? org.springframework.data.domain.Sort.Direction.ASC
                        : org.springframework.data.domain.Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                org.springframework.data.domain.Sort.by(
                        sortDirection,
                        sortBy
                )
        );

        return transactionService.getMyTransactionsPaginated(
                pageable,
                authentication
        );
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {

        return transactionService.updateTransaction(
                id,
                request,
                authentication
        );
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(
            @PathVariable Long id,
            Authentication authentication) {

        transactionService.deleteTransaction(
                id,
                authentication
        );

        return "Transaction deleted successfully";
    }
}
