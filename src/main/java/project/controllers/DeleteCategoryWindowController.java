package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.query.Query;
import project.entity.Category;
import project.entity.Goods;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

public class DeleteCategoryWindowController {

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TextField field;

    @FXML
    private Label label;

    @FXML
    void initialize() {
        setLanguageInterface();
        closeWindowBtn.setOnAction(_ -> deleteBtn.getScene().getWindow().hide());
        deleteBtn.setOnAction(_ -> deleteSelected());
    }

    private void deleteSelected() {
        boolean isEng = Handler.isEng();
        int id;
        Session session = HibernateUtility.getCurrentSession();
        try {
            id = Integer.parseInt(field.getText());
            if (checkIfExists(session, id)) {
                Handler.openInfoAlert(isEng ? "ENTERED ID DON'T EXISTS" : "ВВЕДЁННЫЙ ID НЕ СУЩЕСТВУЕТ",
                        isEng ? "The category with this id don't exists." : "Категория с данным id не существует.");
                return;
            }
        } catch (NumberFormatException e) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING" : "НЕПРАВИЛЬНЫЙ ВВОД",
                    isEng ? "Enter the only digits without other symbols or letters." :
                            "Введите только числа без других символов или букв.");
            return;
        }
        session.beginTransaction();
        Query<Goods> query = session.createQuery("delete from Category" +
                " where enterprise = :enterprise and id = :id");
        query.setParameter("enterprise", Handler.getCurrentEnterprise());
        query.setParameter("id", id);
        query.executeUpdate();
        session.getTransaction().commit();
        if (!checkIfExists(session, id)) {
            Handler.openErrorAlert(
                    isEng ? "SUCCESSFULLY DELETED" : "УСПЕШНО УДАЛЕНО",
                    "");
        } else {
            Handler.openInfoAlert(isEng ? "SOMETHING WENT WRONG" : "ЧТО-ТО ПОШЛО НЕ ТАК",
                    isEng ? "Could not delete the category. Entered category still exists." :
                            "Не удалось удалить категорию. Введенная категория все еще существует.");
        }
    }

    private boolean checkIfExists(Session session, int id) {
        return (session.find(Category.class, id)) == null;
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        deleteBtn.setText(isEng ? "Delete" : "Удалить");
        label.setText(isEng ? "Enter the category id to delete" : "Введите id категории для удаления");
    }
}
