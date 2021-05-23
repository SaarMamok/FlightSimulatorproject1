package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import properties.Settings;
import test.TimeSeries;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observable;

public class FGplayer extends Observable {
private  TimeSeries ts;
private Settings settings;
protected ActiveObjectCommon ao;
private float throttle;

    public FGplayer() {

    }

    public void setTs(TimeSeries ts) {
        this.ts = ts;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void play(){
        ao=new ActiveObjectCommon();
        try {
            Socket fg = new Socket(settings.getIp(),settings.getPort());
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
                this.throttle=ts.getDataTable().get(6).valuesList.get(i);
                this.setChanged();
                this.notifyObservers();
                ao.execute(()->{
                        out.println(l);
                        out.flush();
                });
                Thread.sleep(settings.getSleep());
            }
            out.close();
            fg.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }



    }

    public float getThrottle() {
        return throttle;
    }
}
