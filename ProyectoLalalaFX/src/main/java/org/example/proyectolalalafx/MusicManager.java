package org.example.proyectolalalafx;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mediaPlayer;
    private static String currentMusic = "";
    
    public static void playMusic(String musicFile) {
        // Si ya hay música sonando y es la misma, no hacer nada
        if (currentMusic.equals(musicFile) && mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            return;
        }
        
        // Detener música anterior si existe
        stopMusic();
        
        try {
            String musicPath = MusicManager.class.getResource(musicFile).toExternalForm();
            Media media = new Media(musicPath);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Reproducción en bucle
            mediaPlayer.setVolume(0.25); // Volumen al 25%
            mediaPlayer.play();
            currentMusic = musicFile;
        } catch (Exception e) {
            System.err.println("Error al reproducir música: " + musicFile);
            e.printStackTrace();
        }
    }
    
    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
        currentMusic = "";
    }
    
    public static void setVolume(double volume) {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }
}
