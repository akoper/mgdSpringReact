package com.example.mgdSpringReact.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "uk_users_username", columnList = "username", unique = true)
})
@Getter
@Setter
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String username;

    @Column(nullable = false, length = 200)
    private String password; // BCrypt hash

    @Column(nullable = false, length = 100)
    private String roles = "USER"; // comma-separated roles

    @ManyToMany
    @JoinTable(name = "organization_members",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private Set<Organization> organizations = new HashSet<>();
}
