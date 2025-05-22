package fr.planningcampus.planningcampus.model;

/**
 * Classe représentant une note d'étudiant pour un cours
 */
public class NoteEtudiant {

    private int id;
    private int id_etudiant;
    private int id_cours;
    private float note;

    /**
     * Constructeur par défaut
     */
    public NoteEtudiant() {
    }

    /**
     * Constructeur avec paramètres
     *
     * @param id Identifiant de la note
     * @param id_etudiant Identifiant de l'étudiant
     * @param id_cours Identifiant du cours
     * @param note La note attribuée
     */
    public NoteEtudiant(int id, int id_etudiant, int id_cours, float note) {
        this.id = id;
        this.id_etudiant = id_etudiant;
        this.id_cours = id_cours;
        this.note = note;
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_etudiant() {
        return id_etudiant;
    }

    public void setId_etudiant(int id_etudiant) {
        this.id_etudiant = id_etudiant;
    }

    public int getId_cours() {
        return id_cours;
    }

    public void setId_cours(int id_cours) {
        this.id_cours = id_cours;
    }

    public float getNote() {
        return note;
    }

    public void setNote(float note) {
        this.note = note;
    }
}