package project.other;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import project.entity.Employee;

import java.io.IOException;

public class EmployeePane extends AnchorPane {
    @Getter
    private static Employee employee;
    
    public EmployeePane(Employee employee) {
        EmployeePane.employee = employee;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(
                "/scenes/employeePane.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getChildren().add(new AnchorPane(root));
    }
}
