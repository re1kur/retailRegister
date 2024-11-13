package project.entity;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.io.IOException;

public class MeasureUnitPane extends VBox {
    @Getter
    public static MeasureUnit measureUnit;

    public MeasureUnitPane(MeasureUnit measureUnit) {
        MeasureUnitPane.measureUnit = measureUnit;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(
                "/scenes/unitsMeasurementPane.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getChildren().add(new VBox(root));
    }
}
