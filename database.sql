-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- Drop tables if they exist (to ensure a clean setup)
DROP TABLE IF EXISTS favorites;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'READER') NOT NULL
);

-- Create categories table
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Create books table
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    summary TEXT,
    isbn VARCHAR(13) NOT NULL UNIQUE,
    image_path VARCHAR(255),
    page_count INT NOT NULL,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Create reservations table
CREATE TABLE reservations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    book_id INT,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    UNIQUE (book_id) -- Ensures one reservation per book
);

-- Create favorites table
CREATE TABLE favorites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    book_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    UNIQUE (user_id, book_id) -- No duplicate favorites per user
);

-- Insert sample data
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'ADMIN'),
('reader1', 'read123', 'READER');

INSERT INTO categories (name) VALUES 
('Fiction'),
('Non-Fiction'),
('Science');

INSERT INTO books (title, summary, isbn, page_count, category_id) VALUES 
('The Great Gatsby', 'A story of wealth and love', '1234567890123', 180, 1),
('Sapiens', 'A brief history of humankind', '9876543210987', 443, 2),
('Cosmos', 'Exploring the universe', '1111222233334', 352, 3);

-- Optional: Insert a sample reservation
INSERT INTO reservations (user_id, book_id) VALUES 
(2, 1); -- reader1 reserves The Great Gatsby

-- Optional: Insert a sample favorite
INSERT INTO favorites (user_id, book_id) VALUES 
(2, 2); -- reader1 favorites Sapiens