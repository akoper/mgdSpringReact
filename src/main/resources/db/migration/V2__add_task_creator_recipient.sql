ALTER TABLE task
    ADD COLUMN IF NOT EXISTS creator_id BIGINT NULL,
    ADD COLUMN IF NOT EXISTS recipient_id BIGINT NULL;

ALTER TABLE task
    ADD CONSTRAINT fk_task_creator FOREIGN KEY (creator_id) REFERENCES users(id),
    ADD CONSTRAINT fk_task_recipient FOREIGN KEY (recipient_id) REFERENCES users(id);

CREATE INDEX IF NOT EXISTS idx_task_creator ON task (creator_id);
CREATE INDEX IF NOT EXISTS idx_task_recipient ON task (recipient_id);
