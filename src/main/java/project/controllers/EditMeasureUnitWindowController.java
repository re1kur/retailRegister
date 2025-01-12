package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import project.entity.MeasureUnit;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.io.IOException;

public class EditMeasureUnitWindowController {

    @FXML
    private Button backBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button applyBtn;

    @FXML
    private Label label;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private TextField nameField;

    @FXML
    private TextField nameField1;

    @FXML
    private Label pastUnitLabel;

    @FXML
    void initialize() {
        setLanguageInterface();
        backBtn.setOnAction(_ -> Handler.changeScene("unitsMeasurementWindow"));
        applyBtn.setOnAction(_ -> edit());
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        label.setText(isEng ? "Enter new name of measure unit ↓" : "Введите новое название ед.измерения ↓");
        nameField.setText(Handler.getEnteredMeasureUnit().getName());
        nameField1.setText(Handler.getEnteredMeasureUnit().getSymbol());
        applyBtn.setText(isEng ? "Apply" : "Применить");
        backBtn.setText(isEng ? "Measure units" : "Ед.измерения");
        label2.setText(isEng ? "Past data:" : "Прошлые данные:");
        pastUnitLabel.setText(Handler.getUnitsMeasurement().toString());
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        label1.setText(isEng ? "Symbol of measure unit ↓" : "Обозначение единицы измерения ↓");
    }

    private void edit() {
        boolean isEng = Handler.isEng();
        if (!checkFields(isEng)) return;
        String name = nameField.getText();
        String symbol = nameField1.getText();
        MeasureUnit category = MeasureUnit.builder()
                .id(Handler.getEnteredMeasureUnit().getId())
                .name(name)
                .enterprise(Handler.getCurrentEnterprise())
                .symbol(symbol)
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
        Handler.setEnteredMeasureUnit(null);
        Handler.changeScene("unitsMeasurementWindow");
    }

    private boolean checkFields(boolean isEng) {
        try {
            String name = nameField.getText();
            String symbol = nameField1.getText();
            if (name.isEmpty()) throw new IOException(isEng ? "Name field is empty." :
                    "Поле названия пустое.");
            if (name.length() > 30) throw new IOException(isEng ? "Name field is too long" :
                    "Поле имени слишком длинное.");
            if (symbol.isEmpty()) throw new IOException(isEng ? "Symbol field is empty." :
                    "Поле обозначения пустое.");
            if (symbol.length() > 5) throw new IOException(isEng ? "Symbol field is too long" :
                    "Поле обозначения слишком длинное.");
        } catch (IOException e) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING OF VALUES"
                            : "НЕПРАВИЛЬНЫЙ ВВОД ДАННЫХ",
                    e.getMessage());
            return false;
        }
        return true;
    }

}
