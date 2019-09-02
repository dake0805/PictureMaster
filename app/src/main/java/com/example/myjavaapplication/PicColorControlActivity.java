package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

public class PicColorControlActivity extends AppCompatActivity {


    private ImageView imageView;
    private Uri imageUri;

    private static final int MAX_VALUE = 255;
    private static final int MID_VALUE = 127;

    private double brightness;

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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightness = progress * 1.0f / MID_VALUE;
                //do something
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}
