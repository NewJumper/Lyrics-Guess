package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class ScoresMenu {
    public Text modeText;
    public Text mode1, mode2, mode3, mode4, mode5, mode6, mode7, mode8, mode9, mode10, mode11, mode12, mode13, mode14, mode15, mode16;
    public Text score1, score2, score3, score4, score5, score6, score7, score8, score9, score10, score11, score12, score13, score14, score15, score16;
    public Text tracks1, tracks2, tracks3, tracks4, tracks5, tracks6, tracks7, tracks8, tracks9, tracks10, tracks11, tracks12, tracks13, tracks14, tracks15, tracks16;
    public Text date1, date2, date3, date4, date5, date6, date7, date8, date9, date10, date11, date12, date13, date14, date15, date16;
    private boolean refresh;
    private int mode;

    public static void scoresMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("scores.fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("main.fxml"))));
        MainMenu.updateScene(oldRoot, root, false);
    }

    public void filter() throws IOException {
        if(++mode > 6) mode = 0;
        refresh = false;
        listScores();
    }

    public void listScores() throws IOException {
        if(refresh) return;
        refresh = true;

        switch (mode) {
            case 0 -> modeText.setFill(Color.valueOf("#aaaaaa"));
            case 1 -> modeText.setFill(Color.valueOf("#cacaca"));
            case 2 -> modeText.setFill(Color.valueOf("#ab3e3e"));
            case 3 -> modeText.setFill(Color.valueOf("#6f9aab"));
            case 4 -> modeText.setFill(Color.valueOf("#34a38c"));
            case 5 -> modeText.setFill(Color.valueOf("#5b53a6"));
            case 6 -> modeText.setFill(Color.valueOf("#3da863"));
        }

        List<String> scores = Files.readAllLines(Paths.get("src/main/resources/scores.txt"));
        List<Text> scoreNames = List.of(mode1, mode2, mode3, mode4, mode5, mode6, mode7, mode8, mode9, mode10, mode11, mode12, mode13, mode14, mode15, mode16);
        List<Text> scoreInfo = List.of(score1, score2, score3, score4, score5, score6, score7, score8, score9, score10, score11, score12, score13, score14, score15, score16);
        List<Text> completion = List.of(tracks1, tracks2, tracks3, tracks4, tracks5, tracks6, tracks7, tracks8, tracks9, tracks10, tracks11, tracks12, tracks13, tracks14, tracks15, tracks16);
        List<Text> dates = List.of(date1, date2, date3, date4, date5, date6, date7, date8, date9, date10, date11, date12, date13, date14, date15, date16);

        for(int i = 1; i < scores.size(); i++) {
            String saveData = scores.get(i);
            int j = i - 1;
            int index = findPlacement(scores, saveData, j);
            while(j >= index) scores.set(j + 1, scores.get(j--));
            scores.set(j + 1, saveData);
        }

        int buffer = 0;
        for(int i = 0; i < 16 + buffer; i++) {
            if(i >= scores.size()) {
                scoreNames.get(i - buffer).setText("-");
                scoreNames.get(i - buffer).setFill(Color.WHITE);
                scoreInfo.get(i - buffer).setText("-");
                completion.get(i - buffer).setText("-");
                dates.get(i - buffer).setText("-");
                continue;
            }
            String data = scores.get(i);
            String mode = data.substring(3, data.indexOf(" ", 8));
            String score = data.substring(data.indexOf(" ", 8), data.indexOf(" ("));
            String tracks = data.substring(data.indexOf("(") + 1, data.indexOf(")"));

            switch (mode) {
                default -> {
                    if(this.mode != 0 && this.mode != 1) {
                        buffer++;
                        continue;
                    }
                    scoreNames.get(i - buffer).setFill(Color.WHITE);
                }
                case "HARDCORE" -> {
                    if(this.mode != 0 && this.mode != 2) {
                        buffer++;
                        continue;
                    }
                    scoreNames.get(i - buffer).setFill(Color.valueOf("#e65353"));
                }
                case "ENDLESS" -> {
                    if(this.mode != 0 && this.mode != 3) {
                        buffer++;
                        continue;
                    }
                    scoreNames.get(i - buffer).setFill(Color.valueOf("#a4e4fc"));
                }
                case "TIME ATTACK" -> {
                    if(this.mode != 0 && this.mode != 4) {
                        buffer++;
                        continue;
                    }
                    scoreNames.get(i - buffer).setFill(Color.valueOf("#49e3c3"));
                }
                case "OPENING", "CLOSING" -> {
                    if(this.mode != 0 && this.mode != 5) {
                        buffer++;
                        continue;
                    }
                    scoreNames.get(i - buffer).setFill(Color.valueOf("#7e73e6"));
                }
                case "ASSOCIATION" -> {
                    if(this.mode != 0 && this.mode != 6) {
                        buffer++;
                        continue;
                    }
                    scoreNames.get(i - buffer).setFill(Color.valueOf("#55ed8d"));
                }
            }

            scoreNames.get(i - buffer).setText(mode);
            scoreInfo.get(i - buffer).setText(score);
            completion.get(i - buffer).setText(tracks);
            dates.get(i - buffer).setText(data.substring(data.indexOf(") ") + 2));
        }
    }

    private static int findPlacement(List<String> scores, String compare, int high) {
        int low = 0;
        while(low <= high) {
            int mid = (low + high) / 2;
            if(getScoringInfo(compare, true) == getScoringInfo(scores.get(mid), true) && getScoringInfo(compare, false) > getScoringInfo(scores.get(mid), false)) return mid + 1;
            else if(getScoringInfo(compare, true) < getScoringInfo(scores.get(mid), true)) low = mid + 1;
            else high = mid - 1;
        }

        return low;
    }

    private static int getScoringInfo(String text, boolean score) {
        return Integer.parseInt(score ? text.substring(text.indexOf(" ", 8) + 1, text.indexOf(" (")) : text.substring(text.indexOf("(") + 1, text.indexOf(")")));
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }
}
