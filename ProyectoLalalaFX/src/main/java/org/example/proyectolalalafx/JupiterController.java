package org.example.proyectolalalafx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class JupiterController {

    @FXML
    private Pane rootPane;
    @FXML private ImageView imgAmarillo, imgAzul, imgMarron, imgMorado, imgRojo, imgRosa, imgVerde;
    @FXML private ImageView btnRepetir;

    private ImageView fondoView;

    private final Map<String, ImageView> colorToImage = new HashMap<>();
    private final Map<String, String> colorToAudio = new HashMap<>();
    private String colorObjetivo;
    private final Random random = new Random();
    private MediaPlayer currentPlayer;

    private final double anchoImagen = 150;
    private final double altoImagen = 150;

    public void initialize() {
        // --- Fondo ---
        fondoView = new ImageView(loadImage("/imagenes/fondo.png"));
        fondoView.setMouseTransparent(true);
        fondoView.fitWidthProperty().bind(rootPane.widthProperty());
        fondoView.fitHeightProperty().bind(rootPane.heightProperty());
        fondoView.setPreserveRatio(false);
        rootPane.getChildren().add(0, fondoView);

        // --- Botón repetir ---
        btnRepetir.setImage(loadImage("/imagenes/repetir.png"));
        btnRepetir.setPreserveRatio(true);
        btnRepetir.setFitWidth(60);
        btnRepetir.setFitHeight(60);
        btnRepetir.setPickOnBounds(true);
        btnRepetir.layoutXProperty().bind(rootPane.widthProperty().subtract(btnRepetir.fitWidthProperty()).divide(2));
        btnRepetir.setLayoutY(20);
        btnRepetir.setOnMouseClicked(e -> repetirColor());



        // --- Mapear colores a ImageView (sin naranja) ---
        colorToImage.put("amarillo", imgAmarillo);
        colorToImage.put("azul", imgAzul);
        colorToImage.put("marron", imgMarron);
        colorToImage.put("morado", imgMorado);
        colorToImage.put("rojo", imgRojo);
        colorToImage.put("rosa", imgRosa);
        colorToImage.put("verde", imgVerde);

        // --- Mapear audios (sin naranja) ---
        for (String color : colorToImage.keySet()) {
            colorToAudio.put(color, "/audios/" + color + ".mp3");
        }

        // --- Cargar imágenes ---
        for (Map.Entry<String, ImageView> entry : colorToImage.entrySet()) {
            String color = entry.getKey();
            entry.getValue().setImage(loadImage("/imagenes/" + color + ".png"));
        }

        // --- Posicionar imágenes aleatoriamente sin sobreposición ---
        colocarImagenesAleatoriamente();

        // --- Eventos clic ---
        for (Map.Entry<String, ImageView> entry : colorToImage.entrySet()) {
            String color = entry.getKey();
            ImageView iv = entry.getValue();
            iv.setOnMouseClicked(e -> verificarYCambiarPosicion(iv, color));
        }

        // --- Primera ronda ---
        elegirNuevoColorAleatorio();
    }

    private Image loadImage(String path) {
        URL resource = getClass().getResource(path);
        if (resource == null) throw new RuntimeException("No se encontró la imagen: " + path);
        return new Image(resource.toExternalForm());
    }

    private void colocarImagenesAleatoriamente() {
        Platform.runLater(() -> {
            List<Rectangle2D> ocupadas = new ArrayList<>();

            // Área del botón repetir como ocupada
            ocupadas.add(new javafx.geometry.Rectangle2D(btnRepetir.getLayoutX(), btnRepetir.getLayoutY(),
                    btnRepetir.getFitWidth(), btnRepetir.getFitHeight()));


            for (ImageView iv : colorToImage.values()) {
                boolean posicionValida;
                double x = 0, y = 0;

                do {
                    x = random.nextDouble() * (rootPane.getWidth() - anchoImagen);
                    y = random.nextDouble() * (rootPane.getHeight() - altoImagen);

                    javafx.geometry.Rectangle2D nuevoRect = new javafx.geometry.Rectangle2D(x, y, anchoImagen, altoImagen);
                    posicionValida = ocupadas.stream().noneMatch(r -> r.intersects(nuevoRect));
                } while (!posicionValida);

                iv.setLayoutX(x);
                iv.setLayoutY(y);
                ocupadas.add(new javafx.geometry.Rectangle2D(x, y, anchoImagen, altoImagen));
            }
        });
    }
    @FXML
    private void goBack() {
        try {
            Parent mainView = FXMLLoader.load(getClass().getResource("/org/example/proyectolalalafx/main-view.fxml"));
            Scene currentScene = rootPane.getScene();
            if (currentScene != null) {
                currentScene.setRoot(mainView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void verificarYCambiarPosicion(ImageView iv, String colorTocado) {
        if (colorTocado.equals(colorObjetivo)) {
            reproducirAudioConCallback("/audios/aplausos.mp3", () -> Platform.runLater(() -> {
                // Elegir otra imagen aleatoria distinta
                List<ImageView> otrasImagenes = new ArrayList<>(colorToImage.values());
                otrasImagenes.remove(iv);
                ImageView ivAleatoria = otrasImagenes.get(random.nextInt(otrasImagenes.size()));

                // Intercambiar posiciones
                double tempX = iv.getLayoutX();
                double tempY = iv.getLayoutY();
                iv.setLayoutX(ivAleatoria.getLayoutX());
                iv.setLayoutY(ivAleatoria.getLayoutY());
                ivAleatoria.setLayoutX(tempX);
                ivAleatoria.setLayoutY(tempY);

                // Elegir nuevo color objetivo
                elegirNuevoColorAleatorio();
            }));
        } else {
            reproducirAudio("/audios/error.mp3");
        }
    }

    private void elegirNuevoColorAleatorio() {
        List<String> colores = new ArrayList<>(colorToImage.keySet());
        colorObjetivo = colores.get(random.nextInt(colores.size()));
        reproducirAudio(colorToAudio.get(colorObjetivo));
    }

    private void repetirColor() {
        reproducirAudio(colorToAudio.get(colorObjetivo));
    }


    private void reproducirAudio(String ruta) {
        try {
            if (currentPlayer != null) currentPlayer.stop();
            URL resource = getClass().getResource(ruta);
            if (resource == null) {
                System.err.println("No se encontró el archivo: " + ruta);
                return;
            }
            currentPlayer = new MediaPlayer(new Media(resource.toExternalForm()));
            currentPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reproducirAudioConCallback(String ruta, Runnable callback) {
        try {
            if (currentPlayer != null) currentPlayer.stop();
            URL resource = getClass().getResource(ruta);
            if (resource == null) {
                System.err.println("No se encontró el archivo: " + ruta);
                return;
            }
            currentPlayer = new MediaPlayer(new Media(resource.toExternalForm()));
            currentPlayer.setOnEndOfMedia(() -> {
                callback.run();
                currentPlayer.dispose();
                currentPlayer = null;
            });
            currentPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
