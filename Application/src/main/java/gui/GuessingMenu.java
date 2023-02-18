package gui;

import game.SongGuessing;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuessingMenu {
    public static Stage window = MainMenu.window;
    public List<Text> storeLines;
    public Text lines0, lines1, lines2, lines3, lines4, lines5, lines6, lines7, lines8, lines9, lines10, lines11;
    public Text track;
    public TextField textBox;
    public Text guessHistory;
    public Text albumAnswer, albumAnswerB;
    public Text answer1, answer2, answer3;
    public Text scoreText;

    private boolean newSong = true;
    private List<String> currentSong;
    private int score, trackCount, correct, incorrect;

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
                    guessHistory.setFill(Color.valueOf("#3fbf53"));
                    guessHistory.setText(currentSong.get(0).replaceAll("=", ""));
                    albumAnswerB.setText(", ");
                    albumAnswer.setText(SongGuessing.albums.get(SongGuessing.order.get(trackCount - 1)[0]).get(0));
                    newSong = true;

                    revealNeighbors();

                    correct++;
                    score += 10;
                    scoreText.setFill(Color.valueOf("#3fbf53"));
                    scoreText.setText(String.valueOf(score));
                } else {
                    if(!guess.equals("")) {
                        guessHistory.setFill(Color.valueOf("#bbbbbb"));
                        guessHistory.setText(guess);
                        albumAnswerB.setText("");
                        albumAnswer.setText("");
                        answer1.setText("---");
                        answer2.setText("---");
                        answer3.setText("---");
                    }

                    incorrect++;
                    score--;
                    scoreText.setFill(Color.valueOf("#bf3f3f"));
                    scoreText.setText(String.valueOf(score));
                }
            }

            textBox.clear();
            if(newSong) nextTrack();
            updateLines();
        }
    }

    public void revealNeighbors() {
        List<Integer> keys = new ArrayList<>(SongGuessing.history.keySet());
        int row = keys.get(keys.size() - 1);
        if(row > 1) answer1.setText(currentSong.get(row - 1));
        else answer1.setText("---");
        answer2.setText(currentSong.get(row));
        if(row < currentSong.size() - 1) answer3.setText(currentSong.get(row + 1));
        else answer3.setText("---");
    }

    public void skipTrack() {
        if(trackCount == 0) return;

        guessHistory.setFill(Color.valueOf("#bf3f3f"));
        guessHistory.setText(SongGuessing.filterSongName(currentSong.get(0)));
        albumAnswerB.setText(", ");
        albumAnswer.setText(SongGuessing.albums.get(SongGuessing.order.get(trackCount - 1)[0]).get(0));
        newSong = true;

        revealNeighbors();

        incorrect += 2;
        score -= 2;
        scoreText.setFill(Color.valueOf("#bf3f3f"));
        scoreText.setText(String.valueOf(score));

        textBox.clear();
        nextTrack();
        updateLines();
    }

    public void nextTrack() {
        currentSong = SongGuessing.getSong(SongGuessing.albums.get(SongGuessing.order.get(trackCount)[0]), SongGuessing.order.get(trackCount)[1]);
        trackCount++;
        newSong = false;

        SongGuessing.history.clear();
        storeLines.forEach(text -> text.setText(""));
        track.setText("Track " + trackCount + "/" + SongGuessing.order.size());
    }

    public void updateLines() {
        SongGuessing.randomLine(currentSong);
        List<Integer> keys = new ArrayList<>(SongGuessing.history.keySet());

        for(int i = storeLines.size() - 1; i >= 0; i--) {
            if(i >= SongGuessing.history.size()) continue;
            String songName = SongGuessing.filterSongName(currentSong.get(0)).replace("&", "and").replace("...", "").replace("?", "");
            String line = SongGuessing.replaceName(SongGuessing.history.get(keys.get(keys.size() - i - 1)), songName);
            storeLines.get(i).setText(line);
        }
    }
}
