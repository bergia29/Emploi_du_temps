package fr.planningcampus.planningcampus.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

/**
 * Classe représentant un horaire dans le système
 */
public class Seance {

    private int id;
    private String jour;
    private Date heureDebut;
    private Date heureFin;
    private int semaine;
    private Date date; // Nouvelle propriété pour stocker la date complète

    /**
     * Constructeur par défaut
     */
    public Seance() {
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id Identifiant de l'horaire
     * @param jour Jour de la semaine
     * @param heureDebut Heure de début
     * @param heureFin Heure de fin
     * @param semaine Numéro de la semaine
     */
    public Seance(int id, String jour, Date heureDebut, Date heureFin, int semaine) {
        this.id = id;
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.semaine = semaine;
        this.date = heureDebut; // Utiliser heureDebut comme date complète
    }

    /**
     * Constructeur avec date complète
     *
     * @param id Identifiant de l'horaire
     * @param jour Jour de la semaine
     * @param heureDebut Heure de début
     * @param heureFin Heure de fin
     * @param semaine Numéro de la semaine
     * @param date Date complète incluant jour/mois/année
     */
    public Seance(int id, String jour, Date heureDebut, Date heureFin, int semaine, Date date) {
        this.id = id;
        this.jour = jour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.semaine = semaine;
        this.date = date;
    }

    /**
     * Gère l'horaire
     */
    public void gererHoraire() {
        System.out.println("Gestion de l'horaire du " + this.jour);
    }

    /**
     * Retourne une représentation textuelle de l'horaire
     *
     * @return Représentation textuelle
     */
    @Override
    public String toString() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");

        StringBuilder sb = new StringBuilder();

        if (date != null) {
            sb.append(sdfDate.format(date));
            sb.append(" (").append(jour).append(")");
        } else {
            sb.append(jour);
        }

        sb.append(" de ");
        sb.append(sdfHeure.format(heureDebut));
        sb.append(" à ");
        sb.append(sdfHeure.format(heureFin));

        if (semaine > 0) {
            sb.append(" (semaine ").append(semaine).append(")");
        }

        return sb.toString();
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public Date getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public int getSemaine() {
        return semaine;
    }

    public void setSemaine(int semaine) {
        this.semaine = semaine;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Vérifie si l'horaire est en conflit avec un autre
     *
     * @param autre Autre horaire à comparer
     * @return true si les horaires sont en conflit, false sinon
     */
    public boolean enConflit(Seance autre) {
        // Vérifier si c'est le même jour et la même semaine
        if (!this.jour.equals(autre.jour) || this.semaine != autre.semaine) {
            return false;
        }

        // Si les deux horaires ont des dates, vérifier qu'elles sont identiques
        if (this.date != null && autre.date != null) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(this.date);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(autre.date);

            // Comparer année, mois et jour
            if (cal1.get(Calendar.YEAR) != cal2.get(Calendar.YEAR) ||
                    cal1.get(Calendar.MONTH) != cal2.get(Calendar.MONTH) ||
                    cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)) {
                return false;
            }
        }

        // Vérifier si les plages horaires se chevauchent
        // cas 1: this commence pendant autre
        if (this.heureDebut.after(autre.heureDebut) && this.heureDebut.before(autre.heureFin)) {
            return true;
        }

        // cas 2: this finit pendant autre
        if (this.heureFin.after(autre.heureDebut) && this.heureFin.before(autre.heureFin)) {
            return true;
        }

        // cas 3: this englobe autre
        if (this.heureDebut.before(autre.heureDebut) && this.heureFin.after(autre.heureFin)) {
            return true;
        }

        // cas 4: autre englobe this
        if (autre.heureDebut.before(this.heureDebut) && autre.heureFin.after(this.heureFin)) {
            return true;
        }

        // cas 5: this et autre commencent ou finissent exactement au même moment
        if (this.heureDebut.equals(autre.heureDebut) || this.heureFin.equals(autre.heureFin)) {
            return true;
        }

        return false;
    }
}