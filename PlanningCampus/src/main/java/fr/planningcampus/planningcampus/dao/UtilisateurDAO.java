package fr.planningcampus.planningcampus.dao;

import fr.planningcampus.planningcampus.model.Administrateur;
import fr.planningcampus.planningcampus.model.Enseignant;
import fr.planningcampus.planningcampus.model.Etudiant;
import fr.planningcampus.planningcampus.model.Utilisateur;
import fr.planningcampus.planningcampus.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO pour les opérations CRUD sur les utilisateurs
 */
public class UtilisateurDAO {

    /**
     * Récupère tous les groupes d'étudiants distincts
     *
     * @return Liste des noms de groupes
     */
    public List<String> getAllGroupes() {
        List<String> groupes = new ArrayList<>();
        String query = "SELECT DISTINCT groupe FROM etudiant WHERE groupe IS NOT NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String groupe = rs.getString("groupe");
                if (groupe != null && !groupe.isEmpty()) {
                    groupes.add(groupe);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des groupes: " + e.getMessage());
        }

        return groupes;
    }

    /**
     * Récupère tous les étudiants d'un groupe
     *
     * @param groupe Nom du groupe
     * @return Liste des étudiants du groupe
     */
    public List<Etudiant> getEtudiantsByGroupe(String groupe) {
        List<Etudiant> etudiants = new ArrayList<>();
        String query = "SELECT u.id, u.nom, u.prenom, u.email, u.mot_de_passe, e.groupe FROM utilisateur u JOIN etudiant e ON u.id = e.id WHERE e.groupe = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, groupe);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String email = rs.getString("email");
                    String motDePasse = rs.getString("mot_de_passe");
                    String groupeEtudiant = rs.getString("groupe");

                    Etudiant etudiant = new Etudiant(id, nom, prenom, email, motDePasse, groupeEtudiant);

                    etudiants.add(etudiant);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des étudiants du groupe: " + e.getMessage());
        }

        return etudiants;
    }

    /**
     * Compte le nombre d'étudiants dans un groupe
     *
     * @param groupe Nom du groupe
     * @return Nombre d'étudiants dans le groupe
     */
    public int countEtudiantsByGroupe(String groupe) {
        String query = "SELECT COUNT(*) FROM etudiant WHERE groupe = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, groupe);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage des étudiants du groupe: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Récupère les IDs des étudiants d'un groupe
     *
     * @param groupe Nom du groupe
     * @return Liste des IDs des étudiants
     */
    public List<Integer> getEtudiantsIdsByGroupe(String groupe) {
        List<Integer> ids = new ArrayList<>();
        String query = "SELECT id FROM etudiant WHERE groupe = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, groupe);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("id"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des IDs des étudiants: " + e.getMessage());
        }

        return ids;
    }

    /**
     * Récupère les groupes d'étudiants associés à un cours
     *
     * @param coursId ID du cours
     * @return Liste des noms de groupes
     */
    public List<String> getGroupesByCours(int coursId) {
        List<String> groupes = new ArrayList<>();
        String query = "SELECT DISTINCT e.groupe FROM etudiant e " +
                "JOIN etudiant_cours ec ON e.id = ec.id_etudiant " +
                "WHERE ec.id_cours = ? AND e.groupe IS NOT NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, coursId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String groupe = rs.getString("groupe");
                    if (groupe != null && !groupe.isEmpty()) {
                        groupes.add(groupe);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des groupes du cours: " + e.getMessage());
        }

        return groupes;
    }

    /**
     * Récupère tous les administrateurs
     *
     * @return Liste des administrateurs
     */
    public List<Utilisateur> getAllAdministrateurs() {
        List<Utilisateur> administrateurs = new ArrayList<>();
        String query = "SELECT u.id, u.nom, u.prenom, u.email, u.motDePasse FROM utilisateur u WHERE u.type = 'admin'";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("motDePasse");

                Administrateur admin = new Administrateur(id, nom, prenom, email, motDePasse);
                administrateurs.add(admin);
            }

            // Si aucun administrateur n'est trouvé, ajouter un administrateur par défaut
            if (administrateurs.isEmpty()) {
                System.out.println("Aucun administrateur trouvé, création d'un administrateur par défaut");
                Administrateur adminDefaut = new Administrateur(0, "Admin", "Default", "admin@timeplanner.com", "admin123");
                if (addUtilisateur(adminDefaut)) {
                    administrateurs.add(adminDefaut);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des administrateurs: " + e.getMessage());
        }

        return administrateurs;
    }

    /**
     * Récupère tous les utilisateurs de la base de données
     *
     * @return Liste des utilisateurs
     */
    public List<Utilisateur> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String motDePasse = rs.getString("motDePasse");
                String type = rs.getString("type");

                Utilisateur utilisateur = null;

                switch (type) {
                    case "admin":
                        utilisateur = new Administrateur(id, nom, prenom, email, motDePasse);
                        break;
                    case "enseignant":
                        utilisateur = new Enseignant(id, nom, prenom, email, motDePasse, null);
                        break;
                    case "etudiant":
                        utilisateur = new Etudiant(id, nom, prenom, email, motDePasse, null);
                        break;
                }

                if (utilisateur != null) {
                    utilisateurs.add(utilisateur);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs: " + e.getMessage());
        }

        return utilisateurs;
    }

    /**
     * Récupère un utilisateur par son ID
     *
     * @param id ID de l'utilisateur
     * @return Utilisateur trouvé ou null
     */
    public Utilisateur getUtilisateurById(int id) {
        String query = "SELECT * FROM utilisateur WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String email = rs.getString("email");
                    String motDePasse = rs.getString("motDePasse");
                    String type = rs.getString("type");

                    switch (type) {
                        case "admin":
                            return new Administrateur(id, nom, prenom, email, motDePasse);
                        case "enseignant":
                            return new Enseignant(id, nom, prenom, email, motDePasse, null);
                        case "etudiant":
                            return new Etudiant(id, nom, prenom, email, motDePasse, null);
                        default:
                            return null;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
        }

        return null;
    }

    /**
     * Authentifie un utilisateur
     *
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @return Utilisateur authentifié ou null
     */
    public Utilisateur authentifier(String email, String motDePasse) {
        String query = "SELECT * FROM utilisateur WHERE email = ? AND motDePasse = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            pstmt.setString(2, motDePasse);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String type = rs.getString("type");

                    switch (type) {
                        case "admin":
                            return new Administrateur(id, nom, prenom, email, motDePasse);
                        case "enseignant":
                            return new Enseignant(id, nom, prenom, email, motDePasse, null);
                        case "etudiant":
                            return new Etudiant(id, nom, prenom, email, motDePasse, null);
                        default:
                            return null;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
        }

        return null;
    }

    /**
     * Ajoute un utilisateur dans la base de données
     *
     * @param utilisateur Utilisateur à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addUtilisateur(Utilisateur utilisateur) {
        String query = "INSERT INTO utilisateur (nom, prenom, email, motDePasse, type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getEmail());
            pstmt.setString(4, utilisateur.getMotDePasse());

            String type = "";
            if (utilisateur instanceof Administrateur) {
                type = "admin";
            } else if (utilisateur instanceof Enseignant) {
                type = "enseignant";
            } else if (utilisateur instanceof Etudiant) {
                type = "etudiant";
            }

            pstmt.setString(5, type);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        utilisateur.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        }

        return false;
    }

    /**
     * Met à jour un utilisateur dans la base de données
     *
     * @param utilisateur Utilisateur à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateUtilisateur(Utilisateur utilisateur) {
        String query = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, motDePasse = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getPrenom());
            pstmt.setString(3, utilisateur.getEmail());
            pstmt.setString(4, utilisateur.getMotDePasse());
            pstmt.setInt(5, utilisateur.getId());

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un utilisateur de la base de données
     *
     * @param id ID de l'utilisateur à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteUtilisateur(int id) {
        String query = "DELETE FROM utilisateur WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
        }

        return false;
    }
}