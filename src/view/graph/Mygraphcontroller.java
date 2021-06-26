package view.graph;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;


public class Mygraphcontroller {

    @FXML
    LineChart leftgraph;
    @FXML
    LineChart rightgraph;
    @FXML
    ScatterChart<Number, Number> algo;
    @FXML
    public ScatterChart<Number,Number> welzel;
    @FXML
    LineChart linegraph;
    @FXML
    public LineChart zscoregraph;
    @FXML
    NumberAxis linex;
    @FXML
    NumberAxis liney;
    @FXML
    Label cover;
    public FloatProperty listvalue;
    public IntegerProperty time;
    final int size = 10;
    private int counter;

    public Mygraphcontroller() {
        listvalue = new SimpleFloatProperty();
        time = new SimpleIntegerProperty();
    }


    public void AddtoGraph(XYChart.Series ser, String t, Number v) {
        Platform.runLater(() -> ser.getData().add(new XYChart.Data<String, Number>(t, v)));
    }

    public void SimpleAnomalyDetectorGraph(XYChart.Series ser, Float x, Float y) {
        Platform.runLater(() -> {
            ser.getData().add(new XYChart.Data(x, y));
            counter++;
        });
    }

    public void ZscoreGraphadd(XYChart.Series ser, Float x, Float y) {
        Platform.runLater(() -> {
            ser.getData().add(new XYChart.Data(x, y));
        });
    }

    public void createCircle(XYChart.Series ser,float cx,float cy,float r){

        Platform.runLater(() -> {
            float x,y;
            for(int i=0;i<360;i++) {
                x= (float) (cx+(r*Math.cos(i)));
                y= (float) (cy+(r*Math.sin(i)));
                ser.getData().add(new XYChart.Data(x, y));
            }
        });
    }

    public void addWelzlpoints(XYChart.Series ser,float x,float y){
        Platform.runLater(() -> ser.getData().add(new XYChart.Data(x,y)));
    }
}
