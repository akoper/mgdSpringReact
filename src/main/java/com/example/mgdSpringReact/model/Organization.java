package com.example.mgdSpringReact.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organization", indexes = {
        @Index(name = "uk_organization_name", columnList = "name", unique = true)
})
@Getter
@Setter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200, unique = true)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(mappedBy = "organizations")
    private Set<UserAccount> members = new HashSet<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
    }
}
