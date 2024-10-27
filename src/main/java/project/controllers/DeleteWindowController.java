package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.hibernate.Session;
import org.hibernate.query.Query;
import project.entity.Product;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

public class DeleteWindowController {
    String selectedString;

    @FXML
    private Button backBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private ChoiceBox<String> choiceToDelete;

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

        choiceToDelete.setOnAction(_ -> selected());
        setLanguageInterface();
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        backBtn.setOnAction(_ -> Handler.changeScene("productsWindow"));
        deleteBtn.setOnAction(_ -> deleteSelected());

    }

    private void deleteSelected() {
        boolean isEng = Handler.isEng();
        if (selectedString.isEmpty()) {
            Handler.openInfoAlert(isEng ? "NOTHING SELECTED" :
                            "НИЧЕГО НЕ ВЫБРАНО",
                    isEng ? "Select what you want to delete." :
                            "Выберите то, что хотите удалить.");
            return;
        }
        Session session = HibernateUtility.getCurrentSession();
        if (selectedString.equals("Product")) {
            int id = Integer.parseInt(field.getText());
            session.beginTransaction();
            Query<Product> query = session.createQuery("delete from Product where id = :id and enterprise = :enterprise");
            query.setParameter("id", id);
            query.setParameter("enterprise", Handler.getCurrentEnterprise());
            query.executeUpdate();
            session.getTransaction().commit();
        }
        if (selectedString.equals("Category")) {
            String name = field.getText();
            session.beginTransaction();
            Query<Product> query = session.createQuery("delete from Product where category = :category and enterprise = :enterprise");
            query.setParameter("enterprise", Handler.getCurrentEnterprise());
            query.setParameter("category", name);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    private void selected() {
        boolean isEng = Handler.isEng();
        String select = choiceToDelete.getSelectionModel().getSelectedItem();
        if (select.equals(isEng ? "Product" : "Товар")) {
            selectedString = "Product";
            field.setPromptText(isEng ? "Product Id" : "Id товара");
        }
        if (select.equals(isEng ? "Category" : "Категория")) {
            selectedString = "Category";
            field.setPromptText(isEng ? "Category name" : "Название категории");
        }
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        choiceToDelete.getSelectionModel().clearSelection();
        selectedString = "";
        field.setPromptText("");
        choiceToDelete.getItems().clear();
        choiceToDelete.getItems().addAll(isEng ? "Product" : "Продукт",
                isEng ? "Category" : "Категория");
        backBtn.setText(isEng ? "Products" : "Товары");
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        deleteBtn.setText(isEng ? "Delete" : "Удалить");
        label.setText(isEng ? "<- Select what you want to delete" : "<- Выберите, что хотите удалить");
    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

}
