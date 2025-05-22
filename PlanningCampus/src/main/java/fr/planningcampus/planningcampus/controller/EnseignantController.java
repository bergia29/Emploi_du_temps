package fr.planningcampus.planningcampus.controller;

import fr.planningcampus.planningcampus.dao.CoursDAO;
import fr.planningcampus.planningcampus.dao.NotificationDAO;
import fr.planningcampus.planningcampus.dao.UtilisateurDAO;
import fr.planningcampus.planningcampus.model.Cours;
import fr.planningcampus.planningcampus.model.Enseignant;
import fr.planningcampus.planningcampus.model.Etudiant;
import fr.planningcampus.planningcampus.model.Notification;
import fr.planningcampus.planningcampus.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le tableau de bord enseignant
 */
public class EnseignantController implements Initializable {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Cours> coursTable;

    @FXML
    private TableView<Notification> problemesTable;

    @FXML
    private TableView<Notification> notificationsTable;

    private Enseignant enseignant;

    /**
     * Initialise le contrôleur
     *
     * @param url URL de localisation
     * @param rb Ressources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation des tableaux
        initCoursTable();
        initProblemesTable();
        initNotificationsTable();
    }

    /**
     * Définit l'utilisateur connecté
     *
     * @param utilisateur Utilisateur connecté
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        if (utilisateur instanceof Enseignant) {
            this.enseignant = (Enseignant) utilisateur;
            welcomeLabel.setText("Bienvenue, " + enseignant.getPrenom() + " " + enseignant.getNom());

            // Charger les données
            loadCours();
            loadProblemes();
            loadNotifications();
        }
    }

    /**
     * Charge les cours de l'enseignant
     */
    private void loadCours() {
        if (enseignant != null) {
            CoursDAO coursDAO = new CoursDAO();
            ObservableList<Cours> cours = FXCollections.observableArrayList(
                    coursDAO.getCoursByEnseignant(enseignant.getId()));
            coursTable.setItems(cours);
        }
    }

    /**
     * Charge les problèmes signalés
     */
    private void loadProblemes() {
        // Dans une implémentation réelle, on chargerait les problèmes depuis la base de données
        // Pour cet exemple, on va utiliser une liste observable avec des problèmes statiques
        ObservableList<Notification> problemes = FXCollections.observableArrayList();

        // Charger les notifications envoyées par l'enseignant (problèmes signalés)
        NotificationDAO notificationDAO = new NotificationDAO();
        List<Notification> notifs = notificationDAO.getNotificationsByExpediteur(enseignant.getId());
        if (notifs != null) {
            problemes.addAll(notifs);
        }

        problemesTable.setItems(problemes);
    }

    /**
     * Charge les notifications
     */
    private void loadNotifications() {
        NotificationDAO notificationDAO = new NotificationDAO();
        ObservableList<Notification> notifications = FXCollections.observableArrayList(
                notificationDAO.getNotificationsByDestinataire(enseignant.getId()));
        notificationsTable.setItems(notifications);
    }

    /**
     * Initialise le tableau des cours
     */
    private void initCoursTable() {
        TableColumn<Cours, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Cours, String> matiereCol = new TableColumn<>("Matière");
        matiereCol.setCellValueFactory(new PropertyValueFactory<>("matiere"));

        TableColumn<Cours, String> horaireCol = new TableColumn<>("Horaire");
        horaireCol.setCellValueFactory(cellData -> {
            Cours cours = cellData.getValue();
            String horaire = "Non défini";
            if (cours.getHoraire() != null) {
                horaire = cours.getHoraire().toString();
            }
            return new javafx.beans.property.SimpleStringProperty(horaire);
        });

        TableColumn<Cours, String> salleCol = new TableColumn<>("Salle");
        salleCol.setCellValueFactory(cellData -> {
            Cours cours = cellData.getValue();
            String salle = "Non assignée";
            if (cours.getSalle() != null) {
                salle = cours.getSalle().getNom();
            }
            return new javafx.beans.property.SimpleStringProperty(salle);
        });

        coursTable.getColumns().addAll(idCol, matiereCol, horaireCol, salleCol);
    }

    /**
     * Initialise le tableau des problèmes
     */
    private void initProblemesTable() {
        TableColumn<Notification, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Notification, String> contenuCol = new TableColumn<>("Description");
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        contenuCol.setPrefWidth(300);

        TableColumn<Notification, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(column -> new TableCell<Notification, Date>() {
            private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(sdf.format(item));
                }
            }
        });

        TableColumn<Notification, Boolean> traiteeCol = new TableColumn<>("Traité");
        traiteeCol.setCellValueFactory(new PropertyValueFactory<>("lue"));
        traiteeCol.setCellFactory(column -> new TableCell<Notification, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item ? "Oui" : "Non");
                    setStyle(item ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                }
            }
        });

        problemesTable.getColumns().addAll(idCol, contenuCol, dateCol, traiteeCol);
    }

    /**
     * Initialise le tableau des notifications
     */
    private void initNotificationsTable() {
        TableColumn<Notification, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Notification, String> contenuCol = new TableColumn<>("Contenu");
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        contenuCol.setPrefWidth(300);

        TableColumn<Notification, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(column -> new TableCell<Notification, Date>() {
            private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(sdf.format(item));
                }
            }
        });

        TableColumn<Notification, Boolean> lueCol = new TableColumn<>("Lue");
        lueCol.setCellValueFactory(new PropertyValueFactory<>("lue"));
        lueCol.setCellFactory(column -> new TableCell<Notification, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item ? "Oui" : "Non");
                    setStyle(item ? "" : "-fx-text-fill: red;");
                }
            }
        });

        notificationsTable.getColumns().addAll(idCol, contenuCol, dateCol, lueCol);

        // Ajouter un gestionnaire de double-clic pour afficher le contenu complet de la notification
        notificationsTable.setRowFactory(tv -> {
            TableRow<Notification> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Notification notification = row.getItem();
                    afficherContenuNotification(notification);
                }
            });
            return row;
        });
    }

    /**
     * Affiche le contenu complet d'une notification dans une fenêtre popup
     *
     * @param notification Notification à afficher
     */
    private void afficherContenuNotification(Notification notification) {
        if (notification == null) return;

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Détail de la notification");
        dialog.setHeaderText("Notification reçue le " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(notification.getDate()));

        // Définir les boutons
        ButtonType buttonTypeOk = new ButtonType("Fermer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        // Créer un TextArea pour afficher le contenu complet
        TextArea textArea = new TextArea(notification.getContenu());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(400);
        textArea.setPrefHeight(200);

        dialog.getDialogPane().setContent(textArea);

        // Marquer la notification comme lue si elle ne l'est pas déjà
        if (!notification.isLue()) {
            notification.setLue(true);
            NotificationDAO notificationDAO = new NotificationDAO();
            if (notificationDAO.updateNotification(notification)) {
                loadNotifications();
            }
        }

        dialog.showAndWait();
    }

    /**
     * Gère l'événement de déconnexion
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/timeplanner/view/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors du chargement de la page de connexion.");
        }
    }

    /**
     * Gère l'événement pour voir l'emploi du temps
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleVoirEmploiDuTemps(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/planningcampus/planningcampus/view/emploi-du-temps.fxml"));
            Parent root = loader.load();

            fr.planningcampus.planningcampus.controller.EmploiDuTempsController controller = loader.getController();
            controller.setUtilisateur(enseignant);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'emploi du temps: " + e.getMessage());
        }
    }

    /**
     * Gère l'événement pour signaler un problème
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleSignalerProbleme(ActionEvent event) {
        Cours coursSelectionne = coursTable.getSelectionModel().getSelectedItem();

        // Création d'un dialogue pour signaler un problème
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Signaler un problème");
        dialog.setHeaderText("Veuillez décrire le problème rencontré");

        // Boutons
        ButtonType signalerButtonType = new ButtonType("Signaler", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(signalerButtonType, ButtonType.CANCEL);

        // Créer le champ de description
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description du problème");

        // Si un cours est sélectionné, on prérempli avec les informations du cours
        if (coursSelectionne != null) {
            descriptionArea.setText("Problème concernant le cours: " + coursSelectionne.getMatiere() +
                    "\nHoraire: " + (coursSelectionne.getHoraire() != null ? coursSelectionne.getHoraire().toString() : "Non défini") +
                    "\nSalle: " + (coursSelectionne.getSalle() != null ? coursSelectionne.getSalle().getNom() : "Non assignée") +
                    "\n\nDescription du problème: ");
        }

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPrefWidth(400);
        grid.add(new Label("Description:"), 0, 0);
        grid.add(descriptionArea, 0, 1);
        descriptionArea.setPrefHeight(150);

        dialog.getDialogPane().setContent(grid);

        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == signalerButtonType) {
                return descriptionArea.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(probleme -> {
            // Créer une notification pour l'administrateur
            try {
                NotificationDAO notificationDAO = new NotificationDAO();
                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

                // Récupérer tous les administrateurs
                List<Utilisateur> administrateurs = utilisateurDAO.getAllAdministrateurs();

                if (!administrateurs.isEmpty()) {
                    // Envoi de la notification à tous les administrateurs
                    for (Utilisateur admin : administrateurs) {
                        Notification notification = new Notification();
                        notification.setIdDestinataire(admin.getId());
                        notification.setContenu("Problème signalé par " + enseignant.getPrenom() + " " +
                                enseignant.getNom() + ": " + probleme);
                        notification.setDate(new Date());
                        notification.setLue(false);
                        notification.setIdExpediteur(enseignant.getId()); // Pour retrouver les problèmes signalés par l'enseignant

                        notificationDAO.addNotification(notification);
                    }

                    // Ajouter le problème au tableau
                    loadProblemes();

                    showAlert("Succès", "Le problème a été signalé aux administrateurs.");
                } else {
                    showAlert("Erreur", "Aucun administrateur n'a été trouvé dans le système.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur est survenue lors du signalement du problème: " + e.getMessage());
            }
        });
    }

    /**
     * Gère l'événement pour voir les étudiants d'un cours
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleVoirEtudiants(ActionEvent event) {
        Cours coursSelectionne = coursTable.getSelectionModel().getSelectedItem();

        if (coursSelectionne == null) {
            showAlert("Information", "Veuillez sélectionner un cours.");
            return;
        }

        try {
            // Charger la liste des étudiants pour ce cours
            CoursDAO coursDAO = new CoursDAO();
            List<Etudiant> etudiants = coursDAO.getEtudiantsByCours(coursSelectionne.getId());

            if (etudiants.isEmpty()) {
                showAlert("Information", "Aucun étudiant n'est inscrit à ce cours.");
                return;
            }

            // Créer un dialogue pour afficher la liste des étudiants
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Étudiants du cours");
            dialog.setHeaderText("Liste des étudiants pour le cours: " + coursSelectionne.getMatiere());

            // Bouton OK
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            // Créer le tableau des étudiants
            TableView<Etudiant> etudiantsTable = new TableView<>();

            TableColumn<Etudiant, Integer> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Etudiant, String> nomCol = new TableColumn<>("Nom");
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

            TableColumn<Etudiant, String> prenomCol = new TableColumn<>("Prénom");
            prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));

            TableColumn<Etudiant, String> emailCol = new TableColumn<>("Email");
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

            TableColumn<Etudiant, String> groupeCol = new TableColumn<>("Groupe");
            groupeCol.setCellValueFactory(new PropertyValueFactory<>("groupe"));

            etudiantsTable.getColumns().addAll(idCol, nomCol, prenomCol, emailCol, groupeCol);
            etudiantsTable.setPrefWidth(500);
            etudiantsTable.setPrefHeight(300);

            etudiantsTable.setItems(FXCollections.observableArrayList(etudiants));

            dialog.getDialogPane().setContent(etudiantsTable);
            dialog.getDialogPane().setPrefWidth(550);
            dialog.getDialogPane().setPrefHeight(400);

            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue: " + e.getMessage());
        }
    }

    /**
     * Gère l'événement pour voir les détails d'un cours
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleVoirDetailsCours(ActionEvent event) {
        Cours coursSelectionne = coursTable.getSelectionModel().getSelectedItem();

        if (coursSelectionne == null) {
            showAlert("Information", "Veuillez sélectionner un cours.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du cours");
        alert.setHeaderText("Informations sur le cours");

        String details = "Matière: " + coursSelectionne.getMatiere() + "\n";

        if (coursSelectionne.getHoraire() != null) {
            details += "Horaire: " + coursSelectionne.getHoraire().toString() + "\n";
        } else {
            details += "Horaire: Non défini\n";
        }

        if (coursSelectionne.getSalle() != null) {
            details += "Salle: " + coursSelectionne.getSalle().getNom() + "\n";
            details += "Localisation: " + coursSelectionne.getSalle().getLocalisation() + "\n";
            details += "Capacité: " + coursSelectionne.getSalle().getCapacite() + " places\n";

            if (!coursSelectionne.getSalle().getEquipements().isEmpty()) {
                details += "Équipements: " + String.join(", ", coursSelectionne.getSalle().getEquipements()) + "\n";
            } else {
                details += "Équipements: Aucun équipement spécifique\n";
            }
        } else {
            details += "Salle: Non assignée\n";
        }

        // Nombre d'étudiants inscrits
        CoursDAO coursDAO = new CoursDAO();
        List<Etudiant> etudiants = coursDAO.getEtudiantsByCours(coursSelectionne.getId());
        details += "Nombre d'étudiants inscrits: " + etudiants.size() + "\n";

        alert.setContentText(details);
        alert.showAndWait();
    }

    /**
     * Gère l'événement pour marquer une notification comme lue
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleMarquerLue(ActionEvent event) {
        Notification notificationSelectionnee = notificationsTable.getSelectionModel().getSelectedItem();

        if (notificationSelectionnee == null) {
            showAlert("Information", "Veuillez sélectionner une notification.");
            return;
        }

        NotificationDAO notificationDAO = new NotificationDAO();
        notificationSelectionnee.setLue(true);

        if (notificationDAO.updateNotification(notificationSelectionnee)) {
            loadNotifications();
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de la mise à jour de la notification.");
        }
    }

    /**
     * Affiche une boîte de dialogue d'alerte
     *
     * @param title Titre de l'alerte
     * @param message Message de l'alerte
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}