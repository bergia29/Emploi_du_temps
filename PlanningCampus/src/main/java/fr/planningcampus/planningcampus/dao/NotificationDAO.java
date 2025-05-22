package fr.planningcampus.planningcampus.dao;

import fr.planningcampus.planningcampus.utils.DatabaseConnection;
import fr.planningcampus.planningcampus.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès aux données pour les notifications
 */
public class NotificationDAO {

    /**
     * Ajoute une notification dans la base de données
     *
     * @param notification Notification à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addNotification(Notification notification) {
        String query = "INSERT INTO notification (id_destinataire, id_expediteur, contenu, date, lue) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, notification.getIdDestinataire());
            pstmt.setInt(2, notification.getIdExpediteur());
            pstmt.setString(3, notification.getContenu());
            pstmt.setTimestamp(4, new Timestamp(notification.getDate().getTime()));
            pstmt.setBoolean(5, notification.isLue());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        notification.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la notification: " + e.getMessage());
        }

        return false;
    }

    /**
     * Met à jour une notification dans la base de données
     *
     * @param notification Notification à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateNotification(Notification notification) {
        String query = "UPDATE notification SET id_destinataire = ?, id_expediteur = ?, contenu = ?, date = ?, lue = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, notification.getIdDestinataire());
            pstmt.setInt(2, notification.getIdExpediteur());
            pstmt.setString(3, notification.getContenu());
            pstmt.setTimestamp(4, new Timestamp(notification.getDate().getTime()));
            pstmt.setBoolean(5, notification.isLue());
            pstmt.setInt(6, notification.getId());

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la notification: " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime une notification de la base de données
     *
     * @param id ID de la notification à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteNotification(int id) {
        String query = "DELETE FROM notification WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la notification: " + e.getMessage());
        }

        return false;
    }

    /**
     * Récupère une notification par son ID
     *
     * @param id ID de la notification
     * @return Notification correspondante ou null si non trouvée
     */
    public Notification getNotificationById(int id) {
        String query = "SELECT * FROM notification WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractNotificationFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la notification: " + e.getMessage());
        }

        return null;
    }

    /**
     * Récupère toutes les notifications destinées à un utilisateur
     *
     * @param idDestinataire ID de l'utilisateur destinataire
     * @return Liste des notifications
     */
    public List<Notification> getNotificationsByDestinataire(int idDestinataire) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification WHERE id_destinataire = ? ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idDestinataire);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(extractNotificationFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notifications: " + e.getMessage());
        }

        return notifications;
    }

    /**
     * Récupère toutes les notifications envoyées par un utilisateur
     *
     * @param idExpediteur ID de l'utilisateur expéditeur
     * @return Liste des notifications
     */
    public List<Notification> getNotificationsByExpediteur(int idExpediteur) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification WHERE id_expediteur = ? ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idExpediteur);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(extractNotificationFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notifications: " + e.getMessage());
        }

        return notifications;
    }

    /**
     * Récupère toutes les notifications non lues destinées à un utilisateur
     *
     * @param idDestinataire ID de l'utilisateur destinataire
     * @return Liste des notifications non lues
     */
    public List<Notification> getUnreadNotificationsByDestinataire(int idDestinataire) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification WHERE id_destinataire = ? AND lue = FALSE ORDER BY date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idDestinataire);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    notifications.add(extractNotificationFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des notifications non lues: " + e.getMessage());
        }

        return notifications;
    }

    /**
     * Marque toutes les notifications d'un destinataire comme lues
     *
     * @param idDestinataire ID du destinataire
     * @return Nombre de notifications mises à jour
     */
    public int markAllNotificationsAsRead(int idDestinataire) {
        String query = "UPDATE notification SET lue = TRUE WHERE id_destinataire = ? AND lue = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idDestinataire);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors du marquage des notifications comme lues: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Extrait une notification d'un ResultSet
     *
     * @param rs ResultSet contenant les données de la notification
     * @return Notification créée à partir du ResultSet
     * @throws SQLException En cas d'erreur lors de l'accès aux données
     */
    private Notification extractNotificationFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int idDestinataire = rs.getInt("id_destinataire");
        int idExpediteur = rs.getInt("id_expediteur");
        String contenu = rs.getString("contenu");
        Timestamp timestamp = rs.getTimestamp("date");
        boolean lue = rs.getBoolean("lue");

        return new Notification(id, idDestinataire, idExpediteur, contenu, timestamp, lue);
    }

    /**
     * Vérifie si la colonne id_expediteur existe dans la table notification
     * et la crée si elle n'existe pas
     */
    public void verifierColonneIdExpediteur() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Vérifier si la colonne existe déjà
            boolean colonneExiste = false;

            try (ResultSet rs = conn.getMetaData().getColumns(null, null, "notification", "id_expediteur")) {
                colonneExiste = rs.next();
            }

            // Si la colonne n'existe pas, l'ajouter
            if (!colonneExiste) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate("ALTER TABLE notification ADD COLUMN id_expediteur INT DEFAULT NULL");
                    stmt.executeUpdate("ALTER TABLE notification ADD FOREIGN KEY (id_expediteur) REFERENCES utilisateur(id) ON DELETE SET NULL");
                    System.out.println("Colonne id_expediteur ajoutée à la table notification");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification/création de la colonne id_expediteur: " + e.getMessage());
        }
    }
}