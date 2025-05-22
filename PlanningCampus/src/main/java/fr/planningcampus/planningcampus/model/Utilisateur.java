package fr.planningcampus.planningcampus.model;

/**
 * Classe abstraite représentant un utilisateur du système
 */
public abstract class Utilisateur implements fr.planningcampus.planningcampus.model.Authentication {

    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasse;

    /**
     * Constructeur par défaut
     */
    public Utilisateur() {
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id Identifiant de l'utilisateur
     * @param nom Nom de l'utilisateur
     * @param prenom Prénom de l'utilisateur
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     */
    public Utilisateur(int id, String nom, String prenom, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    /**
     * Se connecter au système
     */
    public void seConnecter() {
        System.out.println("L'utilisateur " + this.prenom + " " + this.nom + " s'est connecté.");
    }

    /**
     * Se déconnecter du système
     */
    public void seDeconnecter() {
        System.out.println("L'utilisateur " + this.prenom + " " + this.nom + " s'est déconnecté.");
    }

    /**
     * Implémentation de la méthode d'authentification
     */
    @Override
    public boolean authentifier(String email, String motDePasse) {
        return this.email.equals(email) && this.motDePasse.equals(motDePasse);
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
}