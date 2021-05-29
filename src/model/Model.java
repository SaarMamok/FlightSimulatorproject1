package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import properties.Settings;
import test.TimeSeries;

import java.beans.XMLDecoder;
import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class Model extends Observable {
    private TimeSeries ts;
    private Settings prop;
     private FGplayer fGplayer;
    protected Thread theThread;
    private float throttle,rudder,elevators,aileron;
    private String altitude,speed,direction,roll,pitch,yaw;
    protected ActiveObjectCommon ao;
    public Model(){

        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new FileInputStream(new File("setting.xml")));
            this.prop= (Settings) decoder.readObject();
            decoder.close();
            fGplayer=new FGplayer();
            fGplayer.setSettings(prop);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public float getThrottle() {
        return this.throttle;
    }

    public void setTs(TimeSeries ts) {
        this.ts = ts;
        this.fGplayer.setTs(ts);
    }

    public void run() {
        theThread=new Thread(()->play());
        theThread.start();
    }
   public void play(){
        ao=new ActiveObjectCommon();
        try {
            Socket fg = new Socket(prop.getIp(),prop.getPort());
            fg.setSoTimeout(10000);
            PrintWriter out=new PrintWriter(fg.getOutputStream());
            int sizeline=ts.getDataTable().get(0).valuesList.size();
            int sizecol=ts.getDataTable().size();
            ao.start();
            for(int i=0;i<sizeline;i++){
                String line="";
                for(int j=0;j<sizecol;j++){
                    line+=ts.getDataTable().get(j).valuesList.get(i).toString();
                    line+=",";
                }
                final String l=line;
                this.throttle=ts.getDataTable().get(prop.getProp().get("throttle")).valuesList.get(i);
                this.rudder=ts.getDataTable().get(prop.getProp().get("rudder")).valuesList.get(i);
                this.aileron=ts.getDataTable().get(prop.getProp().get("aileron")).valuesList.get(i);
                this.elevators=ts.getDataTable().get(prop.getProp().get("elevator")).valuesList.get(i);

                this.yaw=ts.getDataTable().get(prop.getProp().get("side-slip-deg")).valuesList.get(i).toString();
                this.altitude=ts.getDataTable().get(prop.getProp().get("altimeter_indicated-altitude-ft")).valuesList.get(i).toString();
                this.speed=ts.getDataTable().get(prop.getProp().get("airspeed-indicator_indicated-speed-kt")).valuesList.get(i).toString();
                this.direction=ts.getDataTable().get(prop.getProp().get("indicated-heading-deg")).valuesList.get(i).toString();
                this.roll=ts.getDataTable().get(prop.getProp().get("attitude-indicator_indicated-roll-deg")).valuesList.get(i).toString();
                this.pitch=ts.getDataTable().get(prop.getProp().get("attitude-indicator_internal-pitch-deg")).valuesList.get(i).toString();
                this.setChanged();
                this.notifyObservers();
                ao.execute(()->{
                    out.println(l);
                    out.flush();
                });
                Thread.sleep(prop.getSleep());
            }
            out.close();
            fg.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }



    }

    public float getRudder() {
        return rudder;
    }

    public String getAltitude() {
        return altitude;
    }

    public String getSpeed() {
        return speed;
    }

    public String getDirection() {
        return direction;
    }

    public String getRoll() {
        return roll;
    }

    public String getPitch() {
        return pitch;
    }

    public String  getYaw() {
        return yaw;
    }

    public float getElevators() {
        return elevators;
    }

    public float getAileron() {
        return aileron;
    }
}
