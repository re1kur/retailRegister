package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.hibernate.Session;
import org.hibernate.query.Query;
import project.entity.Goods;
import project.entity.GoodsPane;
import project.handlers.Handler;
import project.handlers.HibernateUtility;
import java.util.List;

public class GoodsWindowController {

    @FXML
    private Button addBtn;

    @FXML
    private AnchorPane anchorPaneContainer;

    @FXML
    private Button backBtn;

    @FXML
    private Label categoryLabel;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private VBox containerOfGoodsPanes;

    @FXML
    private ChoiceBox<String> criteriaChooseBox;

    @FXML
    private ChoiceBox<String> methodBox;

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
    private Label priceLabel;

    @FXML
    private Button searchBtn;

    @FXML
    private AnchorPane searchPane;

    @FXML
    private TextField substringField;

    @FXML
    void initialize() {
        setLanguageInterface();
        fillTheVBox(Handler.getGoods());
        methodBox.setOnAction(_ -> setMethod());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        backBtn.setOnAction(_ -> Handler.changeScene("mainWindow"));
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        addBtn.setOnAction(_ -> Handler.changeScene("addGoodsWindow"));
        deleteBtn.setOnAction(_ -> Handler.changeScene("deleteGoodsWindow"));
        dropBtn.setOnAction(_ -> dropSearch());
        searchBtn.setOnAction(_ -> searchGoods());
    }
    private void setMethod() {
        if (methodBox.getSelectionModel().getSelectedItem().isEmpty() |
        methodBox.getSelectionModel().getSelectedItem() == null) return;
        searchPane.setVisible(false);
        String method = methodBox.getSelectionModel().getSelectedItem();
        switch (method){
            case "Search":
            case "Поиск":
                searchPane.setVisible(true);
                break;
            case "Sort":
            case "Сортировка":
                break;
            case "Filter":
            case "Фильтровка":
                break;
        }
    }

    private void searchGoods() {
        boolean isInt = false;
        boolean isEng = Handler.isEng();
        if (criteriaChooseBox.getSelectionModel().getSelectedItem() == null ||
                criteriaChooseBox.getSelectionModel().getSelectedItem().isEmpty()) {
            Handler.openInfoAlert(isEng ? "SELECT PARAMETERS FOR THE SEARCH" : "ВЫБЕРИТЕ ПАРАМЕТРЫ ДЛЯ ПОИСКА",
                    isEng ? "First select parameters and try to search again" : "Сначала выберите параметры и попробуйте поиск ещё раз.");
            return;
        }
        if (substringField.getText().isEmpty()) {
            Handler.openInfoAlert(isEng ? "THE CRITERIA FOR SEARCH ARE NOT DEFINED" : "КРИТЕРИЙ ПОИСКА НЕ ОПРЕДЕЛЕН",
                    isEng ? "First enter the criteria for search." : "Сначала введите критерий для поиска.");
            return;
        }
        String criteria = criteriaChooseBox.getSelectionModel().getSelectedItem();
        String substring = substringField.getText();
        Session session = HibernateUtility.getCurrentSession();
        String hql = "from Goods where enterprise = :enterprise and ";
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
            case "Number":
            case "Количество":
                hql += "number = :substring";
                isInt = true;
                break;
            case "Price":
            case "Цена":
                hql += "price = :substring";
                isInt = true;
                break;
            default:
                Handler.openInfoAlert(isEng ? "INVALID CRITERIA SELECTED" : "НЕДОПУСТИМЫЙ КРИТЕРИЙ",
                        isEng ? "Please select valid criteria." : "Пожалуйста, выберите допустимый критерий.");
                return;
        }
        Query<Goods> query = session.createQuery(hql, Goods.class);
        query.setParameter("enterprise", Handler.getCurrentEnterprise());
        if (isInt) {
            try {
                query.setParameter("substring", Integer.valueOf(substring));
            } catch (NumberFormatException e) {
                Handler.openErrorAlert(isEng ? "YOU HAVE ENTERED CHARACTERS INSTEAD OF DIGITS" : "ВЫ ВВЕЛИ СИМВОЛЫ ВМЕСТО ЦИФР",
                        isEng ? "Please, enter digits only." : "Пожалуйста, введите только цифры.");
                return;
            }
        } else {
            query.setParameter("substring", "%" + substring + "%");
        }
        List<Goods> goods = query.list();
        fillTheVBox(goods);
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        addBtn.setText(isEng ? "Add" : "Добавить");
        deleteBtn.setText(isEng ? "Delete" : "Удалить");
        editBtn.setText(isEng ? "Edit" : "Изменить");
        searchBtn.setText(isEng ? "Search" : "Поиск");
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        backBtn.setText(isEng ? "Menu" : "Меню");
        idLabel.setText(isEng ? "Id" : "Номер");
        nameLabel.setText(isEng ? "Name" : "Название");
        numberLabel.setText(isEng ? "Number" : "Количество");
        categoryLabel.setText(isEng ? "Category" : "Категория");
        priceLabel.setText(isEng ? "Price" : "Цена");
        criteriaChooseBox.getSelectionModel().clearSelection();
        criteriaChooseBox.getItems().clear();
        criteriaChooseBox.getItems().addAll(
                isEng ? "Id" : "Номер",
                isEng ? "Name" : "Название",
                isEng ? "Number" : "Количество",
                isEng ? "Price" : "Цена");
        methodBox.getSelectionModel().clearSelection();
        methodBox.getItems().clear();
        methodBox.getItems().addAll(
                isEng ? "Search" : "Поиск",
                isEng ? "Sort" : "Сортировка",
                isEng ? "Filter" : "Фильтровка");
        dropBtn.setText(isEng ? "Drop" : "Сброс");
        labelCriteria.setText(isEng ? "<- Select\nthe criteria"
                : "<- Выберите\nкритерий");
    }

    private void fillTheVBox(List<Goods> goodsList) {
        containerOfGoodsPanes.getChildren().clear();
        if (goodsList.isEmpty()) {
            System.out.println("No products found");
            return;
        }
        anchorPaneContainer.setPrefHeight(22 + goodsList.size() * 22);
        containerOfGoodsPanes.setPrefHeight(22 + goodsList.size() * 22);
        for (Goods goods : goodsList) {
            containerOfGoodsPanes.
                    getChildren().add(new GoodsPane(goods));
        }
    }

    public void dropSearch() {
        criteriaChooseBox.getSelectionModel().clearSelection();
        fillTheVBox(Handler.getGoods());
    }
}
