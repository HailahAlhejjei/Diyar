package com.example.diar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.adapters.ExperienceAdapter;
import com.example.diar.data.DemoData;
import com.example.diar.data.DiyarDb;
import com.example.diar.data.Experience;
import com.example.diar.data.Fmt;
import com.example.diar.data.Prefs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** الرئيسية: وجهة الأسبوع + المدن + التجارب حسب مزاج الرحلة */
public class HomeActivity extends AppCompatActivity {
    private final List<Experience> shown = new ArrayList<>();
    private ExperienceAdapter adapter;
    private TextView moodCount, notices;
    private int mood = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        Ui.insets(findViewById(R.id.root), true);
        BottomNav.setup(this, BottomNav.HOME);
        NotifyHelper.askPermissionOnce(this);

        ListView list = findViewById(R.id.list);
        View header = LayoutInflater.from(this).inflate(R.layout.header_home, list, false);
        list.addHeaderView(header, null, false);
        adapter = new ExperienceAdapter(this, shown, null);
        list.setAdapter(adapter);

        moodCount = header.findViewById(R.id.mood_count);
        notices = header.findViewById(R.id.btn_notices);
        ((TextView) header.findViewById(R.id.greeting)).setText(greeting());

        ImageView[] cityImages = {header.findViewById(R.id.city_riyadh_img),
                header.findViewById(R.id.city_alula_img), header.findViewById(R.id.city_abha_img)};
        Ui.image(cityImages[0], DemoData.cityImage(DemoData.RIYADH));
        Ui.image(cityImages[1], DemoData.cityImage(DemoData.ALULA));
        Ui.image(cityImages[2], DemoData.cityImage(DemoData.ABHA));
        Ui.image(header.findViewById(R.id.hero_image), R.drawable.onb_1);
        header.findViewById(R.id.hero).setClipToOutline(true);

        header.findViewById(R.id.search_box).setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        header.findViewById(R.id.btn_saved).setOnClickListener(v -> startActivity(new Intent(this, SavedActivity.class)));
        notices.setOnClickListener(v -> startActivity(new Intent(this, NotificationsActivity.class)));
        header.findViewById(R.id.hero).setOnClickListener(v -> openCity(DemoData.ALULA));
        header.findViewById(R.id.city_riyadh).setOnClickListener(v -> openCity(DemoData.RIYADH));
        header.findViewById(R.id.city_alula).setOnClickListener(v -> openCity(DemoData.ALULA));
        header.findViewById(R.id.city_abha).setOnClickListener(v -> openCity(DemoData.ABHA));

        RadioGroup moods = header.findViewById(R.id.mood_group);
        moods.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.mood_adventure) mood = Experience.ADVENTURE;
            else if (checkedId == R.id.mood_relax) mood = Experience.RELAX;
            else if (checkedId == R.id.mood_culture) mood = Experience.CULTURE;
            else mood = 0;
            filter();
        });
        filter();

        list.setOnItemClickListener((parent, view, position, id) -> {
            Experience e = (Experience) parent.getItemAtPosition(position);
            BookingActivity.open(this, e.id);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int unseen = new DiyarDb(this).unseenCount();
        notices.setText(unseen > 0 ? "🔔" + Fmt.num(unseen) : "🔔");
    }

    private void filter() {
        shown.clear();
        for (Experience e : DemoData.EXPERIENCES) {
            if (mood == 0 || e.hasMood(mood)) shown.add(e);
        }
        adapter.notifyDataSetChanged();
        moodCount.setText(Fmt.num(shown.size()) + " تجربة");
    }

    private void openCity(String cityId) {
        Intent i = new Intent(this, DestinationActivity.class);
        i.putExtra("city", cityId);
        startActivity(i);
    }

    private String greeting() {
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String part = h < 12 ? "صباح الخير" : "مساء الخير";
        return part + "، " + Prefs.name(this);
    }
}
