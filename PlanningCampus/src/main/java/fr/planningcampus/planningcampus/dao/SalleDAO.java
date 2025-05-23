package fr.planningcampus.planningcampus.dao;

import fr.planningcampus.planningcampus.model.Salle;
import fr.planningcampus.planningcampus.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Classe DAO pour les opérations CRUD sur les salles
 */
public class SalleDAO {

    /**
     * Récupère toutes les salles de la base de données
     *
     * @return Liste des salles
     */
    public List<Salle> getAllSalles() {
        List<Salle> salles = new ArrayList<>();
        String query = "SELECT * FROM salle";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                int capacite = rs.getInt("capacite");
                String localisation = rs.getString("localisation");
                String equipementsStr = rs.getString("equipements");

                Salle salle = new Salle(id, nom, capacite, localisation);

                // Gestion des équipements
                if (equipementsStr != null && !equipementsStr.isEmpty()) {
                    List<String> equipements = Arrays.asList(equipementsStr.split(","));
                    for (String equipement : equipements) {
                        salle.ajouterEquipement(equipement.trim());
                    }
                }

                salles.add(salle);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des salles: " + e.getMessage());
        }

        return salles;
    }

    /**
     * Récupère une salle par son ID
     *
     * @param id ID de la salle
     * @return Salle trouvée ou null
     */
    public Salle getSalleById(int id) {
        String query = "SELECT * FROM salle WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("nom");
                    int capacite = rs.getInt("capacite");
                    String localisation = rs.getString("localisation");
                    String equipementsStr = rs.getString("equipements");

                    Salle salle = new Salle(id, nom, capacite, localisation);

                    // Gestion des équipements
                    if (equipementsStr != null && !equipementsStr.isEmpty()) {
                        List<String> equipements = Arrays.asList(equipementsStr.split(","));
                        for (String equipement : equipements) {
                            salle.ajouterEquipement(equipement.trim());
                        }
                    }

                    return salle;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la salle: " + e.getMessage());
        }

        return null;
    }

    /**
     * Récupère les salles avec un équipement spécifique
     *
     * @param equipement L'équipement recherché
     * @return Liste des salles avec cet équipement
     */
    public List<Salle> getSallesByEquipement(String equipement) {
        List<Salle> salles = new ArrayList<>();
        String query = "SELECT s.* FROM salle s " +
                "JOIN equipement_salle es ON s.id = es.id_salle " +
                "WHERE es.equipement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, equipement);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    int capacite = rs.getInt("capacite");
                    String localisation = rs.getString("localisation");

                    Salle salle = new Salle(id, nom, capacite, localisation);

                    // Charger tous les équipements de la salle
                    salle.setEquipements(getEquipementsBySalle(id));

                    salles.add(salle);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des salles par équipement: " + e.getMessage());
        }

        return salles;
    }

    /**
     * Ajoute une salle dans la base de données
     *
     * @param salle Salle à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addSalle(Salle salle) {
        String query = "INSERT INTO salle (nom, capacite, localisation, equipements) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, salle.getNom());
            pstmt.setInt(2, salle.getCapacite());
            pstmt.setString(3, salle.getLocalisation());

            // Conversion de la liste d'équipements en chaîne
            StringBuilder equipementsStr = new StringBuilder();
            List<String> equipements = salle.getEquipements();
            for (int i = 0; i < equipements.size(); i++) {
                equipementsStr.append(equipements.get(i));
                if (i < equipements.size() - 1) {
                    equipementsStr.append(",");
                }
            }
            pstmt.setString(4, equipementsStr.toString());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        salle.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la salle: " + e.getMessage());
        }

        return false;
    }

    /**
     * Met à jour une salle dans la base de données
     *
     * @param salle Salle à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateSalle(Salle salle) {
        String query = "UPDATE salle SET nom = ?, capacite = ?, localisation = ?, equipements = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, salle.getNom());
            pstmt.setInt(2, salle.getCapacite());
            pstmt.setString(3, salle.getLocalisation());

            // Conversion de la liste d'équipements en chaîne
            StringBuilder equipementsStr = new StringBuilder();
            List<String> equipements = salle.getEquipements();
            for (int i = 0; i < equipements.size(); i++) {
                equipementsStr.append(equipements.get(i));
                if (i < equipements.size() - 1) {
                    equipementsStr.append(",");
                }
            }
            pstmt.setString(4, equipementsStr.toString());

            pstmt.setInt(5, salle.getId());

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la salle: " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime une salle de la base de données
     *
     * @param id ID de la salle à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteSalle(int id) {
        String query = "DELETE FROM salle WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la salle: " + e.getMessage());
        }

        return false;
    }

    /**
     * Vérifie si une salle est disponible à un horaire donné
     *
     * @param salleId ID de la salle
     * @param horaireId ID de l'horaire
     * @return true si la salle est disponible, false sinon
     */
    public boolean isSalleDisponible(int salleId, int horaireId) {
        String query = "SELECT COUNT(*) FROM cours c " +
                "JOIN horaire h ON c.id_seance = h.id " +
                "WHERE c.id_salle = ? AND h.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, salleId);
            pstmt.setInt(2, horaireId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // Disponible si aucun cours n'utilise cette salle à cet horaire
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de disponibilité de salle: " + e.getMessage());
        }

        return false;
    }

    /**
     * Renvoie le taux d'occupation d'une salle (nombre d'heures utilisées / nombre d'heures totales)
     *
     * @param salleId ID de la salle
     * @return Taux d'occupation (0.0 à 1.0)
     */
    public double getTauxOccupation(int salleId) {
        String query = "SELECT COUNT(*) FROM cours WHERE id_salle = ?";
        final int HEURES_TOTALES_SEMAINE = 40; // 8 heures par jour, 5 jours

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, salleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int nombreCours = rs.getInt(1);
                    // On suppose qu'un cours dure 2 heures en moyenne
                    double heuresUtilisees = nombreCours * 2.0;
                    return heuresUtilisees / HEURES_TOTALES_SEMAINE;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du taux d'occupation: " + e.getMessage());
        }

        return 0.0;
    }

    /**
     * Récupère les équipements d'une salle
     *
     * @param idSalle ID de la salle
     * @return Liste des équipements
     */
    public List<String> getEquipementsBySalle(int idSalle) {
        List<String> equipements = new ArrayList<>();
        String query = "SELECT equipement FROM equipement_salle WHERE id_salle = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idSalle);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String equipement = rs.getString("equipement");
                    equipements.add(equipement);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des équipements: " + e.getMessage());
        }

        return equipements;
    }

    /**
     * Ajoute un équipement à une salle
     *
     * @param idSalle ID de la salle
     * @param equipement Nom de l'équipement
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEquipementToSalle(int idSalle, String equipement) {
        String query = "INSERT INTO equipement_salle (id_salle, equipement) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idSalle);
            pstmt.setString(2, equipement);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'équipement: " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un équipement d'une salle
     *
     * @param idSalle ID de la salle
     * @param equipement Nom de l'équipement
     * @return true si la suppression a réussi, false sinon
     */
    public boolean removeEquipementFromSalle(int idSalle, String equipement) {
        String query = "DELETE FROM equipement_salle WHERE id_salle = ? AND equipement = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idSalle);
            pstmt.setString(2, equipement);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'équipement: " + e.getMessage());
        }

        return false;
    }
}