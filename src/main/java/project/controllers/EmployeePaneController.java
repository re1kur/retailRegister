package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import project.entity.Employee;
import project.handlers.Handler;
import project.handlers.HibernateUtility;
import project.other.EmployeePane;

public class EmployeePaneController {

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label positionLabel;

    @FXML
    private Label rightsLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button editBtn;

    @FXML
    void initialize() {
        setInterface();
        setActions();
    }

    private void setInterface() {
        Employee current = EmployeePane.getEmployee();
        firstNameLabel.setText(current.getFirstname());
        lastNameLabel.setText(current.getLastname());
        positionLabel.setText(current.getPosition());
        try {
            rightsLabel.setText(current.getRights().toString());
        } catch (Exception _) {
            rightsLabel.setText("Rights123");
        }
    }

    private void setActions() {
        editBtn.setOnAction(_ -> {
            Handler.setEnteredEmployee(EmployeePane.getEmployee());
            Handler.changeScene("editEmployeeWindow");
        });
        deleteBtn.setOnAction(_ -> {
            Session session = HibernateUtility.getCurrentSession();
            session.getTransaction().begin();
            session.delete(EmployeePane.getEmployee());
            session.getTransaction().commit();
            Handler.changeScene("manageEnterpriseWindow");
        });
    }

}
