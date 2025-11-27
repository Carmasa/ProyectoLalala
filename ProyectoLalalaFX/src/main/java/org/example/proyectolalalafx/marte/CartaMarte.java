package org.example.proyectolalalafx.marte;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.io.InputStream;

public class CartaMarte {
    private final String emoji;
    private final Button button;
    private final StackPane stack;
    private boolean volteada = false;
    private boolean encontrada = false;
    private final String colorFondo;
    private final String colorTexto;
    private final boolean esImagen;
    private ImageView imageView;

    public CartaMarte(String emoji) {
        this(emoji, false, null);
    }

    public CartaMarte(String emoji, boolean esImagen, String rutaImagen) {
        this.emoji = emoji;
        this.esImagen = esImagen;

        String[] colores = obtenerColoresPorEmoji(esImagen ? "IMAGEN" : emoji);
        this.colorFondo = colores[0];
        this.colorTexto = colores[1];

        this.button = new Button("🎁");
        button.setFont(javafx.scene.text.Font.font("Comic Sans MS", 28));
        button.setMinSize(100, 100);
        button.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #FFCCCC, #FF6666);
            -fx-text-fill: white;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 2, 2);
        """);

        if (esImagen && rutaImagen != null) {
            try {
                InputStream is = getClass().getResourceAsStream(rutaImagen);
                if (is != null) {
                    Image img = new Image(is);
                    imageView = new ImageView(img);
                    imageView.setFitWidth(70);
                    imageView.setFitHeight(70);
                    imageView.setPreserveRatio(true);
                } else {
                    System.err.println("No se encontró la imagen: " + rutaImagen);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen: " + e.getMessage());
            }
        }

        this.stack = new StackPane(button);
    }

    private String[] obtenerColoresPorEmoji(String emoji) {
        return switch (emoji) {
            case "🎅" -> new String[]{"linear-gradient(to bottom, #FF6B6B, #C92A2A)", "#FFFFFF"};
            case "🎄" -> new String[]{"linear-gradient(to bottom, #51CF66, #2F9E44)", "#FFFFFF"};
            case "⭐" -> new String[]{"linear-gradient(to bottom, #FFD700, #FFA500)", "#333333"};
            case "🔔" -> new String[]{"linear-gradient(to bottom, #A78BFA, #7C3AED)", "#FFFFFF"};
            case "🦌" -> new String[]{"linear-gradient(to bottom, #4FC3F7, #0288D1)", "#FFFFFF"};
            case "IMAGEN" -> new String[]{"linear-gradient(to bottom, #FFB6C1, #FF69B4)", "#FFFFFF"};
            default -> new String[]{"linear-gradient(to bottom, #FFFFCC, #FFCC66)", "#333333"};
        };
    }

    public void voltear() {
        if (encontrada) return;

        ScaleTransition shrink = new ScaleTransition(Duration.millis(200), button);
        shrink.setToX(0.1);
        shrink.setToY(1.0);

        ScaleTransition grow = new ScaleTransition(Duration.millis(200), button);
        grow.setToX(1.0);
        grow.setToY(1.0);

        shrink.setOnFinished(e -> {
            volteada = !volteada;

            if (volteada) {
                if (esImagen && imageView != null) {
                    button.setText("");
                    button.setGraphic(imageView);
                } else {
                    button.setText(emoji);
                    button.setGraphic(null);
                }

                String estilo = "-fx-background-color: " + colorFondo + ";" +
                                "-fx-text-fill: " + colorTexto + ";" +
                                "-fx-background-radius: 20;" +
                                "-fx-border-radius: 20;" +
                                "-fx-border-color: white;" +
                                "-fx-border-width: 3;" +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 2, 2);" +
                                "-fx-font-size: 40px;";
                button.setStyle(estilo);
            } else {
                button.setText("🎁");
                button.setGraphic(null);
                button.setStyle("""
                    -fx-background-color: linear-gradient(to bottom, #FFCCCC, #FF6666);
                    -fx-text-fill: white;
                    -fx-background-radius: 20;
                    -fx-border-radius: 20;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0, 2, 2);
                """);
            }
        });

        new SequentialTransition(shrink, grow).play();
    }

    public void marcarEncontrada() {
        encontrada = true;
        if (esImagen && imageView != null) {
            button.setGraphic(imageView);
            button.setText("");
        }

        String estilo = "-fx-background-color: " + colorFondo + ";" +
                        "-fx-text-fill: " + colorTexto + ";" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: #00FF00;" +
                        "-fx-border-width: 5;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,255,0,0.6), 15, 0, 0, 0);" +
                        "-fx-font-size: 40px;" +
                        "-fx-opacity: 0.9;";
        button.setStyle(estilo);
    }

    public boolean isVolteada() {
        return volteada;
    }

    public boolean isEncontrada() {
        return encontrada;
    }

    public String getEmoji() {
        return emoji;
    }

    public Button getButton() {
        return button;
    }

    public StackPane getStack() {
        return stack;
    }
}
