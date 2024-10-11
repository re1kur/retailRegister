package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.io.IOException;

public class Main extends Application {
    @Getter
    private static Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            mainStage = stage;
            Parent root = FXMLLoader.load(getClass().getResource("/scenes/mainWindow.fxml"));
            mainStage.setScene(new Scene(root));
            mainStage.initStyle(StageStyle.UNDECORATED);
            mainStage.setTitle("RetailRegister");
            mainStage.show();
        } catch (IOException e) {
            System.err.println("Couldn't load the mainWindow.fxml file.\n"
            + e.getMessage());
        }
    }

}
