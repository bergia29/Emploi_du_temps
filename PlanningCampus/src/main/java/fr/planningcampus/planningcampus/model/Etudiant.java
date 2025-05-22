package fr.planningcampus.planningcampus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un étudiant dans le système
 */
public class Etudiant extends Utilisateur {

    private String groupe;
    private List<NoteEtudiant> notes;

    /**
     * Constructeur par défaut
     */
    public Etudiant() {
        super();
        this.notes = new ArrayList<>();
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id Identifiant de l'étudiant
     * @param nom Nom de l'étudiant
     * @param prenom Prénom de l'étudiant
     * @param email Email de l'étudiant
     * @param motDePasse Mot de passe de l'étudiant
     * @param groupe Groupe de l'étudiant
     */
    public Etudiant(int id, String nom, String prenom, String email, String motDePasse, String groupe) {
        super(id, nom, prenom, email, motDePasse);
        this.groupe = groupe;
        this.notes = new ArrayList<>();
    }

    /**
     * Consulte l'emploi du temps de l'étudiant
     * @return Liste des cours de l'étudiant
     */
    public List<fr.planningcampus.planningcampus.model.Cours> consulterEmploiDuTemps() {
        System.out.println("L'étudiant " + this.prenom + " " + this.nom + " consulte son emploi du temps.");
        // Logique pour récupérer l'emploi du temps
        return new ArrayList<>(); // À remplacer par la vraie implémentation
    }

    /**
     * Reçoit une notification
     * @param notification La notification à recevoir
     */
    public void recevoirNotification(Notification notification) {
        System.out.println("L'étudiant " + this.prenom + " " + this.nom +
                " a reçu une notification: " + notification.getContenu());
    }

    // Getters et Setters

    public String getGroupe() {
        return groupe;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    public List<NoteEtudiant> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteEtudiant> notes) {
        this.notes = notes;
    }

    public void ajouterNote(NoteEtudiant note) {
        this.notes.add(note);
    }
}