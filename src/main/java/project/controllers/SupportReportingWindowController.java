package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Report;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.io.IOException;

public class SupportReportingWindowController {

    @FXML
    private Button backBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private TextArea contentField;

    @FXML
    private Label contentLabel;

    @FXML
    private Button introduceBtn;

    @FXML
    private TextField subjectField;

    @FXML
    private Button submitBtn;

    @FXML
    void initialize() {
        setInterface();
        setActions();
    }

    private void setInterface() {
        boolean isEng = Handler.isEng();

        subjectField.setPromptText(isEng ? "Report subject" : "Тема отчета");
        contentField.setPromptText(isEng ? "Content" : "Содержание");
        contentLabel.setText(isEng ? "Content of the report" : "Содержание отчета");
        backBtn.setText(isEng ? "Menu" : "Меню");
        submitBtn.setText(isEng ? "Submit" : "Отправить");
        changeLanguageBtn.setText(isEng ? "en" : "ru");
    }

    private void setActions() {
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        submitBtn.setOnAction(_ -> submitReport());
        backBtn.setOnAction(_ -> Handler.changeScene("mainWindow"));
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
    }

    private void submitReport() {
        if (!checkFields()) return;
        Report report = Report.builder()
                .subject(subjectField.getText())
                .content(contentField.getText())
                .build();
        Session session = HibernateUtility.getCurrentSession();
        session.beginTransaction();
        session.save(report);
        session.getTransaction().commit();
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());

        setInterface();
    }

    private boolean checkFields() {
        boolean isEng = Handler.isEng();

        try {
            if (subjectField.getText().isEmpty()) throw new IOException(isEng ? "Subject text field is empty. Enter the subject of report." : "Поле с темой пустое. Введите тему отчета.");
            if (contentField.getText().isEmpty()) throw new IOException(isEng ? "Content text field is empty. Enter the content of report." : "Поле содержания пустое. Введите содержание отчета.");
        } catch (IOException e) {
            Handler.openErrorAlert(isEng ? "Incorrect input" : "Неправильный ввод", e.getMessage());
            return false;
        }
        return true;
    }
}
