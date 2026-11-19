package com.example.diar.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/** قاعدة بيانات SQLite: المحفوظات، الحجوزات، الرحلة، والتنبيهات */
public class DiyarDb extends SQLiteOpenHelper {

    public DiyarDb(Context context) {
        super(context, "diyar.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table saved(exp_id text primary key not null);");
        db.execSQL("create table booking(id integer primary key autoincrement not null, code text not null, "
                + "exp_id text not null, date text not null, period text, adults integer, children integer, "
                + "total integer, method text, created integer);");
        db.execSQL("create table trip(id integer primary key autoincrement not null, exp_id text not null, "
                + "date text not null, period text, people integer, code text);");
        db.execSQL("create table notice(id integer primary key autoincrement not null, title text not null, "
                + "body text, created integer, seen integer default 0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists saved");
        db.execSQL("drop table if exists booking");
        db.execSQL("drop table if exists trip");
        db.execSQL("drop table if exists notice");
        onCreate(db);
    }

    // ---------- المحفوظات ----------

    public boolean isSaved(String expId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("saved", null, "exp_id = ?", new String[]{expId}, null, null, null);
        boolean found = c.moveToFirst();
        c.close();
        return found;
    }

    /** يقلب حالة الحفظ ويرجع الحالة الجديدة */
    public boolean toggleSaved(String expId) {
        SQLiteDatabase db = getWritableDatabase();
        if (isSaved(expId)) {
            db.delete("saved", "exp_id = ?", new String[]{expId});
            return false;
        }
        ContentValues v = new ContentValues();
        v.put("exp_id", expId);
        db.insert("saved", null, v);
        return true;
    }

    public List<String> savedIds() {
        List<String> out = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("select exp_id from saved", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            out.add(c.getString(0));
            c.moveToNext();
        }
        c.close();
        return out;
    }

    // ---------- الحجوزات ----------

    public static class Booking {
        public long id;
        public String code, expId, date, period, method;
        public int adults, children, total;
    }

    public long addBooking(Booking b) {
        ContentValues v = new ContentValues();
        v.put("code", b.code);
        v.put("exp_id", b.expId);
        v.put("date", b.date);
        v.put("period", b.period);
        v.put("adults", b.adults);
        v.put("children", b.children);
        v.put("total", b.total);
        v.put("method", b.method);
        v.put("created", System.currentTimeMillis());
        return getWritableDatabase().insert("booking", null, v);
    }

    public Booking booking(long id) {
        Cursor c = getReadableDatabase().rawQuery(
                "select id, code, exp_id, date, period, adults, children, total, method from booking where id = ?",
                new String[]{String.valueOf(id)});
        Booking b = null;
        if (c.moveToFirst()) {
            b = new Booking();
            b.id = c.getLong(0);
            b.code = c.getString(1);
            b.expId = c.getString(2);
            b.date = c.getString(3);
            b.period = c.getString(4);
            b.adults = c.getInt(5);
            b.children = c.getInt(6);
            b.total = c.getInt(7);
            b.method = c.getString(8);
        }
        c.close();
        return b;
    }

    public int bookingCount() {
        Cursor c = getReadableDatabase().rawQuery("select count(*) from booking", null);
        c.moveToFirst();
        int n = c.getInt(0);
        c.close();
        return n;
    }

    // ---------- الرحلة ----------

    public static class TripItem {
        public long id;
        public String expId, date, period, code;
        public int people;
    }

    public void addTripItem(String expId, String date, String period, int people, String code) {
        ContentValues v = new ContentValues();
        v.put("exp_id", expId);
        v.put("date", date);
        v.put("period", period);
        v.put("people", people);
        v.put("code", code);
        getWritableDatabase().insert("trip", null, v);
    }

    public boolean tripHasCode(String code) {
        Cursor c = getReadableDatabase().query("trip", null, "code = ?", new String[]{code}, null, null, null);
        boolean found = c.moveToFirst();
        c.close();
        return found;
    }

    public void deleteTripItem(long id) {
        getWritableDatabase().delete("trip", "id = ?", new String[]{String.valueOf(id)});
    }

    public List<TripItem> tripItems() {
        List<TripItem> out = new ArrayList<>();
        Cursor c = getReadableDatabase().query("trip",
                new String[]{"id", "exp_id", "date", "period", "people", "code"},
                null, null, null, null, "date, id");
        c.moveToFirst();
        while (!c.isAfterLast()) {
            TripItem t = new TripItem();
            t.id = c.getLong(0);
            t.expId = c.getString(1);
            t.date = c.getString(2);
            t.period = c.getString(3);
            t.people = c.getInt(4);
            t.code = c.getString(5);
            out.add(t);
            c.moveToNext();
        }
        c.close();
        return out;
    }

    // ---------- التنبيهات ----------

    public static class Notice {
        public long id, created;
        public String title, body;
        public boolean seen;
    }

    public void addNotice(String title, String body) {
        ContentValues v = new ContentValues();
        v.put("title", title);
        v.put("body", body);
        v.put("created", System.currentTimeMillis());
        v.put("seen", 0);
        getWritableDatabase().insert("notice", null, v);
    }

    public List<Notice> notices() {
        List<Notice> out = new ArrayList<>();
        Cursor c = getReadableDatabase().query("notice",
                new String[]{"id", "title", "body", "created", "seen"},
                null, null, null, null, "created desc");
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Notice n = new Notice();
            n.id = c.getLong(0);
            n.title = c.getString(1);
            n.body = c.getString(2);
            n.created = c.getLong(3);
            n.seen = c.getInt(4) == 1;
            out.add(n);
            c.moveToNext();
        }
        c.close();
        return out;
    }

    public int unseenCount() {
        Cursor c = getReadableDatabase().rawQuery("select count(*) from notice where seen = 0", null);
        c.moveToFirst();
        int n = c.getInt(0);
        c.close();
        return n;
    }

    public void markAllSeen() {
        ContentValues v = new ContentValues();
        v.put("seen", 1);
        getWritableDatabase().update("notice", v, null, null);
    }

    /** حذف كل بيانات المستخدم من الإعدادات */
    public void clearAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("saved", null, null);
        db.delete("booking", null, null);
        db.delete("trip", null, null);
        db.delete("notice", null, null);
    }
}
