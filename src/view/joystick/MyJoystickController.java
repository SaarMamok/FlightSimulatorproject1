package view.joystick;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;


public class MyJoystickController{

    @FXML
    Slider throttle;
    @FXML
    Slider rudder;
    @FXML
    Circle bigcircle;
    @FXML
    Circle smallcircle;

    public DoubleProperty aileron,elevators;
    double jx,jy,mx,my;

    public MyJoystickController() {
        aileron=new SimpleDoubleProperty();
        elevators=new SimpleDoubleProperty();
        jx=0;
        jy=0;
    }

    public void paint(){
        mx=smallcircle.getRadius()/2;
        my=smallcircle.getRadius()/2;
        smallcircle.setCenterX(bigcircle.getCenterX()+(elevators.getValue()*smallcircle.getRadius()*2));
        smallcircle.setCenterY(bigcircle.getCenterY()+(aileron.getValue()*smallcircle.getRadius()*2));
    }
}
