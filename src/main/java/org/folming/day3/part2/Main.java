package org.folming.day3.part2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day3/input"), StandardCharsets.UTF_8);
        final List<char[]> map = new ArrayList<>(lines.size());
        for (final var line : lines) {
            map.add(line.toCharArray());
        }

        int[] result = new int[1];

        for (int y = 0; y < map.size(); y++) {
            final char[] line = map.get(y);
            for (int x = 0; x < line.length; x++) {
                checkIsGearAndReturnItsRatio(map, x, y).ifPresent(r -> result[0] += r);
            }
        }

        System.out.println(result[0]); // 81997870
    }

    private static Optional<Integer> checkIsGearAndReturnItsRatio(final List<char[]> map, int x, int y) {
        final char[] line = map.get(y);
        if (line[x] != '*') {
            return Optional.empty();
        }

        final List<Integer> numbers = new ArrayList<>(2);

        // Check left
        if (x > 0 && (line[x - 1] >= '0' && line[x - 1] <= '9')) {
            final int number = getNumberAt(map, x - 1, y);
            numbers.add(number);
        }

        // Check right
        if (x < line.length - 1 && (line[x + 1] >= '0' && line[x + 1] <= '9')) {
            final int number = getNumberAt(map, x + 1, y);
            numbers.add(number);
        }

        // Check above
        if (y > 0) {
            final char[] aboveLine = map.get(y - 1);

            if (aboveLine[x] < '0' || aboveLine[x] > '9') {
                // Check if there are two numbers above
                if (x > 0 && (aboveLine[x - 1] >= '0' && aboveLine[x - 1] <= '9')) {
                    if (numbers.size() == 2) {
                        return Optional.empty();
                    } else {
                        final int number = getNumberAt(map, x - 1, y - 1);
                        numbers.add(number);
                    }
                }

                if (x < aboveLine.length - 1 && (aboveLine[x + 1] >= '0' && aboveLine[x + 1] <= '9')) {
                    if (numbers.size() == 2) {
                        return Optional.empty();
                    } else {
                        final int number = getNumberAt(map, x + 1, y - 1);
                        numbers.add(number);
                    }
                }
            } else {
                int leftX = x > 0 ? x - 1 : 0;
                int rightX = x < aboveLine.length - 1 ? x + 1 : aboveLine.length - 1;
                for (int i = leftX; i <= rightX; i++) {
                    if (aboveLine[i] >= '0' && aboveLine[i] <= '9') {
                        if (numbers.size() == 2) {
                            return Optional.empty();
                        } else {
                            final int number = getNumberAt(map, i, y - 1);
                            numbers.add(number);
                            break;
                        }
                    }
                }
            }
        }

        // Check below
        if (y < map.size() - 1) {
            final char[] belowLine = map.get(y + 1);

            if (belowLine[x] < '0' || belowLine[x] > '9') {
                // Check if there are two numbers above
                if (x > 0 && (belowLine[x - 1] >= '0' && belowLine[x - 1] <= '9')) {
                    if (numbers.size() == 2) {
                        return Optional.empty();
                    } else {
                        final int number = getNumberAt(map, x - 1, y + 1);
                        numbers.add(number);
                    }
                }

                if (x < belowLine.length - 1 && (belowLine[x + 1] >= '0' && belowLine[x + 1] <= '9')) {
                    if (numbers.size() == 2) {
                        return Optional.empty();
                    } else {
                        final int number = getNumberAt(map, x + 1, y + 1);
                        numbers.add(number);
                    }
                }
            } else {
                int leftX = x > 0 ? x - 1 : 0;
                int rightX = x < belowLine.length - 1 ? x + 1 : belowLine.length - 1;
                for (int i = leftX; i <= rightX; i++) {
                    if (belowLine[i] >= '0' && belowLine[i] <= '9') {
                        if (numbers.size() == 2) {
                            return Optional.empty();
                        } else {
                            final int number = getNumberAt(map, i, y + 1);
                            numbers.add(number);
                            break;
                        }
                    }
                }
            }
        }

        if (numbers.size() == 2) {
            return Optional.of(numbers.get(0) * numbers.get(1));
        } else {
            return Optional.empty();
        }
    }

    private static int getNumberAt(final List<char[]> map, int x, int y) {
        final char[] line = map.get(y);
        int number = line[x] - '0';

        // Go left
        int leftX = x - 1;
        int pow10 = 1;
        while (leftX >= 0 && (line[leftX] >= '0' && line[leftX] <= '9')) {
            number += (line[leftX] - '0') * Math.pow(10, pow10);

            ++pow10;
            --leftX;
        }

        // Go right
        int rightX = x + 1;
        while (rightX < line.length && (line[rightX] >= '0' && line[rightX] <= '9')) {
            number *= 10;
            number += line[rightX] - '0';

            ++rightX;
        }

        return number;
    }
}
