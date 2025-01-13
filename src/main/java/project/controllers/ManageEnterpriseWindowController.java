package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.entity.Employee;
import project.entity.Enterprise;
import project.handlers.Handler;
import project.other.EmployeePane;

import java.util.List;

public class ManageEnterpriseWindowController {
    private Enterprise current;

    private boolean isHide = true;

    @FXML
    private Button addBtn;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressTextField;

    @FXML
    private AnchorPane anchorPaneContainer;

    @FXML
    private Button backBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private VBox containerOfEmployeePanes;

    @FXML
    private Button editBtn;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label employeesLabel;

    @FXML
    private ScrollPane employeesPane;

    @FXML
    private AnchorPane enterpriseDataPane;

    @FXML
    private Button introduceBtn;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameTextField;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button swapDataBtn;

    @FXML
    private Label typeLabel;

    @FXML
    private TextField typeTextField;

    @FXML
    private Button visibilityBtn;

    @FXML
    private Label visibilityLabel;

    @FXML
    void initialize() {
        current = Handler.getCurrentEnterprise();
        fillTheVBox(Handler.getEmployees());
        setInterface();
        setActions();
    }

    private void setActions() {
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        backBtn.setOnAction(_ -> Handler.changeScene("mainWindow"));
        addBtn.setOnAction(_ -> {
            Handler.openModalWindow("addEmployeeWindow");
            reloadTable();
        });
        swapDataBtn.setOnAction(_ -> {
            if (enterpriseDataPane.isVisible()) {
                employeesPane.setVisible(true);
                addBtn.setVisible(true);
                editBtn.setVisible(false);
                enterpriseDataPane.setVisible(false);
                swapDataBtn.setText(Handler.isEng() ? "Data" : "Данные");
            }
            else {
                employeesPane.setVisible(false);
                addBtn.setVisible(false);
                editBtn.setVisible(true);
                enterpriseDataPane.setVisible(true);
                swapDataBtn.setText(Handler.isEng() ? "Employees" : "Сотрудники");
            }
        });
        visibilityBtn.setOnAction(_ -> {
            if (isHide) {
                passwordTextField.setEffect(new ColorAdjust(0, 0, 0, 0.5));
                isHide = false;
            }
            else {
                passwordTextField.setEffect(new GaussianBlur(7.0));
                isHide = true;
            }
        });
        editBtn.setOnAction(_ -> Handler.changeScene("editEnterpriseWindow"));
    }

    private void reloadTable() {
        containerOfEmployeePanes.getChildren().clear();
        fillTheVBox(Handler.getEmployees());
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setInterface();
    }

    private void setInterface() {
        boolean isEng = Handler.isEng();

        addBtn.setText(isEng ? "Add" : "Добавить");
        if (!employeesPane.isVisible()) {
            swapDataBtn.setText(isEng ? "Employees" : "Сотрудники");
        } else {
            swapDataBtn.setText(isEng ? "Data" : "Данные");
        }
        backBtn.setText(isEng ? "Menu" : "Меню");
        employeesLabel.setText(isEng ? "Employees" : "Сотрудники");
        changeLanguageBtn.setText(isEng ? "en" : "ru");

        addressLabel.setText(isEng ? "Enterprise address:" : "Адрес предприятия");
        emailLabel.setText(isEng ? "Corporate e-mail:" : "Корпоративная эл. почта:");
        nameLabel.setText(isEng ? "Enterprise name:" : "Название предприятия:");
        passwordLabel.setText(isEng ? "Password to log in:" : "Пароль для входа:");
        typeLabel.setText(isEng ? "Enterprise type:" : "Тип предприятия:");
        visibilityLabel.setText(isEng ? "Visibility" : "Видимость");

        addressTextField.setPromptText(current.getAddress());
        passwordTextField.setPromptText(current.getPassword());
        nameTextField.setPromptText(current.getName());
        typeTextField.setPromptText(current.getType());
        emailTextField.setPromptText(current.getEmail());

    }

    private void fillTheVBox(List<Employee> employeeList) {
        containerOfEmployeePanes.getChildren().clear();
        if (employeeList.isEmpty()) {
            System.out.println("No products found");
            return;
        }
        anchorPaneContainer.setPrefHeight(45 + employeeList.size() * 45);
        containerOfEmployeePanes.setPrefHeight(45 + employeeList.size() * 45);
        for (Employee employee : employeeList) {
            containerOfEmployeePanes.getChildren().add(new EmployeePane(employee));
        }
    }
}
