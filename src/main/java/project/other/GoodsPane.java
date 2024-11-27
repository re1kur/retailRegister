package project.other;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import lombok.Getter;
import project.entity.Goods;

import java.io.IOException;

public class GoodsPane extends VBox {
    @Getter
    private static Goods goods;


    public GoodsPane(Goods goods) {
        GoodsPane.goods = goods;
        FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(
                    "/scenes/goodsPane.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getChildren().add(new VBox(root));
    }
}
