package com.example.diar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.adapters.ExperienceAdapter;
import com.example.diar.data.DemoData;
import com.example.diar.data.Experience;

import java.util.ArrayList;
import java.util.List;

/** الخريطة: الضغط على أي مكان يفتح تطبيق الخرائط في الجهاز (Intent.ACTION_VIEW) */
public class MapActivity extends AppCompatActivity {
    private final List<Experience> shown = new ArrayList<>();
    private ExperienceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_screen);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.map_title));

        TextView sub = findViewById(R.id.subtitle);
        sub.setText(R.string.map_hint);
        sub.setVisibility(View.VISIBLE);

        ViewGroup slot = findViewById(R.id.top_slot);
        View top = LayoutInflater.from(this).inflate(R.layout.top_cities, slot, true);
        RadioGroup group = top.findViewById(R.id.city_group);

        ListView list = findViewById(R.id.list);
        adapter = new ExperienceAdapter(this, shown, "⌖");
        list.setAdapter(adapter);
        list.setOnItemClickListener((p, v, pos, id) -> Ui.openMap(this, shown.get(pos).mapQuery));

        group.setOnCheckedChangeListener((g, checkedId) -> filter(checkedId));
        String city = getIntent().getStringExtra("city");
        if (DemoData.RIYADH.equals(city)) group.check(R.id.c_riyadh);
        else if (DemoData.ALULA.equals(city)) group.check(R.id.c_alula);
        else if (DemoData.ABHA.equals(city)) group.check(R.id.c_abha);
        filter(group.getCheckedRadioButtonId());
    }

    static String cityFor(int checkedId) {
        if (checkedId == R.id.c_riyadh) return DemoData.RIYADH;
        if (checkedId == R.id.c_alula) return DemoData.ALULA;
        if (checkedId == R.id.c_abha) return DemoData.ABHA;
        return null;
    }

    private void filter(int checkedId) {
        String city = cityFor(checkedId);
        shown.clear();
        for (Experience e : DemoData.EXPERIENCES) {
            if (city == null || e.cityId.equals(city)) shown.add(e);
        }
        adapter.notifyDataSetChanged();
    }
}
