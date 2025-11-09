package com.peterpl.powiats.poland;

import java.util.*;

public class Vojv extends Geography<Vojv> {
    private final ArrayList<Powiat> powiats = new ArrayList<>();

    private final HashMap<Integer, Powiat> idPowiats = new HashMap<>();
    private final HashMap<String, Powiat> namePowiats = new HashMap<>();

    public Vojv(int id, String name, ArrayList<String> capital) {
        super(id, name, capital);
    }

    public ArrayList<Powiat> getPowiats() {
        return new ArrayList<>(powiats);
    }

    public Powiat getPowiat(String name) {
        return namePowiats.get(name);
    }

    public Powiat getPowiat(int id) {
        return idPowiats.get(id);
    }

    protected void addPowiat(Powiat powiat) {
        powiats.add(powiat);

        idPowiats.put(powiat.id, powiat);
        namePowiats.put(powiat.name, powiat);
    }

    @Override
    public String toString() {
        return name + " " + capital;
    }
}
