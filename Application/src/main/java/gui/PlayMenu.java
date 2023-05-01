package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class PlayMenu {

    public static void playMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("gamemodes.fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("main.fxml"))));
        MainMenu.updateScene(oldRoot, root, false);
    }

    public void difficultyMenu() throws IOException {
        showMenu("all-songs");
    }

    public void endlessMenu() throws IOException {
        showMenu("endless");
    }

    public void showMenu(String path) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource(path + ".fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("gamemodes.fxml"))));
        MainMenu.updateScene(oldRoot, root, false);
    }

    public void zen() throws IOException {
        GameMenu.guessing(0);
    }

    public void normal() throws IOException {
        GameMenu.guessing(1);
    }

    public void hardcore() throws IOException {
        GameMenu.guessing(2);
    }

    public void opening() throws IOException {
        GameMenu.guessing(3);
    }

    public void closing() throws IOException {
        GameMenu.guessing(4);
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }
}
