package com.guilhermefgl.rolling.helper;

import android.content.Context;
import android.support.annotation.NonNull;

import com.guilhermefgl.rolling.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatterHelper {

    public static String dateToString(@NonNull Date date, @NonNull Context context) {
        return new SimpleDateFormat(context.getString(R.string.date_time_format), Locale.getDefault())
                .format(date);
    }

    public static Date stringToDate(@NonNull String dateString, @NonNull Context context) throws ParseException {
        return new SimpleDateFormat(context.getString(R.string.date_time_format), Locale.getDefault())
                .parse(dateString);
    }
}
