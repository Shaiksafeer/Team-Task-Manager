package com.teammanager.repository;

import com.teammanager.entity.Project;
import com.teammanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByCreatedBy(User user);
}
