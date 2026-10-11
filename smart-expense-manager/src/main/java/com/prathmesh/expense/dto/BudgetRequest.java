package com.prathmesh.expense.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class BudgetRequest {

    @NotNull(message = "Monthly budget is required")
    @Positive(message = "Monthly budget must be greater than 0")
    private BigDecimal monthlyBudget;

    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(BigDecimal monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }
}