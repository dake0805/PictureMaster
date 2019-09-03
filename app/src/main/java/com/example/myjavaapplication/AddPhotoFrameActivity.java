package com.example.myjavaapplication;

import android.app.Activity;
import android.content.Intent;
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

import static android.graphics.Shader.TileMode.CLAMP;

public class AddPhotoFrameActivity extends Activity {
    private Button RoundBut;
    private Button CircleBut;
    private Uri imageUri;
    private Bitmap bitmap;

    private int outWidth;
    private int outHeight;
    private int radius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addphotoframe);
        RoundBut = (Button) findViewById(R.id.RoundBut);
        CircleBut = (Button) findViewById(R.id.CircleBut);

        Intent intent = getIntent();

        if (intent.getStringExtra("extra_addFrame") != null) {
            imageUri = Uri.parse(intent.getStringExtra("extra_addFrame"));
            try {
                // 读取uri所在的图片
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                outWidth = bitmap.getWidth();
                outHeight = bitmap.getHeight();
                double rad = Math.sqrt(outWidth ^ 2 + outHeight ^ 2);
                radius = new Double(rad).intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RoundBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRoundBitmapByShader(bitmap, outWidth, outHeight, radius, 1);
            }
        });

        CircleBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCircleBitmapByShader(bitmap, outWidth, outHeight, 1);
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
            //绘制boarder
            Paint boarderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            boarderPaint.setColor(Color.GREEN);
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
            boarderPaint.setColor(Color.GREEN);
            boarderPaint.setStyle(Paint.Style.STROKE);
            boarderPaint.setStrokeWidth(boarder);
            canvas.drawCircle(outWidth / 2, outHeight / 2, radius - boarder, boarderPaint);
        }
        return desBitmap;
    }
}
