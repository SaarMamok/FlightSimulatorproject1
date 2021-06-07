package view.graph;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Mygraphcontroller {

    @FXML
    LineChart leftgraph;
    @FXML
    LineChart rightgraph;
    public FloatProperty listvalue;
    public IntegerProperty time;
    final int size=10;
    public Mygraphcontroller() {
        listvalue=new SimpleFloatProperty();
        time=new SimpleIntegerProperty();

    }





    public void AddtoGraph(XYChart.Series ser,String t,Number v) {
        Platform.runLater(()->ser.getData().add(new XYChart.Data<String, Number>(t, v)));
    }


}
