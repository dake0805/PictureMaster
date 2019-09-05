package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

public class StyleChange extends AppCompatActivity {

    private Uri imageUri;
    private Bitmap bitmap;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_change);

        Intent intent = getIntent();
        imageUri = Uri.parse(intent.getStringExtra("style_change_pic"));
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

    }


    public void VincentClick(View view) {


//        ProgressDialog progressdialog = new ProgressDialog(getApplicationContext());
//        progressdialog.setMessage("Please Wait....");
//        progressdialog.show();


        showWaitingDialog();

//        //TODO传输数据
//        Context context = getApplicationContext();
//        ServerCommunication.Upload(getApplicationContext(), imageUri);
//        //TODO获取数据
//        Intent intent = new Intent(StyleChange.this, PictureProcessActivity.class);
//        intent.putExtra("extar_uri_process", imageUri.toString());
//        startActivity(intent);
    }

    private void showWaitingDialog() {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
        final ProgressDialog waitingDialog =
                new ProgressDialog(StyleChange.this);
        waitingDialog.setTitle(R.string.ai);
        waitingDialog.setMessage("等待中...");
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 1秒后窗口消失
                waitingDialog.cancel();
            }
        }).start();
    }

}