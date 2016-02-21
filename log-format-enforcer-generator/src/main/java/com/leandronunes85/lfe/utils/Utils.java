package com.leandronunes85.lfe.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.IntStream.rangeClosed;

public final class Utils {

    private Utils() {
    }

    public static String capitalizeIt(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static <L, R> Stream<Pair<L, R>> pairingWithNextElement(List<L> input,
                                                                   R pairForLastElement,
                                                                   Function<L, R> mapper) {
        if (input == null || input.isEmpty()) {
            return Stream.empty();
        }

        int size = input.size();
        return rangeClosed(1, size)
                .mapToObj(i -> Pair.of(input.get(i - 1), i < size ? mapper.apply(input.get(i)) : pairForLastElement));
    }
}
