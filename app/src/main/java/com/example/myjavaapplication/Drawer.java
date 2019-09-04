package com.example.myjavaapplication;



/*
 * 添加文字功能的实现主活动
 * 名字起的有问题
 * @hrncool
 * */


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.myjavaapplication.utils.ImageUtils;
import com.example.myjavaapplication.view.MyRelativeLayout;


public class Drawer extends AppCompatActivity implements View.OnClickListener, MyRelativeLayout.MyRelativeTouchCallBack {
    Uri imageUri_add;
    ImageView imageView;
    Drawable try1;

    private MyRelativeLayout picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = findViewById(R.id.drawer_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        //imageView = (ImageView)findViewById(R.id.photo);
        //Intent intent = getIntent();

        //if(intent.getStringExtra("extra_photoadd")!=null)
        //{
        //    imageUri_add = Uri.parse(intent.getStringExtra("extra_photoadd"));
        //    imageView.setImageURI(imageUri_add);
        //}

        //Button Button_AddText = (Button)findViewById(R.id.Edit_button);
        Button Button_Finish = (Button) findViewById(R.id.drawer_finshed);
        //final EditText EditText_1 = (EditText)findViewById(R.id.Edit_Text1) ;
        //Button_AddText.setOnClickListener(this);
        Button_Finish.setOnClickListener(this);


        try {
            construct();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void construct() throws FileNotFoundException {
        //imageView = (ImageView)findViewById(R.id.photo);
        Intent intent = getIntent();
        imageUri_add = Uri.parse(intent.getStringExtra("extra_photoadd"));
        //imageView.setImageURI(imageUri_add);
        try {
            Bitmap back = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri_add);

            //Bitmap back = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri_add));


            picture = (MyRelativeLayout) findViewById(R.id.add_photo);

            //  picture.setMinimumHeight(back.getHeight());
            // picture.setMinimumWidth(back.getWidth());
            //picture.height = intent.getIntExtra("Height",0);
            //picture.width = intent.getIntExtra("Width",0);

            picture.height = 0;
            picture.width = 0;

            back = ChangeSize(back);


            picture.setBackGroundBitmap(back);
        } catch (IOException e) {
            e.printStackTrace();
        }
        picture.setMyRelativeTouchCallBack(this);

    }

    Bitmap ChangeSize(Bitmap bitmap) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        double rate = (double) height / (double) width;
        float dpi = displayMetrics.densityDpi;
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

    //实现继承的三个函数
    @Override
    public void touchMoveCallBack(int direction) {
    }

    @Override
    public void onTextViewMoving(TextView textView) {
        Log.d("HHH", "TextView正在滑动");
    }

    public void onTextViewMovingDone() {
        Toast.makeText(Drawer.this, "标签TextView滑动完毕！", Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public ComponentName getCallingActivity() {
        return super.getCallingActivity();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /*case R.id.Edit_button:
                Intent intent = new Intent(Drawer.this,TextEdit.class);
                startActivity(intent);

                break;*/

            case R.id.drawer_finshed:
                Intent intent = new Intent(Drawer.this, PictureProcessActivity.class);

                Bitmap bitmap = ImageUtils.createViewBitmap(picture, picture.getWidth(), picture.getHeight());
                Uri Text_edit_Finsh_uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                intent.putExtra("extar_uri_process", Text_edit_Finsh_uri.toString());

                startActivity(intent);


                break;
            default:
        }

    }


    /*
     *当再次返回页面的时候
     * */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String data = intent.getStringExtra("edit_text");

        //TextView changetext = (TextView)findViewById(R.id.drawer_changetext);

        // findViewById(R.id.drawer_changetext).bringToFront();

        //changetext.setText(data);

    }


}

