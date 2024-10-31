package project.entity;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.IOException;

public class CategoryPane extends VBox {
    @Getter
    private static Category category;


    public CategoryPane(Category category) {
        this.category = category;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(
                "/scenes/CategoryPane.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getChildren().add(new VBox(root));
    }
}
