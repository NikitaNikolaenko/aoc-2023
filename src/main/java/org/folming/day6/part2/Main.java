package org.folming.day6.part2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day6/input"), StandardCharsets.UTF_8);

        final String timeString = lines.get(0).split(":")[1].trim();
        final String recordDistanceString = lines.get(1).split(":")[1].trim();

        long timeValue = 0L;
        for (int i = 0; i < timeString.length(); i++) {
            char c = timeString.charAt(i);
            if (c >= '0' && c <= '9') {
                timeValue *= 10;
                timeValue += c - '0';
            }
        }

        long recordDistanceValue = 0L;
        for (int i = 0; i < recordDistanceString.length(); i++) {
            char c = recordDistanceString.charAt(i);
            if (c >= '0' && c <= '9') {
                recordDistanceValue *= 10;
                recordDistanceValue += c - '0';
            }
        }

        long l = 0;
        for (long j = 1; j < timeValue; j++) {
            long distance = j * (timeValue - j);
            if (distance > recordDistanceValue) {
                l = j;
                break;
            }
        }

        long r = 0;
        for (long j = timeValue - 1; j >= 0; j--) {
            long distance = j * (timeValue - j);
            if (distance > recordDistanceValue) {
                r = j;
                break;
            }
        }

        System.out.println(r - l + 1); // 46173809
    }
}
