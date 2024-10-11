package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InfoWindowController {

    @FXML
    private Button closeWindowBtn;

    @FXML
    void initialize() {
        closeWindowBtn.setOnAction(_ -> closeWindow());
    }

    private void closeWindow () {
        closeWindowBtn.getScene().getWindow().hide();
    }
}
