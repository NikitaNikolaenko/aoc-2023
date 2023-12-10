package org.folming.day10.part1;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

record Pos(int x, int y) {
    public boolean equals(Pos other) {
        return this.x == other.x() && this.y == other.y();
    }
}

record PosSearchHelper(Pos current, Direction cameFrom) {}

enum Direction {
    WEST,
    NORTH,
    EAST,
    SOUTH
}

public class Main {
    private static Set<Character> linkedWestDirection = Set.of('-', 'F', 'L');
    private static Set<Character> linkedNorthDirection = Set.of('|', '7', 'F');
    private static Set<Character> linkedEastDirection = Set.of('-', 'J', '7');
    private static Set<Character> linkedSouthDirection = Set.of('|', 'L', 'J');

    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day10/input"), StandardCharsets.UTF_8);
        final char[][] nodes = new char[lines.size()][lines.get(0).length()];

        Pos start = null;
        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            for (int j = 0; j < lines.get(0).length(); j++) {
                nodes[i][j] = line.charAt(j);
                if (nodes[i][j] == 'S') {
                    start = new Pos(j, i);
                }
            }
        }

        final List<PosSearchHelper> nextToSearchFor = new ArrayList<>(2);

        if (start.x() != 0 && linkedWestDirection.contains(nodes[start.y()][start.x() - 1])) {
            nextToSearchFor.add(new PosSearchHelper(new Pos(start.x() - 1, start.y()), Direction.EAST));
        }

        if (start.y() != 0 && linkedNorthDirection.contains(nodes[start.y() - 1][start.x()])) {
            nextToSearchFor.add(new PosSearchHelper(new Pos(start.x(), start.y() - 1), Direction.SOUTH));
        }

        if (start.x() != nodes[0].length - 1 && linkedEastDirection.contains(nodes[start.y()][start.x() + 1])) {
            nextToSearchFor.add(new PosSearchHelper(new Pos(start.x() + 1, start.y()), Direction.WEST));
        }

        if (start.y() != nodes.length - 1 && linkedSouthDirection.contains(nodes[start.y() + 1][start.x()])) {
            nextToSearchFor.add(new PosSearchHelper(new Pos(start.x(), start.y() + 1), Direction.NORTH));
        }

        long steps = 1L;

        while (!nextToSearchFor.get(0).current().equals(nextToSearchFor.get(1).current())) {
            ++steps;

            final PosSearchHelper pipe1 = findNextPipeNode(nextToSearchFor.get(0), nodes);
            final PosSearchHelper pipe2 = findNextPipeNode(nextToSearchFor.get(1), nodes);

            nextToSearchFor.clear();
            nextToSearchFor.add(pipe1);
            nextToSearchFor.add(pipe2);
        }

        System.out.println(steps); // 6907
    }

    private static PosSearchHelper findNextPipeNode(final PosSearchHelper posSearchHelper, final char[][] nodes) {
        final Pos currentPos = posSearchHelper.current();
        final char currentPipe = nodes[currentPos.y()][currentPos.x()];

        switch (currentPipe) {
            case '|':
                return posSearchHelper.cameFrom() == Direction.NORTH ?
                    new PosSearchHelper(new Pos(currentPos.x(), currentPos.y() + 1), Direction.NORTH) :
                    new PosSearchHelper(new Pos(currentPos.x(), currentPos.y() - 1), Direction.SOUTH);
            case '-':
                return posSearchHelper.cameFrom() == Direction.WEST ?
                    new PosSearchHelper(new Pos(currentPos.x() + 1, currentPos.y()), Direction.WEST) :
                    new PosSearchHelper(new Pos(currentPos.x() - 1, currentPos.y()), Direction.EAST);
            case 'L':
                return posSearchHelper.cameFrom() == Direction.NORTH ?
                    new PosSearchHelper(new Pos(currentPos.x() + 1, currentPos.y()), Direction.WEST) :
                    new PosSearchHelper(new Pos(currentPos.x(), currentPos.y() - 1), Direction.SOUTH);
            case 'J':
                return posSearchHelper.cameFrom() == Direction.NORTH ?
                    new PosSearchHelper(new Pos(currentPos.x() - 1, currentPos.y()), Direction.EAST) :
                    new PosSearchHelper(new Pos(currentPos.x(), currentPos.y() - 1), Direction.SOUTH);
            case '7':
                return posSearchHelper.cameFrom() == Direction.WEST ?
                    new PosSearchHelper(new Pos(currentPos.x(), currentPos.y() + 1), Direction.NORTH) :
                    new PosSearchHelper(new Pos(currentPos.x() - 1, currentPos.y()), Direction.EAST);
            case 'F':
                return posSearchHelper.cameFrom() == Direction.EAST ?
                    new PosSearchHelper(new Pos(currentPos.x(), currentPos.y() + 1), Direction.NORTH) :
                    new PosSearchHelper(new Pos(currentPos.x() + 1, currentPos.y()), Direction.WEST);
            default:
                throw new RuntimeException();
        }
    }
}
