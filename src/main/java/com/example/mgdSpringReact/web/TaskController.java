package com.example.mgdSpringReact.web;

import com.example.mgdSpringReact.model.Task;
import com.example.mgdSpringReact.repository.TaskRepository;
import com.example.mgdSpringReact.web.dto.CreateTaskRequest;
import com.example.mgdSpringReact.web.dto.UpdateTaskRequest;
import com.example.mgdSpringReact.web.dto.UpdateTaskStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public List<Task> list(
            @RequestParam(name = "status", required = false) Task.Status status,
            @RequestParam(name = "dueBefore", required = false) OffsetDateTime dueBefore
    ) {
        if (status != null) {
            return taskRepository.findByStatus(status);
        }
        if (dueBefore != null) {
            return taskRepository.findByDueDateBefore(dueBefore);
        }
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> get(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());

        Task saved = taskRepository.save(task);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest request) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(request.getTitle());
                    task.setDescription(request.getDescription());
                    task.setStatus(request.getStatus());
                    task.setDueDate(request.getDueDate());
                    Task saved = taskRepository.save(task);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
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
