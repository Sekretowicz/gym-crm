package com.sekretowicz.gym_crm.utils;

import java.security.SecureRandom;
import java.util.Random;

public class UserUtils {
    private static final Random RANDOM = new SecureRandom();
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";

    public static String generateUsername(String firstName, String lastName) {
        int randomNum = RANDOM.nextInt(900) + 100;
        return firstName.toLowerCase() + lastName.toLowerCase() + randomNum;
    }

    public static String generatePassword(int length) {
        String allChars = LETTERS + DIGITS;
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(allChars.charAt(RANDOM.nextInt(allChars.length())));
        }
        return password.toString();
    }
}
