package view.graph;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.Set;


public class Mygraph extends AnchorPane {
    public FloatProperty listvalue,corvalue,radius;
    public IntegerProperty time;
    public StringProperty righttitle,lefttitle,type,algname;
    public BooleanProperty isred;
    @FXML
    public LineChart leftgraph;
    @FXML
    public LineChart rightgraph;
    public XYChart.Series series;
    public XYChart.Series series2;
    @FXML
    public ScatterChart paintGraph;
    public XYChart.Series seriesalg;
    public XYChart.Series liveseries;
    @FXML
    public Label cover;
    @FXML
    public NumberAxis linex;
    @FXML
    public NumberAxis liney;

    XYChart.Data p;
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
            this.algname=new SimpleStringProperty();
            this.isred=new SimpleBooleanProperty();
            this.cover=mygraphcontroller.cover;
            this.cover.setVisible(false);
            leftgraph=mygraphcontroller.leftgraph;
            rightgraph=mygraphcontroller.rightgraph;
            paintGraph=mygraphcontroller.paintGraph;
            mygraphcontroller.listvalue.bind(this.listvalue);
            mygraphcontroller.time.bind(this.time);
            series=new XYChart.Series();
            series2=new XYChart.Series();
            seriesalg=new XYChart.Series();
            liveseries=new XYChart.Series();
            this.liney= mygraphcontroller.liney;
            this.linex= mygraphcontroller.linex;
            liney.setAutoRanging(true);
            linex.setAutoRanging(true);
            Set<Node> linenodes = leftgraph.lookupAll(".series" + 0);
            for (Node n : linenodes ) {
                n.setStyle("-fx-background-color: #860061, white;\n"
                        + "    -fx-background-insets: 0, 2;\n"
                        + "    -fx-background-radius: 1px;\n"
                        + "    -fx-padding: 1px;");
            }
            leftgraph.setCreateSymbols(false);
            rightgraph.setCreateSymbols(false);
            this.leftgraph.getData().add(series);
            this.rightgraph.getData().add(series2);
            this.paintGraph.getData().add(liveseries);
            this.lefttitle.addListener((o,ov,nv)->{
                leftgraph.setTitle(this.lefttitle.getValue());
                leftgraph.setLegendVisible(false);
                rightgraph.setTitle(this.righttitle.getValue());
                rightgraph.setLegendVisible(false);

            });
            this.time.addListener((o,ov,nv)-> {
                mygraphcontroller.AddtoGraph(series, time.getValue().toString(), listvalue.getValue());
                mygraphcontroller.AddtoGraph(series2, time.getValue().toString(), corvalue.getValue());


                    });

            this.isred.addListener((o,ov,nv)->{
                if(isred.getValue()==true)
                {
                    paintGraph.setStyle("-fx-background-color: #44FFF3;");
                }
                else
                    paintGraph.setStyle("-fx-background-color: none;");
            });
            this.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
