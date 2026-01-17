package com.lip.lip.user.service;

import com.lip.lip.user.dto.response.UserSettingsDTO;
import com.lip.lip.user.entity.User;
import com.lip.lip.user.entity.UserSettings;
import com.lip.lip.user.repository.UserRepository;
import com.lip.lip.user.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

        private final UserSettingsRepository settingsRepository;
        private final UserRepository userRepository;

        public UserSettingsDTO getSettings(Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                UserSettings settings = settingsRepository.findByUser(user)
                                .orElseGet(() -> createDefaultSettings(user));

                return new UserSettingsDTO(settings);
        }

        @Transactional
        public UserSettingsDTO updateSettings(Long userId, UserSettingsDTO dto) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                UserSettings settings = settingsRepository.findByUser(user)
                                .orElseGet(() -> createDefaultSettings(user));

                settings.setFirstRevisionInterval(dto.getFirstRevisionInterval());
                settings.setSecondRevisionInterval(dto.getSecondRevisionInterval());
                settings.setThirdRevisionInterval(dto.getThirdRevisionInterval());

                settingsRepository.save(settings);

                return new UserSettingsDTO(settings);
        }

        private UserSettings createDefaultSettings(User user) {
                UserSettings settings = UserSettings.builder()
                                .user(user)
                                .firstRevisionInterval(1)
                                .secondRevisionInterval(7)
                                .thirdRevisionInterval(14)
                                .build();

                return settingsRepository.save(settings);
        }
}