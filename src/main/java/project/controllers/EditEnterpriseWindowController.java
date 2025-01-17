package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Enterprise;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.io.IOException;
import java.util.Objects;

public class EditEnterpriseWindowController {
    private Enterprise current;

    @FXML
    private TextField addressField;

    @FXML
    private Label addressLabel;

    @FXML
    private Button applyBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private TextField emailField;

    @FXML
    private Label emailLabel;

    @FXML
    private Label enteredLabel;

    @FXML
    private TextField nameField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField typeField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label typeLabel;

    @FXML
    void initialize() {
        current = Handler.getCurrentEnterprise();

        setInterface();
        setActions();
    }

    public void setInterface() {
        boolean isEng = Handler.isEng();

        changeLanguageBtn.setText(isEng ? "en" : "ru");

        enteredLabel.setText(current.toString());
        nameLabel.setText(isEng ? "Name of enterprise:" : "Название предприятия:");
        typeLabel.setText(isEng ? "Enterprise type:" : "Тип предприятия:");
        emailLabel.setText(isEng ? "Corporate e-mail:" : "Корпоративная эл. почта:");
        passwordLabel.setText(isEng ? "Password to log in:" : "Пароль для входа:");
        addressLabel.setText(isEng ? "Enterprise address:" : "Должность:");

        nameField.setPromptText(isEng ? "Name" : "Название");
        typeField.setPromptText(isEng ? "Type" : "Тип");
        emailField.setPromptText(isEng ? "E-mail" : "Эл. почта");
        passwordField.setPromptText(isEng ? "Password" : "Пароль");
        addressField.setPromptText(isEng ? "Address" : "Адрес");

        backBtn.setText(isEng ? "Back" : "Обратно");
        applyBtn.setText(isEng ? "Apply" : "Применить");

        nameField.setText(current.getName());
        typeField.setText(current.getType());
        emailField.setText(current.getEmail());
        passwordField.setText(current.getPassword());
        addressField.setText(current.getAddress());
    }

    public void setActions() {
        backBtn.setOnAction(_ -> Handler.changeScene("manageEnterpriseWindow"));
        applyBtn.setOnAction(_ -> edit());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setInterface();
    }

    private void edit() {
        if (!checkFields()) return;

        current.setEmail(emailField.getText());
        current.setName(nameField.getText());
        current.setType(typeField.getText());
        current.setPassword(passwordField.getText());
        current.setAddress(addressField.getText());
        Session session = HibernateUtility.getCurrentSession();
        session.beginTransaction();
        session.update(current);
        session.getTransaction().commit();

        Handler.changeScene("manageEnterpriseWindow");
    }

    private boolean checkFields() {
        boolean isEng = Handler.isEng();
        try {
            if (nameField.getText().length() > 30) {
                throw new IOException(isEng ? "Incorrect name.\nThe name must be lesser than 30 chars." : "Неправильное название.\nНазвание должно быть меньше 30 символов");
            }
            if (nameField.getText().isEmpty() | emailField.getText().isEmpty() | typeField.getText().isEmpty() | passwordField.getText().isEmpty()) {
                throw new IOException(isEng ? "The field(s) is empty.\nEnter values in the empty field(s) and try again." : "Поле(поля) пустое.\nВведите данные в пустые поля и попробуйте еще раз.");
            }
            if (emailField.getText().length() > 256) {
                throw new IOException(
                        isEng ? "Incorrect email.\nThe email must be lesser than 256 chars." :
                                "Неправильная почта.\nПочта должна быть меньше 256 символов.");
            }
            if (!Handler.parseMail(emailField.getText())) {
                return false;
            }
            if (typeField.getText().length() > 30) {
                throw new IOException(isEng ? "Incorrect type\nType must be lesser than 30 chars."
                        : "Неправильный тип.\nТип должен быть меньше 30 символов.");
            }
            if (passwordField.getText().length() > 64) {
                throw new IOException(isEng ? "Incorrect password\nPassword must be lesser than 64 chars." :
                        "Неправильный пароль.\nПароль должен быть меньше 64 символов");
            }
        } catch (IOException e) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING" : "НЕПРАВИЛЬНЫЙ ВВОД",
                    e.getMessage());
            return false;
        }
        return true;
    }
}
