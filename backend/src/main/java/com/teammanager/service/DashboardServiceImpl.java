package com.teammanager.service;

import com.teammanager.dto.AdminDashboardResponse;
import com.teammanager.dto.MemberDashboardResponse;
import com.teammanager.dto.TaskResponse;
import com.teammanager.dto.UserResponse;
import com.teammanager.entity.Task;
import com.teammanager.entity.User;
import com.teammanager.enums.Role;
import com.teammanager.enums.TaskStatus;
import com.teammanager.repository.ProjectMemberRepository;
import com.teammanager.repository.ProjectRepository;
import com.teammanager.repository.TaskRepository;
import com.teammanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponse getAdminDashboard() {
        List<TaskStatus> pendingStatuses = Arrays.asList(TaskStatus.TODO, TaskStatus.IN_PROGRESS);
        
        return AdminDashboardResponse.builder()
                .totalProjects(projectRepository.count())
                .totalTasks(taskRepository.count())
                .completedTasks(taskRepository.countByStatus(TaskStatus.DONE))
                .pendingTasks(taskRepository.countByStatusIn(pendingStatuses))
                .overdueTasks(taskRepository.countByDueDateBeforeAndStatusNot(LocalDateTime.now(), TaskStatus.DONE))
                .totalMembers(userRepository.count())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDashboardResponse getMemberDashboard(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<TaskStatus> pendingStatuses = Arrays.asList(TaskStatus.TODO, TaskStatus.IN_PROGRESS);

        return MemberDashboardResponse.builder()
                .assignedTasks(taskRepository.countByAssignedTo(user))
                .completedTasks(taskRepository.countByAssignedToAndStatus(user, TaskStatus.DONE))
                .pendingTasks(taskRepository.countByAssignedToAndStatusIn(user, pendingStatuses))
                .overdueTasks(taskRepository.countByAssignedToAndDueDateBeforeAndStatusNot(user, LocalDateTime.now(), TaskStatus.DONE))
                .assignedProjects(projectMemberRepository.countByUser(user))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getOverdueTasks(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> overdueTasks;
        if (user.getRole() == Role.ADMIN) {
            overdueTasks = taskRepository.findByDueDateBeforeAndStatusNot(LocalDateTime.now(), TaskStatus.DONE);
        } else {
            // Filter manually or add repository method if needed for performance
            overdueTasks = taskRepository.findByAssignedTo(user).stream()
                    .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(LocalDateTime.now()) && t.getStatus() != TaskStatus.DONE)
                    .collect(Collectors.toList());
        }

        return overdueTasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    private TaskResponse mapToTaskResponse(Task task) {
        UserResponse assignee = null;
        if (task.getAssignedTo() != null) {
            assignee = UserResponse.builder()
                    .id(task.getAssignedTo().getId())
                    .name(task.getAssignedTo().getName())
                    .email(task.getAssignedTo().getEmail())
                    .role(task.getAssignedTo().getRole().name())
                    .build();
        }

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .assignedTo(assignee)
                .projectId(task.getProject().getId())
                .projectName(task.getProject().getName())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
