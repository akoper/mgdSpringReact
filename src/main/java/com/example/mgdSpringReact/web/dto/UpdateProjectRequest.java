package com.example.mgdSpringReact.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateProjectRequest {
    private String name;
    private String description;
    private Set<Long> userIds;
    private Set<Long> organizationIds;
}
