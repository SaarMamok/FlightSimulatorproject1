package properties;

import java.io.Serializable;
import java.util.HashMap;

public class Settings implements Serializable {
    private String ip;
    private int port;
    private int sleep;
    private HashMap<String,Integer> prop=new HashMap<>();
    private String Learnpath;
    private String algpath;

    public Settings() {
    }

    public String getAlgpath() {
        return algpath;
    }

    public void setAlgpath(String algpath) {
        this.algpath = algpath;
    }

    public String getLearnpath() {
        return Learnpath;
    }

    public void setLearnpath(String learnpath) {
        Learnpath = learnpath;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public HashMap<String, Integer> getProp() {
        return prop;
    }

    public void setProp(HashMap<String, Integer> prop) {
        this.prop = prop;
    }

    public void DefaultProperties(){
        this.ip="localhost";
        this.port=5400;
        this.sleep=1000;
        this.Learnpath="./reg_flight.csv";
        this.algpath="./test/Algoritms/";
        prop.put("aileron",0);
        prop.put("elevator",1);
        prop.put("rudder",2);
        prop.put("throttle",6);
        prop.put("altimeter_indicated-altitude-ft",25);
        prop.put("airspeed-indicator_indicated-speed-kt",24);
        prop.put("indicated-heading-deg",36);
        prop.put("attitude-indicator_indicated-roll-deg",28);
        prop.put("attitude-indicator_internal-pitch-deg",29);
        prop.put("side-slip-deg",20);
    }

}
