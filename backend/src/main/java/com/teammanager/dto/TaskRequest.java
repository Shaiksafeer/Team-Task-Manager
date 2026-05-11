package com.teammanager.dto;

import com.teammanager.enums.TaskPriority;
import com.teammanager.enums.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequest {
    @NotBlank
    @Size(max = 150)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    private TaskStatus status;

    @NotNull
    private TaskPriority priority;

    @NotNull
    @Future(message = "Due date must be in the future")
    private LocalDateTime dueDate;

    private Long assignedToId;

    @NotNull
    private Long projectId;
}
