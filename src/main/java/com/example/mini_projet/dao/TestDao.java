package com.example.mini_projet.dao;

import com.example.mini_projet.model.*;

import java.time.LocalDate;

public class TestDao {
    public static void main(String[] args) {
        laConnexion.setUser("root");
        laConnexion.setPassWord("");

        UserDao userDao = new UserDao();
        user user = new user(0, "agent1", "password123");
        userDao.ajouterUser(user);

        user authenticated = userDao.authenticate("agent1", "password123");
        if (authenticated != null) {
            System.out.println("Authentication successful for " + authenticated.getUsername());
        }

        AvionDao avionDao = new AvionDao();
        avion avion = new avion("Boeing 737", 150, status.disponible);
        avionDao.ajouterAvion(avion);

        EquipeDao equipeDao = new EquipeDao();
        equipe equipe = new equipe(0, "Doe", "John", "Pilot", true);
        equipeDao.ajouterEquipe(equipe);

        VolDao volDao = new VolDao();
        vol vol = new vol(0, "Paris", LocalDate.now(), equipe, avion);
        volDao.ajouterVol(vol);

        laConnexion.closeConnection();
    }
}