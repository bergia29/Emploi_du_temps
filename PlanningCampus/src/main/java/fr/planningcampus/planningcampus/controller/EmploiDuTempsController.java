package fr.planningcampus.planningcampus.controller;

import fr.planningcampus.planningcampus.dao.CoursDAO;
import fr.planningcampus.planningcampus.model.Cours;
import fr.planningcampus.planningcampus.model.Enseignant;
import fr.planningcampus.planningcampus.model.Utilisateur;
import fr.planningcampus.planningcampus.view.EmploiDuTempsView;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la vue d'emploi du temps
 */
public class EmploiDuTempsController implements Initializable {

    @FXML
    private Button prevBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private Button todayBtn;

    @FXML
    private Label dateLabel;

    @FXML
    private ComboBox<String> modeComboBox;

    @FXML
    private TabPane tabPane;

    @FXML
    private ScrollPane calendarPane;

    @FXML
    private ScrollPane listePane;

    @FXML
    private VBox filtresBox;

    @FXML
    private VBox filtresMatieresBox;

    @FXML
    private VBox filtresEnseignantsBox;

    @FXML
    private Button exportButton;

    private EmploiDuTempsView emploiDuTempsView;
    private CoursDAO coursDAO = new CoursDAO();
    private List<Cours> cours;
    private Utilisateur utilisateur;

    /**
     * Initialise le contrôleur
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialisation des combobox
        modeComboBox.setItems(FXCollections.observableArrayList("Jour", "Semaine", "Mois"));
        modeComboBox.setValue("Semaine");

        // Par défaut, on affiche tous les cours
        cours = coursDAO.getAllCours();

        modeComboBox.setOnAction(e -> {
            if (emploiDuTempsView != null) {
                String mode = modeComboBox.getValue().toLowerCase();
                emploiDuTempsView.setModeAffichage(mode);
                emploiDuTempsView.afficherEmploiDuTemps();
            }
        });
    }

    /**
     * Définit l'utilisateur connecté
     *
     * @param utilisateur Utilisateur connecté
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        // Charger les cours en fonction du type d'utilisateur
        if (utilisateur instanceof Enseignant) {
            cours = coursDAO.getCoursByEnseignant(utilisateur.getId());
        } else if (utilisateur instanceof fr.planningcampus.planningcampus.model.Etudiant) {
            cours = coursDAO.getCoursByEtudiant(utilisateur.getId());
        } else {
            // Administrateur - tous les cours
            cours = coursDAO.getAllCours();
        }

        // Initialiser la vue d'emploi du temps
        initialiserVue();
    }

    /**
     * Initialise la vue d'emploi du temps
     */
    private void initialiserVue() {
        emploiDuTempsView = new EmploiDuTempsView(cours);

        // Définir le mode d'affichage initial
        emploiDuTempsView.setModeAffichage(modeComboBox.getValue().toLowerCase());

        // Ajouter la vue à la scrollPane
        calendarPane.setContent(emploiDuTempsView.getCalendarView());
        listePane.setContent(emploiDuTempsView.getListView());

        // Initialiser les filtres
        initFiltres();

        // Afficher la date actuelle
        updateDateLabel();
    }

    /**
     * Initialise les filtres pour les matières et enseignants
     */
    private void initFiltres() {
        // Récupérer toutes les matières
        Set<String> matieres = cours.stream()
                .map(Cours::getMatiere)
                .collect(Collectors.toSet());

        // Récupérer tous les enseignants (sans doublons)
        Map<Integer, Enseignant> enseignantsMap = new HashMap<>();
        cours.stream()
                .map(Cours::getEnseignant)
                .filter(Objects::nonNull)
                .forEach(e -> enseignantsMap.put(e.getId(), e));

        // Vider les boîtes de filtres
        filtresMatieresBox.getChildren().clear();
        filtresEnseignantsBox.getChildren().clear();

        // Ajouter le titre des filtres
        Label matiereLabel = new Label("Matières:");
        matiereLabel.setStyle("-fx-font-weight: bold;");
        filtresMatieresBox.getChildren().add(matiereLabel);

        Label enseignantLabel = new Label("Enseignants:");
        enseignantLabel.setStyle("-fx-font-weight: bold;");
        filtresEnseignantsBox.getChildren().add(enseignantLabel);

        // Ajouter les cases à cocher pour les matières
        for (String matiere : matieres) {
            CheckBox cb = new CheckBox(matiere);
            cb.setSelected(true);
            cb.setOnAction(e -> {
                emploiDuTempsView.setFiltreMatiere(matiere, cb.isSelected());
                emploiDuTempsView.afficherEmploiDuTemps();
            });
            filtresMatieresBox.getChildren().add(cb);
        }

        // Ajouter les cases à cocher pour les enseignants
        for (Enseignant enseignant : enseignantsMap.values()) {
            CheckBox cb = new CheckBox(enseignant.getPrenom() + " " + enseignant.getNom());
            cb.setSelected(true);
            cb.setOnAction(e -> {
                emploiDuTempsView.setFiltreEnseignant(enseignant.getId(), cb.isSelected());
                emploiDuTempsView.afficherEmploiDuTemps();
            });
            filtresEnseignantsBox.getChildren().add(cb);
        }
    }

    /**
     * Met à jour l'affichage de la date
     */
    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH);
        dateLabel.setText(sdf.format(emploiDuTempsView.getDateActuelle()));
    }

    /**
     * Gère l'événement du bouton précédent
     */
    @FXML
    private void handlePrecedent(ActionEvent event) {
        emploiDuTempsView.naviguerPrecedent();
        updateDateLabel();
        emploiDuTempsView.afficherEmploiDuTemps();
    }

    /**
     * Gère l'événement du bouton suivant
     */
    @FXML
    private void handleSuivant(ActionEvent event) {
        emploiDuTempsView.naviguerSuivant();
        updateDateLabel();
        emploiDuTempsView.afficherEmploiDuTemps();
    }

    /**
     * Gère l'événement du bouton aujourd'hui
     */
    @FXML
    private void handleAujourdhui(ActionEvent event) {
        emploiDuTempsView.setDateActuelle(new Date());
        updateDateLabel();
        emploiDuTempsView.afficherEmploiDuTemps();
    }

    /**
     * Gère l'événement du bouton retour
     */
    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            // Déterminer la vue à charger en fonction du type d'utilisateur
            String viewName;

            if (utilisateur instanceof Enseignant) {
                viewName = "enseignant-dashboard.fxml";
            } else if (utilisateur instanceof fr.planningcampus.planningcampus.model.Etudiant) {
                viewName = "etudiant-dashboard.fxml";
            } else {
                viewName = "admin.fxml";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/planningcampus/planningcampus/view/admin-dashboard.fxml"));
            Parent root = loader.load();

            // Passage de l'utilisateur connecté au contrôleur
            if (viewName.equals("admin.fxml")) {
                AdministrateurController controller = loader.getController();
                controller.setUtilisateur(utilisateur);
            } else if (viewName.equals("enseignant-dashboard.fxml")) {
                EnseignantController controller = loader.getController();
                controller.setUtilisateur(utilisateur);
            } else {
                EtudiantController controller = loader.getController();
                controller.setUtilisateur(utilisateur);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page: " + e.getMessage());
        }
    }

    /**
     * Gère l'exportation de l'emploi du temps
     */
    @FXML
    private void handleExport(ActionEvent event) {
        // Créer un sélecteur de fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter l'emploi du temps");

        // Filtres pour les types de fichiers
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("Fichiers CSV (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().addAll(pdfFilter, csvFilter);

        // Afficher le dialogue
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

        if (file != null) {
            String extension = getExtension(file.getName());

            if ("pdf".equals(extension)) {
                exporterPDF(file);
            } else if ("csv".equals(extension)) {
                exporterCSV(file);
            } else {
                showAlert("Erreur", "Format de fichier non pris en charge.");
            }
        }
    }

    /**
     * Exporte l'emploi du temps au format PDF
     */
    private void exporterPDF(File file) {
        try {
            // Récupérer les données à exporter
            List<Cours> coursFiltres = emploiDuTempsView.filtrerCours();

            // Trier les cours par date, jour et heure
            List<Cours> coursTries = coursFiltres.stream()
                    .filter(c -> c.getHoraire() != null)
                    .sorted((c1, c2) -> {
                        // D'abord par date si disponible
                        if (c1.getHoraire().getDate() != null && c2.getHoraire().getDate() != null) {
                            int dateComp = c1.getHoraire().getDate().compareTo(c2.getHoraire().getDate());
                            if (dateComp != 0) {
                                return dateComp;
                            }
                        }

                        // Puis par jour de la semaine
                        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
                        int jour1 = Arrays.asList(jours).indexOf(c1.getHoraire().getJour());
                        int jour2 = Arrays.asList(jours).indexOf(c2.getHoraire().getJour());
                        if (jour1 != jour2) {
                            return jour1 - jour2;
                        }

                        // Enfin par heure de début
                        return c1.getHoraire().getHeureDebut().compareTo(c2.getHoraire().getHeureDebut());
                    })
                    .collect(Collectors.toList());

            // Créer le document PDF
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Ajouter un titre
            Paragraph titre = new Paragraph("Emploi du temps",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            titre.setAlignment(Element.ALIGN_CENTER);
            titre.setSpacingAfter(20);
            document.add(titre);

            // Ajouter une description
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Paragraph description = new Paragraph("Généré le " + dateFormat.format(new Date()),
                    FontFactory.getFont(FontFactory.HELVETICA, 12));
            description.setAlignment(Element.ALIGN_CENTER);
            description.setSpacingAfter(20);
            document.add(description);

            // Créer un tableau pour afficher l'emploi du temps
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            // En-têtes du tableau
            String[] headers = {"Matière", "Jour", "Date", "Heure de début", "Heure de fin", "Enseignant", "Salle"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Format pour les heures et les dates
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            // Ajouter les données
            for (Cours c : coursTries) {
                // Matière
                table.addCell(c.getMatiere());

                if (c.getHoraire() != null) {
                    // Jour
                    table.addCell(c.getHoraire().getJour());

                    // Date
                    if (c.getHoraire().getDate() != null) {
                        table.addCell(dateFormat.format(c.getHoraire().getDate()));
                    } else {
                        table.addCell("N/A");
                    }

                    // Heure de début
                    table.addCell(timeFormat.format(c.getHoraire().getHeureDebut()));

                    // Heure de fin
                    table.addCell(timeFormat.format(c.getHoraire().getHeureFin()));
                } else {
                    table.addCell("N/A");
                    table.addCell("N/A");
                    table.addCell("N/A");
                    table.addCell("N/A");
                }

                // Enseignant
                if (c.getEnseignant() != null) {
                    table.addCell(c.getEnseignant().getPrenom() + " " + c.getEnseignant().getNom());
                } else {
                    table.addCell("N/A");
                }

                // Salle
                if (c.getSalle() != null) {
                    table.addCell(c.getSalle().getNom());
                } else {
                    table.addCell("N/A");
                }
            }

            document.add(table);

            // Fermer le document
            document.close();

            showAlert("Succès", "L'emploi du temps a été exporté avec succès au format PDF.");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de l'exportation en PDF: " + e.getMessage());
        }
    }

    /**
     * Exporte l'emploi du temps au format CSV
     */
    private void exporterCSV(File file) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            // Écrire l'en-tête
            writer.write("Matière;Jour;Date;Heure de début;Heure de fin;Enseignant;Salle\n");

            // Filtrer les cours selon les filtres actifs
            List<Cours> coursFiltres = emploiDuTempsView.filtrerCours();

            // Trier par jour et heure
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            // Écrire les données
            for (Cours c : coursFiltres) {
                StringBuilder line = new StringBuilder();

                line.append(c.getMatiere()).append(";");

                if (c.getHoraire() != null) {
                    line.append(c.getHoraire().getJour()).append(";");

                    if (c.getHoraire().getDate() != null) {
                        line.append(dateFormat.format(c.getHoraire().getDate())).append(";");
                    } else {
                        line.append("N/A;");
                    }

                    line.append(timeFormat.format(c.getHoraire().getHeureDebut())).append(";");
                    line.append(timeFormat.format(c.getHoraire().getHeureFin())).append(";");
                } else {
                    line.append("N/A;N/A;N/A;N/A;");
                }

                if (c.getEnseignant() != null) {
                    line.append(c.getEnseignant().getPrenom()).append(" ").append(c.getEnseignant().getNom()).append(";");
                } else {
                    line.append("N/A;");
                }

                if (c.getSalle() != null) {
                    line.append(c.getSalle().getNom());
                } else {
                    line.append("N/A");
                }

                line.append("\n");
                writer.write(line.toString());
            }

            showAlert("Succès", "L'emploi du temps a été exporté avec succès.");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de l'exportation: " + e.getMessage());
        }
    }

    /**
     * Récupère l'extension d'un fichier
     */
    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * Affiche une boîte de dialogue d'alerte
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}