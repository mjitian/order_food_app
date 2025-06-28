package com.example.finalwork;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class page4_logout extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private zMySqlHelper mySqlHelper;
    private String now_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page4_logout);

        //获取两个输入框，一个注册，一个数据库对象
        etUsername = findViewById(R.id.logout_username);
        etPassword = findViewById(R.id.logout_password);
        TextView exit = findViewById(R.id.logout);
        TextView back = findViewById(R.id.back_to_settings);
        mySqlHelper = new zMySqlHelper(this);
        //获取当前登录的账号username
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        now_username = preferences.getString("username", "");
        // 返回按钮跳转回设置页（page4）
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_page4 = new Intent(page4_logout.this, page4.class);
                to_page4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(to_page4);
            }
        });
        //监听 - 退出程序
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new com.google.android.material.dialog.MaterialAlertDialogBuilder(page4_logout.this)
                        .setTitle("尊敬的用户")
                        .setMessage("您确定要进行注销吗？(此操作将清空所有数据且不可还原，请谨慎选择！)")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // 获取输入框信息
                                String username = etUsername.getText().toString().trim();
                                String password = etPassword.getText().toString().trim();

                                if (!username.equals(now_username)) {
                                    Toast.makeText(view.getContext(), "账号不合法，请重新输入", Toast.LENGTH_SHORT).show();
                                } else {
                                    boolean result = mySqlHelper.logout(username, password);
                                    if (result) {
                                        Toast.makeText(view.getContext(), "注销成功", Toast.LENGTH_SHORT).show();
                                        Intent to_login = new Intent(page4_logout.this, login.class);
                                        to_login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(to_login);
                                    } else {
                                        Toast.makeText(view.getContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消", (dialog, id) -> dialog.cancel())
                        .show();
            }
        });


    }
}