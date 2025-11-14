package com.peterpl.powiats.img;

import com.peterpl.powiats.*;
import com.peterpl.powiats.poland.*;

import java.util.*;

public class MapPaint {
    public static ArrayList<Point> fill(Img img, int x, int y, int colour, int newColour) {
        ArrayList<Point> pixels = new ArrayList<>();
        if(!img.contains(x, y) || img.getPixel(x, y) != colour || colour == newColour) return pixels;

        Stack<Point> stack = new Stack<>();
        Point startPoint = new Point(x, y);
        stack.add(startPoint);

        while(!stack.isEmpty()) {
            Point point = stack.pop();
            img.setPixel(point, newColour);
            pixels.add(point);

            neighbour(img, point, -1, 0, colour, stack);
            neighbour(img, point, 1, 0, colour, stack);
            neighbour(img, point, 0, -1, colour, stack);
            neighbour(img, point, 0, 1, colour, stack);
        }
        return pixels;
    }

    public static ArrayList<Point> fill(Img img, Point point, int colour, int newColour) {
        return fill(img, point.x, point.y, colour, newColour);
    }

    public static void set(Img img, ArrayList<Point> pixels, int colour) {
        for(Point point : pixels) {
            img.setPixel(point, colour);
        }
    }

    public static Point find(Img img, int colour) {
        for(int x = 0; x < img.width; x++) {
            for(int y = 0; y < img.height; y++) {
                if(img.getPixel(x, y) == colour) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    public static void replace(Img img, int colour, int newColour) {
        for(int x = 0; x < img.width; x++) {
            for(int y = 0; y < img.height; y++) {
                if(img.getPixel(x, y) == colour) {
                    img.setPixel(x, y, newColour);
                }
            }
        }
    }

    public static ArrayList<Point> getPixels(Img img, int x, int y) {
        int colour = img.getPixel(x, y);
        int newColour = colour != 0xffff0000 ? 0xffff0000 : 0xff0000ff;
        ArrayList<Point> pixels = fill(img, x, y, colour, newColour);
        fill(img, x, y, newColour, colour);
        return pixels;
    }

    public static ArrayList<Point> getPixels(Img img, Point point) {
        return getPixels(img, point.x, point.y);
    }

    public static ArrayList<Point> getPixels(Img img, int colour) {
        ArrayList<Point> pixels = new ArrayList<>();
        for(int x = 0; x < img.width; x++) {
            for(int y = 0; y < img.height; y++) {
                if(img.getPixel(x, y) == colour) {
                    pixels.add(new Point(x, y));
                }
            }
        }
        return pixels;
    }

    private static void neighbour(Img img, Point origin, int xOffset, int yOffset, int colour, Stack<Point> stack) {
        int newX = origin.x + xOffset;
        int newY = origin.y + yOffset;
        if(!img.contains(newX, newY) || img.getPixel(newX, newY) != colour) return;

        Point point = new Point(newX, newY);
        stack.add(point);
    }

    public static <T extends Geography<T>> void fillGeography(Img map, T elem, int colour) {
        set(map, elem.getPixels(), colour);
    }

    public static void fillVojv(Img vojvsMap, Vojv vojv, int colour) {
        fillGeography(vojvsMap, vojv, colour);
    }

    public static void fillVojv(Img vojvsMap, String vojv, int colour) {
        fillVojv(vojvsMap, Poland.getVojv(vojv), colour);
    }

    public static void fillPowiat(Img powiatsMap, Powiat powiat, int colour) {
        fillGeography(powiatsMap, powiat, colour);
    }

    public static void fillPowiat(Img powiatsMap, String powiat, String vojv, int colour) {
        fillPowiat(powiatsMap, Poland.getPowiat(powiat, vojv), colour);
    }

    public static void fillPowiat(Img powiatsMap, String powiat, int colour) {
        fillPowiat(powiatsMap, Poland.getPowiat(powiat), colour);
    }
}
