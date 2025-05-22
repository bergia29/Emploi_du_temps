package fr.isep.algo.gestionemploisdutemps;

import java.util.Date;

public class Seance {
    private int idSeance;
    private Date dateDebut;
    private Date dateFin;
    private boolean confirmee;
    public Cours cours;

    public Seance(int id, Date debut, Date fin, boolean confirmee,Cours cours) {
        this.idSeance = id;
        this.dateDebut = debut;
        this.dateFin = fin;
        this.confirmee = confirmee;
        this.cours = cours;

    }

    public void annuler() {
        this.confirmee = false;
    }

    public String getInformations() {
        return "Séance ID: " + idSeance + ", Début: " + dateDebut + ", Fin: " + dateFin + ", Confirmée: " + confirmee;
    }

    public void notifierParticipants() {
        System.out.println("Notification envoyée aux participants de la séance " + idSeance);
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }
    public void setCours(Cours cours) {
        this.cours = cours;
    }
}
