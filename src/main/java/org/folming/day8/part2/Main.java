package org.folming.day8.part2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

record NextValue(String left, String right) {}

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day8/input"), StandardCharsets.UTF_8);
        final String steps = lines.get(0);
        final Map<String, NextValue> map = new HashMap<>();
        final List<String> startPositions = new ArrayList<>();
        List<String> currentPositions = new ArrayList<>();

        for (int i = 2; i < lines.size(); i++) {
            String[] temp = lines.get(i).split(" = ");
            final String key = temp[0];
            final String value = temp[1].substring(1, temp[1].length() - 1);

            temp = value.split(", ");
            final String left = temp[0];
            final String right = temp[1];

            map.put(key, new NextValue(left, right));

            if (key.endsWith("A")) {
                startPositions.add(key);
                currentPositions.add(key);
            }
        }

        long stepsDone = 0;
        int idx = 0;
        final Map<String, Long> paths = new HashMap<>();

        while (!finished(currentPositions, startPositions, stepsDone, paths)) {
            ++stepsDone;
            final List<String> newPositions = new ArrayList<>(currentPositions.size());

            for (final String currentPosition : currentPositions) {
                final NextValue nextValue = map.get(currentPosition);

                switch (steps.charAt(idx))  {
                    case 'L' -> newPositions.add(nextValue.left());
                    case 'R' -> newPositions.add(nextValue.right());
                }
            }

            if (++idx == steps.length()) {
                idx = 0;
            }

            currentPositions = newPositions;
        }

        long result = 1;
        for (final long val : paths.values()) {
            result = lcm(result, val);
        }

        System.out.println(result); // 12315788159977
    }

    private static boolean finished(final List<String> currentPositions, final List<String> startPositions, final long steps, final Map<String, Long> paths) {
        for (int i = 0; i < currentPositions.size(); i++) {
            final String position = currentPositions.get(i);

            if (position.endsWith("Z")) {
                final String startPos = startPositions.get(i);
                paths.putIfAbsent(startPos + "-" + position, steps);

                // This check is only possible due to my luck with input - we could have ended up, for example, with
                // 2 paths from the same start point to different end points.
                if (paths.size() == startPositions.size()) {
                    return true;
                }
            }
        }

        return false;
    }

    private static long lcm(long number1, long number2) {
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }
}
