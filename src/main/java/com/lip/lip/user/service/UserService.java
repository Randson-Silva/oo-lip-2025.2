package com.lip.lip.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lip.lip.user.dto.request.PatchUserEmailDto;
import com.lip.lip.user.dto.request.PatchUserNameDto;
import com.lip.lip.user.dto.request.PatchUserPasswordDto;
import com.lip.lip.user.dto.request.UserRegisterDto;
import com.lip.lip.user.dto.response.UserResponseDto;
import com.lip.lip.user.entity.User;
import com.lip.lip.user.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto createUser(UserRegisterDto userRegisterDto){
        if(userRepository.findByEmail(userRegisterDto.email()).isPresent()){
            throw new RuntimeException("Email already registered");
        }
        User newUser = new User(userRegisterDto);
        userRepository.save(newUser);
        return new UserResponseDto(newUser);
    }

    public UserResponseDto getByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponseDto(user);
    }

    public UserResponseDto getByName(String name){
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateName(String name, PatchUserNameDto newName){
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("User name not found!"));
        
        user.setName(newName.name());
        userRepository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateEmail(String email, PatchUserEmailDto newEmail){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User email not found!"));

        if(userRepository.findByEmail(newEmail.email()).isPresent()){
            throw new RuntimeException("User email already exists!");
        }

        user.setEmail(newEmail.email());
        userRepository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updatePassword(String email, PatchUserPasswordDto newPassword){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User email not found!"));
        user.setPassword(newPassword.password());
        userRepository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User email not found!"));
        
        userRepository.delete(user);
    }
}