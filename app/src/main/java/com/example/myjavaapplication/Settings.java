package com.example.myjavaapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Settings extends AppCompatActivity {

    private Button btn1;

    private Button btn2;

    private Button btn3;

    protected void onCreate(Bundle savaInstanceState) {
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_settings);
        btn1 = (Button) findViewById(R.id.setting_about);
        btn2 = (Button) findViewById(R.id.setting_contact);
        btn3 = (Button) findViewById(R.id.setting_feedback);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("保存照片");
                builder.setMessage("是否确定要存储照片");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                AlertDialog b = builder.create();
                b.show();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CSOKDialog okCancelDialog = CSOKDialog.createBuilder(Settings.this);
                okCancelDialog.setMsg("Email:nebula225@protonmail.com\nQQ:645745123\nTel:13679285813");
                //返回键是否关闭对话框  true 表示可以关闭
                okCancelDialog.setCancelable(false);
                //设置在窗口外空白处触摸时是否取消此对话框。 如果设置为true，则对话框设置为可取消
                okCancelDialog.setCanceledOnTouchOutside(false);
                okCancelDialog.show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedback = new Intent(Settings.this, FeedBackActivity.class);
                startActivity(feedback);
            }
        });
    }

}
