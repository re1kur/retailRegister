package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private CheckBox categoryCheckBox;

    @FXML
    private Label categoryLabel;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private VBox containerOfGoodsPanes;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    @FXML
    private CheckBox idCheckBox;

    @FXML
    private Label idLabel;

    @FXML
    private CheckBox nameCheckBox;

    @FXML
    private Label nameLabel;

    @FXML
    private CheckBox numberCheckBox;

    @FXML
    private Label numberLabel;

    @FXML
    private CheckBox priceCheckBox;

    @FXML
    private Label priceLabel;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField substringField;

    @FXML
    private Button dropBtn;

    @FXML
    void initialize() {
        fillTheVBox(Handler.getGoods());
        setLanguageInterface();
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        backBtn.setOnAction(_ -> Handler.changeScene("mainWindow"));
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        addBtn.setOnAction(_ -> Handler.changeScene("addGoodsWindow"));
        deleteBtn.setOnAction(_ -> Handler.changeScene("deleteGoodsWindow"));
        dropBtn.setOnAction(_ -> dropSearch());
        searchBtn.setOnAction(_ -> searchGood());
    }

    private void searchGood() {
        boolean isEng = Handler.isEng();
        if (!nameCheckBox.isSelected() && !numberCheckBox.isSelected() && !priceCheckBox.isSelected() && !idCheckBox.isSelected()) {
            Handler.openInfoAlert(isEng ? "SELECT PARAMETERS FOR THE SEARCH" : "ВЫБЕРИТЕ ПАРАМЕТРЫ ДЛЯ ПОИСКА",
                    isEng ? "First select parameters and try to search again" : "Сначала выберите параметры и попробуйте поиск ещё раз.");
            return;
        }
        if (substringField.getText().isEmpty()) {
            Handler.openInfoAlert(isEng ? "THE CRITERIA FOR SEARCH ARE NOT DEFINED" : "КРИТЕРИЙ ПОИСКА НЕ ОПРЕДЕЛЕН",
                    isEng ? "First enter the criteria for search." : "Сначала введите критерий для поиска.");
            return;
        }
        String substring = substringField.getText();
        Session session = HibernateUtility.getCurrentSession();
        String strQuery = "from Goods where enterprise = :enterprise";
        if (nameCheckBox.isSelected()) {
            strQuery += " and name like '%" + substring + "%'";
        }
        Query<Goods> query = session.createQuery(strQuery, Goods.class);
        query.setParameter("enterprise", Handler.getCurrentEnterprise());
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
        idCheckBox.setText(isEng ? "Id" : "Номер");
        categoryCheckBox.setText(isEng ? "Category" : "Категория");
        nameCheckBox.setText(isEng ? "Name" : "Название");
        numberCheckBox.setText(isEng ? "Number" : "Кол-во");
        priceCheckBox.setText(isEng ? "Price" : "Цена");
        dropBtn.setText(isEng ? "Drop" : "Сброс");
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
        dropCheckBoxes();
        fillTheVBox(Handler.getGoods());
    }

    private void dropCheckBoxes() {
        nameCheckBox.setSelected(false);
        numberCheckBox.setSelected(false);
        priceCheckBox.setSelected(false);
        categoryCheckBox.setSelected(false);
    }
}
