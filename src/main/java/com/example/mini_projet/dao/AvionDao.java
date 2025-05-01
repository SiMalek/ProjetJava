package com.example.mini_projet.dao;

import com.example.mini_projet.model.avion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvionDao {
    public void ajouterAvion(avion avion) {
        String sql = "INSERT INTO avion (modele, capacite, statut) VALUES (?, ?, ?)";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement pst = cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, avion.getModel());
            pst.setInt(2, avion.getNbPlace());
            pst.setString(3, avion.getStatus().name());
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                avion.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add avion: " + e.getMessage(), e);
        }
    }

    public avion findById(int id) {
        String sql = "SELECT * FROM avion WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                avion avion = new avion(
                        rs.getString("modele"),
                        rs.getInt("capacite"),
                        com.example.mini_projet.model.avion.status.valueOf(rs.getString("statut"))
                );
                avion.setId(rs.getInt("id"));
                return avion;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding avion: " + e.getMessage(), e);
        }
        return null;
    }

    public List<avion> findAll() {
        List<avion> avions = new ArrayList<>();
        String sql = "SELECT * FROM avion";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                avion avion = new avion(
                        rs.getString("modele"),
                        rs.getInt("capacite"),
                        com.example.mini_projet.model.avion.status.valueOf(rs.getString("statut"))
                );
                avion.setId(rs.getInt("id"));
                avions.add(avion);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all avions: " + e.getMessage(), e);
        }
        return avions;
    }

    public List<avion> findAvailable() {
        List<avion> avions = new ArrayList<>();
        String sql = "SELECT * FROM avion WHERE statut = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, avion.status.disponible.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                avion avion = new avion(
                        rs.getString("modele"),
                        rs.getInt("capacite"),
                        com.example.mini_projet.model.avion.status.valueOf(rs.getString("statut"))
                );
                avion.setId(rs.getInt("id"));
                avions.add(avion);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding available avions: " + e.getMessage(), e);
        }
        return avions;
    }

    public void updateAvion(avion avion) {
        String sql = "UPDATE avion SET modele = ?, capacite = ?, statut = ? WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, avion.getModel());
            pst.setInt(2, avion.getNbPlace());
            pst.setString(3, avion.getStatus().name());
            pst.setInt(4, avion.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update avion: " + e.getMessage(), e);
        }
    }

    public void deleteAvion(int id) {
        String sql = "DELETE FROM avion WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete avion: " + e.getMessage(), e);
        }
    }
}