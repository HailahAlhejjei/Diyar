package com.example.diar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.adapters.TripAdapter;
import com.example.diar.data.DemoData;
import com.example.diar.data.DiyarDb;
import com.example.diar.data.Experience;
import com.example.diar.data.Fmt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** مخطط الرحلة: تجارب مرتبة حسب التاريخ ومحفوظة في SQLite */
public class TripActivity extends AppCompatActivity {
    private final List<DiyarDb.TripItem> items = new ArrayList<>();
    private TripAdapter adapter;
    private DiyarDb db;
    private ListView list;
    private TextView empty, summary, summarySub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_screen);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.trip_title));
        Ui.hideBack(this);
        Ui.headerAction(this, "♡", v -> startActivity(new Intent(this, SavedActivity.class)));
        findViewById(R.id.bottom_nav).setVisibility(View.VISIBLE);
        BottomNav.setup(this, BottomNav.TRIPS);
        db = new DiyarDb(this);

        ViewGroup slot = findViewById(R.id.top_slot);
        View top = LayoutInflater.from(this).inflate(R.layout.top_trip, slot, true);
        summary = top.findViewById(R.id.trip_summary);
        summarySub = top.findViewById(R.id.trip_summary_sub);

        list = findViewById(R.id.list);
        list.setDivider(null);
        empty = findViewById(R.id.empty);
        empty.setText(R.string.trip_empty);
        adapter = new TripAdapter(this, items);
        list.setAdapter(adapter);
        registerForContextMenu(list);
        list.setOnItemClickListener((p, v, pos, id) -> BookingActivity.open(this, items.get(pos).expId));

        TextView add = findViewById(R.id.btn_bottom);
        add.setText(R.string.trip_add);
        add.setBackgroundResource(R.drawable.bg_btn_gold);
        add.setVisibility(View.VISIBLE);
        add.setOnClickListener(v -> chooseExperience());
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        items.clear();
        items.addAll(db.tripItems());
        adapter.notifyDataSetChanged();
        list.setVisibility(items.isEmpty() ? View.GONE : View.VISIBLE);
        empty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);

        Set<String> days = new LinkedHashSet<>();
        Set<String> cities = new LinkedHashSet<>();
        for (DiyarDb.TripItem t : items) {
            days.add(t.date);
            Experience e = DemoData.find(t.expId);
            if (e != null) cities.add(e.cityName());
        }
        if (items.isEmpty()) {
            summary.setText(R.string.trip_none);
            summarySub.setText(R.string.trip_none_sub);
        } else {
            summary.setText("التجارب: " + Fmt.num(items.size()) + " · الأيام: " + Fmt.num(days.size()));
            String from = items.get(0).date, to = items.get(items.size() - 1).date;
            summarySub.setText(String.join("، ", cities) + " · " + Fmt.shortDate(from)
                    + (from.equals(to) ? "" : " – " + Fmt.shortDate(to)));
        }
    }

    /** إضافة تجربة بدون حجز: اختيار التجربة ثم التاريخ */
    private void chooseExperience() {
        String[] names = new String[DemoData.EXPERIENCES.size()];
        for (int i = 0; i < names.length; i++) {
            Experience e = DemoData.EXPERIENCES.get(i);
            names[i] = e.title + " – " + e.cityName();
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.trip_add)
                .setItems(names, (d, which) -> chooseDate(DemoData.EXPERIENCES.get(which)))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void chooseDate(Experience e) {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(y, m, d);
            db.addTripItem(e.id, Fmt.iso(picked.getTime()), null, 0, null);
            Toast.makeText(this, R.string.trip_added, Toast.LENGTH_SHORT).show();
            load();
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_trip_item, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        DiyarDb.TripItem t = items.get(info.position);
        if (item.getItemId() == R.id.ctx_trip_map) {
            Experience e = DemoData.find(t.expId);
            if (e != null) Ui.openMap(this, e.mapQuery);
            return true;
        } else if (item.getItemId() == R.id.ctx_trip_delete) {
            db.deleteTripItem(t.id);
            load();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
