package view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import test.TimeSeries;
import view.attlist.AttList;
import view.dashboard.Mydashboard;
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


    private File chosen;
    IntegerProperty time;
    ViewModel viewModel;



    public void init(ViewModel vm){

        this.viewModel=vm;
        timebar.setMin(0);
        this.time.bind(this.viewModel.time);


        this.myJoystick.rudder.bind(this.viewModel.rudder);
        this.myJoystick.throttle.bind(this.viewModel.throttle);
        this.myJoystick.aileron.bind(this.viewModel.aileron);
        this.myJoystick.elevators.bind(this.viewModel.elevators);
        


        this.mydashboard.yaw.bind(this.viewModel.yaw);
        this.mydashboard.direction.bind(this.viewModel.direction);
        this.mydashboard.roll.bind(this.viewModel.roll);
        this.mydashboard.pitch.bind(this.viewModel.pitch);
        this.mydashboard.speed.bind(this.viewModel.speed);
        this.mydashboard.altitude.bind(this.viewModel.altitude);

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
        this.viewModel.setTs(new TimeSeries(chosen.getAbsolutePath()));

    }

    public void play(){
        this.viewModel.play();

    }

}
