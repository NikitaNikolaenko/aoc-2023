package org.folming.day9.part1;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day9/input"), StandardCharsets.UTF_8);

        long result = 0;

        for (final String line : lines) {
            final String[] temp = line.split("\s+");
            List<Long> values = new ArrayList<>(temp.length);
            for (final String s : temp) {
                values.add(Long.parseLong(s));
            }

            long partResult = values.get(values.size() - 1);

            boolean finished = false;
            while (!finished) {
                final List<Long> newValues = new ArrayList<>(values.size() - 1);
                boolean allZeros = true;

                for (int i = 0; i < values.size() - 1; i++) {
                    long sub = values.get(i + 1) - values.get(i);
                    if (sub != 0) {
                        allZeros = false;
                    }
                    newValues.add(sub);
                }

                partResult += newValues.get(newValues.size() - 1);

                values = newValues;

                finished = allZeros;
            }

            result += partResult;
        }

        System.out.println(result); // 2098530125
    }
}
