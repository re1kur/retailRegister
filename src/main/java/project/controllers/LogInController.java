package project.controllers;

import jakarta.persistence.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Employee;
import project.entity.Enterprise;
import project.handlers.Handler;
import project.handlers.HibernateUtility;
import project.other.ApplicationRights;

public class LogInController {
    private Enterprise enterprise;
    private Employee employee;
    private boolean isEnterpriseLogging = true;

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
    private Button logInEmployeeBtn;

    @FXML
    void initialize() {
        HibernateUtility.getCurrentSession();
        setLanguageInterface();
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        continueBtn.setOnAction(_ -> continueAction());
        signUpBtn.setOnAction(_ -> Handler.changeScene("signUpWindow"));
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        logInEmployeeBtn.setOnAction(_ -> changeLogging());
    }

    private void continueAction() {
        if (!checkMailPassword()) {
            return;
        }
        Handler.setCurrentEnterprise(enterprise);
        if (!isEnterpriseLogging) Handler.setCurrentEmployee(employee);
        Handler.changeScene("mainWindow");
    }

    private boolean checkMailPassword() {
        try {
            StringBuilder queryText = new StringBuilder("from ");
            queryText.append(isEnterpriseLogging ? "Enterprise " : "Employee ");
            queryText.append("where email = :email and password = :password ");
            String email = emailTextField.getText();
            String password = passwordField.getText();

            Session session = HibernateUtility.getCurrentSession();
            Query query = session.createQuery(queryText.toString());
            query.setParameter("email", email);
            query.setParameter("password", password);
            if (isEnterpriseLogging) {
                enterprise = (Enterprise) query.getSingleResult();
                Handler.setIsEnterpriseLogging(true);
            }
            else {
                employee = (Employee) query.getSingleResult();
                enterprise = employee.getEnterprise();
                Handler.setIsEnterpriseLogging(false);
            }
        } catch (NoResultException _) {
            Handler.openErrorAlert(
                    Handler.isEng() ? "WRONG PASSWORD OR EMAIL" : "НЕПРАВИЛЬНЫЙ ПАРОЛЬ ИЛИ ПОЧТА",
                    Handler.isEng() ? "Please enter valid data." : "Пожалуйста, введите верные данные");
            return false;
        }
        return true;
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void changeLogging() {
        isEnterpriseLogging = !isEnterpriseLogging;
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        continueBtn.setText(isEng ? "Log in" : "Войти");
        signUpBtn.setText(isEng ? "Sign up" : "Регистрация");
        welcomeLabel.setText(isEng ? "WELCOME" : "ДОБРО ПОЖАЛОВАТЬ");
        String employeeButtonText;
        String emailFieldText;
        String passwordFieldText;
        if (isEnterpriseLogging) {
            employeeButtonText = isEng ? "To log in as an employee" : "Для входа в качестве сотрудника";
            emailFieldText = isEng ? "email_of_enterprise" : "email_предприятия";
            passwordFieldText = isEng ? "password_of_enterprise" : "пароль_предприятия";
        } else {
            employeeButtonText = isEng ? "To log in as enterprise" : "Для входа в предприятие";
            emailFieldText = isEng ? "email_of_employee" : "email_сотрудника";
            passwordFieldText = isEng ? "password_of_employee" : "пароль_сотрудника";
        }
        logInEmployeeBtn.setText(employeeButtonText);
        emailTextField.setPromptText(emailFieldText);
        passwordField.setPromptText(passwordFieldText);
    }
}