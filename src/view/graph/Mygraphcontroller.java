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
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    @FXML
    LineChart leftgraph;
    @FXML
    LineChart rightgraph;
    @FXML
    ScatterChart <Number,Number>algo;
    public FloatProperty listvalue;
    public IntegerProperty time;
    final int size=10;
    private int counter;
    public Mygraphcontroller() {
        listvalue=new SimpleFloatProperty();
        time=new SimpleIntegerProperty();
    }





    public void AddtoGraph(XYChart.Series ser,String t,Number v) {
        Platform.runLater(()->ser.getData().add(new XYChart.Data<String, Number>(t, v)));
    }
    public void SimpleAnomalyDetectorGraph(XYChart.Series ser,Float x,Float y){
        Platform.runLater(()->{
            ser.getData().add(new XYChart.Data(x, y));
            counter++;
            if(counter==30){
                ser.getData().clear();
                counter=0;
            }
        });

    }


}
