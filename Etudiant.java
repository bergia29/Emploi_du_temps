package fr.isep.algo.gestionemploisdutemps;

public class Etudiant extends Utilisateur{
    protected String numEtud;
    protected String filiere;
    protected String niveau;

    public Etudiant(int idUtilisateur, String nom, String prenom, String email, String motDePasse,
                    String numEtud, String filiere, String niveau) {
        super(idUtilisateur, nom, prenom, email, motDePasse);
        this.numEtud = numEtud;
        this.filiere = filiere;
        this.niveau = niveau;
    }

    public void consulterEmploiDuTemps() {
        System.out.println("Emploi du temps de l'étudiant consulté.");
    }

    public void consulterSalle() {
        System.out.println("Informations sur la salle de cours consultées.");
    }

    public void recevoirNotification(String message) {
        System.out.println("Notification reçue : " + message);
    }

    // Getters et setters
    public String getNumEtud() {
        return numEtud;
    }

    public void setNumEtud(String numEtud) {
        this.numEtud = numEtud;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
}
