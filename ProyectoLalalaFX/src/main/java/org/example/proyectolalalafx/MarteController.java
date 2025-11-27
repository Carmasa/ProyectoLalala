package org.example.proyectolalalafx;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.proyectolalalafx.marte.GestorSonidosMarte;
import org.example.proyectolalalafx.marte.JuegoVistaMarte;

public class MarteController {

    @FXML
    private StackPane contenedorJuego;

    @FXML
    public void initialize() {
        // Detener música del menú principal
        MusicManager.stopMusic();
        
        // Inicializar el gestor de sonidos de Marte
        GestorSonidosMarte gestorSonidos = GestorSonidosMarte.obtenerInstancia();
        gestorSonidos.inicializar();
        gestorSonidos.iniciarMusicaDeFondo();

        // Crear la vista del juego de memoria
        JuegoVistaMarte vistaJuego = new JuegoVistaMarte(contenedorJuego);
        
        // Agregar el juego al contenedor (posición 0 para que esté debajo de la luna)
        contenedorJuego.getChildren().add(0, vistaJuego.getRoot());
    }
    
    @FXML
    private void volverAlMenu() {
        // Detener y liberar sonidos de Marte
        GestorSonidosMarte gestorSonidos = GestorSonidosMarte.obtenerInstancia();
        gestorSonidos.liberarRecursos();
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Scene scene = new Scene(loader.load(), 1920, 1080);
            Stage stage = (Stage) contenedorJuego.getScene().getWindow();
            stage.setTitle("Sistema Solar - Juegos");
            stage.setScene(scene);
            // La música del menú principal se iniciará automáticamente en MainController.initialize()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
