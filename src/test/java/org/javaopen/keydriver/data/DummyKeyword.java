package org.javaopen.keydriver.data;

import java.util.Random;

public class DummyKeyword {
    private static Random random = new Random();
    public static Keyword getRandom() {
        int index = random.nextInt(Keyword.values().length);
        return Keyword.values()[index];
    }
}
