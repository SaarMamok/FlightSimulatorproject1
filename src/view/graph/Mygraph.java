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
    public FloatProperty corvalue;
    public IntegerProperty time;
    public StringProperty lefttitle;
    public StringProperty righttitle;
    @FXML
    public LineChart leftgraph;
    @FXML
    public LineChart rightgraph;
    public XYChart.Series series;
    public XYChart.Series series2;

    public Mygraph() {
        super();

        try {
            FXMLLoader fxl = new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("Mygraph.fxml").openStream());
            Mygraphcontroller mygraphcontroller = fxl.getController();
            this.listvalue=new SimpleFloatProperty();
            this.time= new SimpleIntegerProperty();
            this.corvalue=new SimpleFloatProperty();
            this.lefttitle=new SimpleStringProperty();
            this.righttitle=new SimpleStringProperty();

            leftgraph=mygraphcontroller.leftgraph;
            rightgraph=mygraphcontroller.rightgraph;
            mygraphcontroller.listvalue.bind(this.listvalue);
            mygraphcontroller.time.bind(this.time);
            series=new XYChart.Series();
            series2=new XYChart.Series();
            this.lefttitle.addListener((o,ov,nv)->series.setName(this.lefttitle.getValue()));
            this.righttitle.addListener((o,ov,nv)->series2.setName(this.righttitle.getValue()));
            this.leftgraph.getData().add(series);
            this.rightgraph.getData().add(series2);
            this.time.addListener((o,ov,nv)-> {
                mygraphcontroller.AddtoGraph(series,time.getValue().toString(),listvalue.getValue());
                mygraphcontroller.AddtoGraph(series2,time.getValue().toString(),corvalue.getValue());
            });
            //this.time.addListener((o,ov,nv)->mygraphcontroller.AddtoGraph());

            this.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
