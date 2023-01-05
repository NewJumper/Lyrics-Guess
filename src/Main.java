import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    public static List<List<String>> albums = new ArrayList<>();
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\033[1;32m";
    public static final String CYAN = "\u001B[36m";
    public static final String RESET = "\u001B[0m";
    private static final List<Integer> rows = new ArrayList<>();
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

        do randomGuessing();
        while(!INPUT.nextLine().equalsIgnoreCase("quit"));

        System.out.println("Correct: " + correct);
        System.out.println("Incorrect: " + incorrect);
        System.out.println("Skipped: " + skips);
        System.out.println(GREEN + "\nScore: " + (10 * correct - 2 * incorrect - skips));
    }

    public static void randomGuessing() {
        List<String> album = albums.get((int) (Math.random() * albums.size()));
        List<String> song = getSong(album, (int) (Math.random() * Integer.parseInt(album.get(0)) + 1));
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
        System.out.println(song.get(0).replaceAll("=", "") + RESET + ":");
        if(row > 1) System.out.println(song.get(row - 1));
        System.out.println(song.get(row));
        if(row < song.size() - 1) System.out.println(song.get(row + 1));

        System.out.println("-----");
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
        songName = songName.replace("&", "and").replace("...", "");
        if(!songName.contains(" (")) return songName.replace("=", "");
        return songName.substring(0, songName.indexOf(" ("));
    }

    public static String randomLine(String songName, List<String> song, int length) {
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

        int buffer = 0;
        for(int i = 0; i < name.length(); i++) {
            if(!guess.substring(i - buffer, i - buffer + 1).equalsIgnoreCase(name.substring(i, i + 1))) {
                if(name.charAt(i) == ',' || name.charAt(i) == '\'') buffer++;
                else return false;
            }
        }

        return true;
    }
}