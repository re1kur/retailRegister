package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.entity.MeasureUnitPane;

public class unitsMeasurementPaneController {

    @FXML
    private Label idValueLabel;

    @FXML
    private Label nameValueLabel;

    @FXML
    private Label symbolValueLabel;

    @FXML
    void initialize() {
        idValueLabel.setText(String.valueOf(MeasureUnitPane.getMeasureUnit().getId()));
        nameValueLabel.setText(MeasureUnitPane.getMeasureUnit().getName());
        symbolValueLabel.setText(MeasureUnitPane.getMeasureUnit().getSymbol());
    }

}
