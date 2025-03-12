package com.maitistaiga.dao;

import com.maitistaiga.model.Maitistaiga;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaitistaigaDAO {
    public void addMaitistaiga(String name) throws SQLException {
        String sql = "INSERT INTO maitinimo_istaigos (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    public void updateMaitistaiga(int id, String name) throws SQLException {
        String sql = "UPDATE maitinimo_istaigos SET name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void deleteMaitistaiga(int id) throws SQLException {
        String sql = "DELETE FROM maitinimo_istaigos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Maitistaiga> getAllMaitistaiga() throws SQLException {
        String sql = "SELECT * FROM maitinimo_istaigos";
        List<Maitistaiga> maitIstaigos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                maitIstaigos.add(new Maitistaiga(rs.getInt("id"), rs.getString("name")));
            }
        }
        return maitIstaigos;
    }

    public Maitistaiga getMaitistaigaById(int id) throws SQLException {
        String sql = "SELECT * FROM maitinimo_istaigos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Maitistaiga(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }
}