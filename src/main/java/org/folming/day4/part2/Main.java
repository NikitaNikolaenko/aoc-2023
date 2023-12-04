package org.folming.day4.part2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day4/input"), StandardCharsets.UTF_8);
        final int[] instances = new int[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            instances[i] = 1;
        }

        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
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

            for (int j = 0; j < count; j++) {
                int cardToCopy = i + j + 1;
                if (cardToCopy < instances.length) {
                    instances[cardToCopy] += instances[i];
                }
            }
        }

        int result = 0;
        for (final var instance : instances) {
            result += instance;
        }

        System.out.println(result); // 5422730
    }
}
