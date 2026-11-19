package com.example.diar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.data.DemoData;
import com.example.diar.data.DiyarDb;
import com.example.diar.data.Experience;
import com.example.diar.data.Fmt;

import java.util.Random;

/** الدفع (تجريبي — لا يتم خصم أي مبلغ) */
public class PaymentActivity extends AppCompatActivity {
    private Experience exp;
    private String date, period;
    private int adults, children, total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        Ui.insets(findViewById(R.id.root), true);
        Ui.header(this, getString(R.string.pay_title));

        Intent in = getIntent();
        exp = DemoData.find(in.getStringExtra("exp"));
        if (exp == null) {
            finish();
            return;
        }
        date = in.getStringExtra("date");
        period = in.getStringExtra("period");
        adults = in.getIntExtra("adults", 1);
        children = in.getIntExtra("children", 0);
        total = in.getIntExtra("total", 0);

        Ui.image(findViewById(R.id.pay_image), exp.image);
        ((TextView) findViewById(R.id.pay_title)).setText(exp.title);
        ((TextView) findViewById(R.id.pay_when)).setText(Fmt.longDate(date) + " · " + period);

        StringBuilder lines = new StringBuilder();
        lines.append(exp.perVehicle ? "سيارات" : "بالغون").append(" × ").append(Fmt.num(adults))
                .append("   ").append(Fmt.sar((long) adults * exp.adultPrice));
        if (children > 0) {
            lines.append("\nأطفال × ").append(Fmt.num(children))
                    .append("   ").append(Fmt.sar((long) children * exp.childPrice));
        }
        ((TextView) findViewById(R.id.pay_lines)).setText(lines);
        ((TextView) findViewById(R.id.pay_total)).setText(Fmt.sar(total));

        TextView pay = findViewById(R.id.btn_pay);
        pay.setText("ادفع " + Fmt.sar(total));
        pay.setOnClickListener(v -> confirm());
    }

    private void confirm() {
        RadioGroup methods = findViewById(R.id.method_group);
        String method = ((RadioButton) findViewById(methods.getCheckedRadioButtonId())).getText().toString();
        new AlertDialog.Builder(this)
                .setTitle(R.string.pay_confirm_title)
                .setMessage("دفع " + Fmt.sar(total) + " عبر " + method + "؟")
                .setPositiveButton(R.string.pay_yes, (d, w) -> pay(method))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void pay(String method) {
        DiyarDb.Booking b = new DiyarDb.Booking();
        b.code = "DIY-" + Integer.toHexString(0x10000 + new Random().nextInt(0xEFFFF)).toUpperCase();
        b.expId = exp.id;
        b.date = date;
        b.period = period;
        b.adults = adults;
        b.children = children;
        b.total = total;
        b.method = method;
        long id = new DiyarDb(this).addBooking(b);

        NotifyHelper.send(this, getString(R.string.notify_booked_title),
                exp.title + " · " + Fmt.longDate(date) + " · " + b.code);

        Intent i = new Intent(this, TicketActivity.class);
        i.putExtra("booking", id);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
