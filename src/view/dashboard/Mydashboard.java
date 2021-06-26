package view.dashboard;

import eu.hansolo.medusa.Gauge;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;


public class Mydashboard extends AnchorPane {
    public DoubleProperty altitude,speed,direction,roll,pitch,yaw;
    @FXML
    Gauge speedgauge;
    @FXML
    Gauge altitudegauge;
    @FXML
    Gauge directiongauge;
    @FXML
    Gauge rollgauge;
    @FXML
    Gauge pitchgauge;
    @FXML
    Gauge yawgauge;

    public Mydashboard(){
        super();

        try {
            FXMLLoader fxl=new FXMLLoader();
            AnchorPane root = fxl.load(getClass().getResource("Mydashboard.fxml").openStream());
            Mydashboardcontroller  mydashboardcontroller=fxl.getController();
            altitude=new SimpleDoubleProperty();
            speed=new SimpleDoubleProperty();
            direction=new SimpleDoubleProperty();
            roll=new SimpleDoubleProperty();
            pitch=new SimpleDoubleProperty();
            yaw=new SimpleDoubleProperty();
            mydashboardcontroller.altitudegauge.valueProperty().bind(altitude);
            mydashboardcontroller.speedgauge.valueProperty().bind(speed);
            mydashboardcontroller.directiongauge.valueProperty().bind(direction);
            mydashboardcontroller.rollgauge.valueProperty().bind(roll);
            mydashboardcontroller.pitchgauge.valueProperty().bind(pitch);
            mydashboardcontroller.yawgauge.valueProperty().bind(yaw);

            this.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
