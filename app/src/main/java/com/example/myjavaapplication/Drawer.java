package com.example.myjavaapplication;



/*
* 添加文字功能的实现主页面
* 名字起的有问题
* @hrncool
* */


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.example.myjavaapplication.view.*;



public class Drawer extends AppCompatActivity implements View.OnClickListener, MyRelativeLayout.MyRelativeTouchCallBack{
    Uri imageUri_add;
    ImageView imageView;
    Drawable try1;

    private MyRelativeLayout background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        //imageView = (ImageView)findViewById(R.id.photo);
        //Intent intent = getIntent();

        //if(intent.getStringExtra("extra_photoadd")!=null)
        //{
        //    imageUri_add = Uri.parse(intent.getStringExtra("extra_photoadd"));
        //    imageView.setImageURI(imageUri_add);
        //}

        //Button Button_AddText = (Button)findViewById(R.id.Edit_button);
        Button Button_Finsh = (Button)findViewById(R.id.drawer_finshed);
        //final EditText EditText_1 = (EditText)findViewById(R.id.Edit_Text1) ;
        //Button_AddText.setOnClickListener(this);
        Button_Finsh.setOnClickListener(this);


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
            Bitmap back = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri_add);
            background =(MyRelativeLayout)findViewById(R.id.add_photo);
            background.setBackGroundBitmap(back);
        } catch (IOException e) {
            e.printStackTrace();
        }
        background.setMyRelativeTouchCallBack(this);

    }


    //实现继承的三个函数
    @Override
    public void touchMoveCallBack(int direction) {
        if (direction == 6) {
            Toast.makeText(Drawer.this, "你在向左滑动！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Drawer.this, "你在向右滑动！", Toast.LENGTH_SHORT).show();
        }
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

        switch (v.getId()){
            /*case R.id.Edit_button:
                Intent intent = new Intent(Drawer.this,TextEdit.class);
                startActivity(intent);

                break;*/

            case R.id.drawer_finshed:

                //用于保存的代码
                Bitmap bitmap = ImageUtils.createViewBitmap(background, background.getWidth(), background.getHeight());
                String fileName = "CRETIN_" + new SimpleDateFormat("yyyyMMdd_HHmmss")
                        .format(new Date()) + ".png";
                String result = ImageUtils.saveBitmapToFile(bitmap, fileName);
                Toast.makeText(Drawer.this, "保存位置:" + result, Toast.LENGTH_SHORT).show();

                break;

            default:
        }

    }


    /*
    *当再次返回页面的时候
    * */
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        String data = intent.getStringExtra("edit_text");

        TextView changetext = (TextView)findViewById(R.id.drawer_changetext);

        findViewById(R.id.drawer_changetext).bringToFront();

        changetext.setText(data);

    }




}

