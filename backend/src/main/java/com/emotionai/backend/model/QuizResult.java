package com.emotionai.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "quiz_results")
@Data
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    private String personalityType; // e.g. "INF", "EST"
}
