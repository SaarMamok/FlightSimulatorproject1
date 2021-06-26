package view.attlist;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AttListController {
    @FXML
    ListView attributeslist;

    public AttListController() {this.index= new SimpleIntegerProperty();
    }

    public IntegerProperty index;

    public void init(String file){
        try {
            //index=new SimpleIntegerProperty();
            BufferedReader bf=new BufferedReader(new FileReader(file));
            String line;
            line= bf.readLine();
            String[]s=line.split(",");
            attributeslist.getItems().clear();
            attributeslist.getItems().addAll(s);
            attributeslist.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            attributeslist.getSelectionModel().select(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Chosen(){
        index.setValue(attributeslist.getSelectionModel().getSelectedIndex());
    }
}
