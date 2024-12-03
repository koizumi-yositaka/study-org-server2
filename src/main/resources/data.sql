
INSERT INTO T_AUTHORITY (id,rank, description) VALUES
('A','ADMIN', 'Administrator Level'),
('B', 'POWER_USER','Moderator Level'),
('C', 'NORMAL','User Level');
INSERT INTO T_USER (email, password, authority_id) VALUES
('admin@example.com', '$2a$10$UgyTyEN6XuwXoHM60xGP4eC3tv6yBJzhVnMtDvhjwBy1hbpESQoKS', 'A'), -- Aランク
('moderator@example.com', '$2a$10$yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy', 'B'), -- Bランク
('user@example.com', '$2a$10$zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz', 'C'); -- Cランク
INSERT INTO T_MEETING (title, detail, opener_id, event_date, duration, delete_flg)
VALUES
('Project Kickoff', 'Initial project discussion', 1, '2024-12-01 10:00:00', 90, '0'),
('Weekly Sync', 'Weekly team sync-up', 2, '2024-12-05 14:00:00', 60, '0'),
('Budget Review', 'Review of budget allocations', 3, '2024-12-10 15:30:00', 120, '0'),
('Client Presentation', 'Presentation to client', 1, '2024-12-15 09:00:00', 45, '0'),
('Team Outing', 'Discussion for team outing', 1, '2024-12-20 13:00:00', 30, '1'),
('Sprint Retrospective', 'Review last sprint progress', 2, '2024-12-25 11:00:00', 60, '0');