package view.graph;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
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
    @FXML
    ScatterChart<Number,Number> algo;
    public NumberAxis xAxis = new NumberAxis();
    public NumberAxis yAxis = new NumberAxis();
    public FloatProperty listvalue;
    public IntegerProperty time;
    final int size=10;
    public Mygraphcontroller() {
        listvalue=new SimpleFloatProperty();
        time=new SimpleIntegerProperty();
        algo=new ScatterChart<>(xAxis,yAxis);
    }





    public void AddtoGraph(XYChart.Series ser,String t,Number v) {
        Platform.runLater(()->ser.getData().add(new XYChart.Data<String, Number>(t, v)));
    }
    public void SimpleAnomalyDetectorGraph(XYChart.Series ser,float x,float y){
        Platform.runLater(()->ser.getData().add(new XYChart.Data(x, y)));
    }


}
