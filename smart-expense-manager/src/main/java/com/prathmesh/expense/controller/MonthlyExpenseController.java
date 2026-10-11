package com.prathmesh.expense.controller;

import com.prathmesh.expense.dto.MonthlyExpenseResponse;
import com.prathmesh.expense.service.MonthlyExpenseService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monthly-expense")
public class MonthlyExpenseController {

    private final MonthlyExpenseService monthlyExpenseService;

    public MonthlyExpenseController(
            MonthlyExpenseService monthlyExpenseService) {
        this.monthlyExpenseService = monthlyExpenseService;
    }

    @GetMapping
    public MonthlyExpenseResponse getCurrentMonthExpense(
            Authentication authentication) {

        return monthlyExpenseService
                .getCurrentMonthExpense(authentication);
    }
}