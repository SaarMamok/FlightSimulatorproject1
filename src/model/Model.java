package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import properties.Settings;
import test.Algoritms.Hybrid;
import test.Algoritms.Zscore;
import test.Point;
import test.SimpleAnomalyDetector;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;
import java.beans.XMLDecoder;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;


public class Model extends Observable {
    private int rate,ratedisplay;
    private TimeSeries ts;
    private Settings prop;
    protected Thread theThread;
    private int index,time,corindex,check=0;
    private int localtime=0;
    public float throttle,rudder,elevators,aileron,
            listvalue,corvalue,x1line,x2line,y1line,y2line,
            zvalue,zanomalyvalue,cx,cy,radius,welzlx,welzly;
    public double altitude,speed,direction,roll,pitch,yaw;
    private String leftval,rightval,detectorname;
    private SimpleAnomalyDetector.Pointanomaly p;
    public StringProperty type;
    protected ActiveObjectCommon ao;
    private TimeSeriesAnomalyDetector ta;
    private TimeSeries learnTimeSeries;
    Timer clock;


    public Model(){

        XMLDecoder decoder = null;
        clock=null;
        try {
            decoder = new XMLDecoder(new FileInputStream(new File("setting.xml")));
            this.prop= (Settings) decoder.readObject();
            decoder.close();
            this.rate=prop.getSleep();
            this.ratedisplay=rate;
            this.detectorname="";
            this.learnTimeSeries = new TimeSeries(prop.getLearnpath());
            type=new SimpleStringProperty();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setIndex(int index) {
        this.index = index;
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

        if(c.getName().compareTo("test.SimpleAnomalyDetector")==0) {
            this.ta=(SimpleAnomalyDetector) c.newInstance();
            ta.learnNormal(learnTimeSeries);
            detectorname="SimpleAnomalyDetector";
            ta.detect(this.ts);
        }
        else if(c.getName().compareTo("test.Algoritms.Zscore")==0){
            this.ta = (Zscore) c.newInstance();
            ta.learnNormal(learnTimeSeries);
            detectorname="Zscore";
            ta.detect(this.ts);
        }
        else if(c.getName().compareTo("test.Algoritms.Hybrid")==0){
            this.ta = (Hybrid) c.newInstance();
            ta.learnNormal(learnTimeSeries);
            detectorname="Hybrid";
            ta.detect(this.ts);
        }
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
           this.listvalue = ts.getDataTable().get(index).valuesList.get(time);
           this.corvalue = ts.getDataTable().get(corindex).valuesList.get(time);
           this.leftval = ts.getDataTable().get(index).featureName;
           this.rightval = ts.getDataTable().get(corindex).featureName;

           if (detectorname.compareTo("SimpleAnomalyDetector") == 0) {
               check = ((SimpleAnomalyDetector) ta).getcorindex(index, ts);
               if (check != -1) {
                   this.y2line = ((SimpleAnomalyDetector) ta).getCorFeatures().get(check).lin_reg.f(-500);
                   this.x2line = -500;
                   this.y1line = ((SimpleAnomalyDetector) ta).getCorFeatures().get(check).lin_reg.f(500);
                   this.x1line = 500;

                   p = new SimpleAnomalyDetector.Pointanomaly(new Point(((SimpleAnomalyDetector) ta).getAnomalymap().get(check).get(localtime).getP().x,
                           ((SimpleAnomalyDetector) ta).getAnomalymap().get(check).get(localtime).getP().y),
                           ((SimpleAnomalyDetector) ta).getAnomalymap().get(check).get(localtime).isAberrant());
               }
           } else if (detectorname.compareTo("Zscore") == 0) {
               zvalue = ((Zscore) ta).getZhash().get(index).get(localtime);
               zanomalyvalue = ((Zscore) ta).getAnomalymap().get(index).get(localtime).getVal();
           } else if (detectorname.compareTo("Hybrid") == 0) {
               type.setValue(((Hybrid) ta).getCorvalues().get(index).getAlgo());
               int innerindex = ((Hybrid) ta).getCorvalues().get(index).getIndex();
               if (type.getValue().compareTo("l") == 0) {
                   this.y2line = ((Hybrid) ta).simple.get(innerindex).getCorFeatures().get(0).lin_reg.f(-500);
                   this.x2line = -500;
                   this.y1line = ((Hybrid) ta).simple.get(innerindex).getCorFeatures().get(0).lin_reg.f(500);
                   this.x1line = 500;
                   p = new SimpleAnomalyDetector.Pointanomaly(new Point(((Hybrid) ta).simple.get(innerindex).getAnomalymap().get(0).get(localtime).getP().x,
                           ((Hybrid) ta).simple.get(innerindex).getAnomalymap().get(0).get(localtime).getP().y),
                           ((Hybrid) ta).simple.get(innerindex).getAnomalymap().get(0).get(localtime).isAberrant());

               } else if (type.getValue().compareTo("z") == 0) {
                   zvalue = ((Hybrid) ta).zscorelist.get(innerindex).getZhash().get(0).get(localtime);
                   zanomalyvalue = ((Hybrid) ta).zscorelist.get(innerindex).getAnomalymap().get(0).get(localtime).getVal();
               } else if (type.getValue().compareTo("w") == 0) {
                   cx = ((Hybrid) ta).welzllist.get(innerindex).cir1.c.x;
                   cy = ((Hybrid) ta).welzllist.get(innerindex).cir1.c.y;
                   radius = (float) ((Hybrid) ta).welzllist.get(innerindex).cir1.r;
                   int index1 = ((Hybrid) ta).getHashvalues().get(((Hybrid) ta).welzllist.get(innerindex).getFeat1());
                   int index2 = ((Hybrid) ta).getHashvalues().get(((Hybrid) ta).welzllist.get(innerindex).getFeat2());
                   welzlx = ts.getDataTable().get(index1).valuesList.get(localtime);
                   welzly = ts.getDataTable().get(index2).valuesList.get(localtime);
               }
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

    public float getX1line() {
        return x1line;
    }

    public float getX2line() {
        return x2line;
    }

    public float getY1line() {
        return y1line;
    }

    public float getY2line() {
        return y2line;
    }

    public float getZvalue() {
        return zvalue;
    }

    public float getZanomalyvalue() {
        return zanomalyvalue;
    }

    public SimpleAnomalyDetector.Pointanomaly getP() {
        return p;
    }

    public int getCheck() {
        return check;
    }

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getRadius() {
        return radius;
    }

    public float getWelzlx() {
        return welzlx;
    }

    public float getWelzly() {
        return welzly;
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
}
