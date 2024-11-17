package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Category;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.io.IOException;

public class AddCategoryWindowController {

    @FXML
    private Label label;

    @FXML
    private Button addBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private TextField nameField;

    @FXML
    void initialize() {
        setLanguageInterface();
        addBtn.setOnAction(_ -> addCategory());
        closeWindowBtn.setOnAction(_ -> nameField.getScene().getWindow().hide());
    }
    private boolean checkFields(boolean isEng) {
        try {
            String name = nameField.getText();
            if (name.isEmpty()) throw new IOException(isEng ? "Name field is empty." :
                    "Поле названия пустое.");
            if (name.length() > 40) throw new IOException(isEng ? "Name field is too long" :
                    "Поле имени слишком длинное.");
        } catch (IOException e) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING OF VALUES"
                            : "НЕПРАВИЛЬНЫЙ ВВОД ДАННЫХ",
                    e.getMessage());
            return false;
        }
        return true;
    }

    private void addCategory() {
        boolean isEng = Handler.isEng();
        if (!checkFields(isEng)) return;
        String name = nameField.getText();
        Category category = Category.builder()
                .name(name)
                .enterprise(Handler.getCurrentEnterprise())
                .build();
        Session session = HibernateUtility.getCurrentSession();
        session.beginTransaction();
        session.persist(category);
        session.getTransaction().commit();
        boolean isAdded = checkIsAdded(session, category);
        if (isEng) Handler.openInfoAlert(isAdded ? "SUCCESSFULLY" : "SOMETHING WENT WRONG",
                isAdded ? "Category has been added successfully." : "Category has not been added.");
        if (!isEng) Handler.openInfoAlert(isAdded ? "УСПЕШНО" : "ЧТО-ТО ПОШЛО НЕ ТАК",
                isAdded ? "Категория успешно добавлена." : "Категория не была добавлена.");
    }

    private boolean checkIsAdded(Session session, Category category) {
        Category clone = session.find(Category.class, category.getId());
        return clone.equals(category);
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        addBtn.setText(isEng ? "Add" : "Добавить");
        label.setText(isEng ? "Enter the name of category ↓" : "Введите название категории ↓");
    }
}
