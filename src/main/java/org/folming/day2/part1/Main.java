package org.folming.day2.part1;

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

    private static boolean possibleSubset(final Map<COLOR, Integer> colorIntegerMap) {
        Integer redInt = colorIntegerMap.get(COLOR.RED);
        if (redInt != null && redInt > 12) {
            return false;
        }

        Integer greenInt = colorIntegerMap.get(COLOR.GREEN);
        if (greenInt != null && greenInt > 13) {
            return false;
        }

        Integer blueInt = colorIntegerMap.get(COLOR.BLUE);
        //noinspection RedundantIfStatement
        if (blueInt != null && blueInt > 14) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day2/input"), StandardCharsets.UTF_8);

        int result = 0;

        for (int i = 0; i < lines.size(); i++) {
            int gameId = i + 1;
            final String line = lines.get(i);

            boolean possibleGame = true;
            int prevEndOfSubset = line.indexOf(':');
            do {
                final int endOfSubset = line.indexOf(';', prevEndOfSubset + 1);
                final String subset = line.substring(prevEndOfSubset + 1, endOfSubset == -1 ? line.length() : endOfSubset).trim();

                final Map<COLOR, Integer> colorIntegerMap = new HashMap<>();
                int prevEndOfWord = 0;
                do {
                    final int endOfNumber = subset.indexOf(' ', prevEndOfWord);
                    final int endOfWord = subset.indexOf(',', prevEndOfWord);

                    final String numberString = subset.substring(prevEndOfWord, endOfNumber);
                    final String wordString = subset.substring(endOfNumber + 1, endOfWord == -1 ? subset.length() : endOfWord);

                    final COLOR color = COLOR.valueOf(wordString.toUpperCase(Locale.ROOT));
                    final int number = Integer.decode(numberString);
                    colorIntegerMap.put(color, number);

                    prevEndOfWord = endOfWord == -1 ? endOfWord : endOfWord + 2;
                } while (prevEndOfWord != -1);

                if (!possibleSubset(colorIntegerMap)) {
                    possibleGame = false;
                    break;
                }

                prevEndOfSubset = endOfSubset;
            } while (prevEndOfSubset != -1);

            if (possibleGame) {
                result += gameId;
            }
        }

        System.out.println(result); // 2771
    }
}
