package project.handlers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Handler {

    private static boolean isEng;

    public static void setIsEng(boolean isEng) {
        Handler.isEng = isEng;
    }

    public static boolean getIsEng() {
        return isEng;
    }

    public static void openInfoWindow(Stage stage) {
        Stage information = new Stage();
        try {
            if (isEng) {
                Parent root = FXMLLoader.load(
                        Objects.requireNonNull(
                                Handler.class.getResource(
                                        "/scenes/infoWindowEN.fxml")));
                information.setScene(new Scene(root));
            }
            if (!isEng) {
                Parent root = FXMLLoader.load(
                        Objects.requireNonNull(
                                Handler.class.getResource(
                                        "/scenes/infoWindowRU.fxml")));
                information.setScene(new Scene(root));
            }
        } catch (IOException e) {
            System.err.println("Couldn't open information window.\n"
                    + e.getMessage());
            information.close();
            return;
        }
        information.initStyle(StageStyle.UNDECORATED);
        information.initModality(Modality.WINDOW_MODAL);
        information.initOwner(stage);
        information.showAndWait();
    }
}
