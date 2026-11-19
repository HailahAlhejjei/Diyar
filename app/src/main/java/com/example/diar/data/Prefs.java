package com.example.diar.data;

import android.content.Context;
import android.content.SharedPreferences;

/** التفضيلات المشتركة: بيانات الدخول والإعدادات */
public final class Prefs {
    private static final String FILE = "diyar_prefs";

    private Prefs() {}

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public static boolean isLoggedIn(Context c) {
        return sp(c).getBoolean("logged_in", false);
    }

    public static void login(Context c, String name, String phone, boolean guest) {
        sp(c).edit()
                .putBoolean("logged_in", true)
                .putString("name", name)
                .putString("phone", phone)
                .putBoolean("guest", guest)
                .apply();
    }

    public static void logout(Context c) {
        sp(c).edit().remove("logged_in").remove("name").remove("phone").remove("guest").apply();
    }

    public static String name(Context c) {
        return sp(c).getString("name", "ضيف");
    }

    public static void setName(Context c, String name) {
        sp(c).edit().putString("name", name).apply();
    }

    public static String phone(Context c) {
        return sp(c).getString("phone", "");
    }

    public static boolean isGuest(Context c) {
        return sp(c).getBoolean("guest", true);
    }

    public static boolean notificationsOn(Context c) {
        return sp(c).getBoolean("notifications", true);
    }

    public static void setNotificationsOn(Context c, boolean on) {
        sp(c).edit().putBoolean("notifications", on).apply();
    }

    public static boolean askedNotifyPermission(Context c) {
        return sp(c).getBoolean("asked_notify", false);
    }

    public static void setAskedNotifyPermission(Context c) {
        sp(c).edit().putBoolean("asked_notify", true).apply();
    }
}
