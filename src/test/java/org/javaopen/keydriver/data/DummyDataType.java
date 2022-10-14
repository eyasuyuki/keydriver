package org.javaopen.keydriver.data;

import java.util.Random;

public class DummyDataType {
    private static Random random = new Random();
    public static DataType getRandom() {
        int index = random.nextInt(DataType.values().length);
        return DataType.values()[index];
    }
}
