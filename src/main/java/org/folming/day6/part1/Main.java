package org.folming.day6.part1;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day6/input"), StandardCharsets.UTF_8);
        final String[] timeStringValues = lines.get(0).split(":")[1].trim().split("[\s]+");
        final String[] recordDistanceStringValues = lines.get(1).split(":")[1].trim().split("[\s]+");

        final List<Integer> timeValues = new ArrayList<>(timeStringValues.length);
        final List<Integer> recordDistanceValues = new ArrayList<>(recordDistanceStringValues.length);

        Arrays.stream(timeStringValues).forEach(v -> timeValues.add(Integer.parseInt(v)));
        Arrays.stream(recordDistanceStringValues).forEach(v -> recordDistanceValues.add(Integer.parseInt(v)));

        final List<Integer> answers = new ArrayList<>();

        for (int i = 0; i < timeValues.size(); i++) {
            final int timeValue = timeValues.get(i);
            final int recordDistanceValue = recordDistanceValues.get(i);

            int l = 0;
            for (int j = 1; j < timeValue; j++) {
                int distance = j * (timeValue - j);
                if (distance > recordDistanceValue) {
                    l = j;
                    break;
                }
            }

            int r = 0;
            for (int j = timeValue - 1; j >= 0; j--) {
                int distance = j * (timeValue - j);
                if (distance > recordDistanceValue) {
                    r = j;
                    break;
                }
            }

            answers.add(r - l + 1);
        }

        int result = 1;
        for (final var answer : answers) {
            result *= answer;
        }

        System.out.println(result); // 608902
    }
}
