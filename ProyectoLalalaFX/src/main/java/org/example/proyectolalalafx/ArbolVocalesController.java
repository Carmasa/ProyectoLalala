package org.example.proyectolalalafx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ArbolVocalesController {

    @FXML
    private Pane rootPane;

    @FXML
    private ImageView letraA;

    @FXML
    private ImageView letraE;

    @FXML
    private ImageView letraI;

    @FXML
    private ImageView letraO;

    @FXML
    private ImageView letraU;

    @FXML
    private ImageView sombra1;

    @FXML
    private ImageView sombra2;

    @FXML
    private ImageView sombra3;

    @FXML
    private ImageView sombra4;

    @FXML
    private ImageView sombra5;

    private ImageView letraSeleccionada = null;
    private final Map<ImageView, ImageView> letraASombra = new HashMap<>();
    private final Map<ImageView, Boolean> letrasColocadas = new HashMap<>();
    private final DropShadow efectoSeleccion = new DropShadow();

    @FXML
    public void initialize() {
        // Reproducir música del minijuego
        MusicManager.playMusic("audio/audio-minijuegos-arboles/background_music/Cosmic Christmas Lights.mp3");
        
        // Configurar efecto de selección
        efectoSeleccion.setColor(Color.YELLOW);
        efectoSeleccion.setRadius(20);
        efectoSeleccion.setSpread(0.7);

        // Mapear cada letra con su sombra correspondiente
        letraASombra.put(letraA, sombra1);
        letraASombra.put(letraE, sombra2);
        letraASombra.put(letraI, sombra3);
        letraASombra.put(letraO, sombra4);
        letraASombra.put(letraU, sombra5);

        // Inicializar estado de letras
        letrasColocadas.put(letraA, false);
        letrasColocadas.put(letraE, false);
        letrasColocadas.put(letraI, false);
        letrasColocadas.put(letraO, false);
        letrasColocadas.put(letraU, false);

        // Configurar eventos de selección de letras
        configurarSeleccion(letraA);
        configurarSeleccion(letraE);
        configurarSeleccion(letraI);
        configurarSeleccion(letraO);
        configurarSeleccion(letraU);

        // Configurar eventos directos sobre sombras (opcional)
        configurarSombra(sombra1);
        configurarSombra(sombra2);
        configurarSombra(sombra3);
        configurarSombra(sombra4);
        configurarSombra(sombra5);
    }

    private void configurarSeleccion(ImageView letra) {
        letra.setOnMouseClicked(event -> {
            if (!letrasColocadas.get(letra)) {

                if (letraSeleccionada != null)
                    letraSeleccionada.setEffect(null);

                letraSeleccionada = letra;
                letraSeleccionada.setEffect(efectoSeleccion);
            }
        });

        letra.setStyle("-fx-cursor: hand;");
    }

    private void configurarSombra(ImageView sombra) {
        sombra.setOnMouseClicked(event -> manejarClickEnSombra(sombra));
        sombra.setStyle("-fx-cursor: hand;");
        sombra.setOpacity(0.3);
        sombra.setPickOnBounds(true);
    }

    // 🔵 LÓGICA CENTRALIZADA DE CLIC EN SOMBRA (botón invisible o sombra real)
    private void manejarClickEnSombra(ImageView sombra) {

        if (letraSeleccionada != null) {

            ImageView sombraCorrecta = letraASombra.get(letraSeleccionada);

            if (sombra == sombraCorrecta) {
                // ¡Correcto! Reproducir sonido de acierto
                SoundEffectManager.playCorrectSound();
                
                // Colocar letra correctamente
                letraSeleccionada.setLayoutX(sombra.getLayoutX());
                letraSeleccionada.setLayoutY(sombra.getLayoutY());
                letraSeleccionada.setFitWidth(sombra.getFitWidth());
                letraSeleccionada.setFitHeight(sombra.getFitHeight());

                letraSeleccionada.setEffect(null);
                letrasColocadas.put(letraSeleccionada, true);
                letraSeleccionada.setStyle("-fx-cursor: default;");
                letraSeleccionada = null;

                verificarJuegoCompletado();

            } else {
                // Incorrecto - reproducir sonido de error y hacer efecto visual
                SoundEffectManager.playErrorSound();
                
                Glow glow = new Glow(0.8);
                sombra.setEffect(glow);

                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                        javafx.application.Platform.runLater(() -> sombra.setEffect(null));
                    } catch (Exception e) {}
                }).start();
            }
        }
    }

    // 🔵 Botones invisibles en FXML llaman a estos métodos:
    @FXML private void clickSombra1() { manejarClickEnSombra(sombra1); }
    @FXML private void clickSombra2() { manejarClickEnSombra(sombra2); }
    @FXML private void clickSombra3() { manejarClickEnSombra(sombra3); }
    @FXML private void clickSombra4() { manejarClickEnSombra(sombra4); }
    @FXML private void clickSombra5() { manejarClickEnSombra(sombra5); }

    private void verificarJuegoCompletado() {
        boolean todas = letrasColocadas.values().stream().allMatch(v -> v);

        if (todas) {
            // Esperar 3 segundos antes de mostrar victoria
            Timeline t = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> mostrarMensajeVictoria())
            );
            t.play();
        }
    }

    private void mostrarMensajeVictoria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("victoria-view.fxml"));
            Pane mensajePane = loader.load();
            rootPane.getChildren().add(mensajePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void volverAlMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tierra-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle("Planeta Tierra - Menú");
            stage.setScene(scene);
            // La música del menú se cambiará automáticamente en TierraMenuController.initialize()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
