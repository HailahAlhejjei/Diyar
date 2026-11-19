package com.example.diar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diar.data.Prefs;

/** الدخول برقم الجوال (بدون تحقق فعلي) أو كضيف */
public class LoginActivity extends AppCompatActivity {
    private View phoneForm;
    private EditText name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        Ui.insets(findViewById(R.id.root), true);

        phoneForm = findViewById(R.id.phone_form);
        name = findViewById(R.id.et_name);
        phone = findViewById(R.id.et_phone);
        TextView phoneBtn = findViewById(R.id.btn_phone);

        phoneBtn.setOnClickListener(v -> {
            if (phoneForm.getVisibility() != View.VISIBLE) {
                phoneForm.setVisibility(View.VISIBLE);
                phoneBtn.setText(R.string.confirm);
                name.requestFocus();
                return;
            }
            String n = name.getText().toString().trim();
            String p = phone.getText().toString().trim();
            if (n.isEmpty()) {
                name.setError(getString(R.string.err_name));
                return;
            }
            // رقم سعودي: 10 أرقام ويبدأ بـ 05
            if (!p.matches("05\\d{8}")) {
                phone.setError(getString(R.string.err_phone));
                return;
            }
            Prefs.login(this, n, p, false);
            openHome();
        });

        findViewById(R.id.btn_guest).setOnClickListener(v -> {
            Prefs.login(this, getString(R.string.guest), "", true);
            openHome();
        });
    }

    private void openHome() {
        Toast.makeText(this, getString(R.string.welcome, Prefs.name(this)), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
