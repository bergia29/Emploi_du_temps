package fr.planningcampus.planningcampus.dao;

import fr.planningcampus.planningcampus.model.Seance;
import fr.planningcampus.planningcampus.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

/**
 * Classe DAO pour les opérations CRUD sur les horaires
 */
public class SeanceDAO {

    /**
     * Récupère tous les horaires de la base de données
     *
     * @return Liste des horaires
     */
    public List<Seance> getAllHoraires() {
        List<Seance> seances = new ArrayList<>();
        String query = "SELECT * FROM horaire";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String jour = rs.getString("jour");
                Time heureDebut = rs.getTime("heureDebut");
                Time heureFin = rs.getTime("heureFin");
                int semaine = rs.getInt("semaine");
                Date date = rs.getDate("date");

                // Créer un calendrier pour l'heure de début (qui contient déjà l'heure)
                Calendar calHeureDebut = Calendar.getInstance();
                calHeureDebut.setTime(new java.util.Date(heureDebut.getTime()));

                // Si date est null, créer une date à partir du jour de la semaine
                if (date == null) {
                    // Créer une date actuelle
                    Calendar calNow = Calendar.getInstance();

                    // Trouver le jour de la semaine correspondant au jour spécifié
                    int targetDayOfWeek = getDayOfWeekFromString(jour);

                    // Ajuster la date au jour de la semaine correct
                    calNow.set(Calendar.DAY_OF_WEEK, targetDayOfWeek);

                    // Créer une nouvelle date SQL
                    date = new java.sql.Date(calNow.getTimeInMillis());
                }

                // Utiliser cette date pour tous les champs
                java.util.Date utilDate = new java.util.Date(date.getTime());

                // Pour heureDebut et heureFin, conserver uniquement l'heure et les minutes
                Calendar calDate = Calendar.getInstance();
                calDate.setTime(utilDate);

                Calendar calDebut = Calendar.getInstance();
                calDebut.setTime(new java.util.Date(heureDebut.getTime()));
                calDate.set(Calendar.HOUR_OF_DAY, calDebut.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calDebut.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, 0);
                calDate.set(Calendar.MILLISECOND, 0);
                java.util.Date dateHeureDebut = calDate.getTime();

                Calendar calFin = Calendar.getInstance();
                calFin.setTime(new java.util.Date(heureFin.getTime()));
                calDate.set(Calendar.HOUR_OF_DAY, calFin.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calFin.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, 0);
                calDate.set(Calendar.MILLISECOND, 0);
                java.util.Date dateHeureFin = calDate.getTime();

                Seance seance = new Seance(id, jour, dateHeureDebut, dateHeureFin, semaine, utilDate);
                seances.add(seance);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des horaires: " + e.getMessage());
        }

        return seances;
    }

    /**
     * Récupère un horaire par son ID
     *
     * @param id ID de l'horaire
     * @return Horaire trouvé ou null
     */
    public Seance getHoraireById(int id) {
        String query = "SELECT * FROM horaire WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String jour = rs.getString("jour");
                    Time heureDebut = rs.getTime("heureDebut");
                    Time heureFin = rs.getTime("heureFin");
                    int semaine = rs.getInt("semaine");
                    Date date = rs.getDate("date");

                    // Si date est null, créer une date à partir du jour de la semaine
                    if (date == null) {
                        // Créer une date actuelle
                        Calendar calNow = Calendar.getInstance();

                        // Trouver le jour de la semaine correspondant au jour spécifié
                        int targetDayOfWeek = getDayOfWeekFromString(jour);

                        // Ajuster la date au jour de la semaine correct
                        calNow.set(Calendar.DAY_OF_WEEK, targetDayOfWeek);

                        // Créer une nouvelle date SQL
                        date = new java.sql.Date(calNow.getTimeInMillis());
                    }

                    // Utiliser cette date pour tous les champs
                    java.util.Date utilDate = new java.util.Date(date.getTime());

                    // Pour heureDebut et heureFin, conserver uniquement l'heure et les minutes
                    Calendar calDate = Calendar.getInstance();
                    calDate.setTime(utilDate);

                    Calendar calDebut = Calendar.getInstance();
                    calDebut.setTime(new java.util.Date(heureDebut.getTime()));
                    calDate.set(Calendar.HOUR_OF_DAY, calDebut.get(Calendar.HOUR_OF_DAY));
                    calDate.set(Calendar.MINUTE, calDebut.get(Calendar.MINUTE));
                    calDate.set(Calendar.SECOND, 0);
                    calDate.set(Calendar.MILLISECOND, 0);
                    java.util.Date dateHeureDebut = calDate.getTime();

                    Calendar calFin = Calendar.getInstance();
                    calFin.setTime(new java.util.Date(heureFin.getTime()));
                    calDate.set(Calendar.HOUR_OF_DAY, calFin.get(Calendar.HOUR_OF_DAY));
                    calDate.set(Calendar.MINUTE, calFin.get(Calendar.MINUTE));
                    calDate.set(Calendar.SECOND, 0);
                    calDate.set(Calendar.MILLISECOND, 0);
                    java.util.Date dateHeureFin = calDate.getTime();

                    return new Seance(id, jour, dateHeureDebut, dateHeureFin, semaine, utilDate);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'horaire: " + e.getMessage());
        }

        return null;
    }

    /**
     * Ajoute un horaire dans la base de données
     *
     * @param seance Horaire à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addHoraire(Seance seance) {
        String query = "INSERT INTO horaire (jour, heureDebut, heureFin, semaine, date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, seance.getJour());
            pstmt.setTime(2, new Time(seance.getHeureDebut().getTime()));
            pstmt.setTime(3, new Time(seance.getHeureFin().getTime()));
            pstmt.setInt(4, seance.getSemaine());

            // Si date est null, utiliser la date actuelle
            if (seance.getDate() == null) {
                seance.setDate(new java.util.Date());
            }

            pstmt.setDate(5, new java.sql.Date(seance.getDate().getTime()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        seance.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'horaire: " + e.getMessage());
        }

        return false;
    }

    /**
     * Met à jour un horaire dans la base de données
     *
     * @param seance Horaire à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateHoraire(Seance seance) {
        String query = "UPDATE horaire SET jour = ?, heureDebut = ?, heureFin = ?, semaine = ?, date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, seance.getJour());
            pstmt.setTime(2, new Time(seance.getHeureDebut().getTime()));
            pstmt.setTime(3, new Time(seance.getHeureFin().getTime()));
            pstmt.setInt(4, seance.getSemaine());

            // Si date est null, utiliser la date actuelle
            if (seance.getDate() == null) {
                seance.setDate(new java.util.Date());
            }

            pstmt.setDate(5, new java.sql.Date(seance.getDate().getTime()));
            pstmt.setInt(6, seance.getId());

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'horaire: " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un horaire de la base de données
     *
     * @param id ID de l'horaire à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteHoraire(int id) {
        String query = "DELETE FROM horaire WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'horaire: " + e.getMessage());
        }

        return false;
    }

    /**
     * Vérifie si la colonne date existe dans la table horaire
     * et la crée si elle n'existe pas
     */
    public void verifierColonneDate() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Vérifier si la colonne existe déjà
            boolean colonneExiste = false;

            try (ResultSet rs = conn.getMetaData().getColumns(null, null, "horaire", "date")) {
                colonneExiste = rs.next();
            }

            // Si la colonne n'existe pas, l'ajouter
            if (!colonneExiste) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("ALTER TABLE horaire ADD COLUMN date DATE DEFAULT NULL");
                    System.out.println("Colonne date ajoutée à la table horaire");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification/création de la colonne date: " + e.getMessage());
        }
    }

    /**
     * Convertit une chaîne représentant un jour de la semaine en constante Calendar.DAY_OF_WEEK
     *
     * @param jour Jour de la semaine (Lundi, Mardi, etc.)
     * @return Constante Calendar.DAY_OF_WEEK correspondante
     */
    private int getDayOfWeekFromString(String jour) {
        switch (jour.toLowerCase()) {
            case "lundi": return Calendar.MONDAY;
            case "mardi": return Calendar.TUESDAY;
            case "mercredi": return Calendar.WEDNESDAY;
            case "jeudi": return Calendar.THURSDAY;
            case "vendredi": return Calendar.FRIDAY;
            case "samedi": return Calendar.SATURDAY;
            case "dimanche": return Calendar.SUNDAY;
            default: return Calendar.MONDAY; // Par défaut
        }
    }
}