package com.peterpl.powiats.my;

import com.peterpl.powiats.img.*;
import com.peterpl.powiats.poland.*;

import java.util.*;
import java.util.function.*;

public class Colours4Map {
    private static final int[] COLOURS = {0xffff0000, 0xffffff00, 0xff00ff00, 0xff0000ff};

    public static <T extends Geography<T>> Img create(Img img, Supplier<ArrayList<T>> elementsSupplier) {
        Img newImg = img.copy();
        ArrayList<T> elements = elementsSupplier.get();

        HashMap<T, ArrayList<T>> elementsNeighbours = new HashMap<>();
        for(T elem : elements) {
            elementsNeighbours.put(elem, elem.getNeighbours());
        }

        HashMap<T, Integer> colours = new HashMap<>();
        ArrayList<T> elementsList = new ArrayList<>();

        ArrayList<T> threeOrLessNeighboursElements = new ArrayList<>();
        for(T elem : elements) {
            if(elementsNeighbours.get(elem).size() <= 3) {
                threeOrLessNeighboursElements.add(elem);
            }
        }
        for(T elem : threeOrLessNeighboursElements) {
            elements.remove(elem);
        }

        T element = elements.getLast();
        while(!elements.isEmpty()) {
            elements.remove(element);
            ArrayList<T> neighbours = elementsNeighbours.get(element);

            int index = getFirstAvailableColourIndex(neighbours, colours);
            if(index == -1) {
                while(index == -1) {
                    T last = elementsList.getLast();

                    ArrayList<T> lastNeighbours = elementsNeighbours.get(last);
                    int lastPowiatColour = colours.get(last);

                    index = getFirstAvailableColourIndex(lastNeighbours, colours, lastPowiatColour + 1);
                    if(index != -1) {
                        colours.put(last, index);
                    } else {
                        colours.remove(last);
                        elementsList.remove(last);
                        elements.add(last);
                    }
                }
                continue;
            }

            colours.put(element, index);
            elementsList.add(element);

            if(elements.isEmpty()) {
                continue;
            }
            Optional<T> next = neighbours.stream().filter(elements::contains).findAny();
            if(next.isPresent()) {
                element = next.get();
            } else {
                int i = 0;
                T n;
                do {
                    T last = elementsList.get(elementsList.size() - i - 2);
                    List<T> matchingNeighbours = elementsNeighbours.get(last).stream().filter(elements::contains).toList();
                    n = !matchingNeighbours.isEmpty() ? matchingNeighbours.getFirst() : null;
                    i++;
                } while(n == null);
                element = n;
            }
        }

        for(T elem : threeOrLessNeighboursElements) {
            colours.put(elem, getFirstAvailableColourIndex(elementsNeighbours.get(elem), colours));
        }

        for(T elem : colours.keySet()) {
            MapPaint.fillGeography(newImg, elem, COLOURS[colours.get(elem)]);
        }
        return newImg;
    }

    private static <T extends Geography<T>> int getFirstAvailableColourIndex(ArrayList<T> neighbours, HashMap<T, Integer> colours, int minIndex) {
        for(int i = minIndex; i < Colours4Map.COLOURS.length; i++) {
            final int j = i;
            if(neighbours.stream().noneMatch(n -> colours.containsKey(n) && colours.get(n) == j)) {
                return i;
            }
        }
        return -1;
    }

    private static <T extends Geography<T>> int getFirstAvailableColourIndex(ArrayList<T> neighbours, HashMap<T, Integer> colours) {
        return getFirstAvailableColourIndex(neighbours, colours, 0);
    }
}
