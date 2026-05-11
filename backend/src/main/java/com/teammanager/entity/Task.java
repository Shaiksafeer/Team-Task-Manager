package com.teammanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teammanager.enums.TaskPriority;
import com.teammanager.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task extends BaseEntity {

    @NotBlank
    @Size(max = 150)
    private String title;

    @Size(max = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskPriority priority;

    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", nullable = false)
    private User createdBy;
}
