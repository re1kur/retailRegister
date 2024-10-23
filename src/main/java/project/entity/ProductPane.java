package project.entity;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.IOException;

public class ProductPane extends VBox {
    @Getter
    private static Product product;


    public ProductPane(Product product) {
        this.product = product;
        FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(
                    "/scenes/productPane.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getChildren().add(new VBox(root));
    }
}
