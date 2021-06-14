package viewmodel;


import javafx.application.Platform;
import javafx.beans.property.*;
import model.Model;
import test.SimpleAnomalyDetector;
import test.StatLib;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;


import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import static java.lang.Math.abs;


public class ViewModel extends Observable implements Observer {
  private TimeSeries ts;
  private  Model model;

  public StringProperty leftval,rightval,Algname;
  public DoubleProperty altitude,speed,direction,roll,pitch,yaw,aileron,elevators,rudder,throttle;
  public IntegerProperty index,corindex,time;
  public FloatProperty listvalue,corvalue,rate,x1line,x2line,y1line,y2line,zvalue;

  private HashMap<Integer,Integer> Hashcor=new HashMap<>();

  public ViewModel(Model m){
    this.model=m;
    m.addObserver(this);
    aileron=new SimpleDoubleProperty();
    elevators=new SimpleDoubleProperty();
    rudder=new SimpleDoubleProperty();
    throttle=new SimpleDoubleProperty();
    altitude=new SimpleDoubleProperty();
    speed=new SimpleDoubleProperty();
    direction=new SimpleDoubleProperty();
    roll=new SimpleDoubleProperty();
    pitch=new SimpleDoubleProperty();
    yaw=new SimpleDoubleProperty();
  time=new SimpleIntegerProperty();
  index=new SimpleIntegerProperty();
  listvalue=new SimpleFloatProperty();
  corvalue=new SimpleFloatProperty();
  corindex=new SimpleIntegerProperty();
  rate=new SimpleFloatProperty();
  leftval=new SimpleStringProperty();
  rightval=new SimpleStringProperty();
  Algname=new SimpleStringProperty();
  x1line=new SimpleFloatProperty();
  x2line=new SimpleFloatProperty();
  y1line=new SimpleFloatProperty();
  y2line=new SimpleFloatProperty();
  zvalue=new SimpleFloatProperty();
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
    this.setCor(ts);
    return ts.Timer();
  }
  public void ChooseAlg() throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
    String input,className;
    input= JOptionPane.showInputDialog(null,"enter a class directory");
    className=JOptionPane.showInputDialog(null,"enter the class name");
// load class directory
    URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] {
            new URL("file://"+input)
    });
    Class<?> c=urlClassLoader.loadClass(className);
    Algname.setValue(c.getName());
    System.out.println(Algname.getValue());
    model.SetAnomaly(c);

  }
  public void setCor(TimeSeries ts) {
    float CurrentCorrlation;
    float bestCor;
    int i, j;
    int index = 0;
    int numOfFeatures = ts.getDataTable().size();
    for (i = 0; i < (numOfFeatures - 1); i++) {
      bestCor = 0;
      for (j = i + 1; j < numOfFeatures; j++) {
        float value = StatLib.pearson(SimpleAnomalyDetector.ListToArray(ts.getDataTable().get(i).valuesList), SimpleAnomalyDetector.ListToArray(ts.getDataTable().get(j).valuesList));
        CurrentCorrlation = abs(value);
        if (CurrentCorrlation > bestCor) //// I was changed from >= to >
        {
          index = j;
          bestCor = CurrentCorrlation;
        }
      }
      Hashcor.put(i,index);
    }
  }
  public void play(){
    this.model.run();
  }
  public void stop(){this.model.stop();}
  public void pause(){this.model.pause();}
  public void forward(){this.model.forward();}
  public void backward() { this.model.backward();}
  public void doubleforward() {this.model.doubleforward();
  }

  public void doublebackward() {
    this.model.doublebackward();
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

            this.model.setIndex(this.index.getValue());
            this.model.setCorindex(Hashcor.get(index.getValue()));
            this.listvalue.set(this.model.getListvalue());
            this.corvalue.set(this.model.getCorvalue());

            this.rate.set(this.model.getRatedisplay()/1000f);

            this.yaw.set(this.model.getYaw());
            this.roll.set(this.model.getRoll());
            this.pitch.set(this.model.getPitch());
            this.altitude.set(this.model.getAltitude());
            this.speed.set(this.model.getSpeed());
            this.direction.set(this.model.getDirection());

            this.leftval.set(this.model.getLeftval());
            this.rightval.set(this.model.getRightval());
          if(Algname.getValue().compareTo("test.SimpleAnomalyDetector")==0) {
            this.x1line.setValue(this.model.getX1line());
            this.x2line.setValue(this.model.getX2line());
            this.y1line.setValue(this.model.getY1line());
            this.y2line.setValue(this.model.getY2line());
          }
          else if(Algname.getValue().compareTo("test.Algoritms.Zscore")==0)
            this.zvalue.setValue(this.model.getZvalue());

          });
        }
       /* else{
          this.time.set(this.model.getTime());
        }*/
    }


  public void slidermove(double t) {
    this.model.slidermove(t);
  }
}
