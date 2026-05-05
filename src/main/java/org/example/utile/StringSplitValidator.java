package org.example.utile;


public class StringSplitValidator {

    public static boolean validateSplitResult(String[] split, int expectedLength, String errorMessage) {
        if (split.length != expectedLength) {
            System.out.println(errorMessage);
            return true;
        }
        return false;
    }
}
