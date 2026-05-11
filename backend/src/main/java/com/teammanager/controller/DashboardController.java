package com.teammanager.controller;

import com.teammanager.dto.AdminDashboardResponse;
import com.teammanager.dto.ApiResponse;
import com.teammanager.dto.MemberDashboardResponse;
import com.teammanager.dto.TaskResponse;
import com.teammanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getAdminDashboard() {
        AdminDashboardResponse response = dashboardService.getAdminDashboard();
        return ResponseEntity.ok(ApiResponse.<AdminDashboardResponse>builder()
                .success(true)
                .message("Admin dashboard data fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/member")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ApiResponse<MemberDashboardResponse>> getMemberDashboard(Authentication authentication) {
        MemberDashboardResponse response = dashboardService.getMemberDashboard(authentication.getName());
        return ResponseEntity.ok(ApiResponse.<MemberDashboardResponse>builder()
                .success(true)
                .message("Member dashboard data fetched successfully")
                .data(response)
                .build());
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getOverdueTasks(Authentication authentication) {
        List<TaskResponse> response = dashboardService.getOverdueTasks(authentication.getName());
        return ResponseEntity.ok(ApiResponse.<List<TaskResponse>>builder()
                .success(true)
                .message("Overdue tasks fetched successfully")
                .data(response)
                .build());
    }
}
