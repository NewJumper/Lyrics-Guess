package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class SettingsMenu {
    public static void settingsMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("settings-menu.fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("main.fxml"))));
        MainMenu.updateScene(oldRoot, root, false);
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }
}
