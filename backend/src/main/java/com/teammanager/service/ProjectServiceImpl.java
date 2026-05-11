package com.teammanager.service;

import com.teammanager.dto.ProjectRequest;
import com.teammanager.dto.ProjectResponse;
import com.teammanager.dto.UserResponse;
import com.teammanager.entity.Project;
import com.teammanager.entity.ProjectMember;
import com.teammanager.entity.User;
import com.teammanager.enums.Role;
import com.teammanager.repository.ProjectMemberRepository;
import com.teammanager.repository.ProjectRepository;
import com.teammanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectRequest request, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(user)
                .build();

        Project savedProject = projectRepository.save(project);
        
        // Creator is also a member
        ProjectMember member = ProjectMember.builder()
                .project(savedProject)
                .user(user)
                .build();
        projectMemberRepository.save(member);

        return mapToResponse(savedProject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ADMIN) {
            return projectRepository.findAll().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        } else {
            return projectMemberRepository.findByUser(user).stream()
                    .map(pm -> mapToResponse(pm.getProject()))
                    .collect(Collectors.toList());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id, String currentUserEmail) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.ADMIN && !projectMemberRepository.existsByProjectAndUser(project, user)) {
            throw new RuntimeException("Access denied to this project");
        }

        return mapToResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        
        return mapToResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addMemberToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (projectMemberRepository.existsByProjectAndUser(project, user)) {
            throw new RuntimeException("User is already a member of this project");
        }

        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(user)
                .build();
        projectMemberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getProjectMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return projectMemberRepository.findByProject(project).stream()
                .map(pm -> mapToUserResponse(pm.getUser()))
                .collect(Collectors.toList());
    }

    private ProjectResponse mapToResponse(Project project) {
        List<UserResponse> members = projectMemberRepository.findByProject(project).stream()
                .map(pm -> mapToUserResponse(pm.getUser()))
                .collect(Collectors.toList());

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdByEmail(project.getCreatedBy().getEmail())
                .members(members)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
