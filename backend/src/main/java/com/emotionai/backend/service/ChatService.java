package com.emotionai.backend.service;

import com.emotionai.backend.dto.ChatRequest;
import com.emotionai.backend.model.ChatMessage;
import com.emotionai.backend.model.Profile;
import com.emotionai.backend.model.QuizResult;
import com.emotionai.backend.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final HuggingFaceAiService huggingFaceAiService;
    private final ProfileService profileService;
    private final QuizService quizService;

    public ChatService(ChatMessageRepository chatMessageRepository, 
                       HuggingFaceAiService huggingFaceAiService,
                       ProfileService profileService,
                       QuizService quizService) {
        this.chatMessageRepository = chatMessageRepository;
        this.huggingFaceAiService = huggingFaceAiService;
        this.profileService = profileService;
        this.quizService = quizService;
    }

    public ChatMessage sendMessage(ChatRequest request) {
        Long userId = request.getUserId();
        Profile profile = profileService.getProfileByUserId(userId);
        QuizResult quiz = quizService.getResultByUserId(userId);
        
        List<ChatMessage> history = chatMessageRepository.findByUserIdOrderByTimestampAsc(userId);
        
        String context = buildPromptContext(profile, quiz, history, request.getMessage());
        
        String aiResponse = huggingFaceAiService.generateResponse(context);
        
        ChatMessage message = new ChatMessage();
        message.setUserId(userId);
        message.setMessage(request.getMessage());
        message.setResponse(aiResponse);
        message.setTimestamp(LocalDateTime.now());
        
        return chatMessageRepository.save(message);
    }
    
    public List<ChatMessage> getChatHistory(Long userId) {
        return chatMessageRepository.findByUserIdOrderByTimestampAsc(userId);
    }

    private String buildPromptContext(Profile profile, QuizResult quiz, List<ChatMessage> history, String currentMessage) {
        StringBuilder sb = new StringBuilder();
        
        String role = profile != null && profile.getAiBuddyRole() != null ? profile.getAiBuddyRole() : "Friend";
        String personality = quiz != null && quiz.getPersonalityType() != null ? quiz.getPersonalityType() : "Unknown";
        String hobbies = profile != null && profile.getHobbies() != null ? profile.getHobbies() : "None";
        String comfort = profile != null && profile.getComfortActivities() != null ? profile.getComfortActivities() : "None";

        sb.append("You are acting as a caring ").append(role).append(".\n");
        sb.append("The user has personality type: ").append(personality).append(".\n");
        sb.append("The user likes: ").append(hobbies).append(".\n");
        sb.append("The user feels comfort by: ").append(comfort).append(".\n");
        sb.append("Respond in a caring, emotionally supportive, and human-like way. Keep it brief and avoid robotic responses.\n\n");
        
        sb.append("Previous conversation:\n");
        int start = Math.max(0, history.size() - 5);
        for (int i = start; i < history.size(); i++) {
            sb.append("User: ").append(history.get(i).getMessage()).append("\n");
            sb.append(role).append(": ").append(history.get(i).getResponse()).append("\n");
        }
        
        sb.append("User: ").append(currentMessage).append("\n");
        sb.append(role).append(":");
        
        return sb.toString();
    }
}
