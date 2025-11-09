package com.peterpl.powiats.poland;

import com.peterpl.powiats.*;
import com.peterpl.powiats.file.*;
import com.peterpl.powiats.img.*;

import java.io.*;
import java.util.*;

public class PolandCreator {
    private static ArrayList<Vojv> vojvs = new ArrayList<>();
    private static ArrayList<Powiat> powiats = new ArrayList<>();

    private static final Img vojvsImg = new Img(Poland.VOJV_IMG);
    private static final Img powiatsImg = new Img(Poland.POWIATS_IMG);

    static {
        Arrays.fill(vojvsImg.pixels, -1);
        Arrays.fill(powiatsImg.pixels, -1);
    }

    public static void create() {
        loadVojvs(vojvs, vojvsImg);
        loadPowiats(vojvs, powiats, powiatsImg);

        createPoints(vojvs, Poland.VOJV_IMG);
        createPoints(powiats, Poland.POWIATS_IMG);

        try(DataOutputStream writer = new DataOutputStream(new FileOutputStream("poland_data.dat"))) {
            writer.writeInt(vojvs.size());
            for(Vojv v : vojvs) {
                writeGeographyElement(writer, v);
            }

            writer.writeInt(powiats.size());
            for(Powiat p : powiats) {
                writeGeographyElement(writer, p);
                writer.writeInt(p.vojv.id);

                Powiat cityPowiatCapital = p.getCityPowiatCapital();
                writer.writeInt(cityPowiatCapital != null ? cityPowiatCapital.id : -1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadVojvs(ArrayList<Vojv> vojvs, Img vojvsImg) {
        HashMap<Vojv, Img> images = new HashMap<>();
        int i = 0;
        ArrayList<String> lines = FileHandler.readResourceFileLines("data/basic/vojvs.txt");
        for(String line : lines) {
            int separator = line.indexOf("[");
            int id = i;
            String name = line.substring(0, separator - 1);
            ArrayList<String> capital = new ArrayList<>(List.of(line.substring(separator + 1, line.length() - 1).split(", ")));
            i++;

            Vojv v = new Vojv(id, name, capital);
            vojvs.add(v);

            Img img = ImgHandler.readResourceImg("data/basic/vojvs/" + name);
            ArrayList<Point> pixels = MapPaint.getPixels(img, Poland.MAIN_COLOUR);
            v.setPixels(pixels);
            MapPaint.set(vojvsImg, pixels, id);
            images.put(v, img);
        }
        createNeighbours(vojvs, images, vojvsImg);
    }

    private static void loadPowiats(ArrayList<Vojv> vojvs, ArrayList<Powiat> powiats, Img powiatsImg) {
        HashMap<Powiat, Img> images = new HashMap<>();
        int i = 0;
        ArrayList<String> lines = FileHandler.readResourceFileLines("data/basic/powiats.txt");
        Vojv vojv = null;
        for(String line : lines) {
            if(line.isEmpty()) continue;
            if(line.charAt(0) == '$') {
                String vojvName = line.substring(2);
                vojv = vojvs.stream().filter(n -> n.name.equals(vojvName)).toList().getFirst();
                continue;
            }
            int id = i;
            String name;
            ArrayList<String> capital;

            int separator = line.indexOf('[');
            if(separator == -1) {
                name = line;
                capital = new ArrayList<>(List.of(name));
            } else {
                name = line.substring(0, separator - 1);
                capital = new ArrayList<>(List.of(line.substring(separator + 1, line.length() - 1).split(", ")));
            }
            i++;

            Powiat p = new Powiat(id, name, capital, vojv);
            powiats.add(p);

            Img img = ImgHandler.readResourceImg("data/basic/powiats/" + vojv.name + "/" + name);
            ArrayList<Point> pixels = MapPaint.getPixels(img, Poland.MAIN_COLOUR);
            p.setPixels(pixels);
            MapPaint.set(powiatsImg, pixels, id);
            images.put(p, img);
        }

        for(Powiat powiat : powiats) {
            if(powiat.isCityPowiat()) continue;
            Optional<Powiat> cityPowiat = powiats.stream().filter(p -> p.isCityPowiat() && powiat.capital.getFirst().equals(p.name)).findFirst();
            cityPowiat.ifPresent(powiat::setCityPowiatCapital);
        }

        createNeighbours(powiats, images, powiatsImg);
    }

    private static <T extends Geography<T>> void writeGeographyElement(DataOutputStream writer, T element) throws IOException {
        writer.writeInt(element.id);
        writer.writeUTF(element.name);

        writer.writeInt(element.capital.size());
        for(String capital : element.capital) {
            writer.writeUTF(capital);
        }

        writer.writeInt(element.neighbours.size());
        for(T n : element.neighbours) {
            writer.writeInt(n.id);
        }

        writePixels(writer, element.pixels);

        writer.writeInt(element.points.size());
        for(Point point : element.points) {
            writer.writeInt(point.x);
            writer.writeInt(point.y);
        }
    }

    static <T extends Geography<T>> GeographyData readGeographyElement(DataInputStream reader) throws IOException {
        int id = reader.readInt();
        String name = reader.readUTF();

        int capitalsNumber = reader.readInt();
        ArrayList<String> capital = new ArrayList<>();
        for(int i = 0; i < capitalsNumber; i++) {
            capital.add(reader.readUTF());
        }

        ArrayList<Integer> neighboursIds = new ArrayList<>();
        int neighboursNumber = reader.readInt();
        for(int i = 0; i < neighboursNumber; i++) {
            neighboursIds.add(reader.readInt());
        }

        ArrayList<Point> pixels = PolandCreator.readPixels(reader);

        ArrayList<Point> points = new ArrayList<>();
        int pointsNumber = reader.readInt();
        for(int i = 0; i < pointsNumber; i++) {
            int x = reader.readInt();
            int y = reader.readInt();
            points.add(new Point(x, y));
        }
        return new GeographyData(id, name, capital, pixels, points, neighboursIds);
    }

    private static <T extends Geography<T>> void createPoints(ArrayList<T> elements, Img img) {
        Img imgBuffer = new Img(img);
        for(T elem : elements) {
            MapPaint.set(imgBuffer, elem.pixels, Poland.MAIN_COLOUR);
            ArrayList<Point> points = new ArrayList<>();
            Point point;
            while((point = MapPaint.find(imgBuffer, Poland.MAIN_COLOUR)) != null) {
                points.add(point);
                MapPaint.fill(imgBuffer, point, Poland.MAIN_COLOUR, Poland.EMPTY_COLOUR);
            }
            elem.setPoints(points);
        }
    }

    private static <T extends Geography<T>> void createNeighbours(ArrayList<T> elements, HashMap<T, Img> images, Img elementsImg) {
        for(T elem : elements) {
            Img img = images.get(elem);
            Point point;
            while((point = MapPaint.find(img, Poland.NEIGHBOUR_COLOUR)) != null) {
                int id = elementsImg.getPixel(point);
                T n = elements.stream().filter(e -> e.id == id).toList().getFirst();
                elem.addNeighbour(n);
                MapPaint.set(img, n.pixels, Poland.EMPTY_COLOUR);
            }
        }
    }

    static ArrayList<Point> readPixels(DataInputStream reader) throws IOException {
        ArrayList<Point> pixels = new ArrayList<>();

        int size = reader.readInt();
        for(int i = 0; i < size; i++) {
            int xPos = reader.readInt();
            int yPos = reader.readInt();
            int width = reader.readInt();
            int height = reader.readInt();

            for(int x = 0; x < width; x++) {
                int xx = x + xPos;
                for(int y = 0; y < height; y++) {
                    int yy = y + yPos;
                    pixels.add(new Point(xx, yy));
                }
            }
        }
        return pixels;
    }

    private static void writePixels(DataOutputStream writer, ArrayList<Point> pixels) throws IOException {
        ArrayList<MapCompressor.Rect> rects = MapCompressor.compress(pixels);

        writer.writeInt(rects.size());

        for(MapCompressor.Rect r : rects) {
            writer.writeInt(r.getX());
            writer.writeInt(r.getY());
            writer.writeInt(r.getWidth());
            writer.writeInt(r.getHeight());
        }
    }

    record GeographyData(int id, String name, ArrayList<String> capital, ArrayList<Point> pixels, ArrayList<Point> points, ArrayList<Integer> neighboursIds) {
    }
}
