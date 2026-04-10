package com.emotionai.backend.dto;

import lombok.Data;

@Data
public class QuizSubmitRequest {
    private Long userId;
    private int extrovertScore;
    private int introvertScore;
    private int thinkingScore;
    private int feelingScore;
    private int sensingScore;
    private int intuitionScore;
}
