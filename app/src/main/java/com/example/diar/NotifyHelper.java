package com.example.diar;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.diar.data.DiyarDb;
import com.example.diar.data.Prefs;

/** إشعارات التطبيق: تُحفظ في شاشة التنبيهات وتظهر في شريط الإشعارات */
public final class NotifyHelper {
    private static final String CHANNEL = "diyar_bookings";

    private NotifyHelper() {}

    /** يطلب إذن الإشعارات مرة واحدة (أندرويد ١٣ فأحدث) */
    public static void askPermissionOnce(Activity a) {
        if (Prefs.askedNotifyPermission(a)) return;
        askPermission(a);
    }

    /** يطلب الإذن إذا ما كان ممنوحاً (يُستخدم عند تفعيل الإشعارات من الإعدادات) */
    public static void askPermission(Activity a) {
        if (Build.VERSION.SDK_INT < 33) return;
        if (ContextCompat.checkSelfPermission(a, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 7);
        }
        Prefs.setAskedNotifyPermission(a);
    }

    public static void send(Context c, String title, String body) {
        new DiyarDb(c).addNotice(title, body);
        if (!Prefs.notificationsOn(c)) return;
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(c,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationManager manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(new NotificationChannel(CHANNEL,
                    c.getString(R.string.notify_channel), NotificationManager.IMPORTANCE_DEFAULT));
        }

        Intent open = new Intent(c, NotificationsActivity.class);
        PendingIntent pending = PendingIntent.getActivity(c, 0, open,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, CHANNEL)
                .setSmallIcon(R.drawable.ic_notify)
                .setColor(ContextCompat.getColor(c, R.color.green))
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pending)
                .setAutoCancel(true);
        manager.notify((int) (System.currentTimeMillis() % 100000), builder.build());
    }
}
