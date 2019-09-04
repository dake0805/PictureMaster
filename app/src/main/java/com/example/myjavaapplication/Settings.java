package com.example.myjavaapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Settings extends AppCompatActivity{

private Button btn1;

private Button btn2;

private Button btn3;

    protected void onCreate(Bundle savaInstanceState){
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_settings);
        btn1 = (Button)findViewById(R.id.button7);
        btn2 = (Button)findViewById(R.id.button10);
        btn3 = (Button)findViewById(R.id.button11) ;
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView about= (TextView)findViewById(R.id.textView2);
                if(about.getVisibility()==View.VISIBLE) {
                    about.setVisibility(View.INVISIBLE);
                }
                else about.setVisibility(View.VISIBLE);
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
