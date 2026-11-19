package com.example.diar.data;

/** تجربة قابلة للحجز. الأسعار مأخوذة من مصادر تم البحث فيها (انظر DemoData). */
public class Experience {
    public static final int ADVENTURE = 1;
    public static final int RELAX = 2;
    public static final int CULTURE = 4;

    public final String id;
    public final String title;
    public final String cityId;      // riyadh / alula / abha
    public final String place;       // الموقع بالتفصيل
    public final String about;
    public final String duration;
    public final int adultPrice;     // بالريال
    public final int childPrice;     // 0 = لا يوجد سعر أطفال منفصل
    public final String childNote;   // مثلاً "٤–١١ سنة"
    public final boolean perVehicle; // السعر للسيارة وليس للشخص
    public final int moods;          // ADVENTURE | RELAX | CULTURE
    public final boolean family;
    public final int image;          // 0 = بدون صورة
    public final String mapQuery;    // اسم البحث في تطبيق الخرائط
    public final String bookingNote; // قيود الحجز إن وجدت
    public final String availableFrom; // yyyy-MM-dd أو null
    public final String availableTo;
    public final String source;

    public Experience(String id, String title, String cityId, String place, String about,
                      String duration, int adultPrice, int childPrice, String childNote,
                      boolean perVehicle, int moods, boolean family, int image, String mapQuery,
                      String bookingNote, String availableFrom, String availableTo, String source) {
        this.id = id;
        this.title = title;
        this.cityId = cityId;
        this.place = place;
        this.about = about;
        this.duration = duration;
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
        this.childNote = childNote;
        this.perVehicle = perVehicle;
        this.moods = moods;
        this.family = family;
        this.image = image;
        this.mapQuery = mapQuery;
        this.bookingNote = bookingNote;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.source = source;
    }

    public boolean hasMood(int mood) {
        return (moods & mood) != 0;
    }

    public String cityName() {
        return DemoData.cityName(cityId);
    }

    /** نص السعر المختصر للقوائم */
    public String priceLabel() {
        String p = Fmt.sar(adultPrice);
        return perVehicle ? p + " للسيارة" : p;
    }

    @Override
    public String toString() {
        return title;
    }
}
