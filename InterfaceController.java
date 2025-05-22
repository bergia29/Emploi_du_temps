package fr.isep.algo.gestionemploisdutemps;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

public class InterfaceController {

    @FXML
    private ImageView myImageView; // L'ImageView qui agit comme un bouton

    @FXML
    private AnchorPane menuPane; // Le menu à afficher

    // Méthode qui est appelée lorsqu'on clique sur l'ImageView
    @FXML
    public void handleImageClick(MouseEvent event) {
        // Vérifie si le menu est actuellement visible ou non et bascule l'état
        boolean isVisible = menuPane.isVisible();
        menuPane.setVisible(!isVisible);  // Affiche ou cache le menu
        System.out.println("Image cliquée ! Menu visible : " + !isVisible);
    }

}
