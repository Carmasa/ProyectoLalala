package org.example.proyectolalalafx;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundEffectManager {
    
    public static void playCorrectSound() {
        playSound("audio/audio-minijuegos-arboles/sound_effect/correcto-100.mp3");
    }
    
    public static void playErrorSound() {
        playSound("audio/audio-minijuegos-arboles/sound_effect/fallo.mp3");
    }
    
    private static void playSound(String soundFile) {
        try {
            String soundPath = SoundEffectManager.class.getResource(soundFile).toExternalForm();
            Media media = new Media(soundPath);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setVolume(0.9); // Volumen al 90%
            mediaPlayer.play();
            
            // Liberar recursos cuando termine de reproducirse
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.dispose();
            });
        } catch (Exception e) {
            System.err.println("Error al reproducir sonido: " + soundFile);
            e.printStackTrace();
        }
    }
}
