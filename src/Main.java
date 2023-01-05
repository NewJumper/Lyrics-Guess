import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    public static List<List<String>> albums = new ArrayList<>();
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[1;32m";
    public static final String CYAN = "\033[36m";
    public static final String YELLOW = "\033[1;33m";
    public static final String BOLD = "\033[1;37m";
    public static final String ITALICS = "\033[3;37m";
    public static final String RESET = "\033[0m";
    private static final List<Integer> rows = new ArrayList<>();
    private static int tracks;
    private static int correct;
    private static int incorrect;
    private static int skips;

    public static void main(String[] args) throws IOException {
        albums.add(Files.readAllLines(Paths.get("src/albums/taylor swift.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/fearless.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/speak now.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/red.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/1989.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/reputation.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/lover.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/folklore.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/evermore.txt")));
        albums.add(Files.readAllLines(Paths.get("src/albums/midnights.txt")));

        System.out.println(YELLOW + """
                \nWhich game do you want to play?
                ALL - You have to guess all 188 songs to complete the game.
                RANDOM - Set a high score by guessing songs that are randomly chosen.""");

        String choice = INPUT.nextLine();
        if(choice.equalsIgnoreCase("ALL")) {
            System.out.println(BOLD + "ALL songs chosen\n" + RESET);
            allGuessing();
        } else if(choice.equalsIgnoreCase("RANDOM")) {
            System.out.println(BOLD + "RANDOM songs chosen\n" + RESET);
            do randomGuessing(0, 0, true);
            while (!INPUT.nextLine().equalsIgnoreCase("quit"));
        }

        System.out.format("""
                Tracks: %s
                Correct: %s
                Incorrect: %s
                Skipped: %s""", tracks, correct, incorrect, skips);
        System.out.println(GREEN + "\nScore: " + (tracks + 10 * correct - 2 * incorrect - skips));
    }

    public static void allGuessing() {
        List<int[]> order = new ArrayList<>();
        for(int i = 0; i < albums.size(); i++) {
            for(int j = 1; j <= Integer.parseInt(albums.get(i).get(1)); j++) {
                order.add(new int[]{i, j});
            }
        }

        Collections.shuffle(order);
        for (int[] id : order) {
            System.out.println("Track " + (order.indexOf(id) + 1) + "/" + order.size());
            randomGuessing(id[0], id[1], false);
            if(INPUT.nextLine().equalsIgnoreCase("quit")) break;
        }
    }

    public static void randomGuessing(int albumSel, int trackSel, boolean random) {
        List<String> album;
        List<String> song;
        if(random) {
            album = albums.get((int) (Math.random() * albums.size()));
            song = getSong(album, (int) (Math.random() * Integer.parseInt(album.get(1)) + 1));
        } else {
            album = albums.get(albumSel);
            song = getSong(album, trackSel);
        }
        String songName = filterSongName(song.get(0));

        boolean giveUp = false;
        String guess;
        do {
            System.out.println(CYAN + randomLine(songName, song, song.size()) + RESET);

            guess = INPUT.nextLine();
            if(guess.equals("")) {
                guess = " ";
                skips++;
                continue;
            }
            if(guess.equalsIgnoreCase("idk")) {
                giveUp = true;
                break;
            }
            if(checkGuess(guess, songName)) break;

            System.out.println(RED + "-X-" + RESET + "\n");
            incorrect++;
        } while(!checkGuess(guess, songName));

        if(giveUp) {
            System.out.print("\n" + RED);
            incorrect++;
        }
        else {
            System.out.print("\n" + GREEN);
            correct++;
        }

        int row = rows.get(0);
        System.out.println(song.get(0).replaceAll("=", "") + RESET + ", " + ITALICS + album.get(0) + RESET + ":");
        if(row > 1) System.out.println(song.get(row - 1));
        System.out.println(song.get(row));
        if(row < song.size() - 1) System.out.println(song.get(row + 1));

        System.out.println("-----");
        tracks++;
    }

    public static List<String> getSong(List<String> album, int track) {
        List<String> song = new ArrayList<>();
        int count = 0;
        for(String line : album) {
            if(line.contains("=")) count++;
            if(count == track) song.add(line);
            if(count > track) return song;
        }

        return song;
    }

    public static String filterSongName(String songName) {
        if(!songName.contains(" (")) return songName.replace("=", "");
        return songName.substring(0, songName.indexOf(" ("));
    }

    public static String randomLine(String songName, List<String> song, int length) {
        songName = songName.replace("&", "and").replace("...", "");

        int row = (int) (Math.random() * (length - 2) + 1);
        if(!rows.isEmpty()) {
            while(rows.contains(row) || song.get(row).equalsIgnoreCase(songName)) row = (int) (Math.random() * (length - 2) + 1);
        }
        rows.add(0, row);

        return replaceName(song.get(row), songName);
    }

    public static String replaceName(String line, String name) {
        String result = line;
        for(int i = 0; i < line.length() - name.length() + 1; i++) {
            if(line.substring(i, i + name.length()).equalsIgnoreCase(name)) {
                result = line.substring(0, i) + "_".repeat(name.length());
                if(i + name.length() < line.length()) {
                    result += line.substring(i + name.length());
                    i += name.length();
                }
            }
        }

        if(name.equals("We Are Never Ever Getting Back Together")) return result.replace("ever getting back together", "__________________________");
        return result;
    }

    public static boolean checkGuess(String guess, String name) {
        if(guess.equalsIgnoreCase(name)) return true;
        name = name.replace("...", "").replace("&", "and").replace("?", "");

        int buffer = 0;
        for(int i = 0; i < name.length(); i++) {
            if(i >= guess.length()) return false;
            if(!guess.substring(i - buffer, i - buffer + 1).equalsIgnoreCase(name.substring(i, i + 1))) {
                if(name.charAt(i) == ',' || name.charAt(i) == '\'') buffer++;
                else return false;
            }
        }

        return true;
    }
}
