package com.teammanager.service;

import com.teammanager.dto.TaskRequest;
import com.teammanager.dto.TaskResponse;
import com.teammanager.dto.TaskStatusUpdate;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request, String currentUserEmail);
    List<TaskResponse> getAllTasks(String currentUserEmail);
    TaskResponse getTaskById(Long id, String currentUserEmail);
    TaskResponse updateTask(Long id, TaskRequest request);
    void deleteTask(Long id);
    TaskResponse updateTaskStatus(Long id, TaskStatusUpdate statusUpdate, String currentUserEmail);
    List<TaskResponse> getMyTasks(String currentUserEmail);
    List<TaskResponse> getOverdueTasks();
}
