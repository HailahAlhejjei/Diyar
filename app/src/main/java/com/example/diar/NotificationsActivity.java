package com.example.diar;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.adapters.NoticeAdapter;
import com.example.diar.data.DiyarDb;

import java.util.ArrayList;
import java.util.List;

/** التنبيهات المحفوظة في SQLite */
public class NotificationsActivity extends AppCompatActivity {
    private final List<DiyarDb.Notice> items = new ArrayList<>();
    private NoticeAdapter adapter;
    private DiyarDb db;
    private ListView list;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_screen);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.notices_title));
        db = new DiyarDb(this);

        Ui.headerAction(this, getString(R.string.mark_all_read), v -> {
            db.markAllSeen();
            load();
        }).setTextSize(11);

        list = findViewById(R.id.list);
        empty = findViewById(R.id.empty);
        empty.setText(R.string.notices_empty);
        adapter = new NoticeAdapter(this, items);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        items.clear();
        items.addAll(db.notices());
        adapter.notifyDataSetChanged();
        list.setVisibility(items.isEmpty() ? View.GONE : View.VISIBLE);
        empty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
