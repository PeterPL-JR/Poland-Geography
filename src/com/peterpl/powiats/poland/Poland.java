package com.peterpl.powiats.poland;

import com.peterpl.powiats.*;
import com.peterpl.powiats.img.*;

import java.io.*;
import java.util.*;

public class Poland {
    public static final Img VOJV_IMG = ImgHandler.readResourceImg("map/vojvs");
    public static final Img POWIATS_IMG = ImgHandler.readResourceImg("map/powiats");

    public static final int EMPTY_COLOUR = 0xff000000;
    public static final int MAIN_COLOUR = 0xffff0000;
    public static final int NEIGHBOUR_COLOUR = 0xff660000;
    public static final int VOJV_BORDER = 0xffffffff;
    public static final int POWIAT_BORDER = 0xffd3d3d3;

    private static final ArrayList<Vojv> vojvs = new ArrayList<>();
    private static final ArrayList<Powiat> powiats = new ArrayList<>();

    private static final HashMap<Integer, Vojv> idVojvs = new HashMap<>();
    private static final HashMap<String, Vojv> nameVojvs = new HashMap<>();

    private static final HashMap<Integer, Powiat> idPowiats = new HashMap<>();
    private static final HashMap<String, Powiat> namePowiats = new HashMap<>();

    private static Img vojvsMap, powiatsMap;

    public static void init() {
        try(DataInputStream reader = new DataInputStream(Poland.class.getResourceAsStream("/data/poland_data.dat"))) {
            assert VOJV_IMG != null;
            vojvsMap = new Img(VOJV_IMG);
            powiatsMap = new Img(POWIATS_IMG);

            Arrays.fill(vojvsMap.pixels, -1);
            Arrays.fill(powiatsMap.pixels, -1);

            HashMap<Vojv, ArrayList<Integer>> vojvsNeighbours = new HashMap<>();

            int vojvsNumber = reader.readInt();
            for(int i = 0; i < vojvsNumber; i++) {
                PolandCreator.GeographyData data = PolandCreator.readGeographyElement(reader);

                Vojv vojv = new Vojv(data.id(), data.name(), data.capital());
                vojv.setPixels(data.pixels());
                vojv.setPoints(data.points());
                MapPaint.set(vojvsMap, data.pixels(), data.id());

                vojvs.add(vojv);
                idVojvs.put(vojv.id, vojv);
                nameVojvs.put(vojv.name, vojv);
                vojvsNeighbours.put(vojv, data.neighboursIds());
            }

            for(Vojv v : vojvs) {
                for(int n : vojvsNeighbours.get(v)) {
                    v.addNeighbour(idVojvs.get(n));
                }
            }

            HashMap<Powiat, ArrayList<Integer>> powiatsNeighbours = new HashMap<>();
            HashMap<Powiat, Integer> cityPowiatCapitals = new HashMap<>();

            int powiatsNumber = reader.readInt();
            for(int i = 0; i < powiatsNumber; i++) {
                PolandCreator.GeographyData data = PolandCreator.readGeographyElement(reader);
                int vojvId = reader.readInt();
                int cityPowiatCapitalId = reader.readInt();

                Powiat powiat = new Powiat(data.id(), data.name(), data.capital(), idVojvs.get(vojvId));
                powiat.setPixels(data.pixels());
                powiat.setPoints(data.points());
                MapPaint.set(powiatsMap, data.pixels(), data.id());

                powiats.add(powiat);
                idPowiats.put(powiat.id, powiat);
                powiatsNeighbours.put(powiat, data.neighboursIds());

                if(namePowiats.containsKey(powiat.name)) {
                    namePowiats.remove(powiat.name);
                } else {
                    namePowiats.put(powiat.name, powiat);
                }

                if(cityPowiatCapitalId != -1) {
                    cityPowiatCapitals.put(powiat, cityPowiatCapitalId);
                }
            }

            for(Powiat p : powiats) {
                for(int n : powiatsNeighbours.get(p)) {
                    p.addNeighbour(idPowiats.get(n));
                }
            }

            for(Powiat p : cityPowiatCapitals.keySet()) {
                p.setCityPowiatCapital(idPowiats.get(cityPowiatCapitals.get(p)));
            }

            for(Powiat p : powiats) {
                p.vojv.addPowiat(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Vojv> getVojvs() {
        return new ArrayList<>(vojvs);
    }

    public static ArrayList<Powiat> getPowiats() {
        return new ArrayList<>(powiats);
    }

    public static Vojv getVojv(String name) {
        return nameVojvs.get(name);
    }

    public static Vojv getVojv(int id) {
        return idVojvs.get(id);
    }

    public static Vojv getVojv(int x, int y) {
        int id = vojvsMap.getPixel(x, y);
        if(id != -1) {
            return getVojv(id);
        }
        return null;
    }

    public static Vojv getVojv(Point point) {
        return getVojv(point.x, point.y);
    }

    public static Powiat getPowiat(String name) {
        return namePowiats.get(name);
    }

    public static Powiat getPowiat(String name, String vojv) {
        return getVojv(vojv).getPowiat(name);
    }

    public static Powiat getPowiat(int id) {
        return idPowiats.get(id);
    }

    public static Powiat getPowiat(int x, int y) {
        int id = powiatsMap.getPixel(x, y);
        if(id != -1) {
            return getPowiat(id);
        }
        return null;
    }

    public static Powiat getPowiat(Point point) {
        return getPowiat(point.x, point.y);
    }
}
