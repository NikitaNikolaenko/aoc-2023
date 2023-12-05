package org.folming.day5.part1;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

record TaskMap(long destinationRangeStart, long sourceRangeStart, long rangeLength) {}

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day5/input"), StandardCharsets.UTF_8);

        final List<Long> seeds = new ArrayList<>();
        Arrays.stream(lines.get(0).split(": ")[1].split(" ")).forEach(v -> seeds.add(Long.parseLong(v)));

        int seedToSoilMapStartLineIdx = 0;
        int soilToFertilizerMapStartLineIdx = 0;
        int fertilizerToWaterMapStartLineIdx = 0;
        int waterToLightMapStartLineIdx = 0;
        int lightToTemperatureStartLineIdx = 0;
        int temperatureToHumidityStartLineIdx = 0;
        int humidityToLocationStartLineIdx = 0;
        for (int i = 2; i < lines.size(); i++) {
            if (lines.get(i).equals("seed-to-soil map:")) {
                seedToSoilMapStartLineIdx = i;
                continue;
            }

            if (lines.get(i).equals("soil-to-fertilizer map:")) {
                soilToFertilizerMapStartLineIdx = i;
                continue;
            }

            if (lines.get(i).equals("fertilizer-to-water map:")) {
                fertilizerToWaterMapStartLineIdx = i;
                continue;
            }

            if (lines.get(i).equals("water-to-light map:")) {
                waterToLightMapStartLineIdx = i;
                continue;
            }

            if (lines.get(i).equals("light-to-temperature map:")) {
                lightToTemperatureStartLineIdx = i;
                continue;
            }

            if (lines.get(i).equals("temperature-to-humidity map:")) {
                temperatureToHumidityStartLineIdx = i;
                continue;
            }

            if (lines.get(i).equals("humidity-to-location map:")) {
                humidityToLocationStartLineIdx = i;
            }
        }

        final List<TaskMap> seedToSoilMapList = new ArrayList<>();
        for (int i = seedToSoilMapStartLineIdx + 1; i < soilToFertilizerMapStartLineIdx - 1; i++) {
            long[] values = Arrays.stream(lines.get(i).split(" ")).mapToLong(Long::parseLong).toArray();
            seedToSoilMapList.add(new TaskMap(values[0], values[1], values[2]));
        }

        final List<TaskMap> soilToFertilizerMapList = new ArrayList<>();
        for (int i = soilToFertilizerMapStartLineIdx + 1; i < fertilizerToWaterMapStartLineIdx - 1; i++) {
            long[] values = Arrays.stream(lines.get(i).split(" ")).mapToLong(Long::parseLong).toArray();
            soilToFertilizerMapList.add(new TaskMap(values[0], values[1], values[2]));
        }

        final List<TaskMap> fertilizerToWaterMapList = new ArrayList<>();
        for (int i = fertilizerToWaterMapStartLineIdx + 1; i < waterToLightMapStartLineIdx - 1; i++) {
            long[] values = Arrays.stream(lines.get(i).split(" ")).mapToLong(Long::parseLong).toArray();
            fertilizerToWaterMapList.add(new TaskMap(values[0], values[1], values[2]));
        }

        final List<TaskMap> waterToLightMapList = new ArrayList<>();
        for (int i = waterToLightMapStartLineIdx + 1; i < lightToTemperatureStartLineIdx - 1; i++) {
            long[] values = Arrays.stream(lines.get(i).split(" ")).mapToLong(Long::parseLong).toArray();
            waterToLightMapList.add(new TaskMap(values[0], values[1], values[2]));
        }

        final List<TaskMap> lightToTemperatureMapList = new ArrayList<>();
        for (int i = lightToTemperatureStartLineIdx + 1; i < temperatureToHumidityStartLineIdx - 1; i++) {
            long[] values = Arrays.stream(lines.get(i).split(" ")).mapToLong(Long::parseLong).toArray();
            lightToTemperatureMapList.add(new TaskMap(values[0], values[1], values[2]));
        }

        final List<TaskMap> temperatureToHumidityMapList = new ArrayList<>();
        for (int i = temperatureToHumidityStartLineIdx + 1; i < humidityToLocationStartLineIdx - 1; i++) {
            long[] values = Arrays.stream(lines.get(i).split(" ")).mapToLong(Long::parseLong).toArray();
            temperatureToHumidityMapList.add(new TaskMap(values[0], values[1], values[2]));
        }

        final List<TaskMap> humidityToLocationMapList = new ArrayList<>();
        for (int i = humidityToLocationStartLineIdx + 1; i < lines.size(); i++) {
            long[] values = Arrays.stream(lines.get(i).split(" ")).mapToLong(Long::parseLong).toArray();
            humidityToLocationMapList.add(new TaskMap(values[0], values[1], values[2]));
        }

        final List<Long> soils = new ArrayList<>();
        seeds.forEach(s -> soils.add(map(s, seedToSoilMapList)));

        final List<Long> fertilizers = new ArrayList<>();
        soils.forEach(s -> fertilizers.add(map(s, soilToFertilizerMapList)));

        final List<Long> waters = new ArrayList<>();
        fertilizers.forEach(f -> waters.add(map(f, fertilizerToWaterMapList)));

        final List<Long> lights = new ArrayList<>();
        waters.forEach(w -> lights.add(map(w, waterToLightMapList)));

        final List<Long> temperatures = new ArrayList<>();
        lights.forEach(l -> temperatures.add(map(l, lightToTemperatureMapList)));

        final List<Long> humidities = new ArrayList<>();
        temperatures.forEach(t -> humidities.add(map(t, temperatureToHumidityMapList)));

        final List<Long> locations = new ArrayList<>();
        humidities.forEach(h -> locations.add(map(h, humidityToLocationMapList)));

        long result = locations.get(0);
        for (int i = 1; i < locations.size(); i++) {
            if (locations.get(i) < result) {
                result = locations.get(i);
            }
        }

        System.out.println(result); // 318728750
    }

    private static long map(final long source, final List<TaskMap> maps) {
        for (final var map : maps) {
            if (map.sourceRangeStart() <= source && map.sourceRangeStart() + map.rangeLength() > source) {
                final long diff = source - map.sourceRangeStart();
                return map.destinationRangeStart() + diff;
            }
        }

        return source;
    }
}
