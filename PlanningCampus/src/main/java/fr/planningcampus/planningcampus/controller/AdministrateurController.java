package fr.planningcampus.planningcampus.controller;

import fr.planningcampus.planningcampus.dao.CoursDAO;
import fr.planningcampus.planningcampus.dao.SalleDAO;
import fr.planningcampus.planningcampus.dao.SeanceDAO;
import fr.planningcampus.planningcampus.dao.UtilisateurDAO;
import fr.planningcampus.planningcampus.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Contrôleur pour le tableau de bord administrateur
 */
public class AdministrateurController implements Initializable {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Utilisateur> utilisateursTable;

    @FXML
    private TableView<Cours> coursTable;

    @FXML
    private TableView<Salle> sallesTable;

    @FXML
    private TableView<fr.planningcampus.planningcampus.model.Notification> notificationsTable;

    private Administrateur administrateur;
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
    private CoursDAO coursDAO = new CoursDAO();
    private SalleDAO salleDAO = new SalleDAO();

    /**
     * Initialise le contrôleur
     *
     * @param url URL de localisation
     * @param rb Ressources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation des tableaux
        initUtilisateursTable();
        initCoursTable();
        initSallesTable();
        initNotificationsTable();
    }

    /**
     * Définit l'utilisateur connecté
     *
     * @param utilisateur Utilisateur connecté
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        if (utilisateur instanceof Administrateur) {
            this.administrateur = (Administrateur) utilisateur;
            welcomeLabel.setText("Bienvenue, " + administrateur.getPrenom() + " " + administrateur.getNom());

            // Charger les données
            loadUtilisateurs();
            loadCours();
            loadSalles();
            loadNotifications();
        }
    }

    /**
     * Charge les utilisateurs depuis la base de données
     */
    private void loadUtilisateurs() {
        ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList(utilisateurDAO.getAllUtilisateurs());
        utilisateursTable.setItems(utilisateurs);
    }

    /**
     * Charge les cours depuis la base de données
     */
    private void loadCours() {
        ObservableList<Cours> cours = FXCollections.observableArrayList(coursDAO.getAllCours());
        coursTable.setItems(cours);
    }

    /**
     * Charge les salles depuis la base de données
     */
    private void loadSalles() {
        ObservableList<Salle> salles = FXCollections.observableArrayList(salleDAO.getAllSalles());
        sallesTable.setItems(salles);
    }

    /**
     * Initialise le tableau des utilisateurs
     */
    private void initUtilisateursTable() {
        TableColumn<Utilisateur, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Utilisateur, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        TableColumn<Utilisateur, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Utilisateur, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> {
            Utilisateur user = cellData.getValue();
            String type = "Inconnu";
            if (user instanceof Administrateur) {
                type = "Administrateur";
            } else if (user instanceof fr.planningcampus.planningcampus.model.Enseignant) {
                type = "Enseignant";
            } else if (user instanceof fr.planningcampus.planningcampus.model.Etudiant) {
                type = "Étudiant";
            }
            return new javafx.beans.property.SimpleStringProperty(type);
        });

        utilisateursTable.getColumns().addAll(idCol, nomCol, prenomCol, emailCol, typeCol);
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
     * Initialise le tableau des salles
     */
    private void initSallesTable() {
        TableColumn<Salle, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Salle, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<Salle, Integer> capaciteCol = new TableColumn<>("Capacité");
        capaciteCol.setCellValueFactory(new PropertyValueFactory<>("capacite"));

        sallesTable.getColumns().addAll(idCol, nomCol, capaciteCol);
    }

    /**
     * Initialise le tableau des notifications
     */
    private void initNotificationsTable() {
        TableColumn<fr.planningcampus.planningcampus.model.Notification, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<fr.planningcampus.planningcampus.model.Notification, String> contenuCol = new TableColumn<>("Contenu");
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        contenuCol.setPrefWidth(300);

        TableColumn<fr.planningcampus.planningcampus.model.Notification, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setCellFactory(column -> new TableCell<fr.planningcampus.planningcampus.model.Notification, Date>() {
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

        TableColumn<fr.planningcampus.planningcampus.model.Notification, Boolean> lueCol = new TableColumn<>("Lue");
        lueCol.setCellValueFactory(new PropertyValueFactory<>("lue"));
        lueCol.setCellFactory(column -> new TableCell<fr.planningcampus.planningcampus.model.Notification, Boolean>() {
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
     * Gère l'événement d'ajout d'utilisateur
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleAddUtilisateur(ActionEvent event) {
        // Création d'un dialogue pour ajouter un utilisateur
        Dialog<Utilisateur> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un utilisateur");
        dialog.setHeaderText("Veuillez remplir les informations de l'utilisateur");

        // Boutons
        ButtonType ajouterButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ajouterButtonType, ButtonType.CANCEL);

        // Création des champs de formulaire
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Administrateur", "Enseignant", "Étudiant");
        typeCombo.setValue("Étudiant");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Mot de passe:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Type:"), 0, 4);
        grid.add(typeCombo, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Focus sur le premier champ
        nomField.requestFocus();

        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ajouterButtonType) {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                String type = typeCombo.getValue();

                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
                    return null;
                }

                Utilisateur nouvelUtilisateur = null;

                switch (type) {
                    case "Administrateur":
                        nouvelUtilisateur = new Administrateur(0, nom, prenom, email, password);
                        break;
                    case "Enseignant":
                        nouvelUtilisateur = new fr.planningcampus.planningcampus.model.Enseignant(0, nom, prenom, email, password, new java.util.Date());
                        break;
                    case "Étudiant":
                        nouvelUtilisateur = new fr.planningcampus.planningcampus.model.Etudiant(0, nom, prenom, email, password, "Groupe A");
                        break;
                }

                // Ajouter l'utilisateur à la base de données
                if (nouvelUtilisateur != null && utilisateurDAO.addUtilisateur(nouvelUtilisateur)) {
                    // Rafraîchir la liste
                    loadUtilisateurs();
                    return nouvelUtilisateur;
                } else {
                    showAlert("Erreur", "Erreur lors de l'ajout de l'utilisateur.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Gère l'événement d'ajout de cours
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleAddCours(ActionEvent event) {
        // Création d'un dialogue pour ajouter un cours
        Dialog<Cours> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un cours");
        dialog.setHeaderText("Veuillez remplir les informations du cours");

        // Boutons
        ButtonType ajouterButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ajouterButtonType, ButtonType.CANCEL);

        // Création des champs de formulaire
        TextField matiereField = new TextField();
        matiereField.setPromptText("Matière");

        ComboBox<fr.planningcampus.planningcampus.model.Enseignant> enseignantCombo = new ComboBox<>();
        enseignantCombo.setPromptText("Enseignant");

        // Récupérer les enseignants depuis la base de données
        fr.planningcampus.planningcampus.dao.EnseignantDAO enseignantDAO = new fr.planningcampus.planningcampus.dao.EnseignantDAO();
        enseignantCombo.setItems(FXCollections.observableArrayList(enseignantDAO.getAllEnseignants()));
        enseignantCombo.setCellFactory(param -> new ListCell<fr.planningcampus.planningcampus.model.Enseignant>() {
            @Override
            protected void updateItem(fr.planningcampus.planningcampus.model.Enseignant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPrenom() + " " + item.getNom());
                }
            }
        });
        enseignantCombo.setButtonCell(new ListCell<fr.planningcampus.planningcampus.model.Enseignant>() {
            @Override
            protected void updateItem(fr.planningcampus.planningcampus.model.Enseignant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPrenom() + " " + item.getNom());
                }
            }
        });

        // Champ pour sélectionner le jour, l'heure de début et l'heure de fin
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date du cours");

        ComboBox<String> jourCombo = new ComboBox<>();
        jourCombo.setPromptText("Jour de la semaine");
        jourCombo.getItems().addAll("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi");

        ComboBox<String> heureDebutCombo = new ComboBox<>();
        heureDebutCombo.setPromptText("Heure de début");
        for (int h = 8; h <= 17; h++) {
            heureDebutCombo.getItems().add(String.format("%02d:00", h));
            heureDebutCombo.getItems().add(String.format("%02d:30", h));
        }

        ComboBox<String> heureFinCombo = new ComboBox<>();
        heureFinCombo.setPromptText("Heure de fin");
        for (int h = 9; h <= 18; h++) {
            heureFinCombo.getItems().add(String.format("%02d:00", h));
            heureFinCombo.getItems().add(String.format("%02d:30", h));
        }

        ComboBox<Integer> semaineCombo = new ComboBox<>();
        semaineCombo.setPromptText("Semaine");
        for (int s = 1; s <= 52; s++) {
            semaineCombo.getItems().add(s);
        }

        ComboBox<Salle> salleCombo = new ComboBox<>();
        salleCombo.setPromptText("Salle");

        // Récupérer les salles depuis la base de données
        fr.planningcampus.planningcampus.dao.SalleDAO salleDAO = new fr.planningcampus.planningcampus.dao.SalleDAO();
        salleCombo.setItems(FXCollections.observableArrayList(salleDAO.getAllSalles()));

        // Sélection de plusieurs groupes d'étudiants
        ListView<String> groupeListView = new ListView<>();
        groupeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Récupérer tous les groupes distincts depuis la base de données
        fr.planningcampus.planningcampus.dao.UtilisateurDAO utilisateurDAO = new fr.planningcampus.planningcampus.dao.UtilisateurDAO();
        groupeListView.setItems(FXCollections.observableArrayList(utilisateurDAO.getAllGroupes()));

        // Ajouter un écouteur pour mettre à jour les salles disponibles en fonction du nombre d'étudiants
        groupeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && heureDebutCombo.getValue() != null && heureFinCombo.getValue() != null &&
                    jourCombo.getValue() != null && semaineCombo.getValue() != null) {

                // Récupérer le nombre d'étudiants dans le groupe
                int nbEtudiants = utilisateurDAO.countEtudiantsByGroupe(newVal);

                // Créer un objet Horaire temporaire
                SeanceDAO seanceDAO = new SeanceDAO();
                Seance seance = new Seance();
                seance.setJour(jourCombo.getValue());
                seance.setSemaine(semaineCombo.getValue());

                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                    seance.setHeureDebut(sdf.parse(heureDebutCombo.getValue()));
                    seance.setHeureFin(sdf.parse(heureFinCombo.getValue()));

                    if (datePicker.getValue() != null) {
                        java.sql.Date date = java.sql.Date.valueOf(datePicker.getValue());
                        seance.setDate(date);
                    }

                    // Sauvegarder l'horaire temporairement
                    seanceDAO.addHoraire(seance);

                    // Récupérer les salles disponibles
                    fr.planningcampus.planningcampus.dao.CoursDAO coursDAO = new fr.planningcampus.planningcampus.dao.CoursDAO();
                    List<Salle> sallesDisponibles = coursDAO.getSallesDisponibles(nbEtudiants, seance.getId());

                    // Mettre à jour la liste des salles disponibles
                    salleCombo.setItems(FXCollections.observableArrayList(sallesDisponibles));

                    // Supprimer l'horaire temporaire
                    seanceDAO.deleteHoraire(seance.getId());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        salleCombo.setCellFactory(param -> new ListCell<Salle>() {
            @Override
            protected void updateItem(Salle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " (capacité: " + item.getCapacite() + ")");
                }
            }
        });
        salleCombo.setButtonCell(new ListCell<Salle>() {
            @Override
            protected void updateItem(Salle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " (capacité: " + item.getCapacite() + ")");
                }
            }
        });

        // Ajouter un écouteur sur l'enseignant pour vérifier les conflits d'horaire
        heureDebutCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            checkHoraireConflict(enseignantCombo, jourCombo, heureDebutCombo, heureFinCombo, semaineCombo);
        });

        heureFinCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            checkHoraireConflict(enseignantCombo, jourCombo, heureDebutCombo, heureFinCombo, semaineCombo);
        });

        jourCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            checkHoraireConflict(enseignantCombo, jourCombo, heureDebutCombo, heureFinCombo, semaineCombo);
        });

        semaineCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            checkHoraireConflict(enseignantCombo, jourCombo, heureDebutCombo, heureFinCombo, semaineCombo);
        });

        enseignantCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            checkHoraireConflict(enseignantCombo, jourCombo, heureDebutCombo, heureFinCombo, semaineCombo);
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Matière:"), 0, 0);
        grid.add(matiereField, 1, 0);
        grid.add(new Label("Enseignant:"), 0, 1);
        grid.add(enseignantCombo, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Jour:"), 0, 3);
        grid.add(jourCombo, 1, 3);
        grid.add(new Label("Heure de début:"), 0, 4);
        grid.add(heureDebutCombo, 1, 4);
        grid.add(new Label("Heure de fin:"), 0, 5);
        grid.add(heureFinCombo, 1, 5);
        grid.add(new Label("Semaine:"), 0, 6);
        grid.add(semaineCombo, 1, 6);
        grid.add(new Label("Groupe d'étudiants:"), 0, 7);
        grid.add(groupeListView, 1, 7);
        grid.add(new Label("Salle:"), 0, 8);
        grid.add(salleCombo, 1, 8);

        dialog.getDialogPane().setContent(grid);

        // Conversion du résultat en objet Cours
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ajouterButtonType) {
                if (matiereField.getText().isEmpty() || enseignantCombo.getValue() == null ||
                        jourCombo.getValue() == null || heureDebutCombo.getValue() == null ||
                        heureFinCombo.getValue() == null || semaineCombo.getValue() == null ||
                        salleCombo.getValue() == null || groupeListView.getSelectionModel().getSelectedItems().isEmpty()) {
                    showAlert("Erreur", "Veuillez remplir tous les champs.");
                    return null;
                }

                try {
                    // Créer un nouvel horaire
                    Seance seance = new Seance();
                    seance.setJour(jourCombo.getValue());
                    seance.setSemaine(semaineCombo.getValue());

                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                    seance.setHeureDebut(sdf.parse(heureDebutCombo.getValue()));
                    seance.setHeureFin(sdf.parse(heureFinCombo.getValue()));

                    if (datePicker.getValue() != null) {
                        java.sql.Date date = java.sql.Date.valueOf(datePicker.getValue());
                        seance.setDate(date);
                    }

                    // Sauvegarder l'horaire
                    SeanceDAO seanceDAO = new SeanceDAO();
                    seanceDAO.addHoraire(seance);

                    // Vérifier si l'enseignant a déjà un cours à cet horaire
                    fr.planningcampus.planningcampus.dao.CoursDAO coursDAO = new fr.planningcampus.planningcampus.dao.CoursDAO();
                    if (coursDAO.enseignantHasCoursAtHoraire(enseignantCombo.getValue().getId(), seance.getId())) {
                        showAlert("Conflit d'horaire", "L'enseignant a déjà un cours programmé à cet horaire.");
                        seanceDAO.deleteHoraire(seance.getId());
                        return null;
                    }

                    // Vérifier si la salle est déjà occupée à cet horaire
                    if (coursDAO.salleIsOccupiedAtHoraire(salleCombo.getValue().getId(), seance.getId())) {
                        showAlert("Conflit de salle", "La salle est déjà occupée à cet horaire.");
                        seanceDAO.deleteHoraire(seance.getId());
                        return null;
                    }

                    // Créer un nouveau cours
                    Cours cours = new Cours();
                    cours.setMatiere(matiereField.getText());
                    cours.setEnseignant(enseignantCombo.getValue());
                    cours.setHoraire(seance);
                    cours.setSalle(salleCombo.getValue());

                    // Ajouter le cours dans la base de données
                    if (coursDAO.addCours(cours)) {
                        // Ajouter les étudiants du groupe au cours
                        List<Integer> etudiantsIds = new ArrayList<>();

                        for (String groupe : groupeListView.getSelectionModel().getSelectedItems()) {
                            coursDAO.addGroupeToCours(cours.getId(), groupe);
                            // Collecter les IDs des étudiants pour les notifications
                            etudiantsIds.addAll(utilisateurDAO.getEtudiantsIdsByGroupe(groupe));
                        }

                        // Envoyer une notification à chaque étudiant concernant le nouveau cours
                        if (!etudiantsIds.isEmpty()) {
                            // Créer le message pour la notification
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                            StringBuilder message = new StringBuilder();
                            message.append("Nouveau cours de ")
                                    .append(cours.getMatiere());

                            if (seance.getDate() != null) {
                                message.append(" le ")
                                        .append(dateFormat.format(seance.getDate()))
                                        .append(" (").append(seance.getJour()).append(")");
                            }

                            message.append(" de ")
                                    .append(timeFormat.format(seance.getHeureDebut()))
                                    .append(" à ")
                                    .append(timeFormat.format(seance.getHeureFin()));

                            if (cours.getSalle() != null) {
                                message.append(" en salle ")
                                        .append(cours.getSalle().getNom());
                            }

                            if (cours.getEnseignant() != null) {
                                message.append(" avec ")
                                        .append(cours.getEnseignant().getPrenom())
                                        .append(" ")
                                        .append(cours.getEnseignant().getNom());
                            }

                            // Envoyer la notification à chaque étudiant
                            fr.planningcampus.planningcampus.dao.NotificationDAO notificationDAO = new fr.planningcampus.planningcampus.dao.NotificationDAO();
                            for (Integer etudiantId : etudiantsIds) {
                                fr.planningcampus.planningcampus.model.Notification notification = new fr.planningcampus.planningcampus.model.Notification();
                                notification.setIdDestinataire(etudiantId);
                                notification.setIdExpediteur(administrateur.getId());
                                notification.setContenu(message.toString());
                                notification.setDate(new Date());
                                notification.setLue(false);

                                notificationDAO.addNotification(notification);
                            }
                        }

                        return cours;
                    } else {
                        showAlert("Erreur", "Une erreur est survenue lors de l'ajout du cours.");
                        return null;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Erreur", "Une erreur est survenue: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(cours -> {
            // Rafraîchir la liste des cours
            loadCours();
        });
    }

    /**
     * Vérifie s'il y a un conflit d'horaire pour un enseignant
     *
     * @param enseignantCombo Combo box de l'enseignant
     * @param jourCombo Combo box du jour
     * @param heureDebutCombo Combo box de l'heure de début
     * @param heureFinCombo Combo box de l'heure de fin
     * @param semaineCombo Combo box de la semaine
     */
    private void checkHoraireConflict(ComboBox<fr.planningcampus.planningcampus.model.Enseignant> enseignantCombo,
                                      ComboBox<String> jourCombo,
                                      ComboBox<String> heureDebutCombo,
                                      ComboBox<String> heureFinCombo,
                                      ComboBox<Integer> semaineCombo) {
        if (enseignantCombo.getValue() != null && jourCombo.getValue() != null &&
                heureDebutCombo.getValue() != null && heureFinCombo.getValue() != null &&
                semaineCombo.getValue() != null) {

            try {
                // Créer un objet Horaire temporaire
                SeanceDAO seanceDAO = new SeanceDAO();
                Seance seance = new Seance();
                seance.setJour(jourCombo.getValue());
                seance.setSemaine(semaineCombo.getValue());

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                seance.setHeureDebut(sdf.parse(heureDebutCombo.getValue()));
                seance.setHeureFin(sdf.parse(heureFinCombo.getValue()));

                // Sauvegarder l'horaire temporairement
                seanceDAO.addHoraire(seance);

                // Vérifier si l'enseignant a déjà un cours à cet horaire
                fr.planningcampus.planningcampus.dao.CoursDAO coursDAO = new fr.planningcampus.planningcampus.dao.CoursDAO();
                if (coursDAO.enseignantHasCoursAtHoraire(enseignantCombo.getValue().getId(), seance.getId())) {
                    showAlert("Conflit d'horaire", "L'enseignant sélectionné a déjà un cours programmé à cet horaire.");
                }

                // Supprimer l'horaire temporaire
                seanceDAO.deleteHoraire(seance.getId());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gère l'événement d'ajout de salle
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleAddSalle(ActionEvent event) {
        // Création d'un dialogue pour ajouter une salle
        Dialog<Salle> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une salle");
        dialog.setHeaderText("Veuillez remplir les informations de la salle");

        // Boutons
        ButtonType ajouterButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ajouterButtonType, ButtonType.CANCEL);

        // Création des champs de formulaire
        TextField nomField = new TextField();
        nomField.setPromptText("Nom de la salle");
        TextField capaciteField = new TextField();
        capaciteField.setPromptText("Capacité");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Capacité:"), 0, 1);
        grid.add(capaciteField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Focus sur le premier champ
        nomField.requestFocus();

        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ajouterButtonType) {
                String nom = nomField.getText();
                String capaciteStr = capaciteField.getText();

                if (nom.isEmpty() || capaciteStr.isEmpty()) {
                    showAlert("Erreur", "Veuillez remplir tous les champs.");
                    return null;
                }

                try {
                    int capacite = Integer.parseInt(capaciteStr);
                    Salle salle = new Salle(0, nom, capacite);

                    // Ajouter la salle à la base de données
                    if (salleDAO.addSalle(salle)) {
                        // Rafraîchir la liste
                        loadSalles();
                        return salle;
                    } else {
                        showAlert("Erreur", "Erreur lors de l'ajout de la salle.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "La capacité doit être un nombre entier.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Gère l'événement d'affichage des statistiques
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleAfficherStatistiques(ActionEvent event) {
        // Création d'une nouvelle fenêtre pour les statistiques
        Stage statistiquesStage = new Stage();
        statistiquesStage.setTitle("Statistiques");

        // Création du contenu
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Onglet pour les statistiques d'enseignants
        Tab enseignantsTab = new Tab("Enseignants");

        // Calculer le nombre d'heures par enseignant
        BarChart<String, Number> enseignantChart = createEnseignantChart();

        enseignantsTab.setContent(enseignantChart);

        // Onglet pour les statistiques de salles
        Tab sallesTab = new Tab("Salles");

        // Calculer le taux d'occupation des salles
        PieChart salleChart = createSalleChart();

        sallesTab.setContent(salleChart);

        // Ajouter les onglets au TabPane
        tabPane.getTabs().addAll(enseignantsTab, sallesTab);

        // Création de la scène
        Scene scene = new Scene(tabPane, 800, 600);
        statistiquesStage.setScene(scene);

        statistiquesStage.show();
    }

    /**
     * Crée un graphique pour les statistiques des enseignants
     *
     * @return Graphique à barres des heures par enseignant
     */
    private BarChart<String, Number> createEnseignantChart() {
        // Création des axes
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Enseignants");
        yAxis.setLabel("Nombre d'heures");

        // Création du graphique
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Nombre d'heures par enseignant");

        // Récupération des données
        fr.planningcampus.planningcampus.dao.EnseignantDAO enseignantDAO = new fr.planningcampus.planningcampus.dao.EnseignantDAO();
        List<fr.planningcampus.planningcampus.model.Enseignant> enseignants = enseignantDAO.getAllEnseignants();

        // Série de données
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Heures de cours");

        // Pour chaque enseignant, calculer le nombre total d'heures de cours
        for (fr.planningcampus.planningcampus.model.Enseignant enseignant : enseignants) {
            int heures = calculateEnseignantHeures(enseignant.getId());
            series.getData().add(new XYChart.Data<>(enseignant.getPrenom() + " " + enseignant.getNom(), heures));
        }

        barChart.getData().add(series);

        return barChart;
    }

    /**
     * Calcule le nombre total d'heures de cours pour un enseignant
     *
     * @param enseignantId ID de l'enseignant
     * @return Nombre total d'heures
     */
    private int calculateEnseignantHeures(int enseignantId) {
        fr.planningcampus.planningcampus.dao.CoursDAO coursDAO = new fr.planningcampus.planningcampus.dao.CoursDAO();
        List<Cours> cours = coursDAO.getCoursByEnseignant(enseignantId);

        int totalHeures = 0;
        for (Cours c : cours) {
            if (c.getHoraire() != null && c.getHoraire().getHeureDebut() != null && c.getHoraire().getHeureFin() != null) {
                // Calcul de la durée du cours en heures
                java.util.Date debut = c.getHoraire().getHeureDebut();
                java.util.Date fin = c.getHoraire().getHeureFin();
                long dureeMs = fin.getTime() - debut.getTime();

                // Protection contre les valeurs négatives ou inappropriées
                if (dureeMs > 0) {
                    // Convertir en heures (avec arrondi supérieur)
                    int dureeHeures = (int) Math.ceil(dureeMs / (1000.0 * 60 * 60));
                    totalHeures += dureeHeures;
                }
            }
        }

        return totalHeures;
    }

    /**
     * Crée un graphique pour les statistiques des salles
     *
     * @return Graphique en camembert de l'occupation des salles
     */
    private PieChart createSalleChart() {
        // Création du graphique
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Taux d'occupation des salles");

        // Récupération des données
        fr.planningcampus.planningcampus.dao.SalleDAO salleDAO = new fr.planningcampus.planningcampus.dao.SalleDAO();
        List<Salle> salles = salleDAO.getAllSalles();

        // Pour chaque salle, calculer le taux d'occupation
        for (Salle salle : salles) {
            int occupationPourcentage = calculateSalleOccupation(salle.getId());
            pieChart.getData().add(new PieChart.Data(salle.getNom() + " (" + occupationPourcentage + "%)", occupationPourcentage));
        }

        return pieChart;
    }

    /**
     * Calcule le taux d'occupation d'une salle en pourcentage
     *
     * @param salleId ID de la salle
     * @return Taux d'occupation en pourcentage
     */
    private int calculateSalleOccupation(int salleId) {
        fr.planningcampus.planningcampus.dao.CoursDAO coursDAO = new fr.planningcampus.planningcampus.dao.CoursDAO();
        List<Cours> coursDansSalle = coursDAO.getCoursBySalle(salleId);

        int totalHeures = 0;
        for (Cours c : coursDansSalle) {
            if (c.getHoraire() != null && c.getHoraire().getHeureDebut() != null && c.getHoraire().getHeureFin() != null) {
                // Calcul de la durée du cours en heures
                java.util.Date debut = c.getHoraire().getHeureDebut();
                java.util.Date fin = c.getHoraire().getHeureFin();
                long dureeMs = fin.getTime() - debut.getTime();

                // Protection contre les valeurs négatives ou inappropriées
                if (dureeMs > 0) {
                    // Convertir en heures (avec arrondi supérieur)
                    int dureeHeures = (int) Math.ceil(dureeMs / (1000.0 * 60 * 60));
                    totalHeures += dureeHeures;
                }
            }
        }

        // 10 heures par jour * 5 jours = 50 heures max par semaine
        int maxHeuresParSemaine = 50;
        // Éviter la division par zéro
        if (maxHeuresParSemaine > 0) {
            int pourcentage = (totalHeures * 100) / maxHeuresParSemaine;
            return Math.min(pourcentage, 100); // Limiter à 100%
        }

        return 0; // Retourner 0 si maxHeuresParSemaine est 0
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

            EmploiDuTempsController controller = loader.getController();
            controller.setUtilisateur(administrateur);

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
     * Gère l'événement pour modifier un cours
     *
     * @param event Événement déclencheur
     */
    @FXML
    private void handleModifierCours(ActionEvent event) {
        Cours coursSelectionne = coursTable.getSelectionModel().getSelectedItem();

        if (coursSelectionne == null) {
            showAlert("Information", "Veuillez sélectionner un cours à modifier.");
            return;
        }

        // Création d'un dialogue pour modifier le cours
        Dialog<Cours> dialog = new Dialog<>();
        dialog.setTitle("Modifier le cours");
        dialog.setHeaderText("Modifier les informations du cours");

        // Boutons
        ButtonType modifierButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierButtonType, ButtonType.CANCEL);

        // Création des champs de formulaire
        TextField matiereField = new TextField(coursSelectionne.getMatiere());
        matiereField.setPromptText("Matière");

        ComboBox<fr.planningcampus.planningcampus.model.Enseignant> enseignantCombo = new ComboBox<>();
        enseignantCombo.setPromptText("Enseignant");

        // Récupérer les enseignants depuis la base de données
        fr.planningcampus.planningcampus.dao.EnseignantDAO enseignantDAO = new fr.planningcampus.planningcampus.dao.EnseignantDAO();
        enseignantCombo.setItems(FXCollections.observableArrayList(enseignantDAO.getAllEnseignants()));
        enseignantCombo.setValue(coursSelectionne.getEnseignant());
        enseignantCombo.setCellFactory(param -> new ListCell<fr.planningcampus.planningcampus.model.Enseignant>() {
            @Override
            protected void updateItem(fr.planningcampus.planningcampus.model.Enseignant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPrenom() + " " + item.getNom());
                }
            }
        });
        enseignantCombo.setButtonCell(new ListCell<fr.planningcampus.planningcampus.model.Enseignant>() {
            @Override
            protected void updateItem(fr.planningcampus.planningcampus.model.Enseignant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPrenom() + " " + item.getNom());
                }
            }
        });

        // Champ pour sélectionner la date
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date du cours");
        if (coursSelectionne.getHoraire() != null && coursSelectionne.getHoraire().getDate() != null) {
            java.sql.Date sqlDate = new java.sql.Date(coursSelectionne.getHoraire().getDate().getTime());
            datePicker.setValue(sqlDate.toLocalDate());
        }

        // Champs pour l'heure de début et l'heure de fin
        ComboBox<String> heureDebutCombo = new ComboBox<>();
        heureDebutCombo.setPromptText("Heure de début");
        for (int h = 8; h <= 17; h++) {
            heureDebutCombo.getItems().add(String.format("%02d:00", h));
            heureDebutCombo.getItems().add(String.format("%02d:30", h));
        }

        ComboBox<String> heureFinCombo = new ComboBox<>();
        heureFinCombo.setPromptText("Heure de fin");
        for (int h = 9; h <= 18; h++) {
            heureFinCombo.getItems().add(String.format("%02d:00", h));
            heureFinCombo.getItems().add(String.format("%02d:30", h));
        }

        // Définir les valeurs actuelles
        if (coursSelectionne.getHoraire() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String heureDebut = sdf.format(coursSelectionne.getHoraire().getHeureDebut());
            String heureFin = sdf.format(coursSelectionne.getHoraire().getHeureFin());

            heureDebutCombo.setValue(heureDebut);
            heureFinCombo.setValue(heureFin);
        }

        // Sélection de plusieurs groupes d'étudiants
        ListView<String> groupeListView = new ListView<>();
        groupeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Récupérer tous les groupes distincts depuis la base de données
        fr.planningcampus.planningcampus.dao.UtilisateurDAO utilisateurDAO = new fr.planningcampus.planningcampus.dao.UtilisateurDAO();
        groupeListView.setItems(FXCollections.observableArrayList(utilisateurDAO.getAllGroupes()));

        // Récupérer les groupes des étudiants de ce cours
        List<String> groupesCours = utilisateurDAO.getGroupesByCours(coursSelectionne.getId());
        for (String groupe : groupesCours) {
            int index = utilisateurDAO.getAllGroupes().indexOf(groupe);
            if (index >= 0) {
                groupeListView.getSelectionModel().select(index);
            }
        }

        // Créer le ComboBox des salles avant son utilisation dans l'écouteur
        ComboBox<Salle> salleCombo = new ComboBox<>();
        salleCombo.setPromptText("Salle");

        // Récupérer les salles depuis la base de données
        fr.planningcampus.planningcampus.dao.SalleDAO salleDAO = new fr.planningcampus.planningcampus.dao.SalleDAO();
        salleCombo.setItems(FXCollections.observableArrayList(salleDAO.getAllSalles()));
        salleCombo.setValue(coursSelectionne.getSalle());

        // Ajouter un écouteur pour mettre à jour les salles disponibles en fonction du nombre d'étudiants
        groupeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateSallesDisponibles(groupeListView, datePicker, heureDebutCombo, heureFinCombo, salleCombo);
        });

        salleCombo.setCellFactory(param -> new ListCell<Salle>() {
            @Override
            protected void updateItem(Salle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " (capacité: " + item.getCapacite() + ")");
                }
            }
        });
        salleCombo.setButtonCell(new ListCell<Salle>() {
            @Override
            protected void updateItem(Salle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNom() + " (capacité: " + item.getCapacite() + ")");
                }
            }
        });

        // Mettre à jour les salles disponibles
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSallesDisponibles(groupeListView, datePicker, heureDebutCombo, heureFinCombo, salleCombo);
        });

        heureDebutCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSallesDisponibles(groupeListView, datePicker, heureDebutCombo, heureFinCombo, salleCombo);
        });

        heureFinCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSallesDisponibles(groupeListView, datePicker, heureDebutCombo, heureFinCombo, salleCombo);
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Matière:"), 0, 0);
        grid.add(matiereField, 1, 0);
        grid.add(new Label("Enseignant:"), 0, 1);
        grid.add(enseignantCombo, 1, 1);
        grid.add(new Label("Date:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new Label("Heure de début:"), 0, 3);
        grid.add(heureDebutCombo, 1, 3);
        grid.add(new Label("Heure de fin:"), 0, 4);
        grid.add(heureFinCombo, 1, 4);
        grid.add(new Label("Groupes d'étudiants:"), 0, 5);

        // Définir une taille pour la ListView des groupes
        groupeListView.setPrefHeight(100);
        grid.add(groupeListView, 1, 5);

        grid.add(new Label("Salle:"), 0, 6);
        grid.add(salleCombo, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Conversion du résultat en objet Cours
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifierButtonType) {
                if (matiereField.getText().isEmpty() || enseignantCombo.getValue() == null ||
                        datePicker.getValue() == null || heureDebutCombo.getValue() == null ||
                        heureFinCombo.getValue() == null || groupeListView.getSelectionModel().getSelectedItems().isEmpty() ||
                        salleCombo.getValue() == null) {
                    showAlert("Erreur", "Veuillez remplir tous les champs.");
                    return null;
                }

                try {
                    // Créer un nouvel horaire
                    Seance seance = coursSelectionne.getHoraire();

                    // Mémoriser les valeurs originales pour la notification
                    String ancienJour = seance.getJour();
                    java.util.Date ancienneDate = seance.getDate();
                    java.util.Date ancienneHeureDebut = seance.getHeureDebut();
                    java.util.Date ancienneHeureFin = seance.getHeureFin();
                    String ancienMatiere = coursSelectionne.getMatiere();
                    int ancienneIdSalle = coursSelectionne.getSalle() != null ? coursSelectionne.getSalle().getId() : -1;
                    int ancienneIdEnseignant = coursSelectionne.getEnseignant() != null ? coursSelectionne.getEnseignant().getId() : -1;

                    // Mettre à jour les propriétés de l'horaire
                    java.time.LocalDate localDate = datePicker.getValue();
                    java.sql.Date date = java.sql.Date.valueOf(localDate);
                    seance.setDate(date);

                    // Définir le jour de la semaine à partir de la date
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(date);
                    int jourSemaine = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1; // 1 = Dimanche, 2 = Lundi, etc.
                    String[] joursSemaine = {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
                    seance.setJour(joursSemaine[jourSemaine]);

                    // Définir le numéro de semaine à partir de la date
                    int numeroSemaine = cal.get(java.util.Calendar.WEEK_OF_YEAR);
                    seance.setSemaine(numeroSemaine);

                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                    seance.setHeureDebut(sdf.parse(heureDebutCombo.getValue()));
                    seance.setHeureFin(sdf.parse(heureFinCombo.getValue()));

                    // Mettre à jour l'horaire dans la base de données
                    SeanceDAO seanceDAO = new SeanceDAO();
                    seanceDAO.updateHoraire(seance);

                    // Mettre à jour le cours
                    Cours cours = coursSelectionne;
                    cours.setMatiere(matiereField.getText());
                    cours.setEnseignant(enseignantCombo.getValue());
                    cours.setSalle(salleCombo.getValue());

                    // Mettre à jour le cours dans la base de données
                    fr.planningcampus.planningcampus.dao.CoursDAO coursDAO = new fr.planningcampus.planningcampus.dao.CoursDAO();

                    // Vérifier si l'enseignant a déjà un cours à cet horaire (sauf le cours actuel)
                    List<Cours> coursEnseignant = coursDAO.getCoursByEnseignant(enseignantCombo.getValue().getId());
                    for (Cours c : coursEnseignant) {
                        if (c.getId() != cours.getId() && c.getHoraire().enConflit(seance)) {
                            showAlert("Conflit d'horaire", "L'enseignant a déjà un cours programmé à cet horaire.");
                            return null;
                        }
                    }

                    // Vérifier si la salle est déjà occupée à cet horaire (sauf le cours actuel)
                    List<Cours> coursSalle = coursDAO.getCoursBySalle(salleCombo.getValue().getId());
                    for (Cours c : coursSalle) {
                        if (c.getId() != cours.getId() && c.getHoraire().enConflit(seance)) {
                            showAlert("Conflit de salle", "La salle est déjà occupée à cet horaire.");
                            return null;
                        }
                    }

                    // Vérifier si nous avons besoin d'envoyer des notifications
                    // (si le cours a été modifié par rapport à l'original)
                    boolean dateModifiee = !seance.getDate().equals(ancienneDate);
                    boolean heureModifiee = !seance.getHeureDebut().equals(ancienneHeureDebut) || !seance.getHeureFin().equals(ancienneHeureFin);
                    boolean jourModifie = !seance.getJour().equals(ancienJour);
                    boolean salleModifiee = coursSelectionne.getSalle() != null &&
                            (ancienneIdSalle == -1 || coursSelectionne.getSalle().getId() != ancienneIdSalle);
                    boolean enseignantModifie = coursSelectionne.getEnseignant() != null &&
                            (ancienneIdEnseignant == -1 || coursSelectionne.getEnseignant().getId() != ancienneIdEnseignant);
                    boolean matiereModifiee = !coursSelectionne.getMatiere().equals(ancienMatiere);

                    if (dateModifiee || heureModifiee || jourModifie || salleModifiee || enseignantModifie || matiereModifiee) {
                        // Récupérer les étudiants concernés par ce cours
                        List<String> groupes = utilisateurDAO.getGroupesByCours(coursSelectionne.getId());

                        // Pour chaque groupe, récupérer les étudiants et envoyer une notification
                        for (String groupe : groupes) {
                            List<Integer> etudiantsIds = utilisateurDAO.getEtudiantsIdsByGroupe(groupe);

                            // Créer le message de notification
                            StringBuilder message = new StringBuilder();
                            message.append("Modification du cours de ").append(coursSelectionne.getMatiere());

                            if (dateModifiee || jourModifie) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                message.append(". Nouvelle date: ").append(dateFormat.format(seance.getDate())).append(" (").append(seance.getJour()).append(")");
                            }

                            if (heureModifiee) {
                                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                                message.append(". Nouvel horaire: ").append(timeFormat.format(seance.getHeureDebut()))
                                        .append(" à ").append(timeFormat.format(seance.getHeureFin()));
                            }

                            if (salleModifiee && coursSelectionne.getSalle() != null) {
                                message.append(". Nouvelle salle: ").append(coursSelectionne.getSalle().getNom());
                            }

                            if (enseignantModifie && coursSelectionne.getEnseignant() != null) {
                                message.append(". Nouvel enseignant: ").append(coursSelectionne.getEnseignant().getPrenom())
                                        .append(" ").append(coursSelectionne.getEnseignant().getNom());
                            }

                            if (matiereModifiee) {
                                message.append(". Le cours ").append(ancienMatiere).append(" est devenu ").append(coursSelectionne.getMatiere());
                            }

                            // Envoyer une notification à chaque étudiant
                            fr.planningcampus.planningcampus.dao.NotificationDAO notificationDAO = new fr.planningcampus.planningcampus.dao.NotificationDAO();
                            for (Integer etudiantId : etudiantsIds) {
                                fr.planningcampus.planningcampus.model.Notification notification = new fr.planningcampus.planningcampus.model.Notification();
                                notification.setIdDestinataire(etudiantId);
                                notification.setIdExpediteur(administrateur.getId());
                                notification.setContenu(message.toString());
                                notification.setDate(new Date());
                                notification.setLue(false);

                                notificationDAO.addNotification(notification);
                            }
                        }
                    }

                    return cours;

                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Erreur", "Une erreur est survenue: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(cours -> {
            // Rafraîchir la liste des cours
            loadCours();
        });
    }

    /**
     * Met à jour la liste des salles disponibles en fonction du nombre d'étudiants et de l'horaire sélectionné
     */
    private void updateSallesDisponibles(ListView<String> groupeListView, DatePicker datePicker,
                                         ComboBox<String> heureDebutCombo, ComboBox<String> heureFinCombo,
                                         ComboBox<Salle> salleCombo) {
        if (groupeListView.getSelectionModel().getSelectedItems().isEmpty() || datePicker.getValue() == null ||
                heureDebutCombo.getValue() == null || heureFinCombo.getValue() == null) {
            return;
        }

        try {
            // Calculer le nombre total d'étudiants
            fr.planningcampus.planningcampus.dao.UtilisateurDAO utilisateurDAO = new fr.planningcampus.planningcampus.dao.UtilisateurDAO();
            int nbEtudiants = 0;
            for (String groupe : groupeListView.getSelectionModel().getSelectedItems()) {
                nbEtudiants += utilisateurDAO.countEtudiantsByGroupe(groupe);
            }

            // Créer un objet Horaire temporaire
            SeanceDAO seanceDAO = new SeanceDAO();
            Seance seance = new Seance();

            // Définir le jour de la semaine à partir de la date
            java.time.LocalDate localDate = datePicker.getValue();
            java.sql.Date date = java.sql.Date.valueOf(localDate);
            seance.setDate(date);

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(date);
            int jourSemaine = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1; // 1 = Dimanche, 2 = Lundi, etc.
            String[] joursSemaine = {"Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
            seance.setJour(joursSemaine[jourSemaine]);

            // Définir le numéro de semaine à partir de la date
            int numeroSemaine = cal.get(java.util.Calendar.WEEK_OF_YEAR);
            seance.setSemaine(numeroSemaine);

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
            seance.setHeureDebut(sdf.parse(heureDebutCombo.getValue()));
            seance.setHeureFin(sdf.parse(heureFinCombo.getValue()));

            // Sauvegarder l'horaire temporairement
            seanceDAO.addHoraire(seance);

            // Récupérer les salles disponibles
            fr.planningcampus.planningcampus.dao.CoursDAO coursDAO = new fr.planningcampus.planningcampus.dao.CoursDAO();
            List<Salle> sallesDisponibles = coursDAO.getSallesDisponibles(nbEtudiants, seance.getId());

            // Sauvegarder la sélection actuelle
            Salle salleSelectionnee = salleCombo.getValue();

            // Mettre à jour la liste des salles disponibles
            salleCombo.setItems(FXCollections.observableArrayList(sallesDisponibles));

            // Restaurer la sélection si elle est toujours disponible
            if (salleSelectionnee != null && sallesDisponibles.contains(salleSelectionnee)) {
                salleCombo.setValue(salleSelectionnee);
            }

            // Supprimer l'horaire temporaire
            seanceDAO.deleteHoraire(seance.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère l'événement pour marquer une notification comme lue
     */
    @FXML
    private void handleMarquerLue(ActionEvent event) {
        fr.planningcampus.planningcampus.model.Notification notificationSelectionnee = notificationsTable.getSelectionModel().getSelectedItem();

        if (notificationSelectionnee == null) {
            showAlert("Information", "Veuillez sélectionner une notification.");
            return;
        }

        fr.planningcampus.planningcampus.dao.NotificationDAO notificationDAO = new fr.planningcampus.planningcampus.dao.NotificationDAO();
        notificationSelectionnee.setLue(true);

        if (notificationDAO.updateNotification(notificationSelectionnee)) {
            loadNotifications();
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de la mise à jour de la notification.");
        }
    }

    /**
     * Charge les notifications depuis la base de données
     */
    private void loadNotifications() {
        fr.planningcampus.planningcampus.dao.NotificationDAO notificationDAO = new fr.planningcampus.planningcampus.dao.NotificationDAO();
        ObservableList<fr.planningcampus.planningcampus.model.Notification> notifications = FXCollections.observableArrayList(
                notificationDAO.getNotificationsByDestinataire(administrateur.getId()));
        notificationsTable.setItems(notifications);
    }

    /**
     * Gère l'événement d'envoi d'une notification
     */
    @FXML
    private void handleEnvoyerNotification(ActionEvent event) {
        // Création d'un dialogue pour envoyer une notification
        Dialog<fr.planningcampus.planningcampus.model.Notification> dialog = new Dialog<>();
        dialog.setTitle("Envoyer une notification");
        dialog.setHeaderText("Envoyer une notification aux utilisateurs");

        // Boutons
        ButtonType envoyerButtonType = new ButtonType("Envoyer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(envoyerButtonType, ButtonType.CANCEL);

        // Création des champs de formulaire
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label destinatairesLabel = new Label("Destinataires:");

        // Choix du type de destinataire
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Tous les utilisateurs", "Tous les enseignants", "Tous les étudiants", "Utilisateur spécifique");
        typeCombo.setValue("Tous les enseignants");

        // Zone pour sélectionner un utilisateur spécifique (visible seulement si "Utilisateur spécifique" est sélectionné)
        HBox utilisateurBox = new HBox(10);
        utilisateurBox.setVisible(false);
        utilisateurBox.setManaged(false);

        Label utilisateurLabel = new Label("Sélectionner l'utilisateur:");
        ComboBox<Utilisateur> utilisateurCombo = new ComboBox<>();

        // Récupérer tous les utilisateurs
        ObservableList<Utilisateur> utilisateurs = FXCollections.observableArrayList(utilisateurDAO.getAllUtilisateurs());
        utilisateurCombo.setItems(utilisateurs);
        utilisateurCombo.setCellFactory(param -> new ListCell<Utilisateur>() {
            @Override
            protected void updateItem(Utilisateur item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPrenom() + " " + item.getNom() + " (" + item.getEmail() + ")");
                }
            }
        });
        utilisateurCombo.setButtonCell(new ListCell<Utilisateur>() {
            @Override
            protected void updateItem(Utilisateur item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getPrenom() + " " + item.getNom());
                }
            }
        });

        utilisateurBox.getChildren().addAll(utilisateurLabel, utilisateurCombo);

        // Afficher la zone de sélection d'utilisateur spécifique si nécessaire
        typeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isSpecifique = "Utilisateur spécifique".equals(newVal);
            utilisateurBox.setVisible(isSpecifique);
            utilisateurBox.setManaged(isSpecifique);

            if (isSpecifique && utilisateurCombo.getValue() == null && !utilisateurs.isEmpty()) {
                utilisateurCombo.setValue(utilisateurs.get(0));
            }
        });

        // Champ pour le contenu de la notification
        Label contenuLabel = new Label("Contenu de la notification:");
        TextArea contenuArea = new TextArea();
        contenuArea.setPrefHeight(100);

        // Ajouter tous les champs
        vbox.getChildren().addAll(
                destinatairesLabel,
                typeCombo,
                utilisateurBox,
                contenuLabel,
                contenuArea
        );

        dialog.getDialogPane().setContent(vbox);

        // Focus sur le premier champ
        typeCombo.requestFocus();

        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == envoyerButtonType) {
                // Vérifier que le contenu n'est pas vide
                if (contenuArea.getText().isEmpty()) {
                    showAlert("Erreur", "Le contenu de la notification ne peut pas être vide.");
                    return null;
                }

                try {
                    fr.planningcampus.planningcampus.dao.NotificationDAO notificationDAO = new fr.planningcampus.planningcampus.dao.NotificationDAO();
                    List<Integer> destinatairesIds = new ArrayList<>();

                    // Déterminer les destinataires selon le type sélectionné
                    switch (typeCombo.getValue()) {
                        case "Tous les utilisateurs":
                            for (Utilisateur u : utilisateurs) {
                                if (u.getId() != administrateur.getId()) { // Ne pas s'envoyer à soi-même
                                    destinatairesIds.add(u.getId());
                                }
                            }
                            break;

                        case "Tous les enseignants":
                            for (Utilisateur u : utilisateurs) {
                                if (u instanceof fr.planningcampus.planningcampus.model.Enseignant) {
                                    destinatairesIds.add(u.getId());
                                }
                            }
                            break;

                        case "Tous les étudiants":
                            for (Utilisateur u : utilisateurs) {
                                if (u instanceof fr.planningcampus.planningcampus.model.Etudiant) {
                                    destinatairesIds.add(u.getId());
                                }
                            }
                            break;

                        case "Utilisateur spécifique":
                            if (utilisateurCombo.getValue() != null) {
                                destinatairesIds.add(utilisateurCombo.getValue().getId());
                            } else {
                                showAlert("Erreur", "Veuillez sélectionner un utilisateur.");
                                return null;
                            }
                            break;
                    }

                    // Vérifier qu'au moins un destinataire est sélectionné
                    if (destinatairesIds.isEmpty()) {
                        showAlert("Erreur", "Aucun destinataire sélectionné.");
                        return null;
                    }

                    // Créer et envoyer les notifications
                    for (Integer destinataireId : destinatairesIds) {
                        fr.planningcampus.planningcampus.model.Notification notification = new fr.planningcampus.planningcampus.model.Notification();
                        notification.setIdDestinataire(destinataireId);
                        notification.setIdExpediteur(administrateur.getId());
                        notification.setContenu(contenuArea.getText());
                        notification.setDate(new Date());
                        notification.setLue(false);

                        notificationDAO.addNotification(notification);
                    }

                    showAlert("Succès", "Les notifications ont été envoyées (" + destinatairesIds.size() + " destinataires).");
                    return null;

                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Erreur", "Une erreur est survenue: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }
}