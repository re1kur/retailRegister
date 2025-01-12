package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.hibernate.Session;
import org.hibernate.query.Query;
import project.entity.Category;
import project.entity.MeasureUnit;
import project.handlers.HibernateUtility;
import project.other.MeasureUnitPane;
import project.handlers.Handler;

import java.util.List;

public class UnitsMeasurementWindowController {

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
    private ChoiceBox<String> criteriaChooseBox;

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
        deleteBtn.setOnAction(_ -> {
            Handler.openModalWindow("deleteMeasureUnitWindow");
            fillTheVBox(Handler.getUnitsMeasurement());
        });
        editBtn.setOnAction(_ -> {
            Handler.openModalWindow("enterMeasureUnitToEditWindow");
            fillTheVBox(Handler.getUnitsMeasurement());
        });
        searchBtn.setOnAction(_ -> searchUnits());
        dropBtn.setOnAction(_ -> dropSearch());
    }

    public void dropSearch() {
        criteriaChooseBox.getSelectionModel().clearSelection();
        fillTheVBox(Handler.getUnitsMeasurement());
    }

    private void searchUnits() {
        boolean isInt = false;
        boolean isEng = Handler.isEng();
        if (criteriaChooseBox.getSelectionModel().getSelectedItem() == null || criteriaChooseBox.getSelectionModel().getSelectedItem().isEmpty()) {
            Handler.openInfoAlert(isEng ? "SELECT PARAMETERS FOR THE SEARCH" : "ВЫБЕРИТЕ ПАРАМЕТРЫ ДЛЯ ПОИСКА", isEng ? "First select parameters and try to search again" : "Сначала выберите параметры и попробуйте поиск ещё раз.");
            return;
        }
        if (substringField.getText().isEmpty()) {
            Handler.openInfoAlert(isEng ? "THE CRITERIA FOR SEARCH ARE NOT DEFINED" : "КРИТЕРИЙ ПОИСКА НЕ ОПРЕДЕЛЕН", isEng ? "First enter the criteria for search." : "Сначала введите критерий для поиска.");
            return;
        }
        String criteria = criteriaChooseBox.getSelectionModel().getSelectedItem();
        String substring = substringField.getText();
        Session session = HibernateUtility.getCurrentSession();
        String hql = "from MeasureUnit where enterprise = :enterprise and ";
        switch (criteria) {
            case "Name":
            case "Название":
                hql += "name like :substring";
                break;
            case "Id":
            case "Номер":
                hql += "id = :substring";
                isInt = true;
                break;
            case "Symbol":
            case "Обозначение":
                hql += "symbol like :substring";
                break;
            default:
                Handler.openInfoAlert(isEng ? "INVALID CRITERIA SELECTED" : "НЕДОПУСТИМЫЙ КРИТЕРИЙ", isEng ? "Please select valid criteria." : "Пожалуйста, выберите допустимый критерий.");
                return;
        }
        Query<MeasureUnit> query = session.createQuery(hql, MeasureUnit.class);
        query.setParameter("enterprise", Handler.getCurrentEnterprise());
        if (isInt) {
            try {
                query.setParameter("substring", Integer.valueOf(substring));
            } catch (NumberFormatException e) {
                Handler.openErrorAlert(isEng ? "YOU HAVE ENTERED CHARACTERS INSTEAD OF DIGITS" : "ВЫ ВВЕЛИ СИМВОЛЫ ВМЕСТО ЦИФР", isEng ? "Please, enter digits only." : "Пожалуйста, введите только цифры.");
                return;
            }
        } else {
            query.setParameter("substring", "%" + substring + "%");
        }
        List<MeasureUnit> measureUnits = query.list();
        fillTheVBox(measureUnits);
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
        criteriaChooseBox.getSelectionModel().clearSelection();
        criteriaChooseBox.getItems().clear();
        criteriaChooseBox.getItems().addAll(isEng ? "Id" : "Номер",
                isEng ? "Name" : "Название",
                isEng ? "Symbol" : "Обозначение");
        dropBtn.setText(isEng ? "Drop" : "Сброс");
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
