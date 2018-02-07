//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//
//import java.io.*;
//import java.util.*;

public class FileNameConverter {

//    // "halite-2-gold-replays_replay-20171120-123300%2B0000--1232402302-240-160-1511181180"
//    private static final String FOLDER_NAME = "C:\\Users\\Nikica\\Downloads\\Halite2_ML-StarterBot-Python_Windows\\data\\20171128";
//    private static final String DESTINATION_FOLDER_NAME = "C:\\Users\\Nikica\\Downloads\\Halite2_ML-StarterBot-Python_Windows\\data\\convert\\all";
////    private static final String FOLDER_NAME = "C:\\Users\\Nikica\\Downloads\\Halite2_ML-StarterBot-Python_Windows\\data\\test";
////    private static final String DESTINATION_FOLDER_NAME = "C:\\Users\\Nikica\\Downloads\\Halite2_ML-StarterBot-Python_Windows\\data\\test";

    public static void main(String[] args) {
//        parseFiles();
//        printSize();
    }

//    private static void printSize() {
//        File folder = new File(DESTINATION_FOLDER_NAME);
//        Map<String, Integer> map = new HashMap<>();
//        for (String filename : Objects.requireNonNull(folder.list())) {
//            File f = new File(DESTINATION_FOLDER_NAME + "\\" + filename);
//            if (f.isDirectory()) {
//                map.put(filename, Objects.requireNonNull(f.list()).length);
//                // System.out.println(filename + " :: " + f.list().length);
//            }
//        }
//
//        map.entrySet().stream()
//                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
//                .forEach(System.out::println); // or any other terminal method
//    }
//
//    private static void parseFiles() {
//        File folder = new File(FOLDER_NAME);
//        int count = 0;
//
//        for (String filename : folder.list()) {
//            if (++count % 25 == 0) {
//                System.out.println(count);
//            }
//
//            final String content = fileToString(filename);
//            final String winner = getWinnerName(content);
//            final String newFilename = getFilenameForTrainig(filename);
//            writeFileForTraining(content, winner, newFilename);
//        }
//    }
//
//    private static void writeFileForTraining(final String content, final String winner, final String newFilename) {
//        BufferedWriter out = null;
//        try {
//            // File folder = new File(DESTINATION_FOLDER_NAME + "\\" + winner);
//            File folder = new File(DESTINATION_FOLDER_NAME);
//            if (!folder.exists()) {
//                final boolean mkdir = folder.mkdir();
//                if (!mkdir) throw new Exception("Folder did not created");
//            }
//            // out = new BufferedWriter(new FileWriter(DESTINATION_FOLDER_NAME + "\\" + winner + "\\" + newFilename));
//            out = new BufferedWriter(new FileWriter(DESTINATION_FOLDER_NAME + "\\" + newFilename));
//            out.write(content);
//            // PrintWriter writer = new PrintWriter(DESTINATION_FOLDER_NAME + "\\" + winner + "\\" + newFilename);
//            // writer.println(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private static String getFilenameForTrainig(final String filename) {
//        String newName = filename.replace("halite-2-gold-replays_", "");
//        newName = newName.replace("%2B", "+");
//        newName = newName.substring(0, newName.indexOf("+") + 4) + newName.substring(newName.length() - 12);
//        return newName;
//    }
//
//    private static String getWinnerName(final String content) {
//        JSONParser parser = new JSONParser();
//        try {
//            JSONObject o = (JSONObject) parser.parse(content);
//            JSONObject stats = (JSONObject) o.get("stats");
//            String winnerId = null;
//            for (Iterator i = stats.keySet().iterator(); i.hasNext(); ) {
//                String key = (String) i.next();
//                JSONObject value = (JSONObject) stats.get(key);
//                if ("1".equals(String.valueOf(value.get("rank")))) {
//                    winnerId = key;
//                    break;
//                }
//            }
//            // System.out.println("Winner id = " + winnerId);
//            JSONArray playerNames = (JSONArray) o.get("player_names");
//            // System.out.println("Winner = " + playerNames.get(Integer.valueOf(winnerId)));
//            assert winnerId != null;
//            return (String) playerNames.get(Integer.valueOf(winnerId));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private static String fileToString(final String filename) {
//        try {
//            Scanner scanner = new Scanner(new File(FOLDER_NAME + "\\" + filename));
//            while (scanner.hasNext()) {
//                String line = scanner.nextLine();
//                line = line.substring(2);
//                line = line.substring(0, line.length() - 1);
//                return line;
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

//    private static void renameFiles() {
//        File folder = new File(FOLDER_NAME);
//        for (String filename : folder.list()) {
//            File oldFile = new File(FOLDER_NAME + "\\" + filename);
//            String newName = filename.replace("halite-2-gold-replays_", "");
//            newName = newName.replace("%2B", "+");
//            newName = newName.substring(0, newName.indexOf("+") + 4) + newName.substring(newName.length() - 12);
//            File newFile = new File(FOLDER_NAME + "\\" + newName);
//
//            final boolean success = oldFile.renameTo(newFile);
//
//            if (!success) {
//                System.out.println(filename + " was not renamed to " + newName);
//            }
//        }
//    }
//
//    private static void sanitizeReplay() {
//        File folder = new File(FOLDER_NAME);
//        for (String filename : folder.list()) {
//            try {
//                Scanner scanner = new Scanner(new File(FOLDER_NAME + "\\" + filename));
//                PrintWriter writer = new PrintWriter(DEST_FOLDER_NAME + "\\" + filename);
//                while (scanner.hasNext()) {
//                    String line = scanner.nextLine();
//                    line = line.substring(2);
//                    line = line.substring(0, line.length() - 1);
//                    if (!line.isEmpty()) {
//                        writer.write(line);
//                        // writer.write("\n");
//                    }
//                }
//                scanner.close();
//                writer.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

}
