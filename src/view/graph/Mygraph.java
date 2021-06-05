package view.graph;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.attlist.AttListController;

import java.io.IOException;

public class Mygraph extends Pane {
    public FloatProperty listvalue;
    public IntegerProperty time;
    @FXML
    public LineChart leftgraph;
    public XYChart.Series series;

    public Mygraph() {
        super();

        try {
            FXMLLoader fxl = new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("Mygraph.fxml").openStream());
            Mygraphcontroller mygraphcontroller = fxl.getController();
            this.listvalue=new SimpleFloatProperty();
            this.time= new SimpleIntegerProperty();
            leftgraph=mygraphcontroller.leftgraph;
            mygraphcontroller.listvalue.bind(this.listvalue);
            mygraphcontroller.time.bind(this.time);
            series=new XYChart.Series();
            this.leftgraph.getData().add(series);
            this.time.addListener((o,ov,nv)-> mygraphcontroller.AddtoGraph(series));
            //this.time.addListener((o,ov,nv)->mygraphcontroller.AddtoGraph());

            this.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
