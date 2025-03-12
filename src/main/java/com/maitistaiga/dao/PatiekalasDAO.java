package com.maitistaiga.dao;

import com.maitistaiga.model.Patiekalas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatiekalasDAO {
    public void pridetiPatiekala(String pavadinimas, String aprasymas, int maitIstaigaId) throws SQLException {
        String sql = "INSERT INTO patiekalai (pavadinimas, aprasymas, maitinimo_istaiga_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pavadinimas);
            stmt.setString(2, aprasymas);
            stmt.setInt(5, maitIstaigaId);
            stmt.executeUpdate();
        }
    }

    public void redaguotiPatiekala(int id, String pavadinimas, String aprasymas, int maitIstaigaId) throws SQLException {
        String sql = "UPDATE patiekalai SET pavadinimas = ?, aprasymas = ?, maitinimo_istaiga_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pavadinimas);
            stmt.setString(2, aprasymas);
            stmt.setInt(5, maitIstaigaId);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }

    public void istrintiPatiekala(int id) throws SQLException {
        String sql = "DELETE FROM patiekalai WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Patiekalas> getVisusPatiekalus() throws SQLException {
        String sql = "SELECT * FROM patiekalai";
        List<Patiekalas> patiekalai = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                patiekalai.add(new Patiekalas(rs.getInt("id"), rs.getString("pavadinimas"), rs.getString("aprasymas"),
                        rs.getInt("maitinimo_istaiga_id")));
            }
        }
        return patiekalai;
    }

    public Patiekalas getPatiekalasById(int id) throws SQLException {
        String sql = "SELECT * FROM patiekalai WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patiekalas(rs.getInt("id"), rs.getString("pavadinimas"), rs.getString("aprasymas"),
                            rs.getInt("maitinimo_istaiga_id"));
                }
            }
        }
        return null;
    }

    public List<Patiekalas> ieskotiPatiekalo(String keyword) throws SQLException {
        String sql = "SELECT * FROM patiekalai WHERE pavadinimas LIKE ? OR aprasymas LIKE ?";
        List<Patiekalas> patiekalai = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patiekalai.add(new Patiekalas(rs.getInt("id"), rs.getString("pavadinimas"), rs.getString("aprasymas"),
                            rs.getInt("maitinimo_istaiga_id")));
                }
            }
        }
        return patiekalai;
    }
}