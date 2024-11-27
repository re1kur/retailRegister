package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import project.entity.Goods;
import project.entity.MeasureUnit;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.util.Collection;

public class DeleteMeasureUnitWindowController {

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private RadioButton deleteGoodsRbtn;

    @FXML
    private TextField field;

    @FXML
    private Label label;

    @FXML
    void initialize() {
        setLanguageInterface();
        deleteBtn.setOnAction(_ -> deleteEntered());
        closeWindowBtn.setOnAction(_ -> deleteBtn.getScene().getWindow().hide());
    }

    private void deleteEntered() {
        boolean deleteGoods = deleteGoodsRbtn.isSelected();
        boolean isEng = Handler.isEng();
        int id;
        Session session = HibernateUtility.getCurrentSession();
        try {
            id = Integer.parseInt(field.getText());
            if (exists(session, id)) {
                Handler.openInfoAlert(isEng ? "ENTERED ID DON'T EXISTS" : "ВВЕДЁННЫЙ ID НЕ СУЩЕСТВУЕТ",
                        isEng ? "The measure unit with this id don't exists." : "Ед.измерения с данным id не существует.");
                return;
            }
        } catch (NumberFormatException e) {
            Handler.openErrorAlert(isEng ? "INVALID ENTERING" : "НЕПРАВИЛЬНЫЙ ВВОД",
                    isEng ? "Enter the only digits without other symbols or letters." :
                            "Введите только числа без других символов или букв.");
            return;
        }
        try {
            MeasureUnit measureUnit = session.find(MeasureUnit.class, id);
            Collection<Goods> goodsList = measureUnit.getGoods();
            if (!deleteGoods && goodsList != null) {
                session.getTransaction().begin();
                for (Goods goods : goodsList) {
                    goods.setMeasureUnit(null);
                }
                session.getTransaction().commit();
            } else if (deleteGoods && goodsList != null) {
                session.getTransaction().begin();
                for (Goods goods : goodsList) {
                    session.remove(goods);
                }
                session.getTransaction().commit();
            }
            session.getTransaction().begin();
            Query<MeasureUnit> query = session.createQuery("delete from MeasureUnit" +
                    " where enterprise = :enterprise and id = :id");
            query.setParameter("enterprise", Handler.getCurrentEnterprise());
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
            if (!exists(session, id)) {
                Handler.openInfoAlert(
                        isEng ? "SUCCESSFULLY DELETED" : "УСПЕШНО УДАЛЕНО",
                        "");
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            System.err.println(e.getMessage());
            Handler.openInfoAlert(isEng ? "SOMETHING WENT WRONG" : "ЧТО-ТО ПОШЛО НЕ ТАК",
                    isEng ? "Could not delete the measure unit. Entered unit still exists." :
                            "Не удалось удалить ед.измерения. Введенная единица все еще существует.");
        }
    }

    private boolean exists(Session session, int id) {
        return (session.find(MeasureUnit.class, id)) == null;
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        deleteBtn.setText(isEng ? "Delete" : "Удалить");
        label.setText(isEng ? "Enter measure unit id to delete" : "Введите id ед.измерения для удаления");
    }

}
