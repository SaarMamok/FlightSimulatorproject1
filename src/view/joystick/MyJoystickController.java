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
       //GraphicsContext gc= smallcircle.getGraphicsContext2D();

        mx=smallcircle.getRadius()/2;
        my=smallcircle.getRadius()/2;

        smallcircle.setCenterX(bigcircle.getCenterX()+(elevators.getValue()*smallcircle.getRadius()));
        smallcircle.setCenterY(bigcircle.getCenterY()+(aileron.getValue()*smallcircle.getRadius()));
        //mx=smallcircle.getRadius()/2;

       // my=smallcircle.getRadius()/2;
        //gc.clearRect(0,0,smallcircle.getWidth(),smallcircle.getHeight());
        //gc.clearRect(0,0,smallcircle.getRadius(),smallcircle.getRadius());
        //gc.strokeOval(my+ aileron.getValue()*61,mx+elevators.getValue()*61,61,61);

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
