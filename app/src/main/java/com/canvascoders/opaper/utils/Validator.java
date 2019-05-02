package com.canvascoders.opaper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by test on 8/29/2017.
 */

public class Validator {

    public static boolean isValidMobile(String phone) {
        boolean isValid;
        isValid=phone.matches("^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$");
        return isValid;
    }

    public final static boolean isEmailValid(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isNullOrEmpty(String myString) {
        return myString != null && !myString.isEmpty() && !myString.equals("null");
    }

    public static boolean isNumberLengthValid(String number) {
        if (number.length() < 10) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isNameLengthValid(String name) {
        if (name.length() < 3) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean isUserNameLengthValid(String name) {
        if (name.length() < 3) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public static boolean isPasswordLengthValid(String password) {
        if (password.length() < 8) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isPasswordMatching(String password, String confirmPassword) {
        Pattern pattern = Pattern.compile(password, Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(confirmPassword);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

}
