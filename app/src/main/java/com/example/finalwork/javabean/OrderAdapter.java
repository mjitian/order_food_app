package com.example.finalwork.javabean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class OrderAdapter extends ArrayAdapter<Order> {

    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.acvitity_my_order_item, parent,
                false);
        TextView priceText = view.findViewById(R.id.order_price);
        TextView timestampText = view.findViewById(R.id.order_timestamp);
        Order order = getItem(position);
        if (order != null) {
            priceText.setText("订单金额：" + order.getTotal_price());
            timestampText.setText("订单时间：" + timestampToString(order.getTimestamp()));
        }
        return view;
    }

    // 时间戳转日期字符串
    public static String timestampToString(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }
}
