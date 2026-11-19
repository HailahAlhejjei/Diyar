package com.example.diar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.adapters.ExperienceAdapter;
import com.example.diar.data.DemoData;
import com.example.diar.data.Experience;

import java.util.List;

/** تجارب مدينة معيّنة */
public class ExperiencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_screen);
        Ui.insets(findViewById(R.id.root), true);

        String city = getIntent().getStringExtra("city");
        if (city == null) city = DemoData.ALULA;
        final String cityId = city;
        Ui.header(this, "تجارب " + DemoData.cityName(city));

        TextView sub = findViewById(R.id.subtitle);
        sub.setText(R.string.experiences_sub);
        sub.setVisibility(View.VISIBLE);

        List<Experience> items = DemoData.byCity(city);
        ListView list = findViewById(R.id.list);
        list.setAdapter(new ExperienceAdapter(this, items, null));
        list.setOnItemClickListener((p, v, pos, id) -> BookingActivity.open(this, items.get(pos).id));

        TextView map = findViewById(R.id.btn_bottom);
        map.setText(R.string.show_on_map);
        map.setVisibility(View.VISIBLE);
        map.setOnClickListener(v -> {
            Intent i = new Intent(this, MapActivity.class);
            i.putExtra("city", cityId);
            startActivity(i);
        });
    }
}
