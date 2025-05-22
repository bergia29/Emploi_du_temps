package fr.planningcampus.planningcampus;

import fr.planningcampus.planningcampus.dao.SeanceDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe principale de l'application
 */
public class PlanningCampusApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Vérifier la colonne id_expediteur
        fr.planningcampus.planningcampus.dao.NotificationDAO notificationDAO = new fr.planningcampus.planningcampus.dao.NotificationDAO();
        notificationDAO.verifierColonneIdExpediteur();

        // Vérifier la colonne date dans la table horaire
        SeanceDAO seanceDAO = new SeanceDAO();
        seanceDAO.verifierColonneDate();

        FXMLLoader fxmlLoader = new FXMLLoader(PlanningCampusApplication.class.getResource("view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("PlanningCampus - Gestion des emplois du temps");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Point d'entrée principal de l'application
     *
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch();
    }
}