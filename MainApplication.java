package fr.isep.algo.gestionemploisdutemps;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class MainApplication extends Application {

    private Stage primaryStage;
    private Utilisateur utilisateurConnecte;
    private Authentification authentification;
    private GestionSalles gestionSalles;
    private EmploiDuTemps emploiDuTemps;

    // Listes pour stocker les données
    private ObservableList<Cours> listeCours = FXCollections.observableArrayList();
    private ObservableList<Salle> listeSalles = FXCollections.observableArrayList();
    private ObservableList<Utilisateur> listeUtilisateurs = FXCollections.observableArrayList();
    private ObservableList<Seance> listeSeances = FXCollections.observableArrayList();
    private ObservableList<Notification> listeNotifications = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.authentification = new Authentification();
        this.gestionSalles = new GestionSalles();
        this.emploiDuTemps = new EmploiDuTemps(1, new Date(), new Date());

        initializeData();
        showLoginScreen();
    }

    private void initializeData() {
        // Initialisation des données d'exemple

        // Création d'utilisateurs d'exemple
        Administrateur admin = new Administrateur(1, "Dupont", "Jean", "admin@isep.fr", "admin123", "Directeur");
        Enseignant enseignant = new Enseignant(2, "Martin", "Marie", "marie@isep.fr", "prof123", "Informatique", 20);
        Etudiant etudiant = new Etudiant(3, "Durand", "Paul", "paul@isep.fr", "etud123", 12345);
        etudiant.setFiliere("Informatique");
        etudiant.setNiveau("L3");

        listeUtilisateurs.addAll(admin, enseignant, etudiant);

        // Création de cours d'exemple
        Cours cours1 = new Cours(1, "Algorithmique", "Introduction aux algorithmes", 90, "Cours magistral");
        Cours cours2 = new Cours(2, "Base de données", "Conception de BDD", 120, "TP");
        listeCours.addAll(cours1, cours2);

        // Création de salles d'exemple
        List<String> equipements1 = Arrays.asList("Projecteur", "Ordinateur", "Tableau");
        List<String> equipements2 = Arrays.asList("Ordinateurs", "Réseau", "Climatisation");
        Salle salle1 = new Salle("A101", "Bâtiment A, 1er étage", 30, equipements1);
        Salle salle2 = new Salle("B205", "Bâtiment B, 2ème étage", 25, equipements2);
        listeSalles.addAll(salle1, salle2);
        gestionSalles.ajSalle(salle1);
        gestionSalles.ajSalle(salle2);

        // Création de séances d'exemple
        Seance seance1 = new Seance(1, new Date(), new Date(), true, cours1);
        Seance seance2 = new Seance(2, new Date(), new Date(), false, cours2);
        listeSeances.addAll(seance1, seance2);

        // Création de notifications d'exemple
        Notification notif1 = new Notification(1, "Nouvelle séance ajoutée", new Date(), false);
        Notification notif2 = new Notification(2, "Modification d'emploi du temps", new Date(), true);
        listeNotifications.addAll(notif1, notif2);
    }

    private void showLoginScreen() {
        VBox loginLayout = new VBox(20);
        loginLayout.setAlignment(Pos.CENTER);
        loginLayout.setPadding(new Insets(50));
        loginLayout.setStyle("-fx-background-color: #f0f8ff;");

        Label titleLabel = new Label("IsePlanning - Connexion");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#114686"));

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(300);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setMaxWidth(300);

        Button loginButton = new Button("Se connecter");
        loginButton.setStyle("-fx-background-color: #114686; -fx-text-fill: white; -fx-font-size: 14px;");
        loginButton.setMinWidth(200);

        Label statusLabel = new Label();
        statusLabel.setTextFill(Color.RED);

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (authentification.authentifier(email, password)) {
                // Trouver l'utilisateur correspondant
                utilisateurConnecte = listeUtilisateurs.stream()
                        .filter(u -> u.getEmail().equals(email))
                        .findFirst()
                        .orElse(null);

                if (utilisateurConnecte != null) {
                    utilisateurConnecte.seConnecter();
                    showMainInterface();
                } else {
                    statusLabel.setText("Utilisateur non trouvé");
                }
            } else {
                statusLabel.setText("Email ou mot de passe incorrect");
            }
        });

        loginLayout.getChildren().addAll(titleLabel, emailField, passwordField, loginButton, statusLabel);

        Scene loginScene = new Scene(loginLayout, 800, 600);
        primaryStage.setTitle("IsePlanning - Connexion");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void showMainInterface() {
        BorderPane mainLayout = new BorderPane();

        // Header
        HBox header = createHeader();
        mainLayout.setTop(header);

        // Menu principal basé sur le type d'utilisateur
        VBox mainMenu = createMainMenu();
        mainLayout.setCenter(mainMenu);

        Scene mainScene = new Scene(mainLayout, 1000, 700);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("IsePlanning - " + utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom());
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPrefHeight(60);
        header.setStyle("-fx-background-color: #c3cedd;");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));

        Label titleLabel = new Label("IsePlanning");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#114686"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Connecté: " + utilisateurConnecte.getNom() + " " + utilisateurConnecte.getPrenom());
        userLabel.setFont(Font.font("Century Gothic", 14));

        Button logoutButton = new Button("Déconnexion");
        logoutButton.setStyle("-fx-background-color: #114686; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            utilisateurConnecte.seDeconnecter();
            showLoginScreen();
        });

        header.getChildren().addAll(titleLabel, spacer, userLabel, logoutButton);
        return header;
    }

    private VBox createMainMenu() {
        VBox menu = new VBox(20);
        menu.setPadding(new Insets(30));
        menu.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Bienvenue, " + utilisateurConnecte.getPrenom() + "!");
        welcomeLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        menu.getChildren().add(welcomeLabel);

        if (utilisateurConnecte instanceof Administrateur) {
            menu.getChildren().addAll(createAdminMenu());
        } else if (utilisateurConnecte instanceof Enseignant) {
            menu.getChildren().addAll(createEnseignantMenu());
        } else if (utilisateurConnecte instanceof Etudiant) {
            menu.getChildren().addAll(createEtudiantMenu());
        }

        return menu;
    }

    private VBox createAdminMenu() {
        VBox adminMenu = new VBox(15);
        adminMenu.setAlignment(Pos.CENTER);

        Button gestionCoursBtn = new Button("Gestion des Cours");
        Button gestionSallesBtn = new Button("Gestion des Salles");
        Button gestionUtilisateursBtn = new Button("Gestion des Utilisateurs");
        Button gestionEmploiBtn = new Button("Gestion Emploi du Temps");
        Button statistiquesBtn = new Button("Statistiques");
        Button notificationsBtn = new Button("Notifications");

        styleButton(gestionCoursBtn);
        styleButton(gestionSallesBtn);
        styleButton(gestionUtilisateursBtn);
        styleButton(gestionEmploiBtn);
        styleButton(statistiquesBtn);
        styleButton(notificationsBtn);

        gestionCoursBtn.setOnAction(e -> showGestionCours());
        gestionSallesBtn.setOnAction(e -> showGestionSalles());
        gestionUtilisateursBtn.setOnAction(e -> showGestionUtilisateurs());
        gestionEmploiBtn.setOnAction(e -> showGestionEmploi());
        statistiquesBtn.setOnAction(e -> showStatistiques());
        notificationsBtn.setOnAction(e -> showNotifications());

        adminMenu.getChildren().addAll(gestionCoursBtn, gestionSallesBtn, gestionUtilisateursBtn,
                gestionEmploiBtn, statistiquesBtn, notificationsBtn);

        return adminMenu;
    }

    private VBox createEnseignantMenu() {
        VBox enseignantMenu = new VBox(15);
        enseignantMenu.setAlignment(Pos.CENTER);

        Button consulterEmploiBtn = new Button("Consulter Emploi du Temps");
        Button consulterCoursBtn = new Button("Mes Cours");
        Button signalerAnomalieBtn = new Button("Signaler une Anomalie");
        Button notificationsBtn = new Button("Notifications");

        styleButton(consulterEmploiBtn);
        styleButton(consulterCoursBtn);
        styleButton(signalerAnomalieBtn);
        styleButton(notificationsBtn);

        Enseignant enseignant = (Enseignant) utilisateurConnecte;

        consulterEmploiBtn.setOnAction(e -> {
            enseignant.consulterEmploiDuTemps();
            showEmploiDuTemps();
        });

        consulterCoursBtn.setOnAction(e -> {
            enseignant.consulterInfosCours();
            enseignant.getCoursEnseignes();
            showMesCours();
        });

        signalerAnomalieBtn.setOnAction(e -> {
            enseignant.signalerAnomalie();
            showSignalerAnomalie();
        });

        notificationsBtn.setOnAction(e -> showNotifications());

        enseignantMenu.getChildren().addAll(consulterEmploiBtn, consulterCoursBtn,
                signalerAnomalieBtn, notificationsBtn);

        return enseignantMenu;
    }

    private VBox createEtudiantMenu() {
        VBox etudiantMenu = new VBox(15);
        etudiantMenu.setAlignment(Pos.CENTER);

        Button consulterEmploiBtn = new Button("Mon Emploi du Temps");
        Button consulterSalleBtn = new Button("Informations Salles");
        Button notificationsBtn = new Button("Mes Notifications");

        styleButton(consulterEmploiBtn);
        styleButton(consulterSalleBtn);
        styleButton(notificationsBtn);

        Etudiant etudiant = (Etudiant) utilisateurConnecte;

        consulterEmploiBtn.setOnAction(e -> {
            etudiant.consulterEmploiDuTemps();
            showEmploiDuTemps();
        });

        consulterSalleBtn.setOnAction(e -> {
            etudiant.consulterSalle();
            showInfosSalles();
        });

        notificationsBtn.setOnAction(e -> showNotifications());

        etudiantMenu.getChildren().addAll(consulterEmploiBtn, consulterSalleBtn, notificationsBtn);

        return etudiantMenu;
    }

    private void styleButton(Button button) {
        button.setMinWidth(250);
        button.setMinHeight(40);
        button.setStyle("-fx-background-color: #114686; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
    }

    // Méthodes pour afficher les différentes interfaces

    private void showGestionCours() {
        Stage coursStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Gestion des Cours");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        TableView<Cours> tableView = new TableView<>();
        tableView.setItems(listeCours);

        TableColumn<Cours, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdCours()).asObject());

        TableColumn<Cours, String> matiereCol = new TableColumn<>("Matière");
        matiereCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMatiere()));

        TableColumn<Cours, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        TableColumn<Cours, Integer> dureeCol = new TableColumn<>("Durée");
        dureeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDuree()).asObject());

        TableColumn<Cours, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTypeCours()));

        tableView.getColumns().addAll(idCol, matiereCol, descCol, dureeCol, typeCol);

        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Ajouter");
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);

        addButton.setOnAction(e -> showAddCoursDialog());
        editButton.setOnAction(e -> {
            Cours selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditCoursDialog(selected);
            }
        });
        deleteButton.setOnAction(e -> {
            Cours selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                listeCours.remove(selected);
            }
        });

        buttonBox.getChildren().addAll(addButton, editButton, deleteButton);
        layout.getChildren().addAll(titleLabel, tableView, buttonBox);

        Scene scene = new Scene(layout, 800, 500);
        coursStage.setTitle("Gestion des Cours");
        coursStage.setScene(scene);
        coursStage.show();
    }

    private void showGestionSalles() {
        Stage salleStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Gestion des Salles");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        TableView<Salle> tableView = new TableView<>();
        tableView.setItems(listeSalles);

        TableColumn<Salle, String> idCol = new TableColumn<>("ID Salle");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdSalle()));

        TableColumn<Salle, String> locCol = new TableColumn<>("Localisation");
        locCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocalisation()));

        TableColumn<Salle, Integer> capCol = new TableColumn<>("Capacité");
        capCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCapacite()).asObject());

        TableColumn<Salle, String> equipCol = new TableColumn<>("Équipements");
        equipCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.join(", ", data.getValue().getEquipements())));

        tableView.getColumns().addAll(idCol, locCol, capCol, equipCol);

        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Ajouter");
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");
        Button statsButton = new Button("Statistiques");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(statsButton);

        addButton.setOnAction(e -> showAddSalleDialog());
        editButton.setOnAction(e -> {
            Salle selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditSalleDialog(selected);
            }
        });
        deleteButton.setOnAction(e -> {
            Salle selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                gestionSalles.supprimer(selected.getIdSalle());
                listeSalles.remove(selected);
            }
        });
        statsButton.setOnAction(e -> gestionSalles.gererStats());

        buttonBox.getChildren().addAll(addButton, editButton, deleteButton, statsButton);
        layout.getChildren().addAll(titleLabel, tableView, buttonBox);

        Scene scene = new Scene(layout, 900, 500);
        salleStage.setTitle("Gestion des Salles");
        salleStage.setScene(scene);
        salleStage.show();
    }

    private void showGestionUtilisateurs() {
        Stage userStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Gestion des Utilisateurs");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        TableView<Utilisateur> tableView = new TableView<>();
        tableView.setItems(listeUtilisateurs);

        TableColumn<Utilisateur, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdUtilisateur()).asObject());

        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));

        TableColumn<Utilisateur, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));

        TableColumn<Utilisateur, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Utilisateur, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getClass().getSimpleName()));

        tableView.getColumns().addAll(idCol, nomCol, prenomCol, emailCol, typeCol);

        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Ajouter");
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);

        addButton.setOnAction(e -> showAddUserDialog());
        editButton.setOnAction(e -> {
            Utilisateur selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                authentification.modifierUtilisateur(selected);
                showEditUserDialog(selected);
            }
        });
        deleteButton.setOnAction(e -> {
            Utilisateur selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                authentification.supprimerUtilisateur(selected.getIdUtilisateur());
                listeUtilisateurs.remove(selected);
            }
        });

        buttonBox.getChildren().addAll(addButton, editButton, deleteButton);
        layout.getChildren().addAll(titleLabel, tableView, buttonBox);

        Scene scene = new Scene(layout, 800, 500);
        userStage.setTitle("Gestion des Utilisateurs");
        userStage.setScene(scene);
        userStage.show();
    }

    private void showGestionEmploi() {
        Stage emploiStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Gestion de l'Emploi du Temps");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        TableView<Seance> tableView = new TableView<>();
        tableView.setItems(listeSeances);

        TableColumn<Seance, String> idCol = new TableColumn<>("ID Séance");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().hashCode())));

        TableColumn<Seance, String> coursCol = new TableColumn<>("Cours");
        coursCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().cours != null ? data.getValue().cours.getMatiere() : "Non défini"));

        TableColumn<Seance, String> debutCol = new TableColumn<>("Début");
        debutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDateDebut().toString()));

        TableColumn<Seance, String> finCol = new TableColumn<>("Fin");
        finCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDateFin().toString()));

        tableView.getColumns().addAll(idCol, coursCol, debutCol, finCol);

        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Ajouter Séance");
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");
        Button conflitsButton = new Button("Vérifier Conflits");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(deleteButton);
        styleButton(conflitsButton);

        addButton.setOnAction(e -> showAddSeanceDialog());
        editButton.setOnAction(e -> {
            Seance selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEditSeanceDialog(selected);
            }
        });
        deleteButton.setOnAction(e -> {
            Seance selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                emploiDuTemps.supprimerSeance(selected);
                listeSeances.remove(selected);
            }
        });
        conflitsButton.setOnAction(e -> emploiDuTemps.verifierConflits());

        if (utilisateurConnecte instanceof Administrateur) {
            Administrateur admin = (Administrateur) utilisateurConnecte;
            addButton.setOnAction(e -> {
                admin.modifierEmploiDuTemps();
                showAddSeanceDialog();
            });
        }

        buttonBox.getChildren().addAll(addButton, editButton, deleteButton, conflitsButton);
        layout.getChildren().addAll(titleLabel, tableView, buttonBox);

        Scene scene = new Scene(layout, 900, 500);
        emploiStage.setTitle("Gestion de l'Emploi du Temps");
        emploiStage.setScene(scene);
        emploiStage.show();
    }

    private void showStatistiques() {
        if (utilisateurConnecte instanceof Administrateur) {
            Administrateur admin = (Administrateur) utilisateurConnecte;
            admin.gererStats();

            Stage statsStage = new Stage();
            VBox layout = new VBox(20);
            layout.setPadding(new Insets(20));
            layout.setAlignment(Pos.CENTER);

            Label titleLabel = new Label("Statistiques du Système");
            titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

            Label statsLabel = new Label(
                    "Nombre total d'utilisateurs: " + listeUtilisateurs.size() + "\n" +
                            "Nombre de cours: " + listeCours.size() + "\n" +
                            "Nombre de salles: " + listeSalles.size() + "\n" +
                            "Nombre de séances: " + listeSeances.size() + "\n" +
                            "Notifications en attente: " + listeNotifications.filtered(n -> !n.isMarqLue()).size()
            );
            statsLabel.setFont(Font.font("Century Gothic", 14));

            Button refreshButton = new Button("Actualiser");
            styleButton(refreshButton);
            refreshButton.setOnAction(e -> {
                gestionSalles.gererStats();
                statsStage.close();
                showStatistiques();
            });

            layout.getChildren().addAll(titleLabel, statsLabel, refreshButton);

            Scene scene = new Scene(layout, 400, 300);
            statsStage.setTitle("Statistiques");
            statsStage.setScene(scene);
            statsStage.show();
        }
    }

    private void showNotifications() {
        Stage notifStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Notifications");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        ListView<Notification> listView = new ListView<>();
        listView.setItems(listeNotifications);

        listView.setCellFactory(param -> new ListCell<Notification>() {
            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMessage() + " - " +
                            (item.isMarqLue() ? "Lu" : "Non lu") +
                            " (" + item.getDateEnvoi() + ")");
                    if (!item.isMarqLue()) {
                        setStyle("-fx-background-color: #e6f3ff;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        Button marquerLueButton = new Button("Marquer comme lu");
        styleButton(marquerLueButton);

        marquerLueButton.setOnAction(e -> {
            Notification selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.marquerLue();
                listView.refresh();

                if (utilisateurConnecte instanceof Etudiant) {
                    ((Etudiant) utilisateurConnecte).recevoirNotification(selected.getMessage());
                }
            }
        });

        layout.getChildren().addAll(titleLabel, listView, marquerLueButton);

        Scene scene = new Scene(layout, 600, 400);
        notifStage.setTitle("Notifications");
        notifStage.setScene(scene);
        notifStage.show();
    }

    private void showEmploiDuTemps() {
        Stage emploiStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Emploi du Temps");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        TableView<Seance> tableView = new TableView<>();
        tableView.setItems(listeSeances);

        TableColumn<Seance, String> coursCol = new TableColumn<>("Cours");
        coursCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().cours != null ? data.getValue().cours.getMatiere() : "Non défini"));

        TableColumn<Seance, String> debutCol = new TableColumn<>("Date/Heure Début");
        debutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDateDebut().toString()));

        TableColumn<Seance, String> finCol = new TableColumn<>("Date/Heure Fin");
        finCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getDateFin().toString()));

        TableColumn<Seance, String> infoCol = new TableColumn<>("Informations");
        infoCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getInformations()));

        tableView.getColumns().addAll(coursCol, debutCol, finCol, infoCol);

        HBox buttonBox = new HBox(10);
        Button jourButton = new Button("Afficher Jour");
        Button semaineButton = new Button("Afficher Semaine");
        Button moisButton = new Button("Afficher Mois");

        styleButton(jourButton);
        styleButton(semaineButton);
        styleButton(moisButton);

        jourButton.setOnAction(e -> emploiDuTemps.afficherJour(new Date()));
        semaineButton.setOnAction(e -> emploiDuTemps.afficherSemaine(new Date()));
        moisButton.setOnAction(e -> emploiDuTemps.afficherMois(new Date()));

        buttonBox.getChildren().addAll(jourButton, semaineButton, moisButton);
        layout.getChildren().addAll(titleLabel, tableView, buttonBox);

        Scene scene = new Scene(layout, 900, 500);
        emploiStage.setTitle("Emploi du Temps");
        emploiStage.setScene(scene);
        emploiStage.show();
    }

    private void showMesCours() {
        Stage coursStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Mes Cours");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        ListView<Cours> listView = new ListView<>();
        listView.setItems(listeCours);

        listView.setCellFactory(param -> new ListCell<Cours>() {
            @Override
            protected void updateItem(Cours item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getInformations());
                }
            }
        });

        layout.getChildren().addAll(titleLabel, listView);

        Scene scene = new Scene(layout, 600, 400);
        coursStage.setTitle("Mes Cours");
        coursStage.setScene(scene);
        coursStage.show();
    }

    private void showSignalerAnomalie() {
        Stage anomalieStage = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Signaler une Anomalie");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Décrivez l'anomalie rencontrée...");
        descriptionArea.setPrefRowCount(5);
        descriptionArea.setMaxWidth(400);

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Problème de salle", "Conflit d'horaire", "Matériel défaillant", "Autre");
        typeCombo.setPromptText("Type d'anomalie");

        Button signalerButton = new Button("Signaler");
        styleButton(signalerButton);

        Label statusLabel = new Label();
        statusLabel.setTextFill(Color.GREEN);

        signalerButton.setOnAction(e -> {
            if (!descriptionArea.getText().isEmpty() && typeCombo.getValue() != null) {
                // Créer une nouvelle notification pour l'anomalie
                Notification anomalieNotif = new Notification(
                        listeNotifications.size() + 1,
                        "Anomalie signalée par " + utilisateurConnecte.getNom() + ": " +
                                typeCombo.getValue() + " - " + descriptionArea.getText(),
                        new Date(),
                        false
                );
                listeNotifications.add(anomalieNotif);
                statusLabel.setText("Anomalie signalée avec succès!");
                descriptionArea.clear();
                typeCombo.setValue(null);
            } else {
                statusLabel.setText("Veuillez remplir tous les champs");
                statusLabel.setTextFill(Color.RED);
            }
        });

        layout.getChildren().addAll(titleLabel, typeCombo, descriptionArea, signalerButton, statusLabel);

        Scene scene = new Scene(layout, 500, 400);
        anomalieStage.setTitle("Signaler une Anomalie");
        anomalieStage.setScene(scene);
        anomalieStage.show();
    }

    private void showInfosSalles() {
        Stage salleStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Informations des Salles");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 18));

        ListView<Salle> listView = new ListView<>();
        listView.setItems(listeSalles);

        listView.setCellFactory(param -> new ListCell<Salle>() {
            @Override
            protected void updateItem(Salle item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getInformations());
                }
            }
        });

        layout.getChildren().addAll(titleLabel, listView);

        Scene scene = new Scene(layout, 600, 400);
        salleStage.setTitle("Informations des Salles");
        salleStage.setScene(scene);
        salleStage.show();
    }

    // Méthodes pour les dialogues d'ajout/modification

    private void showAddCoursDialog() {
        Stage dialog = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Ajouter un Cours");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 16));

        TextField idField = new TextField();
        idField.setPromptText("ID du cours");

        TextField matiereField = new TextField();
        matiereField.setPromptText("Matière");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField dureeField = new TextField();
        dureeField.setPromptText("Durée (en minutes)");

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Cours magistral", "TD", "TP", "Conférence");
        typeCombo.setPromptText("Type de cours");

        Button addButton = new Button("Ajouter");
        Button cancelButton = new Button("Annuler");

        styleButton(addButton);
        styleButton(cancelButton);

        addButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int duree = Integer.parseInt(dureeField.getText());

                Cours nouveauCours = new Cours(id, matiereField.getText(),
                        descriptionField.getText(), duree, typeCombo.getValue());
                listeCours.add(nouveauCours);
                dialog.close();
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer des valeurs numériques valides pour l'ID et la durée.");
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, cancelButton);

        layout.getChildren().addAll(titleLabel, idField, matiereField, descriptionField,
                dureeField, typeCombo, buttonBox);

        Scene scene = new Scene(layout, 400, 350);
        dialog.setTitle("Ajouter un Cours");
        dialog.setScene(scene);
        dialog.show();
    }

    private void showEditCoursDialog(Cours cours) {
        Stage dialog = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Modifier le Cours");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 16));

        TextField idField = new TextField(String.valueOf(cours.getIdCours()));
        TextField matiereField = new TextField(cours.getMatiere());
        TextField descriptionField = new TextField(cours.getDescription());
        TextField dureeField = new TextField(String.valueOf(cours.getDuree()));

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Cours magistral", "TD", "TP", "Conférence");
        typeCombo.setValue(cours.getTypeCours());

        Button saveButton = new Button("Sauvegarder");
        Button cancelButton = new Button("Annuler");

        styleButton(saveButton);
        styleButton(cancelButton);

        saveButton.setOnAction(e -> {
            try {
                cours.setIdCours(Integer.parseInt(idField.getText()));
                cours.setMatiere(matiereField.getText());
                cours.setDescription(descriptionField.getText());
                cours.setDuree(Integer.parseInt(dureeField.getText()));
                cours.setTypeCours(typeCombo.getValue());
                dialog.close();
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer des valeurs numériques valides.");
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        layout.getChildren().addAll(titleLabel, idField, matiereField, descriptionField,
                dureeField, typeCombo, buttonBox);

        Scene scene = new Scene(layout, 400, 350);
        dialog.setTitle("Modifier le Cours");
        dialog.setScene(scene);
        dialog.show();
    }

    private void showAddSalleDialog() {
        Stage dialog = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Ajouter une Salle");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 16));

        TextField idField = new TextField();
        idField.setPromptText("ID de la salle");

        TextField localisationField = new TextField();
        localisationField.setPromptText("Localisation");

        TextField capaciteField = new TextField();
        capaciteField.setPromptText("Capacité");

        TextArea equipementsArea = new TextArea();
        equipementsArea.setPromptText("Équipements (séparés par des virgules)");
        equipementsArea.setPrefRowCount(3);

        Button addButton = new Button("Ajouter");
        Button cancelButton = new Button("Annuler");

        styleButton(addButton);
        styleButton(cancelButton);

        addButton.setOnAction(e -> {
            try {
                int capacite = Integer.parseInt(capaciteField.getText());
                List<String> equipements = Arrays.asList(equipementsArea.getText().split(","));
                for (int i = 0; i < equipements.size(); i++) {
                    equipements.set(i, equipements.get(i).trim());
                }

                Salle nouvelleSalle = new Salle(idField.getText(), localisationField.getText(),
                        capacite, equipements);
                gestionSalles.ajSalle(nouvelleSalle);
                listeSalles.add(nouvelleSalle);
                dialog.close();
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer une valeur numérique valide pour la capacité.");
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, cancelButton);

        layout.getChildren().addAll(titleLabel, idField, localisationField, capaciteField,
                equipementsArea, buttonBox);

        Scene scene = new Scene(layout, 450, 400);
        dialog.setTitle("Ajouter une Salle");
        dialog.setScene(scene);
        dialog.show();
    }

    private void showEditSalleDialog(Salle salle) {
        Stage dialog = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Modifier la Salle");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 16));

        TextField idField = new TextField(salle.getIdSalle());
        TextField localisationField = new TextField(salle.getLocalisation());
        TextField capaciteField = new TextField(String.valueOf(salle.getCapacite()));

        TextArea equipementsArea = new TextArea(String.join(", ", salle.getEquipements()));
        equipementsArea.setPrefRowCount(3);

        Button saveButton = new Button("Sauvegarder");
        Button cancelButton = new Button("Annuler");

        styleButton(saveButton);
        styleButton(cancelButton);

        saveButton.setOnAction(e -> {
            try {
                salle.setIdSalle(idField.getText());
                salle.setLocalisation(localisationField.getText());
                salle.setCapacite(Integer.parseInt(capaciteField.getText()));

                List<String> equipements = Arrays.asList(equipementsArea.getText().split(","));
                for (int i = 0; i < equipements.size(); i++) {
                    equipements.set(i, equipements.get(i).trim());
                }
                salle.setEquipements(equipements);

                gestionSalles.modifierSalle(salle);
                dialog.close();
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer une valeur numérique valide pour la capacité.");
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        layout.getChildren().addAll(titleLabel, idField, localisationField, capaciteField,
                equipementsArea, buttonBox);

        Scene scene = new Scene(layout, 450, 400);
        dialog.setTitle("Modifier la Salle");
        dialog.setScene(scene);
        dialog.show();
    }

    private void showAddUserDialog() {
        Stage dialog = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Ajouter un Utilisateur");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 16));

        TextField idField = new TextField();
        idField.setPromptText("ID utilisateur");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Administrateur", "Enseignant", "Etudiant");
        typeCombo.setPromptText("Type d'utilisateur");

        // Champs spécifiques selon le type
        TextField specialField = new TextField();
        specialField.setPromptText("Spécialité/Fonction/Numéro étudiant");
        specialField.setVisible(false);

        TextField heuresField = new TextField();
        heuresField.setPromptText("Nombre d'heures (pour enseignant)");
        heuresField.setVisible(false);

        typeCombo.setOnAction(e -> {
            String type = typeCombo.getValue();
            if (type != null) {
                specialField.setVisible(true);
                if (type.equals("Enseignant")) {
                    heuresField.setVisible(true);
                    specialField.setPromptText("Spécialité");
                } else if (type.equals("Administrateur")) {
                    heuresField.setVisible(false);
                    specialField.setPromptText("Fonction");
                } else if (type.equals("Etudiant")) {
                    heuresField.setVisible(false);
                    specialField.setPromptText("Numéro étudiant");
                }
            }
        });

        Button addButton = new Button("Ajouter");
        Button cancelButton = new Button("Annuler");

        styleButton(addButton);
        styleButton(cancelButton);

        addButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String type = typeCombo.getValue();

                Utilisateur nouvelUtilisateur = null;

                switch (type) {
                    case "Administrateur":
                        nouvelUtilisateur = new Administrateur(id, nomField.getText(),
                                prenomField.getText(), emailField.getText(),
                                passwordField.getText(), specialField.getText());
                        break;
                    case "Enseignant":
                        int heures = Integer.parseInt(heuresField.getText());
                        nouvelUtilisateur = new Enseignant(id, nomField.getText(),
                                prenomField.getText(), emailField.getText(),
                                passwordField.getText(), specialField.getText(), heures);
                        break;
                    case "Etudiant":
                        int numEtud = Integer.parseInt(specialField.getText());
                        nouvelUtilisateur = new Etudiant(id, nomField.getText(),
                                prenomField.getText(), emailField.getText(),
                                passwordField.getText(), numEtud);
                        break;
                }

                if (nouvelUtilisateur != null) {
                    authentification.creerUtilisateur(nouvelUtilisateur);
                    listeUtilisateurs.add(nouvelUtilisateur);
                    dialog.close();
                }
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer des valeurs numériques valides.");
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, cancelButton);

        layout.getChildren().addAll(titleLabel, idField, nomField, prenomField, emailField,
                passwordField, typeCombo, specialField, heuresField, buttonBox);

        Scene scene = new Scene(layout, 450, 500);
        dialog.setTitle("Ajouter un Utilisateur");
        dialog.setScene(scene);
        dialog.show();
    }

    private void showEditUserDialog(Utilisateur utilisateur) {
        Stage dialog = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Modifier l'Utilisateur");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 16));

        TextField idField = new TextField(String.valueOf(utilisateur.getIdUtilisateur()));
        TextField nomField = new TextField(utilisateur.getNom());
        TextField prenomField = new TextField(utilisateur.getPrenom());
        TextField emailField = new TextField(utilisateur.getEmail());
        PasswordField passwordField = new PasswordField();
        passwordField.setText(utilisateur.getMotDePasse());

        Button saveButton = new Button("Sauvegarder");
        Button cancelButton = new Button("Annuler");

        styleButton(saveButton);
        styleButton(cancelButton);

        saveButton.setOnAction(e -> {
            utilisateur.setIdUtilisateur(Integer.parseInt(idField.getText()));
            utilisateur.setNom(nomField.getText());
            utilisateur.setPrenom(prenomField.getText());
            utilisateur.setEmail(emailField.getText());
            utilisateur.setMotDePasse(passwordField.getText());
            dialog.close();
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        layout.getChildren().addAll(titleLabel, idField, nomField, prenomField,
                emailField, passwordField, buttonBox);

        Scene scene = new Scene(layout, 400, 350);
        dialog.setTitle("Modifier l'Utilisateur");
        dialog.setScene(scene);
        dialog.show();
    }

    private void showAddSeanceDialog() {
        Stage dialog = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Ajouter une Séance");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 16));

        TextField idField = new TextField();
        idField.setPromptText("ID de la séance");

        ComboBox<Cours> coursCombo = new ComboBox<>();
        coursCombo.setItems(listeCours);
        coursCombo.setPromptText("Sélectionner un cours");

        DatePicker dateDebutPicker = new DatePicker();
        dateDebutPicker.setPromptText("Date de début");

        TextField heureDebutField = new TextField();
        heureDebutField.setPromptText("Heure de début (HH:MM)");

        DatePicker dateFinPicker = new DatePicker();
        dateFinPicker.setPromptText("Date de fin");

        TextField heureFinField = new TextField();
        heureFinField.setPromptText("Heure de fin (HH:MM)");

        CheckBox confirmeeBox = new CheckBox("Séance confirmée");

        Button addButton = new Button("Ajouter");
        Button cancelButton = new Button("Annuler");

        styleButton(addButton);
        styleButton(cancelButton);

        addButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                Cours coursSelectionne = coursCombo.getValue();

                if (coursSelectionne != null) {
                    // Pour simplifier, on utilise la date actuelle
                    Date dateDebut = new Date();
                    Date dateFin = new Date();

                    Seance nouvelleSeance = new Seance(id, dateDebut, dateFin,
                            confirmeeBox.isSelected(), coursSelectionne);

                    emploiDuTemps.ajouterSeance(nouvelleSeance);
                    listeSeances.add(nouvelleSeance);
                    nouvelleSeance.notifierParticipants();
                    dialog.close();
                }
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer un ID valide.");
            }
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, cancelButton);

        layout.getChildren().addAll(titleLabel, idField, coursCombo, dateDebutPicker,
                heureDebutField, dateFinPicker, heureFinField,
                confirmeeBox, buttonBox);

        Scene scene = new Scene(layout, 450, 500);
        dialog.setTitle("Ajouter une Séance");
        dialog.setScene(scene);
        dialog.show();
    }

    private void showEditSeanceDialog(Seance seance) {
        Stage dialog = new Stage();
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titleLabel = new Label("Modifier la Séance");
        titleLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 16));

        ComboBox<Cours> coursCombo = new ComboBox<>();
        coursCombo.setItems(listeCours);
        coursCombo.setValue(seance.cours);

        Button saveButton = new Button("Sauvegarder");
        Button cancelButton = new Button("Annuler");
        Button annulerSeanceButton = new Button("Annuler la Séance");

        styleButton(saveButton);
        styleButton(cancelButton);
        styleButton(annulerSeanceButton);

        saveButton.setOnAction(e -> {
            seance.setCours(coursCombo.getValue());
            dialog.close();
        });

        annulerSeanceButton.setOnAction(e -> {
            seance.annuler();
            dialog.close();
        });

        cancelButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(saveButton, annulerSeanceButton, cancelButton);

        layout.getChildren().addAll(titleLabel, coursCombo, buttonBox);

        Scene scene = new Scene(layout, 400, 200);
        dialog.setTitle("Modifier la Séance");
        dialog.setScene(scene);
        dialog.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}