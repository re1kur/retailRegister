package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.hibernate.Session;
import org.hibernate.query.Query;
import project.entity.Product;
import project.entity.ProductPane;
import project.handlers.Handler;
import project.handlers.HibernateUtility;

import java.util.List;

public class ProductsWindowController {

    @FXML
    private Button addBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button changeLanguageBtn;

    @FXML
    private Button closeWindowBtn;

    @FXML
    private VBox containerOfProductPanes;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button findBtn;

    @FXML
    private Button statisticsBtn;

    @FXML
    private Label numberLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label idLabel;

    @FXML
    private Label categoryLabel;


    @FXML
    private AnchorPane anchorPaneContainer;

    @FXML
    void initialize() {
        fillTheVBox();
        setLanguageInterface();
        changeLanguageBtn.setOnAction(_ -> changeLanguage());
        backBtn.setOnAction(_ -> Handler.changeScene("mainWindow"));
        closeWindowBtn.setOnAction(_ -> Handler.closeMainStage());
        addBtn.setOnAction(_ -> Handler.changeScene("addProductWindow"));

    }

    private void changeLanguage() {
        Handler.setEng(!Handler.isEng());
        setLanguageInterface();
    }

    private void setLanguageInterface() {
        boolean isEng = Handler.isEng();
        addBtn.setText(isEng ? "add" : "добавить");
        deleteBtn.setText(isEng ? "delete" : "удалить");
        statisticsBtn.setText(isEng ? "statistics" : "статистика");
        findBtn.setText(isEng ? "find" : "найти");
        changeLanguageBtn.setText(isEng ? "en" : "ru");
        backBtn.setText(isEng ? "back" : "обратно");
        idLabel.setText(isEng ? "Id" : "Номер");
        nameLabel.setText(isEng ? "Name" : "Название");
        numberLabel.setText(isEng ? "Number" : "Количество");
        categoryLabel.setText(isEng  ? "Category" : "Категория");
    }

    private void fillTheVBox() {
        Session session = HibernateUtility.getCurrentSession();
        Query<Product> query = session.createQuery(
                "from Product where enterprise = :enterprise",
                Product.class);
        query.setParameter("enterprise", Handler.getCurrentEnterprise());
        List<Product> rs = query.getResultList();
        if (rs.isEmpty()) {
            System.out.println("No products found");
            return;
        }
        anchorPaneContainer.setPrefHeight(22 + rs.size() * 22);
        containerOfProductPanes.setPrefHeight(22 + rs.size() * 22);
        for (Product product : rs) {
            containerOfProductPanes.
                    getChildren().add(new ProductPane(product));
        }
    }

}
