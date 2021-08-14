package com.motion.toasterlibrary.productive;


public class TestUtils {

    public static int isInteger(String val, int exceptionValue) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return exceptionValue;
        }
    }
}