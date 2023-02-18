package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class PlayMenu {
    public static Stage window = MainMenu.window;

    public static void playMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("play-menu.fxml"))));
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

    public void difficultyMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("difficulty-menu.fxml"))));
        Scene scene = new Scene(root, window.getWidth() - 15, window.getHeight() - 39);
        window.setScene(scene);

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE) {
                try {
                    playMenu();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void normal() throws IOException {
        GuessingMenu.guessing();
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }
}
