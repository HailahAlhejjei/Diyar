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

import com.example.diar.adapters.ServiceAdapter;
import com.example.diar.data.DemoData;
import com.example.diar.data.Service;

import java.util.ArrayList;
import java.util.List;

/** الخدمات القريبة من حي الطريف (معلومات فقط) */
public class NearbyActivity extends AppCompatActivity {
    private final List<Service> shown = new ArrayList<>();
    private ServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_screen);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.nearby_title));

        ViewGroup slot = findViewById(R.id.top_slot);
        View top = LayoutInflater.from(this).inflate(R.layout.top_nearby, slot, true);
        Ui.image(top.findViewById(R.id.nearby_image), R.drawable.img_bujairi);
        top.findViewById(R.id.nearby_hero).setClipToOutline(true);

        ListView list = findViewById(R.id.list);
        adapter = new ServiceAdapter(this, shown);
        list.setAdapter(adapter);
        list.setOnItemClickListener((p, v, pos, id) -> Ui.openMap(this, shown.get(pos).mapQuery));

        RadioGroup group = top.findViewById(R.id.service_group);
        group.setOnCheckedChangeListener((g, checkedId) -> filter(checkedId));
        filter(R.id.s_all);

        TextView sub = findViewById(R.id.subtitle);
        sub.setText(R.string.nearby_hint);
        sub.setVisibility(View.VISIBLE);
    }

    private void filter(int checkedId) {
        shown.clear();
        for (Service s : DemoData.SERVICES) {
            if (checkedId == R.id.s_food && !Service.RESTAURANT.equals(s.type)) continue;
            if (checkedId == R.id.s_parking && !Service.PARKING.equals(s.type)) continue;
            shown.add(s);
        }
        adapter.notifyDataSetChanged();
    }
}
