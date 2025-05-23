package fr.planningcampus.planningcampus.controller;

import fr.planningcampus.planningcampus.dao.UtilisateurDAO;
import fr.planningcampus.planningcampus.model.Administrateur;
import fr.planningcampus.planningcampus.model.Enseignant;
import fr.planningcampus.planningcampus.model.Etudiant;
import fr.planningcampus.planningcampus.model.Utilisateur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;

public class InscriptionController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleSelector;

    @FXML
    public void initialize() {
        // Initialisation éventuelle
        roleSelector.setValue("Étudiant"); // valeur par défaut
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String motDePasse = passwordField.getText();
        String role = roleSelector.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || role == null) {
            showAlert(AlertType.WARNING, "Champs incomplets", "Veuillez remplir tous les champs.");
            return;
        }

        // Créer l'utilisateur selon le rôle
        // (Tu dois créer les classes Étudiant, Enseignant, Administrateur qui héritent de Utilisateur)
        Utilisateur nouvelUtilisateur;

        switch (role) {
            case "Étudiant":
                nouvelUtilisateur = new Etudiant(nom, prenom, email, motDePasse);
                break;
            case "Enseignant":
                nouvelUtilisateur = new Enseignant(nom, prenom, email, motDePasse);
                break;
            case "Administrateur":
                nouvelUtilisateur = new Administrateur(nom, prenom, email, motDePasse);
                break;
            default:
                showAlert(AlertType.ERROR, "Rôle inconnu", "Rôle sélectionné non pris en charge.");
                return;
        }

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
        boolean success = utilisateurDAO.addUtilisateur(nouvelUtilisateur);

        if (success) {
            showAlert(AlertType.INFORMATION, "Inscription réussie", "Bienvenue " + prenom + " !");
            clearForm();
        } else {
            showAlert(AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'inscription.");
        }
    }

    @FXML
    private void goToLogin() {
        // Utilisez n'importe quel contrôle de la vue actuelle (par exemple nomField)
        SceneManager.navigateTo(nomField, "login.fxml");
    }

    public class SceneManager {

        public static void navigateTo(Control sourceControl, String fxmlFile) {
            try {
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/fr/planningcampus/planningcampus/view/login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) sourceControl.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        passwordField.clear();
        roleSelector.getSelectionModel().clearSelection();
    }
}
