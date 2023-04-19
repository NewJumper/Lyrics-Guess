package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class QuitMenu {
    public static Stage window;

    public static void display() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(QuitMenu.class.getResource("quit-menu.fxml")));
        Stage stage = new Stage();
        window = stage;
        stage.setTitle("Quit?");
        stage.getIcons().add(new Image("gui/textures/quit-icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void onAccept() throws IOException {
        if(GuessingMenu.trackCount != 0) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Application/src/main/resources/scores.txt", true));
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            writer.write("NORMAL - " + GuessingMenu.score + " (" + (GuessingMenu.correct + GuessingMenu.incorrect) + ") " + dateFormat.format(new Date()) + "\n");
            writer.flush();

            GuessingMenu.trackCount = 0;
            GuessingMenu.correct = 0;
            GuessingMenu.incorrect = 0;
            GuessingMenu.guesses = 0;
        }

        window.close();
        MainMenu.window.close();
    }

    public void onDecline() {
        window.close();
    }
}
