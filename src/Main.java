import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    public static List<List<String>> albums = new ArrayList<>();
    public static List<String> highScores = new ArrayList<>();
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[1;32m";
    public static final String CYAN = "\033[36m";
    public static final String YELLOW = "\033[1;33m";
    public static final String YELLOW_L = "\u001B[1;93m";
    public static final String BOLD = "\033[1;37m";
    public static final String ITALICS = "\033[3;37m";
    public static final String RESET = "\033[0m";
    private static final List<Integer> rows = new ArrayList<>();
    private static boolean hardcore;
    private static int tracks;
    private static int correct;
    private static int incorrect;
    private static int lines;

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

        highScores = Files.readAllLines(Paths.get("src/scores.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/scores.txt", true));

        System.out.format(YELLOW_L + """
                \nWhich gamemode do you want to play?
                ALL %s-%s You have to guess all 188 songs to complete the game.
                %sRANDOM %s-%s Set a high score by guessing songs that are randomly chosen.
                %sHARDCORE %s-%s Guess the song from only one line and one guess.
                %sSCORES - View the top 5 scores.
                """, RESET, YELLOW, YELLOW_L, RESET, YELLOW, YELLOW_L, RESET, YELLOW, BOLD);

        long start = 0;
        String choice = INPUT.nextLine();
        if(choice.equalsIgnoreCase("ALL")) {
            System.out.println(BOLD + """
                    \nALL songs chosen
                    Try to get all 188 song names correct! You have infinite guesses!
                    If you want to end the game, type "quit" after the song's name is revealed.
                    """ + RESET);
            allGuessing();
            System.out.println(YELLOW_L + "\nALL" + RESET + " songs");
            writer.write("ALL - ");
        } else if(choice.equalsIgnoreCase("RANDOM")) {
            System.out.println(BOLD + """
                    \nRANDOM songs chosen
                    Try to get the song's name, but you only have three guesses!
                    If you want to end the game, type "quit" after the song's name is revealed.
                    Typing "idk" as a guess will immediately end the game.
                    """ + RESET);
            while(randomGuessing(0, 0, true) && !INPUT.nextLine().equalsIgnoreCase("quit")) tracks++;
            tracks++;
            System.out.println(YELLOW_L + "\nRANDOM" + RESET + " songs");
            writer.write("RANDOM - ");
        } else if(choice.equalsIgnoreCase("HARDCORE")) {
            hardcore = true;
            start = System.nanoTime();
            System.out.println(RED + "\nHARDCORE challenge" + BOLD + """
                     chosen
                    Try to guess all 188 song names correct with only\s""" + RED + "ONE" + BOLD + " line and " + RED + "ONE" + BOLD + " guess!" + """
                    \nThe game will end if you guess incorrectly!
                    If you want to end the game, type "quit" after the song's name is revealed.
                    """ + RESET);
            allGuessing();
            System.out.println(RED + "\nHARDCORE CHALLENGE" + RESET);
            writer.write("HARDCORE - ");
        } else if(choice.equalsIgnoreCase("SCORES")) {
            organizeScores();
            writer.flush();
            return;
        } else {
            System.out.println(ITALICS + choice + RESET + " is not a game!");
            writer.flush();
            return;
        }

        int score;
        if(hardcore) {
            long end = System.nanoTime();
            long timeFormat = (end - start) / 60000000000L == 0 ? 1000000000L : 60000000000L;
            System.out.format("""
                    Completed: %s
                    Time: %s %s""", tracks, (end - start) / timeFormat, timeFormat == 1000000000L ? "seconds" : "minutes");
            score = (int) (11L * tracks - (end - start) / 60000000000L + 1);
            System.out.println(GREEN + "\nScore: " + score);
        } else {
            System.out.format("""
                    Tracks: %s
                    Correct: %s
                    Incorrect: %s
                    Lines Given: %s""", tracks, correct, incorrect, lines);
            score = 10 * correct - 2 * incorrect - lines;
            System.out.println(GREEN + "\nScore: " + score);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        writer.write(score + " (" + tracks + ") " + dateFormat.format(new Date()) + "\n");
        writer.flush();
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
            System.out.println("\033[1mTrack " + (order.indexOf(id) + 1) + "/" + order.size() + RESET);
            if(!randomGuessing(id[0], id[1], false) && hardcore) break;
            tracks++;
            if(INPUT.nextLine().equalsIgnoreCase("quit")) break;
        }
    }

    public static boolean randomGuessing(int albumSel, int trackSel, boolean random) {
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

        String guess;
        int maxLines = 4;
        boolean failed = false;
        do {
            System.out.println(CYAN + randomLine(songName, song, song.size()) + RESET);
            lines++;
            maxLines--;

            guess = INPUT.nextLine();
            if(hardcore || (random && maxLines == 0)) {
                while(guess.equals("")) guess = INPUT.nextLine();
                if(!checkGuess(guess, songName)) failed = true;
                break;
            }
            if(guess.equals("")) continue;
            if(guess.equalsIgnoreCase("idk")) {
                failed = true;
                break;
            }
            if(checkGuess(guess, songName)) break;
            if(random) {
                incorrect++;
                if(guess.equals("quit")) break;
            }
            System.out.println(RED + "-X-" + RESET + "\n");
        } while(!checkGuess(guess, songName));

        if(failed) {
            System.out.print("\n" + RED);
            incorrect++;
        } else {
            System.out.print("\n" + GREEN);
            correct++;
        }

        int row = rows.get(0);
        System.out.println(song.get(0).replaceAll("=", "") + RESET + ", " + ITALICS + album.get(0) + RESET + ":");
        if(row > 1) System.out.println(song.get(row - 1));
        System.out.println(song.get(row));
        if(row < song.size() - 1) System.out.println(song.get(row + 1));

        System.out.println("-----");
        rows.clear();

        if(hardcore && failed) return false;
        return !failed;
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
            while(rows.contains(row) || song.get(row).equalsIgnoreCase(songName) || checkGiveaways(song.get(row))) row = (int) (Math.random() * (length - 2) + 1);
        }
        rows.add(0, row);

        return replaceName(song.get(row), songName);
    }

    public static boolean checkGiveaways(String line) {
        return line.contains("And we were happy") ||
                line.contains("But we are never, ever, ever, ever getting back together") ||
                line.contains("Bye, bye, baby") ||
                line.contains("Don't you ever grow up") ||
                line.contains("Ooh-ah, you'll get better") ||
                line.contains("we are never, ever, ever getting back together")||
                line.contains("You should've said, \"No\"");
    }

    public static String replaceName(String line, String name) {
        String result = line;
        for(int i = 0; i < line.length() - name.length() + 1; i++) {
            if(line.substring(i, i + name.length()).equalsIgnoreCase(name)) {
                StringBuilder blanks = new StringBuilder();
                for(int j = 0; j < name.length(); j++) {
                    if(name.toLowerCase().charAt(j) >= 'a' && name.toLowerCase().charAt(j) <= 'z') blanks.append('_');
                    else if(name.charAt(j) == ' ') blanks.append(' ');
                    else blanks.append(name.charAt(j));
                }
                result = result.substring(0, i) + blanks;
                if(i + name.length() < line.length()) {
                    result += line.substring(i + name.length());
                    i += name.length();
                }
            }
        }

        if(name.equals("Snow On The Beach")) return result.replace("snow at the beach", "____ __ ___ _____");
        if(name.equals("the 1")) return result.replace("the one", "___ _");
        return result;
    }

    public static boolean checkGuess(String guess, String name) {
        if(guess.length() == 0 || (!guess.contains("and") && guess.length() > name.length())) return false;
        if(guess.equalsIgnoreCase(name)) return true;
        name = name.replace("...", "").replace("&", "and").replace("?", "");

        int buffer = 0;
        for(int i = 0; i < name.length(); i++) {
            if(i >= guess.length() + buffer) return false;
            if(!guess.substring(i - buffer, i - buffer + 1).equalsIgnoreCase(name.substring(i, i + 1))) {
                if(name.charAt(i) == ',' || name.charAt(i) == '\'' || name.charAt(i) == '.') buffer++;
                else return false;
            }
        }

        return true;
    }

    public static void organizeScores() {
        List<String> topScoring = new ArrayList<>();

        topScoring.add(highScores.get(0));
        for(int i = 1; i < highScores.size(); i++) {
            int score = getScoringInfo(highScores.get(i), true);
            int track = getScoringInfo(highScores.get(i), false);
            for(int j = i - 1; j >= 0; j--) {
                int exScore = getScoringInfo(topScoring.get(j), true);
                int exTrack = getScoringInfo(topScoring.get(j), false);

                if(score < exScore) {
                    topScoring.add(j + 1, highScores.get(i));
                    break;
                } else if(score == exScore) {
                    for(int k = j; k >= 0; k--) {
                        exScore = getScoringInfo(topScoring.get(k), true);
                        if(score < exScore || track >= exTrack) {
                            topScoring.add(k + 1, highScores.get(i));
                            break;
                        } else if (k == 0) topScoring.add(0, highScores.get(i));
                    }
                    break;
                } else if(j == 0) topScoring.add(0, highScores.get(i));
            }
        }
        while(topScoring.size() > 5) topScoring.remove(5);

        for(int i = 0; i < topScoring.size(); i++) {
            String scoring = topScoring.get(i);
            String spacedScoring = scoring.substring(0, scoring.indexOf(")") + 2);
            spacedScoring += " " + "-".repeat(20 - spacedScoring.length()) + "  " + scoring.substring(scoring.indexOf(")") + 2);
            if(spacedScoring.contains("HARDCORE")) spacedScoring = RED + "HARDCORE" + RESET + spacedScoring.substring(spacedScoring.indexOf(" - "));

            topScoring.set(i, spacedScoring);
        }

        System.out.println(GREEN + "\nTOP 5 SCORES" + RESET + ":" + BOLD + "\n   MODE - score (tracks)  Date" + RESET);
        for(int i = 0; i < topScoring.size(); i++) System.out.println((i + 1) + ". " + topScoring.get(i));
    }

    public static int getScoringInfo(String text, boolean score) {
        return Integer.parseInt(score ? text.substring(text.indexOf("- ") + 2, text.indexOf("(") - 1) : text.substring(text.indexOf("(") + 1, text.indexOf(")")));
    }
}
