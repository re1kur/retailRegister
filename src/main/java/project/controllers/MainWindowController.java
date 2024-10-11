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
        Handler.setIsEng(true);
        mainWindow = Main.getMainStage();
        closeWindowBtn.setOnAction(_ -> closeWindow());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        introduceBtn.setOnAction(_ -> Handler.openInfoWindow(mainWindow));
    }

    private void changeLanguage () {
        Handler.setIsEng(!Handler.getIsEng());
        if (Handler.getIsEng()) {
            changeLanguageBtn.setText("en");
            productsBtn.setText("products");
            reportsBtn.setText("reports");
            settingsBtn.setText("settings");
            browserBtn.setText("browser");
        }
        else {
            changeLanguageBtn.setText("ru");
            productsBtn.setText("товары");
            reportsBtn.setText("отчеты");
            settingsBtn.setText("настройки");
            browserBtn.setText("браузер");}
        introduceBtn.setOnAction(_ -> Handler.openInfoWindow(mainWindow));

    }

    private void openProductsWindow(Stage mainWindow) {

    }

    private void openReportsWindow(Stage mainWindow) {

    }

    private void openSettingsWindow(Stage mainWindow) {

    }

    private void openBrowserWindow(Stage mainWindow) {

    }


    private void changeScene (String fxmlFileName) {
        try {
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/" + fxmlFileName + ".fxml"));
            mainWindow.setScene(new Scene(root));
            mainWindow.show();
        } catch (IOException e) {
            System.err.println("Couldn't load " + fxmlFileName + ".fxml file.\n"
            + e.getMessage());
        }
    }
    private void closeWindow () {
        changeLanguageBtn.getScene().getWindow().hide();
    }
}
