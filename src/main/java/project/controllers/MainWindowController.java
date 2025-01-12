package project.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import project.Main;
import project.handlers.Handler;

import java.io.IOException;

public class MainWindowController {
    @FXML
    private Button personalAccountBtn;

    @FXML
    private Button categoriesBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button goodsBtn;

    @FXML
    private Button introduceBtn;

    @FXML
    private Button reportingBtn;

    @FXML
    private Button settingsBtn;

    @FXML
    private Button supportBtn;

    @FXML
    private Button unitsMeasurementBtn;

    @FXML
    private Button exitAccountBtn;

    @FXML
    void initialize() {
        setLanguageInterface();
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        introduceBtn.setOnAction(_ -> Handler.openIntroduceWindow());
        goodsBtn.setOnAction(_ ->
                Handler.changeScene("goodsWindow"));
        categoriesBtn.setOnAction(_ -> Handler.changeScene("categoriesWindow"));
        unitsMeasurementBtn.setOnAction(_ -> Handler.changeScene("unitsMeasurementWindow"));
        exitAccountBtn.setOnAction(_ -> {
            Handler.setCurrentEnterprise(null);
            Handler.setCurrentEmployee(null);
            Handler.setIsEnterpriseLogging(null);
            Handler.changeScene("logInWindow");
        });
        personalAccountBtn.setOnAction(_ ->
        {
            if (!Handler.getIsEnterpriseLogging()) Handler.changeScene("personalAccountWindow");
            else {
                Handler.changeScene("manageEnterpriseWindow");
            }
        });
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();

    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        goodsBtn.setText(isEng ? "Goods" : "Товары");
        reportingBtn.setText(isEng ? "Reporting" : "Отчетность");
        settingsBtn.setText(isEng ? "Settings" : "Настройки");
        supportBtn.setText(isEng ? "Support" : "Поддержка");
        categoriesBtn.setText(isEng ? "Categories" : "Категории");
        unitsMeasurementBtn.setText(isEng ? "Units of\nmeasurement" : "Единицы\nизмерения");
        exitAccountBtn.setText(isEng ? "Exit" : "Выйти");
    }

}
