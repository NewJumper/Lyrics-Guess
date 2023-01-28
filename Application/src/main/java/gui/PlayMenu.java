package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class PlayMenu {
    public static Stage window = MainMenu.window;

    public static void playMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("play-menu.fxml"))));
        window.setScene(new Scene(root, 512, 512));
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }
}
