package com.example.diar.data;

/** خدمة قريبة (مطعم أو موقف) — معلومات فقط بدون حجز */
public class Service {
    public static final String RESTAURANT = "restaurant";
    public static final String PARKING = "parking";

    public final String name;
    public final String type;
    public final String info;
    public final String detail;
    public final int image;
    public final String mapQuery;

    public Service(String name, String type, String info, String detail, int image, String mapQuery) {
        this.name = name;
        this.type = type;
        this.info = info;
        this.detail = detail;
        this.image = image;
        this.mapQuery = mapQuery;
    }
}
