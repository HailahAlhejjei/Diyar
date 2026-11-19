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
import com.example.diar.data.DiyarDb;
import com.example.diar.data.Fmt;

import java.util.List;

/** محول التنبيهات: نقطة ذهبية للجديد ورمادية للمقروء */
public class NoticeAdapter extends ArrayAdapter<DiyarDb.Notice> {
    private final Context context;
    private final List<DiyarDb.Notice> items;

    public NoticeAdapter(Context context, List<DiyarDb.Notice> items) {
        super(context, R.layout.item_notice, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_notice, parent, false);
        }
        DiyarDb.Notice n = items.get(position);
        view.findViewById(R.id.notice_dot).setBackgroundResource(n.seen ? R.drawable.bg_dot_grey : R.drawable.bg_dot_gold);
        ((TextView) view.findViewById(R.id.notice_title)).setText(n.title);
        ((TextView) view.findViewById(R.id.notice_body)).setText(n.body);
        ((TextView) view.findViewById(R.id.notice_time)).setText(Fmt.dateTime(n.created));
        return view;
    }
}
