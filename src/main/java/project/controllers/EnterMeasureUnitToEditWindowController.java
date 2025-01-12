package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import project.entity.Category;
import project.entity.MeasureUnit;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

public class EnterMeasureUnitToEditWindowController {

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
            if (!exists(session, id)) {
                Handler.openInfoAlert(isEng ? "ENTERED MEASURE UNIT DOESN'T EXISTS" : "ВВЕДЕННАЯ ЕД. ИЗМЕРЕНИЯ НЕ СУЩЕСТВУЕТ",
                        isEng ? "Try other one." : "Попробуйте другую.");
                return;
            }
        } catch (NumberFormatException _) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING" : "НЕПРАВИЛЬНЫЙ ВВОД",
                    isEng ? "Enter the only digits without other symbols or letters." :
                            "Введите только числа без других символов или букв.");
            return;
        }
        Handler.setEnteredMeasureUnit(session.find(MeasureUnit.class, id));
        closeWindowBtn.getScene().getWindow().hide();
        Handler.changeScene("editMeasureUnitWindow");
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        label.setText(isEng ? "Enter the measure unit id to edit" : "Введите id единицы измерения\n для его редактирования");
        editBtn.setText(isEng ? "Edit" : "Редактировать");
    }

    private boolean exists(Session session, int id) {
        return session.find(MeasureUnit.class, id) != null;
    }

}
