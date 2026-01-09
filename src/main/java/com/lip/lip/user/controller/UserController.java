package com.lip.lip.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lip.lip.user.dto.request.PatchUserEmailDto;
import com.lip.lip.user.dto.request.PatchUserNameDto;
import com.lip.lip.user.dto.request.PatchUserPasswordDto;
import com.lip.lip.user.dto.request.UserRegisterDto;
import com.lip.lip.user.dto.response.UserResponseDto;
import com.lip.lip.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
@Tag(name="User Controller", description="Endpoints for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(description="Register a new User into system")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid registration data (e.g., email already in use)")   
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRegisterDto newUser) {
        UserResponseDto userResponseDto = userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @Operation(description="Get users by email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the user's data"),
        @ApiResponse(responseCode = "404", description = "User not found with the specified email"),
        @ApiResponse(responseCode = "400", description = "Invalid request body format")
    })
    @GetMapping("email/{email}")
    public ResponseEntity<UserResponseDto> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @Operation(description="Get users by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the user's data"),
        @ApiResponse(responseCode = "404", description = "User not found with the specified name"),
        @ApiResponse(responseCode = "400", description = "Invalid request body format")
    })
    @GetMapping("name/{name}")
    public ResponseEntity<UserResponseDto> getByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.getByName(name));
    }

    @Operation(description = "Update a user's name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User's name updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found with the specified name"),
        @ApiResponse(responseCode = "400", description = "Invalid data provided for the new name")
    })
    @PatchMapping("/name/{name}")
    public ResponseEntity<UserResponseDto> updateName(
            @PathVariable String name, 
            @RequestBody @Valid PatchUserNameDto dto) {
        
        return ResponseEntity.ok(userService.updateName(name, dto));
    }

    @Operation(description = "Update a user's email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User's email updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found with the specified email"),
        @ApiResponse(responseCode = "400", description = "Invalid data provided for the new email")
    })
    @PatchMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> updateEmail(
            @PathVariable String email, 
            @RequestBody @Valid PatchUserEmailDto dto) {
        
        return ResponseEntity.ok(userService.updateEmail(email, dto));
    }

    @Operation(summary = "Update password", description = "Update user's password identified by email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/password/{email}")
    public ResponseEntity<UserResponseDto> updateEmail(
            @PathVariable String email, 
            @RequestBody @Valid PatchUserPasswordDto password) {
        
        return ResponseEntity.ok(userService.updatePassword(email, password));
    }

    @Operation(summary = "Delete a user", description = "Delete a user identified by email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found with the specified email")
    })
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}