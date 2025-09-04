package com.example.mgdSpringReact.web;

import com.example.mgdSpringReact.model.Organization;
import com.example.mgdSpringReact.model.Project;
import com.example.mgdSpringReact.model.UserAccount;
import com.example.mgdSpringReact.repository.OrganizationRepository;
import com.example.mgdSpringReact.repository.ProjectRepository;
import com.example.mgdSpringReact.repository.UserAccountRepository;
import com.example.mgdSpringReact.web.dto.CreateProjectRequest;
import com.example.mgdSpringReact.web.dto.UpdateProjectRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository projects;
    private final UserAccountRepository users;
    private final OrganizationRepository orgs;

    public ProjectController(ProjectRepository projects, UserAccountRepository users, OrganizationRepository orgs) {
        this.projects = projects;
        this.users = users;
        this.orgs = orgs;
    }

    @GetMapping
    public ResponseEntity<List<Project>> list() {
        return ResponseEntity.ok(projects.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable Long id) {
        return projects.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Project> create(@Valid @RequestBody CreateProjectRequest req) {
        Project p = new Project();
        p.setName(req.getName());
        p.setDescription(req.getDescription());
        applyAssociations(p, req.getUserIds(), req.getOrganizationIds());
        Project saved = projects.save(p);
        return ResponseEntity.created(URI.create("/api/projects/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable Long id, @RequestBody UpdateProjectRequest req) {
        return projects.findById(id)
                .map(existing -> {
                    if (req.getName() != null) existing.setName(req.getName());
                    if (req.getDescription() != null) existing.setDescription(req.getDescription());
                    if (req.getUserIds() != null || req.getOrganizationIds() != null) {
                        applyAssociations(existing, req.getUserIds(), req.getOrganizationIds());
                    }
                    Project saved = projects.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (projects.existsById(id)) {
            projects.deleteById(id);
        }
    }

    private void applyAssociations(Project p, Set<Long> userIds, Set<Long> orgIds) {
        if (userIds != null) {
            Set<UserAccount> us = new HashSet<>();
            for (Long uid : userIds) {
                users.findById(uid).ifPresent(us::add);
            }
            p.setUsers(us);
        }
        if (orgIds != null) {
            Set<Organization> os = new HashSet<>();
            for (Long oid : orgIds) {
                orgs.findById(oid).ifPresent(os::add);
            }
            p.setOrganizations(os);
        }
    }
}
