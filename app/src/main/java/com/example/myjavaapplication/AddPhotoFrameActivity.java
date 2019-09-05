package com.example.myjavaapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;

import static android.graphics.Shader.TileMode.CLAMP;

public class AddPhotoFrameActivity extends AppCompatActivity {
    private Button RoundBut;
    private Button CircleBut;
    private Button ConfirmBut;
    private Uri imageUri;
    private Bitmap bitmap;
    private ImageView imageView;
    private Uri AddFrame_Finish_uri;

    private int outWidth;
    private int outHeight;
    private int radius;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addphotoframe);
        RoundBut = (Button) findViewById(R.id.RoundBut);
        CircleBut = (Button) findViewById(R.id.CircleBut);
        ConfirmBut = (Button) findViewById(R.id.ConfirmBut);
        imageView = (ImageView) findViewById(R.id.imageview_addframe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_frame);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        final Intent intent = getIntent();

        if (intent.getStringExtra("extra_uri_process") != null) {
            imageUri = Uri.parse(intent.getStringExtra("extra_uri_process"));
            try {
                // 读取uri所在的图片
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                outWidth = bitmap.getWidth();
                outHeight = bitmap.getHeight();
                double rad = Math.sqrt(outWidth ^ 2 + outHeight ^ 2);
                radius = new Double(rad).intValue();
                imageView.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 加边框并显示
        RoundBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap backBitmap = getRoundBitmapByShader(bitmap, outWidth, outHeight, radius, 5);

                BitmapToUri bitmapToUri = new BitmapToUri();
                try {
                    AddFrame_Finish_uri = bitmapToUri.BitmapToUri(backBitmap, AddPhotoFrameActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageURI(null);
                imageView.setImageURI(AddFrame_Finish_uri);

                RoundBut = findViewById(R.id.RoundBut);
                CircleBut = findViewById(R.id.CircleBut);
                RoundBut.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#008577")));
                RoundBut.setTextColor(ColorStateList.valueOf(Color.parseColor("#008577")));
                CircleBut.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                CircleBut.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));

            }
        });

        CircleBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap backBitmap = getCircleBitmapByShader(bitmap, outWidth, outHeight, 1);

                BitmapToUri bitmapToUri = new BitmapToUri();
                try {
                    AddFrame_Finish_uri = bitmapToUri.BitmapToUri(backBitmap, AddPhotoFrameActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageURI(null);
                imageView.setImageURI(AddFrame_Finish_uri);

                RoundBut = findViewById(R.id.RoundBut);
                CircleBut = findViewById(R.id.CircleBut);
                RoundBut.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                RoundBut.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                CircleBut.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#008577")));
                CircleBut.setTextColor(ColorStateList.valueOf(Color.parseColor("#008577")));

            }
        });

        ConfirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //传回去
                Intent intent = new Intent(AddPhotoFrameActivity.this, PictureProcessActivity.class);
                intent.putExtra("extra_uri_process", AddFrame_Finish_uri.toString());
                startActivity(intent);
            }
        });
    }

    /**
     * 通过BitmapShader 圆角边框
     *
     * @param bitmap
     * @param outWidth
     * @param outHeight
     * @param radius
     * @param boarder
     * @return
     */
    public static Bitmap getRoundBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int radius, int boarder) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;

        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);

        //创建输出的bitmap
        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);

        //创建canvas并传入desBitmap，这样绘制的内容都会在desBitmap上
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //创建着色器
        BitmapShader bitmapShader = new BitmapShader(bitmap, CLAMP, CLAMP);

        //给着色器配置matrix
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);

        //创建矩形区域并且预留出border
        RectF rect = new RectF(boarder, boarder, outWidth - boarder, outHeight - boarder);

        //把传入的bitmap绘制到圆角矩形区域内
        canvas.drawRoundRect(rect, radius, radius, paint);

        if (boarder > 0) {
            //绘制boarder 修改边框参数
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(Color.WHITE);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawRoundRect(rect, radius, radius, boarderPaint);
        }
        return desBitmap;
    }

    /**
     * 通过BitmapShader 圆形边框
     *
     * @param bitmap
     * @param outWidth
     * @param outHeight
     * @param boarder
     * @return
     */
    public static Bitmap getCircleBitmapByShader(Bitmap bitmap, int outWidth, int outHeight, int boarder) {
        if (bitmap == null) {
            return null;
        }
        int radius;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float widthScale = outWidth * 1f / width;
        float heightScale = outHeight * 1f / height;

        Matrix matrix = new Matrix();
        matrix.setScale(widthScale, heightScale);
        Bitmap desBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
        if (outHeight > outWidth) {
            radius = outWidth / 2;
        } else {
            radius = outHeight / 2;
        }
        //创建canvas
        Canvas canvas = new Canvas(desBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapShader bitmapShader = new BitmapShader(bitmap, CLAMP, CLAMP);
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader(bitmapShader);
        canvas.drawCircle(outWidth / 2, outHeight / 2, radius - boarder, paint);
        if (boarder > 0) {
            //绘制boarder
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(Color.WHITE);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawCircle(outWidth / 2, outHeight / 2, radius - boarder, boarderPaint);
        }
        return desBitmap;
    }
}
