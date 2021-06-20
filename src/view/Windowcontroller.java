package view;

import javafx.beans.property.*;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import test.TimeSeries;
import test.TimeSeriesAnomalyDetector;
import view.attlist.AttList;
import view.dashboard.Mydashboard;
import view.graph.Mygraph;
import view.joystick.MyJoystick;
import viewmodel.ViewModel;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Observable;

public class Windowcontroller extends Observable {


    @FXML
    Mydashboard mydashboard;
    @FXML
    MyJoystick myJoystick;
    @FXML
    AttList attributeslist;
    @FXML
    ImageView play;
    @FXML
    ImageView backwards;
    @FXML
    ImageView skipprev;
    @FXML
    ImageView pause;
    @FXML
    ImageView stop;
    @FXML
    ImageView fastforward;
    @FXML
    ImageView skipnext;
    @FXML
    Slider timebar;
    @FXML
    Mygraph mygraph;
    @FXML
    TextField speed;
    @FXML
    Label clock;

    private File chosen;
    public IntegerProperty time;
    public FloatProperty rate;
    ViewModel viewModel;
    float sec=0,min=0,hour=0;


    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public void init(ViewModel vm){


        timebar.setMin(0);
        time=new SimpleIntegerProperty();
        this.viewModel=vm;




        this.myJoystick.rudder.bind(this.viewModel.rudder);
        this.myJoystick.throttle.bind(this.viewModel.throttle);
        this.myJoystick.aileron.bind(this.viewModel.aileron);
        this.myJoystick.elevators.bind(this.viewModel.elevators);

        this.time.bind(this.viewModel.time);
        this.time.addListener((o,ov,nv)->{
            timebar.setValue(time.getValue());
            this.clock.setText(getDurationString(this.time.getValue()));
        });

        this.viewModel.index.bind(attributeslist.index);
        this.mygraph.lefttitle.bind(this.viewModel.leftval);
        this.mygraph.righttitle.bind(this.viewModel.rightval);
        this.attributeslist.index.addListener((o,ov,nv)->{
          //  System.out.println(this.attributeslist.index.getValue());
            this.mygraph.series.getData().clear();
            this.mygraph.series2.getData().clear();
            this.mygraph.seriesline.getData().clear();
            //this.mygraph.time.set(time.getValue());
            this.mygraph.algoseries.getData().clear();
            this.mygraph.detectlinegraph.getData().clear();
            this.mygraph.zscoreseries.getData().clear();
            this.mygraph.zscoreanomalyseries.getData().clear();
        });
        this.mygraph.listvalue.bind(this.viewModel.listvalue);
        this.mygraph.corvalue.bind(this.viewModel.corvalue);
        this.mygraph.time.bind(this.time);
        this.mygraph.Algname.bind(this.viewModel.Algname);
        this.mygraph.x1line.bind(this.viewModel.x1line);
        this.mygraph.x2line.bind(this.viewModel.x2line);
        this.mygraph.y1line.bind(this.viewModel.y1line);
        this.mygraph.y1line.bind(this.viewModel.y2line);
        this.mygraph.px.bind(this.viewModel.px);
        this.mygraph.py.bind(this.viewModel.py);
        this.mygraph.abberant.bind(this.viewModel.aberrant);
        this.mygraph.check.bind(this.viewModel.check);
        this.mygraph.zvalue.bind(this.viewModel.zvalue);
        this.mygraph.zanomalyvalue.bind(this.viewModel.zanomalyvalue);
        this.mygraph.cx.bind(this.viewModel.cx);
        this.mygraph.cy.bind(this.viewModel.cy);
        this.mygraph.radius.bind(this.viewModel.radius);
        this.mygraph.welzlx.bind(this.viewModel.welzlx);
        this.mygraph.welzly.bind(this.viewModel.welzly);
        this.mygraph.type.bind(this.viewModel.type);

        this.mydashboard.yaw.bind(this.viewModel.yaw);
        this.mydashboard.direction.bind(this.viewModel.direction);
        this.mydashboard.roll.bind(this.viewModel.roll);
        this.mydashboard.pitch.bind(this.viewModel.pitch);
        this.mydashboard.speed.bind(this.viewModel.speed);
        this.mydashboard.altitude.bind(this.viewModel.altitude);


        this.rate=new SimpleFloatProperty();
        this.rate.setValue(1);
        this.rate.bind(this.viewModel.rate);
        this.rate.addListener((o,ov,nv)->speed.setText(this.rate.getValue().toString()));
        play.setImage(new Image(getClass().getResourceAsStream("./buttons/play.png")));
        backwards.setImage(new Image(getClass().getResourceAsStream("./buttons/fastbackwards.png")));
        skipprev.setImage(new Image(getClass().getResourceAsStream("./buttons/skiprev.png")));
        pause.setImage(new Image(getClass().getResourceAsStream("./buttons/pause.png")));
        stop.setImage(new Image(getClass().getResourceAsStream("./buttons/stop.png")));
        fastforward.setImage(new Image(getClass().getResourceAsStream("./buttons/fastforward.png")));
        skipnext.setImage(new Image(getClass().getResourceAsStream("./buttons/skipnext.png")));

    }

    public void Choosealg()  {
        Thread t=new Thread(()-> {
            try {
                this.viewModel.ChooseAlg();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });
      t.start();
    }
    public void Opencsv(){
        FileChooser fc =new FileChooser();
        fc.setTitle("Choose file");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Csv Files", "*.csv"));
        chosen=fc.showOpenDialog(null);
        if(chosen!=null) {
            System.out.println(chosen.getName());
        }
        int t=this.viewModel.setTs(new TimeSeries(chosen.getAbsolutePath()));
        timebar.setMax(t);

    }

    public void play(){
        this.viewModel.play();
    }
    public void stop(){
        this.mygraph.series.getData().clear();
        this.mygraph.series2.getData().clear();
        //this.mygraph.seriesline.getData().clear();
        this.viewModel.stop();
    }
    public void pause(){
        this.viewModel.pause();
    }
    public void forward(){
        this.viewModel.forward();
    }
    public void backward(){
        this.viewModel.backward();
    }
    public void doubleforward(){
        this.viewModel.doubleforward();
    }
    public void doublebackward(){
        this.viewModel.doublebackward();
    }
    public void slidermove(){

        this.viewModel.slidermove(this.timebar.getValue());
    }


}
