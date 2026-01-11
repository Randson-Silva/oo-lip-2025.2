package com.lip.lip.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip.lip.email.service.EmailService;
import com.lip.lip.user.dto.request.PatchUserEmailDto;
import com.lip.lip.user.dto.request.PatchUserNameDto;
import com.lip.lip.user.dto.request.PatchUserPasswordDto;
import com.lip.lip.user.dto.request.UserRegisterDto;
import com.lip.lip.user.dto.response.UserResponseDto;
import com.lip.lip.user.entity.User;
import com.lip.lip.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public UserResponseDto createUser(UserRegisterDto dto) {

        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User newUser = new User();
        newUser.setName(dto.name());
        newUser.setEmail(dto.email());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setActive(true);
        newUser.setVerified(false);

        String verificationToken = UUID.randomUUID().toString();
        newUser.setVerificationToken(verificationToken);
        newUser.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));

        userRepository.save(newUser);

        emailService.sendVerificationEmail(
                newUser.getEmail(),
                newUser.getName(),
                verificationToken);

        return new UserResponseDto(newUser);
    }

    public UserResponseDto getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponseDto(user);
    }

    public UserResponseDto getByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateName(String name, PatchUserNameDto newName) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User name not found!"));

        user.setName(newName.name());
        userRepository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateEmail(String email, PatchUserEmailDto newEmail) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User email not found!"));

        if (userRepository.findByEmail(newEmail.email()).isPresent()) {
            throw new RuntimeException("User email already exists!");
        }

        user.setEmail(newEmail.email());
        user.setVerified(false); // Requerer nova verificação

        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));

        userRepository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updatePassword(String email, PatchUserPasswordDto newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User email not found!"));

        user.setPassword(passwordEncoder.encode(newPassword.password()));
        userRepository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User email not found!"));

        userRepository.delete(user);
    }
}