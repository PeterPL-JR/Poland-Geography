package com.peterpl.powiats;

public class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this(point.x, point.y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
