package com.emotionai.backend.service;

import com.emotionai.backend.dto.QuizSubmitRequest;
import com.emotionai.backend.model.QuizResult;
import com.emotionai.backend.repository.QuizResultRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuizService {

    private final QuizResultRepository quizResultRepository;

    public QuizService(QuizResultRepository quizResultRepository) {
        this.quizResultRepository = quizResultRepository;
    }

    public QuizResult processQuiz(QuizSubmitRequest request) {
        String type = "";
        
        type += (request.getExtrovertScore() > request.getIntrovertScore()) ? "E" : "I";
        type += (request.getSensingScore() > request.getIntuitionScore()) ? "S" : "N";
        type += (request.getThinkingScore() > request.getFeelingScore()) ? "T" : "F";

        Optional<QuizResult> existing = quizResultRepository.findByUserId(request.getUserId());
        QuizResult result = existing.orElse(new QuizResult());
        
        result.setUserId(request.getUserId());
        result.setPersonalityType(type);

        return quizResultRepository.save(result);
    }
    
    public QuizResult getResultByUserId(Long userId) {
        return quizResultRepository.findByUserId(userId).orElse(null);
    }
}
