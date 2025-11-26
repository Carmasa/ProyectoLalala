// src/main/java/org/example/proyectolalalafx/GameSaturno.java

package org.example.proyectolalalafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameUrano {

    @FXML private ImageView muñeco;
    @FXML private Circle zonaCabeza, zonaMano1, zonaMano2, zonaPiernaIzq, zonaPiernaDer, zonaOreja1, zonaCodo1, zonaHombre,
            zonaCodo2, zonaOreja2, zonaTorso, zonaCuello, zonaHombro, zonaOjo1, zonaBoca, zonaOjo2, zonaPie1, zonaPie, zonaNariz,
            zonaPierna1, zonaPelo, zonaPierna2;

    // Nuevas imágenes para botones
    @FXML private ImageView imgLuna, imgEsp, imgEng;
    
    // Elementos UI del juego
    @FXML private VBox gameModeContainer;
    @FXML private Label lblTargetParte;
    @FXML private Button btnRepetir;
    @FXML private Button btnJugar;

    // Mapa para asociar círculos con nombres en ambos idiomas
    private final Map<Circle, String[]> partesDelCuerpo = new HashMap<>();

    // Reproductor de audio
    private MediaPlayer mediaPlayer;

    // Variable para guardar el idioma actual
    private String idiomaActual = "Español"; // Por defecto
    
    // Variables del juego
    private boolean isGameMode = false;
    private String currentTargetParte;
    private List<Circle> currentTargetZones;
    private List<String> uniqueBodyParts;

    @FXML
    public void initialize() {
        inicializarPartesDelCuerpo();
        inicializarListaPartesUnicas();

        // Asignar eventos de clic
        for (Circle circle : partesDelCuerpo.keySet()) {
            circle.setOnMouseClicked(this::onParteClick);
        }
    }
    
    private void inicializarListaPartesUnicas() {
        // Obtiene partes del cuerpo en el idioma actual
        Set<String> uniqueParts = new HashSet<>();
        for (String[] nombres : partesDelCuerpo.values()) {
            uniqueParts.add(nombres[0]);
        }
        uniqueBodyParts = new ArrayList<>(uniqueParts);
    }

    private void inicializarPartesDelCuerpo() {
        String[] cabeza = {"cabeza", "head"};
        String[] mano = {"mano", "hand"};
        String[] pierna = {"pierna", "leg"};
        String[] rodilla = {"rodilla", "knee"};
        String[] oreja = {"oreja", "ear"};
        String[] codo = {"codo", "elbow"};
        String[] hombro = {"hombro", "shoulder"};
        String[] ojo = {"ojos", "eye"};
        String[] pie = {"pie", "foot"};
        String[] torso = {"torso", "chest"};
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
            if (isGameMode) {
                // Game mode: validate the click
                validarClick(clickedCircle);
            } else {
                // Normal mode: just play the audio
                String nombreEnIdioma = "Español".equals(idiomaActual) ? nombres[0] : nombres[1];
                reproducirAudio(nombreEnIdioma);
            }
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
        actualizarIdiomaEnJuego();
    }

    @FXML
    private void cambiarAIngles() {
        idiomaActual = "English";
        actualizarIdiomaEnJuego();
    }
    
    private void actualizarIdiomaEnJuego() {
        // Si estamos en modo de juego, actualizar el texto mostrado
        if (isGameMode && currentTargetParte != null) {
            String displayName = "Español".equals(idiomaActual) ? currentTargetParte : getEnglishName(currentTargetParte);
            lblTargetParte.setText(displayName.substring(0, 1).toUpperCase() + displayName.substring(1));
        }
    }
    
    // ========== MODO JUEGO ADIVINAR ==========
    
    @FXML
    private void iniciarJuego() {
        isGameMode = true;
        gameModeContainer.setVisible(true);
        btnJugar.setVisible(false);
        seleccionarParteAleatoria();
    }
    
    private void seleccionarParteAleatoria() {
        Random random = new Random();
        int index = random.nextInt(uniqueBodyParts.size());
        currentTargetParte = uniqueBodyParts.get(index);
        
        // Actualizar UI con la parte del cuerpo
        String displayName = "Español".equals(idiomaActual) ? currentTargetParte : getEnglishName(currentTargetParte);
        lblTargetParte.setText(displayName.substring(0, 1).toUpperCase() + displayName.substring(1));
        
        // Encuentra las zonas que corresponde con la parte del cuerpo
        currentTargetZones = new ArrayList<>();
        for (Map.Entry<Circle, String[]> entry : partesDelCuerpo.entrySet()) {
            if (entry.getValue()[0].equals(currentTargetParte)) {
                currentTargetZones.add(entry.getKey());
            }
        }
        reproducirAudio(displayName);
    }
    
    private String getEnglishName(String spanishName) {
        for (String[] nombres : partesDelCuerpo.values()) {
            if (nombres[0].equals(spanishName)) {
                return nombres[1];
            }
        }
        return spanishName;
    }
    
    private void validarClick(Circle clickedZone) {
        if (currentTargetZones.contains(clickedZone)) {
            // Correct!
            reproducirAudioCorrecto();
            // Wait a moment then select next part
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::seleccionarParteAleatoria);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            reproducirAudioError();
        }
    }
    
    private void reproducirAudioCorrecto() {
        System.out.println("¡Correcto!");
        reproducirAudio("success");
    }
    
    private void reproducirAudioError() {
        System.out.println("¡Incorrecto!");
        reproducirAudio("error");
    }
    
    @FXML
    private void repetirAudio() {
        String displayName = "Español".equals(idiomaActual) ? currentTargetParte : getEnglishName(currentTargetParte);
        reproducirAudio(displayName);
    }
}