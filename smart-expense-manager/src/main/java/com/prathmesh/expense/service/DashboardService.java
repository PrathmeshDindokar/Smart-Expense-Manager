package com.prathmesh.expense.service;

import com.prathmesh.expense.dto.DashboardResponse;
import com.prathmesh.expense.entity.User;
import com.prathmesh.expense.repository.TransactionRepository;
import com.prathmesh.expense.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public DashboardService(
            TransactionRepository transactionRepository,
            UserRepository userRepository) {

        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public DashboardResponse getDashboard(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        BigDecimal totalIncome =
                transactionRepository.getTotalIncome(user.getId());

        BigDecimal totalExpense =
                transactionRepository.getTotalExpense(user.getId());

        BigDecimal balance =
                totalIncome.subtract(totalExpense);

        return new DashboardResponse(
                totalIncome,
                totalExpense,
                balance
        );
    }
}