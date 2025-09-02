package com.example.mgdSpringReact.repository;

import com.example.mgdSpringReact.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByName(String name);
    Optional<Organization> findByName(String name);
}
