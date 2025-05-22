package fr.planningcampus.planningcampus.dao;

import fr.planningcampus.planningcampus.model.Cours;
import fr.planningcampus.planningcampus.model.Enseignant;
import fr.planningcampus.planningcampus.model.Etudiant;
import fr.planningcampus.planningcampus.model.Seance;
import fr.planningcampus.planningcampus.model.Salle;
import fr.planningcampus.planningcampus.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe DAO pour les opérations CRUD sur les cours
 */
public class CoursDAO {

    private EnseignantDAO enseignantDAO = new EnseignantDAO();
    private SalleDAO salleDAO = new SalleDAO();
    private SeanceDAO seanceDAO = new SeanceDAO();

    /**
     * Récupère tous les cours de la base de données
     *
     * @return Liste des cours
     */
    public List<Cours> getAllCours() {
        List<Cours> cours = new ArrayList<>();
        String query = "SELECT * FROM cours";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Récupérer toutes les données nécessaires d'abord
            List<Object[]> coursData = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String matiere = rs.getString("matiere");
                int idEnseignant = rs.getInt("id_enseignant");
                int idHoraire = rs.getInt("id_horaire");
                int idSalle = rs.getInt("id_salle");

                coursData.add(new Object[]{id, matiere, idEnseignant, idHoraire, idSalle});
            }

            // Maintenant traiter les données récupérées
            for (Object[] data : coursData) {
                int id = (int) data[0];
                String matiere = (String) data[1];
                int idEnseignant = (int) data[2];
                int idHoraire = (int) data[3];
                int idSalle = (int) data[4];

                Enseignant enseignant = enseignantDAO.getEnseignantById(idEnseignant);
                Seance seance = seanceDAO.getHoraireById(idHoraire);
                Salle salle = salleDAO.getSalleById(idSalle);

                Cours c = new Cours(id, matiere, enseignant, seance, salle);
                cours.add(c);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des cours: " + e.getMessage());
        }

        return cours;
    }

    /**
     * Récupère un cours par son ID
     *
     * @param id ID du cours
     * @return Cours trouvé ou null
     */
    public Cours getCoursById(int id) {
        String query = "SELECT * FROM cours WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String matiere = rs.getString("matiere");
                    int idEnseignant = rs.getInt("id_enseignant");
                    int idHoraire = rs.getInt("id_horaire");
                    int idSalle = rs.getInt("id_salle");

                    // Fermeture du ResultSet avant d'appeler d'autres méthodes qui utilisent des connexions
                    rs.close();

                    Enseignant enseignant = enseignantDAO.getEnseignantById(idEnseignant);
                    Seance seance = seanceDAO.getHoraireById(idHoraire);
                    Salle salle = salleDAO.getSalleById(idSalle);

                    return new Cours(id, matiere, enseignant, seance, salle);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du cours: " + e.getMessage());
        }

        return null;
    }

    /**
     * Récupère les cours d'un enseignant
     *
     * @param idEnseignant ID de l'enseignant
     * @return Liste des cours de l'enseignant
     */
    public List<Cours> getCoursByEnseignant(int idEnseignant) {
        List<Cours> cours = new ArrayList<>();
        String query = "SELECT * FROM cours WHERE id_enseignant = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idEnseignant);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Récupérer toutes les données nécessaires d'abord
                List<Object[]> coursData = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String matiere = rs.getString("matiere");
                    int idHoraire = rs.getInt("id_horaire");
                    int idSalle = rs.getInt("id_salle");

                    coursData.add(new Object[]{id, matiere, idHoraire, idSalle});
                }

                // Fermeture du ResultSet avant d'appeler d'autres méthodes
                rs.close();

                // Maintenant traiter les données récupérées
                Enseignant enseignant = enseignantDAO.getEnseignantById(idEnseignant);

                for (Object[] data : coursData) {
                    int id = (int) data[0];
                    String matiere = (String) data[1];
                    int idHoraire = (int) data[2];
                    int idSalle = (int) data[3];

                    Seance seance = seanceDAO.getHoraireById(idHoraire);
                    Salle salle = salleDAO.getSalleById(idSalle);

                    Cours c = new Cours(id, matiere, enseignant, seance, salle);
                    cours.add(c);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des cours de l'enseignant: " + e.getMessage());
        }

        return cours;
    }

    /**
     * Récupère les cours d'un étudiant
     *
     * @param idEtudiant ID de l'étudiant
     * @return Liste des cours de l'étudiant
     */
    public List<Cours> getCoursByEtudiant(int idEtudiant) {
        List<Cours> cours = new ArrayList<>();
        String query = "SELECT c.* FROM cours c " +
                "JOIN etudiant_cours ec ON c.id = ec.id_cours " +
                "WHERE ec.id_etudiant = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idEtudiant);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Récupérer toutes les données nécessaires d'abord
                List<Object[]> coursData = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String matiere = rs.getString("matiere");
                    int idEnseignant = rs.getInt("id_enseignant");
                    int idHoraire = rs.getInt("id_horaire");
                    int idSalle = rs.getInt("id_salle");

                    coursData.add(new Object[]{id, matiere, idEnseignant, idHoraire, idSalle});
                }

                // Fermeture du ResultSet avant d'appeler d'autres méthodes
                rs.close();

                // Maintenant traiter les données récupérées
                for (Object[] data : coursData) {
                    int id = (int) data[0];
                    String matiere = (String) data[1];
                    int idEnseignant = (int) data[2];
                    int idHoraire = (int) data[3];
                    int idSalle = (int) data[4];

                    Enseignant enseignant = enseignantDAO.getEnseignantById(idEnseignant);
                    Seance seance = seanceDAO.getHoraireById(idHoraire);
                    Salle salle = salleDAO.getSalleById(idSalle);

                    Cours c = new Cours(id, matiere, enseignant, seance, salle);
                    cours.add(c);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des cours de l'étudiant: " + e.getMessage());
        }

        return cours;
    }

    /**
     * Ajoute un cours dans la base de données
     *
     * @param cours Cours à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addCours(Cours cours) {
        String query = "INSERT INTO cours (matiere, id_enseignant, id_horaire, id_salle) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, cours.getMatiere());
            pstmt.setInt(2, cours.getEnseignant().getId());
            pstmt.setInt(3, cours.getHoraire().getId());
            pstmt.setInt(4, cours.getSalle().getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int coursId = generatedKeys.getInt(1);
                        cours.setId(coursId);

                        // Ajouter les étudiants au cours
                        if (cours.getEtudiants() != null && !cours.getEtudiants().isEmpty()) {
                            addEtudiantsToCours(coursId, cours.getEtudiants());
                        }

                        return true;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du cours: " + e.getMessage());
        }

        return false;
    }

    /**
     * Ajoute des étudiants à un cours
     *
     * @param coursId ID du cours
     * @param etudiants Liste des étudiants à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addEtudiantsToCours(int coursId, List<Etudiant> etudiants) {
        String query = "INSERT INTO etudiant_cours (id_etudiant, id_cours) VALUES (?, ?)";
        boolean success = true;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Commencer une transaction
            conn.setAutoCommit(false);

            for (Etudiant etudiant : etudiants) {
                pstmt.setInt(1, etudiant.getId());
                pstmt.setInt(2, coursId);
                pstmt.addBatch();
            }

            int[] affectedRows = pstmt.executeBatch();

            // Vérifier si tous les ajouts ont réussi
            for (int rows : affectedRows) {
                if (rows <= 0) {
                    success = false;
                    break;
                }
            }

            if (success) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.setAutoCommit(true);

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout des étudiants au cours: " + e.getMessage());
            success = false;
        }

        return success;
    }

    /**
     * Ajoute tous les étudiants d'un groupe à un cours
     *
     * @param coursId ID du cours
     * @param groupe Nom du groupe d'étudiants
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addGroupeToCours(int coursId, String groupe) {
        // Récupérer tous les étudiants du groupe
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        List<Etudiant> etudiants = utilisateurDAO.getEtudiantsByGroupe(groupe);

        if (etudiants.isEmpty()) {
            System.err.println("Aucun étudiant trouvé dans le groupe: " + groupe);
            return false;
        }

        // Ajouter les étudiants au cours
        return addEtudiantsToCours(coursId, etudiants);
    }

    /**
     * Ajoute plusieurs groupes d'étudiants à un cours
     *
     * @param coursId ID du cours
     * @param groupes Liste des groupes d'étudiants
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addGroupesToCours(int coursId, List<String> groupes) {
        boolean success = true;

        for (String groupe : groupes) {
            boolean result = addGroupeToCours(coursId, groupe);
            if (!result) {
                success = false;
            }
        }

        return success;
    }

    /**
     * Récupère les étudiants d'un cours
     *
     * @param coursId ID du cours
     * @return Liste des étudiants du cours
     */
    public List<Etudiant> getEtudiantsByCours(int coursId) {
        List<Etudiant> etudiants = new ArrayList<>();
        String query = "SELECT u.id, u.nom, u.prenom, u.email, u.motDePasse, e.groupe FROM utilisateur u " +
                "JOIN etudiant e ON u.id = e.id " +
                "JOIN etudiant_cours ec ON u.id = ec.id_etudiant " +
                "WHERE ec.id_cours = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, coursId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String email = rs.getString("email");
                    String motDePasse = rs.getString("motDePasse");
                    String groupe = rs.getString("groupe");

                    Etudiant etudiant = new Etudiant(id, nom, prenom, email, motDePasse, groupe);
                    etudiants.add(etudiant);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des étudiants du cours: " + e.getMessage());
        }

        return etudiants;
    }

    /**
     * Vérifie si un enseignant a déjà un cours à un horaire spécifique ou un horaire qui chevauche
     *
     * @param enseignantId ID de l'enseignant
     * @param horaireId ID de l'horaire à vérifier
     * @return true si un conflit existe, false sinon
     */
    public boolean enseignantHasCoursAtHoraire(int enseignantId, int horaireId) {
        boolean conflit = false;
        SeanceDAO seanceDAO = new SeanceDAO();

        // Récupérer l'horaire à vérifier
        Seance seanceToCheck = seanceDAO.getHoraireById(horaireId);
        if (seanceToCheck == null) {
            return false;
        }

        // Récupérer tous les cours de l'enseignant
        List<Cours> coursEnseignant = getCoursByEnseignant(enseignantId);

        // Pour chaque cours de l'enseignant, vérifier si l'horaire est en conflit
        for (Cours cours : coursEnseignant) {
            if (cours.getHoraire() != null && cours.getHoraire().getId() != horaireId) {
                // Vérifier si les deux horaires sont le même jour
                boolean memeJour = cours.getHoraire().getJour().equals(seanceToCheck.getJour());

                // Vérifier si c'est la même semaine
                boolean memeSemaine = cours.getHoraire().getSemaine() == seanceToCheck.getSemaine();

                // Vérifier la date si elle est définie
                boolean memeDate = true;

                // Si les deux horaires ont une date définie, comparer les dates
                if (cours.getHoraire().getDate() != null && seanceToCheck.getDate() != null) {
                    java.util.Calendar cal1 = java.util.Calendar.getInstance();
                    cal1.setTime(cours.getHoraire().getDate());

                    java.util.Calendar cal2 = java.util.Calendar.getInstance();
                    cal2.setTime(seanceToCheck.getDate());

                    // Vérifier si c'est le même jour, mois et année
                    memeDate = cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                            cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                            cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
                } else if (cours.getHoraire().getDate() != null || seanceToCheck.getDate() != null) {
                    // Si un seul des deux horaires a une date définie, ils ne sont pas le même jour
                    memeDate = false;
                }

                if ((memeJour && memeSemaine) || memeDate) {
                    // Vérifier si les plages horaires se chevauchent
                    Date debutCours = cours.getHoraire().getHeureDebut();
                    Date finCours = cours.getHoraire().getHeureFin();
                    Date debutCheck = seanceToCheck.getHeureDebut();
                    Date finCheck = seanceToCheck.getHeureFin();

                    // Vérifier les 4 cas de chevauchement possibles:
                    // Cas 1: Le début du nouveau cours est pendant un cours existant
                    if (debutCheck.after(debutCours) && debutCheck.before(finCours)) {
                        conflit = true;
                        break;
                    }

                    // Cas 2: La fin du nouveau cours est pendant un cours existant
                    if (finCheck.after(debutCours) && finCheck.before(finCours)) {
                        conflit = true;
                        break;
                    }

                    // Cas 3: Le nouveau cours englobe complètement un cours existant
                    if (debutCheck.before(debutCours) && finCheck.after(finCours)) {
                        conflit = true;
                        break;
                    }

                    // Cas 4: Le nouveau cours est complètement dans un cours existant
                    if (debutCheck.equals(debutCours) || finCheck.equals(finCours)) {
                        conflit = true;
                        break;
                    }
                }
            }
        }

        return conflit;
    }

    /**
     * Vérifie si une salle est déjà occupée à un horaire spécifique ou un horaire qui chevauche
     *
     * @param salleId ID de la salle
     * @param horaireId ID de l'horaire à vérifier
     * @return true si la salle est déjà occupée, false sinon
     */
    public boolean salleIsOccupiedAtHoraire(int salleId, int horaireId) {
        boolean conflit = false;
        SeanceDAO seanceDAO = new SeanceDAO();

        // Récupérer l'horaire à vérifier
        Seance seanceToCheck = seanceDAO.getHoraireById(horaireId);
        if (seanceToCheck == null) {
            return false;
        }

        // Récupérer tous les cours dans cette salle
        List<Cours> coursDansSalle = getCoursBySalle(salleId);

        // Pour chaque cours dans la salle, vérifier si l'horaire est en conflit
        for (Cours cours : coursDansSalle) {
            if (cours.getHoraire() != null && cours.getHoraire().getId() != horaireId) {
                // Vérifier si les deux horaires sont le même jour
                boolean memeJour = cours.getHoraire().getJour().equals(seanceToCheck.getJour());

                // Vérifier si c'est la même semaine
                boolean memeSemaine = cours.getHoraire().getSemaine() == seanceToCheck.getSemaine();

                // Vérifier la date si elle est définie
                boolean memeDate = true;

                // Si les deux horaires ont une date définie, comparer les dates
                if (cours.getHoraire().getDate() != null && seanceToCheck.getDate() != null) {
                    java.util.Calendar cal1 = java.util.Calendar.getInstance();
                    cal1.setTime(cours.getHoraire().getDate());

                    java.util.Calendar cal2 = java.util.Calendar.getInstance();
                    cal2.setTime(seanceToCheck.getDate());

                    // Vérifier si c'est le même jour, mois et année
                    memeDate = cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                            cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                            cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
                } else if (cours.getHoraire().getDate() != null || seanceToCheck.getDate() != null) {
                    // Si un seul des deux horaires a une date définie, ils ne sont pas le même jour
                    memeDate = false;
                }

                if ((memeJour && memeSemaine) || memeDate) {
                    // Vérifier si les plages horaires se chevauchent
                    Date debutCours = cours.getHoraire().getHeureDebut();
                    Date finCours = cours.getHoraire().getHeureFin();
                    Date debutCheck = seanceToCheck.getHeureDebut();
                    Date finCheck = seanceToCheck.getHeureFin();

                    // Vérifier les 4 cas de chevauchement possibles:
                    // Cas 1: Le début du nouveau cours est pendant un cours existant
                    if (debutCheck.after(debutCours) && debutCheck.before(finCours)) {
                        conflit = true;
                        break;
                    }

                    // Cas 2: La fin du nouveau cours est pendant un cours existant
                    if (finCheck.after(debutCours) && finCheck.before(finCours)) {
                        conflit = true;
                        break;
                    }

                    // Cas 3: Le nouveau cours englobe complètement un cours existant
                    if (debutCheck.before(debutCours) && finCheck.after(finCours)) {
                        conflit = true;
                        break;
                    }

                    // Cas 4: Le nouveau cours est complètement dans un cours existant
                    if (debutCheck.equals(debutCours) || finCheck.equals(finCours)) {
                        conflit = true;
                        break;
                    }
                }
            }
        }

        return conflit;
    }

    /**
     * Récupère les salles disponibles pour un créneau horaire et une capacité minimale
     *
     * @param capaciteMin Capacité minimale requise
     * @param horaireId ID de l'horaire
     * @return Liste des salles disponibles
     */
    public List<Salle> getSallesDisponibles(int capaciteMin, int horaireId) {
        List<Salle> sallesDisponibles = new ArrayList<>();

        // Récupérer toutes les salles ayant la capacité minimale requise
        SalleDAO salleDAO = new SalleDAO();
        List<Salle> salles = salleDAO.getAllSalles();

        for (Salle salle : salles) {
            // Vérifier si la salle a la capacité suffisante
            if (salle.getCapacite() >= capaciteMin) {
                // Vérifier si la salle est disponible à cet horaire
                if (!salleIsOccupiedAtHoraire(salle.getId(), horaireId)) {
                    sallesDisponibles.add(salle);
                }
            }
        }

        return sallesDisponibles;
    }

    /**
     * Met à jour un cours dans la base de données
     *
     * @param cours Cours à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateCours(Cours cours) {
        String query = "UPDATE cours SET matiere = ?, id_enseignant = ?, id_horaire = ?, id_salle = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, cours.getMatiere());
            pstmt.setInt(2, cours.getEnseignant().getId());
            pstmt.setInt(3, cours.getHoraire().getId());
            pstmt.setInt(4, cours.getSalle().getId());
            pstmt.setInt(5, cours.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Mettre à jour les étudiants
                if (cours.getEtudiants() != null) {
                    // Supprimer les anciennes relations
                    removeAllEtudiantsFromCours(cours.getId());
                    // Ajouter les nouvelles relations
                    addEtudiantsToCours(cours.getId(), cours.getEtudiants());
                }
                return true;
            }

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du cours: " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime toutes les relations entre un cours et ses étudiants
     *
     * @param coursId ID du cours
     * @return true si la suppression a réussi, false sinon
     */
    public boolean removeAllEtudiantsFromCours(int coursId) {
        String query = "DELETE FROM etudiant_cours WHERE id_cours = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, coursId);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression des étudiants du cours: " + e.getMessage());
        }

        return false;
    }

    /**
     * Supprime un cours de la base de données
     *
     * @param id ID du cours à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteCours(int id) {
        // D'abord supprimer les relations avec les étudiants
        removeAllEtudiantsFromCours(id);

        // Ensuite supprimer le cours
        String query = "DELETE FROM cours WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du cours: " + e.getMessage());
        }

        return false;
    }

    /**
     * Récupère les cours d'une salle
     *
     * @param idSalle ID de la salle
     * @return Liste des cours de la salle
     */
    public List<Cours> getCoursBySalle(int idSalle) {
        List<Cours> cours = new ArrayList<>();
        String query = "SELECT * FROM cours WHERE id_salle = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, idSalle);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Récupérer toutes les données nécessaires d'abord
                List<Object[]> coursData = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String matiere = rs.getString("matiere");
                    int idEnseignant = rs.getInt("id_enseignant");
                    int idHoraire = rs.getInt("id_horaire");

                    coursData.add(new Object[]{id, matiere, idEnseignant, idHoraire});
                }

                // Fermeture du ResultSet avant d'appeler d'autres méthodes
                rs.close();

                // Maintenant traiter les données récupérées
                Salle salle = salleDAO.getSalleById(idSalle);

                for (Object[] data : coursData) {
                    int id = (int) data[0];
                    String matiere = (String) data[1];
                    int idEnseignant = (int) data[2];
                    int idHoraire = (int) data[3];

                    Enseignant enseignant = enseignantDAO.getEnseignantById(idEnseignant);
                    Seance seance = seanceDAO.getHoraireById(idHoraire);

                    Cours c = new Cours(id, matiere, enseignant, seance, salle);
                    cours.add(c);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des cours de la salle: " + e.getMessage());
        }

        return cours;
    }
}