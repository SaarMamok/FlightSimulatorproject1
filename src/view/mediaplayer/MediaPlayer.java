package view.mediaplayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;


public class MediaPlayer extends AnchorPane {

    @FXML
    public  Slider timebar;
    @FXML
    public  TextField speed;
    @FXML
    public Label clock;
    @FXML
    public ImageView play;
    @FXML
    public ImageView backwards;
    @FXML
    public ImageView skipprev;
    @FXML
    public ImageView pause;
    @FXML
    public ImageView stop;
    @FXML
    public ImageView fastforward;
    @FXML
    public ImageView skipnext;
    @FXML
    public Button opencsv;
    @FXML
    public Button openalg;
    @FXML
    public Button openxml;



    public MediaPlayer(){
        super();

        try {
            FXMLLoader fxl=new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("MediaPlayer.fxml").openStream());
            MediaPlayerController mediaPlayerController=fxl.getController();
            mediaPlayerController.init();
            this.timebar= mediaPlayerController.timebar;
            this.speed=mediaPlayerController.speed;
            this.clock=mediaPlayerController.clock;
            this.play= mediaPlayerController.play;
            this.skipprev=mediaPlayerController.skipprev;
            this.pause= mediaPlayerController.pause;
            this.stop=mediaPlayerController.stop;
            this.fastforward=mediaPlayerController.fastforward;
            this.skipnext=mediaPlayerController.skipnext;
            this.backwards=mediaPlayerController.backwards;
            this.openalg=mediaPlayerController.openalg;
            this.opencsv= mediaPlayerController.opencsv;
            this.timebar=mediaPlayerController.timebar;
            this.openxml=mediaPlayerController.openxml;
            this.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
