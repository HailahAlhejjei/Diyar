package com.example.diar.data;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** تنسيق الأرقام والتواريخ بالعربية */
public final class Fmt {
    private static final Locale AR = new Locale("ar", "SA");
    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    private Fmt() {}

    public static String num(long n) {
        return NumberFormat.getInstance(AR).format(n);
    }

    public static String sar(long n) {
        return num(n) + " ر.س";
    }

    public static String iso(Date d) {
        return ISO.format(d);
    }

    public static Date parse(String iso) {
        try {
            return ISO.parse(iso);
        } catch (ParseException e) {
            return null;
        }
    }

    /** مثال: الأربعاء ١٢ نوفمبر ٢٠٢٦ */
    public static String longDate(String iso) {
        Date d = parse(iso);
        if (d == null) return iso;
        return new SimpleDateFormat("EEEE d MMMM yyyy", AR).format(d);
    }

    public static String shortDate(String iso) {
        Date d = parse(iso);
        if (d == null) return iso;
        return new SimpleDateFormat("d MMMM", AR).format(d);
    }

    public static String dateTime(long millis) {
        return new SimpleDateFormat("d MMMM، h:mm a", AR).format(new Date(millis));
    }
}
