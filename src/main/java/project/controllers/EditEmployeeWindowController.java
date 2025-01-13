package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.hibernate.Session;
import project.entity.Employee;
import project.handlers.Handler;
import project.handlers.HibernateUtility;
import project.other.ApplicationRights;

import java.io.IOException;

public class EditEmployeeWindowController {
    private Employee employee;

    @FXML
    private Button backBtn;

    @FXML
    private Label categoryLabel;

    @FXML
    private Button changeImgBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button deleteImgBtn;

    @FXML
    private Button applyBtn;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label enteredLabel;

    @FXML
    private Label firstNameLabel;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private ImageView imgAvatar;

    @FXML
    private Label lastNameLabel;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label positionLabel;

    @FXML
    private TextField positionTextField;

    @FXML
    private ChoiceBox<ApplicationRights> rightsChoiceBox;

    @FXML
    void initialize() {
        employee = Handler.getEnteredEmployee();
        setInterface();
        setActions();
    }

    private void setActions() {
        backBtn.setOnAction(_ -> Handler.changeScene("manageEnterpriseWindow"));
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        applyBtn.setOnAction(_ -> editEmployee());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
    }

    private void editEmployee() {
        if (!checkFields()) return;

        employee.setEmail(emailTextField.getText());
        employee.setFirstname(firstNameTextField.getText());
        employee.setLastname(lastNameTextField.getText());
        employee.setPassword(passwordTextField.getText());
        employee.setPosition(positionTextField.getText());
        employee.setRights(rightsChoiceBox.getValue());
        Session session = HibernateUtility.getCurrentSession();
        session.beginTransaction();
        session.update(employee);
        session.getTransaction().commit();

        Handler.changeScene("manageEnterpriseWindow");
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

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());

        setInterface();
    }

    private void setInterface() {
        boolean isEng = Handler.isEng();

        changeLanguageBtn.setText(isEng ? "en" : "ru");
        changeImgBtn.setText(isEng ? "Change" : "Сменить");
        deleteImgBtn.setText(isEng ? "Delete" : "Удалить");

        enteredLabel.setText(employee.toString());
        firstNameLabel.setText(isEng ? "First Name:" : "Имя:");
        lastNameLabel.setText(isEng ? "Last Name:" : "Фамилия:");
        emailLabel.setText(isEng ? "E-mail:" : "Эл. почта:");
        passwordLabel.setText(isEng ? "Password:" : "Пароль:");
        positionLabel.setText(isEng ? "Position:" : "Должность:");
        categoryLabel.setText(isEng ? "Select the rights ↓" : "Выберите права ↓");

        firstNameTextField.setPromptText(isEng ? "First Name of employee" : "Имя сотрудника");
        lastNameTextField.setPromptText(isEng ? "Last Name of employee" : "Фамилия сотрудника");
        emailTextField.setPromptText(isEng ? "E-mail of employee" : "Эл. почта сотрудника");
        passwordTextField.setPromptText(isEng ? "Password of employee" : "Пароль сотрудника");
        positionTextField.setPromptText(isEng ? "Position of employee" : "Должность сотрудника");

        backBtn.setText(isEng ? "Back" : "Обратно");
        applyBtn.setText(isEng ? "Apply" : "Применить");

        firstNameTextField.setText(employee.getFirstname());
        lastNameTextField.setText(employee.getLastname());
        emailTextField.setText(employee.getEmail());
        passwordTextField.setText(employee.getPassword());
        positionTextField.setText(employee.getPosition());

        rightsChoiceBox.getSelectionModel().clearSelection();
        rightsChoiceBox.getItems().clear();
        rightsChoiceBox.getItems().addAll(ApplicationRights.values());
        rightsChoiceBox.getSelectionModel().select(employee.getRights());
    }

}
