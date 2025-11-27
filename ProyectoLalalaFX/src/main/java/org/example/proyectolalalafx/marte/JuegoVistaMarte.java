package org.example.proyectolalalafx.marte;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class JuegoVistaMarte {

    private final BorderPane root;
    private final GridPane tablero;
    private final JuegoControladorMarte controlador;
    private final Label labelMovimientos;

    public JuegoVistaMarte(javafx.scene.layout.StackPane contenedorPadre) {
        root = new BorderPane();
        root.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #FFFAF0, #FFDAB9);
            -fx-padding: 20;
        """);

        Label titulo = new Label("🎅 MEMORIA NAVIDEÑA 🎄");
        titulo.setFont(Font.font("Comic Sans MS", 36));
        titulo.setTextFill(Color.DARKRED);
        titulo.setAlignment(Pos.CENTER);

        labelMovimientos = new Label("Movimientos: 0");
        labelMovimientos.setFont(Font.font("Comic Sans MS", 20));
        labelMovimientos.setTextFill(Color.DARKGREEN);
        labelMovimientos.setAlignment(Pos.CENTER);
        labelMovimientos.setStyle("-fx-padding: 10;");

        Button btnReiniciar = new Button("🔄 Reiniciar Juego");
        btnReiniciar.setFont(Font.font("Comic Sans MS", 16));
        btnReiniciar.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #FFD700, #FFA500);
            -fx-text-fill: white;
            -fx-background-radius: 15;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);
        """);

        btnReiniciar.setOnMouseEntered(e -> btnReiniciar.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #FFE55C, #FFB84D);
            -fx-text-fill: white;
            -fx-background-radius: 15;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0, 3, 3);
        """));

        btnReiniciar.setOnMouseExited(e -> btnReiniciar.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #FFD700, #FFA500);
            -fx-text-fill: white;
            -fx-background-radius: 15;
            -fx-padding: 10 20;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 2, 2);
        """));

        HBox panelInferior = new HBox(20);
        panelInferior.setAlignment(Pos.CENTER);
        panelInferior.setPadding(new Insets(10));
        panelInferior.getChildren().addAll(labelMovimientos, btnReiniciar);

        tablero = new GridPane();
        tablero.setAlignment(Pos.CENTER);
        tablero.setHgap(15);
        tablero.setVgap(15);

        root.setTop(titulo);
        BorderPane.setAlignment(titulo, Pos.CENTER);
        root.setCenter(tablero);
        root.setBottom(panelInferior);
        BorderPane.setAlignment(panelInferior, Pos.CENTER);

        controlador = new JuegoControladorMarte(this, contenedorPadre);
        controlador.inicializarJuego();

        btnReiniciar.setOnAction(e -> controlador.reiniciarJuego());
    }

    public BorderPane getRoot() {
        return root;
    }

    public GridPane getTablero() {
        return tablero;
    }

    public void actualizarMovimientos(int movimientos) {
        labelMovimientos.setText("Movimientos: " + movimientos);
    }
}
