package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import properties.Settings;
import viewmodel.ViewModel;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxl=new FXMLLoader();
        Parent root = fxl.load(getClass().getResource("main.fxml").openStream());

        XMLEncoder encoder = new XMLEncoder(new FileOutputStream(new File("setting.xml")));
        Settings mySetting=new Settings();
        mySetting.DefaultProperties();
        encoder.writeObject(mySetting);
        encoder.flush();
        encoder.close();
        Windowcontroller wc=fxl.getController();
        Model m=new Model();
        ViewModel vm=new ViewModel(m);
        wc.init(vm);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800  , 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
