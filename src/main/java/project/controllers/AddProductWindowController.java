package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Product;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.io.IOException;

public class AddProductWindowController {

    @FXML
    private Button addBtn;

    @FXML
    private Button backBtn;

    @FXML
    private TextField categoryField;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField numberField;

    @FXML
    void initialize() {
        setLanguageInterface();
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        addBtn.setOnAction(_ -> addProduct());
        backBtn.setOnAction(_ -> Handler.changeScene("productsWindow"));
    }

    private boolean checkFields(boolean isEng) {
        try {
            String name = nameField.getText();
            String category = categoryField.getText();
            if (name.isEmpty()) throw new IOException(isEng ? "Name field is empty." :
                    "Поле названия пустое.");
            if (category.isEmpty()) throw new IOException(isEng ? "Category field is empty." :
                    "Поле категории пустое.");
            if (name.length() > 40) throw new IOException(isEng ? "Name field is too long" :
                    "Поле имени слишком длинное.");
            if (category.length() > 40) throw new IOException(isEng ? "Category field is too long" :
                    "Поле категории слишком длинное.");
            Integer number = Integer.parseInt(numberField.getText());
            if (number < 0) throw new IOException(isEng ? "Number field is negative." :
                    "Поле количества отрицательное.");
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

    private void addProduct() {
        boolean isEng = Handler.isEng();
        if (!checkFields(isEng)) return;
        String category = categoryField.getText();
        String name = nameField.getText();
        Integer number = Integer.parseInt(numberField.getText());
        Product product = Product.builder()
                .name(name)
                .category(category)
                .number(number)
                .enterprise(Handler.getCurrentEnterprise())
                .build();
        Session session = HibernateUtility.getCurrentSession();
        session.beginTransaction();
        session.persist(product);
        session.getTransaction().commit();
        boolean isAdded = checkIsAdded(session, product);
        if (isEng) Handler.openInfoAlert(isAdded ? "SUCCESSFULLY" : "SOMETHING WENT WRONG",
                isAdded ? "Product has been added successfully." : "Product has not been added.");
        if (!isEng) Handler.openInfoAlert(isAdded ? "УСПЕШНО" : "ЧТО-ТО ПОШЛО НЕ ТАК",
                isAdded ? "Продукт успешно добавлен." : "Продукт не был добавлен.");
    }

    private boolean checkIsAdded(Session session, Product product) {
        Product clone = session.find(Product.class, product.getId());
        return clone.equals(product);
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        backBtn.setText(isEng ? "Products" : "Товары");
        addBtn.setText(isEng ? "Add" : "Добавить");
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        nameField.setPromptText(isEng ? "Product name" : "Название товара");
        categoryField.setPromptText(isEng ? "Product category" : "Категория товара");
        numberField.setPromptText(isEng ? "Number of products" : "Количество товара");
    }

}
