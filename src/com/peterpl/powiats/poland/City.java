package com.peterpl.powiats.poland;

public class City {
    public final int id;
    public final String name;
    public final int population;
    public final Powiat powiat;

    public City(int id, String name, int population, Powiat powiat) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.powiat = powiat;
    }

    @Override
    public String toString() {
        return powiat.vojv.name + " -> " + (powiat.isCityPowiat() ? "" : powiat.name + " -> ") + toSimpleString();
    }

    public String toSimpleString() {
        return name + " [" + population + "]";
    }
}
