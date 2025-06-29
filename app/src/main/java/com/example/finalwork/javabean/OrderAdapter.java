package com.example.finalwork.javabean;

import static android.icu.text.DateFormat.getDateInstance;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.finalwork.R;
import com.example.finalwork.zMySqlHelper;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends ArrayAdapter<Order> {

    private static class ViewHolder {

        public TextView priceText;
        public TextView timestampText;
        public ViewHolder (TextView priceText, TextView timestampText) {
            this.priceText = priceText;
            this.timestampText = timestampText;
        }
    }

    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.acvitity_my_order_item,
                    parent,
                    false);
            TextView priceText = view.findViewById(R.id.order_price);
            TextView timestampText = view.findViewById(R.id.order_timestamp);
            viewHolder = new ViewHolder(priceText, timestampText);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        Order order = getItem(position);
        if (order != null) {
            viewHolder.priceText.setText(String.format(
                    getContext().getString(R.string.OrderPrice),
                    order.getTotal_price()
            ));
            viewHolder.timestampText.setText(String.format(
                    getContext().getString(R.string.OrderTimestamp),
                    timestampToString(order.getTimestamp())
            ));
        }
        return view;
    }

    // 时间戳转日期字符串
    public static String timestampToString(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
