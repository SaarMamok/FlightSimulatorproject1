package view.graph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class Mygraphcontroller {

    @FXML
    LineChart <String,Number> leftgraph;
    @FXML
    LineChart rightgraph;
    public FloatProperty listvalue;
    public DoubleProperty time;
    XYChart.Series<String,Number>series=new XYChart.Series<String,Number>();
    public Mygraphcontroller() {
        listvalue=new SimpleFloatProperty();
        time=new SimpleDoubleProperty();
    }




    public void AddtoGraph(){


        //dataSeries1.getData().add(new XYChart.Data(time.getValue(),listvalue.getValue()));
        series.getData().add(new XYChart.Data<String,Number>(time.getValue().toString(),listvalue.getValue()));

        leftgraph.getData().add(series);
    }


}
