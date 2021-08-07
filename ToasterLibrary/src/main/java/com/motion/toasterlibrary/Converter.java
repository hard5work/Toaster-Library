package com.motion.toasterlibrary;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Converter {
    public static String beautifyLongTimeDate(long millisUntilFinished) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisUntilFinished);
        return formatter.format(calendar.getTime());
    }
}
