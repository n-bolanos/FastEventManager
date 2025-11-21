USE authdb;

-- Table storing system users
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    username VARCHAR(100) UNIQUE,
    email VARCHAR(255) UNIQUE,
    name VARCHAR(255) UNIQUE,
    
    password_hash VARCHAR(255) NOT NULL
);
