package com.emotionai.backend.controller;

import com.emotionai.backend.dto.QuizSubmitRequest;
import com.emotionai.backend.model.QuizResult;
import com.emotionai.backend.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/quiz")
public class QuizController {
    
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/submit")
    public ResponseEntity<QuizResult> submitQuiz(@RequestBody QuizSubmitRequest request) {
        try {
            return ResponseEntity.ok(quizService.processQuiz(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<QuizResult> getQuizResult(@PathVariable Long userId) {
        QuizResult result = quizService.getResultByUserId(userId);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.notFound().build();
    }
}
