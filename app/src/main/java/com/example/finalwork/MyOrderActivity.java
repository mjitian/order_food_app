package com.example.finalwork;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalwork.javabean.Order;
import com.example.finalwork.javabean.OrderAdapter;

import java.util.ArrayList;
import java.util.Comparator;

public class MyOrderActivity extends AppCompatActivity {

    private final ArrayList<Order> my_order = new ArrayList<Order>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        TextView order_back = findViewById(R.id.my_order_back);
        order_back.setOnClickListener(view -> finish());

        initMyOrder();
        ListView my_order_list = findViewById(R.id.order_list);
        my_order_list.setAdapter(new OrderAdapter(this, R.layout.acvitity_my_order_item,
                my_order));

    }

    private void initMyOrder() {
        // 获取用户名
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String username = preferences.getString("username", "");

        SQLiteDatabase sqLiteDatabase = new zMySqlHelper(this).getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("MyOrder",
                null, "username = ?", new String[]{username},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String uuid = cursor.getString(cursor.getColumnIndexOrThrow("uuid"));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("time"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("total_price"));
                my_order.add(new Order(uuid, price, timestamp));
            } while (cursor.moveToNext());
        }
        my_order.sort(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Long.compare(o2.getTimestamp(), o1.getTimestamp());
            }
        });
        cursor.close();
    }

}