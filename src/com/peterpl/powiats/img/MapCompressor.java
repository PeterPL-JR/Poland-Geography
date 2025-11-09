package com.peterpl.powiats.img;

import com.peterpl.powiats.*;

import java.util.*;

public class MapCompressor {
    public static ArrayList<Rect> compress(ArrayList<Point> pixels) {
        Rect bounds = getBounds(pixels);

        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;

        Img img = new Img(width, height);
        int[] heights = new int[width];
        NumberStack stack = new NumberStack(width);

        for(Point point : pixels) {
            img.setPixel(point.x - x, point.y - y, 1);
        }

        ArrayList<Rect> rects = new ArrayList<>();
        while(!img.isEmpty()) {
            Rect rect = find(img, heights, stack);
            rect.x += x;
            rect.y += y;
            rects.add(rect);
        }
        return rects;
    }

    private static Rect find(Img img, int[] heights, NumberStack stack) {
        Rect theBest = new Rect();
        int maxArea = 0;

        for (int i = 0; i < img.height; i++) {
            for (int j = 0; j < img.width; j++) {
                if (img.getPixel(j, i) == 1) {
                    heights[j]++;
                } else {
                    heights[j] = 0;
                }
            }

            stack.clear();

            int j = 0;
            while (j <= img.width) {
                int h = (j == img.width) ? 0 : heights[j];
                if (stack.isEmpty() || h >= heights[stack.peek()]) {
                    stack.add(j++);
                } else {
                    int top = stack.pop();
                    int height = stack.isEmpty() ? j : j - stack.peek() - 1;
                    int area = heights[top] * height;
                    if (area > maxArea) {
                        maxArea = area;
                        int x = stack.isEmpty() ? 0 : stack.peek() + 1;
                        int y = i - heights[top] + 1;
                        theBest.set(x, y, height, heights[top]);
                    }
                }
            }
        }
        img.setRect(theBest, 0);
        reset(heights);
        return theBest;
    }

    private static void reset(int[] heights) {
        Arrays.fill(heights, 0);
    }

    private static Rect getBounds(ArrayList<Point> pixels) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = -1;
        int maxY = -1;

        for(Point pixel : pixels) {
            int x = pixel.x;
            int y = pixel.y;

            if(x < minX) minX = x;
            if(y < minY) minY = y;
            if(x > maxX) maxX = x;
            if(y > maxY) maxY = y;
        }

        int width = maxX - minX + 1;
        int height = maxY - minY + 1;
        return new Rect(minX, minY, width, height);
    }

    public static class Rect {
        protected int x, y, width, height;

        public Rect() {
            this(0, 0, 0, 0);
        }

        public Rect(int x, int y, int width, int height) {
            set(x, y, width, height);
        }

        protected void set(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int area() {
            return width * height;
        }

        @Override
        public String toString() {
            return String.format("[x=%d, y=%d, width=%d, height=%d]", x, y, width, height);
        }
    }

    private static class Img {
        public final int width, height;
        public final int[] pixels;

        public Img(int width, int height) {
            this.width = width;
            this.height = height;
            pixels = new int[width * height];
        }

        public void setPixel(int x, int y, int colour) {
            pixels[x + y * width] = colour;
        }

        public int getPixel(int x, int y) {
            return pixels[x + y * width];
        }

        public void setRect(Rect rect, int colour) {
            setRect(rect.x, rect.y, rect.width, rect.height, colour);
        }

        public void setRect(int x, int y, int width, int height, int colour) {
            for(int i = 0; i < width; i++) {
                int xx = i + x;
                if(xx >= this.width) continue;
                for(int j = 0; j < height; j++) {
                    int yy = j + y;
                    if(yy >= this.height) continue;
                    setPixel(xx, yy, colour);
                }
            }
        }

        public boolean isEmpty() {
            for(int pixel : pixels) {
                if(pixel != 0) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class NumberStack {
        private final byte[] array;
        private int top = -1;
        private boolean empty = true;

        public NumberStack(int size) {
            array = new byte[size + 1];
        }

        public void add(int i) {
            array[i] = 1;
            if (i > top) {
                top = i;
            }
            empty = false;
        }

        public void remove(int i) {
            array[i] = 0;
            if (i == top) {
                for (int j = top - 1; j >= 0; j--) {
                    if (array[j] == 1) {
                        top = j;
                        return;
                    }
                }
                top = -1;
                empty = true;
            }
        }

        public void clear() {
            for (int i = 0; i <= top; i++) {
                array[i] = 0;
            }
            top = -1;
            empty = true;
        }

        public boolean isEmpty() {
            return empty;
        }

        public int peek() {
            return empty ? -1 : top;
        }

        public int pop() {
            int last = peek();
            if (last != -1) {
                remove(last);
            }
            return last;
        }
    }
}
