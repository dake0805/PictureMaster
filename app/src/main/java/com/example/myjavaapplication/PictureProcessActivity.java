package com.example.myjavaapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class PictureProcessActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri ImageUri;
    protected void onCreate(Bundle savaInstanceState){
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_pic_process);
        imageView = findViewById(R.id.imageView);
        Intent intent = getIntent();
        ImageUri =Uri.parse(intent.getStringExtra("extra_uri"));
        imageView.setImageURI(ImageUri);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    Uri cropPhoto = UCrop.getOutput(data);          //得到的裁剪结果URI
                    Intent intent = new Intent(PictureProcessActivity.this,PhotoResultActivity.class);
                    intent.putExtra("extra_uri",ImageUri.toString());
                    startActivity(intent);
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void EditClick(View view){
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "test.jpg"));
        UCrop.of(ImageUri, destinationUri)
                .withMaxResultSize(1920, 1080)
                .start(this);
    }
}
