package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Enterprise;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

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
        if (!Handler.isEng()) {
            setRussianGUI();
        }
        backBtn.setOnAction(_ -> Handler.changeScene("logInWindow"));
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        signUpBtn.setOnAction(_ -> signUpEnterprise());
    }

    private void setRussianGUI() {
        changeLanguageBtn.setText("ru");
        signUpBtn.setText("зарег-ть");
        backBtn.setText("обратно");
        nameField.setPromptText("Название предприятия");
        emailField.setPromptText("Корпоративный адрес предприятия");
        typeField.setPromptText("Тип предприятия");
        passwordField.setPromptText("Пароль для входа позднее");
        tryPasswordField.setPromptText("Введите пароль снова");
    }

    private void changeLanguage () {
        Handler.setEng(!Handler.isEng());
        if (Handler.isEng()) {
            changeLanguageBtn.setText("en");
            signUpBtn.setText("sign up");
            backBtn.setText("back");
            nameField.setPromptText("Enterprise name");
            emailField.setPromptText("Enterprise corporate email");
            typeField.setPromptText("Enterprise type");
            passwordField.setPromptText("Password to log in later");
            tryPasswordField.setPromptText("Enter password again");

        } else {
            setRussianGUI();
        }
    }

    private void signUpEnterprise() {
        if (!checkFields()) return;
        System.out.println("It is correct.");
        Enterprise enterprise = Enterprise.builder()
                .name(nameField.getText())
                .type(typeField.getText())
                .email(emailField.getText())
                .password(passwordField.getText())
                .build();
        Session session = HibernateUtility.getCurrentSession();
        session.getTransaction().begin();
        session.persist(enterprise);
        session.getTransaction().commit();
        if (checkIsAdded(session, enterprise)) {
            Handler.openInfoAlert(
                    "COMPLETE",
                    "The enterprise is registered.\n" +
                            "Log in");
            Handler.changeScene("logInWindow");
            return;
        }
        Handler.openErrorAlert(
                "SOMETHING WRONG",
                "Enterprise is not registered.");
    }

    private boolean checkIsAdded(Session session, Enterprise enterprise) {
        Enterprise clone = session.find(
                Enterprise.class,
                enterprise.getId());
        return clone.equals(enterprise);
    }

    private boolean checkFields() {
        if (nameField.getText().length() > 30) {
            Handler.openErrorAlert("LONG NAME",
                    "The length of name is long." +
                            " The name must be lesser than 30 chars.");
            return false;
        }
        if (nameField.getText().isEmpty() | emailField.getText().isEmpty() |
                typeField.getText().isEmpty() | passwordField.getText().isEmpty()) {
            Handler.openErrorAlert("THE TEXT FIELD(S) IS EMPTY.",
                    "Fill in the text field(s) and try again.");
            return false;
        }
        if (emailField.getText().length() > 256) {
            Handler.openErrorAlert("LONG EMAIL",
                    "The length of email is long." +
                            " The email must be lesser than 256 chars.");
            return false;
        }
        if (!parseMail(emailField.getText())) {
            return false;
        }
        if (typeField.getText().length() > 30) {
            Handler.openErrorAlert("LONG TYPE",
                    "The length of type is long." +
                            " The type must be lesser than 30 chars.");
            return false;
        }
        if (passwordField.getText().length() > 64) {
            Handler.openErrorAlert("LONG PASSWORD",
                    "The length of password is long." +
                            " The password must be lesser than 64 chars.");
            return false;
        }
        if (!Objects.equals(passwordField.getText(), tryPasswordField.getText())) {
            Handler.openInfoAlert("PASSWORDS DO NOT MATCH",
                    "Passwords do not match. Try again.");
            return false;
        }
        return true;
    }

    private boolean parseMail (String mail) {
        Pattern invalidChars = Pattern.compile("[^a-zA-Z0-9@.]");
        Matcher matcher = invalidChars.matcher(mail);
        if (matcher.find()) {
            Handler.openErrorAlert("INVALID EMAIL",
                    "Enter the email without invalid characters.");
            return false;
        }
        Pattern correct = Pattern.compile(
                "^[a-zA-Z0-9.]{1,64}" +
                        "@[a-zA-Z0-9.]{1,126}\\.[a-zA-Z]{2,63}$");
        matcher = correct.matcher(mail);
        if (matcher.matches()) {
            return true;
        } else {
            Handler.openErrorAlert("INVALID EMAIL",
                    "Enter a valid email address.");
            return false;
        }
    }
}
