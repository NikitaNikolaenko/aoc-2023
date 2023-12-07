package org.folming.day7.part2;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND
}

enum Card {
    JACK('J'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');

    final char c;

    Card(char c) {
        this.c = c;
    }

    public static Card ofChar(char c){
        Card[] values = Card.values();
        for (Card value : values) {
            if(value.c == c){
                return value;
            }

        }
        throw new IllegalArgumentException("Card not found");
    }
}

class Hand implements Comparable<Hand> {
    public Card[] cards;
    public long bid;
    private HandType handType;

    public Hand(final Card[] cards, final long bid) {
        this.cards = cards;
        this.bid = bid;
        calculateHandType();
    }

    @Override
    public int compareTo(Hand o) {
        if (this.handType.ordinal() > o.handType.ordinal()) {
            return 1;
        } else if (this.handType.ordinal() < o.handType.ordinal()) {
            return -1;
        }

        for (int i = 0; i < cards.length; i++) {
            if (this.cards[i].ordinal() > o.cards[i].ordinal()) {
                return 1;
            } else if (this.cards[i].ordinal() < o.cards[i].ordinal()) {
                return -1;
            }
        }

        return 0;
    }

    private void calculateHandType() {
        int jokers = 0;

        Map<Card, Integer> cardMap = new HashMap<>();
        for (final Card card : cards) {
            if (card.c == 'J') {
                ++jokers;
                continue;
            }
            cardMap.merge(card, 1, Integer::sum);
        }

        if (cardMap.size() == 0) {
            // Super rare edge case. All cards are J.
            handType = HandType.FIVE_OF_A_KIND;
            return;
        }

        if (cardMap.size() == 1) {
            handType = HandType.FIVE_OF_A_KIND;
            return;
        }

        if (cardMap.size() == 5) {
            handType = HandType.HIGH_CARD;
            return;
        }

        List<Integer> sortedValues = new ArrayList<>(cardMap.values().stream().sorted(Comparator.reverseOrder()).toList());
        int topValue = sortedValues.get(0);
        topValue += jokers;
        sortedValues.set(0, topValue);

        boolean possibleFullHouse = false;
        boolean possibleThreeOfAKind = false;
        boolean possibleTwoPair = false;

        for (final int value : sortedValues) {
            if (value == 4) {
                handType = HandType.FOUR_OF_A_KIND;
                return;
            }

            if (value == 3) {
                possibleFullHouse = true;
                possibleThreeOfAKind = true;
            }

            if (value == 2) {
                if (possibleFullHouse) {
                    handType = HandType.FULL_HOUSE;
                    return;
                }

                if (possibleTwoPair) {
                    handType = HandType.TWO_PAIR;
                    return;
                }
                possibleTwoPair = true;
            }
        }

        if (possibleThreeOfAKind) {
            handType = HandType.THREE_OF_A_KIND;
            return;
        }

        if (possibleTwoPair) {
            handType = HandType.ONE_PAIR;
        }
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        final List<String> lines = Files.readAllLines(Path.of("src/main/java/org/folming/day7/input"), StandardCharsets.UTF_8);

        final List<Hand> hands = new ArrayList<>(lines.size());
        for (final var line : lines) {
            final String[] temp = line.split(" ");
            final String cardsString = temp[0];
            final long bid = Long.parseLong(temp[1]);

            final Card[] cards = new Card[cardsString.length()];
            for (int i = 0; i < cardsString.length(); i++) {
                cards[i] = Card.ofChar(cardsString.charAt(i));
            }

            hands.add(new Hand(cards, bid));
        }

        Collections.sort(hands);

        long result = 0;

        for (int i = 0; i < hands.size(); i++) {
            result += hands.get(i).bid * (i + 1);
        }

        System.out.println(result); // 247885995
    }
}
