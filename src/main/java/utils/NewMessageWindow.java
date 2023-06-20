package utils;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class NewMessageWindow {

    public static void getMessageInTextField(String txt){
        Stage stage=new Stage();
        Label label = new Label(txt);
        ScrollPane scrollPane = new ScrollPane();
        label.setAlignment(Pos.CENTER);
        scrollPane.setContent(label);
        Scene scene = new Scene(label, 200,50);
        stage.setScene(scene);
        stage.show();
    }
}
