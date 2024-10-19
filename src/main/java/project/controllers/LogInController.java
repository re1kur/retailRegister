package project.controllers;

import jakarta.persistence.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import project.entity.Enterprise;
import project.handlers.Handler;

public class LogInController {
    private Enterprise enterprise;

    @PersistenceContext
    private EntityManager em;

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
        if (!Handler.isEng()) {
            setRussianGUI();
        }
        closeWindowBtn.setOnAction(_ -> closeWindow());
        continueBtn.setOnAction(_ -> continueAction());
        signUpBtn.setOnAction(_ -> Handler.changeScene("signUpWindow"));
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
    }

    private void closeWindow() {
        continueBtn.getScene().getWindow().hide();
    }

    private void continueAction() {
        if (!checkMailPassword()) {
            Handler.openErrorAlert("WRONG PASSWORD",
                    "Please enter a valid password.");
            return;
        }
        Handler.setCurrentEnterprise(enterprise);
        Handler.changeScene("mainWindow");
    }

    private boolean checkMailPassword() {
        String email = emailTextField.getText();
        String password = passwordField.getText();
        em = null;
        try {
            em = Persistence.createEntityManagerFactory(
                    "retailRegister").createEntityManager();
            TypedQuery<Enterprise> query = em.createQuery(
                    "SELECT e FROM Enterprise e WHERE e.email" +
                            " = :email", Enterprise.class);
            query.setParameter("email", email);
            Enterprise enterprise = query.getSingleResult();
            if (enterprise.getPassword().equals(password)) {
                this.enterprise = enterprise;
                return true;
            }
            return false;
        } catch (NoResultException e) {
            Handler.openErrorAlert("INVALID EMAIL",
                    "Could not find the enterprise with the provided email.\n" +
                            "Please, try again.");
            return false;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        if (Handler.isEng()) {
            changeLanguageBtn.setText("en");
            continueBtn.setText("continue");
            emailTextField.setPromptText("email_of_enterprise");
            passwordField.setPromptText("password_of_enterprise");
            signUpBtn.setText("sign up");
        } else {
            setRussianGUI();
        }
    }

    private void setRussianGUI() {
        changeLanguageBtn.setText("ru");
        continueBtn.setText("дальше");
        emailTextField.setPromptText("email_предприятия");
        passwordField.setPromptText("пароль_предприятия");
        signUpBtn.setText("зарег-ть");
    }
}