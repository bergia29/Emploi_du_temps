package fr.planningcampus.planningcampus.model;

import java.util.List;

/**
 * Classe représentant un administrateur dans le système
 */
public class Administrateur extends Utilisateur {

    /**
     * Constructeur par défaut
     */
    public Administrateur() {
        super();
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id Identifiant de l'administrateur
     * @param nom Nom de l'administrateur
     * @param prenom Prénom de l'administrateur
     * @param email Email de l'administrateur
     * @param motDePasse Mot de passe de l'administrateur
     */
    public Administrateur(int id, String nom, String prenom, String email, String motDePasse) {
        super(id, nom, prenom, email, motDePasse);
    }

    /**
     * Gère les emplois du temps
     */
    public void gererEmploiDuTemps() {
        System.out.println("L'administrateur " + this.prenom + " " + this.nom + " gère les emplois du temps.");
    }

    /**
     * Gère les utilisateurs du système
     * @param utilisateurs Liste des utilisateurs à gérer
     */
    public void gererUtilisateurs(List<Utilisateur> utilisateurs) {
        System.out.println("L'administrateur " + this.prenom + " " + this.nom +
                " gère " + utilisateurs.size() + " utilisateurs.");
    }

    /**
     * Gère les salles du système
     * @param salles Liste des salles à gérer
     */
    public void gererSalles(List<Salle> salles) {
        System.out.println("L'administrateur " + this.prenom + " " + this.nom +
                " gère " + salles.size() + " salles.");
    }
}