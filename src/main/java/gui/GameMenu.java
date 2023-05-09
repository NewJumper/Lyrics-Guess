package gui;

import game.SongGuessing;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameMenu {
    public List<Text> storeLines;
    public Text gamemode;
    public Text lines0, lines1, lines2, lines3, lines4, lines5, lines6, lines7, lines8, lines9, lines10, lines11;
    public Text track;
    public TextField textBox;
    public Text guessHistory;
    public Text albumAnswer, albumAnswerB;
    public Text answer1, answer2, answer3;
    public Text scoreText;
    public Text timeText;
    public Button skipButton;

    public static int mode;
    private boolean newSong = true;
    private List<String> currentSong;
    public static int score;
    public static int trackCount;
    public static int correct;
    public static int incorrect;
    public static int guesses;
    public static int strikes;
    public static long startTime;
    public static long time;
    public Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
        if(startTime == 0) return;
        time = System.nanoTime();
        long secondsText = Math.abs((time - startTime) / 1000000000L % 60);
        timeText.setText(Math.abs((time - startTime) / 60000000000L) + ":" + (secondsText < 10 ? "0" + secondsText : secondsText));
        if(mode == 4 && time / 1000000000L - startTime / 1000000000L == 0) {
            try {
                endGame();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }), new KeyFrame(Duration.seconds(1)));

    /**
     *  0 - ZEN
     *  1 - NORMAL
     *  2 - HARDCORE
     *  3 - ENDLESS
     *  4 - TIME ATTACK
     *  5 - OPENING
     *  6 - CLOSING
     *  7 - ASSOCIATION
     */
    public static void guessing(int mode) throws IOException {
        GameMenu.mode = mode;
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("game.fxml"))));
        root.setId("GameRoot");
        MainMenu.window.getScene().setRoot(root);
    }

    public void checkGuess(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode() != KeyCode.ENTER) return;

        if((mode == 2 && incorrect == 3) || (mode == 3 && incorrect == 1)) {
            endGame();
            return;
        }

        if(trackCount % 188 == 0) {
            switch (mode) {
                default -> gamemode.setText("NORMAL");
                case 0 -> {
                    gamemode.setText("ZEN");
                    gamemode.setFill(Color.valueOf("#d8e0e3"));
                    scoreText.setText("--");
                }
                case 2 -> {
                    gamemode.setText("HARDCORE");
                    gamemode.setFill(Color.valueOf("#e65353"));
                }
                case 3 -> gamemode.setText("ENDLESS");
                case 4 -> {
                    gamemode.setText("TIME ATTACK");
                    gamemode.setFill(Color.valueOf("#49e3c3"));
                }
                case 5 -> {
                    gamemode.setText("OPENING");
                    gamemode.setFill(Color.valueOf("#7e73e6"));
                }
                case 6 -> {
                    gamemode.setText("CLOSING");
                    gamemode.setFill(Color.valueOf("#7e73e6"));
                }
            }

            if(trackCount == 0) {
                storeLines = List.of(lines0, lines1, lines2, lines3, lines4, lines5, lines6, lines7, lines8, lines9, lines10, lines11);
                startTime = System.nanoTime();
                if(mode == 4) startTime += SettingsMenu.timeControl * 60000000000L;
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
            }

            SongGuessing.randomSong();
        }

        if(!newSong) {
            String guess = textBox.getText().trim();
            if(SongGuessing.checkGuess(currentSong.get(0), guess)) {
                strikes = 0;
                guessHistory.setFill(Color.valueOf("#3fbf53"));
                guessHistory.setText(currentSong.get(0).replaceAll("=", ""));
                albumAnswerB.setText(", ");
                albumAnswer.setText(SongGuessing.albums.get(SongGuessing.order.get((trackCount - 1) % 188)[0]).get(0));
                newSong = true;

                revealNeighbors();

                correct++;
                score = updateScore(true);
            } else {
                if(mode == 2) {
                    if(strikes == 0) {
                        strikes++;
                    } else if(strikes == 1) {
                        guesses++;
                        strikes = 0;
                        skipTrack();
                        return;
                    }
                }

                if(!guess.equals("")) {
                    guessHistory.setFill(Color.valueOf("#c0c0c0"));
                    guessHistory.setText(guess);
                    albumAnswerB.setText("");
                    albumAnswer.setText("");
                    answer1.setText("---");
                    answer2.setText("---");
                    answer3.setText("---");
                }

                if(!guess.equals("") || !SongGuessing.capped && mode < 5) {
                    guesses++;
                    score = updateScore(false);
                }
            }
        }

        textBox.clear();
        if(trackCount == SongGuessing.order.size() && mode != 3 && !skipButton.isDisabled()) skipButton.setDisable(true);
        if(trackCount - 1 == SongGuessing.order.size() && mode != 3) {
            trackCount--;
            endGame();
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

        if(mode == 5) answer1.setFill(Color.valueOf("#c0c0c0"));
        else answer1.setFill(Color.valueOf("#888888"));
        if(mode == 6) answer3.setFill(Color.valueOf("#c0c0c0"));
        else answer3.setFill(Color.valueOf("#888888"));
    }

    public void skipTrack() throws IOException {
        if(trackCount == 0 || (trackCount == SongGuessing.order.size() && mode != 3)) return;
        if((mode == 2 && incorrect == 3) || (mode == 3 && incorrect == 1)) {
            endGame();
            return;
        }

        guessHistory.setFill(Color.valueOf("#bf3f3f"));
        guessHistory.setText(SongGuessing.filterSongName(currentSong.get(0)));
        albumAnswerB.setText(", ");
        albumAnswer.setText(SongGuessing.albums.get(SongGuessing.order.get((trackCount - 1) % 188)[0]).get(0));
        newSong = true;

        revealNeighbors();

        incorrect++;
        score = updateScore(false);

        textBox.clear();
        nextTrack();
        updateLines();
    }

    public void nextTrack() {
        if(trackCount == 188 && mode != 3) {
            trackCount++;
            return;
        }
        currentSong = SongGuessing.getSong(SongGuessing.albums.get(SongGuessing.order.get(trackCount % 188)[0]), SongGuessing.order.get(trackCount % 188)[1]);
        trackCount++;
        newSong = false;

        SongGuessing.history.clear();
        storeLines.forEach(text -> text.setText(""));
        if(mode == 3) track.setText("Track " + trackCount);
        else track.setText("Track " + trackCount + "/" + SongGuessing.order.size());

        if(mode == 5) {
            storeLines.get(1).setText(SongGuessing.replaceName(currentSong.get(1), SongGuessing.filterSongName(currentSong.get(0))));
            storeLines.get(0).setText(SongGuessing.replaceName(currentSong.get(2), SongGuessing.filterSongName(currentSong.get(0))));
            SongGuessing.history.put(1, storeLines.get(1).getText());
            SongGuessing.history.put(2, storeLines.get(0).getText());
        } else if(mode == 6) {
            storeLines.get(1).setText(SongGuessing.replaceName(currentSong.get(currentSong.size() - 2), SongGuessing.filterSongName(currentSong.get(0))));
            storeLines.get(0).setText(SongGuessing.replaceName(currentSong.get(currentSong.size() - 1), SongGuessing.filterSongName(currentSong.get(0))));
            SongGuessing.history.put(currentSong.size() - 1, storeLines.get(0).getText());
            SongGuessing.history.put(currentSong.size() - 2, storeLines.get(1).getText());
        }
    }

    public void updateLines() {
        if(mode < 5) SongGuessing.randomLine(currentSong);
        List<Integer> keys = new ArrayList<>(SongGuessing.history.keySet());

        for(int i = storeLines.size() - 1; i >= 0; i--) {
            if(i >= SongGuessing.history.size()) continue;
            String songName = SongGuessing.filterSongName(currentSong.get(0)).replace("&", "and").replace("...", "").replace("?", "");
            String line = SongGuessing.replaceName(SongGuessing.history.get(keys.get(keys.size() - i - 1)), songName);
            storeLines.get(i).setText(line);
        }
    }

    public int updateScore(boolean green) {
        switch (mode) {
            default -> score = (int) (10 * correct - 2 * incorrect - guesses - (time - startTime) / 20000000000L);
            case 2 -> score = (int) (11.11 * correct - 2.11 * incorrect - guesses - (time - startTime) / 15000000000L);
            case 4 -> score = 15 * correct - 5 * incorrect - 3 * guesses;
            case 3 -> score = (int) (9.5 * correct - 7/3.0 * incorrect - guesses / 4.0 - (time - startTime) / 20000000000L) + trackCount / 47;
            case 5, 6 -> score = (int) (10 * correct - 2.5 * incorrect - guesses - (time - startTime) / 60000000000L);
        }

        if(green) scoreText.setFill(Color.valueOf("#3fbf53"));
        else scoreText.setFill(Color.valueOf("#bf3f3f"));
        if(mode != 0) scoreText.setText(String.valueOf(score));

        return score;
    }

    public void endGame() throws IOException {
        MainMenu.saveGame();
        timeline.stop();
        MainMenu.showMenu();
    }
}
