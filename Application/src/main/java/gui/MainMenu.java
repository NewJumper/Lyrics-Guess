package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenu extends Application {
    public static Stage window;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        stage.setTitle("TS Games");
        stage.getIcons().add(new Image("gui/textures/icon.png"));
        stage.setOnCloseRequest(event -> {
            event.consume();
            try {
                QuitMenu.display();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Font.loadFont(Objects.requireNonNull(MainMenu.class.getResource("fonts/norwester.otf")).toExternalForm(), 20);

        showMenu();
        stage.show();
    }

    public static void showMenu() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainMenu.class.getResource("main.fxml")));
        window.setScene(new Scene(root));
    }

    public void play() throws IOException {
        PlayMenu.playMenu();
    }

    public void scores() throws IOException {
        ScoresMenu.scoresMenu();
    }

    public void settings() {
        System.out.println("settings");
    }

    public void quit() {
        window.close();
    }
}
