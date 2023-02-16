package gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenerateLine {
    public static List<List<String>> albums = new ArrayList<>();
    public static List<String> rows = new ArrayList<>();

    public static List<String> randomSong() throws IOException {
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/taylor swift.txt")));
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/fearless.txt")));
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/speak now.txt")));
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/red.txt")));
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/1989.txt")));
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/reputation.txt")));
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/lover.txt")));
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/folklore.txt")));
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/evermore.txt")));
        albums.add(Files.readAllLines(Paths.get("Application/src/main/resources/albums/midnights.txt")));

        List<int[]> order = new ArrayList<>();
        for(int i = 0; i < albums.size(); i++) {
            for(int j = 1; j <= Integer.parseInt(albums.get(i).get(1)); j++) {
                order.add(new int[]{i, j});
            }
        }
        Collections.shuffle(order);

        return getSong(albums.get(order.get(0)[0]), order.get(0)[1]);
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

    public static String randomLine(List<String> song) {
        String songName = filterSongName(song.get(0));
        songName = songName.replace("&", "and").replace("...", "").replace("?", "");

        int counter = 0;
        int row = (int) (Math.random() * (song.size() - 2) + 1);
        while(rows.contains(song.get(row)) || song.get(row).equalsIgnoreCase(songName) || checkValidCase(song.get(row))) {
            counter++;
            if(counter == song.size()) return replaceName(rows.get(0), songName);

            row = (int) (Math.random() * (song.size() - 2) + 1);
        }

        rows.add(0, song.get(row));
        return replaceName(song.get(row), songName);
    }

    public static String filterSongName(String songName) {
        if(!songName.contains(" (")) return songName.replace("=", "");
        return songName.substring(0, songName.indexOf(" ("));
    }

    public static boolean checkValidCase(String line) {
        return line.contains("And we were happy") ||
                line.contains("Beautiful tragic") ||
                line.contains("But we are never, ever, ever, ever getting back together") ||
                line.contains("Bye, bye, baby") ||
                line.contains("Don't you ever grow up") ||
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

        if(name.equals("Dancing With Our Hands Tied")) return result.replace("_, hands tied", "_, _____ ____");
        if(name.equals("Mary's Song")) return result.replace("Oh my, my, my", "__ __, __, __");
        if(name.equals("Me!")) return result.replace("\"me\"", "\"__\"").replace("me-e-e", "__-_-_").replace(" me", " __").replace("after __", "after me").replace("Let __", "Let me");
        if(name.equals("Mr. Perfectly Fine")) return result.replace("Mr.", "__").replace("Mr. \"Perfectly fine\"", "__ \"_________ ____\"");
        if(name.equals("Snow On The Beach")) return result.replace("snow at the beach", "____ __ ___ _____");
        if(name.equals("the 1")) return result.replace("the one", "___ _");
        return result;
    }
}
