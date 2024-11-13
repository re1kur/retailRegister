package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.entity.MeasureUnit;
import project.entity.MeasureUnitPane;
import project.handlers.Handler;

import java.util.List;

public class unitsMeasurementWindowController {

    @FXML
    private Button addBtn;

    @FXML
    private AnchorPane anchorPaneContainer;

    @FXML
    private Button backBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private VBox containerOfUnitPanes;

    @FXML
    private ChoiceBox<?> criteriaChooseBox;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button dropBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Label idLabel;

    @FXML
    private Label labelCriteria;

    @FXML
    private Label nameLabel;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField substringField;

    @FXML
    private Label symbolLabel;

    @FXML
    void initialize() {
        setLanguageInterface();
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        addBtn.setOnAction(_ -> {
            Handler.openModalWindow("addMeasureUnitWindow");
            fillTheVBox(Handler.getUnitsMeasurement());
        });
        backBtn.setOnAction(_ -> Handler.changeScene("mainWindow"));
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        fillTheVBox(Handler.getUnitsMeasurement());
        addBtn.setText(isEng ? "Add" : "Добавить");
        deleteBtn.setText(isEng ? "Delete" : "Удалить");
        editBtn.setText(isEng ? "Edit" : "Изменить");
        searchBtn.setText(isEng ? "Search" : "Поиск");
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        backBtn.setText(isEng ? "Menu" : "Меню");
        idLabel.setText(isEng ? "Id" : "Номер");
        nameLabel.setText(isEng ? "Name" : "Название");
        symbolLabel.setText(isEng ? "Symbol" : "Обозначение");
    }

    private void fillTheVBox(List<MeasureUnit> unitsMeasurement) {
        containerOfUnitPanes.getChildren().clear();
        if (unitsMeasurement.isEmpty()) {
            System.out.println("No products found");
            return;
        }
        anchorPaneContainer.setPrefHeight(22 + unitsMeasurement.size() * 22);
        containerOfUnitPanes.setPrefHeight(22 + unitsMeasurement.size() * 22);
        for (MeasureUnit measureUnit : unitsMeasurement) {
            containerOfUnitPanes.
                    getChildren().add(new MeasureUnitPane(measureUnit));
        }
    }
}
