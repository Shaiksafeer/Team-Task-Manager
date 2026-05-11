package com.teammanager.service;

import com.teammanager.dto.TaskRequest;
import com.teammanager.dto.TaskResponse;
import com.teammanager.dto.TaskStatusUpdate;
import com.teammanager.dto.UserResponse;
import com.teammanager.entity.Project;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest request, String currentUserEmail) {
        User creator = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = null;
        if (request.getAssignedToId() != null) {
            assignee = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            
            if (!projectMemberRepository.existsByProjectAndUser(project, assignee)) {
                throw new RuntimeException("Task can only be assigned to project members");
            }
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .project(project)
                .createdBy(creator)
                .assignedTo(assignee)
                .build();

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            return taskRepository.findAll().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } else {
            return taskRepository.findByAssignedTo(user).stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id, String currentUserEmail) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.ADMIN &&
            (task.getAssignedTo() == null || !task.getAssignedTo().getEmail().equals(currentUserEmail))) {
            throw new RuntimeException("Access denied to this task");
        }

        return mapToResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (request.getAssignedToId() != null) {
            User assignee = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            
            if (!projectMemberRepository.existsByProjectAndUser(project, assignee)) {
                throw new RuntimeException("Task can only be assigned to project members");
            }
            task.setAssignedTo(assignee);
        } else {
            task.setAssignedTo(null);
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setProject(project);

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public TaskResponse updateTaskStatus(Long id, TaskStatusUpdate statusUpdate, String currentUserEmail) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.ADMIN &&
            (task.getAssignedTo() == null || !task.getAssignedTo().getEmail().equals(currentUserEmail))) {
            throw new RuntimeException("You can only update status of your own tasks");
        }

        task.setStatus(statusUpdate.getStatus());
        return mapToResponse(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getMyTasks(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return taskRepository.findByAssignedTo(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getOverdueTasks() {
        return taskRepository.findByDueDateBeforeAndStatusNot(LocalDateTime.now(), TaskStatus.DONE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TaskResponse mapToResponse(Task task) {
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
