package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.handlers.Handler;
import project.other.CategoryPane;

public class CategoryPaneController {

    @FXML
    private Label idValueLabel;

    @FXML
    private Label nameValueLabel;

    @FXML
    private Label numberValueLabel;

    @FXML
    void initialize() {
        idValueLabel.setText(String.valueOf(CategoryPane.getCategory().getId()));
        nameValueLabel.setText(CategoryPane.getCategory().getName());
        numberValueLabel.setText(Handler.getCategoryNumber(CategoryPane.getCategory()));
    }
}
