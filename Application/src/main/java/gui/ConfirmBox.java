package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
    static boolean result;

    public static boolean display(String title, String message) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        stage.setMinWidth(250);

        Button accept = new Button("Yes");
        Button decline = new Button("No");
        accept.setOnAction(event -> {
            result = true;
            stage.close();
        });
        decline.setOnAction(event -> {
            result = false;
            stage.close();
        });

        Label label = new Label(message);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, accept, decline);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
        stage.showAndWait();

        return result;
    }
}
