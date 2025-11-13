package com.peterpl.powiats.poland;

import java.util.*;

public class Powiat extends Geography<Powiat> {
    public final Vojv vojv;
    private boolean cityPowiat;
    private Powiat cityPowiatCapital;
    private final ArrayList<City> cities = new ArrayList<>();

    private final HashMap<Integer, City> idCities = new HashMap<>();
    private final HashMap<String, City> nameCities = new HashMap<>();

    public Powiat(int id, String name, Vojv vojv) {
        super(id, name);
        this.vojv = vojv;
    }

    @Override
    protected void setCapital(ArrayList<City> capital) {
        super.setCapital(capital);
        cityPowiat = capital.size() == 1 && capital.getFirst().name.equals(name);
    }

    public boolean isCityPowiat() {
        return cityPowiat;
    }

    public Powiat getCityPowiatCapital() {
        return cityPowiatCapital;
    }

    protected void setCityPowiatCapital(Powiat cityPowiat) {
        this.cityPowiatCapital = cityPowiat;
        setCapital(cityPowiat.capital);
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

        vojv.addCity(city);
    }

    @Override
    public String toString() {
        return vojv.name + " -> " + toSimpleString();
    }

    public String toSimpleString() {
        return name + (isCityPowiat() ? " [" + capital.getFirst().population + "]" : " " + capital.stream().map(City::toSimpleString).toList());
    }
}
