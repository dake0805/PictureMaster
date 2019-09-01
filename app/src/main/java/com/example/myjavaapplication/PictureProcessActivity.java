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
import android.widget.TextView;
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


    //点击AI按钮弹出界面所需的5个变量
    private TextView backgroudforbrauty;
    private  Button highfraction;
    private  Button Stylemigration;
    private  Button beauty;
    private  boolean isbeautybutton =true;

    //点击添加按钮弹出界面所需的6个变量
    private TextView backgroudforadd;
    private  Button addtext;
    private  Button addaccessories;
    private  Button addphotoframe;
    private  Button add;
    private  boolean isaddbutton =true;

    //其余的home和select按钮
    private Button Home;
    private boolean isthetwoon = false;

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

    /**
     * 当点击beauty按钮时，
     * 如果isbeautybutton=true，
     * add相关的控件消失在屏幕上，之后置isaddbutton为true
     * beauty相关的控件显示在屏幕上，之后置isbeautybutton为false
     * 其余两个按钮消失，之后置isthetwoon为true
     *
     * 如果isbeautybutton=false，beauty相关的控件不显示在屏幕上，之后置isbeautybutton为true
     * 其余两个按钮出现，之后置isthetwoon为false
     */
    public void btn_onclickofbeauty(View view){
        backgroudforbrauty=(TextView)findViewById(R.id.backgroundforbeauty);
        highfraction=(Button)findViewById(R.id.highfraction);
        Stylemigration=(Button)findViewById(R.id.Stylemigration);
        backgroudforadd=(TextView)findViewById(R.id.backgroundforadd);
        addtext=(Button)findViewById(R.id.addtext);
        addaccessories=(Button)findViewById(R.id.addaccessories);
        addphotoframe=(Button)findViewById(R.id.addphotoframe);
        Home=(Button)findViewById(R.id.homebutton);

        if(isbeautybutton){
            backgroudforadd.setVisibility(View.GONE);
            addtext.setVisibility(View.GONE);
            addaccessories.setVisibility(View.GONE);
            addphotoframe.setVisibility(View.GONE);
            isaddbutton=true;
            backgroudforbrauty.setVisibility(View.VISIBLE);
            highfraction.setVisibility(View.VISIBLE);
            Stylemigration.setVisibility(View.VISIBLE);
            isbeautybutton=false;
            Home.setVisibility(View.GONE);
            isthetwoon=true;
        }
        else{
            backgroudforbrauty.setVisibility(View.GONE);
            highfraction.setVisibility(View.GONE);
            Stylemigration.setVisibility(View.GONE);
            isbeautybutton=true;
            Home.setVisibility(View.VISIBLE);
            isthetwoon=false;
        }

    }

    /**
     * 当点击add按钮时，
     * 如果isaddbutton=true，
     * beauty相关的控件消失在屏幕上，之后置isbeautybutton为true
     * add相关的控件显示在屏幕上，之后置isaddbutton为false
     *
     * 如果isaddbutton=false，add相关的控件不显示在屏幕上，之后置isaddbutton为true
     */

    public void btn_onclickofadd(View view){
        backgroudforadd=(TextView)findViewById(R.id.backgroundforadd);
        addtext=(Button)findViewById(R.id.addtext);
        addaccessories=(Button)findViewById(R.id.addaccessories);
        addphotoframe=(Button)findViewById(R.id.addphotoframe);
        backgroudforbrauty=(TextView)findViewById(R.id.backgroundforbeauty);
        highfraction=(Button)findViewById(R.id.highfraction);
        Stylemigration=(Button)findViewById(R.id.Stylemigration);
        Home=(Button)findViewById(R.id.homebutton);

        if(isaddbutton){
            backgroudforbrauty.setVisibility(View.GONE);
            highfraction.setVisibility(View.GONE);
            Stylemigration.setVisibility(View.GONE);
            isbeautybutton=true;
            backgroudforadd.setVisibility(View.VISIBLE);
            addtext.setVisibility(View.VISIBLE);
            addaccessories.setVisibility(View.VISIBLE);
            addphotoframe.setVisibility(View.VISIBLE);
            isaddbutton=false;
            Home.setVisibility(View.GONE);
            isthetwoon=true;

        }
        else{
            backgroudforadd.setVisibility(View.GONE);
            addtext.setVisibility(View.GONE);
            addaccessories.setVisibility(View.GONE);
            addphotoframe.setVisibility(View.GONE);
            isaddbutton=true;
            Home.setVisibility(View.VISIBLE);
            isthetwoon=false;
        }
    }
}
