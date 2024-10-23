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
        setLanguageInterface();
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        introduceBtn.setOnAction(_ -> Handler.openIntroduceWindow());
        productsBtn.setOnAction(_ ->
                Handler.changeScene("productsWindow"));
    }

    private void changeLanguage () {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();

    }

    private void setLanguageInterface () {
        changeLanguageBtn.setText(Handler.isEng() ? "en": "ru");
        productsBtn.setText(Handler.isEng() ? "products": "товары");
        reportsBtn.setText(Handler.isEng() ? "reports": "отчеты");
        settingsBtn.setText(Handler.isEng() ? "settings": "настройки");
        browserBtn.setText(Handler.isEng() ? "browser": "браузер");
    }

}
