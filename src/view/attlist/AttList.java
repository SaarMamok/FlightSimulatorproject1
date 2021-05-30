package view.attlist;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import view.joystick.MyJoystickController;

import java.io.IOException;

public class AttList extends Pane {
    public IntegerProperty index;
    public AttList(){
        super();

        try {
            FXMLLoader fxl=new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("AttList.fxml").openStream());
            AttListController attListController=fxl.getController();
            attListController.init();

            index=new SimpleIntegerProperty();
            this.index.bind(attListController.index);
            this.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
