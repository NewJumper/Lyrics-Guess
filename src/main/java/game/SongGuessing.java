package game;

import gui.SettingsMenu;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class SongGuessing {
    public static List<List<String>> albums = new ArrayList<>();
    public static List<int[]> order = new ArrayList<>();
    public static LinkedHashMap<Integer, String> history = new LinkedHashMap<>();
    public static boolean capped;
    private static int counter;

    public static void randomSong() throws IOException {
        albums.clear();
        order.clear();

        String artistPath = switch (SettingsMenu.artist) {
            case "AJ" -> "ajr";
            case "FC" -> "friday_pilots_club";
            case "SC" -> "sabrina_carpenter";
            case "TP" -> "twenty_one_pilots";
            default -> "taylor_swift";
        };

        File[] artistPaths = new File("src/main/resources/lyrics").listFiles();
        assert artistPaths != null;

        for(int i = 0; i < (SettingsMenu.artist.equals("AA") ? artistPaths.length : 1); i++) {
            File[] albumPaths;
            if(SettingsMenu.artist.equals("AA")) albumPaths = new File(artistPaths[i].toURI()).listFiles();
            else albumPaths = new File("src/main/resources/lyrics/" + artistPath).listFiles();
            assert albumPaths != null;
            for(File album : albumPaths) albums.add(Files.readAllLines(Paths.get(album.toURI())));
        }

        for(int i = 0; i < albums.size(); i++) {
            for(int j = 1; j <= Integer.parseInt(albums.get(i).get(1)); j++) {
                order.add(new int[]{i, j});
            }
        }
        Collections.shuffle(order);
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

    public static void randomLine(List<String> song) {
        String songName = filterSongName(song.get(0)).replace("&", "and").replace("...", "").replace("?", "");
        int counter = 0;
        int row = (int) (Math.random() * (song.size() - 2) + 1);
        capped = false;

        while(history.containsValue(song.get(row)) || song.get(row).equalsIgnoreCase(songName) || checkValidCase(song.get(row))) {
            counter++;
            if(counter == song.size()) {
                capped = true;
                return;
            }

            row = (int) (Math.random() * (song.size() - 2) + 1);
        }

        history.put(row, song.get(row));
    }

    public static List<String> randomWord(List<String> song) {
        int row = (int) (Math.random() * (song.size() - 2) + 1);
        String line = song.get(row);
        history.put(row, line);
        int wordCount = (int) (Math.random() * (line.chars().filter(c -> c == ' ').count() + 1));

        int loc = 0;
        while(wordCount != 0) {
            if(line.charAt(loc) == ' ') wordCount--;
            loc++;
        }

        String word;
        if(line.indexOf(' ') == -1) word = line;
        else {
            int lastIndex = line.indexOf(' ', loc) == -1 ? line.length() : line.indexOf(' ', loc);
            word = line.substring(loc, lastIndex).replaceAll("\"", "").replaceAll(",", "").replaceAll("[.]", "").replaceAll("[?]", "").replaceAll("[(]", "").replaceAll("[)]", "");
        }
        if(word.charAt(word.length() - 1) == '\'') word = word.substring(0, word.length() - 1);

        List<String> songsContainingWord = checkRecurrence(word);
        if(songsContainingWord.size() <= 5) {
            songsContainingWord.add(0, word + "|" + songsContainingWord.size());
            System.out.println(songsContainingWord);
            return songsContainingWord;
        }

        history.remove(row);
        counter++;
        if(counter == 1000) return List.of("NCW-ER|");
        return randomWord(song);
    }

    public static List<String> checkRecurrence(String word) {
        List<String> songsWithWord = new ArrayList<>();

        for(List<String> album : albums) {
            for(int i = 3; i < album.size(); i++) {
                if(album.get(i).toLowerCase().contains(word.toLowerCase()) && !album.get(i).contains("=")) {
                    String song = getSongNameFromRow(album, i);
                    if(songsWithWord.contains(song)) continue;
                    songsWithWord.add(song);
                }
            }
        }

        return songsWithWord;
    }

    public static String getSongNameFromRow(List<String> album, int row) {
        for(int i = row; i > 1; i--) if(album.get(i).contains("=")) return album.get(i);
        throw new IndexOutOfBoundsException("\"" + album.get(row) + "\" is not from a song. Row: " + row);
    }

    public static String filterSongName(String songName) {
        if(!songName.contains(" (") || songName.contains("Finale")) return songName.replace("=", "");
        return songName.substring(0, songName.indexOf(" ("));
    }

    public static boolean checkValidCase(String line) {
        return line.contains("And we were happy") ||
                line.contains("Beautiful tragic") ||
                line.contains("But we are never, ever, ever, ever getting back together") ||
                line.contains("Bye, bye, baby") ||
                line.contains("Come back, be here") ||
                line.contains("Come back, be here, come back, be here") ||
                line.contains("Don't you ever grow up") ||
                line.contains("Look what you just made me...") ||
                line.contains("Look what you just made me do") ||
                line.contains("Oh, all you had to do was-") ||
                line.contains("Ooh-ah, you'll get better") ||
                line.contains("We are never, ever, ever getting back together")||
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
                }
                result = result.substring(0, i) + blanks;

                if(i + name.length() < line.length()) {
                    result += line.substring(i + name.length());
                    i += name.length();
                }
            }
        }

        if(name.equals("prfct")) return result.replace("perfect", "_______");
        if(name.equals("Diamonds Are Forever")) return result.replace("diamonds", "________");

        if(name.equals("Bye Bye Baby")) return result.replace("Bye, bye", "___, ___");
        if(name.equals("Dancing With Our Hands Tied")) return result.replace("_, hands tied", "_, _____ ____");
        if(name.equals("Mary's Song")) return result.replace("Oh my, my, my", "__ __, __, __");
        if(name.equals("Me!")) return result.replace("\"me\"", "\"__\"").replace("me-e-e", "__-_-_").replace(" me", " __").replace("after __", "after me").replace("Let __", "Let me");
        if(name.equals("Mr. Perfectly Fine")) return result.replace("Mr.", "__").replace("Mr. \"Perfectly fine\"", "__ \"_________ ____\"");
        if(name.equals("Snow On The Beach")) return result.replace("snow at the beach", "____ __ ___ _____");
        if(name.equals("the 1")) return result.replace("the one", "___ _");
        if(name.equals("The Last Time")) return result.replace("last time", "____ ____");
        return result;
    }

    public static boolean checkGuess(List<String> names, String guess) {
        for(String name : names) if(checkGuess(name, guess)) return true;
        return false;
    }

    public static boolean checkGuess(String name, String guess) {
        name = filterSongName(name);
        if(guess.length() == 0) return false;
        if(name.equalsIgnoreCase(guess)) return true;

        name = guessingEdgeCases(name.replace("&", "and"), guess);
        if(guess.length() > name.length()) return false;

        int buffer = 0;
        for(int i = 0; i < name.length(); i++) {
            if(i >= guess.length() + buffer || !guess.substring(i - buffer, i - buffer + 1).equalsIgnoreCase(name.substring(i, i + 1))) {
                if(name.charAt(i) == '\'' || name.charAt(i) == '.' || name.charAt(i) == ',' || name.charAt(i) == '!' || name.charAt(i) == '?' || name.charAt(i) == '-') buffer++;
                else return false;
            }

            if(i == name.length() - 1 && i != guess.length() + buffer - 1) return false;
        }

        return true;
    }

    private static String guessingEdgeCases(String name, String guess) {
        if(name.equals("Finale (Can't Wait To See What You Do Next)") && guess.length() == 6) return "Finale";
        if(name.equals("Finale (Can't Wait To See What You Do Next)") && guess.length() > 6) return "Can't Wait To See What You Do Next";
        if(name.equals("Turning Out Pt. ii")) return "Turning Out 2";

        if(name.equals("prfct")) return "Perfect";

        if(name.equals("Anti-Hero") && guess.length() == 8) return "AntiHero";
        if(name.equals("Come Back...Be Here")) return "Come Back... Be Here";
        return name;
    }
}
