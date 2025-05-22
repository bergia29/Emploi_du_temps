package fr.isep.algo.gestionemploisdutemps;

public class Enseignant extends Utilisateur {
    private String specialite;
    private int nbrHeure;

    public Enseignant(int idUtilisateur, String nom, String prenom, String email, String motDePasse,String role,
                      String specialite, int nbrHeure) {
        super(idUtilisateur, nom, prenom, email, motDePasse, role);
        this.specialite = specialite;
        this.nbrHeure = nbrHeure;
    }

    public void consulterEmploiDuTemps() {
        System.out.println("Emploi du temps de l'enseignant consulté.");
    }

    public void consulterInfosCours() {
        System.out.println("Informations sur les cours consultées.");
    }

    public void signalerAnomalie() {
        System.out.println("Anomalie signalée à l'administration.");
    }

    public void getCoursEnseignes() {
        System.out.println("Liste des cours enseignés affichée.");
    }

    // Getters et setters
    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public int getNbrHeure() {
        return nbrHeure;
    }

    public void setNbrHeure(int nbrHeure) {
        this.nbrHeure = nbrHeure;
    }
}


