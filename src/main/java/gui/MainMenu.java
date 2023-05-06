package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        Parent root = FXMLLoader.load(Objects.requireNonNull(MainMenu.class.getResource("main.fxml")));
        window.setScene(new Scene(root));
        window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        showMenu();
        stage.show();
    }

    public static void showMenu() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(MainMenu.class.getResource("main.fxml")));
        updateScene(null, root, true);
    }

    public static void updateScene(Parent oldRoot, Parent newRoot, boolean isMain) throws IOException {
        Parent main = FXMLLoader.load(Objects.requireNonNull(MainMenu.class.getResource("main.fxml")));
        Scene scene = window.getScene();
        scene.setRoot(newRoot);

        scene.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.F11) window.setFullScreen(!window.isFullScreen());
            if(event.getCode() == KeyCode.ESCAPE) {
                String rootId = window.getScene().getRoot().getId();
                if(isMain || oldRoot == main || (rootId != null && rootId.equals("GameRoot"))) return;

                try {
                    updateScene(main, oldRoot, false);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void saveGame() throws IOException {
        if(GameMenu.trackCount > 1 && GameMenu.mode != 0) {
            String modeName;
            switch (GameMenu.mode) {
                default -> modeName = "NORMAL";
                case 2 -> modeName = "HARDCORE";
                case 3 -> modeName = "ENDLESS";
                case 4 -> modeName = "TIME ATTACK";
                case 5 -> modeName = "OPENING";
                case 6 -> modeName = "CLOSING";
                case 7 -> modeName = "ASSOCIATION";
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/scores.txt", true));
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            writer.write(SettingsMenu.artist + " " + modeName + " " + GameMenu.score + " (" + (GameMenu.correct + GameMenu.incorrect) + ") " + dateFormat.format(new Date()) + "\n");
            writer.flush();
        }

        GameMenu.trackCount = 0;
        GameMenu.correct = 0;
        GameMenu.incorrect = 0;
        GameMenu.guesses = 0;
        GameMenu.strikes = 0;
    }

    public void play() throws IOException {
        PlayMenu.playMenu();
    }

    public void scores() throws IOException {
        ScoresMenu.scoresMenu();
    }

    public void settings() throws IOException {
        SettingsMenu.settingsMenu();
    }

    public void quit() {
        window.close();
    }
}
