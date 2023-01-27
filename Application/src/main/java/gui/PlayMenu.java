package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PlayMenu {
    public static void playMenu(Stage stage) {
        StackPane layout2 = new StackPane();
        Button button = new Button("test");
        layout2.getChildren().add(button);
        Scene scene = new Scene(layout2, 500, 500);

        stage.setScene(scene);
        stage.show();
    }
}
