package com.prathmesh.expense.controller;

import com.prathmesh.expense.dto.BudgetRequest;
import com.prathmesh.expense.dto.BudgetResponse;
import com.prathmesh.expense.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/budget")
@Tag(
        name = "Budget APIs",
        description = "Set and view monthly budget information"
)
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public BudgetResponse setBudget(
            @Valid @RequestBody BudgetRequest request,
            Authentication authentication) {

        return budgetService.setBudget(
                request,
                authentication
        );
    }

    @GetMapping
    public BudgetResponse getBudget(
            Authentication authentication) {

        return budgetService.getBudget(
                authentication
        );
    }
}