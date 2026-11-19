package com.example.diar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.data.DiyarDb;
import com.example.diar.data.Fmt;
import com.example.diar.data.Prefs;

/** الملف الشخصي */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.profile_title));
        BottomNav.setup(this, BottomNav.PROFILE);

        // زر الرجوع يصير زر الإعدادات، والزر الثاني للتنبيهات
        TextView settings = findViewById(R.id.btn_back);
        settings.setText("⚙");
        settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        Ui.headerAction(this, "🔔", v -> startActivity(new Intent(this, NotificationsActivity.class)));

        findViewById(R.id.row_trip).setOnClickListener(v -> startActivity(new Intent(this, TripActivity.class)));
        findViewById(R.id.stat_bookings).setOnClickListener(v -> startActivity(new Intent(this, TripActivity.class)));
        findViewById(R.id.row_saved).setOnClickListener(v -> startActivity(new Intent(this, SavedActivity.class)));
        findViewById(R.id.stat_saved).setOnClickListener(v -> startActivity(new Intent(this, SavedActivity.class)));
        findViewById(R.id.row_name).setOnClickListener(v -> editName());
        findViewById(R.id.row_logout).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle(R.string.row_logout)
                .setMessage(R.string.logout_msg)
                .setPositiveButton(R.string.row_logout, (d, w) -> {
                    Prefs.logout(this);
                    Intent i = new Intent(this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                })
                .setNegativeButton(R.string.cancel, null)
                .show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        DiyarDb db = new DiyarDb(this);
        String name = Prefs.name(this);
        ((TextView) findViewById(R.id.avatar)).setText(name.isEmpty() ? "؟" : name.substring(0, 1));
        ((TextView) findViewById(R.id.p_name)).setText(name);
        ((TextView) findViewById(R.id.p_sub)).setText(Prefs.isGuest(this)
                ? getString(R.string.guest_account) : Prefs.phone(this));
        ((TextView) findViewById(R.id.stat_saved_n)).setText(Fmt.num(db.savedIds().size()));
        ((TextView) findViewById(R.id.stat_bookings_n)).setText(Fmt.num(db.bookingCount()));
    }

    private void editName() {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        input.setText(Prefs.name(this));
        new AlertDialog.Builder(this)
                .setTitle(R.string.row_name)
                .setView(input)
                .setPositiveButton(R.string.save, (d, w) -> {
                    String n = input.getText().toString().trim();
                    if (!n.isEmpty()) Prefs.setName(this, n);
                    onResume();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
