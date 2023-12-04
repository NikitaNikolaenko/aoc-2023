package org.folming.day4.part1;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day4/input"), StandardCharsets.UTF_8);

        int result = 0;
        for (final var line : lines) {
            final String[] temp = line.split(":[\s]+")[1].split("[\s]+\\|[\s]+");
            final String winningNumbersLine = temp[0];
            final String myNumbersLine = temp[1];

            final String[] winningNumbersStringArray = winningNumbersLine.split("[\s]+", -1);
            final String[] myNumbersStringArray = myNumbersLine.split("[\s]+", -1);

            final List<Integer> winningNumbers = new ArrayList<>(winningNumbersStringArray.length);
            final List<Integer> myNumbers = new ArrayList<>(myNumbersStringArray.length);

            Arrays.stream(winningNumbersStringArray).forEach(s -> winningNumbers.add(Integer.parseInt(s)));
            Arrays.stream(myNumbersStringArray).forEach(s -> myNumbers.add(Integer.parseInt(s)));

            int count = 0;
            for (final var myNumber : myNumbers) {
                for (final var winningNumber : winningNumbers) {
                    if (myNumber.equals(winningNumber)) {
                        ++count;
                        break;
                    }
                }
            }

            if (count != 0) {
                result += Math.pow(2, count - 1);
            }
        }

        System.out.println(result); // 24733
    }
}
