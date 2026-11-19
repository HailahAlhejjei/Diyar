package com.example.diar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.diar.adapters.OnboardingAdapter;
import com.example.diar.data.Fmt;
import com.example.diar.data.Prefs;

/** الشاشة الرئيسية للتطبيق (LAUNCHER): ٣ صفحات تعريفية */
public class OnboardingActivity extends AppCompatActivity {
    private ViewPager2 pager;
    private TextView next, count;
    private View[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // إذا سجل المستخدم دخوله سابقاً يذهب مباشرة للرئيسية
        if (Prefs.isLoggedIn(this)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);

        findViewById(R.id.root).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        pager = findViewById(R.id.pager);
        next = findViewById(R.id.btn_next);
        count = findViewById(R.id.page_count);
        dots = new View[]{findViewById(R.id.dot1), findViewById(R.id.dot2), findViewById(R.id.dot3)};

        pager.setAdapter(new OnboardingAdapter());
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                showPage(position);
            }
        });
        showPage(0);

        next.setOnClickListener(v -> {
            int p = pager.getCurrentItem();
            if (p < 2) pager.setCurrentItem(p + 1);
            else openLogin();
        });
        findViewById(R.id.btn_skip).setOnClickListener(v -> openLogin());
    }

    private void showPage(int position) {
        float dp = getResources().getDisplayMetrics().density;
        for (int i = 0; i < dots.length; i++) {
            boolean on = i == position;
            dots[i].setBackgroundResource(on ? R.drawable.bg_dot_on : R.drawable.bg_dot_off);
            dots[i].getLayoutParams().width = (int) ((on ? 22 : 8) * dp);
            dots[i].requestLayout();
        }
        count.setText(Fmt.num(position + 1) + " من " + Fmt.num(3));
        next.setText(position == 2 ? R.string.start_journey : R.string.next);
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
