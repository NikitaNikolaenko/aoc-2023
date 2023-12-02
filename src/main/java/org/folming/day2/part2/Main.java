package org.folming.day2.part2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Main {
    private enum COLOR {
        RED,
        GREEN,
        BLUE
    }

    private static void combineSubsets(final Map<COLOR, Integer> oldColorIntegerMap, final Map<COLOR, Integer> newColorIntegerMap) {
        final Integer oldRedInt = oldColorIntegerMap.get(COLOR.RED);
        final Integer newRedInt = newColorIntegerMap.get(COLOR.RED);
        if (newRedInt != null) {
            if (oldRedInt == null || oldRedInt < newRedInt) {
                oldColorIntegerMap.put(COLOR.RED, newRedInt);
            }
        }

        final Integer oldGreenInt = oldColorIntegerMap.get(COLOR.GREEN);
        final Integer newGreenInt = newColorIntegerMap.get(COLOR.GREEN);
        if (newGreenInt != null) {
            if (oldGreenInt == null || oldGreenInt < newGreenInt) {
                oldColorIntegerMap.put(COLOR.GREEN, newGreenInt);
            }
        }

        final Integer oldBlueInt = oldColorIntegerMap.get(COLOR.BLUE);
        final Integer newBlueInt = newColorIntegerMap.get(COLOR.BLUE);
        if (newBlueInt != null) {
            if (oldBlueInt == null || oldBlueInt < newBlueInt) {
                oldColorIntegerMap.put(COLOR.BLUE, newBlueInt);
            }
        }
    }

    private static int getPowerOfSubset(final Map<COLOR, Integer> colorIntegerMap) {
        int result = 1;

        final Integer redInt = colorIntegerMap.get(COLOR.RED);
        if (redInt != null) {
            result *= redInt;
        }

        final Integer greenInt = colorIntegerMap.get(COLOR.GREEN);
        if (greenInt != null) {
            result *= greenInt;
        }

        final Integer blueInt = colorIntegerMap.get(COLOR.BLUE);
        if (blueInt != null) {
            result *= blueInt;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day2/input"), StandardCharsets.UTF_8);

        int result = 0;

        for (int i = 0; i < lines.size(); i++) {
            int gameId = i + 1;
            final String line = lines.get(i);
            final Map<COLOR, Integer> colorIntegerMap = new HashMap<>();

            boolean possibleGame = true;
            int prevEndOfSubset = line.indexOf(':');
            do {
                final int endOfSubset = line.indexOf(';', prevEndOfSubset + 1);
                final String subset = line.substring(prevEndOfSubset + 1, endOfSubset == -1 ? line.length() : endOfSubset).trim();

                final Map<COLOR, Integer> newColorIntegerMap = new HashMap<>();
                int prevEndOfWord = 0;
                do {
                    final int endOfNumber = subset.indexOf(' ', prevEndOfWord);
                    final int endOfWord = subset.indexOf(',', prevEndOfWord);

                    final String numberString = subset.substring(prevEndOfWord, endOfNumber);
                    final String wordString = subset.substring(endOfNumber + 1, endOfWord == -1 ? subset.length() : endOfWord);

                    final COLOR color = COLOR.valueOf(wordString.toUpperCase(Locale.ROOT));
                    final int number = Integer.decode(numberString);
                    newColorIntegerMap.put(color, number);

                    prevEndOfWord = endOfWord == -1 ? endOfWord : endOfWord + 2;
                } while (prevEndOfWord != -1);

                combineSubsets(colorIntegerMap, newColorIntegerMap);

                prevEndOfSubset = endOfSubset;
            } while (prevEndOfSubset != -1);

            result += getPowerOfSubset(colorIntegerMap);
        }

        System.out.println(result); // 70924
    }
}
