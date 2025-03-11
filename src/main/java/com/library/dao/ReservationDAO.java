package com.library.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationDAO {
    public boolean reserveBook(int userId, int bookId) throws SQLException {
        String checkSql = "SELECT * FROM reservations WHERE book_id = ?";
        String insertSql = "INSERT INTO reservations (user_id, book_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if book is already reserved
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bookId);
                if (checkStmt.executeQuery().next()) {
                    return false; // Book already reserved
                }
            }
            // Reserve the book
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, bookId);
                insertStmt.executeUpdate();
                return true;
            }
        }
    }
}