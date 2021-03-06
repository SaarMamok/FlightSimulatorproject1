package view.attlist;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;


public class AttList extends AnchorPane {
    @FXML
    public ListView attributeslist;
    public IntegerProperty index;
    public StringProperty filechosen;
    public AttList(){
        super();
//commit
        try {
            FXMLLoader fxl=new FXMLLoader();
            Parent root = fxl.load(getClass().getResource("AttList.fxml").openStream());
            AttListController attListController=fxl.getController();
          //  attListController.init();
            this.attributeslist= attListController.attributeslist;
            this.filechosen=new SimpleStringProperty();
            filechosen.addListener((o,ov,nv)->{
                attListController.init(filechosen.getValue());
            });
            index=new SimpleIntegerProperty();
            this.index.bind(attListController.index);
            this.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
