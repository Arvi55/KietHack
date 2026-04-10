package com.emotionai.backend.controller;

import com.emotionai.backend.dto.ChatRequest;
import com.emotionai.backend.model.ChatMessage;
import com.emotionai.backend.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*") // Allow Vue application connections
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(@RequestBody ChatRequest request) {
        try {
            return ResponseEntity.ok(chatService.sendMessage(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/history/{userId}/{persona}")
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable Long userId, @PathVariable String persona) {
        return ResponseEntity.ok(chatService.getChatHistory(userId, persona));
    }
}
