package org.example.proyectolalalafx;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.Locale;
import java.util.ResourceBundle;

public class GameSaturno {

    @FXML private ImageView muñeco;
    @FXML private Circle zonaCabeza, zonaBrazoIzq, zonaBrazoDer, zonaPiernaIzq, zonaPiernaDer;
    @FXML private ComboBox<String> idiomaBox;

    private ResourceBundle bundle;

    @FXML
    public void initialize() {
        // Inicializar idiomas

        // Eventos de clic
        zonaCabeza.setOnMouseClicked(e -> mostrarParte("head"));
        zonaBrazoIzq.setOnMouseClicked(e -> mostrarParte("left_arm"));
        zonaBrazoDer.setOnMouseClicked(e -> mostrarParte("right_arm"));
        zonaPiernaIzq.setOnMouseClicked(e -> mostrarParte("left_leg"));
        zonaPiernaDer.setOnMouseClicked(e -> mostrarParte("right_leg"));
    }


    private void mostrarParte(String key) {
        String texto = bundle.getString(key);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Parte del cuerpo");
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        System.out.println("Volviendo al menú...");
    }
}
