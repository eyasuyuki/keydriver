package org.javaopen.keydriver.data;

import java.util.Random;

public class DummyMatches {
    private static Random random = new Random();
    public static Matches getRandom() {
        int index = random.nextInt(Matches.values().length);
        return Matches.values()[index];
    }
}
