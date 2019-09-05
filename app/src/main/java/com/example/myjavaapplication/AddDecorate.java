package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;

import com.example.myjavaapplication.utils.ImageUtils;
import com.example.myjavaapplication.view.*;

public class AddDecorate extends AppCompatActivity implements View.OnClickListener, MyRelativeLayout.MyRelativeTouchCallBack {


    MyRelativeActivit_Decorate picture_dec;
    Uri imagUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_decorate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add_decorate);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        Button Button_Finish = (Button) findViewById(R.id.add_decorate_finished);
        Button_Finish.setOnClickListener(this);


        try {
            into();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void into() throws FileNotFoundException {

        Intent intent = getIntent();
        imagUri = Uri.parse(intent.getStringExtra("extra_decoradd"));

        try {

            Bitmap back = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagUri);

            picture_dec = findViewById(R.id.add_decorate_photo);
            back = ChangeSize(back);


            picture_dec.setBackGroundBitmap(back);
        } catch (IOException e) {
            e.printStackTrace();
        }
        picture_dec.setMyRelativeTouchCallBack(this);


    }


    Bitmap ChangeSize(Bitmap bitmap) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        double rate = (double) height / (double) width;
        //获得dpi（每英寸有多少个像素）
        float dpi = displayMetrics.densityDpi;

        //有点问题
        int newWidth = displayMetrics.widthPixels;
        int newHeight = (int) Math.floor(rate * ((double) newWidth));

        //计算压缩的比率
        float scaleWidth = (((float) newWidth) / width) * (dpi / 160.0f);
        float scaleHeight = (((float) newHeight) / height) * (dpi / 160.0f);

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);


        //获取新的bitmap
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }


    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.add_decorate_finished:
                Intent intent = new Intent(AddDecorate.this, PictureProcessActivity.class);

                Bitmap bitmap = ImageUtils.createViewBitmap(picture_dec, picture_dec.getWidth(), picture_dec.getHeight());

                BitmapToUri bitmapToUri = new BitmapToUri();
                Uri Text_edit_Finish_uri = null;
                try {
                    Text_edit_Finish_uri = bitmapToUri.BitmapToUri(bitmap, AddDecorate.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent.putExtra("extra_uri_process", Text_edit_Finish_uri.toString());

                startActivity(intent);

                break;


            default:

        }
    }

    //实现继承的三个函数
    @Override
    public void touchMoveCallBack(int direction) {
    }

    @Override
    public void onTextViewMoving(TextView textView) {
        Log.d("HHH", "TextView正在滑动");
    }

    public void onTextViewMovingDone() {
        //Toast.makeText(AddDecorate.this, "标签TextView滑动完毕！", Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public ComponentName getCallingActivity() {
        return super.getCallingActivity();
    }


}
