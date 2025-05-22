package fr.planningcampus.planningcampus.model;

/**
 * Interface définissant les méthodes d'authentification
 */
public interface Authentication {

    /**
     * Authentifie un utilisateur avec son email et mot de passe
     *
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @return true si l'authentification réussit, false sinon
     */
    boolean authentifier(String email, String motDePasse);
}