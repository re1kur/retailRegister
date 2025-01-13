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
import org.hibernate.Session;
import org.hibernate.query.Query;
import project.Main;
import project.entity.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Handler {
    @Setter
    @Getter
    private static Stage mainStage;

    @Setter
    @Getter
    private static boolean isEng = true;

    @Setter
    @Getter
    private static Enterprise currentEnterprise;

    @Getter
    @Setter
    private static Goods enteredGoods;

    @Setter
    @Getter
    private static Category enteredCategory;

    @Setter
    @Getter
    private static MeasureUnit enteredMeasureUnit;

    @Setter
    @Getter
    private static Employee enteredEmployee;

    @Setter
    @Getter
    private static Employee currentEmployee;

    @Setter
    @Getter
    private static Boolean isEnterpriseLogging;

    public static boolean parseMail(String mail) {
        Pattern invalidChars = Pattern.compile("[^a-zA-Z0-9@.]");
        Matcher matcher = invalidChars.matcher(mail);
        if (matcher.find()) {
            Handler.openErrorAlert("INVALID EMAIL", "Enter the email without invalid characters.");
            return false;
        }
        Pattern correct = Pattern.compile("^[a-zA-Z0-9.]{1,64}" + "@[a-zA-Z0-9.]{1,126}\\.[a-zA-Z]{2,63}$");
        matcher = correct.matcher(mail);
        if (matcher.matches()) {
            return true;
        } else {
            Handler.openErrorAlert("INVALID EMAIL", "Enter a valid email address.");
            return false;
        }
    }

    public static List<Employee> getEmployees() {
        Session session = HibernateUtility.getCurrentSession();
        Query<Employee> query = session.createQuery("from Employee where enterprise = :enterprise",
                Employee.class);
        query.setParameter("enterprise", getCurrentEnterprise());
        return query.getResultList();
    }

    public static void openModalWindow(String fxmlFileName) {
        Parent root = null;
        Stage stage = new Stage();
        try {
            root = FXMLLoader
                    .load(Objects.requireNonNull(Main.class
                            .getResource("/scenes/" + fxmlFileName + ".fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(mainStage);
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
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

    public static void openYesOrNowWindow(String message) {
        Stage window = new Stage();
        window.initStyle(StageStyle.UNDECORATED);
        window.initModality(Modality.WINDOW_MODAL);
        window.initOwner(mainStage);
        window.showAndWait();
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

    public static List<Goods> getGoods() {
        Session session = HibernateUtility.getCurrentSession();
        Query<Goods> query = session.createQuery(
                "from Goods where enterprise = :enterprise",
                Goods.class);
        query.setParameter("enterprise", getCurrentEnterprise());
        return query.getResultList();
    }

    public static List<Category> getCategories() {
        Session session = HibernateUtility.getCurrentSession();
        Query<Category> query = session.createQuery("from Category where enterprise = :enterprise",
                Category.class);
        query.setParameter("enterprise", getCurrentEnterprise());
        return query.getResultList();
    }

    public static List<MeasureUnit> getUnitsMeasurement() {
        Session session = HibernateUtility.getCurrentSession();
        Query<MeasureUnit> query = session.createQuery("from MeasureUnit where enterprise = :enterprise",
                MeasureUnit.class);
        query.setParameter("enterprise", currentEnterprise);
        return query.list();
    }

    public static String getCategoryNumber(Category category) {
        Session session = HibernateUtility.getCurrentSession();
        Query<Goods> query = session.createQuery(
                "from Goods where enterprise = :enterprise and category = :category",
                Goods.class);
        query.setParameter("enterprise", currentEnterprise);
        query.setParameter("category", category);
        return String.valueOf(query.list().size());
    }
}
