// C:\Users\andre\springboot\mgdSpringReact\src\main\java\com\example\mgdSpringReact\web\dto\CreateTaskRequest.java
package com.example.mgdSpringReact.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public class CreateTaskRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    private String description; // optional

    private OffsetDateTime dueDate; // optional

    @jakarta.validation.constraints.NotNull
    private Long recipientId; // required: selected from dropdown

    private Long projectId; // optional

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

    public OffsetDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(OffsetDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Long getRecipientId() { return recipientId; }
    public void setRecipientId(Long recipientId) { this.recipientId = recipientId; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
}