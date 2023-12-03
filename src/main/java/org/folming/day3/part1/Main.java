package org.folming.day3.part1;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// xStart and xEnd are inclusive.
record NumberRectangle(int y, int xStart, int xEnd, int number) {}

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day3/input"), StandardCharsets.UTF_8);
        final List<char[]> map = new ArrayList<>(lines.size());
        for (final var line : lines) {
            map.add(line.toCharArray());
        }

        final List<NumberRectangle> numberRectangles = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            int xStart = -1;
            int number = 0;

            final String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                char atJ = line.charAt(j);

                if (atJ >= '0' && atJ <= '9') {
                    if (xStart == -1) {
                        xStart = j;
                    }

                    number *= 10;
                    number += atJ - '0';
                } else if (number != 0) {
                    numberRectangles.add(new NumberRectangle(i, xStart, j - 1, number));
                    xStart = -1;
                    number = 0;
                }

                if ((j == line.length() - 1) && number != 0) {
                    numberRectangles.add(new NumberRectangle(i, xStart, lines.size() - 1, number));
                }
            }
        }

        int result = 0;

        for (final var numberRectangle : numberRectangles) {
            if (hasAdjacentSymbol(numberRectangle, map)) {
                result += numberRectangle.number();
            }
        }

        System.out.println(result); // 550934
    }

    private static boolean hasAdjacentSymbol(final NumberRectangle numberRectangle, final List<char[]> map) {
        // Check left and right sides
        final char[] sameLineArray = map.get(numberRectangle.y());
        if (numberRectangle.xStart() > 0) {
            final char leftChar = sameLineArray[numberRectangle.xStart() - 1];
            if (charIsNotNumberOrDot(leftChar)) {
                return true;
            }
        }

        if (numberRectangle.xEnd() < sameLineArray.length - 1) {
            final char rightChar = sameLineArray[numberRectangle.xEnd() + 1];
            if (charIsNotNumberOrDot(rightChar)) {
                return true;
            }
        }

        // Check above row
        if (numberRectangle.y() > 0) {
            final char[] aboveLineArray = map.get(numberRectangle.y() - 1);
            int leftX = numberRectangle.xStart() == 0 ? 0 : numberRectangle.xStart() - 1;
            int rightX = numberRectangle.xEnd() >= aboveLineArray.length - 1 ? aboveLineArray.length - 1 : numberRectangle.xEnd() + 1;
            for (int i = leftX; i <= rightX; i++) {
                if (charIsNotNumberOrDot(aboveLineArray[i])) {
                    return true;
                }
            }
        }

        // Check below row
        if (numberRectangle.y() < map.size() - 1) {
            final char[] aboveLineArray = map.get(numberRectangle.y() + 1);
            int leftX = numberRectangle.xStart() == 0 ? 0 : numberRectangle.xStart() - 1;
            int rightX = numberRectangle.xEnd() >= aboveLineArray.length - 1 ? aboveLineArray.length - 1 : numberRectangle.xEnd() + 1;
            for (int i = leftX; i <= rightX; i++) {
                if (charIsNotNumberOrDot(aboveLineArray[i])) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean charIsNotNumberOrDot(final char ch) {
        return ch != '.' && (ch < '0' || ch > '9');
    }
}
