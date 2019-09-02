package com.example.myjavaapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PicColorControlActivity extends AppCompatActivity {


    private ImageView imageView;
    private Uri imageUri;
    private Bitmap bitmap;
    private SeekBar seekBar;
    private static final int MAX_VALUE = 200;
    private static final int MID_VALUE = 100;

    private float brightness;
    private float saturation;
    private float contrast;

    enum Mode {
        Brightness,
        Saturation,
        Contrast,
        None;
    }

    private Mode mode = Mode.None;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_color_control);

        imageView = findViewById(R.id.colorControl_ImageView);

        SeekBar seekBar = findViewById(R.id.seekBar_editPhoto);
        seekBar.setMax(MAX_VALUE);
        seekBar.setProgress(MID_VALUE);

        Intent intent = getIntent();
        if (intent.getStringExtra("brightness_change_pic") != null) {
            mode = Mode.Brightness;
            imageUri = Uri.parse(intent.getStringExtra("brightness_change_pic"));
            getBitmap();
            Brightness();
        } else if (intent.getStringExtra("saturation_change_pic") != null) {
            mode = Mode.Saturation;
            imageUri = Uri.parse(intent.getStringExtra("saturation_change_pic"));
            getBitmap();
            Saturation();
        } else if (intent.getStringExtra("contrast_change_pic") != null) {
            mode = Mode.Contrast;
            imageUri = Uri.parse(intent.getStringExtra("contrast_change_pic"));
            getBitmap();
            Contrast();
        }

    }


    private void getBitmap() {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageURI(imageUri);
    }


    //亮度
    private void Brightness() {
        seekBar = findViewById(R.id.seekBar_editPhoto);
        seekBar.setMax(160);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(40);
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

    //饱和度
    private void Saturation() {
        seekBar = findViewById(R.id.seekBar_editPhoto);
//

//
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                saturation = progress * 0.01f;
                imageView.setImageBitmap(ChangeSaturation(bitmap, saturation));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //对比度
    private void Contrast() {
        seekBar = findViewById(R.id.seekBar_editPhoto);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            seekBar.setMin(30);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                contrast = (progress - 100) * 0.01f;
                imageView.setImageBitmap(ChangeContrast(bitmap, contrast));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private Bitmap ChangeContrast(Bitmap startImage, float contrast) {
        float scale = contrast + 1.f;
        float translate = (-.5f * scale + .5f) * 255.f;
        float[] array = new float[]{
                scale, 0, 0, 0, translate,
                0, scale, 0, 0, translate,
                0, 0, scale, 0, translate,
                0, 0, 0, 1, 0};
        ColorMatrix contrastMatrix = new ColorMatrix(array);

        return PaintBitmap(startImage, contrastMatrix);
    }


    private static Bitmap ChangeSaturation(Bitmap startImage, float saturation) {
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        return PaintBitmap(startImage, saturationMatrix);
    }

    private static Bitmap ChangeBrightness(Bitmap startImage, float brightness) {
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(brightness, brightness, brightness, 1);

        return PaintBitmap(startImage, lumMatrix);
    }


    private static Bitmap PaintBitmap(Bitmap startImage, ColorMatrix matrix) {
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        Bitmap bitmap = Bitmap.createBitmap(startImage.getWidth(), startImage.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(startImage, 0, 0, paint);
        return bitmap;
    }


    public void FinishChange(View view) throws IOException {


        switch (mode) {
            case Brightness:
                bitmap = ChangeBrightness(bitmap, brightness);
                break;
            case Saturation:
                bitmap = ChangeSaturation(bitmap, saturation);
                break;
            case Contrast:
                bitmap = ChangeContrast(bitmap, contrast);
                break;
        }


        Uri finishUri;

        File finishFile = new File(getCacheDir(), "tmp_brightness.jpg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();

        FileOutputStream fileOutputStream = new FileOutputStream(finishFile);
        fileOutputStream.write(bitmapData);
        fileOutputStream.flush();
        fileOutputStream.close();

        finishUri = Uri.fromFile(finishFile);

        imageView.setImageURI(null);
        imageView.setImageURI(finishUri);
        if (finishUri != null) {
            Intent intent = new Intent();
            intent.putExtra("finishChange", finishUri.toString());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
