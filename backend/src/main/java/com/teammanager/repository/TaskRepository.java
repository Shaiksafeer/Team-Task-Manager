package com.teammanager.repository;

import com.teammanager.entity.Project;
import com.teammanager.entity.Task;
import com.teammanager.entity.User;
import com.teammanager.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    List<Task> findByAssignedTo(User user);
    List<Task> findByProjectAndStatus(Project project, TaskStatus status);
    List<Task> findByDueDateBeforeAndStatusNot(java.time.LocalDateTime date, TaskStatus status);
    
    long countByStatus(TaskStatus status);
    long countByStatusIn(java.util.Collection<TaskStatus> statuses);
    long countByDueDateBeforeAndStatusNot(java.time.LocalDateTime date, TaskStatus status);
    
    long countByAssignedTo(User user);
    long countByAssignedToAndStatus(User user, TaskStatus status);
    long countByAssignedToAndStatusIn(User user, java.util.Collection<TaskStatus> statuses);
    long countByAssignedToAndDueDateBeforeAndStatusNot(User user, java.time.LocalDateTime date, TaskStatus status);
}
