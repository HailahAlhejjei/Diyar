package com.example.diar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.data.DemoData;
import com.example.diar.data.Experience;
import com.example.diar.data.Fmt;

import java.util.List;

/** تفاصيل الوجهة (المدينة) */
public class DestinationActivity extends AppCompatActivity {
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_destination);
        Ui.insets(findViewById(R.id.root), false);
        BottomNav.setup(this, BottomNav.HOME);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        city = getIntent().getStringExtra("city");
        if (city == null) city = DemoData.ALULA;
        List<Experience> items = DemoData.byCity(city);

        Ui.image(findViewById(R.id.dest_image), DemoData.cityImage(city));
        ((TextView) findViewById(R.id.dest_name)).setText(DemoData.cityName(city));
        ((TextView) findViewById(R.id.dest_tagline)).setText(DemoData.cityTagline(city));
        ((TextView) findViewById(R.id.dest_about)).setText(DemoData.cityAbout(city));
        ((TextView) findViewById(R.id.dest_count)).setText(Fmt.num(items.size()) + " تجارب قابلة للحجز");

        bindPick(items, 0, R.id.pick1, R.id.pick1_img, R.id.pick1_title, R.id.pick1_price);
        bindPick(items, 1, R.id.pick2, R.id.pick2_img, R.id.pick2_title, R.id.pick2_price);

        findViewById(R.id.tab_experiences).setOnClickListener(v -> {
            Intent i = new Intent(this, ExperiencesActivity.class);
            i.putExtra("city", city);
            startActivity(i);
        });
        findViewById(R.id.tab_nearby).setOnClickListener(v -> {
            if (DemoData.RIYADH.equals(city)) {
                startActivity(new Intent(this, NearbyActivity.class));
            } else {
                Toast.makeText(this, R.string.nearby_only_riyadh, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_map).setOnClickListener(v -> {
            Intent i = new Intent(this, MapActivity.class);
            i.putExtra("city", city);
            startActivity(i);
        });
    }

    private void bindPick(List<Experience> items, int index, int box, int img, int title, int price) {
        View card = findViewById(box);
        if (index >= items.size()) {
            card.setVisibility(View.INVISIBLE);
            return;
        }
        Experience e = items.get(index);
        Ui.image((ImageView) findViewById(img), e.image);
        ((TextView) findViewById(title)).setText(e.title);
        ((TextView) findViewById(price)).setText(e.priceLabel());
        card.setOnClickListener(v -> BookingActivity.open(this, e.id));
    }
}
