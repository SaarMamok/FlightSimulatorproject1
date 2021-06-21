package view.mediaplayer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MediaPlayerController {
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
    TextField speed;
    @FXML
    Label clock;


    public MediaPlayerController() {
        play.setImage(new Image(getClass().getResourceAsStream("play.png")));
        /*backwards.setImage(new Image(getClass().getResourceAsStream("buttons/fastbackwards.png")));
        skipprev.setImage(new Image(getClass().getResourceAsStream("buttons/skiprev.png")));
        pause.setImage(new Image(getClass().getResourceAsStream("buttons/pause.png")));
        stop.setImage(new Image(getClass().getResourceAsStream("buttons/stop.png")));
        fastforward.setImage(new Image(getClass().getResourceAsStream("buttons/fastforward.png")));
        skipnext.setImage(new Image(getClass().getResourceAsStream("buttons/skipnext.png")));*/
    }
}