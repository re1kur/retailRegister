package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Enterprise;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpWindowController {

    @FXML
    private TextField emailField;

    @FXML
    private Button backBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpBtn;

    @FXML
    private PasswordField tryPasswordField;

    @FXML
    private TextField typeField;

    @FXML
    void initialize() {
        setLanguageInterface();
        backBtn.setOnAction(_ -> Handler.changeScene("logInWindow"));
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        signUpBtn.setOnAction(_ -> signUpEnterprise());
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        signUpBtn.setText(isEng ? "Sign up" : "Регистрация");
        backBtn.setText(isEng ? "Back" : "Обратно");
        nameField.setPromptText(isEng ? "Enterprise name" : "Название предприятия");
        emailField.setPromptText(isEng ? "Enterprise corporate email" : "Корп. адрес предприятия");
        typeField.setPromptText(isEng ? "Enterprise type" : "Тип предприятия");
        passwordField.setPromptText(isEng ? "Password" : "Пароль");
        tryPasswordField.setPromptText(isEng ? "Password again" : "Ещё раз пароль");
    }

    private void signUpEnterprise() {
        boolean isEng = Handler.isEng();
        if (!checkFields()) return;
        System.out.println("It is correct.");
        Enterprise enterprise = Enterprise.builder().name(nameField.getText()).type(typeField.getText()).email(emailField.getText()).password(passwordField.getText()).build();
        Session session = HibernateUtility.getCurrentSession();
        session.getTransaction().begin();
        session.persist(enterprise);
        session.getTransaction().commit();
        if (checkIsAdded(session, enterprise)) {
            Handler.openInfoAlert(isEng ? "SUCCESSFULLY" : "УСПЕШНО", isEng ? "The enterprise is registered.\n" + "Log in" : "Предприятие зарегистрировано.\n" + "Авторизуйтесь.");
            Handler.changeScene("logInWindow");
            return;
        }
        Handler.openErrorAlert(isEng ? "SOMETHING WRONG" : "ЧТО-ТО НЕ ТАК", isEng ? "Enterprise is not registered." : "Предприятие не зарегистрировалось.");
    }

    private boolean checkIsAdded(Session session, Enterprise enterprise) {
        Enterprise clone = session.find(Enterprise.class, enterprise.getId());
        return clone.equals(enterprise);
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
            if (!parseMail(emailField.getText())) {
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
            if (!Objects.equals(passwordField.getText(), tryPasswordField.getText())) {
                throw new IOException(isEng ? "Passwords is don't match.\nTry again." :
                        "Пароли не сходятся\nПопробуйте ещё раз.");
            }
        } catch (IOException e) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING" : "НЕПРАВИЛЬНЫЙ ВВОД",
                    e.getMessage());
            return false;
        }
        return true;
    }

    private boolean parseMail(String mail) {
        Pattern invalidChars = Pattern.compile("[^a-zA-Z0-9@.]");
        Matcher matcher = invalidChars.matcher(mail);
        if (matcher.find()) {
            Handler.openErrorAlert("INVALID EMAIL", "Enter the email without invalid characters.");
            return false;
        }
        Pattern correct = Pattern.compile("^[a-zA-Z0-9.]{1,64}" + "@[a-zA-Z0-9.]{1,126}\\.[a-zA-Z]{2,63}$");
        matcher = correct.matcher(mail);
        if (matcher.matches()) {
            return true;
        } else {
            Handler.openErrorAlert("INVALID EMAIL", "Enter a valid email address.");
            return false;
        }
    }
}
