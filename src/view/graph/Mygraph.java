package view.graph;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.attlist.AttListController;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.Set;

public class Mygraph extends AnchorPane {
    public FloatProperty listvalue,x1line,x2line,y1line,y2line,zvalue,zanomalyvalue,corvalue,px,py,cx,cy,radius,welzlx,welzly;
    public IntegerProperty time,check;
    public StringProperty righttitle,Algname,lefttitle,type;
    public BooleanProperty abberant;
    @FXML
    public LineChart leftgraph;
    @FXML
    public LineChart rightgraph;
    @FXML
    public ScatterChart<Number,Number> algo;
    @FXML
    public ScatterChart<Number,Number> welzel;
    @FXML
    public LineChart linegraph;
    @FXML
    public LineChart zscoregraph;
    public XYChart.Series <Number,Number>algoseries;
    public XYChart.Series series;
    public XYChart.Series series2;
    public XYChart.Series welzelpoints;
    public XYChart.Series welzelcircle;
    public XYChart.Series seriesline;
    public XYChart.Series zscoreseries;
    public XYChart.Series zscoreanomalyseries;
    public XYChart.Series detectlinegraph;


    @FXML
    NumberAxis linex;
    @FXML
    NumberAxis liney;
    @FXML
    Label cover;

    public Mygraph() {
        super();

        try {

            FXMLLoader fxl = new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("Mygraph.fxml").openStream());
            Mygraphcontroller mygraphcontroller = fxl.getController();
            this.cover=mygraphcontroller.cover;
            this.cover.setVisible(false);
            this.listvalue=new SimpleFloatProperty();
            this.time= new SimpleIntegerProperty();
            this.corvalue=new SimpleFloatProperty();
            this.lefttitle=new SimpleStringProperty();
            this.righttitle=new SimpleStringProperty();
            this.Algname=new SimpleStringProperty();
            this.zvalue=new SimpleFloatProperty();
            this.zanomalyvalue=new SimpleFloatProperty();
            this.type=new SimpleStringProperty();
            cx=new SimpleFloatProperty();
            cy=new SimpleFloatProperty();
            radius=new SimpleFloatProperty();
            welzlx=new SimpleFloatProperty();
            welzly=new SimpleFloatProperty();
            check=new SimpleIntegerProperty();
            px=new SimpleFloatProperty();
            py=new SimpleFloatProperty();
            abberant=new SimpleBooleanProperty();
            x1line=new SimpleFloatProperty();
            x2line=new SimpleFloatProperty();
            y1line=new SimpleFloatProperty();
            y2line=new SimpleFloatProperty();
            leftgraph=mygraphcontroller.leftgraph;
            rightgraph=mygraphcontroller.rightgraph;
            algo=mygraphcontroller.algo;
            welzel= mygraphcontroller.welzel;
            linegraph=mygraphcontroller.linegraph;
            zscoregraph=mygraphcontroller.zscoregraph;
            linex= mygraphcontroller.linex;
            liney= mygraphcontroller.liney;
            linex.setAutoRanging(false);
            liney.setAutoRanging(false);
            mygraphcontroller.listvalue.bind(this.listvalue);
            mygraphcontroller.time.bind(this.time);
            series=new XYChart.Series();
            series2=new XYChart.Series();
            welzelcircle=new XYChart.Series();
            welzelpoints=new XYChart.Series();
            algoseries =new XYChart.Series();
            seriesline=new XYChart.Series();
            zscoreseries=new XYChart.Series();
            zscoreanomalyseries=new XYChart.Series();
            detectlinegraph=new XYChart.Series();
            this.linegraph.getData().add(seriesline);
            this.zscoregraph.getData().add(zscoreseries);
            this.zscoregraph.getData().add(zscoreanomalyseries);
            this.welzel.getData().add(welzelcircle);
            this.welzel.getData().add(welzelpoints);
            welzel.setVisible(false);
            this.lefttitle.addListener((o,ov,nv)->series.setName(this.lefttitle.getValue()));
            this.righttitle.addListener((o,ov,nv)-> {
                series2.setName(this.righttitle.getValue());

            });
            this.leftgraph.getData().add(series);
            this.rightgraph.getData().add(series2);

            this.algo.getData().add(algoseries);

            this.algo.getData().add(detectlinegraph);
            this.lefttitle.addListener((o,ov,nv)->{
                Platform.runLater(()-> {
                    seriesline.getData().add(new XYChart.Data(this.x1line.getValue(), this.y1line.getValue()));
                    seriesline.getData().add(new XYChart.Data(this.x2line.getValue(), this.y2line.getValue()));

                });
            });
            Set<Node> nodes = welzel.lookupAll(".series" + 0);
            for (Node n : nodes) {
                n.setStyle("-fx-background-color: #860061, black;\n"
                        + "    -fx-background-insets: 0, 2;\n"
                        + "    -fx-background-radius: 1px;\n"
                        + "    -fx-padding: 1px;");
            }

            this.time.addListener((o,ov,nv)-> {
                mygraphcontroller.AddtoGraph(series,time.getValue().toString(),listvalue.getValue());
                mygraphcontroller.AddtoGraph(series2,time.getValue().toString(),corvalue.getValue());
                        if(Algname.getValue().compareTo("test.SimpleAnomalyDetector")==0) {
                            if (check.getValue() == -1) {
                                cover.setVisible(true);
                            } else
                                cover.setVisible(false);
                            zscoregraph.setVisible(false);
                            linegraph.setVisible(true);
                            algo.setVisible(true);
                            welzel.setVisible(false);
                            mygraphcontroller.SimpleAnomalyDetectorGraph(algoseries, listvalue.getValue(), corvalue.getValue());

                            mygraphcontroller.SimpleAnomalyDetectorGraph(detectlinegraph, px.getValue(), py.getValue());



                            /*  if(abberant.getValue()==true){
                               this.algoseries.getNode().setStyle("-fx-stroke: #ff0000;");
                               this.algoseries.getNode().setStyle("-fx-background-color: #ff0000");
                               System.out.println("jjjjj");
                           }
                        */


                        }
                        else if(Algname.getValue().compareTo("test.Algoritms.Zscore")==0)
                        {
                            zscoregraph.setVisible(true);
                            linegraph.setVisible(false);
                            algo.setVisible(false);
                            cover.setVisible(false);
                            welzel.setVisible(false);
                            //System.out.println(zvalue.getValue());
                            mygraphcontroller.ZscoreGraphadd(zscoreseries,time.floatValue(),zvalue.getValue());
                            mygraphcontroller.ZscoreGraphadd(zscoreanomalyseries,time.floatValue(),zanomalyvalue.getValue());
                        }
                        else if(Algname.getValue().compareTo("test.Algoritms.Hybrid")==0){
                            cover.setVisible(false);
                                if(type.getValue().compareTo("l")==0){
                                    zscoregraph.setVisible(false);
                                    linegraph.setVisible(true);
                                    algo.setVisible(true);
                                    seriesline.setName(" ");
                                    detectlinegraph.setName("linear regression");
                                    welzel.setVisible(false);
                                    mygraphcontroller.SimpleAnomalyDetectorGraph(algoseries, listvalue.getValue(), corvalue.getValue());

                                    mygraphcontroller.SimpleAnomalyDetectorGraph(detectlinegraph,px.getValue(),py.getValue());
                                }
                                else if(type.getValue().compareTo("z")==0){
                                    zscoregraph.setVisible(true);
                                    zscoreseries.setName("Zscore");
                                    zscoreanomalyseries.setName(" ");
                                    linegraph.setVisible(false);
                                    algo.setVisible(false);
                                    cover.setVisible(false);
                                    welzel.setVisible(false);
                                    mygraphcontroller.ZscoreGraphadd(zscoreseries,time.floatValue(),zvalue.getValue());
                                    mygraphcontroller.ZscoreGraphadd(zscoreanomalyseries,time.floatValue(),zanomalyvalue.getValue());

                                }
                                else if((type.getValue().compareTo("w")==0)){
                                    zscoregraph.setVisible(false);
                                    linegraph.setVisible(false);
                                    algo.setVisible(false);
                                    cover.setVisible(false);
                                    welzel.setVisible(true);
                                    welzelcircle.setName("Welzel");

                                    mygraphcontroller.createCircle(welzelcircle,this.cx.getValue(),this.cy.getValue(),this.radius.getValue());
                                    mygraphcontroller.addWelzlpoints(welzelpoints,welzlx.getValue(),welzly.getValue());
                                }
                        }
                    });

            //this.time.addListener((o,ov,nv)->mygraphcontroller.AddtoGraph());
            this.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
