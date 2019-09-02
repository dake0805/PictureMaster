package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.IOException;

public class PicColorControlActivity extends AppCompatActivity {


    private ImageView imageView;
    private Uri imageUri;
    private Bitmap bitmap;

    private static final int MAX_VALUE = 255;
    private static final int MID_VALUE = 127;

    private float brightness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_color_control);

        imageView = (ImageView) findViewById(R.id.colorControl_ImageView);

        Intent intent = getIntent();
        if (intent.getStringExtra("brightness_change_pic") != null) {
            imageUri = Uri.parse(intent.getStringExtra("brightness_change_pic"));
        }

        imageView.setImageURI(imageUri);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar_brightness);
        seekBar.setMax(MAX_VALUE);
        seekBar.setProgress(MID_VALUE);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress * 1.0f / MID_VALUE;
                imageView.setImageBitmap(ChangeBrightness(bitmap, brightness));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private static Bitmap ChangeBrightness(Bitmap startImage, float brightness) {
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(brightness, brightness, brightness, 1);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(lumMatrix));
        Bitmap bitmap = Bitmap.createBitmap(startImage.getWidth(), startImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(startImage, 0, 0, paint);
        return bitmap;
    }

}
