package fr.isep.algo.gestionemploisdutemps;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class EmploiDuTemps {
    protected int idEmploiDuTemps;
    protected Date dateDebut;
    protected Date dateFin;
    private List<Seance> seances;

    public EmploiDuTemps(int id, Date debut, Date fin) {
        this.idEmploiDuTemps = id;
        this.dateDebut = debut;
        this.dateFin = fin;
        this.seances = new ArrayList<>();
    }

    public void ajouterSeance(Seance seance) {
        seances.add(seance);
        System.out.println("EMPLOIDUTE");
    }

    public void supprimerSeance(Seance seance) {
        seances.remove(seance);
    }

    public void verifierConflits() {
        // Logique pour vérifier les conflits entre séances
        System.out.println("Vérification des conflits...");
    }

    public void afficherJour(Date date) {
        System.out.println("Séances du jour " + date + ":");
        for (Seance s : seances) {
            if (s.getDateDebut().equals(date)) {
                System.out.println(s.getInformations());
            }
        }
    }

    public void afficherSemaine(Date semaine) {
        System.out.println("Affichage de la semaine contenant : " + semaine);
        // À implémenter : afficher toutes les séances de la semaine donnée
    }

    public void afficherMois(Date mois) {
        System.out.println("Affichage du mois contenant : " + mois);
        // À implémenter : afficher toutes les séances du mois donné
    }

    // Getters et setters
    public int getIdEmploiDuTemps() {
        return idEmploiDuTemps;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public List<Seance> getSeances() {
        return seances;
    }

    public void setSeances(List<Seance> seances) {
        this.seances = seances;
    }

}
