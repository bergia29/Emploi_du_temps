package fr.isep.algo.gestionemploisdutemps;

import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

public class Administrateur extends Utilisateur {
    protected String fonction;

    public Administrateur(int idUtilisateur, String nom, String prenom, String email, String motDePasse, String fonction) {
        super (idUtilisateur, nom, prenom, email, motDePasse);
        this.fonction = fonction;
    }
    @Override
    public String toString(){
        return "L'admin " + getNom() + " " + getPrenom() + ", Fonction: " + fonction;
    }

    public void modifierEmploiDuTemps() {
        System.out.println("Modification de l'emploi du temps par l'administrateur.");
    }

    public void affecterEnseignant() {
        System.out.println("Affectation d'un enseignant effectuée.");
    }

    public void gererSalles() {
        System.out.println("Gestion des salles en cours.");
    }

    public void gererConflits() {
        System.out.println("Résolution des conflits d'emploi du temps.");
    }

    public void gererStats() {
        System.out.println("Affichage des statistiques.");
    }

    // Getter et setter
    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }
}