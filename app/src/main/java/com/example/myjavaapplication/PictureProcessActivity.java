package com.example.myjavaapplication;

import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.core.content.FileProvider.getUriForFile;

public class PictureProcessActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri imageUri;
    private Button doneButton;
    public static final int CHOOSE_PICTURE = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_process);
        doneButton = (Button) findViewById(R.id.done_button);
        imageView = findViewById(R.id.imageView_process);
        Intent intent = getIntent();
        if (intent.getStringExtra("extra_uri") != null) {
            imageUri = Uri.parse(intent.getStringExtra("extra_uri"));
            imageView.setImageURI(imageUri);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    imageUri = data.getData();
                    imageView.setImageURI(null);
                    imageView.setImageURI(imageUri);
                    break;
                case UCrop.REQUEST_CROP:
                    imageUri = UCrop.getOutput(data);
                    File tempFile = new File(imageUri.getPath());


                    imageView.setImageURI(null);
                    imageView.setImageURI(imageUri);
                    break;
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void EditClick(View view) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "test.jpg"));

        //////////////Uri destinationUri格式:file://*


        UCrop ucrop = UCrop.of(imageUri, destinationUri);
        ucrop = UcropConfig(ucrop);

        ucrop.start(this);


    }

    public void DoneClick(View view) {
        Intent intent = new Intent(PictureProcessActivity.this, PhotoResultActivity.class);
        intent.putExtra("extra_resultUri", imageUri.toString());

        startActivity(intent);
    }

    public void HomeClick(View view) {
        Intent intent = new Intent(PictureProcessActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //select button 绑定
    public void SelectPhoto_Pre(View view) {
        Intent selectPhoto = new Intent();
        selectPhoto.setAction(Intent.ACTION_PICK);
        selectPhoto.setType("image/*");
        startActivityForResult(selectPhoto, CHOOSE_PICTURE);
    }


    private UCrop UcropConfig(UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
//        options.setToolbarColor(Color.GREEN);
//        options.setActiveWidgetColor(Color.GREEN);
//        options.setCropFrameColor(Color.GREEN);
//        options.setStatusBarColor(Color.GREEN);
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.NONE, UCropActivity.ALL);
        options.setFreeStyleCropEnabled(true);
        options.setHideBottomControls(true);
        return uCrop.withOptions(options);
    }


}
