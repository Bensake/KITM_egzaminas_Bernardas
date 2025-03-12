-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS maitistaiga_db;
USE maitistaiga_db;

-- Drop tables if they exist (to ensure a clean setup)
DROP TABLE IF EXISTS uzsakymai;
DROP TABLE IF EXISTS patiekalai;
DROP TABLE IF EXISTS maitinimo_istaigos;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'CLIENT') NOT NULL
);

-- Create categories table
CREATE TABLE maitinimo_istaigos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Create books table
CREATE TABLE patiekalai (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pavadinimas VARCHAR(100) NOT NULL,
    aprasymas TEXT,
    maitinimo_istaiga_id INT,
    FOREIGN KEY (maitinimo_istaiga_id) REFERENCES maitinimo_istaigos(id)
);

-- Create reservations table
CREATE TABLE uzsakymai (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    patiekalas_id INT,
    uzsakymas_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (patiekalas_id) REFERENCES patiekalai(id),
    UNIQUE (patiekalas_id) -- Ensures one reservation per book
);


-- Insert sample data
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'ADMIN'),
('client1', 'client123', 'CLIENT');

INSERT INTO maitinimo_istaigos (name) VALUES
('Picerija'),
('Kebabine'),
('Magiski grybai');

INSERT INTO patiekalai (pavadinimas, aprasymas, maitinimo_istaiga_id) VALUES
('Napoli', '30cm dydzio', 1),
('Kebabas su kiauliena', 'Didelis', 2),
('Golden Teacher', '3.5g', 3);
