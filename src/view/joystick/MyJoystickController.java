package view.joystick;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;



public class MyJoystickController{
    @FXML
    Slider throttle;
    @FXML
    Slider rudder;
    @FXML
    Circle bigcircle;
    @FXML
    Canvas smallcircle;
    @FXML
    Circle joy;

    public DoubleProperty aileron,elevators;
    double jx,jy,mx,my;



    public MyJoystickController() {
        aileron=new SimpleDoubleProperty();
        elevators=new SimpleDoubleProperty();
        jx=0;
        jy=0;

    }
    public void paint(){
        GraphicsContext gc= smallcircle.getGraphicsContext2D();
        mx=smallcircle.getWidth()/2;
        my=smallcircle.getHeight()/2;
        gc.clearRect(0,0,smallcircle.getWidth(),smallcircle.getHeight());
        gc.strokeOval(my+ aileron.getValue()*61,mx+elevators.getValue()*61,61,61);

        /*mx = smallcircle.get() / 2;
        my = smallcircle.getHeight() / 2;
        gc.clearRect(0, 0, smallcircle.getWidth(), smallcircle.getHeight());
        gc.strokeOval(aileron.getValue() - 50, elevators.getValue() - 50, 100, 100);
        // aileron=(jx-mx)/mx;
        //aileron.set((jx - mx) / mx);
        //elevator=(my-jy)/my;
        //elevators.set((my - jy) / my);*/
    }



}
