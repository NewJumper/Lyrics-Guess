package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class PlayMenu {

    public static void playMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("play-menu.fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("main.fxml"))));
        MainMenu.justDoStuff(oldRoot, root, false);
    }

    public void difficultyMenu() throws IOException {
        showMenu("difficulty-menu");
    }

    public void endlessMenu() throws IOException {
        showMenu("endless-menu");
    }

    public void showMenu(String path) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource(path + ".fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("play-menu.fxml"))));
        MainMenu.justDoStuff(oldRoot, root, false);
    }

    public void zen() throws IOException {
        GuessingMenu.guessing(0);
    }

    public void normal() throws IOException {
        GuessingMenu.guessing(1);
    }

    public void hardcore() throws IOException {
        GuessingMenu.guessing(2);
    }

    public void opening() throws IOException {
        GuessingMenu.guessing(3);
    }

    public void closing() throws IOException {
        GuessingMenu.guessing(4);
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }
}
