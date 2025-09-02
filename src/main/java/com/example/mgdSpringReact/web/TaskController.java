package com.example.mgdSpringReact.web;

import com.example.mgdSpringReact.model.Task;
import com.example.mgdSpringReact.model.UserAccount;
import com.example.mgdSpringReact.repository.TaskRepository;
import com.example.mgdSpringReact.repository.UserAccountRepository;
import com.example.mgdSpringReact.web.dto.CreateTaskRequest;
import com.example.mgdSpringReact.web.dto.UpdateTaskRequest;
import com.example.mgdSpringReact.web.dto.UpdateTaskStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final UserAccountRepository users;

    public TaskController(TaskRepository taskRepository, UserAccountRepository users) {
        this.taskRepository = taskRepository;
        this.users = users;
    }

    @GetMapping
    public ResponseEntity<List<Task>> list(
            @RequestParam(name = "status", required = false) Task.Status status,
            @RequestParam(name = "dueBefore", required = false) OffsetDateTime dueBefore,
            Authentication auth
    ) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = auth.getName();
        UserAccount current = users.findByUsername(username).orElse(null);
        if (current == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long uid = current.getId();
        if (status != null) {
            return ResponseEntity.ok(taskRepository.findByRecipientIdAndStatus(uid, status));
        }
        if (dueBefore != null) {
            return ResponseEntity.ok(taskRepository.findByRecipientIdAndDueDateBefore(uid, dueBefore));
        }
        return ResponseEntity.ok(taskRepository.findByRecipientId(uid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> get(@PathVariable Long id, Authentication auth) {
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = auth.getName();
        UserAccount current = users.findByUsername(username).orElse(null);
        if (current == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return taskRepository.findByIdAndRecipientId(id, current.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody CreateTaskRequest request, Authentication auth) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        // Set creator from authenticated user
        String username = auth.getName();
        UserAccount creator = users.findByUsername(username).orElse(null);
        if (creator == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        task.setCreator(creator);

        // Set recipient from request
        if (request.getRecipientId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        UserAccount recipient = users.findById(request.getRecipientId()).orElse(null);
        if (recipient == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        task.setRecipient(recipient);

        Task saved = taskRepository.save(task);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody String body) {
        return taskRepository.findById(id)
                .map(task -> {
                    try {
                        String trimmed = body == null ? "" : body.trim();
                        // If the body looks like a JSON object, treat it as full update
                        if (trimmed.startsWith("{")) {
                            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper().findAndRegisterModules();
                            UpdateTaskRequest request = mapper.readValue(trimmed, UpdateTaskRequest.class);
                            // Apply full update (fields are optional; only set when non-null)
                            if (request.getTitle() != null) task.setTitle(request.getTitle());
                            if (request.getDescription() != null) task.setDescription(request.getDescription());
                            if (request.getStatus() != null) task.setStatus(request.getStatus());
                            if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
                        } else {
                            // Otherwise, treat it as a JSON string representing the status, possibly quoted
                            // Remove surrounding quotes if present
                            if ((trimmed.startsWith("\"") && trimmed.endsWith("\"")) || (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
                                trimmed = trimmed.substring(1, trimmed.length() - 1);
                            }
                            if (trimmed.isEmpty()) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                            }
                            // Map to enum (case-sensitive to match enum names)
                            try {
                                Task.Status newStatus = Task.Status.valueOf(trimmed);
                                task.setStatus(newStatus);
                            } catch (IllegalArgumentException ex) {
                                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                            }
                        }
                        Task saved = taskRepository.save(task);
                        return ResponseEntity.ok(saved);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                })
                .orElseGet(() -> ResponseEntity.<Task>status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable long id, @Valid @RequestBody UpdateTaskStatusRequest request) {
        return taskRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(request.getStatus());
                    Task saved = taskRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        }
    }
}
