package fr.planningcampus.planningcampus.controller;

import fr.planningcampus.planningcampus.dao.CoursDAO;
import fr.planningcampus.planningcampus.dao.NotificationDAO;
import fr.planningcampus.planningcampus.model.Cours;
import fr.planningcampus.planningcampus.model.Etudiant;
import fr.planningcampus.planningcampus.model.NoteEtudiant;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le tableau de bord étudiant
 */
public class EtudiantController implements Initializable {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Cours> coursTable;

    @FXML
    private TableView<NoteEtudiant> notesTable;

    @FXML
    private TableView<Notification> notificationsTable;

    private Etudiant etudiant;
    private CoursDAO coursDAO = new CoursDAO();

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
        initNotesTable();
        initNotificationsTable();
    }

    /**
     * Définit l'utilisateur connecté
     *
     * @param utilisateur Utilisateur connecté
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        if (utilisateur instanceof Etudiant) {
            this.etudiant = (Etudiant) utilisateur;
            welcomeLabel.setText("Bienvenue, " + etudiant.getPrenom() + " " + etudiant.getNom());

            // Charger les données
            loadCours();
            loadNotes();
            loadNotifications();
        }
    }

    /**
     * Charge l'emploi du temps de l'étudiant
     */
    private void loadCours() {
        if (etudiant != null) {
            // Récupération des cours depuis la base de données
            List<Cours> cours = coursDAO.getCoursByEtudiant(etudiant.getId());
            ObservableList<Cours> observableCours = FXCollections.observableArrayList(cours);
            coursTable.setItems(observableCours);
        }
    }

    /**
     * Charge les notes de l'étudiant
     */
    private void loadNotes() {
        if (etudiant != null) {
            // Dans une vraie application, on récupérerait les notes depuis la base de données
            // Ici on utilise directement les notes de l'étudiant
            List<NoteEtudiant> notes = etudiant.getNotes();
            ObservableList<NoteEtudiant> observableNotes = FXCollections.observableArrayList(notes);
            notesTable.setItems(observableNotes);
        }
    }

    /**
     * Charge les notifications de l'étudiant
     */
    private void loadNotifications() {
        NotificationDAO notificationDAO = new NotificationDAO();
        ObservableList<Notification> notifications = FXCollections.observableArrayList(
                notificationDAO.getNotificationsByDestinataire(etudiant.getId()));
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

        TableColumn<Cours, String> enseignantCol = new TableColumn<>("Enseignant");
        enseignantCol.setCellValueFactory(cellData -> {
            Cours cours = cellData.getValue();
            String enseignant = "Non assigné";
            if (cours.getEnseignant() != null) {
                enseignant = cours.getEnseignant().getPrenom() + " " + cours.getEnseignant().getNom();
            }
            return new javafx.beans.property.SimpleStringProperty(enseignant);
        });

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

        coursTable.getColumns().addAll(idCol, matiereCol, enseignantCol, horaireCol, salleCol);
    }

    /**
     * Initialise le tableau des notes
     */
    private void initNotesTable() {
        TableColumn<NoteEtudiant, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<NoteEtudiant, Integer> coursCol = new TableColumn<>("Cours ID");
        coursCol.setCellValueFactory(new PropertyValueFactory<>("id_cours"));

        // Ajout d'une colonne pour afficher le nom du cours
        TableColumn<NoteEtudiant, String> coursNomCol = new TableColumn<>("Cours");
        coursNomCol.setCellValueFactory(cellData -> {
            int coursId = cellData.getValue().getId_cours();
            Cours cours = coursDAO.getCoursById(coursId);
            String nomCours = "Inconnu";
            if (cours != null) {
                nomCours = cours.getMatiere();
            }
            return new javafx.beans.property.SimpleStringProperty(nomCours);
        });

        TableColumn<NoteEtudiant, Float> noteCol = new TableColumn<>("Note");
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));

        notesTable.getColumns().addAll(idCol, coursCol, coursNomCol, noteCol);
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
            Parent root = FXMLLoader.load(getClass().getResource("/fr/planningcampus/planningcampus/view/login.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/timeplanner/view/emploi-du-temps.fxml"));
            Parent root = loader.load();

            fr.planningcampus.planningcampus.controller.EmploiDuTempsController controller = loader.getController();
            controller.setUtilisateur(etudiant);

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
     * Gère l'événement pour voir les notifications
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleVoirNotifications(ActionEvent event) {
        // Sélection automatique de l'onglet des notifications
        TabPane tabPane = (TabPane) coursTable.getScene().lookup("#tabPane");
        if (tabPane != null) {
            // Chercher l'onglet Notifications
            for (Tab tab : tabPane.getTabs()) {
                if (tab.getText().equals("Notifications")) {
                    tabPane.getSelectionModel().select(tab);
                    break;
                }
            }
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

        if (coursSelectionne.getEnseignant() != null) {
            details += "Enseignant: " + coursSelectionne.getEnseignant().getPrenom() + " " +
                    coursSelectionne.getEnseignant().getNom() + "\n";
        } else {
            details += "Enseignant: Non assigné\n";
        }

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
     * Affiche une alerte
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