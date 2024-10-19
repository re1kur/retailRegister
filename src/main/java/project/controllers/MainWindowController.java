package project.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import project.Main;
import project.handlers.Handler;

import java.io.IOException;

public class MainWindowController {
    private static Stage mainWindow;

    @FXML
    private Button browserBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button introduceBtn;

    @FXML
    private Button productsBtn;

    @FXML
    private Button reportsBtn;

    @FXML
    private Button settingsBtn;

    @FXML
    void initialize () {
        if (!Handler.isEng()) {
            setRussianGUI();
        }
        mainWindow = Handler.getMainStage();
        closeWindowBtn.setOnAction(_ -> closeWindow());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        introduceBtn.setOnAction(_ -> Handler.openIntroduceWindow());
    }

    private void changeLanguage () {
        Handler.setEng(!Handler.isEng());
        if (Handler.isEng()) {
            changeLanguageBtn.setText("en");
            productsBtn.setText("products");
            reportsBtn.setText("reports");
            settingsBtn.setText("settings");
            browserBtn.setText("browser");
        }
        else {
            setRussianGUI();
            }
    }

    private void closeWindow () {
        changeLanguageBtn.getScene().getWindow().hide();
    }

    private void setRussianGUI() {
        changeLanguageBtn.setText("ru");
        productsBtn.setText("товары");
        reportsBtn.setText("отчеты");
        settingsBtn.setText("настройки");
        browserBtn.setText("браузер");
    }
}
