package com.example.mgdSpringReact.web;

import com.example.mgdSpringReact.model.Organization;
import com.example.mgdSpringReact.model.UserAccount;
import com.example.mgdSpringReact.repository.OrganizationRepository;
import com.example.mgdSpringReact.repository.UserAccountRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationRepository organizations;
    private final UserAccountRepository users;

    public OrganizationController(OrganizationRepository organizations, UserAccountRepository users) {
        this.organizations = organizations;
        this.users = users;
    }

    public static class CreateOrgRequest {
        @NotBlank
        public String name;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateOrgRequest request, Authentication auth) {
        String name = request.getName().trim();
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body("name required");
        }
        if (organizations.existsByName(name)) {
            return ResponseEntity.status(409).body("organization already exists");
        }
        Organization org = new Organization();
        org.setName(name);
        Organization saved = organizations.save(org);

        // Automatically join creator
        String username = auth.getName();
        UserAccount user = users.findByUsername(username).orElseThrow();
        user.getOrganizations().add(saved);
        users.save(user);

        return ResponseEntity.created(URI.create("/api/organizations/" + saved.getId())).body(saved);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<?> join(@PathVariable Long id, Authentication auth) {
        Organization org = organizations.findById(id).orElse(null);

        if (org == null) {
            return ResponseEntity.notFound().build();
        }

        String username = auth.getName();
        UserAccount user = users.findByUsername(username).orElseThrow();

        boolean added = user.getOrganizations().add(org);
        if (added) {
            users.save(user);
        }
        return ResponseEntity.ok(Map.of("joined", true, "organizationId", org.getId()));
    }
}
