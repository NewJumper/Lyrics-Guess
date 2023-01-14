import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TitleGuessing {
    static List<List<String>> albums = Main.albums;
    static String RESET = Main.RESET;
    static List<Integer> rows = Main.rows;
    static boolean hardcore = Main.hardcore;

    public static void allSongsGuessing() {
        List<int[]> order = new ArrayList<>();
        for(int i = 0; i < albums.size(); i++) {
            for(int j = 1; j <= Integer.parseInt(albums.get(i).get(1)); j++) {
                order.add(new int[]{i, j});
            }
        }

        Collections.shuffle(order);
        for (int[] id : order) {
            System.out.println(Main.WHITE + "Track " + (order.indexOf(id) + 1) + "/" + order.size() + RESET);
            if(!titleGuessing(id[0], id[1], false) && hardcore) break;
            Main.tracks++;
            if(Main.INPUT.nextLine().equalsIgnoreCase("quit")) break;
        }
    }

    public static boolean titleGuessing(int albumSel, int trackSel, boolean random) {
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
            System.out.println(Main.CYAN + randomLine(songName, song, song.size()) + RESET);
            Main.lines++;
            maxLines--;

            guess = Main.INPUT.nextLine().trim();
            if(hardcore || (random && maxLines == 0)) {
                while(guess.equals("")) guess = Main.INPUT.nextLine();
                if(!checkGuess(songName, guess)) failed = true;
                break;
            }
            if(guess.equals("")) continue;
            if(guess.equalsIgnoreCase("idk")) {
                failed = true;
                break;
            }
            if(checkGuess(songName, guess)) break;
            if(random) {
                Main.incorrect++;
                if(guess.equals("quit")) break;
            }
            System.out.println(Main.RED + "-X-" + RESET + "\n");
        } while(!checkGuess(songName, guess));

        if(failed) {
            System.out.print("\n" + Main.RED);
            Main.incorrect++;
        } else {
            System.out.print("\n" + Main.GREEN);
            Main.correct++;
        }

        int row = rows.get(0);
        System.out.println(song.get(0).replaceAll("=", "") + RESET + ", " + Main.ITALICS + album.get(0) + RESET + ":");
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
        if(!rows.isEmpty() || song.get(row).equalsIgnoreCase(songName) || checkValidCase(song.get(row))) {
            while(rows.contains(row) || song.get(row).equalsIgnoreCase(songName) || checkValidCase(song.get(row)))
                row = (int) (Math.random() * (length - 2) + 1);
        }
        rows.add(0, row);

        return replaceName(song.get(row), songName);
    }

    public static boolean checkValidCase(String line) {
        int rep = line.indexOf(' ');
        if(hardcore && (rep == line.indexOf(' ', rep + 1) || rep == line.lastIndexOf(' ') || line.indexOf(' ', rep + 1) == line.lastIndexOf(' '))) return false;
        return line.contains("And we were happy") ||
                line.contains("Beautiful tragic") ||
                line.contains("But we are never, ever, ever, ever getting back together") ||
                line.contains("Bye, bye, baby") ||
                line.contains("Don't you ever grow up") ||
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
        if(name.equals("Mr. Perfectly Fine")) return result.replace("Mr.", "__").replace("Mr. \"Perfectly fine\"", "__ \"_________ ____\"");
        if(name.equals("Snow On The Beach")) return result.replace("snow at the beach", "____ __ ___ _____");
        if(name.equals("the 1")) return result.replace("the one", "___ _");
        return result;
    }

    public static boolean checkGuess(String name, String guess) {
        if(guess.length() == 0) return false;
        if(name.equalsIgnoreCase(guess)) return true;

        name = name.replace("&", "and");
        if(name.equals("Come Back...Be Here")) name = "Come Back... Be Here";
        if(guess.length() > name.length()) return false;

        int buffer = 0;
        for(int i = 0; i < name.length(); i++) {
            if(i >= guess.length() + buffer || !guess.substring(i - buffer, i - buffer + 1).equalsIgnoreCase(name.substring(i, i + 1))) {
                if(name.charAt(i) == '\'' || name.charAt(i) == '.' || name.charAt(i) == ',' || name.charAt(i) == '!' || name.charAt(i) == '?') buffer++;
                else return false;
            }

            if(i == name.length() - 1 && i != guess.length() + buffer - 1) return false;
        }

        return true;
    }
}
