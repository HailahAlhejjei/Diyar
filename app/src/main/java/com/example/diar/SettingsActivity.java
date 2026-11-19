package com.example.diar;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.data.DemoData;
import com.example.diar.data.DiyarDb;
import com.example.diar.data.Prefs;

/** الإعدادات */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.settings_title));

        CompoundButton notify = findViewById(R.id.sw_notify);
        notify.setChecked(Prefs.notificationsOn(this));
        notify.setOnCheckedChangeListener((b, on) -> {
            Prefs.setNotificationsOn(this, on);
            if (on) NotifyHelper.askPermission(this);
        });

        findViewById(R.id.row_data).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(R.string.manage_data)
                .setMessage(R.string.clear_data_msg)
                .setPositiveButton(R.string.clear_data, (d, w) -> {
                    new DiyarDb(this).clearAll();
                    Toast.makeText(this, R.string.data_cleared, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show());

        findViewById(R.id.row_help).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(R.string.help)
                .setMessage(R.string.help_msg)
                .setPositiveButton(R.string.ok, null)
                .show());

        findViewById(R.id.row_about).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(R.string.about)
                .setMessage(DemoData.CREDITS)
                .setPositiveButton(R.string.ok, null)
                .show());
    }
}
