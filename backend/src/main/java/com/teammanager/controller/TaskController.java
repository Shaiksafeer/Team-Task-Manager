package com.teammanager.controller;

import com.teammanager.dto.ApiResponse;
import com.teammanager.dto.TaskRequest;
import com.teammanager.dto.TaskResponse;
import com.teammanager.dto.TaskStatusUpdate;
import com.teammanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        TaskResponse response = taskService.createTask(request, authentication.getName());
        return ResponseEntity.ok(ApiResponse.<TaskResponse>builder()
                .success(true)
                .message("Task created successfully")
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(Authentication authentication) {
        List<TaskResponse> response = taskService.getAllTasks(authentication.getName());
        return ResponseEntity.ok(ApiResponse.<List<TaskResponse>>builder()
                .success(true)
                .message("Tasks fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @PathVariable Long id,
            Authentication authentication) {
        TaskResponse response = taskService.getTaskById(id, authentication.getName());
        return ResponseEntity.ok(ApiResponse.<TaskResponse>builder()
                .success(true)
                .message("Task fetched successfully")
                .data(response)
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        TaskResponse response = taskService.updateTask(id, request);
        return ResponseEntity.ok(ApiResponse.<TaskResponse>builder()
                .success(true)
                .message("Task updated successfully")
                .data(response)
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Task deleted successfully")
                .build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody TaskStatusUpdate statusUpdate,
            Authentication authentication) {
        TaskResponse response = taskService.updateTaskStatus(id, statusUpdate, authentication.getName());
        return ResponseEntity.ok(ApiResponse.<TaskResponse>builder()
                .success(true)
                .message("Task status updated successfully")
                .data(response)
                .build());
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getMyTasks(Authentication authentication) {
        List<TaskResponse> response = taskService.getMyTasks(authentication.getName());
        return ResponseEntity.ok(ApiResponse.<List<TaskResponse>>builder()
                .success(true)
                .message("Your tasks fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getOverdueTasks() {
        List<TaskResponse> response = taskService.getOverdueTasks();
        return ResponseEntity.ok(ApiResponse.<List<TaskResponse>>builder()
                .success(true)
                .message("Overdue tasks fetched successfully")
                .data(response)
                .build());
    }
}
