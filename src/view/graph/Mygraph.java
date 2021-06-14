package view.graph;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.attlist.AttListController;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.IOException;

public class Mygraph extends Pane {
    public FloatProperty listvalue,x1line,x2line,y1line,y2line,zvalue;
    public FloatProperty corvalue;
    public IntegerProperty time;
    public StringProperty lefttitle;
    public StringProperty righttitle,Algname;
    @FXML
    public LineChart leftgraph;
    @FXML
    public LineChart rightgraph;
    @FXML
    public ScatterChart<Number,Number> algo;
    @FXML
    public LineChart linegraph;
    @FXML
    public LineChart zscoregraph;
    public XYChart.Series <Number,Number>algoseries;
    public XYChart.Series series;
    public XYChart.Series series2;
    public XYChart.Series seriesline;
    public XYChart.Series zscoreseries;

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
            this.Algname=new SimpleStringProperty();
            this.zvalue=new SimpleFloatProperty();
            x1line=new SimpleFloatProperty();
            x2line=new SimpleFloatProperty();
            y1line=new SimpleFloatProperty();
            y2line=new SimpleFloatProperty();
            leftgraph=mygraphcontroller.leftgraph;
            rightgraph=mygraphcontroller.rightgraph;
            algo=mygraphcontroller.algo;
            linegraph=mygraphcontroller.linegraph;
            zscoregraph=mygraphcontroller.zscoregraph;
            mygraphcontroller.listvalue.bind(this.listvalue);
            mygraphcontroller.time.bind(this.time);
            series=new XYChart.Series();
            series2=new XYChart.Series();
            algoseries =new XYChart.Series();
            seriesline=new XYChart.Series();
            zscoreseries=new XYChart.Series();
            this.linegraph.getData().add(seriesline);
            this.zscoregraph.getData().add(zscoreseries);
            this.lefttitle.addListener((o,ov,nv)->series.setName(this.lefttitle.getValue()));
            this.righttitle.addListener((o,ov,nv)-> {
                series2.setName(this.righttitle.getValue());
                Platform.runLater(()-> {
                    seriesline.getData().add(new XYChart.Data(this.x1line.getValue(), this.y1line.getValue()));
                    seriesline.getData().add(new XYChart.Data(this.x2line.getValue(), this.y2line.getValue()));
                });
            });
            this.leftgraph.getData().add(series);
            this.rightgraph.getData().add(series2);
            this.algo.getData().add(algoseries);
            this.time.addListener((o,ov,nv)-> {
                mygraphcontroller.AddtoGraph(series,time.getValue().toString(),listvalue.getValue());
                mygraphcontroller.AddtoGraph(series2,time.getValue().toString(),corvalue.getValue());
                        if(Algname.getValue().compareTo("test.SimpleAnomalyDetector")==0)
                            mygraphcontroller.SimpleAnomalyDetectorGraph(algoseries,listvalue.getValue() ,corvalue.getValue() );
                        else if(Algname.getValue().compareTo("test.Algoritms.Zscore")==0)
                        {
                            linegraph.setVisible(false);
                            algo.setVisible(false);
                            mygraphcontroller.ZscoreGraphadd(zscoreseries,time.floatValue(),zvalue.getValue());
                        }
                    });

            //this.time.addListener((o,ov,nv)->mygraphcontroller.AddtoGraph());
            this.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
