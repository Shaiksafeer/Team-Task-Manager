package com.teammanager.service;

import com.teammanager.dto.ProjectRequest;
import com.teammanager.dto.ProjectResponse;
import com.teammanager.dto.UserResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse createProject(ProjectRequest request, String currentUserEmail);
    List<ProjectResponse> getAllProjects(String currentUserEmail);
    ProjectResponse getProjectById(Long id, String currentUserEmail);
    ProjectResponse updateProject(Long id, ProjectRequest request);
    void deleteProject(Long id);
    void addMemberToProject(Long projectId, Long userId);
    List<UserResponse> getProjectMembers(Long projectId);
}
