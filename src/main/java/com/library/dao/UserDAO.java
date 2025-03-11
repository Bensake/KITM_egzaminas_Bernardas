package com.library.dao;

import com.library.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        System.out.println("Attempting to connect to database...");
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Connection established. Executing query...");
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Trim inputs to remove leading/trailing spaces
                username = username.trim();
                password = password.trim();
                System.out.println("Querying for username: '" + username + "' (length: " + username.length() + ")");
                System.out.println("Querying for password: '" + password + "' (length: " + password.length() + ")");
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                System.out.println("Query executed. Checking results...");
                if (rs.next()) {
                    System.out.println("User found: " + rs.getString("username") + " with role " + rs.getString("role"));
                    return new User(rs.getInt("id"), rs.getString("username"),
                            rs.getString("password"), rs.getString("role"));
                } else {
                    System.out.println("No user found with those credentials.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Authentication failed - returning null.");
        return null;
    }
}