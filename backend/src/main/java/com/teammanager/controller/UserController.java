package com.teammanager.controller;

import com.teammanager.dto.ApiResponse;
import com.teammanager.dto.UserResponse;
import com.teammanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(response)
                .build());
    }
}
