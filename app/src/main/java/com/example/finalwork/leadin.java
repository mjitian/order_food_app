package com.example.finalwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class leadin extends AppCompatActivity {


    private static final int SPLASH_DISPLAY_TIME = 2000; // 2秒钟的等待时间

    private ImageView splashImageView;//声明变量 用于绑定图片控件
    private Handler handler;//延时

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leadin);

        splashImageView = (ImageView) findViewById(R.id.splash_image_view);

        // 设置 ImageView 显示指定的图片
        Glide.with(this).load(R.drawable.leadin1).into(splashImageView);

        // 初始化 Handler，在延迟3秒钟后启动 MainActivity
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //跳转 从leadin to login
                Intent mainIntent = new Intent(leadin.this, login.class);
                startActivity(mainIntent);//启动意图的activity
                finish();//销毁当前activity pop stack
            }
        }, SPLASH_DISPLAY_TIME);
    }
}