package org.example.proyectolalalafx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ArbolAnimalesController {

    @FXML
    private Pane rootPane;

    // Animales (piezas que el niño arrastra)
    @FXML
    private ImageView perro;

    @FXML
    private ImageView pajaro;

    @FXML
    private ImageView conejo;

    @FXML
    private ImageView pez;

    @FXML
    private ImageView gato;

    // Sombras / huecos del árbol
    @FXML
    private ImageView sombra1; // Perro (arriba)
    @FXML
    private ImageView sombra2; // Conejo (medio izquierda)
    @FXML
    private ImageView sombra3; // pajaro (medio derecha)
    @FXML
    private ImageView sombra4; // Gato (abajo izquierda)
    @FXML
    private ImageView sombra5; // Pez (abajo derecha)

    private ImageView animalSeleccionado = null;
    private final Map<ImageView, ImageView> animalASombra = new HashMap<>();
    private final Map<ImageView, Boolean> animalesColocados = new HashMap<>();
    private final DropShadow efectoSeleccion = new DropShadow();

    @FXML
    public void initialize() {
        // Reproducir música del minijuego
        MusicManager.playMusic("audio/audio-minijuegos-arboles/background_music/Cosmic Christmas Lights.mp3");
        
        // Efecto cuando seleccionas una pieza
        efectoSeleccion.setColor(Color.YELLOW);
        efectoSeleccion.setRadius(20);
        efectoSeleccion.setSpread(0.7);

        // Mapear cada animal con su sombra correcta
        animalASombra.put(perro,   sombra1);
        animalASombra.put(conejo,  sombra2);
        animalASombra.put(pajaro,  sombra3);
        animalASombra.put(gato,    sombra4);
        animalASombra.put(pez,     sombra5);

        // Inicializar estado (sin colocar)
        animalesColocados.put(perro,   false);
        animalesColocados.put(pajaro,  false);
        animalesColocados.put(conejo,  false);
        animalesColocados.put(pez,     false);
        animalesColocados.put(gato,    false);

        // Eventos de selección para cada animal
        configurarSeleccion(perro);
        configurarSeleccion(pajaro);
        configurarSeleccion(conejo);
        configurarSeleccion(pez);
        configurarSeleccion(gato);

        // Eventos de click para cada sombra (por si el niño pincha justo ahí)
        configurarSombra(sombra1);
        configurarSombra(sombra2);
        configurarSombra(sombra3);
        configurarSombra(sombra4);
        configurarSombra(sombra5);
    }

    private void configurarSeleccion(ImageView animal) {
        animal.setOnMouseClicked(event -> {
            if (!animalesColocados.get(animal)) {
                if (animalSeleccionado != null) {
                    animalSeleccionado.setEffect(null);
                }
                animalSeleccionado = animal;
                animalSeleccionado.setEffect(efectoSeleccion);
            }
        });
        animal.setStyle("-fx-cursor: hand;");
    }

    private void configurarSombra(ImageView sombra) {
        sombra.setOnMouseClicked(event -> {
            manejarClickEnSombra(sombra);
        });
        sombra.setStyle("-fx-cursor: hand;");
        sombra.setOpacity(0.3);
        sombra.setPickOnBounds(true);
    }

    /**
     * Lógica común al hacer clic en una sombra (o en el botón invisible encima).
     */
    private void manejarClickEnSombra(ImageView sombra) {
        if (animalSeleccionado != null) {
            ImageView sombraCorrecta = animalASombra.get(animalSeleccionado);

            if (sombra == sombraCorrecta) {
                // ¡Correcto! Reproducir sonido de acierto
                SoundEffectManager.playCorrectSound();
                
                // Colocar animal en la sombra
                animalSeleccionado.setLayoutX(sombra.getLayoutX());
                animalSeleccionado.setLayoutY(sombra.getLayoutY());
                animalSeleccionado.setFitWidth(sombra.getFitWidth());
                animalSeleccionado.setFitHeight(sombra.getFitHeight());

                animalSeleccionado.setEffect(null);
                animalesColocados.put(animalSeleccionado, true);
                animalSeleccionado.setStyle("-fx-cursor: default;");
                animalSeleccionado = null;

                verificarJuegoCompletado();
            } else {
                // Incorrecto - reproducir sonido de error
                SoundEffectManager.playErrorSound();
                
                // Efecto de error
                Glow errorEffect = new Glow(0.8);
                sombra.setEffect(errorEffect);

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

    // Métodos que llaman los botones invisibles
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
        boolean todosColocados = animalesColocados.values().stream().allMatch(colocado -> colocado);
        if (todosColocados) {

            // Esperar 3 segundos antes de mostrar la victoria
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // 3000 ms = 3 segundos

                    javafx.application.Platform.runLater(() -> {
                        mostrarMensajeVictoria();
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
