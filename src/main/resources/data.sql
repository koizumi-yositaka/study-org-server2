
INSERT INTO T_AUTHORITY (id,rank, description) VALUES
('A','ADMIN', 'Administrator Level'),
('B', 'POWER_USER','Moderator Level'),
('C', 'NORMAL','User Level');
INSERT INTO T_USER (email, password, authority_id) VALUES
('admin@example.com', '$2a$10$UgyTyEN6XuwXoHM60xGP4eC3tv6yBJzhVnMtDvhjwBy1hbpESQoKS', 'A'), -- Aランク
('moderator@example.com', '$2a$10$yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy', 'B'), -- Bランク
('user@example.com', '$2a$10$zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz', 'C'); -- Cランク
INSERT INTO T_MEETING (title, detail, opener_id, event_date,start_time, end_time,delete_flg)
VALUES
('Project Kickoff', 'Initial project discussion', 1, '2024-11-01','1700','1800','0'),
('Weekly Sync', 'Weekly team sync-up', 2, '2024-11-05', '1700','1800','0'),
('Budget Project', 'Review of budget allocations', 3, '2023-10-10', '1700','1800','0'),
('Client Presentation', 'Presentation to client', 1, '2024-12-15', '1700','1800','0'),
('Sprint Retrospective', 'Review last sprint progress', 2, '2025-12-25', '1700','1800','0'),
('Sprint Retrospective', 'Review last sprint progress', 2, '2025-12-25', '1700','1800','1');