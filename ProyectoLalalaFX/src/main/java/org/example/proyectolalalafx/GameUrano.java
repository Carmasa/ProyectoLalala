// src/main/java/org/example/proyectolalalafx/GameSaturno.java

package org.example.proyectolalalafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GameUrano {

    @FXML private ImageView muñeco;
    @FXML private Circle zonaCabeza, zonaMano1, zonaMano2, zonaPiernaIzq, zonaPiernaDer, zonaOreja1, zonaCodo1, zonaHombre,
            zonaCodo2, zonaOreja2, zonaTorso, zonaCuello, zonaHombro, zonaOjo1, zonaBoca, zonaOjo2, zonaPie1, zonaPie, zonaNariz,
            zonaPierna1, zonaPelo, zonaPierna2;

    // Nuevas imágenes para botones
    @FXML private ImageView imgLuna, imgEsp, imgEng;

    // Mapa para asociar círculos con nombres en ambos idiomas
    private final Map<Circle, String[]> partesDelCuerpo = new HashMap<>();

    // Reproductor de audio
    private MediaPlayer mediaPlayer;

    // Variable para guardar el idioma actual
    private String idiomaActual = "Español"; // Por defecto

    @FXML
    public void initialize() {
        inicializarPartesDelCuerpo();

        // Asignar eventos de clic
        for (Circle circle : partesDelCuerpo.keySet()) {
            circle.setOnMouseClicked(this::onParteClick);
        }
    }

    private void inicializarPartesDelCuerpo() {
        String[] cabeza = {"cabeza", "head"};
        String[] mano = {"mano", "hand"};
        String[] pierna = {"pierna", "leg"};
        String[] rodilla = {"rodilla", "knee"};
        String[] oreja = {"oreja", "ear"};
        String[] codo = {"codo", "elbow"};
        String[] hombro = {"hombro", "shoulder"};
        String[] ojo = {"ojo", "eye"};
        String[] pie = {"pie", "foot"};
        String[] torso = {"torso", "torso"};
        String[] cuello = {"cuello", "neck"};
        String[] boca = {"boca", "mouth"};
        String[] nariz = {"nariz", "nose"};
        String[] pelo = {"pelo", "hair"};

        partesDelCuerpo.put(zonaCabeza, cabeza);
        partesDelCuerpo.put(zonaMano1, mano);
        partesDelCuerpo.put(zonaMano2, mano);
        partesDelCuerpo.put(zonaPiernaIzq, rodilla);
        partesDelCuerpo.put(zonaPiernaDer, rodilla);
        partesDelCuerpo.put(zonaOreja1, oreja);
        partesDelCuerpo.put(zonaCodo1, codo);
        partesDelCuerpo.put(zonaHombre, hombro);
        partesDelCuerpo.put(zonaCodo2, codo);
        partesDelCuerpo.put(zonaOreja2, oreja);
        partesDelCuerpo.put(zonaTorso, torso);
        partesDelCuerpo.put(zonaCuello, cuello);
        partesDelCuerpo.put(zonaHombro, hombro);
        partesDelCuerpo.put(zonaOjo1, ojo);
        partesDelCuerpo.put(zonaBoca, boca);
        partesDelCuerpo.put(zonaOjo2, ojo);
        partesDelCuerpo.put(zonaPie1, pie);
        partesDelCuerpo.put(zonaPie, pie);
        partesDelCuerpo.put(zonaNariz, nariz);
        partesDelCuerpo.put(zonaPierna1, pierna);
        partesDelCuerpo.put(zonaPelo, pelo);
        partesDelCuerpo.put(zonaPierna2, pierna);
    }

    @FXML
    private void onParteClick(MouseEvent event) {
        Circle clickedCircle = (Circle) event.getSource();
        String[] nombres = partesDelCuerpo.get(clickedCircle);
        if (nombres != null) {
            String nombreEnIdioma = "Español".equals(idiomaActual) ? nombres[0] : nombres[1];
            reproducirAudio(nombreEnIdioma);
        }
    }

    private void reproducirAudio(String nombre) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        String audioFileName = "audio/" + nombre.replace(" ", "_") + ".mp3";
        URL audioUrl = getClass().getResource(audioFileName);

        if (audioUrl != null) {
            Media media = new Media(audioUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } else {
            System.out.println("Archivo de audio no encontrado: " + audioFileName);
        }
    }

    @FXML
    private void goBack() {
        try {
            Parent mainView = FXMLLoader.load(getClass().getResource("main-view.fxml"));
            Scene currentScene = muñeco.getScene();
            currentScene.setRoot(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cambiarAEspanol() {
        idiomaActual = "Español";
    }

    @FXML
    private void cambiarAIngles() {
        idiomaActual = "English";
    }
}