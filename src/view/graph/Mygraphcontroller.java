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
    LineChart <String,Number> leftgraph;
    @FXML
    LineChart rightgraph;
    public FloatProperty listvalue;
    public IntegerProperty time;
    XYChart.Series<String,Number>series;
    final int size=10;
    public Mygraphcontroller() {
        listvalue=new SimpleFloatProperty();
        time=new SimpleIntegerProperty();
        series=new XYChart.Series<String,Number>();

    }





    public void AddtoGraph() {
        String s=time.getValue().toString();
        float f = listvalue.getValue();
              Platform.runLater(()->series.getData().add(new XYChart.Data<String, Number>(s, f)));
                leftgraph.getData().add(series);
                if(series.getData().size()>size)
                    series.getData().remove(0);

    }


}
