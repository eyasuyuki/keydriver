package org.javaopen.keydriver.data;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

public class DummyParam {
    private static Random random = new Random();
    public static Param getRandom() {
        if (random.nextBoolean()) {
            return getRandomDataType();
        } else {
            return getRandomMatches();
        }
    }

    public static Param getRandomDataType() {
        Tag tag = RandomDataType.getRandom();
        String value = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        String attribute = RandomStringUtils.randomAlphanumeric(random.nextInt(10)+1);
        return new Param(tag.toString()+"["+value+"#"+attribute+"]");
    }

    public static Param getRandomMatches() {
        Tag tag = DummyMatches.getRandom();
        String value = RandomStringUtils.randomAlphanumeric(random.nextInt(20)+1);
        return new Param(tag.toString()+"["+value+"]");
    }
}
