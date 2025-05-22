package fr.isep.algo.gestionemploisdutemps;

import java.util.Date;

public class Notification {
    protected int idNotif;
    protected String message;
    protected Date dateEnvoi;
    protected boolean marqLue;

    public Notification(int idNotif, String message, Date dateEnvoi, boolean marqLue) {
        this.idNotif = idNotif;
        this.message = message;
        this.dateEnvoi = dateEnvoi;
        this.marqLue = false;// par défaut false(nolue)
    }

    public void marquerLue() {
        this.marqLue = true;
        System.out.println("NOTIFICATION    Fonction Marquer lue");
    }

    public void modifierEmploiDuTemps() {
        System.out.println("NOTIFICATION    Fonction modifier l'emploi du temps");
    }

    // Getters
    public int getIdNotif() {
        return idNotif;
    }

    public String getMessage() {
        return message;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public boolean isMarqLue() {
        return marqLue;
    }

    // Setters
    public void setIdNotif(int idNotif) {
        this.idNotif = idNotif;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setMarqLue(boolean marqLue) {
        this.marqLue = marqLue;
    }

}