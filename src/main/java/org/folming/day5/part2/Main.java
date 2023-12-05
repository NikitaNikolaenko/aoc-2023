package org.folming.day5.part2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

record Range(long rangeStart, long rangeLength) {}

record TaskMap(long destinationRangeStart, long sourceRangeStart, long rangeLength) implements Comparable<TaskMap> {
    @Override
    public int compareTo(TaskMap o) {
        if (this.sourceRangeStart - o.sourceRangeStart < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day5/input"), StandardCharsets.UTF_8);

        final List<Range> seeds = new ArrayList<>();
        final List<String> seedsStringValues = Arrays.stream(lines.get(0).split(": ")[1].split(" ")).toList();
        for (int i = 0; i < seedsStringValues.size(); i += 2) {
            long rangeStart = Long.parseLong(seedsStringValues.get(i));
            long rangeLength = Long.parseLong(seedsStringValues.get(i + 1));
            seeds.add(new Range(rangeStart, rangeLength));
        }

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

        final List<Range> soils = map(seeds, seedToSoilMapList);
        final List<Range> fertilizers = map(soils, soilToFertilizerMapList);
        final List<Range> waters = map(fertilizers, fertilizerToWaterMapList);
        final List<Range> lights = map(waters, waterToLightMapList);
        final List<Range> temperatures = map(lights, lightToTemperatureMapList);
        final List<Range> humidities = map(temperatures, temperatureToHumidityMapList);
        final List<Range> locations = map(humidities, humidityToLocationMapList);

        long result = locations.get(0).rangeStart();
        for (int i = 1; i < locations.size(); i++) {
            if (locations.get(i).rangeStart() < result) {
                result = locations.get(i).rangeStart();
            }
        }

        System.out.println(result); // 37384986
    }

    private static List<Range> map(final List<Range> ranges, final List<TaskMap> maps) {
        final List<Range> result = new ArrayList<>();

        Collections.sort(maps);

        for (final Range range : ranges) {
            long rangeEnd = range.rangeStart() + range.rangeLength() - 1;
            long currentStart = range.rangeStart();
            for (int i = 0; i < maps.size(); i++) {
                final TaskMap map = maps.get(i);
                if (currentStart > rangeEnd) {
                    break;
                }

                if (currentStart < map.sourceRangeStart()) {
                    long newRangeEnd = Math.min(map.sourceRangeStart() - 1, range.rangeStart() + range.rangeLength() - 1);
                    long newRangeLength = newRangeEnd - currentStart + 1;
                    result.add(new Range(currentStart, newRangeLength));
                    currentStart = newRangeEnd + 1;
                    i -= 1;
                    continue;
                }

                if (currentStart >= map.sourceRangeStart() && currentStart <= map.sourceRangeStart() + map.rangeLength() - 1) {
                    long newRangeEnd = Math.min(map.sourceRangeStart() + map.rangeLength() - 1, rangeEnd);
                    long newRangeLength = newRangeEnd - currentStart + 1;
                    long newRangeStart = currentStart - map.sourceRangeStart() + map.destinationRangeStart();
                    result.add(new Range(newRangeStart, newRangeLength));
                    currentStart = newRangeEnd + 1;
                }
            }

            if (currentStart <= rangeEnd) {
                long newRangeLength = rangeEnd - currentStart + 1;
                result.add(new Range(currentStart, newRangeLength));
            }
        }

        return result;
    }
}
