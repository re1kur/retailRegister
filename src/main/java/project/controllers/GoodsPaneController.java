package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.entity.GoodsPane;

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
        categoryValueLabel.setText(GoodsPane.getGoods().getCategory().getName());
        numberValueLabel.setText(getNumber());
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
