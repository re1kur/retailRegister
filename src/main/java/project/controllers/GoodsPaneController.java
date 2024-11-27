package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.handlers.Handler;
import project.other.GoodsPane;

public class GoodsPaneController {
    @FXML
    private Label categoryValueLabel;

    @FXML
    private Label idValueLabel;

    @FXML
    private Label nameValueLabel;

    @FXML
    private Label numberValueLabel;

    @FXML
    private Label priceValueLabel;

    @FXML
    void initialize() {
        idValueLabel.setText(String.valueOf(GoodsPane.getGoods().getId()));
        nameValueLabel.setText(GoodsPane.getGoods().getName());
        numberValueLabel.setText(getNumber());
        try {
            categoryValueLabel.setText(GoodsPane.getGoods().getCategory().getName());
        } catch (NullPointerException _) {
        }
        priceValueLabel.setText(GoodsPane.getGoods().getPrice().toString());
    }

    private String getNumber() {
        String number = GoodsPane.getGoods().getNumber().toString();
        try {
            String symbol = GoodsPane.getGoods().getMeasureUnit().getSymbol();
            return number + symbol;
        } catch (NullPointerException _) {
            return number;
        }
    }
}
