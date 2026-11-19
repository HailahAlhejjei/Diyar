package com.example.diar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diar.R;

/** صفحات البداية الثلاث لـ ViewPager2 */
public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.Page> {
    private final int[] images = {R.drawable.onb_1, R.drawable.onb_2, R.drawable.onb_3};
    private final int[] titles = {R.string.onb_title_1, R.string.onb_title_2, R.string.onb_title_3};
    private final int[] bodies = {R.string.onb_body_1, R.string.onb_body_2, R.string.onb_body_3};

    static class Page extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView title, body;

        Page(View v) {
            super(v);
            image = v.findViewById(R.id.onb_image);
            title = v.findViewById(R.id.onb_title);
            body = v.findViewById(R.id.onb_body);
        }
    }

    @NonNull
    @Override
    public Page onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_onboarding, parent, false);
        return new Page(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Page p, int position) {
        p.image.setImageResource(images[position]);
        p.title.setText(titles[position]);
        p.body.setText(bodies[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}
