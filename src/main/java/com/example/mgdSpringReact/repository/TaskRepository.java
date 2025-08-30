package com.example.mgdSpringReact.repository;

import com.example.mgdSpringReact.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitle(String title);
    List<Task> findByStatus(Task.Status status);
    List<Task> findByDueDateBefore(OffsetDateTime date);
}
