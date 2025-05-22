package fr.planningcampus.planningcampus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un emploi du temps dans le système
 */
public class EmploiDuTemps {

    private List<fr.planningcampus.planningcampus.model.Cours> cours;

    /**
     * Constructeur par défaut
     */
    public EmploiDuTemps() {
        this.cours = new ArrayList<>();
    }

    /**
     * Constructeur avec paramètres
     *
     * @param cours Liste des cours de l'emploi du temps
     */
    public EmploiDuTemps(List<fr.planningcampus.planningcampus.model.Cours> cours) {
        this.cours = cours;
    }

    /**
     * Gère les cours de l'emploi du temps
     */
    public void gererCours() {
        System.out.println("Gestion des cours de l'emploi du temps");
        for (fr.planningcampus.planningcampus.model.Cours cours : this.cours) {
            cours.gererCours();
        }
    }

    /**
     * Recherche les disponibilités pour les cours
     * @return true si des disponibilités sont trouvées pour tous les cours, false sinon
     */
    public boolean rechercherDisponibilites() {
        System.out.println("Recherche de disponibilités pour l'emploi du temps");
        boolean disponible = true;
        for (fr.planningcampus.planningcampus.model.Cours cours : this.cours) {
            disponible &= cours.rechercherDisponibilites();
        }
        return disponible;
    }

    // Getters et Setters

    public List<fr.planningcampus.planningcampus.model.Cours> getCours() {
        return cours;
    }

    public void setCours(List<fr.planningcampus.planningcampus.model.Cours> cours) {
        this.cours = cours;
    }

    public void ajouterCours(fr.planningcampus.planningcampus.model.Cours cours) {
        this.cours.add(cours);
    }

    public void supprimerCours(fr.planningcampus.planningcampus.model.Cours cours) {
        this.cours.remove(cours);
    }
}