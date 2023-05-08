package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class SettingsMenu {
    public static String artist = "TS";
    public Text artistSel;

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
    }

    public void changeArtist() {
        if(artist.equals("TS")) {
            artistSel.setText("AJ");
            artist = "AJ";
        }
        else if(artist.equals("AJ")) {
            artistSel.setText("TS");
            artist = "TS";
        }
    }

    public void returnToMenu() throws IOException {
        MainMenu.showMenu();
    }
}
