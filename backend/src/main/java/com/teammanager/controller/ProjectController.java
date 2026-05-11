package com.teammanager.controller;

import com.teammanager.dto.ApiResponse;
import com.teammanager.dto.ProjectRequest;
import com.teammanager.dto.ProjectResponse;
import com.teammanager.dto.UserResponse;
import com.teammanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @Valid @RequestBody ProjectRequest request,
            Authentication authentication) {
        ProjectResponse response = projectService.createProject(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.<ProjectResponse>builder()
                .success(true)
                .message("Project created successfully")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects(Authentication authentication) {
        List<ProjectResponse> response = projectService.getAllProjects(authentication.getName());
        return ResponseEntity.ok(ApiResponse.<List<ProjectResponse>>builder()
                .success(true)
                .message("Projects fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(
            @PathVariable Long id,
            Authentication authentication) {
        ProjectResponse response = projectService.getProjectById(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.<ProjectResponse>builder()
                .success(true)
                .message("Project fetched successfully")
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {
        ProjectResponse response = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.<ProjectResponse>builder()
                .success(true)
                .message("Project updated successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Project deleted successfully")
                .build());
    }

    @PostMapping("/{projectId}/members/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> addMemberToProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        projectService.addMemberToProject(projectId, userId);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Member added to project successfully")
                .build());
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getProjectMembers(@PathVariable Long projectId) {
        List<UserResponse> response = projectService.getProjectMembers(projectId);
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .message("Project members fetched successfully")
                .data(response)
                .build());
    }
}
