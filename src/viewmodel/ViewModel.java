package viewmodel;


import javafx.application.Platform;
import javafx.beans.property.*;
import model.Model;
import test.TimeSeries;

import java.util.Observable;
import java.util.Observer;

public class ViewModel extends Observable implements Observer {
  private TimeSeries ts;
  private  Model model;

  public DoubleProperty aileron,elevators,rudder,throttle;
  public StringProperty altitude,speed,direction,roll,pitch,yaw;
  public IntegerProperty time;


  public ViewModel(Model m){
    this.model=m;
    m.addObserver(this);
    aileron=new SimpleDoubleProperty();
    elevators=new SimpleDoubleProperty();
    rudder=new SimpleDoubleProperty();
    throttle=new SimpleDoubleProperty();
    altitude=new SimpleStringProperty();
    speed=new SimpleStringProperty();
    direction=new SimpleStringProperty();
    roll=new SimpleStringProperty();
    pitch=new SimpleStringProperty();
    yaw=new SimpleStringProperty();
  time=new SimpleIntegerProperty();

    }
    public DoubleProperty getAileron(){
    return this.aileron;
    }
  public DoubleProperty getElevators(){
    return this.elevators;
  }
  public DoubleProperty getRudder(){
    return this.rudder;
  }
  public DoubleProperty getThrottle(){
    return this.throttle;
  }

  public int setTs(TimeSeries ts) {
    this.ts = ts;
    this.model.setTs(ts);
    return ts.Timer();

  }

  public void play(){
    this.model.run();
  }

  @Override
    public void update(Observable o, Object arg) {
        if(o==this.model){
          Platform.runLater(()->{
            this.time.set(this.model.getTime());

            this.throttle.set((this.model.getThrottle()));
            this.rudder.set((this.model.getRudder()));
            this.elevators.set((this.model.getElevators()));
            this.aileron.set(this.model.getAileron());


            this.yaw.set(this.model.getYaw());
            this.roll.set(this.model.getRoll());
            this.pitch.set(this.model.getPitch());
            this.altitude.set(this.model.getAltitude());
            this.speed.set(this.model.getSpeed());
            this.direction.set(this.model.getDirection());

          });
        }
    }
}
