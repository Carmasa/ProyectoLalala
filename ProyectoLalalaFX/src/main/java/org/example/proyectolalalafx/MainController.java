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
        imgSaturno.setOnMouseClicked(e -> loadGame("Saturno"));
        imgUrano.setOnMouseClicked(e -> loadGame("Urano"));
    }

    private void iniciarAnimaciones() {
        // Tierra: Flotación suave + ligera rotación
        crearAnimacionPersonalizada(imgTierra, 3.0, -8, 2.0);

        // Marte: Flotación más rápida + rotación más pronunciada
        crearAnimacionPersonalizada(imgMarte, 2.5, -5, 1.0);

        // Jupiter: Flotación lenta + sin rotación
        crearAnimacionPersonalizada(imgJupiter, 4.0, -10, 1.0);

        // Saturno: Flotación media + rotación muy lenta
        crearAnimacionPersonalizada(imgSaturno, 3.5, -7, 1.0);

        // Urano: Flotación irregular + rotación invertida
        crearAnimacionPersonalizada(imgUrano, 3.2, -9, -3.0);
    }

    private void crearAnimacionPersonalizada(ImageView imageView, double durationSeconds, double deltaY, double rotationSpeed) {
        // Animación de traslación vertical (flotación)
        TranslateTransition tt = new TranslateTransition(Duration.seconds(durationSeconds), imageView);
        tt.setByY(deltaY); // Mover hacia arriba
        tt.setCycleCount(TranslateTransition.INDEFINITE);
        tt.setAutoReverse(true);
        tt.play();

        // Si se especifica velocidad de rotación, añadir animación de rotación
        if (rotationSpeed != 0) {
            RotateTransition rt = new RotateTransition(Duration.seconds(5.0), imageView);
            rt.setByAngle(rotationSpeed * 360); // Rotar 360 grados en 5 segundos
            rt.setCycleCount(RotateTransition.INDEFINITE);
            rt.setAutoReverse(false); // No volver atrás, rotar continuamente
            rt.play();
        }
    }

    private void loadGame(String planeta) {
        try {
            Parent gameView = FXMLLoader.load(getClass().getResource("game-view.fxml"));
            rootPane.getChildren().setAll(gameView);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}