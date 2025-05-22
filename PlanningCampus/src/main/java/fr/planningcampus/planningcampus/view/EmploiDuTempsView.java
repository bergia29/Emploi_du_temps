package fr.planningcampus.planningcampus.view;

import fr.planningcampus.planningcampus.model.Cours;
import fr.planningcampus.planningcampus.model.Seance;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe gérant la visualisation de l'emploi du temps
 */
public class EmploiDuTempsView extends BorderPane {

    private static final String[] JOURS = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
    private static final String[] MOIS = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

    private List<Cours> listeCours;
    private Date dateActuelle;
    private String modeAffichage; // "jour", "semaine", "mois"

    private VBox calendarView;
    private VBox listView;

    private Map<String, Boolean> filtresMatiere;
    private Map<Integer, Boolean> filtresEnseignant;

    /**
     * Constructeur
     *
     * @param cours Liste des cours à afficher
     */
    public EmploiDuTempsView(List<Cours> cours) {
        this.listeCours = cours;
        this.dateActuelle = new Date();
        this.modeAffichage = "semaine";
        this.filtresMatiere = new HashMap<>();
        this.filtresEnseignant = new HashMap<>();

        initialiserFiltres();
        initialiserUI();
    }

    /**
     * Initialise les filtres
     */
    private void initialiserFiltres() {
        // Initialiser les filtres matière
        for (Cours cours : listeCours) {
            filtresMatiere.put(cours.getMatiere(), true);
            if (cours.getEnseignant() != null) {
                filtresEnseignant.put(cours.getEnseignant().getId(), true);
            }
        }
    }

    /**
     * Initialise l'interface utilisateur
     */
    private void initialiserUI() {
        // Création des vues pour le calendrier et la liste
        calendarView = new VBox(10);
        listView = new VBox(10);

        // Affichage initial
        afficherEmploiDuTemps();
    }

    /**
     * Retourne la vue calendrier
     *
     * @return Vue du calendrier
     */
    public Node getCalendarView() {
        return calendarView;
    }

    /**
     * Retourne la vue liste
     *
     * @return Vue de la liste
     */
    public Node getListView() {
        return listView;
    }

    /**
     * Change le mode d'affichage
     *
     * @param mode Mode d'affichage ("jour", "semaine", "mois")
     */
    public void setModeAffichage(String mode) {
        this.modeAffichage = mode.toLowerCase();
    }

    /**
     * Change la date actuelle
     *
     * @param date Nouvelle date
     */
    public void setDateActuelle(Date date) {
        this.dateActuelle = date;
    }

    /**
     * Retourne la date actuelle
     *
     * @return Date actuelle
     */
    public Date getDateActuelle() {
        return dateActuelle;
    }

    /**
     * Définit un filtre pour une matière
     *
     * @param matiere Nom de la matière
     * @param visible true si visible, false sinon
     */
    public void setFiltreMatiere(String matiere, boolean visible) {
        filtresMatiere.put(matiere, visible);
    }

    /**
     * Définit un filtre pour un enseignant
     *
     * @param idEnseignant ID de l'enseignant
     * @param visible true si visible, false sinon
     */
    public void setFiltreEnseignant(int idEnseignant, boolean visible) {
        filtresEnseignant.put(idEnseignant, visible);
    }

    /**
     * Navigue vers la période précédente en fonction du mode d'affichage
     */
    public void naviguerPrecedent() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateActuelle);

        switch (modeAffichage) {
            case "jour":
                cal.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case "semaine":
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case "mois":
                cal.add(Calendar.MONTH, -1);
                break;
        }

        dateActuelle = cal.getTime();
    }

    /**
     * Navigue vers la période suivante en fonction du mode d'affichage
     */
    public void naviguerSuivant() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateActuelle);

        switch (modeAffichage) {
            case "jour":
                cal.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case "semaine":
                cal.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case "mois":
                cal.add(Calendar.MONTH, 1);
                break;
        }

        dateActuelle = cal.getTime();
    }

    /**
     * Filtre les cours selon les critères sélectionnés
     *
     * @return Liste des cours filtrés
     */
    public List<Cours> filtrerCours() {
        return listeCours.stream()
                .filter(cours -> filtresMatiere.getOrDefault(cours.getMatiere(), true))
                .filter(cours -> cours.getEnseignant() == null ||
                        filtresEnseignant.getOrDefault(cours.getEnseignant().getId(), true))
                .collect(Collectors.toList());
    }

    /**
     * Affiche l'emploi du temps en fonction du mode et des filtres
     */
    public void afficherEmploiDuTemps() {
        List<Cours> coursFiltres = filtrerCours();

        // Affichage calendrier
        calendarView.getChildren().clear();

        switch (modeAffichage) {
            case "jour":
                calendarView.getChildren().add(creerVueJour(coursFiltres));
                break;
            case "semaine":
                calendarView.getChildren().add(creerVueSemaine(coursFiltres));
                break;
            case "mois":
                calendarView.getChildren().add(creerVueMois(coursFiltres));
                break;
        }

        // Affichage liste
        listView.getChildren().clear();
        listView.getChildren().add(creerVueListe(coursFiltres));
    }

    /**
     * Crée la vue par jour
     *
     * @param cours Liste des cours à afficher
     * @return Node contenant la vue
     */
    private Node creerVueJour(List<Cours> cours) {
        VBox container = new VBox(10);

        // Récupérer le jour actuel
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateActuelle);
        int jourSemaine = cal.get(Calendar.DAY_OF_WEEK) - 2; // -2 car Calendar.MONDAY = 2
        if (jourSemaine < 0) jourSemaine = 6; // Si c'est dimanche (0-2=-2), on définit à 6 (samedi)
        if (jourSemaine > 5) jourSemaine = 5; // Limiter à samedi (5)

        String jour = JOURS[jourSemaine];

        // Titre du jour
        Label titreJour = new Label(jour + " " + new SimpleDateFormat("dd MMMM yyyy").format(dateActuelle));
        titreJour.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        container.getChildren().add(titreJour);

        // Créer la grille horaire
        GridPane grille = new GridPane();
        grille.setStyle("-fx-grid-lines-visible: true;");

        // Heures (de 8h à 18h)
        for (int heure = 8; heure <= 18; heure++) {
            Label heureLabel = new Label(heure + "h");
            heureLabel.setPadding(new Insets(5));
            grille.add(heureLabel, 0, heure - 8);

            // Cellule vide pour les cours
            Pane cellule = new Pane();
            cellule.setPrefHeight(60);
            cellule.setPrefWidth(800);
            grille.add(cellule, 1, heure - 8);
        }

        // Filtrer les cours du jour affiché
        List<Cours> coursDuJour = new ArrayList<>();
        for (Cours c : cours) {
            if (c.getHoraire() != null && c.getHoraire().getJour().equals(jour)) {
                coursDuJour.add(c);
            }
        }

        // Afficher les cours
        for (Cours c : coursDuJour) {
            Seance seance = c.getHoraire();

            if (seance != null) {
                Calendar calStart = Calendar.getInstance();
                calStart.setTime(seance.getHeureDebut());
                int heureDebut = calStart.get(Calendar.HOUR_OF_DAY);
                int minuteDebut = calStart.get(Calendar.MINUTE);

                Calendar calEnd = Calendar.getInstance();
                calEnd.setTime(seance.getHeureFin());
                int heureFin = calEnd.get(Calendar.HOUR_OF_DAY);
                int minuteFin = calEnd.get(Calendar.MINUTE);

                // Calculer la position et taille
                double startPos = (heureDebut - 8) + (minuteDebut / 60.0);
                double height = (heureFin - heureDebut) + ((minuteFin - minuteDebut) / 60.0);

                // Créer la carte du cours
                StackPane coursPane = creerCarteCours(c);
                coursPane.setPrefHeight(height * 60);
                coursPane.setLayoutY(startPos * 60);

                // Ajouter à la cellule correspondante
                Pane cellule = (Pane) grille.getChildren().get((heureDebut - 8) * 2 + 1);
                cellule.getChildren().add(coursPane);
            }
        }

        container.getChildren().add(grille);
        return container;
    }

    /**
     * Crée la vue par semaine
     *
     * @param cours Liste des cours à afficher
     * @return Node contenant la vue
     */
    private Node creerVueSemaine(List<Cours> cours) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        // Titre de la semaine
        Label titreLabel = new Label("Semaine du " + new SimpleDateFormat("dd MMMM yyyy").format(dateActuelle));
        titreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        container.getChildren().add(titreLabel);

        // Filtrer les cours de la semaine
        Calendar calActuelle = Calendar.getInstance();
        calActuelle.setTime(dateActuelle);
        int semaineActuelle = calActuelle.get(Calendar.WEEK_OF_YEAR);

        List<Cours> coursSemaine = cours.stream()
                .filter(c -> {
                    if (c.getHoraire() == null) return false;

                    // Utiliser la date du cours si disponible
                    if (c.getHoraire().getDate() != null) {
                        Calendar calCours = Calendar.getInstance();
                        calCours.setTime(c.getHoraire().getDate());
                        return calCours.get(Calendar.WEEK_OF_YEAR) == semaineActuelle;
                    }

                    // Sinon, utiliser le numéro de semaine du cours
                    return c.getHoraire().getSemaine() == semaineActuelle;
                })
                .collect(Collectors.toList());

        // Créer la grille horaire
        GridPane grille = new GridPane();
        grille.setStyle("-fx-grid-lines-visible: true;");

        // En-têtes des colonnes (jours)
        for (int i = 0; i < JOURS.length; i++) {
            Label jourLabel = new Label(JOURS[i]);
            jourLabel.setPadding(new Insets(5));
            jourLabel.setStyle("-fx-font-weight: bold;");
            grille.add(jourLabel, i + 1, 0);
        }

        // En-têtes des lignes (heures)
        for (int heure = 8; heure <= 18; heure++) {
            Label heureLabel = new Label(heure + "h");
            heureLabel.setPadding(new Insets(5));
            grille.add(heureLabel, 0, heure - 7);

            // Cellules vides pour chaque jour
            for (int jour = 0; jour < JOURS.length; jour++) {
                Pane cellule = new Pane();
                cellule.setPrefHeight(80); // Augmenter la hauteur des cellules
                cellule.setPrefWidth(160); // Augmenter la largeur des cellules
                cellule.setStyle("-fx-background-color: rgba(255,255,255,0.5);");
                grille.add(cellule, jour + 1, heure - 7);
            }
        }

        // Définir les contraintes de colonnes
        ColumnConstraints colLabel = new ColumnConstraints();
        colLabel.setPrefWidth(40);
        colLabel.setMinWidth(40);
        grille.getColumnConstraints().add(colLabel);

        for (int i = 0; i < JOURS.length; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(160); // Augmenter la largeur des colonnes
            col.setMinWidth(160);
            grille.getColumnConstraints().add(col);
        }

        // Ajouter les cours à la grille
        for (Cours c : coursSemaine) {
            Seance seance = c.getHoraire();

            if (seance != null) {
                // Déterminer l'index du jour (0=lundi, 1=mardi, etc.)
                int jourIndex = -1;
                for (int i = 0; i < JOURS.length; i++) {
                    if (JOURS[i].equals(seance.getJour())) {
                        jourIndex = i;
                        break;
                    }
                }

                if (jourIndex >= 0) {
                    Calendar calStart = Calendar.getInstance();
                    calStart.setTime(seance.getHeureDebut());
                    int heureDebut = calStart.get(Calendar.HOUR_OF_DAY);
                    int minuteDebut = calStart.get(Calendar.MINUTE);

                    Calendar calEnd = Calendar.getInstance();
                    calEnd.setTime(seance.getHeureFin());
                    int heureFin = calEnd.get(Calendar.HOUR_OF_DAY);
                    int minuteFin = calEnd.get(Calendar.MINUTE);

                    // Calculer la position et taille
                    double startPos = (heureDebut - 8) + (minuteDebut / 60.0);
                    double height = (heureFin - heureDebut) + ((minuteFin - minuteDebut) / 60.0);

                    // Créer la carte du cours
                    StackPane coursPane = creerCarteCours(c);
                    coursPane.setPrefHeight(height * 80); // Adapter à la nouvelle hauteur de cellule
                    coursPane.setLayoutY(startPos * 80); // Adapter à la nouvelle hauteur de cellule

                    // Ajouter à la cellule correspondante
                    Pane cellule = (Pane) getNodeFromGridPane(grille, jourIndex + 1, heureDebut - 7);
                    if (cellule != null) {
                        cellule.getChildren().add(coursPane);
                    }
                }
            }
        }

        // Ajouter une ScrollPane pour permettre de défiler si nécessaire
        ScrollPane scrollPane = new ScrollPane(grille);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        container.getChildren().add(scrollPane);

        return container;
    }

    /**
     * Obtient un nœud à partir d'une GridPane par ses coordonnées
     */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Crée la vue par mois
     *
     * @param cours Liste des cours à afficher
     * @return Node contenant la vue
     */
    private Node creerVueMois(List<Cours> cours) {
        VBox container = new VBox(10);

        // Récupérer le mois actuel
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateActuelle);
        int mois = cal.get(Calendar.MONTH);
        int annee = cal.get(Calendar.YEAR);

        // Titre du mois
        Label titreMois = new Label(MOIS[mois] + " " + annee);
        titreMois.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        container.getChildren().add(titreMois);

        // Grille du mois
        GridPane grille = new GridPane();
        grille.setStyle("-fx-grid-lines-visible: true;");

        // En-tête des jours
        for (int i = 0; i < JOURS.length; i++) {
            Label jourLabel = new Label(JOURS[i]);
            jourLabel.setPadding(new Insets(5));
            jourLabel.setPrefWidth(150);
            jourLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
            grille.add(jourLabel, i, 0);
        }

        // Récupérer le premier jour du mois
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int premierJour = cal.get(Calendar.DAY_OF_WEEK) - 2; // -2 car Calendar.MONDAY = 2
        if (premierJour < 0) premierJour = 6; // Si c'est dimanche, on définit à 6 (samedi)

        // Nombre de jours dans le mois
        int nbJours = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Filtrer les cours pour le mois actuel
        List<Cours> coursMois = cours.stream()
                .filter(c -> c.getHoraire() != null && c.getHoraire().getDate() != null)
                .filter(c -> {
                    Calendar calCours = Calendar.getInstance();
                    calCours.setTime(c.getHoraire().getDate());
                    return calCours.get(Calendar.MONTH) == mois && calCours.get(Calendar.YEAR) == annee;
                })
                .collect(Collectors.toList());

        // Remplir le calendrier
        int jour = 1;
        for (int semaine = 0; semaine < 6; semaine++) {
            for (int j = 0; j < 7; j++) {
                if ((semaine == 0 && j < premierJour) || (jour > nbJours)) {
                    // Cellule vide
                    Pane celluleVide = new Pane();
                    celluleVide.setPrefHeight(100);
                    celluleVide.setPrefWidth(150);
                    grille.add(celluleVide, j, semaine + 1);
                } else {
                    // Cellule avec jour
                    VBox cellule = new VBox(2);
                    cellule.setPadding(new Insets(5));
                    cellule.setPrefHeight(100);
                    cellule.setPrefWidth(150);
                    cellule.setStyle("-fx-background-color: white;");

                    // Numéro du jour
                    Label jourLabel = new Label(String.valueOf(jour));
                    jourLabel.setStyle("-fx-font-weight: bold;");
                    cellule.getChildren().add(jourLabel);

                    // Date complète pour ce jour
                    cal.set(Calendar.DAY_OF_MONTH, jour);
                    final int jourFinal = jour;

                    // Ajouter les cours du jour
                    List<Cours> coursJour = coursMois.stream()
                            .filter(c -> {
                                Calendar calCours = Calendar.getInstance();
                                calCours.setTime(c.getHoraire().getDate());
                                return calCours.get(Calendar.DAY_OF_MONTH) == jourFinal;
                            })
                            .collect(Collectors.toList());

                    // Ajouter au maximum 3 cours par cellule
                    int count = 0;
                    for (Cours c : coursJour) {
                        if (count < 3) {
                            HBox coursBox = new HBox(5);

                            Rectangle rect = new Rectangle(10, 10);
                            rect.setFill(Color.LIGHTBLUE);

                            Text coursText = new Text(c.getMatiere());
                            coursText.setWrappingWidth(125);
                            coursBox.getChildren().addAll(rect, coursText);

                            cellule.getChildren().add(coursBox);
                            count++;
                        } else {
                            // Indiquer qu'il y a plus de cours
                            Text plusCours = new Text("+" + (coursJour.size() - 3) + " autres cours");
                            plusCours.setStyle("-fx-fill: gray;");
                            cellule.getChildren().add(plusCours);
                            break;
                        }
                    }

                    grille.add(cellule, j, semaine + 1);
                    jour++;
                }
            }
        }

        container.getChildren().add(grille);
        return container;
    }

    /**
     * Crée la vue en liste
     *
     * @param cours Liste des cours à afficher
     * @return Node contenant la vue
     */
    private Node creerVueListe(List<Cours> cours) {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        // Titre
        Label titreLabel = new Label("Liste des cours");
        titreLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        container.getChildren().add(titreLabel);

        // Trier les cours par jour et heure
        List<Cours> coursTries = cours.stream()
                .sorted((c1, c2) -> {
                    // D'abord par jour
                    if (c1.getHoraire() == null || c2.getHoraire() == null) {
                        return 0;
                    }

                    int jourComp = c1.getHoraire().getJour().compareTo(c2.getHoraire().getJour());
                    if (jourComp != 0) {
                        return jourComp;
                    }

                    // Puis par heure de début
                    return c1.getHoraire().getHeureDebut().compareTo(c2.getHoraire().getHeureDebut());
                })
                .collect(Collectors.toList());

        // Grouper par jour
        Map<String, List<Cours>> coursParJour = new HashMap<>();
        for (Cours c : coursTries) {
            if (c.getHoraire() != null) {
                String jour = c.getHoraire().getJour();
                if (!coursParJour.containsKey(jour)) {
                    coursParJour.put(jour, new ArrayList<>());
                }
                coursParJour.get(jour).add(c);
            }
        }

        // Afficher par jour
        for (String jour : JOURS) {
            if (coursParJour.containsKey(jour)) {
                Label jourLabel = new Label(jour);
                jourLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                container.getChildren().add(jourLabel);

                for (Cours c : coursParJour.get(jour)) {
                    VBox coursBox = new VBox(5);
                    coursBox.setPadding(new Insets(5, 5, 10, 15));

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Label horaireLabel = new Label(sdf.format(c.getHoraire().getHeureDebut()) +
                            " - " + sdf.format(c.getHoraire().getHeureFin()));

                    Label matiereLabel = new Label(c.getMatiere());
                    matiereLabel.setStyle("-fx-font-weight: bold;");

                    String enseignant = c.getEnseignant() != null ?
                            c.getEnseignant().getPrenom() + " " + c.getEnseignant().getNom() : "Non assigné";
                    Label enseignantLabel = new Label("Enseignant: " + enseignant);

                    String salle = c.getSalle() != null ? c.getSalle().getNom() : "Non assignée";
                    Label salleLabel = new Label("Salle: " + salle);

                    coursBox.getChildren().addAll(horaireLabel, matiereLabel, enseignantLabel, salleLabel);
                    container.getChildren().add(coursBox);
                }
            }
        }

        return container;
    }

    /**
     * Crée une carte visuelle pour un cours
     *
     * @param cours Le cours à représenter
     * @return StackPane contenant la carte
     */
    private StackPane creerCarteCours(Cours cours) {
        StackPane pane = new StackPane();
        pane.setPrefWidth(140);
        pane.setMaxWidth(140);
        pane.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");

        // Fond coloré
        Rectangle rect = new Rectangle();
        rect.setWidth(140);
        rect.setHeight(80); // Augmenter la hauteur pour accommoder l'horaire
        rect.setFill(Color.LIGHTBLUE);
        rect.setOpacity(0.8);
        rect.setArcWidth(10);
        rect.setArcHeight(10);

        // Texte
        VBox textBox = new VBox(2);
        textBox.setPadding(new Insets(3));
        textBox.setMaxWidth(136);
        textBox.setAlignment(Pos.TOP_LEFT);

        // Ajouter l'horaire du cours
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Text horaireText = new Text(sdf.format(cours.getHoraire().getHeureDebut()) +
                " - " + sdf.format(cours.getHoraire().getHeureFin()));
        horaireText.setStyle("-fx-font-size: 10px;");
        horaireText.setWrappingWidth(136);

        Text matiereText = new Text(cours.getMatiere());
        matiereText.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        matiereText.setWrappingWidth(136);

        String enseignant = cours.getEnseignant() != null ?
                cours.getEnseignant().getPrenom().charAt(0) + ". " + cours.getEnseignant().getNom() : "";
        Text enseignantText = new Text(enseignant);
        enseignantText.setStyle("-fx-font-size: 10px;");
        enseignantText.setWrappingWidth(136);

        String salle = cours.getSalle() != null ? cours.getSalle().getNom() : "";
        Text salleText = new Text(salle);
        salleText.setStyle("-fx-font-size: 10px;");
        salleText.setWrappingWidth(136);

        textBox.getChildren().addAll(horaireText, matiereText, enseignantText, salleText);

        pane.getChildren().addAll(rect, textBox);

        // Style au survol
        pane.setOnMouseEntered(e -> {
            rect.setOpacity(1.0);
            rect.setFill(Color.SKYBLUE);
            pane.setStyle("-fx-border-color: #999999; -fx-border-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 1);");
        });

        pane.setOnMouseExited(e -> {
            rect.setOpacity(0.8);
            rect.setFill(Color.LIGHTBLUE);
            pane.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");
        });

        return pane;
    }

    /**
     * Méthode pour mettre à jour la liste des cours
     *
     * @param cours Nouvelle liste de cours
     */
    public void mettreAJourCours(List<Cours> cours) {
        this.listeCours = cours;
        initialiserFiltres();
        afficherEmploiDuTemps();
    }
}