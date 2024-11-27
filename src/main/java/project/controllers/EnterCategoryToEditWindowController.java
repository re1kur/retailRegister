package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Category;
import project.entity.Goods;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

public class EnterCategoryToEditWindowController {

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button editBtn;

    @FXML
    private TextField field;

    @FXML
    private Label label;

    @FXML
    void initialize() {
        setLanguageInterface();
        editBtn.setOnAction(_ -> nextActionEdit());
        closeWindowBtn.setOnAction(_ -> editBtn.getScene().getWindow().hide());
    }

    private void nextActionEdit() {
        boolean isEng = Handler.isEng();
        Session session = HibernateUtility.getCurrentSession();
        int id;
        try {
            id = Integer.parseInt(field.getText());
            if (!exists(session,id)) {
                Handler.openInfoAlert(isEng ? "ENTERED CATEGORY DOESN'T EXISTS" : "ВВЕДЕННАЯ КАТЕГОРИЯ НЕ СУЩЕСТВУЕТ",
                        isEng ? "Try other one." : "Попробуйте другую.");
                return;
            }
        } catch (NumberFormatException _) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING" : "НЕПРАВИЛЬНЫЙ ВВОД",
                    isEng ? "Enter the only digits without other symbols or letters." :
                            "Введите только числа без других символов или букв.");
            return;
        }
        Handler.setEnteredCategory(session.find(Category.class, id));
        closeWindowBtn.getScene().getWindow().hide();
        Handler.changeScene("editCategoryWindow");
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        label.setText(isEng ? "Enter the category id to edit" : "Введите id категории для редактирования");
        editBtn.setText(isEng ? "Edit" : "Редактировать");
    }
    private boolean exists(Session session, int id) {
        return session.find(Category.class, id) != null;
    }

}
