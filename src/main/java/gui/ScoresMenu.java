package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScoresMenu {
    public Text score1A, score2A, score3A, score4A, score5A, score6A, score7A, score8A, score9A, score10A, score11A, score12A, score13A, score14A, score15A, score16A;
    public Text score1B, score2B, score3B, score4B, score5B, score6B, score7B, score8B, score9B, score10B, score11B, score12B, score13B, score14B, score15B, score16B;
    public Text date1, date2, date3, date4, date5, date6, date7, date8, date9, date10, date11, date12, date13, date14, date15, date16;

    public static void scoresMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("scores.fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("main.fxml"))));
        MainMenu.updateScene(oldRoot, root, false);
    }

    public void organizeScores() throws IOException {
        List<String> scores = Files.readAllLines(Paths.get("src/main/resources/scores.txt"));
        List<Text> scoreNames = List.of(score1A, score2A, score3A, score4A, score5A, score6A, score7A, score8A, score9A, score10A, score11A, score12A, score13A, score14A, score15A, score16A);
        List<Text> scoreInfo = List.of(score1B, score2B, score3B, score4B, score5B, score6B, score7B, score8B, score9B, score10B, score11B, score12B, score13B, score14B, score15B, score16B);
        List<Text> dates = List.of(date1, date2, date3, date4, date5, date6, date7, date8, date9, date10, date11, date12, date13, date14, date15, date16);
        List<String> leaderboard = new ArrayList<>();
        leaderboard.add(scores.get(0));

        for(int i = 1; i < scores.size(); i++) {
            int score = getScoringInfo(scores.get(i), true);

            for(int j = i - 1; j >= 0; j--) {
                int exScore = getScoringInfo(leaderboard.get(j), true);
                if(score < exScore) {
                    leaderboard.add(j + 1, scores.get(i));
                    break;
                } else if(score == exScore) {
                    for(int k = j; k >= 0; k--) {
                        exScore = getScoringInfo(leaderboard.get(k), true);
                        if(score < exScore || getScoringInfo(scores.get(i), false) >= getScoringInfo(leaderboard.get(j), false)) {
                            leaderboard.add(k + 1, scores.get(i));
                            break;
                        } else if (k == 0) leaderboard.add(0, scores.get(i));
                    }

                    break;
                } else if(j == 0) leaderboard.add(0, scores.get(i));
            }
        }

        leaderboard.subList(16, leaderboard.size()).clear();
        for(int i = 0; i < leaderboard.size(); i++) {
            String save = leaderboard.get(i);
            String mode = save.substring(0, save.indexOf(" - "));
            String info = save.substring(save.indexOf(" - "), save.indexOf(") ") + 1);

            switch (mode) {
                case "HARDCORE" -> scoreNames.get(i).setFill(Color.valueOf("#ef4e40"));
                case "TIME ATTACK" -> scoreNames.get(i).setFill(Color.valueOf("#49e3c3"));
                case "OPENING", "CLOSING" -> scoreNames.get(i).setFill(Color.valueOf("#7e73e6"));
                default -> scoreNames.get(i).setFill(Color.valueOf("#ffffff"));
            }

            scoreNames.get(i).setText(mode);
            scoreInfo.get(i).setText(info);
            dates.get(i).setText(save.substring(save.indexOf(") ") + 2));
        }
    }

    public static int getScoringInfo(String text, boolean score) {
        return Integer.parseInt(score ? text.substring(text.indexOf("- ") + 2, text.indexOf("(") - 2) : text.substring(text.indexOf("(") + 1, text.indexOf(")")));
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }
}
