package com.prathmesh.expense.dto;

import java.math.BigDecimal;

public class MonthlyExpenseResponse {

    private String month;
    private BigDecimal totalExpense;

    public MonthlyExpenseResponse(
            String month,
            BigDecimal totalExpense) {

        this.month = month;
        this.totalExpense = totalExpense;
    }

    public String getMonth() {
        return month;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }
}