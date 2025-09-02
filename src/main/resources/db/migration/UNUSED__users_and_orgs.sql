-- Create users table matching UserAccount entity
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    roles VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- Create organization table matching Organization entity
CREATE TABLE IF NOT EXISTS organization (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE,
    created_at DATETIME(6) NOT NULL
) ENGINE=InnoDB;

-- Join table for ManyToMany between users and organization
CREATE TABLE IF NOT EXISTS organization_members (
    user_id BIGINT NOT NULL,
    organization_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, organization_id),
    CONSTRAINT fk_org_members_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_org_members_org FOREIGN KEY (organization_id) REFERENCES organization(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Indexes to support lookups (redundant with PK but useful if PK changes)
CREATE INDEX idx_org_members_user ON organization_members(user_id);
CREATE INDEX idx_org_members_org ON organization_members(organization_id);

-- Add foreign keys to task for creator and recipient
ALTER TABLE task
    ADD COLUMN creator_id BIGINT NOT NULL,
    ADD COLUMN recipient_id BIGINT NOT NULL;

-- Indexes for the new FKs
CREATE INDEX idx_task_creator ON task (creator_id);
CREATE INDEX idx_task_recipient ON task (recipient_id);

-- Foreign key constraints
ALTER TABLE task
    ADD CONSTRAINT fk_task_creator FOREIGN KEY (creator_id) REFERENCES users(id),
    ADD CONSTRAINT fk_task_recipient FOREIGN KEY (recipient_id) REFERENCES users(id);
