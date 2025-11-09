package com.peterpl.powiats.img;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.util.function.*;

public class ImgHandler {
    public static Img readImg(String path) {
        try {
            return new Img(ImageIO.read(new File(path + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Img readResourceImg(String path) {
        try {
            return new Img(ImageIO.read(Img.class.getResourceAsStream("/" + path + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeImg(String path, Img img) {
        try {
            ImageIO.write(img.getImage(), "png", new File(path + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
