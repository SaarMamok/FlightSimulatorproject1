package model;

import properties.Settings;
import test.SimpleAnomalyDetector;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;

import java.beans.XMLDecoder;
import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class Model extends Observable {
    private int rate,ratedisplay;
    private TimeSeries ts;
    private Settings prop;
    private FGplayer fGplayer;
    protected Thread theThread;
    private int index,time,corindex;
    private int localtime=0;
    private float throttle,rudder,elevators,aileron,listvalue,corvalue,x1line,x2line,y1line,y2line;
    private double altitude,speed,direction,roll,pitch,yaw;
    private String leftval,rightval;
    protected ActiveObjectCommon ao;
    public SimpleAnomalyDetector t;
    private TimeSeries learn;
    public Model(){

        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new FileInputStream(new File("setting.xml")));
            this.prop= (Settings) decoder.readObject();
            decoder.close();
            fGplayer=new FGplayer();
            fGplayer.setSettings(prop);
            this.rate=prop.getSleep();
            this.ratedisplay=rate;
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
        this.fGplayer.setTs(ts);
    }

    public void run() {
        theThread=new Thread(()->play(this.rate));
        theThread.start();
    }
    public void stop(){
        theThread.interrupt();
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
        this.t=(SimpleAnomalyDetector) c.newInstance();
        this.learn=new TimeSeries(prop.getLearnpath());
        t.learnNormal(learn);


    }
   public void play(int r){
        ao=new ActiveObjectCommon();
        try {
            Socket fg = new Socket(prop.getIp(),prop.getPort());
            fg.setSoTimeout(10000);
            PrintWriter out=new PrintWriter(fg.getOutputStream());
            int sizeline=ts.getDataTable().get(0).valuesList.size();
            int sizecol=ts.getDataTable().size();
            ao.start();
            while(localtime<sizeline){

                String line="";
                for(int j=0;j<sizecol;j++){
                    line+=ts.getDataTable().get(j).valuesList.get(localtime).toString();
                    line+=",";
                }
                final String l=line;
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

                this.listvalue=ts.getDataTable().get(index).valuesList.get(time);
                this.corvalue=ts.getDataTable().get(corindex).valuesList.get(time);
                this.leftval=ts.getDataTable().get(index).featureName;
                this.rightval=ts.getDataTable().get(corindex).featureName;



                this.y2line=t.getCorFeatures().get(index).lin_reg.f(-1);
                this.x2line=-1;
                this.y1line=t.getCorFeatures().get(index).lin_reg.f(1);
                this.x1line=1;

                this.setChanged();
                this.notifyObservers();
                ao.execute(()->{
                    out.println(l);
                    out.flush();
                });
                Thread.sleep(this.rate);
                localtime++;
            }
            out.close();
            fg.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }



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

    public void slidermove(double t) {this.localtime=(int)t;
    }

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
}
