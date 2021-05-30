package view.graph;

import javafx.beans.property.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.attlist.AttListController;

import java.io.IOException;

public class Mygraph extends Pane {
    public FloatProperty listvalue;
    public IntegerProperty time;
    public Mygraph() {
        super();

        try {
            FXMLLoader fxl = new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("Mygraph.fxml").openStream());
            Mygraphcontroller mygraphcontroller = fxl.getController();
            this.listvalue=new SimpleFloatProperty();
            this.time= new SimpleIntegerProperty();
            mygraphcontroller.listvalue.bind(this.listvalue);
            mygraphcontroller.time.bind(this.time);
            this.time.addListener((o,ov,nv)->mygraphcontroller.AddtoGraph());

            this.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
