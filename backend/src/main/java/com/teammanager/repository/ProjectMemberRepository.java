package com.teammanager.repository;

import com.teammanager.entity.Project;
import com.teammanager.entity.ProjectMember;
import com.teammanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProject(Project project);
    List<ProjectMember> findByUser(User user);
    Optional<ProjectMember> findByProjectAndUser(Project project, User user);
    Boolean existsByProjectAndUser(Project project, User user);
    long countByUser(User user);
}
