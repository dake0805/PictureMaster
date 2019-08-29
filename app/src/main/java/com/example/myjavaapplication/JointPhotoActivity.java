package com.example.myjavaapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class JointPhotoActivity extends AppCompatActivity {

    private ImageView imageView;

    protected void onCreate(Bundle savaInstanceState){
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_jointphoto);
    }

    public void ClickToResult(View view)
    {
        //test wyw
        Intent intent = new Intent(JointPhotoActivity.this,PhotoResultActivity.class);
        startActivity(intent);
    }
}
