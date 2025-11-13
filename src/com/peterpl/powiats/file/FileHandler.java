package com.peterpl.powiats.file;

import java.io.*;
import java.util.*;

public class FileHandler {
    public static void writeFile(String path, String content) {
        try(FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFileLines(String path, ArrayList<String> lines) {
        try(FileWriter writer = new FileWriter(path)) {
            for(String line : lines) {
                writer.write(line);
                writer.write('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> readFileLines(String path) {
        try(BufferedReader reader = new BufferedReader(new FileReader(path))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> readResourceFileLines(String path) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(FileHandler.class.getResourceAsStream("/" + path)))) {
            return readFileLines(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArrayList<String> readFileLines(BufferedReader reader) throws Exception {
        ArrayList<String> lines = new ArrayList<>();
        String line;
        while((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    public static File[] loadDir(String dir) {
        return new File(dir).listFiles();
    }

    public static File[] loadResourceDir(String dir) {
        try {
            return new File(FileHandler.class.getResource("/" + dir).toURI()).listFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createDir(String path) {
        File dir = new File(path);
        if(!dir.exists()) {
            dir.mkdir();
        }
    }
}
