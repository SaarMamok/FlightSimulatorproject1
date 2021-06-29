package model;

import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import properties.Settings;
import test.Algoritms.Hybrid;
import test.Algoritms.Zscore;
import test.Point;
import test.Algoritms.SimpleAnomalyDetector;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;

import java.awt.*;
import java.beans.XMLDecoder;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Model extends Observable {
    private int rate,ratedisplay;

    private TimeSeries ts;
    private Settings prop;
    protected Thread theThread;
    private int time,corindex,check=0;
    private int localtime=0;
    public float throttle,rudder,elevators,aileron,
            listvalue,corvalue;
    public double altitude,speed,direction,roll,pitch,yaw;
    private String leftval,rightval,detectorname;
    private SimpleAnomalyDetector.Pointanomaly p;
    public StringProperty type,algname;
    protected ActiveObjectCommon ao;
    private TimeSeriesAnomalyDetector ta;
    private TimeSeries learnTimeSeries;
    public BooleanProperty iscor;
    private  XMLDecoder decoder = null;
    public IntegerProperty index;
    private XYChart.Series series;
    @FXML
    private ScatterChart <Number,Number>scatterChart;
    NumberAxis xaxis=new NumberAxis();
    NumberAxis yaxis=new NumberAxis();
    private boolean isred=false;
    public Model(){
        index=new SimpleIntegerProperty();
        type=new SimpleStringProperty();
        series=new XYChart.Series();

        algname=new SimpleStringProperty();
        iscor=new SimpleBooleanProperty();
        scatterChart=new ScatterChart<Number, Number>(xaxis,yaxis);
        scatterChart.getData().add(series);
        this.Changexml("setting.xml");
        this.index.addListener((o,ov,nv)->{


            //this.scatterChart.getData().clear();
        });
    }

    public void Changexml(String path){
        try {
            decoder = new XMLDecoder(new FileInputStream(new File(path)));
            this.prop= (Settings) decoder.readObject();
            decoder.close();
            this.rate=prop.getSleep();
            this.ratedisplay=rate;
            this.detectorname="";
            this.learnTimeSeries = new TimeSeries(prop.getLearnpath());
            this.algname.addListener((o,ov,nv)->{
                series=new XYChart.Series();
                this.series.getData().clear();
                series.setName("Detect");
                scatterChart.getData().add(series);
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public int getIndex() {
        return index.get();
    }

    public IntegerProperty indexProperty() {
        return index;
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public float getThrottle() {
        return this.throttle;
    }

    public void setTs(TimeSeries ts) {
        this.ts = ts;
    }

    public void run() {
        theThread=new Thread(()-> {
            try {
                play(this.rate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        theThread.start();
    }

    public void stop(){

        this.localtime=0;
        this.throttle=ts.getDataTable().get(prop.getProp().get("throttle")).valuesList.get(localtime);
        this.rudder=ts.getDataTable().get(prop.getProp().get("rudder")).valuesList.get(localtime);
        this.aileron=ts.getDataTable().get(prop.getProp().get("aileron")).valuesList.get(localtime);
        this.elevators=ts.getDataTable().get(prop.getProp().get("elevator")).valuesList.get(localtime);
        this.yaw=ts.getDataTable().get(prop.getProp().get("side-slip-deg")).valuesList.get(localtime);
        this.altitude=ts.getDataTable().get(prop.getProp().get("altimeter_indicated-altitude-ft")).valuesList.get(localtime);
        this.speed=ts.getDataTable().get(prop.getProp().get("airspeed-indicator_indicated-speed-kt")).valuesList.get(localtime);
        this.direction=ts.getDataTable().get(prop.getProp().get("indicated-heading-deg")).valuesList.get(localtime);
        this.roll=ts.getDataTable().get(prop.getProp().get("attitude-indicator_indicated-roll-deg")).valuesList.get(localtime);
        this.pitch=ts.getDataTable().get(prop.getProp().get("attitude-indicator_internal-pitch-deg")).valuesList.get(localtime);
        this.time=localtime;
        this.setChanged();
        this.notifyObservers();
        theThread.interrupt();
    }

    public void pause(){
        theThread.interrupt();
    }

    public void forward(){
        if(this.rate-250>0) {
            this.rate -= 250;
            this.ratedisplay+=250;
        }
    }

    public void backward(){
        this.rate+=250;
        this.ratedisplay-=250;
    }

    public void doubleforward() {
        if(this.rate-500>0) {
            this.rate -= 500;
            this.ratedisplay+=500;
        }
    }

    public void doublebackward() {
        this.rate+=500;
        this.ratedisplay-=500;
    }

    public void SetAnomaly(Class<?> c) throws IllegalAccessException, InstantiationException {
        ta=(TimeSeriesAnomalyDetector)c.newInstance();
        ta.learnNormal(learnTimeSeries);
        ta.detect(this.ts);
        algname.setValue(ta.getname());
        this.index.addListener((o, ov, nv)->{
            iscor.setValue(ta.Paintlearn(ts,index.getValue(),scatterChart));
            series=new XYChart.Series();
            this.series.getData().clear();
            series.setName("Detect");
            scatterChart.getData().add(series);
        });
    }

   public void play(int r) throws InterruptedException {
       ao = new ActiveObjectCommon();

       int sizeline = ts.getDataTable().get(0).valuesList.size();
       int sizecol = ts.getDataTable().size();
       Socket fg = null;
       PrintWriter out = null;
       try {
           fg = new Socket(prop.getIp(), prop.getPort());
           fg.setSoTimeout(10000);
           out = new PrintWriter(fg.getOutputStream());

       } catch (SocketException e) {

       } catch (UnknownHostException e) {

       } catch (IOException e) {

       }
       ao.start();
       while (localtime < sizeline) {

           String line = "";
           for (int j = 0; j < sizecol; j++) {
               line += ts.getDataTable().get(j).valuesList.get(localtime).toString();
               line += ",";
           }
           final String l = line;
           this.throttle = ts.getDataTable().get(prop.getProp().get("throttle")).valuesList.get(localtime);
           this.rudder = ts.getDataTable().get(prop.getProp().get("rudder")).valuesList.get(localtime);
           this.aileron = ts.getDataTable().get(prop.getProp().get("aileron")).valuesList.get(localtime);
           this.elevators = ts.getDataTable().get(prop.getProp().get("elevator")).valuesList.get(localtime);
           this.yaw = ts.getDataTable().get(prop.getProp().get("side-slip-deg")).valuesList.get(localtime);
           this.altitude = ts.getDataTable().get(prop.getProp().get("altimeter_indicated-altitude-ft")).valuesList.get(localtime);
           this.speed = ts.getDataTable().get(prop.getProp().get("airspeed-indicator_indicated-speed-kt")).valuesList.get(localtime);
           this.direction = ts.getDataTable().get(prop.getProp().get("indicated-heading-deg")).valuesList.get(localtime);
           this.roll = ts.getDataTable().get(prop.getProp().get("attitude-indicator_indicated-roll-deg")).valuesList.get(localtime);
           this.pitch = ts.getDataTable().get(prop.getProp().get("attitude-indicator_internal-pitch-deg")).valuesList.get(localtime);
           this.time = localtime;
           this.listvalue = ts.getDataTable().get(index.getValue()).valuesList.get(time);
           this.corvalue = ts.getDataTable().get(corindex).valuesList.get(time);
           this.leftval = ts.getDataTable().get(index.getValue()).featureName;
           this.rightval = ts.getDataTable().get(corindex).featureName;
           if(iscor.getValue()==true){
               isred= ta.Paintdetect(series,index.getValue(),time);
           }

           this.setChanged();
           this.notifyObservers();


           localtime++;

           PrintWriter finalOut = out;
           ao.execute(() -> {
               if (finalOut != null) {
                   finalOut.println(l);
                   finalOut.flush();
               }
           });

               Thread.sleep(this.rate);


       }
       if (out != null)
           out.close();
   }


    public String getLeftval() {
        return leftval;
    }

    public String getRightval() {
        return rightval;
    }

    public float getListvalue() {
        return listvalue;
    }

    public float getRudder() {
        return rudder;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDirection() {
        return direction;
    }

    public double getRoll() {
        return roll;
    }

    public double getPitch() {
        return pitch;
    }

    public double  getYaw() {
        return yaw;
    }

    public float getElevators() {
        return elevators;
    }

    public float getAileron() {
        return aileron;
    }

    public int getTime() {
        return time;
    }


    public void setCorindex(int corindex) {
        this.corindex = corindex;
    }

    public float getCorvalue() {
        return corvalue;
    }

    public int getRate() {
        return rate;
    }

    public int getRatedisplay() {
        return ratedisplay;
    }

    public void slidermove(double t) {this.localtime=(int)t; }

    public Settings getProp() {
        return prop;
    }

    public SimpleAnomalyDetector.Pointanomaly getP() {
        return p;
    }

    public int getCheck() {
        return check;
    }


    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public TimeSeries getLearnTimeSeries() {
        return learnTimeSeries;
    }

    public XYChart.Series getSeries() {
        return series;
    }

    public ScatterChart<Number, Number> getScatterChart() {
        return scatterChart;
    }

    public boolean isIsred() {
        return isred;
    }
}
