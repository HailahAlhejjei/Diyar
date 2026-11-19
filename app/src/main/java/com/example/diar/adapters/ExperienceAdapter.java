package com.example.diar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diar.R;
import com.example.diar.Ui;
import com.example.diar.data.Experience;

import java.util.List;

/** محول خاص لسطر التجربة: صورة + عنوان + مدينة + سعر */
public class ExperienceAdapter extends ArrayAdapter<Experience> {
    private final Context context;
    private final List<Experience> items;
    private final String endText; // نص بديل للسعر مثل ♥ أو ⌖، أو null

    public ExperienceAdapter(Context context, List<Experience> items, String endText) {
        super(context, R.layout.item_experience, items);
        this.context = context;
        this.items = items;
        this.endText = endText;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_experience, parent, false);
        }
        Experience e = items.get(position);
        ImageView iv = view.findViewById(R.id.item_image);
        TextView title = view.findViewById(R.id.item_title);
        TextView sub = view.findViewById(R.id.item_sub);
        TextView price = view.findViewById(R.id.item_price);

        Ui.image(iv, e.image);
        title.setText(e.title);
        sub.setText(e.cityName() + " · " + e.duration);
        price.setText(endText != null ? endText : e.priceLabel());
        return view;
    }
}
