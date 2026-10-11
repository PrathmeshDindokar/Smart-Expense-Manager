package com.prathmesh.expense.controller;

import com.prathmesh.expense.dto.AIInsightResponse;
import com.prathmesh.expense.service.AIInsightService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/ai")
@Tag(
        name = "AI Insights APIs",
        description = "AI-powered financial analysis and spending insights"
)
public class AIController {

    private final AIInsightService aiInsightService;

    public AIController(AIInsightService aiInsightService) {
        this.aiInsightService = aiInsightService;
    }

    @GetMapping("/insight")
    public AIInsightResponse getInsight(
            Authentication authentication) {

        String insight =
                aiInsightService.generateUserInsight(
                        authentication
                );

        return new AIInsightResponse(insight);
    }
}