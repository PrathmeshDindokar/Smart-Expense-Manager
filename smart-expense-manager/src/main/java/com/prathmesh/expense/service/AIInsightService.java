package com.prathmesh.expense.service;

import com.prathmesh.expense.dto.AIExpenseData;
import com.prathmesh.expense.entity.Transaction;
import com.prathmesh.expense.entity.User;
import com.prathmesh.expense.repository.TransactionRepository;
import com.prathmesh.expense.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIInsightService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final GeminiAIService geminiAIService;

    public AIInsightService(
            TransactionRepository transactionRepository,
            UserRepository userRepository,
            GeminiAIService geminiAIService) {

        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.geminiAIService = geminiAIService;
    }

    public String generateUserInsight(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<Transaction> transactions =
                transactionRepository.findByUserId(user.getId());

        if (transactions.isEmpty()) {
            return "No transactions found. Add some income or expenses to get an AI financial analysis.";
        }

        StringBuilder expenseData = new StringBuilder();

        for (Transaction transaction : transactions) {

            AIExpenseData data = new AIExpenseData(
                    transaction.getCategory().getName(),
                    transaction.getAmount(),
                    transaction.getTransactionDate(),
                    transaction.getDescription()
            );

            expenseData.append(
                    "Category: "
            ).append(data.getCategory());

            expenseData.append(
                    ", Amount: ₹"
            ).append(data.getAmount());

            expenseData.append(
                    ", Date: "
            ).append(data.getDate());

            expenseData.append(
                    ", Description: "
            ).append(data.getDescription());

            expenseData.append("\n");
        }

        return geminiAIService.generateInsight(
                expenseData.toString()
        );
    }
}