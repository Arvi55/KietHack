package com.emotionai.backend.service;

import com.emotionai.backend.dto.ProfileRequest;
import com.emotionai.backend.model.Profile;
import com.emotionai.backend.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile saveProfile(ProfileRequest request) {
        Optional<Profile> existing = profileRepository.findByUserId(request.getUserId());
        Profile profile = existing.orElse(new Profile());

        profile.setUserId(request.getUserId());
        profile.setHobbies(request.getHobbies());
        profile.setFavoriteActivities(request.getFavoriteActivities());
        profile.setFavoriteMovieGenres(request.getFavoriteMovieGenres());
        profile.setFavoriteMusicType(request.getFavoriteMusicType());
        profile.setFavoriteFood(request.getFavoriteFood());
        profile.setComfortActivities(request.getComfortActivities());
        profile.setSocialPreference(request.getSocialPreference());

        return profileRepository.save(profile);
    }
    
    public Profile saveProfileDirect(Profile profile) {
        return profileRepository.save(profile);
    }
    
    public Profile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId).orElse(null);
    }
}
