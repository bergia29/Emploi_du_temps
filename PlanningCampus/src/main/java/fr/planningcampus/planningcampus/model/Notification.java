package fr.planningcampus.planningcampus.model;

import java.util.Date;

/**
 * Classe représentant une notification dans le système
 */
public class Notification {

    private int id;
    private int idDestinataire;
    private int idExpediteur;
    private String contenu;
    private Date date;
    private boolean lue;

    /**
     * Constructeur par défaut
     */
    public Notification() {
        this.date = new Date();
        this.lue = false;
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id ID de la notification
     * @param idDestinataire ID du destinataire
     * @param idExpediteur ID de l'expéditeur
     * @param contenu Contenu de la notification
     * @param date Date de la notification
     * @param lue État de lecture de la notification
     */
    public Notification(int id, int idDestinataire, int idExpediteur, String contenu, Date date, boolean lue) {
        this.id = id;
        this.idDestinataire = idDestinataire;
        this.idExpediteur = idExpediteur;
        this.contenu = contenu;
        this.date = date;
        this.lue = lue;
    }

    /**
     * Récupère l'ID de la notification
     *
     * @return ID de la notification
     */
    public int getId() {
        return id;
    }

    /**
     * Définit l'ID de la notification
     *
     * @param id Nouvel ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Récupère l'ID du destinataire
     *
     * @return ID du destinataire
     */
    public int getIdDestinataire() {
        return idDestinataire;
    }

    /**
     * Définit l'ID du destinataire
     *
     * @param idDestinataire Nouvel ID du destinataire
     */
    public void setIdDestinataire(int idDestinataire) {
        this.idDestinataire = idDestinataire;
    }

    /**
     * Récupère l'ID de l'expéditeur
     *
     * @return ID de l'expéditeur
     */
    public int getIdExpediteur() {
        return idExpediteur;
    }

    /**
     * Définit l'ID de l'expéditeur
     *
     * @param idExpediteur Nouvel ID de l'expéditeur
     */
    public void setIdExpediteur(int idExpediteur) {
        this.idExpediteur = idExpediteur;
    }

    /**
     * Récupère le contenu de la notification
     *
     * @return Contenu de la notification
     */
    public String getContenu() {
        return contenu;
    }

    /**
     * Définit le contenu de la notification
     *
     * @param contenu Nouveau contenu
     */
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    /**
     * Récupère la date de la notification
     *
     * @return Date de la notification
     */
    public Date getDate() {
        return date;
    }

    /**
     * Définit la date de la notification
     *
     * @param date Nouvelle date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Vérifie si la notification a été lue
     *
     * @return true si la notification a été lue, false sinon
     */
    public boolean isLue() {
        return lue;
    }

    /**
     * Définit l'état de lecture de la notification
     *
     * @param lue Nouvel état de lecture
     */
    public void setLue(boolean lue) {
        this.lue = lue;
    }

    /**
     * Pour la compatibilité avec les PropertyValueFactory de JavaFX
     */
    public boolean getLue() {
        return lue;
    }

    @Override
    public String toString() {
        return contenu;
    }
}