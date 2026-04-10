package com.emotionai.backend.controller;

import com.emotionai.backend.dto.ProfileRequest;
import com.emotionai.backend.model.Profile;
import com.emotionai.backend.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/save")
    public ResponseEntity<Profile> saveProfile(@RequestBody ProfileRequest request) {
        try {
            return ResponseEntity.ok(profileService.saveProfile(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long userId) {
        Profile profile = profileService.getProfileByUserId(userId);
        if (profile != null) {
            return ResponseEntity.ok(profile);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/set-role")
    public ResponseEntity<Profile> setRole(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            String role = payload.get("role").toString();
            Profile profile = profileService.getProfileByUserId(userId);
            if (profile != null) {
                profile.setAiBuddyRole(role);
                return ResponseEntity.ok(profileService.saveProfileDirect(profile));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
