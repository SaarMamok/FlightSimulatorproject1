package view.attlist;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import test.TimeSeries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AttListController {
    @FXML
    ListView attributeslist;


    public void init(){
        try {
            BufferedReader bf=new BufferedReader(new FileReader("anomaly_flight.csv"));
            String line;
            line= bf.readLine();
            String[]s=line.split(",");
            attributeslist.getItems().addAll(s);
            attributeslist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
