package fr.isep.algo.gestionemploisdutemps;

import java.util.*;

public class GestionSalles {
    private Map<String, Salle> salles;  // clé : idSalle, valeur : Salle

    public GestionSalles() {
        salles = new HashMap<>();
    }

    // Ajouter une salle
    public boolean ajSalle(Salle salle) {
        if (salle == null || salles.containsKey(salle.getIdSalle())) {
            return false; // salle déjà existante ou salle nulle
        }
        salles.put(salle.getIdSalle(), salle);
        return true;
    }

    // Supprimer une salle par son id
    public boolean supprimer(String idSalle) {
        if (salles.containsKey(idSalle)) {
            salles.remove(idSalle);
            return true;
        }
        return false; // salle non trouvée
    }

    // Modifier une salle (remplace l'ancienne salle par la nouvelle, identifiée par idSalle)
    public boolean modifierSalle(Salle salleModifiee) {
        String id = salleModifiee.getIdSalle();
        if (salles.containsKey(id)) {
            salles.put(id, salleModifiee);
            return true;
        }
        return false; // salle non trouvée
    }

    // Trouver une salle disponible à une date donnée avec une capacité minimale requise
    public List<Salle> trouverSalleDisponible(Date date, int capaciteMin) {
        List<Salle> disponibles = new ArrayList<>();
        for (Salle salle : salles.values()) {
            if (salle.getCapacite() >= capaciteMin && salle.salleDisponible(date)) {
                disponibles.add(salle);
            }
        }
        return disponibles;
    }

    // Vérifier si une salle dispose des équipements requis
    public boolean verifierEquipements(String idSalle, List<String> equipementsRequis) {
        Salle salle = salles.get(idSalle);
        if (salle == null) return false;

        List<String> equipementsSalle = salle.getEquipements();
        // vérifier que tous les équipements requis sont dans la liste de la salle
        return equipementsSalle.containsAll(equipementsRequis);
    }

    // Gérer les statistiques (exemple simple : taux d'occupation moyen des salles)
    public void gererStats() {
        int totalSalles = salles.size();
        if (totalSalles == 0) {
            System.out.println("Aucune salle disponible.");
            return;
        }

        int totalReservations = 0;
        for (Salle salle : salles.values()) {
            totalReservations += salle.getReservations().size();
        }

        double moyenneReservations = (double) totalReservations / totalSalles;
        System.out.println("Nombre total de salles : " + totalSalles);
        System.out.println("Nombre total de réservations : " + totalReservations);
        System.out.printf("Taux moyen d'occupation (réservations par salle) : %.2f\n", moyenneReservations);
    }
}
