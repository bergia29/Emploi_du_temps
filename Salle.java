package fr.isep.algo.gestionemploisdutemps;

import java.util.*;

public class Salle {
    public String idSalle;
    public String localisation;
    public int capacite;
    public List<String> equipements;
    public Map<Date, Cours> reservations; // pour stocker les réservations par date

    // Constructeur
    public Salle(String idSalle, String localisation, int capacite, List<String> equipements) {
        this.idSalle = idSalle;
        this.localisation = localisation;
        this.capacite = capacite;
        this.equipements = new ArrayList<>(equipements);
        this.reservations = new HashMap<>();
    }

    // Vérifie si la salle est disponible à une date donnée
    public boolean salleDisponible(Date date) {
        return !reservations.containsKey(date);
    }

    // Réserve la salle pour un cours à une date donnée
    public boolean reserverDate(Date date, Cours cours) {
        if (salleDisponible(date)) {
            reservations.put(date, cours);
            return true;
        } else {
            return false; // salle déjà réservée à cette date
        }
    }

    // Libère la salle pour une date donnée
    public void libere(Date date) {
        reservations.remove(date);
    }

    // Retourne les informations de la salle sous forme de chaîne
    public String getInformations() {
        return "Salle ID: " + idSalle + "\nLocalisation: " + localisation +
                "\nCapacité: " + capacite + "\nÉquipements: " + equipements.toString();
    }

    // Getters et setters si besoin
    public String getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(String idSalle) {
        this.idSalle = idSalle;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public List<String> getEquipements() {
        return new ArrayList<>(equipements);
    }

    public void setEquipements(List<String> equipements) {
        this.equipements = new ArrayList<>(equipements);
    }

    public Map<Date, Cours> getReservations() {
        return new HashMap<>(reservations);
    }
}
