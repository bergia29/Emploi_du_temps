package fr.isep.algo.gestionemploisdutemps;

public class Authentification {
    public boolean authentifier(String email, String motDePasse) {
        System.out.println("AUTHENTIFICATION  Authentification");
        return true;
    }

    public void creerUtilisateur(Utilisateur utilisateur) {
        System.out.println("AUTHENTIFICATION    Creer utilisateur");
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        System.out.println("AUTHENTIFICATION    modifier utilisateur");
    }

    public void supprimerUtilisateur(int idUtilisateur) {
        System.out.println("AUTHENTIFICATION    supprimer utilisateur");
    }

    public void verifRole(Utilisateur utilisateur) {
        System.out.println("AUTHENTIFICATION    Verifier utilisateur");
    }
}
