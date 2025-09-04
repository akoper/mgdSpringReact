package com.example.mgdSpringReact.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CreateProjectRequest {
    @NotBlank
    private String name;
    private String description;

    // Optional initial associations by ids
    private Set<Long> userIds;
    private Set<Long> organizationIds;
}
