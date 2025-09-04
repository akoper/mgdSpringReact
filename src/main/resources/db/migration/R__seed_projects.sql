-- Seed 15 demo projects and associate them with various users and organizations
-- Uses INSERT IGNORE so it is idempotent across re-runs

-- Base projects
INSERT IGNORE INTO project (name, description)
VALUES
  ('Project Apollo', 'Lunar module redesign and testing'),
  ('Project Borealis', 'Next-gen analytics platform'),
  ('Project Comet', 'Event streaming backbone'),
  ('Project Delta', 'Customer onboarding revamp'),
  ('Project Eclipse', 'Mobile app modernization'),
  ('Project Falcon', 'Realtime notification service'),
  ('Project Gaia', 'Sustainability reporting portal'),
  ('Project Helix', 'Identity and access overhaul'),
  ('Project Ion', 'Edge computing rollout'),
  ('Project Juno', 'Billing and invoicing rewrite'),
  ('Project Kronos', 'Observability stack upgrade'),
  ('Project Lumos', 'Data catalog and lineage'),
  ('Project Nimbus', 'Cloud cost optimization'),
  ('Project Orion', 'Partner integration hub'),
  ('Project Phoenix', 'Legacy system decommissioning');

-- Associate some projects with users (many-to-many)
-- Users expected from R__seed_users.sql: akoper, alice, bob, carol, dave, eve, frank, grace, heidi, ivan
INSERT IGNORE INTO user_projects(user_id, project_id)
SELECT u.id, p.id FROM users u JOIN project p ON p.name IN ('Project Apollo','Project Borealis') WHERE u.username IN ('alice','bob');

INSERT IGNORE INTO user_projects(user_id, project_id)
SELECT u.id, p.id FROM users u JOIN project p ON p.name IN ('Project Comet','Project Delta','Project Eclipse') WHERE u.username IN ('carol','dave');

INSERT IGNORE INTO user_projects(user_id, project_id)
SELECT u.id, p.id FROM users u JOIN project p ON p.name IN ('Project Falcon','Project Gaia') WHERE u.username IN ('eve','frank');

INSERT IGNORE INTO user_projects(user_id, project_id)
SELECT u.id, p.id FROM users u JOIN project p ON p.name IN ('Project Helix','Project Ion','Project Juno') WHERE u.username IN ('grace','heidi');

INSERT IGNORE INTO user_projects(user_id, project_id)
SELECT u.id, p.id FROM users u JOIN project p ON p.name IN ('Project Kronos','Project Lumos','Project Nimbus','Project Orion','Project Phoenix') WHERE u.username IN ('ivan','akoper');

-- Create some orgs->projects links (requires V4/V5 orgs)
-- Orgs expected from V5__seed_organizations.sql
INSERT IGNORE INTO organization_projects(organization_id, project_id)
SELECT o.id, p.id FROM organization o JOIN project p ON p.name IN ('Project Apollo','Project Comet','Project Falcon') WHERE o.name IN ('Acme Corp','Globex Inc');

INSERT IGNORE INTO organization_projects(organization_id, project_id)
SELECT o.id, p.id FROM organization o JOIN project p ON p.name IN ('Project Delta','Project Helix','Project Kronos') WHERE o.name IN ('Initech LLC','Umbrella Group');

INSERT IGNORE INTO organization_projects(organization_id, project_id)
SELECT o.id, p.id FROM organization o JOIN project p ON p.name IN ('Project Borealis','Project Gaia','Project Phoenix') WHERE o.name IN ('Wayne Enterprises');
