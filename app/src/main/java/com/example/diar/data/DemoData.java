package com.example.diar.data;

import com.example.diar.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * المحتوى المحلي للتطبيق. كل سعر هنا تم البحث عنه في أكتوبر ٢٠٢٦،
 * والمصدر مكتوب مع كل تجربة. لا توجد أماكن أو أسعار وهمية.
 */
public final class DemoData {
    private DemoData() {}

    public static final String RIYADH = "riyadh";
    public static final String ALULA = "alula";
    public static final String ABHA = "abha";

    public static final List<Experience> EXPERIENCES = Arrays.asList(
            new Experience("sixflags", "Six Flags Qiddiya", RIYADH, "مدينة القدية، الرياض",
                    "مدينة ألعاب افتتحت في ٣١ ديسمبر ٢٠٢٥ ضمن مشروع القدية، وفيها أفعوانية Falcons Flight. تذكرة يوم كامل.",
                    "يوم كامل", 275, 170, "الأطفال ٤–١١ سنة، وأقل من ٤ سنوات مجاناً",
                    false, Experience.ADVENTURE, true, R.drawable.img_sixflags,
                    "Six Flags Qiddiya City", null, null, null,
                    "fanspass.sa · timeoutriyadh.com"),
            new Experience("blvd_world", "Boulevard World", RIYADH, "موسم الرياض، الرياض",
                    "منطقة ضمن موسم الرياض تضم أجنحة لثقافات دول مختلفة وبحيرة وتلفريك.",
                    "زيارة مسائية", 30, 0, "الأطفال ٥ سنوات وأقل مجاناً",
                    false, Experience.ADVENTURE, true, R.drawable.img_blvd_world,
                    "Boulevard World Riyadh", "متاحة خلال موسم الرياض فقط", null, null,
                    "houseofsaud.com"),
            new Experience("skybridge", "جسر السماء – برج المملكة", RIYADH, "برج المملكة، الرياض",
                    "جسر مشاة في أعلى برج المملكة يطل على مدينة الرياض.",
                    "حوالي ساعة", 138, 69, "الأطفال أقل من ١٠ سنوات",
                    false, Experience.ADVENTURE, true, R.drawable.img_skybridge,
                    "Sky Bridge Kingdom Centre Riyadh", null, null, null,
                    "visitsaudi.com"),
            new Experience("turaif", "حي الطريف", RIYADH, "الدرعية، الرياض",
                    "موقع مسجل في قائمة التراث العالمي لليونسكو. تذكرة الدخول بعد الساعة ٥ مساءً وتُحتسب رصيداً في مطاعم المنطقة.",
                    "زيارة مسائية", 50, 0, null,
                    false, Experience.CULTURE, true, R.drawable.img_turaif,
                    "At-Turaif Diriyah", "التذكرة للدخول بعد الساعة ٥ مساءً", null, null,
                    "diriyah.sa"),
            new Experience("hegra", "جولة الحِجر اليومية", ALULA, "الحِجر، العُلا",
                    "جولة بالحافلة مع مرشد في الحِجر، أول موقع سعودي يُسجّل في قائمة التراث العالمي. القيادة الذاتية داخل الموقع غير مسموحة.",
                    "حوالي ٣ ساعات", 95, 0, null,
                    false, Experience.CULTURE, false, R.drawable.img_hegra,
                    "Hegra AlUla", null, null, null,
                    "experiencealula.com"),
            new Experience("hegra_premium", "جولة الحِجر المميزة", ALULA, "الحِجر، العُلا",
                    "جولة خاصة بسيارة دفع رباعي داخل الحِجر. السعر للسيارة كاملة وتتسع حتى ٧ أشخاص.",
                    "جولة خاصة", 1000, 0, null,
                    true, Experience.CULTURE, true, R.drawable.img_hegra_premium,
                    "Hegra AlUla", "السيارة تتسع حتى ٧ أشخاص", null, null,
                    "experiencealula.com"),
            new Experience("hegra_wildlife", "جولة الحياة الفطرية في الحِجر", ALULA, "الحِجر، العُلا",
                    "جولة تركز على طبيعة الحِجر والحياة الفطرية فيها.",
                    "جولة مع مرشد", 150, 0, null,
                    false, Experience.RELAX, false, R.drawable.img_hegra_wildlife,
                    "Hegra AlUla", null, null, null,
                    "experiencealula.com"),
            new Experience("oldtown", "جولة البلدة القديمة", ALULA, "البلدة القديمة، العُلا",
                    "جولة مع مرشد في البلدة القديمة بالعُلا. دخول البلدة نفسها مجاني، والجولة المرشدة مدفوعة.",
                    "جولة مع مرشد", 70, 0, "الأطفال حتى ٤ سنوات مجاناً",
                    false, Experience.CULTURE, true, R.drawable.img_alula_oldtown,
                    "AlUla Old Town", null, null, null,
                    "experiencealula.com"),
            new Experience("heli", "جولة الهليكوبتر فوق العُلا", ALULA, "العُلا",
                    "رحلة بالهليكوبتر تمر فوق معالم العُلا مثل جبل الفيل والحِجر.",
                    "٣٠ دقيقة", 1099, 0, null,
                    false, Experience.ADVENTURE, false, R.drawable.img_heli,
                    "AlUla helicopter tour", null, null, null,
                    "desertnighttours.com · mysauditours.com"),
            new Experience("stargazing", "رصد النجوم في غراميل", ALULA, "غراميل، العُلا",
                    "جولة ليلية مع مرشد لرصد النجوم في منطقة غراميل، باللغتين العربية والإنجليزية.",
                    "٣–٤ ساعات", 397, 340, "الأطفال (العمر ٨ سنوات فأكثر)",
                    false, Experience.RELAX, false, R.drawable.img_stargazing,
                    "Gharameel AlUla", "العمر ٨ سنوات فأكثر، والحد الأدنى للمجموعة ٤ أشخاص", null, "2026-11-14",
                    "experiencealula.com"),
            new Experience("balloon", "منطاد AlUla Skyrise", ALULA, "العُلا",
                    "رحلة بالمنطاد فوق العُلا، وتشمل النقل إلى موقع الإقلاع ومرطبات خفيفة.",
                    "رحلة صباحية", 990, 0, null,
                    false, Experience.RELAX | Experience.ADVENTURE, false, R.drawable.img_balloon,
                    "AlUla Skyrise", "متاحة من ٦ إلى ١٥ نوفمبر ٢٠٢٦، والحجز قبل الساعة ٤ عصر اليوم السابق", "2026-11-06", "2026-11-15",
                    "experiencealula.com"),
            new Experience("soudah", "تلفريك السودة", ABHA, "السودة، عسير",
                    "رحلة بالتلفريك في جبال السودة قرب أبها.",
                    "رحلة ذهاب وعودة", 80, 40, "الأطفال",
                    false, Experience.RELAX, true, R.drawable.img_soudah,
                    "Al Soudah cable car Abha", null, null, null,
                    "houseofsaud.com"),
            new Experience("rijal", "قرية رجال ألمع", ABHA, "رجال ألمع، عسير",
                    "قرية تراثية في عسير فيها متحف داخل قصر آل علوان.",
                    "زيارة نهارية", 20, 0, null,
                    false, Experience.CULTURE, true, R.drawable.img_rijal,
                    "Rijal Almaa Heritage Village", null, null, null,
                    "laurewanders.com · houseofsaud.com")
    );

    public static final List<Service> SERVICES = Arrays.asList(
            new Service("مطعم Maiz", Service.RESTAURANT, "مطل البجيري، الدرعية",
                    "مطعم سعودي معاصر مستوحى من أطباق ١٣ منطقة في المملكة.",
                    R.drawable.img_maiz, "Maiz Bujairi Terrace Diriyah"),
            new Service("مطعم Takya", Service.RESTAURANT, "مطل البجيري، الدرعية",
                    "علامة سعودية من مطاعم مطل البجيري.",
                    R.drawable.img_takya, "Takya Bujairi Terrace Diriyah"),
            new Service("مواقف البجيري", Service.PARKING, "مطل البجيري، الدرعية",
                    "٣٠ ر.س لأول ٣ ساعات، و١٠ ر.س لكل ساعة إضافية. تتسع لأكثر من ١٤٠٠ سيارة.",
                    R.drawable.img_bujairi, "Bujairi Terrace parking Diriyah")
    );

    public static String cityName(String id) {
        switch (id) {
            case RIYADH: return "الرياض";
            case ALULA: return "العُلا";
            case ABHA: return "أبها";
            default: return "";
        }
    }

    public static String cityTagline(String id) {
        switch (id) {
            case RIYADH: return "العاصمة… بين التراث والترفيه";
            case ALULA: return "حيث يلتقي الزمن بالجمال";
            case ABHA: return "جبال عسير وقراها التراثية";
            default: return "";
        }
    }

    public static String cityAbout(String id) {
        switch (id) {
            case RIYADH: return "من حي الطريف المسجل في التراث العالمي إلى مدينة القدية الترفيهية، تجمع الرياض تجارب ثقافية ومغامرات عائلية.";
            case ALULA: return "تضم العُلا الحِجر، أول موقع سعودي في قائمة التراث العالمي، وبلدتها القديمة وسماءها الصافية لرصد النجوم.";
            case ABHA: return "في منطقة عسير جنوب المملكة، اشتهرت أبها بجبال السودة وقرية رجال ألمع التراثية.";
            default: return "";
        }
    }

    public static int cityImage(String id) {
        switch (id) {
            case RIYADH: return R.drawable.img_turaif;
            case ALULA: return R.drawable.onb_1;
            case ABHA: return R.drawable.img_soudah;
            default: return 0;
        }
    }

    public static Experience find(String id) {
        for (Experience e : EXPERIENCES) {
            if (e.id.equals(id)) return e;
        }
        return null;
    }

    public static List<Experience> byCity(String cityId) {
        List<Experience> out = new ArrayList<>();
        for (Experience e : EXPERIENCES) {
            if (e.cityId.equals(cityId)) out.add(e);
        }
        return out;
    }

    /** نص "عن دِيار": مصادر الأسعار والصور */
    public static final String CREDITS =
            "مصادر الأسعار (تم التحقق في أكتوبر ٢٠٢٦):\n"
            + "• Six Flags Qiddiya: fanspass.sa، timeoutriyadh.com\n"
            + "• Boulevard World: houseofsaud.com\n"
            + "• جسر السماء: visitsaudi.com\n"
            + "• حي الطريف ومطل البجيري: diriyah.sa\n"
            + "• تجارب العُلا: experiencealula.com\n"
            + "• الهليكوبتر: desertnighttours.com، mysauditours.com\n"
            + "• تلفريك السودة ورجال ألمع: houseofsaud.com، laurewanders.com\n\n"
            + "مصادر الصور:\n"
            + "• Wikimedia Commons (CC BY / CC BY-SA / CC0): وادي عشار، الدرعية، تلفريك أبها، Six Flags Qiddiya، الحِجر، المها العربي، البلدة القديمة، حي الطريف\n"
            + "• صور محمية بحقوق النشر ومستخدمة لأغراض المشروع الجامعي فقط: MICHELIN Guide (Maiz، Takya)، skybridge.sa، rezdy.com، mysauditours.com، tripadvisor.com، vecteezy.com، destinationksa.com، middleeastjourneys.com، matadornetwork.com، travelandleisureasia.com";
}
