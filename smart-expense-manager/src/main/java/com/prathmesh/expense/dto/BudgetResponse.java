package com.prathmesh.expense.dto;

import java.math.BigDecimal;

public class BudgetResponse {

    private BigDecimal monthlyBudget;
    private BigDecimal totalExpense;
    private BigDecimal remainingBudget;
    private String status;

    public BudgetResponse(
            BigDecimal monthlyBudget,
            BigDecimal totalExpense,
            BigDecimal remainingBudget,
            String status) {

        this.monthlyBudget = monthlyBudget;
        this.totalExpense = totalExpense;
        this.remainingBudget = remainingBudget;
        this.status = status;
    }

    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public BigDecimal getRemainingBudget() {
        return remainingBudget;
    }

    public String getStatus() {
        return status;
    }
}