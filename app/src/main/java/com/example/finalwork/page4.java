package com.example.finalwork;

import static android.graphics.Bitmap.CompressFormat.PNG;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class page4 extends AppCompatActivity {

    private zMySqlHelper mySqlHelper;
    private SQLiteDatabase sqLiteDatabase;

    private static final int REQUEST_CODE_SELECT_IMAGE = 1;

    private ImageView avatarImageView;

    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4);

        //显示账号按钮
        TextView showname = findViewById(R.id.show_username);
        //中间各项设置按钮
        TextView m1 = findViewById(R.id.p4_1);
        TextView m21 = findViewById(R.id.p4_2_1);
        TextView m22 = findViewById(R.id.p4_2_2);
        TextView m23 = findViewById(R.id.p4_2_3);
        TextView m24 = findViewById(R.id.p4_2_4);
        TextView m31 = findViewById(R.id.p4_3_1);
        TextView m32 = findViewById(R.id.p4_3_2);
        TextView m33 = findViewById(R.id.p4_3_3);
        TextView m34 = findViewById(R.id.p4_3_4);
        TextView m4 = findViewById(R.id.p4_4);
        //退出登录按钮
        TextView exit = findViewById(R.id.p4_exit);
        //注销登录按钮
        TextView logout = findViewById(R.id.p4_logout);
        //获取底部导航栏的其他三个页面接口
        TextView p1 = findViewById(R.id.below_1);
        TextView p2 = findViewById(R.id.below_2);
        TextView p3 = findViewById(R.id.below_3);

        //获取当前登录的账号username
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        username = preferences.getString("username", "");

        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        Bitmap bitmap = readImage(this, username);
        if (bitmap != null) {
            avatarImageView.setImageBitmap(bitmap);
        } else {
            Log.d("bitmap", "null");
        }
        avatarImageView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        });

        //初始化数据库对象
        mySqlHelper = new zMySqlHelper(this);
        //显示真正的账号
        showname.setText("欢迎您，" + username + "  !");

        //监听 - 各项设置
        m1.setOnClickListener(view -> Toast.makeText(page4.this,"该功能暂未开放",
                Toast.LENGTH_SHORT).show());
        m22.setOnClickListener(view -> Toast.makeText(page4.this,"该功能暂未开放",
                Toast.LENGTH_SHORT).show());
        // 我的订单
        m21.setOnClickListener(view -> {
            Intent intent = new Intent(page4.this, MyOrderActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        // 评价
        m23.setOnClickListener(view -> {
//            Intent to_comment = new Intent(page4.this, comment.class);
//            to_comment.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(to_comment);
            Toast.makeText(page4.this,"该功能暂未开放",
                    Toast.LENGTH_SHORT).show();

        });

        m24.setOnClickListener(view -> Toast.makeText(page4.this,"该功能暂未开放",
                Toast.LENGTH_SHORT).show());
        m31.setOnClickListener(view -> Toast.makeText(page4.this,
                "当前只有默认支付的方式",Toast.LENGTH_SHORT).show());
        m32.setOnClickListener(view -> Toast.makeText(page4.this,
                "默认为北京信息科技大学",Toast.LENGTH_SHORT).show());
        m33.setOnClickListener(view -> {
            Intent to_help = new Intent(page4.this, page4_help.class);
            to_help.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(to_help);
        });
        m34.setOnClickListener(view -> Toast.makeText(page4.this,
                "暂无其他设置",Toast.LENGTH_SHORT).show());
        m4.setOnClickListener(view -> {
            Intent to_p1 = new Intent(page4.this, MainActivity.class);
            to_p1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(to_p1);
        });
        //监听 - 导航栏跳转
        p1.setOnClickListener(view -> {
            Intent to_p1 = new Intent(page4.this, MainActivity.class);
            to_p1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(to_p1);
        });
        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_p2 = new Intent(page4.this, page2.class);
                to_p2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(to_p2);
            }
        });
        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击跳转到购物车，需判断购物车是否为空，来决定跳转到哪一个xml页面
                sqLiteDatabase = openOrCreateDatabase("MYsqlite.db", MODE_PRIVATE, null);
                Cursor cursor = sqLiteDatabase.query("cart",
                        null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String foodname = null;
                        String foodprice = null;
                        int quantity = 0;
                        try {
                            //拿到下标，游标指向相应下标位置即可拿到对应数据信息
                            foodname = cursor.getString(cursor.getColumnIndexOrThrow("foodname"));
                            foodprice = cursor.getString(cursor.getColumnIndexOrThrow("foodprice"));
                            quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                        } catch (IllegalArgumentException e) {
                            Log.e(TAG, "getColumnIndexOrThrow error: " + e.getMessage());
                        }
                        if (quantity !=0) {
                            //但凡找到一个数量不为0的商品都说明购物车不为空，需跳到 page3_Cart
                            Intent to_p3cart = new Intent(page4.this, page3_Cart.class);
                            to_p3cart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(to_p3cart);
                            return;
                        }
                    } while (cursor.moveToNext());
                }
                // 关闭游标和数据库连接
                cursor.close();
                sqLiteDatabase.close();
                //前面的quantity都为0,没有return,所以要执行以下代码，跳转到 page3
                Intent to_p3 = new Intent(page4.this, page3.class);
                to_p3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(to_p3);
            }
        });

        //监听 - 注销页面跳转
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_logout = new Intent(page4.this, page4_logout.class);
                to_logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(to_logout);
            }
        });

        //监听 - 退出程序
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(page4.this)
                        .setTitle("尊敬的用户")
                        .setMessage("您确定要退出程序吗？")
                        .setPositiveButton("残忍离开", (dialog, id) -> {
                            finishAffinity();
                            System.exit(0);
                        })
                        .setNegativeButton("我再想想", (dialog, id) -> dialog.cancel())
                        .show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri selectedImageUri = data.getData();
                    Bitmap bitmap = null;
                    // 将头像转为bitmap
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 保存头像到username.png
                    assert bitmap != null;
                    saveImage(this, bitmap, username);
                    avatarImageView.setImageBitmap(bitmap);
                }

        }
    }
    // 保存图片
    private static boolean saveImage(Context context, Bitmap bitmap, String imageName) {
        File directory = context.getFilesDir();
        File file = new File(directory, imageName + ".png");

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 读取图片
    private static Bitmap readImage(Context context, String imageName) {
        File directory = context.getFilesDir();
        File file = new File(directory, imageName + ".png");

        Bitmap bitmap = null;

        if (file.exists()) {
            try {
                bitmap = BitmapFactory.decodeFile(file.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }


}