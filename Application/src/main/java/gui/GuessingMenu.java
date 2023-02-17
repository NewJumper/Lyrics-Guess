package gui;

import game.SongGuessing;
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
    public List<Text> storeLines;
    public Text lines0;
    public Text lines1;
    public Text lines2;
    public Text lines3;
    public Text lines4;
    public Text lines5;
    public Text lines6;
    public Text lines7;
    public Text lines8;
    public Text lines9;
    public Text lines10;
    public Text lines11;
    public Text track;
    public TextField textBox;

    private boolean newSong = true;
    private List<String> currentSong;
    private int trackCount;
    private int correct;
    private int incorrect;

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
            if(trackCount == 0) {
                SongGuessing.randomSong();
                storeLines = List.of(lines0, lines1, lines2, lines3, lines4, lines5, lines6, lines7, lines8, lines9, lines10, lines11);
            }

            if(!newSong) {
                String guess = textBox.getText();
                if(SongGuessing.checkGuess(currentSong.get(0), guess)) {
                    newSong = true;
                    correct++;
                } else {
                    incorrect++;
                }
            }
            textBox.clear();

            if(newSong) {
                currentSong = SongGuessing.getSong(SongGuessing.albums.get(SongGuessing.order.get(trackCount)[0]), SongGuessing.order.get(trackCount)[1]);
                System.out.println(SongGuessing.filterSongName(currentSong.get(0)));
                trackCount++;
                newSong = false;
                reset();
            }

            String line = SongGuessing.randomLine(currentSong);
            shiftLines();
            lines0.setText(line);
        }
    }

    public void shiftLines() {
        for(int i = storeLines.size() - 1; i >= 0; i--) {
            if(i >= SongGuessing.rows.size()) continue;
            String lineTest = SongGuessing.rows.get(i);
            storeLines.get(i).setText(lineTest);
        }
    }

    public void reset() {
        SongGuessing.rows.clear();
        storeLines.forEach(text -> text.setText(""));
        track.setText("Track " + trackCount + "/" + SongGuessing.order.size());
    }
}
