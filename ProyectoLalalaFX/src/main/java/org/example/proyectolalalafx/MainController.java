// src/main/java/org/example/proyectolalalafx/MainController.java

package org.example.proyectolalalafx;

import java.io.IOException;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {

    @FXML private Pane rootPane;
    @FXML private ImageView imgTierra, imgMarte, imgJupiter, imgSaturno, imgUrano;

    @FXML
    public void initialize() {
        // Detener música al volver al menú principal
        MusicManager.stopMusic();
        
        // Iniciar animaciones con configuraciones únicas
        iniciarAnimaciones();

        // Añadir eventos de clic
        imgTierra.setOnMouseClicked(e -> loadGame("Tierra")); // Este se lo pidio el grupo de Elena
        imgMarte.setOnMouseClicked(e -> loadGame("Marte"));
        imgJupiter.setOnMouseClicked(e -> loadGame("Jupiter")); //Este se lo pidio el grupo de David
        imgSaturno.setOnMouseClicked(e -> loadGame("Saturno"));
        imgUrano.setOnMouseClicked(e -> loadGame("Urano"));
    }

    private void iniciarAnimaciones() {
        crearAnimacionPersonalizada(imgTierra, 3.0, -8, 2.0);
        crearAnimacionPersonalizada(imgMarte, 2.5, -5, 1.0);
        crearAnimacionPersonalizada(imgJupiter, 4.0, -10, 2.0);
        crearAnimacionPersonalizada(imgSaturno, 3.5, -7, 1.0);
        crearAnimacionPersonalizada(imgUrano, 3.2, -9, -1.5);
    }

    private void crearAnimacionPersonalizada(ImageView imageView, double durationSeconds, double deltaY, double rotationSpeed) {
        if (imageView == null) return; // protección por si algún ImageView no se inyectó

        // Animación de flotación (arriba y abajo)
        TranslateTransition tt = new TranslateTransition(Duration.seconds(durationSeconds), imageView);
        tt.setByY(deltaY);
        tt.setCycleCount(TranslateTransition.INDEFINITE);
        tt.setAutoReverse(true);
        tt.play();

        // Animación de rotación (si rotationSpeed != 0)
        if (rotationSpeed != 0) {
            RotateTransition rt = new RotateTransition(Duration.seconds(5.0), imageView);
            rt.setByAngle(rotationSpeed * 360); // grados por ciclo
            rt.setCycleCount(RotateTransition.INDEFINITE);
            rt.setAutoReverse(false);
            rt.play();
        }
    }

    private void loadGame(String planeta) {
        try {
            String archivoFXML = switch (planeta) {
                case "Tierra"  -> "tierra-view.fxml";
                case "Marte"   -> "marte-view.fxml";
                case "Jupiter" -> "jupiter-iew.fxml";
                case "Saturno" -> "/app/menuView.fxml";
                case "Urano"   -> "urano-view.fxml";
                default        -> "game-view.fxml";
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFXML));
            Scene newScene = new Scene(loader.load(), 1920, 1080);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(newScene);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}