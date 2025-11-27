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

public class ArbolNumerosController {

    @FXML
    private Pane rootPane;

    @FXML
    private ImageView numero1;

    @FXML
    private ImageView numero2;

    @FXML
    private ImageView numero3;

    @FXML
    private ImageView numero4;

    @FXML
    private ImageView numero5;

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

    private ImageView adornoSeleccionado = null;
    private final Map<ImageView, ImageView> adornoASombra = new HashMap<>();
    private final Map<ImageView, Boolean> adornosColocados = new HashMap<>();
    private final DropShadow efectoSeleccion = new DropShadow();

    @FXML
    public void initialize() {
        // Reproducir música del minijuego
        MusicManager.playMusic("audio/audio-minijuegos-arboles/background_music/Cosmic Christmas Lights.mp3");
        
        // Configurar efecto de selección
        efectoSeleccion.setColor(Color.YELLOW);
        efectoSeleccion.setRadius(20);
        efectoSeleccion.setSpread(0.7);

        // Mapear cada número con su sombra correspondiente
        adornoASombra.put(numero1, sombra1);
        adornoASombra.put(numero2, sombra2);
        adornoASombra.put(numero3, sombra3);
        adornoASombra.put(numero4, sombra4);
        adornoASombra.put(numero5, sombra5);

        // Inicializar estado de números
        adornosColocados.put(numero1, false);
        adornosColocados.put(numero2, false);
        adornosColocados.put(numero3, false);
        adornosColocados.put(numero4, false);
        adornosColocados.put(numero5, false);

        // Configurar eventos de clic para cada número/adorno
        configurarSeleccion(numero1);
        configurarSeleccion(numero2);
        configurarSeleccion(numero3);
        configurarSeleccion(numero4);
        configurarSeleccion(numero5);

        // Configurar eventos de clic para cada sombra (por si pinchan justo ahí)
        configurarSombra(sombra1);
        configurarSombra(sombra2);
        configurarSombra(sombra3);
        configurarSombra(sombra4);
        configurarSombra(sombra5);
    }

    private void configurarSeleccion(ImageView adorno) {
        adorno.setOnMouseClicked(event -> {
            // Solo permitir seleccionar si no está ya colocado
            if (!adornosColocados.get(adorno)) {
                // Deseleccionar el anterior
                if (adornoSeleccionado != null) {
                    adornoSeleccionado.setEffect(null);
                }

                // Seleccionar el nuevo
                adornoSeleccionado = adorno;
                adornoSeleccionado.setEffect(efectoSeleccion);
            }
        });
        adorno.setStyle("-fx-cursor: hand;");
    }

    private void configurarSombra(ImageView sombra) {
        sombra.setOnMouseClicked(event -> manejarClickEnSombra(sombra));
        sombra.setStyle("-fx-cursor: hand;");
        sombra.setOpacity(0.3); // Hacer las sombras más visibles
        sombra.setPickOnBounds(true);
    }

    /**
     * Lógica común al hacer clic en una sombra o en el botón invisible encima.
     */
    private void manejarClickEnSombra(ImageView sombra) {
        if (adornoSeleccionado != null) {
            // Verificar si la sombra corresponde al adorno seleccionado
            ImageView sombraCorrecta = adornoASombra.get(adornoSeleccionado);

            if (sombra == sombraCorrecta) {
                // ¡Correcto! Reproducir sonido de acierto
                SoundEffectManager.playCorrectSound();
                
                // Colocar el adorno sobre la sombra
                adornoSeleccionado.setLayoutX(sombra.getLayoutX());
                adornoSeleccionado.setLayoutY(sombra.getLayoutY());
                adornoSeleccionado.setFitWidth(sombra.getFitWidth());
                adornoSeleccionado.setFitHeight(sombra.getFitHeight());

                // Remover efecto de selección
                adornoSeleccionado.setEffect(null);

                // Marcar como colocado
                adornosColocados.put(adornoSeleccionado, true);
                adornoSeleccionado.setStyle("-fx-cursor: default;");

                // Deseleccionar
                adornoSeleccionado = null;

                // Verificar si todos los números están colocados
                verificarJuegoCompletado();
            } else {
                // Incorrecto - reproducir sonido de error y hacer efecto visual
                SoundEffectManager.playErrorSound();
                
                Glow errorEffect = new Glow(0.8);
                sombra.setEffect(errorEffect);

                // Remover el efecto después de 500ms
                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                        javafx.application.Platform.runLater(() -> sombra.setEffect(null));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    // Métodos que llaman los botones invisibles del FXML
    @FXML
    private void clickSombra1() {
        manejarClickEnSombra(sombra1);
    }

    @FXML
    private void clickSombra2() {
        manejarClickEnSombra(sombra2);
    }

    @FXML
    private void clickSombra3() {
        manejarClickEnSombra(sombra3);
    }

    @FXML
    private void clickSombra4() {
        manejarClickEnSombra(sombra4);
    }

    @FXML
    private void clickSombra5() {
        manejarClickEnSombra(sombra5);
    }

    private void verificarJuegoCompletado() {
        boolean todosColocados = adornosColocados.values().stream().allMatch(colocado -> colocado);

        if (todosColocados) {
            // Esperar 3 segundos antes de mostrar victoria
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
                mostrarMensajeVictoria();
            }));
            timeline.play();
        }
    }

    private void mostrarMensajeVictoria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("victoria-view.fxml"));
            Pane mensajePane = loader.load();

            // Agregar el mensaje encima del contenido actual
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
