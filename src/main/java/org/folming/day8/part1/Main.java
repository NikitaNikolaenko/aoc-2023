package org.folming.day8.part1;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

record NextValue(String left, String right) {}

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day8/input"), StandardCharsets.UTF_8);
        final String steps = lines.get(0);
        final Map<String, NextValue> map = new HashMap<>();

        for (int i = 2; i < lines.size(); i++) {
            String[] temp = lines.get(i).split(" = ");
            final String key = temp[0];
            final String value = temp[1].substring(1, temp[1].length() - 1);

            temp = value.split(", ");
            final String left = temp[0];
            final String right = temp[1];

            map.put(key, new NextValue(left, right));
        }

        int result = 0;
        String current = "AAA";
        int idx = 0;

        while (!current.equals("ZZZ")) {
            ++result;

            final NextValue nextValue = map.get(current);

            switch (steps.charAt(idx)) {
                case 'L' -> current = nextValue.left();
                case 'R' -> current = nextValue.right();
            }

            if (++idx == steps.length()) {
                idx = 0;
            }
        }

        System.out.println(result); // 18113
    }
}
