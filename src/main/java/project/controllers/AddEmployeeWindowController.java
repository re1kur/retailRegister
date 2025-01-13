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

import java.io.IOException;
import java.util.Objects;

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
        if (!checkFields()) return;

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

    private boolean checkFields() {
        boolean isEng = Handler.isEng();
        try {
            if (firstNameTextField.getText().length() > 30) {
                throw new IOException(isEng ? "Incorrect first Name.\nThe first name must be lesser than 30 chars." : "Неправильное имя.\nИмя должно быть меньше 30 символов");
            }
            if (lastNameTextField.getText().length() > 30) {
                throw new IOException(isEng ? "Incorrect last Name.\nThe last name must be lesser than 30 chars." : "Неправильная фамилия.\nФамилия должно быть меньше 30 символов");
            }
            if (firstNameTextField.getText().isEmpty() | lastNameTextField.getText().isEmpty() | emailTextField.getText().isEmpty() | passwordTextField.getText().isEmpty() | positionTextField.getText().isEmpty()) {
                throw new IOException(isEng ? "The field(s) is empty.\nEnter values in the empty field(s) and try again." : "Поле(поля) пустое.\nВведите данные в пустые поля и попробуйте еще раз.");
            }
            if (emailTextField.getText().length() > 256) {
                throw new IOException(
                        isEng ? "Incorrect e-mail.\nThe e-mail must be lesser than 256 chars." :
                                "Неправильная почта.\nПочта должна быть меньше 256 символов.");
            }
            if (!Handler.parseMail(emailTextField.getText())) {
                return false;
            }
            if (positionTextField.getText().length() > 30) {
                throw new IOException(isEng ? "Incorrect position\nType position be lesser than 30 chars."
                        : "Неправильная должность.\nДолжность должна быть меньше 30 символов.");
            }
            if (passwordTextField.getText().length() > 64) {
                throw new IOException(isEng ? "Incorrect password\nPassword must be lesser than 64 chars." :
                        "Неправильный пароль.\nПароль должен быть меньше 64 символов");
            }
            if (rightsChoiceBox.getSelectionModel().isEmpty()) throw new IOException(isEng ? "Select application rights." :
                    "Выберите права управления в приложении.");
        } catch (IOException e) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING" : "НЕПРАВИЛЬНЫЙ ВВОД",
                    e.getMessage());
            return false;
        }
        return true;
    }
}
