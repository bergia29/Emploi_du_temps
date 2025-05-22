package fr.planningcampus.planningcampus.dao;

import fr.planningcampus.planningcampus.model.Enseignant;
import fr.planningcampus.planningcampus.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour les opérations CRUD sur les enseignants
 */
public class EnseignantDAO {

    /**
     * Récupère tous les enseignants de la base de données
     *
     * @return Liste des enseignants
     */
    public List<Enseignant> getAllEnseignants() {
        List<Enseignant> enseignants = new ArrayList<>();
        String query = "SELECT u.*, e.datePriseFonction FROM utilisateur u " +
                "JOIN enseignant e ON u.id = e.id " +
                "WHERE u.type = 'enseignant'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("motDePasse");
                Date datePriseFonction = rs.getDate("datePriseFonction");

                Enseignant enseignant = new Enseignant(id, nom, prenom, email, motDePasse, datePriseFonction);
                enseignants.add(enseignant);

                // Récupération des matières de l'enseignant
                String matieresQuery = "SELECT matiere FROM matiere_enseignant WHERE id_enseignant = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(matieresQuery)) {
                    pstmt.setInt(1, id);
                    try (ResultSet matieresRs = pstmt.executeQuery()) {
                        while (matieresRs.next()) {
                            enseignant.ajouterMatiere(matieresRs.getString("matiere"));
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des enseignants: " + e.getMessage());
        }

        return enseignants;
    }

    /**
     * Récupère un enseignant par son ID
     *
     * @param id ID de l'enseignant
     * @return Enseignant trouvé ou null
     */
    public Enseignant getEnseignantById(int id) {
        String query = "SELECT u.*, e.datePriseFonction FROM utilisateur u " +
                "JOIN enseignant e ON u.id = e.id " +
                "WHERE u.id = ? AND u.type = 'enseignant'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String email = rs.getString("email");
                    String motDePasse = rs.getString("motDePasse");
                    Date datePriseFonction = rs.getDate("datePriseFonction");

                    Enseignant enseignant = new Enseignant(id, nom, prenom, email, motDePasse, datePriseFonction);

                    // Récupération des matières de l'enseignant
                    String matieresQuery = "SELECT matiere FROM matiere_enseignant WHERE id_enseignant = ?";
                    try (PreparedStatement matieresStmt = conn.prepareStatement(matieresQuery)) {
                        matieresStmt.setInt(1, id);
                        try (ResultSet matieresRs = matieresStmt.executeQuery()) {
                            while (matieresRs.next()) {
                                enseignant.ajouterMatiere(matieresRs.getString("matiere"));
                            }
                        }
                    }

                    return enseignant;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'enseignant: " + e.getMessage());
        }

        return null;
    }

    /**
     * Ajoute un enseignant dans la base de données
     *
     * @param enseignant Enseignant à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEnseignant(Enseignant enseignant) {
        // Ajout dans la table utilisateur
        String userQuery = "INSERT INTO utilisateur (nom, prenom, email, motDePasse, type) VALUES (?, ?, ?, ?, 'enseignant')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {

            userStmt.setString(1, enseignant.getNom());
            userStmt.setString(2, enseignant.getPrenom());
            userStmt.setString(3, enseignant.getEmail());
            userStmt.setString(4, enseignant.getMotDePasse());

            int affectedRows = userStmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        enseignant.setId(id);

                        // Ajout dans la table enseignant
                        String enseignantQuery = "INSERT INTO enseignant (id, datePriseFonction) VALUES (?, ?)";
                        try (PreparedStatement enseignantStmt = conn.prepareStatement(enseignantQuery)) {
                            enseignantStmt.setInt(1, id);
                            enseignantStmt.setDate(2, new java.sql.Date(enseignant.getDatePriseFonction().getTime()));

                            enseignantStmt.executeUpdate();

                            // Ajout des matières
                            if (enseignant.getMatieres() != null && !enseignant.getMatieres().isEmpty()) {
                                String matiereQuery = "INSERT INTO matiere_enseignant (id_enseignant, matiere) VALUES (?, ?)";
                                try (PreparedStatement matiereStmt = conn.prepareStatement(matiereQuery)) {
                                    for (String matiere : enseignant.getMatieres()) {
                                        matiereStmt.setInt(1, id);
                                        matiereStmt.setString(2, matiere);
                                        matiereStmt.executeUpdate();
                                    }
                                }
                            }

                            return true;
                        }
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'enseignant: " + e.getMessage());
        }

        return false;
    }

    /**
     * Met à jour un enseignant dans la base de données
     *
     * @param enseignant Enseignant à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateEnseignant(Enseignant enseignant) {
        // Mise à jour dans la table utilisateur
        String userQuery = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, motDePasse = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(userQuery)) {

            userStmt.setString(1, enseignant.getNom());
            userStmt.setString(2, enseignant.getPrenom());
            userStmt.setString(3, enseignant.getEmail());
            userStmt.setString(4, enseignant.getMotDePasse());
            userStmt.setInt(5, enseignant.getId());

            int affectedRows = userStmt.executeUpdate();

            if (affectedRows > 0) {
                // Mise à jour dans la table enseignant
                String enseignantQuery = "UPDATE enseignant SET datePriseFonction = ? WHERE id = ?";
                try (PreparedStatement enseignantStmt = conn.prepareStatement(enseignantQuery)) {
                    enseignantStmt.setDate(1, new java.sql.Date(enseignant.getDatePriseFonction().getTime()));
                    enseignantStmt.setInt(2, enseignant.getId());

                    enseignantStmt.executeUpdate();

                    // Suppression des anciennes matières
                    String deleteMatiereQuery = "DELETE FROM matiere_enseignant WHERE id_enseignant = ?";
                    try (PreparedStatement deleteMatiereStmt = conn.prepareStatement(deleteMatiereQuery)) {
                        deleteMatiereStmt.setInt(1, enseignant.getId());
                        deleteMatiereStmt.executeUpdate();

                        // Ajout des nouvelles matières
                        if (enseignant.getMatieres() != null && !enseignant.getMatieres().isEmpty()) {
                            String matiereQuery = "INSERT INTO matiere_enseignant (id_enseignant, matiere) VALUES (?, ?)";
                            try (PreparedStatement matiereStmt = conn.prepareStatement(matiereQuery)) {
                                for (String matiere : enseignant.getMatieres()) {
                                    matiereStmt.setInt(1, enseignant.getId());
                                    matiereStmt.setString(2, matiere);
                                    matiereStmt.executeUpdate();
                                }
                            }
                        }

                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'enseignant: " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un enseignant de la base de données
     *
     * @param id ID de l'enseignant à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteEnseignant(int id) {
        // La suppression en cascade supprimera automatiquement les entrées dans les tables enseignant et matiere_enseignant
        String query = "DELETE FROM utilisateur WHERE id = ? AND type = 'enseignant'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'enseignant: " + e.getMessage());
        }

        return false;
    }
}