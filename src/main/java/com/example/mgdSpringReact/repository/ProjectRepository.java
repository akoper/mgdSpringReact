package com.example.mgdSpringReact.repository;

import com.example.mgdSpringReact.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
