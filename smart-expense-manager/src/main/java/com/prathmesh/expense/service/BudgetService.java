package com.prathmesh.expense.service;

import com.prathmesh.expense.dto.BudgetRequest;
import com.prathmesh.expense.dto.BudgetResponse;
import com.prathmesh.expense.entity.Budget;
import com.prathmesh.expense.entity.User;
import com.prathmesh.expense.repository.BudgetRepository;
import com.prathmesh.expense.repository.TransactionRepository;
import com.prathmesh.expense.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public BudgetService(
            BudgetRepository budgetRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository) {

        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public BudgetResponse setBudget(
            BudgetRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();

        Integer month = today.getMonthValue();
        Integer year = today.getYear();

        Budget budget = budgetRepository
                .findByUserIdAndMonthAndYear(
                        user.getId(),
                        month,
                        year
                )
                .orElseGet(Budget::new);

        budget.setUser(user);
        budget.setMonthlyBudget(request.getMonthlyBudget());
        budget.setMonth(month);
        budget.setYear(year);

        budgetRepository.save(budget);

        return getBudget(authentication);
    }

    public BudgetResponse getBudget(
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();

        Integer month = today.getMonthValue();
        Integer year = today.getYear();

        Budget budget = budgetRepository
                .findByUserIdAndMonthAndYear(
                        user.getId(),
                        month,
                        year
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Budget not set for current month"
                        ));

        LocalDate startDate =
                today.withDayOfMonth(1);

        LocalDate endDate =
                today.withDayOfMonth(
                        today.lengthOfMonth()
                );

        BigDecimal totalExpense =
                transactionRepository.getMonthlyExpense(
                        user.getId(),
                        startDate,
                        endDate
                );

        BigDecimal remainingBudget =
                budget.getMonthlyBudget()
                        .subtract(totalExpense);

        String status;

        if (remainingBudget.compareTo(BigDecimal.ZERO) < 0) {
            status = "BUDGET_EXCEEDED";
        } else {
            status = "WITHIN_BUDGET";
        }

        return new BudgetResponse(
                budget.getMonthlyBudget(),
                totalExpense,
                remainingBudget,
                status
        );
    }
}