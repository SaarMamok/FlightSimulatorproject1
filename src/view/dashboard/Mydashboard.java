package view.dashboard;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;


import java.io.IOException;

public class Mydashboard extends Pane {
    public StringProperty altitude,speed,direction,roll,pitch,yaw;
    public Mydashboard(){
        super();

        try {
            FXMLLoader fxl=new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("Mydashboard.fxml").openStream());
            Mydashboardcontroller mydashboardcontroller=fxl.getController();
            altitude=new SimpleStringProperty();
            speed=new SimpleStringProperty();
            direction=new SimpleStringProperty();
            roll=new SimpleStringProperty();
            pitch=new SimpleStringProperty();
            yaw=new SimpleStringProperty();
            mydashboardcontroller.yaw.textProperty().bind(yaw);
            mydashboardcontroller.pitch.textProperty().bind(pitch);
            mydashboardcontroller.roll.textProperty().bind(roll);
            mydashboardcontroller.direction.textProperty().bind(direction);
            mydashboardcontroller.speed.textProperty().bind(speed);
            mydashboardcontroller.altitude.textProperty().bind(altitude);
            this.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
