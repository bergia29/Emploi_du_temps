package fr.planningcampus.planningcampus.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour la connexion à la base de données
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/planningcampus";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    /**
     * Établit une connexion à la base de données
     *
     * @return Connexion à la base de données
     * @throws SQLException En cas d'erreur de connexion
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion à la base de données établie avec succès.");
            } catch (ClassNotFoundException e) {
                System.err.println("Erreur de chargement du driver MySQL: " + e.getMessage());
                throw new SQLException("Driver MySQL non trouvé", e);
            } catch (SQLException e) {
                System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Ferme la connexion à la base de données
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion à la base de données fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
}