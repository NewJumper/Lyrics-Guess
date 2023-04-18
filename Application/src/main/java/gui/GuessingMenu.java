package gui;

import game.SongGuessing;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public Text timeText;
    public Button skipButton;

    private boolean newSong = true;
    private List<String> currentSong;
    public static int score;
    public static int trackCount;
    public static int correct;
    public static int incorrect;
    public static int guesses;
    public static long startTime;
    public static long time;
    public Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
        if(startTime == 0) return;
        time = System.nanoTime();
        long secondsText = (time - startTime) / 1000000000L % 60;
        timeText.setText((time - startTime) / 60000000000L + ":" + (secondsText < 10 ? "0" + secondsText : secondsText));
    }), new KeyFrame(Duration.seconds(1)));

    public static void guessing() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("guessing-menu.fxml"))));
        Scene scene = new Scene(root, window.getWidth() - 15, window.getHeight() - 39);
        window.setScene(scene);
    }

    public void returnToMenu() throws IOException {
        timeline.stop();
        MainMenu.showMenu();
    }

    public void checkGuess(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode() != KeyCode.ENTER) return;

        if(trackCount == 0) {
            startTime = System.nanoTime();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            SongGuessing.randomSong();
            storeLines = List.of(lines0, lines1, lines2, lines3, lines4, lines5, lines6, lines7, lines8, lines9, lines10, lines11);
        }
        if(skipButton.isDisabled()) return;

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
                score = updateScore(true);
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

                if(!SongGuessing.capped) {
                    guesses++;
                    score = updateScore(false);
                }
            }
        }

        textBox.clear();
        if(trackCount == SongGuessing.order.size()) {
            endGame();
            skipButton.setDisable(true);
            return;
        }
        if(newSong) nextTrack();
        updateLines();
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
        if(trackCount == 0 || trackCount == SongGuessing.order.size()) return;

        guessHistory.setFill(Color.valueOf("#bf3f3f"));
        guessHistory.setText(SongGuessing.filterSongName(currentSong.get(0)));
        albumAnswerB.setText(", ");
        albumAnswer.setText(SongGuessing.albums.get(SongGuessing.order.get(trackCount - 1)[0]).get(0));
        newSong = true;

        revealNeighbors();

        incorrect++;
        score = updateScore(false);

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

    public int updateScore(boolean green) {
        score = 10 * correct - 2 * incorrect - guesses - (int) ((time - startTime) / 30000000000L);

        if(green) scoreText.setFill(Color.valueOf("#3fbf53"));
        else scoreText.setFill(Color.valueOf("#bf3f3f"));
        scoreText.setText(String.valueOf(score));

        return score;
    }

    public void endGame() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("Application/src/main/resources/scores.txt", true));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        writer.write("NORMAL - " + score + " (" + trackCount + ") " + dateFormat.format(new Date()) + "\n");
        writer.flush();

        trackCount = 0;
        correct = 0;
        incorrect = 0;
        guesses = 0;

        returnToMenu();
    }
}
