package project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.handlers.Handler;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Handler.setMainStage(stage);
        Stage mainStage = Handler.getMainStage();
        Parent root = FXMLLoader
                .load(getClass()
                .getResource("/scenes/logInWindow.fxml"));
        mainStage.setScene(new Scene(root));
        mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.show();
    }

}
