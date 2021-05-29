package view.graph;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import view.attlist.AttListController;

import java.io.IOException;

public class Mygraph extends Pane {
    public Mygraph() {
        super();

        try {
            FXMLLoader fxl = new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("Mygraph.fxml").openStream());
            Mygraphcontroller mygraphcontroller = fxl.getController();
            this.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
