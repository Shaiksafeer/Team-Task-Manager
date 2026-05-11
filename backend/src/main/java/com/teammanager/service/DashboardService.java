package com.teammanager.service;

import com.teammanager.dto.AdminDashboardResponse;
import com.teammanager.dto.MemberDashboardResponse;
import com.teammanager.dto.TaskResponse;

import java.util.List;

public interface DashboardService {
    AdminDashboardResponse getAdminDashboard();
    MemberDashboardResponse getMemberDashboard(String currentUserEmail);
    List<TaskResponse> getOverdueTasks(String currentUserEmail);
}
