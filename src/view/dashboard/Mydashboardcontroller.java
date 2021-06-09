package view.dashboard;

import eu.hansolo.medusa.Gauge;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class Mydashboardcontroller {
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

    public Mydashboardcontroller() {

    }



}
