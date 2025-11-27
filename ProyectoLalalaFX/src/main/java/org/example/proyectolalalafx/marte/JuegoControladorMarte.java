package org.example.proyectolalalafx.marte;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JuegoControladorMarte {

    private final JuegoVistaMarte vista;
    private final StackPane contenedorPadre;
    private final List<CartaMarte> cartasVolteadas = new ArrayList<>();
    private int paresEncontrados = 0;
    private int movimientos = 0;
    private boolean bloqueado = false; // Evitar clicks durante animaciones
    private static final int NUM_PARES = 6;
    // Quitamos el regalo 🎁 y lo reemplazaremos con una imagen
    private final String[] emojis = {"🎅", "🎄", "⭐", "🔔", "🦌"};
    // Ruta de la imagen personalizada - MARIQUITA
    private static final String RUTA_IMAGEN_PERSONALIZADA = "/org/example/proyectolalalafx/marte/MARIQUITA.png";

    // Gestor de sonidos
    private final GestorSonidosMarte gestorSonidos;

    public JuegoControladorMarte(JuegoVistaMarte vista, StackPane contenedorPadre) {
        this.vista = vista;
        this.contenedorPadre = contenedorPadre;
        this.gestorSonidos = GestorSonidosMarte.obtenerInstancia();
    }

    public void inicializarJuego() {
        List<Object[]> listaCartas = new ArrayList<>();

        // Agregar los emojis normales (2 de cada uno)
        for (String e : emojis) {
            listaCartas.add(new Object[]{e, false, null}); // emoji, esImagen, rutaImagen
            listaCartas.add(new Object[]{e, false, null});
        }

        // Agregar la pareja de imagen personalizada
        listaCartas.add(new Object[]{"IMAGEN", true, RUTA_IMAGEN_PERSONALIZADA});
        listaCartas.add(new Object[]{"IMAGEN", true, RUTA_IMAGEN_PERSONALIZADA});

        Collections.shuffle(listaCartas);

        GridPane grid = vista.getTablero();
        grid.getChildren().clear();

        for (int i = 0; i < listaCartas.size(); i++) {
            Object[] datoCarta = listaCartas.get(i);
            String identificador = (String) datoCarta[0];
            boolean esImagen = (boolean) datoCarta[1];
            String rutaImagen = (String) datoCarta[2];

            CartaMarte carta = new CartaMarte(identificador, esImagen, rutaImagen);
            carta.getButton().setOnAction(e -> manejarVolteo(carta));
            grid.add(carta.getStack(), i % 4, i / 4);
        }
    }

    public void reiniciarJuego() {
        paresEncontrados = 0;
        movimientos = 0;
        bloqueado = false;
        cartasVolteadas.clear();
        vista.actualizarMovimientos(0);
        inicializarJuego();
    }

    private void manejarVolteo(CartaMarte carta) {
        if (carta.isVolteada() || carta.isEncontrada() || cartasVolteadas.size() >= 2 || bloqueado) return;

        carta.voltear();
        cartasVolteadas.add(carta);

        if (cartasVolteadas.size() == 2) {
            bloqueado = true;
            movimientos++;
            vista.actualizarMovimientos(movimientos);
            PauseTransition pausa = new PauseTransition(Duration.seconds(1));
            pausa.setOnFinished(e -> {
                comprobarPareja();
                bloqueado = false;
            });
            pausa.play();
        }
    }

    private void comprobarPareja() {
        CartaMarte c1 = cartasVolteadas.get(0);
        CartaMarte c2 = cartasVolteadas.get(1);

        if (c1.getEmoji().equals(c2.getEmoji())) {
            // ¡Pareja encontrada! Reproducir sonido de acierto
            gestorSonidos.reproducirAcierto();

            c1.marcarEncontrada();
            c2.marcarEncontrada();
            paresEncontrados++;

            if (paresEncontrados == NUM_PARES) {
                // Usar Platform.runLater para evitar el error de showAndWait durante animación
                Platform.runLater(this::mostrarMensajeVictoria);
            }
        } else {
            // Pareja incorrecta. Reproducir sonido de fallo
            gestorSonidos.reproducirFallo();

            c1.voltear();
            c2.voltear();
        }
        cartasVolteadas.clear();
    }

    private void mostrarMensajeVictoria() {
        // ¡Victoria! Reproducir sonido de celebración y pausar música de fondo
        gestorSonidos.pausarMusicaDeFondo();
        gestorSonidos.reproducirVictoria();

        try {
            // Cargar el panel de victoria
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/proyectolalalafx/marte/victoria-marte-view.fxml"));
            StackPane panelVictoria = loader.load();
            
            // Obtener los labels para actualizar estadísticas
            Label labelMovimientos = (Label) panelVictoria.lookup("#labelMovimientos");
            Label labelParejas = (Label) panelVictoria.lookup("#labelParejas");
            Button btnContinuar = (Button) panelVictoria.lookup("#btnContinuar");
            
            if (labelMovimientos != null) {
                labelMovimientos.setText("• Movimientos: " + movimientos);
            }
            if (labelParejas != null) {
                labelParejas.setText("• Parejas encontradas: " + paresEncontrados + "/" + NUM_PARES);
            }
            
            // Agregar hover effects al botón
            if (btnContinuar != null) {
                btnContinuar.setOnMouseEntered(e -> btnContinuar.setStyle("""
                    -fx-background-color: linear-gradient(to bottom, #5CBF60, #4FB052);
                    -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;
                    -fx-padding: 15 40; -fx-background-radius: 25; -fx-cursor: hand;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0, 0, 4);
                    -fx-scale-x: 1.05; -fx-scale-y: 1.05;
                """));
                
                btnContinuar.setOnMouseExited(e -> btnContinuar.setStyle("""
                    -fx-background-color: linear-gradient(to bottom, #4CAF50, #45A049);
                    -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;
                    -fx-padding: 15 40; -fx-background-radius: 25; -fx-cursor: hand;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 0, 3);
                """));
                
                btnContinuar.setOnAction(e -> {
                    contenedorPadre.getChildren().remove(panelVictoria);
                    mostrarDialogoReiniciar();
                });
            }
            
            // Agregar el panel al contenedor padre
            contenedorPadre.getChildren().add(panelVictoria);
            
        } catch (Exception e) {
            System.err.println("Error al mostrar panel de victoria: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostrarDialogoReiniciar() {
        try {
            // Cargar el panel de reiniciar
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/proyectolalalafx/marte/reiniciar-marte-view.fxml"));
            StackPane panelReiniciar = loader.load();
            
            Button btnSi = (Button) panelReiniciar.lookup("#btnSi");
            Button btnNo = (Button) panelReiniciar.lookup("#btnNo");
            
            // Efectos hover para btnSi
            if (btnSi != null) {
                btnSi.setOnMouseEntered(e -> btnSi.setStyle("""
                    -fx-background-color: linear-gradient(to bottom, #00E676, #00C853);
                    -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;
                    -fx-padding: 15 35; -fx-background-radius: 30; -fx-cursor: hand;
                    -fx-effect: dropshadow(gaussian, rgba(0,230,118,0.8), 15, 0, 0, 5);
                    -fx-scale-x: 1.08; -fx-scale-y: 1.08;
                """));
                
                btnSi.setOnMouseExited(e -> btnSi.setStyle("""
                    -fx-background-color: linear-gradient(to bottom, #00C851, #007E33);
                    -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;
                    -fx-padding: 15 35; -fx-background-radius: 30; -fx-cursor: hand;
                    -fx-effect: dropshadow(gaussian, rgba(0,200,81,0.6), 10, 0, 0, 4);
                """));
                
                btnSi.setOnAction(e -> {
                    contenedorPadre.getChildren().remove(panelReiniciar);
                    gestorSonidos.iniciarMusicaDeFondo();
                    reiniciarJuego();
                });
            }
            
            // Efectos hover para btnNo
            if (btnNo != null) {
                btnNo.setOnMouseEntered(e -> btnNo.setStyle("""
                    -fx-background-color: linear-gradient(to bottom, #FF6B6B, #EE0000);
                    -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;
                    -fx-padding: 15 35; -fx-background-radius: 30; -fx-cursor: hand;
                    -fx-effect: dropshadow(gaussian, rgba(255,107,107,0.8), 15, 0, 0, 5);
                    -fx-scale-x: 1.08; -fx-scale-y: 1.08;
                """));
                
                btnNo.setOnMouseExited(e -> btnNo.setStyle("""
                    -fx-background-color: linear-gradient(to bottom, #FF4444, #CC0000);
                    -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;
                    -fx-padding: 15 35; -fx-background-radius: 30; -fx-cursor: hand;
                    -fx-effect: dropshadow(gaussian, rgba(255,68,68,0.6), 10, 0, 0, 4);
                """));
                
                btnNo.setOnAction(e -> {
                    contenedorPadre.getChildren().remove(panelReiniciar);
                    gestorSonidos.liberarRecursos();
                    volverAlMenuPrincipal();
                });
            }
            
            // Agregar el panel al contenedor padre
            contenedorPadre.getChildren().add(panelReiniciar);
            
        } catch (Exception e) {
            System.err.println("Error al mostrar panel de reiniciar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void volverAlMenuPrincipal() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/org/example/proyectolalalafx/main-view.fxml")
            );
            javafx.scene.Scene newScene = new javafx.scene.Scene(loader.load(), 1920, 1080);
            javafx.stage.Stage stage = (javafx.stage.Stage) vista.getRoot().getScene().getWindow();
            stage.setScene(newScene);
        } catch (Exception e) {
            System.err.println("Error al volver al menú principal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
