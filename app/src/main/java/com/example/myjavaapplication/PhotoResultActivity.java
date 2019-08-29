package com.example.myjavaapplication;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class PhotoResultActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri ImageUri;
    private Button setBackground;
    protected void onCreate(Bundle savaInstanceState){
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_photo_result);
        imageView = findViewById(R.id.imageResult);
        setBackground = findViewById(R.id.button10);
        final WallpaperManager wpManager = WallpaperManager.getInstance(this);

        setBackground.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                try {
                    wpManager.setResource(R.id.imageResult); //墙纸
                    Toast.makeText(PhotoResultActivity.this, "更换壁纸成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent intent = getIntent();
        if(intent.getStringExtra("extra_uri")!=null)
        {
            ImageUri = Uri.parse(intent.getStringExtra("extra_uri"));
            imageView.setImageURI(ImageUri);
        }
    }


}
