package com.prathmesh.expense.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GeminiAIService {

    @Value("${ai.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public GeminiAIService() {
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newHttpClient();
    }

    public String generateInsight(String expenseData) {

        try {

            String prompt = """
                    You are a personal finance assistant.

                    Analyze the following user's expense data.

                    Give:
                    1. Spending pattern
                    2. Unnecessary spending
                    3. Budget suggestion
                    4. Saving suggestion

                    Keep the answer simple and practical.

                    Expense data:
                    """ + expenseData;

            String requestBody = objectMapper.createObjectNode()
                    .put("model", "gemini-3.5-flash")
                    .put("input", prompt)
                    .toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://generativelanguage.googleapis.com/v1beta/interactions"
                    ))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() < 200 ||
                    response.statusCode() >= 300) {

                throw new RuntimeException(
                        "Gemini API Error: " + response.body()
                );
            }

            JsonNode root =
                    objectMapper.readTree(response.body());

            JsonNode steps = root.path("steps");

            for (JsonNode step : steps) {

                if ("model_output".equals(
                        step.path("type").asText())) {

                    JsonNode content =
                            step.path("content");

                    for (JsonNode item : content) {

                        if ("text".equals(
                                item.path("type").asText())) {

                            return item.path("text").asText();
                        }
                    }
                }
            }

            throw new RuntimeException(
                    "No AI response received from Gemini"
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to communicate with Gemini AI",
                    e
            );
        }
    }
}