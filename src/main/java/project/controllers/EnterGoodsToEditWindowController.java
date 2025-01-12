package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Goods;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

public class EnterGoodsToEditWindowController {

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
        closeWindowBtn.setOnAction(_ -> editBtn.getScene().getWindow().hide());
        editBtn.setOnAction(_ -> nextActionEdit());
    }

    private void nextActionEdit() {
        boolean isEng = Handler.isEng();
        Session session = HibernateUtility.getCurrentSession();
        int id;
        try {
            id = Integer.parseInt(field.getText());
            if (!exists(session, id)) {
                Handler.openInfoAlert(isEng ? "ENTERED GOODS DOESN'T EXISTS" : "ВВЕДЕННЫЙ ТОВАР НЕ СУЩЕСТВУЕТ",
                        isEng ? "Try other one." : "Попробуйте другой.");
                return;
            }
        } catch (NumberFormatException _) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING" : "НЕПРАВИЛЬНЫЙ ВВОД",
                    isEng ? "Enter the only digits without other symbols or letters." :
                            "Введите только числа без других символов или букв.");
            return;
        }
        Handler.setEnteredGoods(session.find(Goods.class, id));
        closeWindowBtn.getScene().getWindow().hide();
        Handler.changeScene("editGoodsWindow");
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        editBtn.setText(isEng ? "Edit" : "Редактировать");
        label.setText(isEng ? "Enter the goods id to edit it" : "Введите id товара\n для его редактирования");
    }

    private boolean exists(Session session, int id) {
        return session.find(Goods.class, id) != null;
    }
}
