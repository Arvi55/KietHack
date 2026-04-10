package com.emotionai.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class HuggingFaceAiService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    private final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateResponse(String prompt) {

        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_TOKEN_HERE")) {
            return getSimulatedResponse(prompt);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("HTTP-Referer", "http://localhost:3000");
        headers.set("X-Title", "EmotionAI");

        Map<String, Object> body = new HashMap<>();
        body.put("model", "nvidia/nemotron-3-super-120b-a12b:free");

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> system = new HashMap<>();
        system.put("role", "system");
        system.put("content", "You are Girlfriend Buddy, a caring, emotional, supportive AI. Speak warmly, kindly, and naturally.");

        Map<String, String> user = new HashMap<>();
        user.put("role", "user");
        user.put("content", prompt);

        messages.add(system);
        messages.add(user);

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getBody() != null) {
                List choices = (List) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map first = (Map) choices.get(0);
                    Map message = (Map) first.get("message");
                    return message.get("content").toString().trim();
                }
            }

            return "I'm here for you 💙 Tell me what's going on.";

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }

    private String getSimulatedResponse(String prompt) {
        if (prompt.toLowerCase().contains("sad")) {
            return "Hey… I’m really sorry you're feeling this way 🥺 I'm here with you.";
        }
        return "Tell me more… I’m listening 💙";
    }
}