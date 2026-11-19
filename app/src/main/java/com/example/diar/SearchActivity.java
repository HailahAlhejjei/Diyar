package com.example.diar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.adapters.ExperienceAdapter;
import com.example.diar.data.DemoData;
import com.example.diar.data.Experience;
import com.example.diar.data.Fmt;

import java.util.ArrayList;
import java.util.List;

/** البحث أثناء الكتابة + الفلاتر */
public class SearchActivity extends AppCompatActivity {
    private final List<Experience> shown = new ArrayList<>();
    private ExperienceAdapter adapter;
    private EditText search;
    private CheckBox family, riyadh, alula, abha, cheap;
    private TextView count;
    private ListView list;
    private View empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.search_title));
        Ui.hideBack(this);
        BottomNav.setup(this, BottomNav.SEARCH);

        search = findViewById(R.id.et_search);
        family = findViewById(R.id.f_family);
        riyadh = findViewById(R.id.f_riyadh);
        alula = findViewById(R.id.f_alula);
        abha = findViewById(R.id.f_abha);
        cheap = findViewById(R.id.f_cheap);
        count = findViewById(R.id.result_count);
        list = findViewById(R.id.list);
        empty = findViewById(R.id.empty);

        adapter = new ExperienceAdapter(this, shown, null);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> BookingActivity.open(this, shown.get(position).id));

        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { filter(); }
        });
        for (CheckBox cb : new CheckBox[]{family, riyadh, alula, abha, cheap}) {
            cb.setOnCheckedChangeListener((button, checked) -> filter());
        }
        filter();
    }

    /** يحوّل النص لصيغة موحدة: يشيل التشكيل ويوحّد الألف والتاء المربوطة */
    private static String norm(String s) {
        return s.toLowerCase()
                .replaceAll("[\\u064B-\\u0652\\u0640]", "")
                .replaceAll("[أإآ]", "ا")
                .replace('ة', 'ه')
                .replace('ى', 'ي')
                .trim();
    }

    private void filter() {
        String q = norm(search.getText().toString());
        boolean anyCity = riyadh.isChecked() || alula.isChecked() || abha.isChecked();
        shown.clear();
        for (Experience e : DemoData.EXPERIENCES) {
            if (!q.isEmpty()) {
                String hay = norm(e.title + " " + e.place + " " + e.cityName() + " " + e.about + " " + e.mapQuery);
                boolean all = true;
                for (String word : q.split("\\s+")) {
                    if (!hay.contains(word)) { all = false; break; }
                }
                if (!all) continue;
            }
            if (family.isChecked() && !e.family) continue;
            if (cheap.isChecked() && e.adultPrice >= 100) continue;
            if (anyCity) {
                boolean match = (riyadh.isChecked() && e.cityId.equals(DemoData.RIYADH))
                        || (alula.isChecked() && e.cityId.equals(DemoData.ALULA))
                        || (abha.isChecked() && e.cityId.equals(DemoData.ABHA));
                if (!match) continue;
            }
            shown.add(e);
        }
        adapter.notifyDataSetChanged();
        count.setText(Fmt.num(shown.size()) + " نتيجة");
        list.setVisibility(shown.isEmpty() ? View.GONE : View.VISIBLE);
        empty.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
