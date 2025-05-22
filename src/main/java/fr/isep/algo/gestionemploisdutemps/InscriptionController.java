package fr.isep.algo.gestionemploisdutemps;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;

import java.io.IOException;

import java.io.IOException;

public class InscriptionController {

    @FXML
    private TextField nomField, prenomField, emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleSelector;

    @FXML
    private void handleRegister() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleSelector.getValue();


        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        if (!Utilisateur.emailValide(email)) {
            showAlert("Erreur", "Format d'email invalide : " + email);
            return;
        }

        if (Utilisateur.emailExistant(email)) {
            showAlert("Erreur", "Cet email est déjà utilisé : " + email);
            return;
        }

        // Créer un nouvel utilisateur
        Utilisateur nouvelUtilisateur = new Utilisateur(0, nom, prenom, email, password, role);

        // Sauvegarder dans la base
        if (nouvelUtilisateur.sauvegarderDansJson()){
            showAlert("Succès", "Inscription réussie en tant que : " + role);
        }else {
            showAlert("Erreur", "Erreur lors de l'enregistrement. Réessayez.");
        }
    }

   // @FXML
   // private void goToLogin() {
     //   HelloController controller = new HelloController();
     //   controller.loadPage("hello-view.fxml");
   // }
   @FXML
   private void goToLogin() {
       // Utilisez n'importe quel contrôle de la vue actuelle (par exemple nomField)
       SceneManager.navigateTo(nomField, "hello-view.fxml");
   }

    public class SceneManager {

        public static void navigateTo(Control sourceControl, String fxmlFile) {
            try {
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/fr/isep/algo/gestionemploisdutemps/" + fxmlFile));
                Parent root = loader.load();
                Stage stage = (Stage) sourceControl.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
