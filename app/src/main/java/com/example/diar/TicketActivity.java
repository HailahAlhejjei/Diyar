package com.example.diar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.data.DemoData;
import com.example.diar.data.DiyarDb;
import com.example.diar.data.Experience;
import com.example.diar.data.Fmt;

/** التذكرة وتأكيد الحجز */
public class TicketActivity extends AppCompatActivity {
    private DiyarDb db;
    private DiyarDb.Booking b;
    private Experience exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.ticket_title));
        findViewById(R.id.btn_back).setOnClickListener(v -> goHome());

        db = new DiyarDb(this);
        b = db.booking(getIntent().getLongExtra("booking", -1));
        exp = b != null ? DemoData.find(b.expId) : null;
        if (b == null || exp == null) {
            finish();
            return;
        }

        String people = (exp.perVehicle ? "سيارات: " : "بالغون: ") + Fmt.num(b.adults)
                + (b.children > 0 ? " · أطفال: " + Fmt.num(b.children) : "");
        ((TextView) findViewById(R.id.t_brand)).setText("دِيار × " + exp.cityName());
        ((TextView) findViewById(R.id.t_title)).setText(exp.title);
        ((TextView) findViewById(R.id.t_date)).setText(Fmt.longDate(b.date) + "\n" + b.period);
        ((TextView) findViewById(R.id.t_people)).setText(people);
        ((TextView) findViewById(R.id.t_code)).setText("رمز الحجز: " + b.code);
        ((TextView) findViewById(R.id.t_paid)).setText("المبلغ: " + Fmt.sar(b.total) + " · " + b.method);

        TextView addTrip = findViewById(R.id.btn_add_trip);
        if (db.tripHasCode(b.code)) addTrip.setText(R.string.in_trip);
        addTrip.setOnClickListener(v -> {
            if (!db.tripHasCode(b.code)) {
                db.addTripItem(exp.id, b.date, b.period, b.adults + b.children, b.code);
                Toast.makeText(this, R.string.trip_added, Toast.LENGTH_SHORT).show();
            }
            Intent i = new Intent(this, TripActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });

        // مشاركة التذكرة بهدف مضمن ACTION_SEND
        findViewById(R.id.btn_share).setOnClickListener(v -> {
            Intent send = new Intent();
            send.setAction(Intent.ACTION_SEND);
            send.putExtra(Intent.EXTRA_TEXT, "حجزت عبر دِيار: " + exp.title + "\n"
                    + Fmt.longDate(b.date) + " · " + b.period + "\n" + people + "\nرمز الحجز: " + b.code);
            send.setType("text/plain");
            startActivity(Intent.createChooser(send, getString(R.string.share_ticket)));
        });
        findViewById(R.id.btn_home).setOnClickListener(v -> goHome());

        // زر الرجوع في الجهاز يرجع للرئيسية بدل شاشة الدفع
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goHome();
            }
        });
    }

    private void goHome() {
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
