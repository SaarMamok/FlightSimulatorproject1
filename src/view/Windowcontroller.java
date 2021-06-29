package view;

import javafx.beans.property.*;
import javafx.fxml.FXML;

import javafx.scene.chart.XYChart;
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
import view.mediaplayer.MediaPlayer;
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
    MediaPlayer mediaPlayer;
    @FXML
    Mygraph mygraph;


    private File chosen,xmlchosen;
    public IntegerProperty time;
    public FloatProperty rate;
    ViewModel viewModel;
    float sec=0,min=0,hour=0;
    StringProperty fileChoosen,xmlpath;
    private XYChart.Series series;


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
        series=new XYChart.Series();
        xmlpath=new SimpleStringProperty();
        mediaPlayer.timebar.setMin(0);
        time=new SimpleIntegerProperty();
        this.viewModel=vm;
        this.myJoystick.rudder.bind(this.viewModel.rudder);
        this.myJoystick.throttle.bind(this.viewModel.throttle);
        this.myJoystick.aileron.bind(this.viewModel.aileron);
        this.myJoystick.elevators.bind(this.viewModel.elevators);
        this.time.bind(this.viewModel.time);
        this.time.addListener((o,ov,nv)->{
            mediaPlayer.timebar.setValue(time.getValue());
            this.mediaPlayer.clock.setText(getDurationString(this.time.getValue()));
            this.mygraph.listvalue.bind(this.viewModel.listvalue);
            this.mygraph.corvalue.bind(this.viewModel.corvalue);
            this.mygraph.paintGraph.setData(this.viewModel.getScatterChart().getData());
        });

        this.viewModel.index.bind(attributeslist.index);
        this.mygraph.lefttitle.bind(this.viewModel.leftval);
        this.mygraph.righttitle.bind(this.viewModel.rightval);
        this.mygraph.time.bind(this.viewModel.time);
        this.mygraph.isred.bind(viewModel.isred);
        this.viewModel.iscor.addListener((o,ov,nv)->{
            if(viewModel.iscor.getValue()==true) {
                this.mygraph.cover.setVisible(false);
                this.mygraph.paintGraph.setVisible(true);
            }
            else{
                this.mygraph.cover.setVisible(true);
                this.mygraph.paintGraph.setVisible(false);
            }
        });
        this.attributeslist.index.addListener((o,ov,nv)->{
            this.mygraph.righttitle.bind(this.viewModel.rightval);
            this.mygraph.series.getData().clear();
            this.mygraph.series2.getData().clear();
            this.mygraph.paintGraph.getData().clear();//dont change - karin!!!
            if(viewModel.iscor.getValue()==true) {
                this.mygraph.cover.setVisible(false);
                this.mygraph.paintGraph.setVisible(true);


                this.mygraph.paintGraph.setData(this.viewModel.getScatterChart().getData());
                this.mygraph.paintGraph.setTitle(this.viewModel.Algname.getValue());
            }
            else{
                    this.mygraph.cover.setVisible(true);
                    this.mygraph.paintGraph.setVisible(false);
            }

        });


        this.mydashboard.yaw.bind(this.viewModel.yaw);
        this.mydashboard.direction.bind(this.viewModel.direction);
        this.mydashboard.roll.bind(this.viewModel.roll);
        this.mydashboard.pitch.bind(this.viewModel.pitch);
        this.mydashboard.speed.bind(this.viewModel.speed);
        this.mydashboard.altitude.bind(this.viewModel.altitude);

        this.rate=new SimpleFloatProperty();
        this.rate.setValue(1);
        this.rate.bind(this.viewModel.rate);
        this.rate.addListener((o,ov,nv)->this.mediaPlayer.speed.setText(this.rate.getValue().toString()));
        this.mediaPlayer.play.setOnMouseClicked(event->this.play());
        this.mediaPlayer.backwards.setOnMouseClicked(event -> this.backward());
        this.mediaPlayer.skipprev.setOnMouseClicked(event -> this.doublebackward());
        this.mediaPlayer.pause.setOnMouseClicked(event -> this.pause());
        this.mediaPlayer.stop.setOnMouseClicked(event -> this.stop());
        this.mediaPlayer.fastforward.setOnMouseClicked(event -> this.forward());
        this.mediaPlayer.skipnext.setOnMouseClicked(event -> this.doubleforward());
        this.mediaPlayer.opencsv.setOnAction(event -> this.Opencsv());
        this.mediaPlayer.openalg.setOnAction(event -> this.Choosealg());
        this.mediaPlayer.openxml.setOnAction(event -> this.Choosexml());
        this.mediaPlayer.timebar.setOnMouseReleased(event -> {
            this.slidermove();

        });

        this.fileChoosen = new SimpleStringProperty();
        this.attributeslist.filechosen.bind(fileChoosen);
        this.viewModel.xmlpath.bind(this.xmlpath);
    }

    public void Choosexml(){
        FileChooser fc =new FileChooser();
        fc.setTitle("Choose file");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Xml Files", "*.xml"));
        xmlchosen=fc.showOpenDialog(null);
        if(xmlchosen==null){
            return;
        }
        else{
            xmlpath.setValue(xmlchosen.getAbsolutePath());
        }
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
        if(chosen==null){
         return;
        }
        else {
            int t = this.viewModel.setTs(new TimeSeries(chosen.getAbsolutePath()));
            if(t!=-1) {
                this.mediaPlayer.timebar.setMax(t);
                fileChoosen.set(chosen.getAbsolutePath());
            }
        }
        this.mediaPlayer.clock.setText(getDurationString(0));
    }

    public void play(){
        this.viewModel.play();
    }

    public void stop(){
        this.mygraph.series.getData().clear();
        this.mygraph.series2.getData().clear();
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
        this.viewModel.slidermove(this.mediaPlayer.timebar.getValue());
    }
}
