package fr.planningcampus.planningcampus.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe représentant un enseignant dans le système
 */
public class Enseignant extends Utilisateur {

    private List<String> matieres;
    private Date datePriseFonction;

    /**
     * Constructeur par défaut
     */
    public Enseignant() {
        super();
        this.matieres = new ArrayList<>();
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id Identifiant de l'enseignant
     * @param nom Nom de l'enseignant
     * @param prenom Prénom de l'enseignant
     * @param email Email de l'enseignant
     * @param motDePasse Mot de passe de l'enseignant
     * @param datePriseFonction Date de prise de fonction
     */
    public Enseignant(int id, String nom, String prenom, String email, String motDePasse, Date datePriseFonction) {
        super(id, nom, prenom, email, motDePasse);
        this.datePriseFonction = datePriseFonction;
        this.matieres = new ArrayList<>();
    }

    /**
     * Consulte les cours de l'enseignant
     * @return Liste des cours de l'enseignant
     */
    public List<fr.planningcampus.planningcampus.model.Cours> consulterCours() {
        System.out.println("L'enseignant " + this.prenom + " " + this.nom + " consulte ses cours.");
        // Logique pour récupérer les cours
        return new ArrayList<>(); // À remplacer par la vraie implémentation
    }

    /**
     * Accède aux informations d'un cours
     * @param cours Le cours dont on veut les informations
     */
    public void accederInformationsCours(fr.planningcampus.planningcampus.model.Cours cours) {
        System.out.println("L'enseignant " + this.prenom + " " + this.nom +
                " accède aux informations du cours " + cours.getMatiere());
    }

    /**
     * Signale un problème pour un cours
     * @param cours Le cours concerné
     * @param probleme Description du problème
     */
    public void signalerProbleme(fr.planningcampus.planningcampus.model.Cours cours, String probleme) {
        System.out.println("L'enseignant " + this.prenom + " " + this.nom +
                " signale un problème pour le cours " + cours.getMatiere() + ": " + probleme);
    }

    /**
     * Accède aux informations d'une salle
     * @param salle La salle dont on veut les informations
     */
    public void accederInformationsSalle(Salle salle) {
        System.out.println("L'enseignant " + this.prenom + " " + this.nom +
                " accède aux informations de la salle " + salle.getNom());
    }

    // Getters et Setters

    public List<String> getMatieres() {
        return matieres;
    }

    public void setMatieres(List<String> matieres) {
        this.matieres = matieres;
    }

    public void ajouterMatiere(String matiere) {
        this.matieres.add(matiere);
    }

    public Date getDatePriseFonction() {
        return datePriseFonction;
    }

    public void setDatePriseFonction(Date datePriseFonction) {
        this.datePriseFonction = datePriseFonction;
    }
}