package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.MeasureUnit;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.io.IOException;

public class AddMeasureUnitWindowController {

    @FXML
    private Button addBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Label label;

    @FXML
    private Label label1;

    @FXML
    private TextField nameField;

    @FXML
    private TextField symbolField;

    @FXML
    void initialize() {
        setLanguageInterface();
        addBtn.setOnAction(_ -> addCategory());
        closeWindowBtn.setOnAction(_ -> nameField.getScene().getWindow().hide());
    }

    private boolean checkFields(boolean isEng) {
        try {
            String name = nameField.getText();
            String symbol = symbolField.getText();
            if (name.isEmpty()) throw new IOException(isEng ? "Name field is empty." :
                    "Поле названия пустое.");
            if (name.length() > 30) throw new IOException(isEng ? "Name field is too long" :
                    "Поле имени слишком длинное.");
            if (symbol.isEmpty()) throw new IOException(isEng ? "Symbol field is empty." :
                    "Поле обозначения пустое.");
            if (symbol.length() > 5) throw new IOException(isEng ? "Symbol field is too long." +
                    "\nSymbol must be less than 5 letters." :
                    "Поле обозначения слишком длинное.");
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
        MeasureUnit measureUnit = MeasureUnit.builder()
                .name(nameField.getText())
                .symbol(symbolField.getText())
                .enterprise(Handler.getCurrentEnterprise())
                .build();
        Session session = HibernateUtility.getCurrentSession();
        session.beginTransaction();
        session.persist(measureUnit);
        session.getTransaction().commit();
        boolean isAdded = checkIsAdded(session, measureUnit);
        if (isEng) Handler.openInfoAlert(isAdded ? "SUCCESSFULLY" : "SOMETHING WENT WRONG",
                isAdded ? "Measure unit has been added successfully." : "Measure unit has not been added.");
        if (!isEng) Handler.openInfoAlert(isAdded ? "УСПЕШНО" : "ЧТО-ТО ПОШЛО НЕ ТАК",
                isAdded ? "Единица измерения успешно добавлена." : "Единица измерения не была добавлена.");
    }

    private boolean checkIsAdded(Session session, MeasureUnit measureUnit) {
        MeasureUnit clone = session.find(MeasureUnit.class, measureUnit.getId());
        return clone.equals(measureUnit);
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        addBtn.setText(isEng ? "Add" : "Добавить");
        label.setText(isEng ? "Enter the name ↓" : "Введите название ↓");
        label1.setText(isEng ? "Enter the symbol ↓" : "Введите обозначение ↓");

    }

}
