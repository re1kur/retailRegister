package project.controllers;

import jakarta.persistence.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Enterprise;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

public class LogInController {
    private Enterprise enterprise;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button continueBtn;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button signUpBtn;

    @FXML
    void initialize() {
        HibernateUtility.getCurrentSession();
        setLanguageInterface();
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        continueBtn.setOnAction(_ -> continueAction());
        signUpBtn.setOnAction(_ -> Handler.changeScene("signUpWindow"));
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
    }

    private void continueAction() {
        if (!checkMailPassword()) {
            return;
        }
        Handler.setCurrentEnterprise(enterprise);
        Handler.changeScene("mainWindow");
    }

    private boolean checkMailPassword() {
        try {
            String email = emailTextField.getText();
            String password = passwordField.getText();
            Session session = HibernateUtility.getCurrentSession();
            Query query = session.createQuery(
                    "from Enterprise where email = :email and password = :password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            enterprise = (Enterprise) query.getSingleResult();
        } catch (NoResultException _) {
            Handler.openErrorAlert(
                    Handler.isEng() ? "WRONG PASSWORD OR EMAIL": "НЕПРАВИЛЬНЫЙ ПАРОЛЬ ИЛИ ПОЧТА",
                    Handler.isEng() ? "Please enter valid data." : "Пожалуйста, введите верные данные");
            return false;
        }
        return true;
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface () {
        boolean isEng = Handler.isEng();
        changeLanguageBtn.setText(isEng ? "en": "ru");
        continueBtn.setText(isEng ? "Log in": "Войти");
        emailTextField.setPromptText(isEng ? "email_of_enterprise": "email_предприятия");
        passwordField.setPromptText(isEng ? "password_of_enterprise": "пароль_предприятия");
        signUpBtn.setText(isEng ? "Sign up": "Регистрация");
        welcomeLabel.setText(isEng ? "WELCOME" : "ДОБРО ПОЖАЛОВАТЬ");
    }
}