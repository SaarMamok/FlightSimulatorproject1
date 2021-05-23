package view.graph;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;

import java.net.URL;
import java.util.ResourceBundle;

public class Mygraphcontroller implements Initializable {
    @FXML
    LineChart leftgraph;
    @FXML
    LineChart rightgraph;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
