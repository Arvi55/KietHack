package com.emotionai.backend.repository;

import com.emotionai.backend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByUserIdAndPersonaOrderByTimestampAsc(Long userId, String persona);
}
