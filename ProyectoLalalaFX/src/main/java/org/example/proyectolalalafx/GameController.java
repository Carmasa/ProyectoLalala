package org.example.proyectolalalafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class GameController {

    @FXML
    private void goBack(ActionEvent event) {
        try {
            Parent mainView = FXMLLoader.load(getClass().getResource("main-view.fxml"));
            Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();
            currentScene.setRoot(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}