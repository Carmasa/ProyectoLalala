package org.example.proyectolalalafx.marte;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;

/**
 * Gestor de sonidos del juego de memoria de Marte.
 * Maneja efectos de sonido y música de fondo.
 */
public class GestorSonidosMarte {

    private MediaPlayer musicaDeFondo;
    private MediaPlayer efectoAcierto;
    private MediaPlayer efectoFallo;
    private MediaPlayer efectoVictoria;

    private boolean sonidosActivados = true;
    private boolean musicaActivada = true;

    private static GestorSonidosMarte instancia;

    private GestorSonidosMarte() {
        // Constructor privado para patrón Singleton
    }

    /**
     * Obtiene la instancia única del gestor de sonidos
     */
    public static GestorSonidosMarte obtenerInstancia() {
        if (instancia == null) {
            instancia = new GestorSonidosMarte();
        }
        return instancia;
    }

    /**
     * Inicializa todos los sonidos del juego
     */
    public void inicializar() {
        try {
            cargarMusicaDeFondo();
            System.out.println("✅ Sistema de sonidos Marte inicializado correctamente");
        } catch (Exception e) {
            System.err.println("⚠️ Error al cargar algunos sonidos: " + e.getMessage());
            System.out.println("ℹ️ El juego continuará sin sonido");
        }
    }

    /**
     * Carga la música de fondo y la configura para reproducción en bucle
     */
    private void cargarMusicaDeFondo() {
        try {
            URL recurso = getClass().getResource("/org/example/proyectolalalafx/marte/sonidos/fondo.mp3");
            if (recurso != null) {
                Media media = new Media(recurso.toString());
                musicaDeFondo = new MediaPlayer(media);
                musicaDeFondo.setCycleCount(MediaPlayer.INDEFINITE); // Reproducir en bucle
                musicaDeFondo.setVolume(0.3); // Volumen moderado para fondo (30%)
                System.out.println("✅ Música de fondo Marte cargada");
            } else {
                System.out.println("ℹ️ No se encontró archivo de música de fondo Marte");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al cargar música de fondo Marte: " + e.getMessage());
        }
    }

    /**
     * Carga el sonido de acierto
     */
    private void cargarEfectoAcierto() {
        try {
            URL recurso = getClass().getResource("/org/example/proyectolalalafx/marte/sonidos/acierto.mp3");
            if (recurso != null) {
                Media media = new Media(recurso.toString());
                efectoAcierto = new MediaPlayer(media);
                efectoAcierto.setVolume(0.6); // Volumen 60%
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al cargar sonido de acierto: " + e.getMessage());
        }
    }

    /**
     * Carga el sonido de fallo
     */
    private void cargarEfectoFallo() {
        try {
            URL recurso = getClass().getResource("/org/example/proyectolalalafx/marte/sonidos/fallo.mp3");
            if (recurso != null) {
                Media media = new Media(recurso.toString());
                efectoFallo = new MediaPlayer(media);
                efectoFallo.setVolume(0.5); // Volumen 50%
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al cargar sonido de fallo: " + e.getMessage());
        }
    }

    /**
     * Carga el sonido de victoria
     */
    private void cargarEfectoVictoria() {
        try {
            URL recurso = getClass().getResource("/org/example/proyectolalalafx/marte/sonidos/victoria.mp3");
            if (recurso != null) {
                Media media = new Media(recurso.toString());
                efectoVictoria = new MediaPlayer(media);
                efectoVictoria.setVolume(0.7); // Volumen 70%
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al cargar sonido de victoria: " + e.getMessage());
        }
    }

    /**
     * Inicia la música de fondo
     */
    public void iniciarMusicaDeFondo() {
        if (musicaActivada && musicaDeFondo != null) {
            try {
                musicaDeFondo.play();
                System.out.println("🎵 Música de fondo Marte iniciada");
            } catch (Exception e) {
                System.err.println("⚠️ Error al reproducir música de fondo Marte: " + e.getMessage());
            }
        }
    }

    /**
     * Detiene la música de fondo
     */
    public void detenerMusicaDeFondo() {
        if (musicaDeFondo != null && musicaDeFondo.getStatus() == MediaPlayer.Status.PLAYING) {
            musicaDeFondo.stop();
            System.out.println("🔇 Música de fondo Marte detenida");
        }
    }

    /**
     * Pausa la música de fondo
     */
    public void pausarMusicaDeFondo() {
        if (musicaDeFondo != null && musicaDeFondo.getStatus() == MediaPlayer.Status.PLAYING) {
            musicaDeFondo.pause();
        }
    }

    /**
     * Reproduce el sonido de acierto (cuando se encuentra una pareja)
     */
    public void reproducirAcierto() {
        if (!sonidosActivados) return;

        try {
            // Cargar efecto si no está cargado
            if (efectoAcierto == null) {
                cargarEfectoAcierto();
            }

            if (efectoAcierto != null) {
                // Reiniciar el sonido si ya se está reproduciendo
                efectoAcierto.stop();
                efectoAcierto.seek(Duration.ZERO);
                efectoAcierto.play();
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al reproducir sonido de acierto: " + e.getMessage());
        }
    }

    /**
     * Reproduce el sonido de fallo (cuando las cartas no coinciden)
     */
    public void reproducirFallo() {
        if (!sonidosActivados) return;

        try {
            // Cargar efecto si no está cargado
            if (efectoFallo == null) {
                cargarEfectoFallo();
            }

            if (efectoFallo != null) {
                efectoFallo.stop();
                efectoFallo.seek(Duration.ZERO);
                efectoFallo.play();
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al reproducir sonido de fallo: " + e.getMessage());
        }
    }

    /**
     * Reproduce el sonido de victoria (cuando se completa el juego)
     */
    public void reproducirVictoria() {
        if (!sonidosActivados) return;

        try {
            // Cargar efecto si no está cargado
            if (efectoVictoria == null) {
                cargarEfectoVictoria();
            }

            if (efectoVictoria != null) {
                efectoVictoria.stop();
                efectoVictoria.seek(Duration.ZERO);
                efectoVictoria.play();
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al reproducir sonido de victoria: " + e.getMessage());
        }
    }

    /**
     * Activa o desactiva todos los efectos de sonido
     */
    public void activarSonidos(boolean activar) {
        this.sonidosActivados = activar;
        System.out.println(activar ? "🔊 Sonidos activados" : "🔇 Sonidos desactivados");
    }

    /**
     * Activa o desactiva la música de fondo
     */
    public void activarMusica(boolean activar) {
        this.musicaActivada = activar;
        if (activar) {
            iniciarMusicaDeFondo();
        } else {
            detenerMusicaDeFondo();
        }
    }

    /**
     * Verifica si los sonidos están activados
     */
    public boolean estanSonidosActivados() {
        return sonidosActivados;
    }

    /**
     * Verifica si la música está activada
     */
    public boolean estaMusicaActivada() {
        return musicaActivada;
    }

    /**
     * Libera todos los recursos de audio
     */
    public void liberarRecursos() {
        if (musicaDeFondo != null) {
            musicaDeFondo.stop();
            musicaDeFondo.dispose();
        }
        if (efectoAcierto != null) {
            efectoAcierto.dispose();
        }
        if (efectoFallo != null) {
            efectoFallo.dispose();
        }
        if (efectoVictoria != null) {
            efectoVictoria.dispose();
        }
        System.out.println("🔇 Recursos de audio Marte liberados");
    }
}
