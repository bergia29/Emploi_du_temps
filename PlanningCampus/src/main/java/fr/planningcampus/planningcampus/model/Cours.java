package fr.planningcampus.planningcampus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un cours dans le système
 */
public class Cours {

    private int id;
    private String matiere;
    private Enseignant enseignant;
    private List<Etudiant> etudiants;
    private Seance seance;
    private Salle salle;

    /**
     * Constructeur par défaut
     */
    public Cours() {
        this.etudiants = new ArrayList<>();
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id Identifiant du cours
     * @param matiere Nom de la matière
     * @param enseignant Enseignant du cours
     * @param seance Horaire du cours
     * @param salle Salle du cours
     */
    public Cours(int id, String matiere, Enseignant enseignant, Seance seance, Salle salle) {
        this.id = id;
        this.matiere = matiere;
        this.enseignant = enseignant;
        this.seance = seance;
        this.salle = salle;
        this.etudiants = new ArrayList<>();
    }

    /**
     * Gère le cours
     */
    public void gererCours() {
        System.out.println("Gestion du cours " + this.matiere);
    }

    /**
     * Accède aux informations du cours
     * @return Informations du cours sous forme de chaîne
     */
    public String accederInformationsCours() {
        return "Cours: " + this.matiere +
                ", Enseignant: " + this.enseignant.getPrenom() + " " + this.enseignant.getNom() +
                ", Salle: " + this.salle.getNom() +
                ", Horaire: " + this.seance.toString();
    }

    /**
     * Recherche les disponibilités pour le cours
     * @return true si des disponibilités sont trouvées, false sinon
     */
    public boolean rechercherDisponibilites() {
        System.out.println("Recherche de disponibilités pour le cours " + this.matiere);
        // Logique de recherche de disponibilités
        return true; // À remplacer par la vraie implémentation
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    public void ajouterEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
    }

    public Seance getHoraire() {
        return seance;
    }

    public void setHoraire(Seance seance) {
        this.seance = seance;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }
}