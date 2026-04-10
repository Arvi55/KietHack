package com.emotionai.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "profiles")
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    private String hobbies;
    private String favoriteActivities;
    private String favoriteMovieGenres;
    private String favoriteMusicType;
    private String favoriteFood;
    private String comfortActivities;
    private String socialPreference;
    
    private String aiBuddyRole;
}
