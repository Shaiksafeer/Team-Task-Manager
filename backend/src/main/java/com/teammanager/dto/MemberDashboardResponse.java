package com.teammanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDashboardResponse {
    private long assignedTasks;
    private long completedTasks;
    private long pendingTasks;
    private long overdueTasks;
    private long assignedProjects;
}
