package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GuessingMenu {
    public static Stage window = MainMenu.window;
    public Text lines;
    public TextField textBox;
    private boolean flag = true;
    private List<String> song;

    public static void guessing() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("guessing-menu.fxml"))));
        Scene scene = new Scene(root, window.getWidth() - 15, window.getHeight() - 39);
        window.setScene(scene);
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }

    public void checkGuess(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode() == KeyCode.ENTER) {
            if(flag) {
                song = GenerateLine.randomSong();
                flag = false;
            }
            String line = GenerateLine.randomLine(song);

            lines.setText(line);

            String guess = textBox.getText();
            textBox.clear();
        }
    }
}
