package gui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SettingsMenu implements Initializable {
    public Text artistSel;
    public Text timeSel;

    private static Text artistSelStatic;
    private static Text timeSelStatic;
    public static String artist = "TS";
    public static int timeControl = 3;

    /*
     * WHAT TO STORE:
     * WHAT ARTIST: AJR, SABRINA CARPENTER, TAYLOR SWIFT, TWENTY ONE PILOTS
     *      ARTIST CODES: AJ, SC, TS, TP
     * TIME FOR TIME ATTACK: 1 MIN, 2 MIN, 3 MIN, 5 MIN
     */
    public static void settingsMenu() throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("settings.fxml"))));
        Parent oldRoot = FXMLLoader.load((Objects.requireNonNull(PlayMenu.class.getResource("main.fxml"))));
        MainMenu.updateScene(oldRoot, root, false);

        artistSelStatic.setText(artist);
        timeSelStatic.setText(timeControl + ":00");
    }

    public void changeArtist() {
        if(artist.equals("TS")) artistSelStatic.setText(artist = "AJ");
        else if(artist.equals("AJ")) artistSelStatic.setText(artist = "TS");
    }

    public void changeTime() {
        switch(timeControl) {
            default -> timeSelStatic.setText((timeControl = 1) + ":00");
            case 1 -> timeSelStatic.setText((timeControl = 2) + ":00");
            case 2 -> timeSelStatic.setText((timeControl = 3) + ":00");
            case 3 -> timeSelStatic.setText((timeControl = 5) + ":00");
            case 5 -> timeSelStatic.setText((timeControl = 10) + ":00");
        }
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        artistSelStatic = artistSel;
        timeSelStatic = timeSel;
    }
}
