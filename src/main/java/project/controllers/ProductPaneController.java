package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.entity.ProductPane;

public class ProductPaneController {
    @FXML
    private Label categoryValueLabel;


    @FXML
    private Label idValueLabel;


    @FXML
    private Label nameValueLabel;

    @FXML
    private Label numberValueLabel;

    @FXML
    void initialize() {
        idValueLabel.setText(String.valueOf(ProductPane.getProduct().getId()));
        nameValueLabel.setText(ProductPane.getProduct().getName());
        categoryValueLabel.setText(ProductPane.getProduct().getCategory());
        numberValueLabel.setText(ProductPane.getProduct().getNumber().toString());

    }
}
