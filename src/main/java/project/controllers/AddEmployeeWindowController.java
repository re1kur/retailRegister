package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Employee;
import project.handlers.Handler;
import project.handlers.HibernateUtility;
import project.other.ApplicationRights;

public class AddEmployeeWindowController {

    @FXML
    private Button addBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField positionTextField;

    @FXML
    private ChoiceBox<ApplicationRights> rightsChoiceBox;

    @FXML
    void initialize() {
        setInterface();
        setActions();
    }

    private void setInterface() {
        boolean isEng = Handler.isEng();

        firstNameTextField.setPromptText(isEng ? "First name (optionally)" : "Имя (необязательно)");
        lastNameTextField.setPromptText(isEng ? "Last name (optionally)" : "Фамилия (необязательно)");
        emailTextField.setPromptText(isEng ? "E-mail" : "Эл. почта");
        passwordTextField.setPromptText(isEng ? "Password" : "Пароль");
        positionTextField.setPromptText(isEng ? "Position" : "Профессия");
        addBtn.setText(isEng ? "Add" : "Добавить");
        rightsChoiceBox.getItems().addAll(ApplicationRights.values());

    }

    private void setActions() {
        closeWindowBtn.setOnAction(_ -> firstNameTextField.getScene().getWindow().hide());
        addBtn.setOnAction(_ -> addEmployee());
    }

    private void addEmployee() {
        ApplicationRights rights = rightsChoiceBox.getSelectionModel().getSelectedItem();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String position = positionTextField.getText();
        Employee employee = Employee.builder()
                .email(email)
                .enterprise(Handler.getCurrentEnterprise())
                .password(password)
                .position(position)
                .firstname(firstName)
                .lastname(lastName)
                .rights(rights)
                .build();
        Session session = HibernateUtility.getCurrentSession();
        session.beginTransaction();
        session.save(employee);
        session.getTransaction().commit();
    }

}
