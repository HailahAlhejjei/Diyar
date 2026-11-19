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
import com.example.diar.data.Service;

import java.util.List;

/** محول الخدمات القريبة */
public class ServiceAdapter extends ArrayAdapter<Service> {
    private final Context context;
    private final List<Service> items;

    public ServiceAdapter(Context context, List<Service> items) {
        super(context, R.layout.item_experience, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_experience, parent, false);
        }
        Service s = items.get(position);
        Ui.image((ImageView) view.findViewById(R.id.item_image), s.image);
        ((TextView) view.findViewById(R.id.item_title)).setText(s.name);
        ((TextView) view.findViewById(R.id.item_sub)).setText(s.detail);
        ((TextView) view.findViewById(R.id.item_price)).setText("⌖");
        return view;
    }
}
