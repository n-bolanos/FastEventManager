USE authdb;

INSERT IGNORE INTO user(name, username, email, password_hash)
VALUES ('pruebaa jaja', 'test', 'test@example.com', '$2a$10$YX.t.dWgg6Ohr1wl9xlj9ONC5sbpGp9hutKEtJXzQ7Q1mP5b20khy'),
 ('pruebaa jaja', 'demo2', 'deo@example.com', '$2a$10$hash_goes_here'),
 ('pruebaa jaja', 'demo3', 'dmo@example.com', '$3a$10$hash_goes_here'),
 ('pruebaa jaja', 'demo4', 'emo@example.com', '$4a$10$hash_goes_here'),
 ('pruebaa jaja', 'demo5', 'demo@example.com', '$5a$10$hash_goes_here');
