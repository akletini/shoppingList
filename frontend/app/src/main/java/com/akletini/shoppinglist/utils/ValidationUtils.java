package com.akletini.shoppinglist.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    private ValidationUtils(){}

    public static boolean validateEmail(String email) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return patternMatches(email, regexPattern);
    }

    private static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
