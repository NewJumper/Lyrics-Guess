package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ScoresMenu {
    public static Stage window = MainMenu.window;

    public static void scoresMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("scores-menu.fxml"))));
        Scene scene = new Scene(root, window.getWidth() - 15, window.getHeight() - 39);
        window.setScene(scene);

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE) {
                try {
                    MainMenu.showMenu();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }
}
