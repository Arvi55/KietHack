package com.emotionai.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class HuggingFaceAiService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    private final String API_URL = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-v0.1";
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateResponse(String prompt) {
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_TOKEN_HERE")) {
            // Mock response if no token is provided for testing
            return getSimulatedResponse(prompt);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> body = new HashMap<>();
        body.put("inputs", prompt);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("max_new_tokens", 80);
        parameters.put("temperature", 0.7);
        parameters.put("return_full_text", false);
        body.put("parameters", parameters);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<List> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, List.class);
            if (response.getBody() != null && !response.getBody().isEmpty()) {
                Map<String, Object> firstChoice = (Map<String, Object>) response.getBody().get(0);
                return firstChoice.get("generated_text").toString().trim();
            }
            return "I'm here for you. Just a bit quiet today.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, my systems are experiencing an error. Please try again later. (" + e.getMessage() + ")";
        }
    }
    
    private String getSimulatedResponse(String prompt) {
        if (prompt.toLowerCase().contains("sad")) {
            return "I'm so sorry you're feeling that way. I'm here for you, just take a deep breath.";
        }
        return "That sounds interesting. Tell me more about what's on your mind.";
    }
}
