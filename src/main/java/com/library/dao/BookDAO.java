package com.library.dao;

import com.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public void addBook(String title, String summary, String isbn, int pageCount, int categoryId) throws SQLException {
        String sql = "INSERT INTO books (title, summary, isbn, page_count, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, summary);
            stmt.setString(3, isbn);
            stmt.setInt(4, pageCount);
            stmt.setInt(5, categoryId);
            stmt.executeUpdate();
        }
    }

    public void updateBook(int id, String title, String summary, String isbn, int pageCount, int categoryId) throws SQLException {
        String sql = "UPDATE books SET title = ?, summary = ?, isbn = ?, page_count = ?, category_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, summary);
            stmt.setString(3, isbn);
            stmt.setInt(4, pageCount);
            stmt.setInt(5, categoryId);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Book> getAllBooks() throws SQLException {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"),
                        rs.getString("isbn"), rs.getInt("page_count"), rs.getInt("category_id")));
            }
        }
        return books;
    }

    public Book getBookById(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"),
                            rs.getString("isbn"), rs.getInt("page_count"), rs.getInt("category_id"));
                }
            }
        }
        return null;
    }

    public List<Book> searchBooks(String keyword) throws SQLException {
        String sql = "SELECT * FROM books WHERE title LIKE ? OR summary LIKE ?";
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"),
                            rs.getString("isbn"), rs.getInt("page_count"), rs.getInt("category_id")));
                }
            }
        }
        return books;
    }
}