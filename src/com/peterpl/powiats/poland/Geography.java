package com.peterpl.powiats.poland;

import com.peterpl.powiats.*;
import com.peterpl.powiats.img.*;

import java.util.*;

public abstract class Geography<T extends Geography<T>> {
    public final int id;
    public final String name;

    protected final ArrayList<String> capital;
    protected ArrayList<T> neighbours = new ArrayList<>();
    protected ArrayList<Point> pixels = new ArrayList<>();
    protected ArrayList<Point> points = new ArrayList<>();

    public Geography(int id, String name, ArrayList<String> capital) {
        this.id = id;
        this.name = name;
        this.capital = capital;
    }

    public ArrayList<String> getCapital() {
        return capital;
    }

    public ArrayList<T> getNeighbours() {
        return new ArrayList<>(neighbours);
    }

    protected void addNeighbour(T elem) {
        neighbours.add(elem);
    }

    public ArrayList<Point> getPixels() {
        return new ArrayList<>(pixels);
    }

    protected void setPixels(ArrayList<Point> pixels) {
        this.pixels = pixels;
    }

    public ArrayList<Point> getPoints() {
        return new ArrayList<>(points);
    }

    protected void setPoints(ArrayList<Point> points) {
        this.points = points;
    }
}
