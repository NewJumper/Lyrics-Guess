import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final Scanner INPUT = new Scanner(System.in);
    public static List<List<String>> albums = new ArrayList<>();
    public static List<String> scores = new ArrayList<>();
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[1;32m";
    public static final String CYAN = "\033[36m";
    public static final String PURPLE = "\033[1;35m";
    public static final String PURPLE_L = "\u001B[1;95m";
    public static final String WHITE = "\033[1m";
    public static final String BOLD = "\033[1;37m";
    public static final String ITALICS = "\033[3;37m";
    public static final String RESET = "\033[0m";
    public static final List<Integer> rows = new ArrayList<>();
    public static boolean hardcore;
    public static boolean opening;
    public static boolean closing;
    public static boolean zen;
    public static boolean random;
    public static int maxRankings;
    public static int tracks;
    public static int correct;
    public static int incorrect;
    public static int lines;

    public static boolean debug;

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

        scores = Files.readAllLines(Paths.get("src/scores.txt"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/scores.txt", true));

        System.out.format(PURPLE_L + """
                \nWhich gamemode do you want to play?
                ALL %s-%s Guess all 188 songs to complete the game.
                %sRANDOM %s-%s Set a high score by guessing songs that are randomly chosen.
                %sTIME ATTACK %s-%s Identify as many lines in two minutes.
                %sSCORES - View the top scores
                """, RESET, PURPLE, PURPLE_L, RESET, PURPLE, PURPLE_L, RESET, PURPLE, BOLD);

        String choice = INPUT.nextLine();
        long start = System.nanoTime();

        if(choice.equalsIgnoreCase("-d")) {
            debug = true;
            choice = INPUT.nextLine();
        }

        switch(choice.toUpperCase()) {
            case "ALL" -> {
                System.out.format(PURPLE_L + """
                        \nSelect a difficulty:
                        NORMAL %s-%s Infinite guesses.
                        %sHARDCORE %s-%s Guess with only one line and one guess.
                        %sOPENING %s-%s Guess with only the first two lines.
                        %sCLOSING %s-%s Guess with only the last two lines.
                        %sZEN %s-%s Guess with two neighboring lines.
                        """, RESET, PURPLE, PURPLE_L, RESET, PURPLE, PURPLE_L, RESET, PURPLE, PURPLE_L, RESET, PURPLE, PURPLE_L, RESET, PURPLE);

                choice = INPUT.nextLine();
                switch(choice.toUpperCase()) {
                    case "NORMAL" -> {
                        System.out.println(RESET + WHITE + "\nNORMAL mode " + BOLD + """
                                chosen
                                Try to guess all the songs' name from its lines!
                                You have infinite guesses!
                                If you want to end the game, type "quit" after the song's name is revealed.
                                """ + RESET);

                        TitleGuessing.allSongsGuessing();
                        System.out.println(PURPLE_L + "\nNORMAL" + RESET + " mode");
                        writer.write("NORMAL - ");
                    }
                    case "HARDCORE" -> {
                        hardcore = true;
                        System.out.println(RED + "\nHARDCORE challenge " + BOLD + """
                                chosen
                                Try to guess the song's name with only\s""" + RED + "ONE" + BOLD + " line and " + RED + "ONE" + BOLD + " guess!" + """
                                \nThe game will end if you guess incorrectly!
                                If you want to end the game, type "quit" after the song's name is revealed.
                                """ + RESET);

                        TitleGuessing.allSongsGuessing();
                        System.out.println(RED + "\nHARDCORE CHALLENGE" + RESET);
                        writer.write("HARDCORE - ");
                    }
                    case "OPENING" -> {
                        opening = true;
                        System.out.println(RESET + WHITE + "\nOPENING lines " + BOLD + """
                                chosen
                                Try to guess the song's name from its first two lines!
                                You only have three guesses!
                                If you want to end the game, type "quit" after the song's name is revealed.
                                """ + RESET);

                        TitleGuessing.allSongsGuessing();
                        System.out.println(PURPLE_L + "\nOPENING" + RESET + " lines");
                        writer.write("OPENING - ");
                    }
                    case "CLOSING" -> {
                        closing = true;
                        System.out.println(RESET + WHITE + "\nCLOSING lines " + BOLD + """
                                chosen
                                Try to guess the song's name from its last two lines!
                                You only have three guesses!
                                If you want to end the game, type "quit" after the song's name is revealed.
                                """ + RESET);

                        TitleGuessing.allSongsGuessing();
                        System.out.println(PURPLE_L + "\nCLOSING" + RESET + " lines");
                        writer.write("CLOSING - ");
                    }
                    case "ZEN" -> {
                        zen = true;
                        writer.flush();
                        return;
                    }
                    default -> {
                        System.out.println(ITALICS + choice + RESET + " is not a game!");
                        writer.flush();
                        return;
                    }
                }
            }
            case "RANDOM" -> {
                random = true;
                System.out.println(RESET + WHITE + "\nRANDOM songs " + BOLD + """
                        chosen
                        Try to get the song's name, but you only have three guesses!
                        If you want to end the game, type "quit" after the song's name is revealed.
                        Typing "idk" as a guess will immediately end the game.
                        """ + RESET);

                while(TitleGuessing.titleGuessing(0, 0) && !INPUT.nextLine().equalsIgnoreCase("quit")) tracks++;
                tracks++;
                System.out.println(PURPLE_L + "\nRANDOM" + RESET + " songs");
                writer.write("RANDOM - ");
            }
            case "TIME ATTACK" -> {
                System.out.println(ITALICS + choice + RESET + " is not a game, yet!");
                writer.flush();
                return;
            }
            default -> {
                if(choice.toUpperCase().contains("SCORES")) {
                    if(choice.length() == 6) maxRankings = 5;
                    else maxRankings = Math.max(3, Math.min(Integer.parseInt(choice.substring(7)), 15));
                    organizeScores();
                    writer.flush();
                    return;
                }
                System.out.println(ITALICS + choice + RESET + " is not a game!");
                writer.flush();
                return;
            }
        }

        long end = System.nanoTime();
        double minutes = (end - start) / 60000000000d;
        long timeFormat = minutes == 0 ? 1000000000L : 60000000000L;
        int score = 0;
        if(!hardcore && !zen) {
            System.out.format("""
                    Tracks: %s
                    Correct: %s
                    Incorrect: %s""", tracks, correct, incorrect);
            if(!(opening || closing)) {
                System.out.print("\nLines Given: " + lines);
                score = (int) (10 * correct - 2 * incorrect - lines - minutes);
            } else score = (int) (5 * correct - 2 * incorrect - minutes);
        } else {
            System.out.println("Completed: " + tracks + " tracks");
            if(hardcore) score = (int) (11.1d * tracks - minutes + 1);
        }

        System.out.println("\nTime: " + (end - start) / timeFormat + " " + (timeFormat == 1000000000L ? "second" : "minute") + ((end - start) / timeFormat != 1 ? "s" : ""));
        if(!zen) System.out.println(GREEN + "Score: " + score);
        else return;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        writer.write(score + " (" + tracks + ") " + dateFormat.format(new Date()) + "\n");
        writer.flush();
    }

    public static void organizeScores() {
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

        while(leaderboard.size() > maxRankings) leaderboard.remove(maxRankings);

        for(int i = 0; i < leaderboard.size(); i++) {
            String scoring = leaderboard.get(i);
            String spacedScoring = scoring.substring(0, scoring.indexOf(")") + 2);
            spacedScoring += " " + "-".repeat(23 - spacedScoring.length()) + "  " + scoring.substring(scoring.indexOf(")") + 2);
            if(spacedScoring.contains("HARDCORE")) spacedScoring = RED + "HARDCORE" + RESET + spacedScoring.substring(spacedScoring.indexOf(" - "));

            leaderboard.set(i, spacedScoring);
        }

        System.out.println(GREEN + "\nTOP " + maxRankings + " SCORES" + RESET + ":" + BOLD + "\n    MODE - score (tracks)     Date" + RESET);
        for(int i = 0; i < leaderboard.size(); i++) System.out.println((i + 1) + (i > 8 ? ". " : ".  ") + leaderboard.get(i));
    }

    public static int getScoringInfo(String text, boolean score) {
        return Integer.parseInt(score ? text.substring(text.indexOf("- ") + 2, text.indexOf("(") - 1) : text.substring(text.indexOf("(") + 1, text.indexOf(")")));
    }
}
