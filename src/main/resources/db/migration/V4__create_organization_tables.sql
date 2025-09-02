CREATE TABLE IF NOT EXISTS organization (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS organization_members (
    organization_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (organization_id, user_id),
    CONSTRAINT fk_org_members_org FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE CASCADE,
    CONSTRAINT fk_org_members_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;
