package fr.planningcampus.planningcampus.controller;

import fr.planningcampus.planningcampus.PlanningCampusApplication;
import fr.planningcampus.planningcampus.dao.UtilisateurDAO;
import fr.planningcampus.planningcampus.model.Administrateur;
import fr.planningcampus.planningcampus.model.Enseignant;
import fr.planningcampus.planningcampus.model.Etudiant;
import fr.planningcampus.planningcampus.model.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur pour la page de connexion
 */
public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    /**
     * Gère l'événement de connexion
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Erreur de connexion", "Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur utilisateur = utilisateurDAO.authentifier(email, password);

        if (utilisateur != null) {
            try {
                FXMLLoader loader = null;

                if (utilisateur instanceof Administrateur) {
                    loader = new FXMLLoader(PlanningCampusApplication.class.getResource("view/admin-dashboard.fxml"));
                } else if (utilisateur instanceof Enseignant) {
                    loader = new FXMLLoader(PlanningCampusApplication.class.getResource("view/enseignant-dashboard.fxml"));
                } else if (utilisateur instanceof Etudiant) {
                    loader = new FXMLLoader(PlanningCampusApplication.class.getResource("view/etudiant-dashboard.fxml"));
                }

                if (loader != null) {
                    Parent root = loader.load();

                    if (utilisateur instanceof Administrateur) {
                        AdministrateurController controller = loader.getController();
                        controller.setUtilisateur(utilisateur);
                    } else if (utilisateur instanceof Enseignant) {
                        EnseignantController controller = loader.getController();
                        controller.setUtilisateur(utilisateur);
                    } else if (utilisateur instanceof Etudiant) {
                        EtudiantController controller = loader.getController();
                        controller.setUtilisateur(utilisateur);
                    }

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur est survenue lors du chargement du tableau de bord.");
            }
        } else {
            showAlert("Erreur de connexion", "Email ou mot de passe incorrect.");
        }
    }

    /**
     * Affiche une alerte
     *
     * @param title Titre de l'alerte
     * @param message Message de l'alerte
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}