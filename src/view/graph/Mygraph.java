package view.graph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.attlist.AttListController;

import java.io.IOException;

public class Mygraph extends Pane {
    public FloatProperty listvalue;
    public DoubleProperty time;
    public Mygraph() {
        super();

        try {
            FXMLLoader fxl = new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("Mygraph.fxml").openStream());
            Mygraphcontroller mygraphcontroller = fxl.getController();
            this.listvalue=new SimpleFloatProperty();
            this.time= new SimpleDoubleProperty();
            mygraphcontroller.listvalue.bind(this.listvalue);
            mygraphcontroller.time.bind(this.time);
            this.time.addListener((o,ov,nv)->mygraphcontroller.AddtoGraph());

            this.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
