package org.folming.day1.part1;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day1/input"), StandardCharsets.UTF_8);

        int result = 0;
        for (final String line : lines) {
            int firstDigit = 0;
            int lastDigit = 0;

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) >= '0' && line.charAt(i) <= '9') {
                    firstDigit = line.charAt(i) - '0';
                    break;
                }
            }

            for (int i = line.length() - 1; i >= 0; i--) {
                if (line.charAt(i) >= '0' && line.charAt(i) <= '9') {
                    lastDigit = line.charAt(i) - '0';
                    break;
                }
            }

            final int partialResult = (firstDigit * 10) + lastDigit;
            result += partialResult;
        }

        System.out.println(result); // 54877
    }
}
