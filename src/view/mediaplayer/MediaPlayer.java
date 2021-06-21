package view.mediaplayer;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import view.joystick.MyJoystickController;

import java.io.IOException;

public class MediaPlayer extends AnchorPane {

    @FXML
    public  Slider timebar;
    @FXML
    public  TextField speed;
    @FXML
    public Label clock;

    public MediaPlayer(){
        super();

        try {
            FXMLLoader fxl=new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("MediaPlayer.fxml").openStream());
            MediaPlayerController mediaPlayerController=fxl.getController();
            this.timebar= mediaPlayerController.timebar;
            this.speed=mediaPlayerController.speed;
            this.clock=mediaPlayerController.clock;

            this.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
