package com.peterpl.powiats.img;

import com.peterpl.powiats.*;

import java.awt.image.*;
import java.io.*;
import java.util.*;

public class Img {
    public final int width, height;
    public final int[] pixels;

    public Img(int width, int height) {
        this(width, height, 0x00000000);
    }

    public Img(int width, int height, int colour) {
        this.width = width;
        this.height = height;

        pixels = new int[width * height];
        Arrays.fill(pixels, colour);
    }

    public Img(BufferedImage img) {
        width = img.getWidth();
        height = img.getHeight();
        pixels = new int[width * height];
        img.getRGB(0, 0, width, height, pixels, 0, width);
    }

    public Img(Img img) {
        this(img.width, img.height);
        System.arraycopy(img.pixels, 0, pixels, 0, pixels.length);
    }

    public void setPixel(int x, int y, int colour) {
        pixels[getIndex(x, y)] = colour;
    }

    public int getPixel(int x, int y) {
        return pixels[getIndex(x, y)];
    }

    public void setPixel(Point point, int colour) {
        setPixel(point.x, point.y, colour);
    }

    public int getPixel(Point point) {
        return getPixel(point.x, point.y);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public boolean contains(Point point) {
        return contains(point.x, point.y);
    }

    private int getIndex(int x, int y) {
        return x + y * width;
    }

    public BufferedImage getImage() {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, width, height, pixels, 0, width);
        return img;
    }

    public Img copy() {
        return new Img(this);
    }
}
