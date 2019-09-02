package com.example.myjavaapplication;



/*
* 添加功能的实现
* 名字起的有问题
* @hrncool
* */


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;

import com.example.myjavaapplication.view.*;

import java.io.InputStream;

import static android.graphics.drawable.Drawable.*;


public class Drawer extends AppCompatActivity implements View.OnClickListener, MyRelativeLayout.MyRelativeTouchCallBack{
    Uri imageUri_add;
    ImageView imageView;
    Drawable try1;

    private MyRelativeLayout background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        imageView = (ImageView)findViewById(R.id.photo);
        Intent intent = getIntent();

        if(intent.getStringExtra("extra_photoadd")!=null)
        {
            imageUri_add = Uri.parse(intent.getStringExtra("extra_photoadd"));
            imageView.setImageURI(imageUri_add);
        }

        Button Button_AddText = (Button)findViewById(R.id.Edit_button);
        final EditText EditText_1 = (EditText)findViewById(R.id.Edit_Text1) ;
        Button_AddText.setOnClickListener(this);

/*
        try {
            construct();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/


    }



    public void construct() throws FileNotFoundException {
        imageView = (ImageView)findViewById(R.id.photo);
        Intent intent = getIntent();
        imageUri_add = Uri.parse(intent.getStringExtra("extra_photoadd"));
        //imageView.setImageURI(imageUri_add);

        InputStream a;
        Drawable d;
        d = createFromStream(getContentResolver().openInputStream(imageUri_add),null);

/*
        background =(MyRelativeLayout)findViewById(R.id.add_photo);
        background.setBackground(d);

        background.setMyRelativeTouchCallBack(this);

*/

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

    public void onResume()
    {
        super.onResume();
        imageView.setImageURI(null);
        imageView.setImageURI(imageUri_add);
    }





    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Edit_button:
                Intent intent = new Intent(Drawer.this,TextEdit.class);
                startActivity(intent);

                //EditText EditText_1 = (EditText)findViewById(R.id.Edit_Text1) ;
                //EditText_1.requestFocus();
                //InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
               //imm.showSoftInput(EditText_1, InputMethodManager.SHOW_IMPLICIT);

                break;


            case R.id.Decorate_Button:

                break;

            default:
        }

    }


    /*
    *
    * */
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        //Intent intent2 = getIntent();
        String data = intent.getStringExtra("edit_text");

        TextView changetext = (TextView)findViewById(R.id.drawer_changetext);

        findViewById(R.id.drawer_changetext).bringToFront();

        //Log.d("Drawer",data);
        changetext.setText(data);



    }
 /*




    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


/*
    private void initListener(){

        screenWidth = getScreenWidth(this);//获取屏幕宽度
        screenHeight = getScreenHeight(this) - getStatusHeight(this);//屏幕高度-状态栏

        testTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;

                        int top = v.getTop() + dy;


                        int left = v.getLeft() + dx;


                        if (top <= 0) {
                            top = 0;
                        }
                        if (top >= screenHeight - testTv.getHeight() ) {
                            top = screenHeight - testTv.getHeight();
                        }
                        if (left >= screenWidth - testTv.getWidth()) {
                            left = screenWidth - testTv.getWidth();
                        }

                        if (left <= 0) {
                            left = 0;
                        }

                        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(v.getWidth(), v.getHeight());
                        param.leftMargin = left;
                        param.topMargin = top;
                        v.setLayoutParams(param);
//            v.layout(left, top, left+v.getWidth(), top+v.getHeight());

                        v.postInvalidate();

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }
                return true;
            }
        });
    }


*/



}

