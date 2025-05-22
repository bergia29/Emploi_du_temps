package fr.planningcampus.planningcampus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une salle dans le système
 */
public class Salle {

    private int id;
    private String nom;
    private int capacite;
    private String localisation;
    private List<String> equipements;

    /**
     * Constructeur par défaut
     */
    public Salle() {
        this.equipements = new ArrayList<>();
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id Identifiant de la salle
     * @param nom Nom de la salle
     * @param capacite Capacité de la salle
     */
    public Salle(int id, String nom, int capacite) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.localisation = "Non spécifiée";
        this.equipements = new ArrayList<>();
    }

    /**
     * Constructeur complet
     *
     * @param id Identifiant de la salle
     * @param nom Nom de la salle
     * @param capacite Capacité de la salle
     * @param localisation Localisation de la salle
     */
    public Salle(int id, String nom, int capacite, String localisation) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.localisation = localisation;
        this.equipements = new ArrayList<>();
    }

    /**
     * Gère la salle
     */
    public void gererSalle() {
        System.out.println("Gestion de la salle " + this.nom);
    }

    /**
     * Vérifie la disponibilité de la salle
     * @return true si la salle est disponible, false sinon
     */
    public boolean verifierDisponibilite() {
        System.out.println("Vérification de la disponibilité de la salle " + this.nom);
        // Logique de vérification de disponibilité
        return true; // À remplacer par la vraie implémentation
    }

    /**
     * Ajoute un équipement à la salle
     * @param equipement L'équipement à ajouter
     */
    public void ajouterEquipement(String equipement) {
        if (!equipements.contains(equipement)) {
            equipements.add(equipement);
        }
    }

    /**
     * Supprime un équipement de la salle
     * @param equipement L'équipement à supprimer
     * @return true si l'équipement a été supprimé, false sinon
     */
    public boolean supprimerEquipement(String equipement) {
        return equipements.remove(equipement);
    }

    /**
     * Vérifie si la salle possède un équipement spécifique
     * @param equipement L'équipement à vérifier
     * @return true si la salle possède l'équipement, false sinon
     */
    public boolean possedeEquipement(String equipement) {
        return equipements.contains(equipement);
    }

    /**
     * Génère une description détaillée de la salle
     * @return Description détaillée
     */
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Salle: ").append(nom).append("\n");
        sb.append("Capacité: ").append(capacite).append(" places\n");
        sb.append("Localisation: ").append(localisation).append("\n");

        if (equipements.isEmpty()) {
            sb.append("Aucun équipement spécifique");
        } else {
            sb.append("Équipements: ");
            for (int i = 0; i < equipements.size(); i++) {
                sb.append(equipements.get(i));
                if (i < equipements.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public List<String> getEquipements() {
        return equipements;
    }

    public void setEquipements(List<String> equipements) {
        this.equipements = equipements;
    }
}