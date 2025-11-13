package com.peterpl.powiats.poland;

import java.util.*;

public class Vojv extends Geography<Vojv> {
    private final ArrayList<Powiat> powiats = new ArrayList<>();
    private final HashMap<Integer, Powiat> idPowiats = new HashMap<>();
    private final HashMap<String, Powiat> namePowiats = new HashMap<>();

    private final ArrayList<City> cities = new ArrayList<>();
    private final HashMap<Integer, City> idCities = new HashMap<>();
    private final HashMap<String, City> nameCities = new HashMap<>();

    public Vojv(int id, String name) {
        super(id, name);
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

    public ArrayList<City> getCities() {
        return new ArrayList<>(cities);
    }

    public City getCity(String name) {
        return nameCities.get(name);
    }

    public City getCity(int id) {
        return idCities.get(id);
    }

    protected void addCity(City city) {
        cities.add(city);

        idCities.put(city.id, city);
        nameCities.put(city.name, city);
    }

    @Override
    public String toString() {
        return name + " " + capital.stream().map(City::toSimpleString).toList();
    }
}
