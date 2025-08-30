// C:\Users\andre\springboot\mgdSpringReact\src\main\java\com\example\mgdSpringReact\web\dto\UpdateTaskRequest.java
package com.example.mgdSpringReact.web.dto;

import com.example.mgdSpringReact.model.Task.Status;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public class UpdateTaskRequest {
    @Size(max = 255)
    private String title; // optional

    private String description; // optional

    private Status status; // optional

    private OffsetDateTime dueDate; // optional

    // getters & setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }
}
