package com.example.diar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diar.R;
import com.example.diar.data.DemoData;
import com.example.diar.data.DiyarDb;
import com.example.diar.data.Experience;
import com.example.diar.data.Fmt;

import java.util.List;

/** محول مخطط الرحلة: خط زمني بالتاريخ والفترة */
public class TripAdapter extends ArrayAdapter<DiyarDb.TripItem> {
    private final Context context;
    private final List<DiyarDb.TripItem> items;

    public TripAdapter(Context context, List<DiyarDb.TripItem> items) {
        super(context, R.layout.item_trip, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_trip, parent, false);
        }
        DiyarDb.TripItem t = items.get(position);
        Experience e = DemoData.find(t.expId);
        String title = e != null ? e.title : t.expId;
        String when = Fmt.longDate(t.date) + (t.period != null ? " · " + t.period : "");
        String extra = (e != null ? e.cityName() : "")
                + (t.people > 0 ? " · الأشخاص: " + Fmt.num(t.people) : "")
                + (t.code != null ? " · حجز مؤكد " + t.code : "");
        ((TextView) view.findViewById(R.id.trip_when)).setText(when);
        ((TextView) view.findViewById(R.id.trip_title)).setText(title);
        ((TextView) view.findViewById(R.id.trip_extra)).setText(extra);
        return view;
    }
}
