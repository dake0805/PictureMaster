package com.nwpu.paim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AiSuperResolution extends AppCompatActivity {

    private Uri imageUri;
    private Bitmap bitmap;
    private Uri getServerUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_super_resolution);

        Toolbar toolbar = findViewById(R.id.superResolution_toolbar2);
        Intent intent = getIntent();

        imageUri = Uri.parse(intent.getStringExtra("super_resolution_pic"));
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void SuperResolutionClick(View view) throws Exception {
        ServerCommunication.Upload(getApplicationContext(), imageUri, "ESRGAN");
        String fileName = Photo.getName(getApplicationContext(), imageUri);
        downloadPic(fileName, "ESRGAN");
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
                        Intent intent = new Intent(AiSuperResolution.this, PictureProcessActivity.class);
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

        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpName = df.format(day);
        File finishFile = new File(getCacheDir(), tmpName+".jpg");
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
