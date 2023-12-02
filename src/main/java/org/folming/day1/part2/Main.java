package org.folming.day1.part2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    private static Optional<Integer> containsDigit(final String buffer) {
        final Map<String, Integer> integerMap = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
        );

        for (final var entry : integerMap.entrySet()) {
            if (buffer.contains(entry.getKey())) {
                return Optional.of(entry.getValue());
            }
        }

        return Optional.empty();
    }

    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day1/input"), StandardCharsets.UTF_8);

        int result = 0;
        for (final String line : lines) {
            int firstDigit = 0;
            int lastDigit = 0;
            StringBuilder tempBuffer = new StringBuilder();

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) >= '0' && line.charAt(i) <= '9') {
                    firstDigit = line.charAt(i) - '0';
                    tempBuffer = new StringBuilder();
                    break;
                } else {
                    tempBuffer.append(line.charAt(i));
                    if (tempBuffer.length() > 5) {
                        tempBuffer.delete(0, tempBuffer.length() - 5);
                    }
                    Optional<Integer> optionalInteger = containsDigit(tempBuffer.toString());
                    if (optionalInteger.isPresent()) {
                        firstDigit = optionalInteger.get();
                        tempBuffer = new StringBuilder();
                        break;
                    }
                }
            }

            for (int i = line.length() - 1; i >= 0; i--) {
                if (line.charAt(i) >= '0' && line.charAt(i) <= '9') {
                    lastDigit = line.charAt(i) - '0';
                    break;
                } else {
                    tempBuffer.insert(0, line.charAt(i));
                    if (tempBuffer.length() > 5) {
                        tempBuffer.delete(5, tempBuffer.length());
                    }
                    Optional<Integer> optionalInteger = containsDigit(tempBuffer.toString());
                    if (optionalInteger.isPresent()) {
                        lastDigit = optionalInteger.get();
                        break;
                    }
                }
            }

            final int partialResult = (firstDigit * 10) + lastDigit;
            result += partialResult;
        }

        System.out.println(result); // 54100
    }
}
