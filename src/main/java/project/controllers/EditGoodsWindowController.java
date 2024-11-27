package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import project.entity.Category;
import project.entity.Goods;
import project.entity.MeasureUnit;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.io.IOException;

public class EditGoodsWindowController {

    @FXML
    private Button backBtn;

    @FXML
    private ChoiceBox<Category> categoriesChoiceBox;

    @FXML
    private Label categoryLabel;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Label enteredLabel;

    @FXML
    private Label measUnitLabel;

    @FXML
    private ChoiceBox<MeasureUnit> measureUnitsChoiceBox;

    @FXML
    private TextField nameField;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField numberField;

    @FXML
    private Label numberLabel;

    @FXML
    private TextField priceField;

    @FXML
    private Label priceLabel;

    @FXML
    void initialize() {
        setLanguageInterface();
        setGoodsParameters();
        categoriesChoiceBox.getItems().addAll(Handler.getCategories());
        measureUnitsChoiceBox.getItems().addAll(Handler.getUnitsMeasurement());
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        editBtn.setOnAction(_ -> editGoods());
        backBtn.setOnAction(_ -> Handler.changeScene("goodsWindow"));
    }

    private boolean checkFields(boolean isEng) {
        try {
            String name = nameField.getText();
            if (name.isEmpty()) throw new IOException(isEng ? "Name field is empty." :
                    "Поле названия пустое.");
            if (name.length() > 40) throw new IOException(isEng ? "Name field is too long" :
                    "Поле имени слишком длинное.");
            int number = Integer.parseInt(numberField.getText());
            int price = Integer.parseInt(priceField.getText());
            if (number < 0) throw new IOException(isEng ? "Number field is negative." :
                    "Поле количества отрицательное.");
            if (price < 0) throw new IOException(isEng ? "Price field is negative." :
                    "Поле цены отрицательное.");
        } catch (NumberFormatException _) {
            Handler.openErrorAlert(isEng ? "INVALID NUMBER" : "НЕВЕРНОЕ КОЛИЧЕСТВО",
                    isEng ? "Please, enter a valid number.\n" +
                            "(without symbols, only digits)" :
                            "Пожалуйста, введите правильное количество.\n" +
                                    "(без символов, только числа)");
            return false;
        } catch (IOException e) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING OF VALUES"
                            : "НЕПРАВИЛЬНЫЙ ВВОД ДАННЫХ",
                    e.getMessage());
            return false;
        }
        return true;
    }

    private void editGoods() {
        boolean isEng = Handler.isEng();
        if (!checkFields(isEng)) return;
        MeasureUnit measureUnit = measureUnitsChoiceBox.getSelectionModel().getSelectedItem();
        Category category = categoriesChoiceBox.getSelectionModel().getSelectedItem();
        String name = nameField.getText();
        Integer number = Integer.parseInt(numberField.getText());
        Integer price = Integer.parseInt(priceField.getText());
        Goods goods = Goods.builder()
                .id(Handler.getEnteredGoods().getId())
                .name(name)
                .category(category)
                .number(number)
                .measureUnit(measureUnit)
                .price(price)
                .enterprise(Handler.getCurrentEnterprise())
                .build();
        try {
            Session session = HibernateUtility.getCurrentSession();
            session.beginTransaction();
            session.merge(goods);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            Handler.openErrorAlert(isEng ? "SOMETHING WENT WRONG" : "ЧТО-ТО ПОШЛО НЕ ТАК",
                    isEng ? "Transaction is rolled back." : "Транзакция отменена.");
            HibernateUtility.getCurrentSession().getTransaction().rollback();
            return;
        }
        Handler.setEnteredGoods(null);
        Handler.changeScene("goodsWindow");
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        backBtn.setText(isEng ? "Goods" : "Товары");
        editBtn.setText(isEng ? "Edit" : "Редактировать");
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        nameField.setPromptText(isEng ? "Product name" : "Название товара");
        numberField.setPromptText(isEng ? "Number of products" : "Количество товара");
        categoryLabel.setText(isEng ? "Select the category ↓" : "Выберите категорию ↓");
        measUnitLabel.setText(isEng ? "Select the measure unit ↓" : "Выберите ед.измерения ↓");
        nameLabel.setText(isEng ? "Enter the name of goods:" : "Введите название товара:");
        numberLabel.setText(isEng ? "Enter the number of goods:" : "Введите количество товаров:");
        priceLabel.setText(isEng ? "Enter the price of one goods:" : "Введите цену одного товара:");
        nameField.setPromptText(isEng ? "Name of goods" : "Название товара");
        numberField.setPromptText(isEng ? "Number of goods" : "Количество товара");
        enteredLabel.setText(Handler.getEnteredGoods().toString());
        priceField.setPromptText(isEng ? "Price of one goods" : "Цена одного товара");
    }
    private void setGoodsParameters () {
        Goods entered = Handler.getEnteredGoods();
        nameField.setText(entered.getName());
        numberField.setText(entered.getNumber().toString());
        priceField.setText(entered.getPrice().toString());
        categoriesChoiceBox.getSelectionModel().select(entered.getCategory());
        measureUnitsChoiceBox.getSelectionModel().select(entered.getMeasureUnit());
    }
}
