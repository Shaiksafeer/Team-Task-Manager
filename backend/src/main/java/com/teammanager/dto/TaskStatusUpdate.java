package com.teammanager.dto;

import com.teammanager.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusUpdate {
    @NotNull
    private TaskStatus status;
}
