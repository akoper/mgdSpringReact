-- Seed 10 users including username 'akoper' with bcrypt-hashed password for 'password'
-- Use upsert so the migration is idempotent and updates existing rows if rerun
-- BCrypt hash corresponds to raw password: password
-- Source: widely used example hash from Spring Security docs
SET @HASH_PASSWORD := '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5R8v7vC18m0DutZCRa14O6gttxyq2';

INSERT INTO users (username, password, roles) VALUES
  ('akoper', @HASH_PASSWORD, 'USER'),
  ('alice',  @HASH_PASSWORD, 'USER'),
  ('bob',    @HASH_PASSWORD, 'USER'),
  ('carol',  @HASH_PASSWORD, 'USER'),
  ('dave',   @HASH_PASSWORD, 'USER'),
  ('eve',    @HASH_PASSWORD, 'USER'),
  ('frank',  @HASH_PASSWORD, 'USER'),
  ('grace',  @HASH_PASSWORD, 'USER'),
  ('heidi',  @HASH_PASSWORD, 'USER')
ON DUPLICATE KEY UPDATE password = VALUES(password), roles = VALUES(roles);

-- 10th user
INSERT INTO users (username, password, roles) VALUES ('ivan', @HASH_PASSWORD, 'USER')
ON DUPLICATE KEY UPDATE password = VALUES(password), roles = VALUES(roles);
