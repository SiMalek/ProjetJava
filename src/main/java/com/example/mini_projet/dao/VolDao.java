package com.example.mini_projet.dao;

import com.example.mini_projet.model.avion;
import com.example.mini_projet.model.equipe;
import com.example.mini_projet.model.vol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VolDao {
    private AvionDao avionDao = new AvionDao();
    private EquipeDao equipeDao = new EquipeDao();

    public void ajouterVol(vol vol) {
        avion avion = avionDao.findById(vol.getAvion().getId());
        equipe equipe = equipeDao.findById(vol.getEquipe().getId());
        if (avion == null || !avion.getStatus().equals(avion.status.disponible)) {
            throw new IllegalStateException("Avion is not available");
        }
        if (equipe == null || !equipe.isDisponible()) {
            throw new IllegalStateException("Equipe is not available");
        }
        String sql = "INSERT INTO vol (destination, dateDepart, equipe_id, avion_id) VALUES (?, ?, ?, ?)";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vol.getDestination());
            stmt.setString(2, vol.getDateDepart().toString());
            stmt.setInt(3, vol.getEquipe().getId());
            stmt.setInt(4, vol.getAvion().getId());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                vol.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add vol: " + e.getMessage(), e);
        }
    }

    public List<vol> trouverVol(String destination, LocalDate date, Integer equipeId, Integer avionId) {
        List<vol> vols = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM vol WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (destination != null && !destination.isEmpty()) {
            sql.append(" AND destination LIKE ?");
            params.add("%" + destination + "%");
        }
        if (date != null) {
            sql.append(" AND dateDepart = ?");
            params.add(date.toString());
        }
        if (equipeId != null) {
            sql.append(" AND equipe_id = ?");
            params.add(equipeId);
        }
        if (avionId != null) {
            sql.append(" AND avion_id = ?");
            params.add(avionId);
        }
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                avion avion = avionDao.findById(rs.getInt("avion_id"));
                equipe equipe = equipeDao.findById(rs.getInt("equipe_id"));
                vols.add(new vol(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        LocalDate.parse(rs.getString("dateDepart")),
                        equipe,
                        avion
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding vols: " + e.getMessage(), e);
        }
        return vols;
    }

    public vol findById(int id) {
        String sql = "SELECT * FROM vol WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                avion avion = avionDao.findById(rs.getInt("avion_id"));
                equipe equipe = equipeDao.findById(rs.getInt("equipe_id"));
                return new vol(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        LocalDate.parse(rs.getString("dateDepart")),
                        equipe,
                        avion
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding vol: " + e.getMessage(), e);
        }
        return null;
    }

    public List<vol> findAll() {
        List<vol> vols = new ArrayList<>();
        String sql = "SELECT * FROM vol";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                avion avion = avionDao.findById(rs.getInt("avion_id"));
                equipe equipe = equipeDao.findById(rs.getInt("equipe_id"));
                vols.add(new vol(
                        rs.getInt("id"),
                        rs.getString("destination"),
                        LocalDate.parse(rs.getString("dateDepart")),
                        equipe,
                        avion
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all vols: " + e.getMessage(), e);
        }
        return vols;
    }

    public void updateVol(vol vol) {
        avion avion = avionDao.findById(vol.getAvion().getId());
        equipe equipe = equipeDao.findById(vol.getEquipe().getId());
        if (avion == null || !avion.getStatus().equals(avion.status.disponible)) {
            throw new IllegalStateException("Avion is not available");
        }
        if (equipe == null || !equipe.isDisponible()) {
            throw new IllegalStateException("Equipe is not available");
        }
        String sql = "UPDATE vol SET destination = ?, dateDepart = ?, equipe_id = ?, avion_id = ? WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setString(1, vol.getDestination());
            stmt.setString(2, vol.getDateDepart().toString());
            stmt.setInt(3, vol.getEquipe().getId());
            stmt.setInt(4, vol.getAvion().getId());
            stmt.setInt(5, vol.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update vol: " + e.getMessage(), e);
        }
    }

    public void deleteVol(int id) {
        String sql = "DELETE FROM vol WHERE id = ?";
        try (Connection cn = laConnexion.seConnecter();
             PreparedStatement stmt = cn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete vol: " + e.getMessage(), e);
        }
    }
}