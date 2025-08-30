// C:\Users\andre\springboot\mgdSpringReact\src\main\java\com\example\mgdSpringReact\web\dto\UpdateTaskStatusRequest.java
package com.example.mgdSpringReact.web.dto;

import com.example.mgdSpringReact.model.Task.Status;
import jakarta.validation.constraints.NotNull;

public class UpdateTaskStatusRequest {
    @NotNull
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}