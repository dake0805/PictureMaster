package com.example.myjavaapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoResultActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri ImageUri;
    protected void onCreate(Bundle savaInstanceState){
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_photo_result);
        imageView = findViewById(R.id.imageResult);
        Intent intent = getIntent();
        if(intent.getStringExtra("extra_uri")!=null)
        {
            ImageUri = Uri.parse(intent.getStringExtra("extra_uri"));
            imageView.setImageURI(ImageUri);
        }
    }


}
