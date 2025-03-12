package com.maitistaiga.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UzsakymasDAO {
    public boolean uzsakytiPatiekala(int userId, int patiekalasId) throws SQLException {
        String checkSql = "SELECT * FROM uzsakymai WHERE patiekalas_id = ?";
        String insertSql = "INSERT INTO uzsakymai (user_id, patiekalas_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if book is already reserved
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, patiekalasId);
                if (checkStmt.executeQuery().next()) {
                    return false; // Book already reserved
                }
            }
            // Reserve the book
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, patiekalasId);
                insertStmt.executeUpdate();
                return true;
            }
        }
    }
}