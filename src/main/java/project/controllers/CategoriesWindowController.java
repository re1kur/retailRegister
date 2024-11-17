package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.entity.Category;
import project.entity.CategoryPane;
import project.handlers.Handler;

import java.util.List;

public class CategoriesWindowController {

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
    private VBox containerOfCategoryPanes;

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
    private Label numberLabel;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField substringField;

    @FXML
    void initialize() {
        setLanguageInterface();
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        dropBtn.setOnAction(_ -> dropSearch());
        backBtn.setOnAction(_ -> Handler.changeScene("mainWindow"));
        addBtn.setOnAction(_ -> {
            Handler.openModalWindow("addCategoryWindow");
            fillTheVBox(Handler.getCategories());
        });
        deleteBtn.setOnAction(_ -> {
            Handler.openModalWindow("deleteCategoryWindow");
            fillTheVBox(Handler.getCategories());
        });
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        fillTheVBox(Handler.getCategories());
        addBtn.setText(isEng ? "Add" : "Добавить");
        deleteBtn.setText(isEng ? "Delete" : "Удалить");
        editBtn.setText(isEng ? "Edit" : "Изменить");
        searchBtn.setText(isEng ? "Search" : "Поиск");
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        backBtn.setText(isEng ? "Menu" : "Меню");
        idLabel.setText(isEng ? "Id" : "Номер");
        nameLabel.setText(isEng ? "Name" : "Название");
        numberLabel.setText(isEng ? "Number" : "Количество");
        criteriaChooseBox.getSelectionModel().clearSelection();
        criteriaChooseBox.getItems().clear();
        criteriaChooseBox.getItems().addAll(isEng ? "Id" : "Номер",
                isEng ? "Name" : "Название",
                isEng ? "Number" : "Количество");
        dropBtn.setText(isEng ? "Drop" : "Сброс");
        labelCriteria.setText(isEng ? "<- Select\nthe criteria"
                : "<- Выберите\nкритерий");
    }

    private void fillTheVBox(List<Category> categories) {
        containerOfCategoryPanes.getChildren().clear();
        if (categories.isEmpty()) {
            System.out.println("No products found");
            return;
        }
        anchorPaneContainer.setPrefHeight(22 + categories.size() * 22);
        containerOfCategoryPanes.setPrefHeight(22 + categories.size() * 22);
        for (Category category : categories) {
            containerOfCategoryPanes.
                    getChildren().add(new CategoryPane(category));
        }
    }

    public void dropSearch() {
        criteriaChooseBox.getSelectionModel().clearSelection();
        fillTheVBox(Handler.getCategories());
    }
}
