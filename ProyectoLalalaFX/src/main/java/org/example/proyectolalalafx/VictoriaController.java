package org.example.proyectolalalafx;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class VictoriaController {
    
    @FXML
    private StackPane rootPane;
    
    @FXML
    private void volverAlMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tierra-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle("Planeta Tierra - Menú");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
