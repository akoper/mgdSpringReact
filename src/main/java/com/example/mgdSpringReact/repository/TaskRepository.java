package com.example.mgdSpringReact.repository;

import com.example.mgdSpringReact.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitle(String title);
    List<Task> findByStatus(Task.Status status);
    List<Task> findByDueDateBefore(OffsetDateTime date);

    // Restricting queries by recipient id for per-user visibility
    List<Task> findByRecipientId(Long recipientId);
    List<Task> findByRecipientIdAndStatus(Long recipientId, Task.Status status);
    List<Task> findByRecipientIdAndDueDateBefore(Long recipientId, OffsetDateTime date);
    Optional<Task> findByIdAndRecipientId(Long id, Long recipientId);
}
