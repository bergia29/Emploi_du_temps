package fr.isep.algo.gestionemploisdutemps;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;

import java.util.Optional;

public class HelloController {

    @FXML
    private ComboBox<String> languageSelector;
    @FXML
    private void initialize() {
        languageSelector.setValue("Français"); // Définit la langue par défaut
    }
    @FXML
    private void handleLanguageChange(ActionEvent event) {
        String selectedLanguage = languageSelector.getValue();
        String currentLanguage = selectedLanguage.equals("Français") ? "Anglais" : "Français"; // Détecte la langue avant le changement

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        if (currentLanguage.equals("Anglais") && selectedLanguage.equals("Français")) {
            alert.setTitle("Language Change Confirmation");
            alert.setHeaderText("Do you want to switch to French?");
            alert.setContentText("This action will reload the application.");
            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonYes, buttonNo);
        } else {
            alert.setTitle("Confirmation du changement de langue");
            alert.setHeaderText("Voulez-vous passer à " + selectedLanguage + "?");
            alert.setContentText("Ce changement nécessitera une mise à jour de l'application.");
            ButtonType buttonYes = new ButtonType("Oui");
            ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonYes, buttonNo);
        }
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == alert.getButtonTypes().get(0)) {
            System.out.println("Langue changée en : " + selectedLanguage);
            // Ajoute ici le code pour recharger l'interface avec la nouvelle langue
        } else {
            languageSelector.setValue(currentLanguage); // Annule le changement
        }
    }


    @FXML
    private Button adminButton, studentButton, teacherButton;

    @FXML
    private void handleAdmin(ActionEvent event) {
        loadPage("login-admin.fxml");
    }

    @FXML
    private void handleStudent(ActionEvent event) {
        loadPage("login-student.fxml");
    }

    @FXML
    private void handleTeacher(ActionEvent event) {
        loadPage("login-teacher.fxml");
    }

    public void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/isep/algo/gestionemploisdutemps/" + fxmlFile));
            Parent root = loader.load();

            // Utilisation de la scène actuelle depuis n'importe quel contrôle qui n'est pas null
            Scene currentScene = null;
            if (adminButton != null) {
                currentScene = adminButton.getScene();
            } else if (studentButton != null) {
                currentScene = studentButton.getScene();
            } else if (teacherButton != null) {
                currentScene = teacherButton.getScene();
            } else if (languageSelector != null) {
                currentScene = languageSelector.getScene();
            }

            if (currentScene != null) {
                Stage stage = (Stage) currentScene.getWindow();
                stage.setScene(new Scene(root));
            } else {
                // Si aucun contrôle n'est disponible, c'est probablement appelé depuis un autre contrôleur
                throw new IllegalStateException("Impossible de trouver la scène actuelle depuis HelloController");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
