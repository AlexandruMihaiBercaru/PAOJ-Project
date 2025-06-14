package utils;

import java.util.Random;

public class RandomStringGenerator {

    public static String newString(int targetStringLength){
        int leftLimit = 48; // '0'
        int rightLimit = 122; // 'z'

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
