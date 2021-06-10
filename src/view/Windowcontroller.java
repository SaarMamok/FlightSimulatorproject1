package view;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import test.TimeSeries;
import view.attlist.AttList;
import view.dashboard.Mydashboard;
import view.graph.Mygraph;
import view.joystick.MyJoystick;
import viewmodel.ViewModel;

import java.io.File;
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
            this.mygraph.series.getData().clear();
            this.mygraph.series2.getData().clear();
            //this.mygraph.time.set(time.getValue());
        });
        this.mygraph.listvalue.bind(this.viewModel.listvalue);
        this.mygraph.corvalue.bind(this.viewModel.corvalue);
        this.mygraph.time.bind(this.time);





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
