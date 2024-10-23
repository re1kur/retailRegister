package project.handlers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import project.Main;
import project.entity.Enterprise;

import java.io.IOException;
import java.util.Objects;

public class Handler {
    @Getter
    @Setter
    private static Scene lastScene;

    @Setter
    @Getter
    private static Stage mainStage;

    @Setter
    @Getter
    private static boolean isEng = true;

    @Setter
    @Getter
    private static Enterprise currentEnterprise;

    public static void reloadEnterprise() {
        HibernateUtility.getCurrentSession().getTransaction().begin();
        HibernateUtility.getCurrentSession().clear();
        currentEnterprise = HibernateUtility.getCurrentSession()
                .get(Enterprise.class, currentEnterprise.getId());
        HibernateUtility.getCurrentSession().getTransaction().commit();
        HibernateUtility.getCurrentSession().close();
    }

    public static void changeScene(String fxmlFileName) {
        Parent root = null;
        try {
            root = FXMLLoader
                    .load(Objects.requireNonNull(Main.class
                            .getResource("/scenes/" + fxmlFileName + ".fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mainStage.setScene(new Scene(root));
            mainStage.show();
    }

    public static void openIntroduceWindow() {
        Stage introduceWindow = new Stage();
        try {
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(
                            Handler.class.getResource(
                                    "/scenes/introduceWindow.fxml")));
            introduceWindow.setScene(new Scene(root));
        } catch (IOException e) {
            System.err.println("Couldn't open information window.\n"
                    + e.getMessage());
            introduceWindow.close();
            return;
        }
        introduceWindow.initStyle(StageStyle.UNDECORATED);
        introduceWindow.initModality(Modality.WINDOW_MODAL);
        introduceWindow.initOwner(mainStage);
        introduceWindow.showAndWait();
    }

    public static void openErrorAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(text);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public static void openInfoAlert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(text);
        alert.setTitle(title);
        alert.showAndWait();
    }

    public static void closeMainStage() {
        mainStage.close();
    }
}
