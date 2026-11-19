package com.example.diar;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

/** الشريط السفلي: اكتشف / بحث / رحلاتي / حسابي — كل زر يفتح شاشته بـ Intent */
public final class BottomNav {
    private BottomNav() {}

    public static final int HOME = 0, SEARCH = 1, TRIPS = 2, PROFILE = 3;

    public static void setup(Activity a, int selected) {
        int[] ids = {R.id.nav_home, R.id.nav_search, R.id.nav_trips, R.id.nav_profile};
        Class<?>[] targets = {HomeActivity.class, SearchActivity.class, TripActivity.class, ProfileActivity.class};
        for (int i = 0; i < ids.length; i++) {
            TextView tab = a.findViewById(ids[i]);
            boolean on = i == selected;
            tab.setTextColor(ContextCompat.getColor(a, on ? R.color.green : R.color.muted));
            tab.setTypeface(null, on ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
            final Class<?> target = targets[i];
            tab.setOnClickListener(v -> {
                if (on) return;
                Intent intent = new Intent(a, target);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                a.startActivity(intent);
                a.overridePendingTransition(0, 0);
            });
        }
    }
}
