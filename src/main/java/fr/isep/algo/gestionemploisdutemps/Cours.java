package fr.isep.algo.gestionemploisdutemps;

import java.util.ArrayList;
import java.util.List;

public class Cours {
    public int idCours;
    public String matiere;
    public String description;
    public int duree;
    public String typeCours;
    public List<Etudiant> etudiants;

    public Cours(int idCours, String matiere, String description, int duree, String typeCours) {
        this.idCours = idCours;
        this.matiere = matiere;
        this.description = description;
        this.duree = duree;
        this.typeCours = typeCours;
        this.etudiants = new ArrayList<>();
    }

    // Ajoute un étudiant
    public void ajouterEtudiant(Etudiant etudiant) {
        etudiants.add(etudiant);
    }

    // Supprime un étudiant
    public void supprimerEtudiant(Etudiant etudiant) {
        etudiants.remove(etudiant);
    }

    // Retourne les informations du cours
    public String getInformations() {
        return "Cours ID: " + idCours +
                "\nMatière: " + matiere +
                "\nDescription: " + description +
                "\nDurée: " + duree + " min" +
                "\nType: " + typeCours +
                "\nNombre d'étudiants: " + etudiants.size();
    }

    // Getters et Setters
    public int getIdCours() {
        return idCours;
    }

    public void setIdCours(int idCours) {
        this.idCours = idCours;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getTypeCours() {
        return typeCours;
    }

    public void setTypeCours(String typeCours) {
        this.typeCours = typeCours;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }
}