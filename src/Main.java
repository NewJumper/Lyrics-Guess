import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static final Scanner INPUT = new Scanner(System.in);
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
    public static final List<Integer> rows = new ArrayList<>();
    public static boolean hardcore;
    public static int tracks;
    public static int correct;
    public static int incorrect;
    public static int lines;

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
                %sSCORES - View the top 5 scores
                """, RESET, YELLOW, YELLOW_L, RESET, YELLOW, YELLOW_L, RESET, YELLOW, BOLD);

        String choice = INPUT.nextLine();
        long start = System.nanoTime();
        if(choice.equalsIgnoreCase("ALL")) {
            System.out.println(BOLD + """
                    \nALL songs chosen
                    Try to get all 188 song names correct! You have infinite guesses!
                    If you want to end the game, type "quit" after the song's name is revealed.
                    """ + RESET);

            TitleGuessing.allSongsGuessing();
            System.out.println(YELLOW_L + "\nALL" + RESET + " songs");
            writer.write("ALL - ");
        } else if(choice.equalsIgnoreCase("RANDOM")) {
            System.out.println(BOLD + """
                    \nRANDOM songs chosen
                    Try to get the song's name, but you only have three guesses!
                    If you want to end the game, type "quit" after the song's name is revealed.
                    Typing "idk" as a guess will immediately end the game.
                    """ + RESET);

            while(TitleGuessing.titleGuessing(0, 0, true) && !INPUT.nextLine().equalsIgnoreCase("quit")) tracks++;
            tracks++;
            System.out.println(YELLOW_L + "\nRANDOM" + RESET + " songs");
            writer.write("RANDOM - ");
        } else if(choice.equalsIgnoreCase("HARDCORE")) {
            hardcore = true;
            System.out.println(RED + "\nHARDCORE challenge" + BOLD + """
                     chosen
                    Try to guess all 188 song names correct with only\s""" + RED + "ONE" + BOLD + " line and " + RED + "ONE" + BOLD + " guess!" + """
                    \nThe game will end if you guess incorrectly!
                    If you want to end the game, type "quit" after the song's name is revealed.
                    """ + RESET);

            TitleGuessing.allSongsGuessing();
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

        long end = System.nanoTime();
        long timeFormat = (end - start) / 60000000000L == 0 ? 1000000000L : 60000000000L;
        int score;
        if(hardcore) {
            System.out.format("Completed: " + tracks);
            score = (int) (11.1d * tracks - (end - start) / 60000000000d + 1);
        } else {
            System.out.format("""
                    Tracks: %s
                    Correct: %s
                    Incorrect: %s
                    Lines Given: %s""", tracks, correct, incorrect, lines);
            score = 10 * correct - 2 * incorrect - lines;
        }
        System.out.println("\nTime: " + (end - start) / timeFormat + " " + (timeFormat == 1000000000L ? "second" : "minute") + ((end - start) / timeFormat != 1 ? "s" : ""));
        System.out.println(GREEN + "Score: " + score);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        writer.write(score + " (" + tracks + ") " + dateFormat.format(new Date()) + "\n");
        writer.flush();
    }

    public static void organizeScores() {
        List<String> topScoring = new ArrayList<>();
        topScoring.add(highScores.get(0));

        for(int i = 1; i < highScores.size(); i++) {
            int score = getScoringInfo(highScores.get(i), true);

            for(int j = i - 1; j >= 0; j--) {
                int exScore = getScoringInfo(topScoring.get(j), true);
                if(score < exScore) {
                    topScoring.add(j + 1, highScores.get(i));
                    break;
                } else if(score == exScore) {
                    for(int k = j; k >= 0; k--) {
                        exScore = getScoringInfo(topScoring.get(k), true);
                        if(score < exScore || getScoringInfo(highScores.get(i), false) >= getScoringInfo(topScoring.get(j), false)) {
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
