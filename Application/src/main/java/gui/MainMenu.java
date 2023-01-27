package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenu extends Application {
    public Button playButton;
    public Button scoresButton;
    public Button settingsButton;
    public Button quitButton;

    public static void main(String[] args) {
        launch();
    }

    public static Stage window;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main.fxml")));
        stage.setTitle("TS Games");
        stage.getIcons().add(new Image("gui/textures/icon.png"));
        stage.setOnCloseRequest(event -> {
            event.consume();
            if(ConfirmBox.display("Quit?", "Are you sure you want to quit?")) stage.close();
        });

        stage.setScene(new Scene(root, 512, 512));
        stage.show();
    }

    public void play() {
        PlayMenu.playMenu(window);
    }

    public void scores() {
    }

    public void settings() {
    }

    public void quit() {
        window.close();
    }
}
