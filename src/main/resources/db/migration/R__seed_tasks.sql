INSERT INTO task (title, description, status, due_date)
VALUES ('First task', 'Set up project scaffolding', 'PENDING', NOW() + INTERVAL 2 DAY),
('Second task', 'Write initial tests', 'IN_PROGRESS', NOW() + INTERVAL 5 DAY);