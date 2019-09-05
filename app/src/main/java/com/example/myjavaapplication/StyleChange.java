package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StyleChange extends AppCompatActivity {

    private Uri imageUri;
    private Bitmap bitmap;
    private Uri getServerUri;

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


    public void VincentClick(View view) throws Exception {
        StyleChangeClick("CycleGAN_Monet");
//        ServerCommunication.Upload(getApplicationContext(), imageUri,"CartoonGAN_Hayao");
//        String fileName = Photo.getName(getApplicationContext(),imageUri);
//        downloadPic(fileName,"CartoonGAN_Hayao");
//        Thread.sleep(500);
//        Toast.makeText(getApplicationContext(), "上传成功，正在下载······", Toast.LENGTH_SHORT).show();
    }


    private void StyleChangeClick(String type) throws Exception {
        ServerCommunication.Upload(getApplicationContext(), imageUri, type);
        String fileName = Photo.getName(getApplicationContext(), imageUri);
        downloadPic(fileName, type);
        Thread.sleep(500);
        Toast.makeText(getApplicationContext(), "上传成功，正在下载······", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("HandlerLeak")
    public void downloadPic(String picName, String type) throws InterruptedException {
        ServerCommunication serverCommunication = new ServerCommunication();
        serverCommunication.setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ServerCommunication.CHANGE_UI) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    try {
                        getServerUri = bitMapToUri(bitmap);
                        Intent intent = new Intent(StyleChange.this, PictureProcessActivity.class);
                        intent.putExtra("extra_uri_process", getServerUri.toString());
                        startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == ServerCommunication.ERROR) {
                    //throw new Exception("download error");
                }
            }
        });
        serverCommunication.Download(picName, type);
    }

    public Uri bitMapToUri(Bitmap bitmap) throws IOException {
        Uri finishUri;

        File finishFile = new File(getCacheDir(), "tmp_styleChange.jpg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();

        FileOutputStream fileOutputStream = new FileOutputStream(finishFile);
        fileOutputStream.write(bitmapData);
        fileOutputStream.flush();
        fileOutputStream.close();

        finishUri = Uri.fromFile(finishFile);
        return finishUri;
    }
}