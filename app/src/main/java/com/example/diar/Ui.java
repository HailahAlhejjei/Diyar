package com.example.diar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.diar.data.DiyarDb;

/** أدوات مشتركة بين الشاشات */
public final class Ui {
    private Ui() {}

    /** يضيف مسافة أشرطة النظام (الشاشة من الحافة للحافة) */
    public static void insets(View root, boolean top) {
        // التطبيق عربي أولاً: اتجاه من اليمين لليسار حتى لو لغة الجهاز إنجليزية
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, in) -> {
            Insets bars = in.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(bars.left, top ? bars.top : 0, bars.right, bars.bottom);
            return in;
        });
    }

    /** يضع الصورة، وإذا ما فيه صورة يبقى اللون الرملي */
    public static void image(ImageView iv, int res) {
        iv.setClipToOutline(true);
        if (res != 0) {
            iv.setImageResource(res);
        } else {
            iv.setImageDrawable(null);
            iv.setBackgroundResource(R.drawable.bg_image_round);
        }
    }

    /** يفتح تطبيق الخرائط في الجهاز على المكان */
    public static void openMap(Activity a, String query) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(query)));
        if (i.resolveActivity(a.getPackageManager()) != null) {
            a.startActivity(i);
        } else {
            Toast.makeText(a, R.string.no_map_app, Toast.LENGTH_SHORT).show();
        }
    }

    /** شريط العنوان: زر رجوع + عنوان */
    public static void header(Activity a, CharSequence title) {
        View b = a.findViewById(R.id.btn_back);
        if (b != null) b.setOnClickListener(v -> a.finish());
        TextView t = a.findViewById(R.id.header_title);
        if (t != null) t.setText(title);
    }

    /** زر إضافي في يسار شريط العنوان */
    public static TextView headerAction(Activity a, CharSequence text, View.OnClickListener click) {
        TextView act = a.findViewById(R.id.header_action);
        act.setText(text);
        act.setVisibility(View.VISIBLE);
        act.setOnClickListener(click);
        return act;
    }

    /** إخفاء زر الرجوع في شاشات الشريط السفلي */
    public static void hideBack(Activity a) {
        View b = a.findViewById(R.id.btn_back);
        if (b != null) b.setVisibility(View.INVISIBLE);
    }

    /** زر القلب: يقرأ الحالة من قاعدة البيانات ويقلبها عند الضغط */
    public static void heart(Activity a, TextView heart, String expId) {
        DiyarDb db = new DiyarDb(a);
        heart.setText(db.isSaved(expId) ? "♥" : "♡");
        heart.setOnClickListener(v -> {
            boolean saved = db.toggleSaved(expId);
            heart.setText(saved ? "♥" : "♡");
            Toast.makeText(a, saved ? R.string.saved_added : R.string.saved_removed, Toast.LENGTH_SHORT).show();
        });
    }
}
