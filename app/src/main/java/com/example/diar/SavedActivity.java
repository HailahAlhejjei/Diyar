package com.example.diar;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.adapters.ExperienceAdapter;
import com.example.diar.data.DemoData;
import com.example.diar.data.DiyarDb;
import com.example.diar.data.Experience;
import com.example.diar.data.Fmt;

import java.util.ArrayList;
import java.util.List;

/** الأماكن المحفوظة من SQLite. الضغط المطول يفتح قائمة سياق للإزالة */
public class SavedActivity extends AppCompatActivity {
    private final List<Experience> shown = new ArrayList<>();
    private ExperienceAdapter adapter;
    private DiyarDb db;
    private RadioGroup group;
    private ListView list;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_screen);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.saved_title));
        findViewById(R.id.bottom_nav).setVisibility(View.VISIBLE);
        BottomNav.setup(this, BottomNav.TRIPS);
        db = new DiyarDb(this);

        ViewGroup slot = findViewById(R.id.top_slot);
        View top = LayoutInflater.from(this).inflate(R.layout.top_cities, slot, true);
        group = top.findViewById(R.id.city_group);
        group.setOnCheckedChangeListener((g, id) -> load());

        list = findViewById(R.id.list);
        empty = findViewById(R.id.empty);
        empty.setText(R.string.saved_empty);
        adapter = new ExperienceAdapter(this, shown, "♥");
        list.setAdapter(adapter);
        list.setOnItemClickListener((p, v, pos, id) -> BookingActivity.open(this, shown.get(pos).id));
        registerForContextMenu(list);

        TextView sub = findViewById(R.id.subtitle);
        sub.setText(R.string.saved_hint);
        sub.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        String city = MapActivity.cityFor(group.getCheckedRadioButtonId());
        shown.clear();
        for (String id : db.savedIds()) {
            Experience e = DemoData.find(id);
            if (e != null && (city == null || e.cityId.equals(city))) shown.add(e);
        }
        adapter.notifyDataSetChanged();
        list.setVisibility(shown.isEmpty() ? View.GONE : View.VISIBLE);
        empty.setVisibility(shown.isEmpty() ? View.VISIBLE : View.GONE);
        ((TextView) findViewById(R.id.header_title)).setText(
                getString(R.string.saved_title) + " (" + Fmt.num(db.savedIds().size()) + ")");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_saved_item, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Experience e = shown.get(info.position);
        int id = item.getItemId();
        if (id == R.id.ctx_book) {
            BookingActivity.open(this, e.id);
            return true;
        } else if (id == R.id.ctx_map) {
            Ui.openMap(this, e.mapQuery);
            return true;
        } else if (id == R.id.ctx_remove) {
            db.toggleSaved(e.id);
            Toast.makeText(this, R.string.saved_removed, Toast.LENGTH_SHORT).show();
            load();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
