package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import project.entity.Employee;
import project.handlers.Handler;

public class PersonalAccountWindowController {
    private Employee current;

    @FXML
    private Label positionLabel;

    @FXML
    private ImageView avatarView;

    @FXML
    private Button backBtn;

    @FXML
    private Button changeAvatarBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Button introduceBtn;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label rightsLabel;

    @FXML
    void initialize() {
        current = Handler.getCurrentEmployee();
        setLanguageInterface();
        setData();
        setActions();
    }


    private void setActions() {
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        backBtn.setOnAction(_ -> Handler.changeScene("mainWindow"));
        editBtn.setOnAction(_ -> Handler.changeScene("editPersonalDataWindow"));
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();

        backBtn.setText(isEng ? "Back" : "Обратно");
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        changeAvatarBtn.setText(isEng ? "Change Avatar" : "Сменить Аватар");
        editBtn.setText(isEng ? "Edit" : "Изменить");

    }

    private void setData() {
        firstNameLabel.setText(current.getFirstname());
        lastNameLabel.setText(current.getLastname());
        positionLabel.setText(current.getPosition());
        rightsLabel.setText(current.getRights().toString());
    }
}
