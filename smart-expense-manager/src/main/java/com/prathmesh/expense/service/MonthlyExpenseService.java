package com.prathmesh.expense.service;

import com.prathmesh.expense.dto.MonthlyExpenseResponse;
import com.prathmesh.expense.entity.User;
import com.prathmesh.expense.repository.TransactionRepository;
import com.prathmesh.expense.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class MonthlyExpenseService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public MonthlyExpenseService(
            TransactionRepository transactionRepository,
            UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public MonthlyExpenseResponse getCurrentMonthExpense(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        YearMonth currentMonth = YearMonth.now();

        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        BigDecimal totalExpense =
                transactionRepository.getMonthlyExpense(
                        user.getId(),
                        startDate,
                        endDate
                );

        return new MonthlyExpenseResponse(
                currentMonth.toString(),
                totalExpense
        );
    }
}