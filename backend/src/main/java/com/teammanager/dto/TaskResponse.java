package com.teammanager.dto;

import com.teammanager.enums.TaskPriority;
import com.teammanager.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private UserResponse assignedTo;
    private Long projectId;
    private String projectName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
