package com.lip.lip.user.controller;

import com.lip.lip.user.dto.response.UserSettingsDTO;
import com.lip.lip.user.entity.User;
import com.lip.lip.user.service.UserSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/settings")
@RequiredArgsConstructor
@Tag(name = "User Settings", description = "Endpoints for user settings")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @Operation(description = "Get user settings")
    @GetMapping
    public ResponseEntity<UserSettingsDTO> getSettings(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userSettingsService.getSettings(user.getId()));
    }

    @Operation(description = "Update user settings")
    @PutMapping
    public ResponseEntity<UserSettingsDTO> updateSettings(
            @RequestBody UserSettingsDTO dto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userSettingsService.updateSettings(user.getId(), dto));
    }
}