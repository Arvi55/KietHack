package com.emotionai.backend.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private Long userId;
    private String hobbies;
    private String favoriteActivities;
    private String favoriteMovieGenres;
    private String favoriteMusicType;
    private String favoriteFood;
    private String comfortActivities;
    private String socialPreference;
}
