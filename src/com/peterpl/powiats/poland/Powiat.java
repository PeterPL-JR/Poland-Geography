package com.peterpl.powiats.poland;

import java.util.*;

public class Powiat extends Geography<Powiat> {
    public final Vojv vojv;
    private final boolean cityPowiat;
    private Powiat cityPowiatCapital;

    public Powiat(int id, String name, ArrayList<String> capital, Vojv vojv) {
        super(id, name, capital);
        this.vojv = vojv;
        cityPowiat = name.equals(capital.getFirst());
    }

    public boolean isCityPowiat() {
        return cityPowiat;
    }

    public Powiat getCityPowiatCapital() {
        return cityPowiatCapital;
    }

    protected void setCityPowiatCapital(Powiat cityPowiat) {
        this.cityPowiatCapital = cityPowiat;
    }

    @Override
    public String toString() {
        return vojv.name + " -> " + name + (isCityPowiat() ? "" : " " + capital);
    }
}
