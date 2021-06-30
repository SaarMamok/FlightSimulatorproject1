package viewmodel;


import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

import javafx.scene.control.Alert;
import model.Model;
import test.Algoritms.SimpleAnomalyDetector;
import test.StatLib;
import test.TimeSeries;
import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static java.lang.Math.abs;


public class ViewModel extends Observable implements Observer {

  private TimeSeries ts;
  private  Model model;
  public StringProperty leftval,rightval,Algname,type,xmlpath;
  public DoubleProperty altitude,speed,direction,roll,pitch,yaw,aileron,elevators,rudder,throttle;
  public IntegerProperty index,corindex,time,check;
  public FloatProperty listvalue,corvalue,rate;
  public BooleanProperty aberrant, iscor,isred;
  private HashMap<Integer,Integer> Hashcor=new HashMap<>();
  private XYChart.Series series;

  public ViewModel(Model m){
    this.model=m;
    m.addObserver(this);
    this.setCor(new TimeSeries("reg_flight.csv"));
    xmlpath=new SimpleStringProperty();
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
    aberrant=new SimpleBooleanProperty();
    check=new SimpleIntegerProperty();
    type=new SimpleStringProperty();
    iscor=new SimpleBooleanProperty();
    series=new XYChart.Series();
  isred=new SimpleBooleanProperty();
    this.xmlpath.addListener((o,ov,nv)->model.Changexml(this.xmlpath.getValue()));
    this.iscor.bind(this.model.iscor);
    this.Algname.bind(this.model.algname);
    this.index.addListener((o,ov,nv)->{
      if(iscor.getValue()==true) {
        series.setData(model.getSeries().getData());
      }
    });
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
    if(ts.getDataTable().size()!=model.getLearnTimeSeries().getDataTable().size()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("CSV file is not valid.");
      alert.show();
      this.ts = null;
      return -1;
    }
    else {
      this.model.setTs(ts);
      return ts.Timer();
    }
  }

  public void ChooseAlg() throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {

    String className,input;

    input=JOptionPane.showInputDialog(null,"Enter a class directory");
    while((input == "/0")||(input.compareTo("")==0))
      input= JOptionPane.showInputDialog(null,"It was wrong input please enter a class directory again");

    className=JOptionPane.showInputDialog(null,"Enter the class name");

    while(className.compareTo("")==0)
      className=JOptionPane.showInputDialog(null,"It was wrong input please enter the class name again");
    URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[] {
            new URL("file://"+input)
    });
    Class<?> c=urlClassLoader.loadClass(className);
    model.SetAnomaly(c);
  }

  public void setCor(TimeSeries ts) {
    float CurrentCorrlation;//commit
    float bestCor;
    int i, j;
    int index = 0;
    int numOfFeatures = ts.getDataTable().size();
    for (i = 0; i < numOfFeatures ; i++) {
      bestCor = 0;
      for (j = 0; j < numOfFeatures; j++) {
        if(i!=j) {
          float value = StatLib.pearson(SimpleAnomalyDetector.ListToArray(ts.getDataTable().get(i).valuesList), SimpleAnomalyDetector.ListToArray(ts.getDataTable().get(j).valuesList));
          CurrentCorrlation = abs(value);
          if (CurrentCorrlation > bestCor) //// I was changed from >= to >
          {
            index = j;
            bestCor = CurrentCorrlation;
          }
        }
      }
      Hashcor.put(i,index);
    }
  }
public boolean detectfunc(XYChart.Series series){
    return this.model.detectfunc(series);
}
  public void play(){
    if(this.ts!=null&&this.Algname.getValue()!=null)
        this.model.run();
  }
  public void stop(){
    if(this.ts!=null&&this.Algname.getValue()!=null)
      this.model.stop();}
  public void pause(){
    if(this.ts!=null&&this.Algname.getValue()!=null)
      this.model.pause();}
  public void forward(){this.model.forward();}
  public void backward() { this.model.backward();}
  public void doubleforward() {this.model.doubleforward(); }
  public void doublebackward() {
    this.model.doublebackward();
  }

  @Override
    public void update(Observable o, Object arg) {
        if(o==this.model){
          Platform.runLater(()-> {
            this.time.set(this.model.getTime());
            this.throttle.set((this.model.getThrottle()));
            this.rudder.set((this.model.getRudder()));
            this.elevators.set((this.model.getElevators()));
            this.aileron.set(this.model.getAileron());
            this.model.setIndex(this.index.getValue());
            this.model.setCorindex(Hashcor.get(index.getValue()));
            this.listvalue.set(this.model.getListvalue());
            this.corvalue.set(this.model.getCorvalue());
            this.rate.set(this.model.getRatedisplay() / 1000f);
            this.yaw.set(this.model.getYaw());
            this.roll.set(this.model.getRoll());
            this.pitch.set(this.model.getPitch());
            this.altitude.set(this.model.getAltitude());
            this.speed.set(this.model.getSpeed());
            this.direction.set(this.model.getDirection());
            this.leftval.set(this.model.getLeftval());
            this.rightval.set(this.model.getRightval());
            this.isred.set(this.model.isIsred());
          });
        }
    }

  public XYChart.Series getSeries() {
    return series;
  }

  public void slidermove(double t) {
    this.model.slidermove(t);
  }
  public ScatterChart<Number, Number> getScatterChart() {
    return model.getScatterChart();
  }
}
