package com.example.diar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.diar.data.DemoData;
import com.example.diar.data.Experience;
import com.example.diar.data.Fmt;

import java.util.Calendar;
import java.util.Date;

/** تفاصيل التجربة + اختيار الموعد والعدد */
public class BookingActivity extends AppCompatActivity {
    private Experience exp;
    private int adults = 1, children = 0;
    private String date; // yyyy-MM-dd
    private TextView adultCount, childCount, total, dateView;

    public static void open(Activity from, String expId) {
        Intent i = new Intent(from, BookingActivity.class);
        i.putExtra("exp", expId);
        from.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking);
        Ui.insets(findViewById(R.id.root), false);
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        exp = DemoData.find(getIntent().getStringExtra("exp"));
        if (exp == null) {
            finish();
            return;
        }
        // استرجاع الحالة بعد تدوير الشاشة
        if (savedInstanceState != null) {
            adults = savedInstanceState.getInt("adults", 1);
            children = savedInstanceState.getInt("children", 0);
            date = savedInstanceState.getString("date");
        }

        Ui.image(findViewById(R.id.exp_image), exp.image);
        Ui.heart(this, findViewById(R.id.btn_heart), exp.id);
        ((TextView) findViewById(R.id.exp_title)).setText(exp.title);
        ((TextView) findViewById(R.id.exp_price)).setText("من " + exp.priceLabel() + (exp.perVehicle ? "" : " للشخص"));
        ((TextView) findViewById(R.id.exp_place)).setText(exp.place);
        ((TextView) findViewById(R.id.exp_duration)).setText(exp.duration);
        ((TextView) findViewById(R.id.exp_about)).setText(exp.about);
        ((TextView) findViewById(R.id.exp_source)).setText("مصدر السعر: " + exp.source);
        TextView note = findViewById(R.id.exp_note);
        if (exp.bookingNote != null) {
            note.setText("ⓘ " + exp.bookingNote);
            note.setVisibility(View.VISIBLE);
        }
        findViewById(R.id.btn_open_map).setOnClickListener(v -> Ui.openMap(this, exp.mapQuery));

        // العدد
        adultCount = findViewById(R.id.adult_count);
        childCount = findViewById(R.id.child_count);
        total = findViewById(R.id.tv_total);
        ((TextView) findViewById(R.id.adult_label)).setText(exp.perVehicle ? R.string.vehicles : R.string.adults);
        ((TextView) findViewById(R.id.adult_price)).setText(exp.priceLabel());
        findViewById(R.id.adult_minus).setOnClickListener(v -> { if (adults > 1) adults--; refresh(); });
        findViewById(R.id.adult_plus).setOnClickListener(v -> { if (adults < 10) adults++; refresh(); });

        TextView childNote = findViewById(R.id.child_note);
        if (exp.childPrice > 0) {
            ((TextView) findViewById(R.id.child_price)).setText(Fmt.sar(exp.childPrice));
            findViewById(R.id.child_minus).setOnClickListener(v -> { if (children > 0) children--; refresh(); });
            findViewById(R.id.child_plus).setOnClickListener(v -> { if (children < 10) children++; refresh(); });
        } else {
            findViewById(R.id.child_row).setVisibility(View.GONE);
            children = 0;
        }
        if (exp.childNote != null) childNote.setText(exp.childNote);
        else childNote.setVisibility(View.GONE);

        // التاريخ
        dateView = findViewById(R.id.tv_date);
        dateView.setOnClickListener(v -> pickDate());

        findViewById(R.id.btn_next).setOnClickListener(v -> next());
        refresh();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("adults", adults);
        outState.putInt("children", children);
        outState.putString("date", date);
    }

    private int totalPrice() {
        return adults * exp.adultPrice + children * exp.childPrice;
    }

    private void refresh() {
        adultCount.setText(Fmt.num(adults));
        childCount.setText(Fmt.num(children));
        total.setText(Fmt.sar(totalPrice()));
        if (date != null) {
            dateView.setText(Fmt.longDate(date));
            dateView.setTextColor(ContextCompat.getColor(this, R.color.ink));
        }
    }

    private void pickDate() {
        Calendar start = Calendar.getInstance();
        long min = start.getTimeInMillis() - 1000;
        Date from = exp.availableFrom != null ? Fmt.parse(exp.availableFrom) : null;
        if (from != null && from.getTime() > min) {
            min = from.getTime();
            start.setTime(from);
        }
        DatePickerDialog dialog = new DatePickerDialog(this, (view, y, m, d) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(y, m, d);
            date = Fmt.iso(picked.getTime());
            refresh();
        }, start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(min);
        Date to = exp.availableTo != null ? Fmt.parse(exp.availableTo) : null;
        if (to != null) {
            if (to.getTime() < min) {
                Toast.makeText(this, R.string.not_available, Toast.LENGTH_LONG).show();
                return;
            }
            dialog.getDatePicker().setMaxDate(to.getTime());
        }
        dialog.show();
    }

    private void next() {
        if (date == null) {
            Toast.makeText(this, R.string.err_date, Toast.LENGTH_SHORT).show();
            pickDate();
            return;
        }
        RadioGroup periods = findViewById(R.id.period_group);
        RadioButton period = findViewById(periods.getCheckedRadioButtonId());

        Intent i = new Intent(this, PaymentActivity.class);
        i.putExtra("exp", exp.id);
        i.putExtra("date", date);
        i.putExtra("period", period.getText().toString());
        i.putExtra("adults", adults);
        i.putExtra("children", children);
        i.putExtra("total", totalPrice());
        startActivity(i);
    }
}
