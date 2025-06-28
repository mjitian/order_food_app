package com.example.finalwork;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 滚动视图初始化
        setupHorizontalScrollView();

        // 视频初始化
        setupVideoView(R.id.video1, R.raw.food1);
        setupVideoView(R.id.video2, R.raw.food2);
        setupVideoView(R.id.video3, R.raw.food3);

        // 推荐图片设置
        setupImageView(R.id.p1_png1_1, R.drawable.main101);
        setupImageView(R.id.p1_png1_2, R.drawable.main102);
        setupImageView(R.id.p1_png2_1, R.drawable.main201);
        setupImageView(R.id.p1_png2_2, R.drawable.main202);
        setupImageView(R.id.p1_png3_1, R.drawable.main301);
        setupImageView(R.id.p1_png3_2, R.drawable.main302);
        setupImageView(R.id.p1_png4_1, R.drawable.main401);
        setupImageView(R.id.p1_png4_2, R.drawable.main402);

        // 对应文字初始化
        TextView t1_1 = findViewById(R.id.p1_tx1_1);
        TextView t1_2 = findViewById(R.id.p1_tx1_2);
        TextView t2_1 = findViewById(R.id.p1_tx2_1);
        TextView t2_2 = findViewById(R.id.p1_tx2_2);
        TextView t3_1 = findViewById(R.id.p1_tx3_1);
        TextView t3_2 = findViewById(R.id.p1_tx3_2);
        TextView t4_1 = findViewById(R.id.p1_tx4_1);
        TextView t4_2 = findViewById(R.id.p1_tx4_2);

        // 获取底部导航栏的其他三个页面接口
        TextView p2 = findViewById(R.id.below_2);
        TextView p3 = findViewById(R.id.below_3);
        TextView p4 = findViewById(R.id.below_4);

        // 监听 - 店铺跳转
        setupShopClickListener(R.id.p1_png1_1, page2_noodles.class);
        setupShopClickListener(R.id.p1_png1_2, page2_bigstall.class);
        setupShopClickListener(R.id.p1_png2_1, page2_chicken.class);
        setupShopClickListener(R.id.p1_png2_2, page2_midfood.class);
        setupShopClickListener(R.id.p1_png3_1, page2_Italian.class);
        setupShopClickListener(R.id.p1_png3_2, page2_juicetea.class);
        setupShopClickListener(R.id.p1_png4_1, page2_pizza.class);
        setupShopClickListener(R.id.p1_png4_2, page2_northwest.class);

        setupShopClickListener(t1_1, page2_noodles.class);
        setupShopClickListener(t1_2, page2_bigstall.class);
        setupShopClickListener(t2_1, page2_chicken.class);
        setupShopClickListener(t2_2, page2_midfood.class);
        setupShopClickListener(t3_1, page2_Italian.class);
        setupShopClickListener(t3_2, page2_juicetea.class);
        setupShopClickListener(t4_1, page2_pizza.class);
        setupShopClickListener(t4_2, page2_northwest.class);

        // 监听 - 导航栏跳转
        setupNavigationClickListener(p2, page2.class);
        setupNavigationClickListener(p3, () -> checkCartAndNavigate());
        setupNavigationClickListener(p4, page4.class);
    }

    private void setupHorizontalScrollView() {
        HorizontalScrollView horizontalScrollView = findViewById(R.id.scroll);
        horizontalScrollView.post(() -> horizontalScrollView.scrollTo(0, 0));
    }

    private void setupVideoView(int videoViewId, int videoResourceId) {
        VideoView videoView = findViewById(videoViewId);
        String videoPath = "android.resource://" + getPackageName() + "/" + videoResourceId;
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.setOnPreparedListener(mp -> mp.seekTo(0));
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }

    private void setupImageView(int imageViewId, int drawableResourceId) {
        ImageView imageView = findViewById(imageViewId);
        imageView.setImageResource(drawableResourceId);
    }

    private void setupShopClickListener(int viewId, Class<?> targetActivity) {
        findViewById(viewId).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, targetActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void setupShopClickListener(TextView textView, Class<?> targetActivity) {
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, targetActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void setupNavigationClickListener(TextView textView, Class<?> targetActivity) {
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, targetActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void setupNavigationClickListener(TextView textView, Runnable action) {
        textView.setOnClickListener(view -> action.run());
    }

    private void checkCartAndNavigate() {
        sqLiteDatabase = openOrCreateDatabase("MYsqlite.db", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("create table if not exists cart(foodname varchar(32),foodprice varchar(32),quantity varchar(32))");
        Log.e("1", "先执行这里的建表cart，否则下面的query一直报错没有建表");
        Cursor cursor = sqLiteDatabase.query("cart", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String foodname = null;
                String foodprice = null;
                int quantity = 0;
                try {
                    foodname = cursor.getString(cursor.getColumnIndexOrThrow("foodname"));
                    foodprice = cursor.getString(cursor.getColumnIndexOrThrow("foodprice"));
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "getColumnIndexOrThrow error: " + e.getMessage());
                }
                if (quantity != 0) {
                    Intent to_p3cart = new Intent(MainActivity.this, page3_Cart.class);
                    to_p3cart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(to_p3cart);
                    cursor.close();
                    sqLiteDatabase.close();
                    return;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        Intent to_p3 = new Intent(MainActivity.this, page3.class);
        to_p3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(to_p3);
    }
}