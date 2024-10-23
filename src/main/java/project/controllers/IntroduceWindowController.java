package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import project.handlers.Handler;

public class IntroduceWindowController {

    private final boolean isEng = Handler.isEng();

    @FXML
    private Button closeWindowBtn;

    @FXML
    private Label firstStringLabel;

    @FXML
    private Label secondStringLabel;

    @FXML
    private Label thirdStringLabel;

    @FXML
    private Label fourthStringLabel;

    @FXML
    void initialize() {
        if (!isEng) {
            setRussianGUI();
        }
        closeWindowBtn.setOnAction(_ -> firstStringLabel.getScene().getWindow().hide());
    }

    private void setRussianGUI() {
        firstStringLabel.setText(" Здравствуйте. Это приложение было соз");
        secondStringLabel.setText("-дано только для проверки своих навыков.");
        thirdStringLabel.setText(" Я использовал такие библиотеки, как");
        fourthStringLabel.setText(" Hibernate(postgreSQL), minIO.");
    }
}
