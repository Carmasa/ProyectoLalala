// src/main/java/org/example/proyectolalalafx/MainController.java

package org.example.proyectolalalafx;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

    @FXML private Pane rootPane;
    @FXML private ImageView imgTierra, imgMarte, imgJupiter, imgSaturno, imgUrano;

    @FXML
    public void initialize() {
        // Iniciar animaciones con configuraciones únicas
        iniciarAnimaciones();

        // Añadir eventos de clic
        imgTierra.setOnMouseClicked(e -> loadGame("Tierra"));
        imgMarte.setOnMouseClicked(e -> loadGame("Marte"));
        imgJupiter.setOnMouseClicked(e -> loadGame("Jupiter"));
        imgSaturno.setOnMouseClicked(e -> loadGame("Saturno")); // Solo Saturno carga el juego
        imgUrano.setOnMouseClicked(e -> loadGame("Urano"));
    }

    private void iniciarAnimaciones() {
        // ... (animaciones como antes)
    }

    private void crearAnimacionPersonalizada(ImageView imageView, double durationSeconds, double deltaY, double rotationSpeed) {
        // ... (animación como antes)
    }

    private void loadGame(String planeta) {
        try {
            Parent gameView;
            if ("Saturno".equals(planeta)) {
                // Cargar el juego específico de Saturno
                gameView = FXMLLoader.load(getClass().getResource("game-saturno.fxml"));
            } else {
                // Para otros planetas, cargar un panel genérico o mostrar un mensaje
                gameView = FXMLLoader.load(getClass().getResource("game-view.fxml"));
            }
            rootPane.getChildren().setAll(gameView);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}