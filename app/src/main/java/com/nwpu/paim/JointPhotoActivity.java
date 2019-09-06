package com.nwpu.paim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class JointPhotoActivity extends AppCompatActivity {

    private ImageView imageView;

    protected void onCreate(Bundle savaInstanceState) {
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_jointphoto);
    }

    public void JointDoneClick(View view) {
        Intent intent = new Intent(JointPhotoActivity.this, PictureProcessActivity.class);
        Uri jointImageUri;

//        jointImageUri = FileProvider.getUriForFile(this,
//                "com.nwpu.paim",
//                jointImageFile);
//        intent.putExtra("extra_uri", jointImageUri.toString());

        startActivity(intent);
    }
}
