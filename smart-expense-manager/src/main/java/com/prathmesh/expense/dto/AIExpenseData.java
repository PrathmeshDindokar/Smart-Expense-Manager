package com.prathmesh.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AIExpenseData {

    private String category;
    private BigDecimal amount;
    private LocalDate date;
    private String description;

    public AIExpenseData(
            String category,
            BigDecimal amount,
            LocalDate date,
            String description) {

        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}