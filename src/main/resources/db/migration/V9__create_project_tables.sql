-- Create projects table and relations
CREATE TABLE IF NOT EXISTS project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE UNIQUE INDEX IF NOT EXISTS uk_project_name ON project(name);

-- Join table for users <-> projects (many-to-many)
CREATE TABLE IF NOT EXISTS user_projects (
    user_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, project_id),
    CONSTRAINT fk_user_projects_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_projects_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Join table for organizations <-> projects (many-to-many)
CREATE TABLE IF NOT EXISTS organization_projects (
    organization_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    PRIMARY KEY (organization_id, project_id),
    CONSTRAINT fk_org_projects_org FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE CASCADE,
    CONSTRAINT fk_org_projects_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Add optional project_id to task to associate tasks with a project or leave standalone
ALTER TABLE task
    ADD COLUMN IF NOT EXISTS project_id BIGINT NULL;

ALTER TABLE task
    ADD CONSTRAINT fk_task_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_task_project ON task(project_id);
