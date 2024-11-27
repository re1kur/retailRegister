package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import project.entity.Category;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.io.IOException;

public class EditCategoryWindowController {

    @FXML
    private Button backBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Label label;

    @FXML
    private Label label2;

    @FXML
    private TextField nameField;

    @FXML
    private Label pastNameLabel;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    void initialize() {
        setLanguageInterface();
        backBtn.setOnAction(_ -> Handler.changeScene("categoriesWindow"));
        editBtn.setOnAction(_ -> edit());
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        label.setText(isEng ? "Enter new name of category ↓" : "Введите новое название категории ↓");
        nameField.setText(Handler.getEnteredCategory().getName());
        editBtn.setText(isEng ? "Edit" : "Редактировать");
        backBtn.setText(isEng ? "Categories" : "Категории");
        label2.setText(isEng ? "Past name:" : "Прошлое название:");
        pastNameLabel.setText(Handler.getEnteredCategory().getName());
        changeLanguageBtn.setText(isEng ? "en" : "ru");
    }

    private void edit() {
        boolean isEng = Handler.isEng();
        if (!checkFields(isEng)) return;
        String name = nameField.getText();
        Category category = Category.builder()
                .id(Handler.getEnteredCategory().getId())
                .name(name)
                .enterprise(Handler.getCurrentEnterprise())
                .build();
        try {
            Session session = HibernateUtility.getCurrentSession();
            session.beginTransaction();
            session.merge(category);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            Handler.openErrorAlert(isEng ? "SOMETHING WENT WRONG" : "ЧТО-ТО ПОШЛО НЕ ТАК",
                    isEng ? "Transaction is rolled back." : "Транзакция отменена.");
            HibernateUtility.getCurrentSession().getTransaction().rollback();
        }
        Handler.setEnteredCategory(null);
        Handler.changeScene("categoriesWindow");
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

}
