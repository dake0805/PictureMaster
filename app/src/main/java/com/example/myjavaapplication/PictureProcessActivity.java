package com.example.myjavaapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class PictureProcessActivity extends AppCompatActivity {

    private ImageView imageView_drawer;
    private ImageView imageView_origin;
    private ImageView imageView_background;

    private Uri imageUri;

    public static final int CHOOSE_PICTURE = 1;
    public static final int CHANGE_PICTURE = 7;

    private Bitmap photoBmp;
    private Bitmap fuzzyPhotoBmp;

    private TextView backgroudforButton;

    //点击编辑按钮弹出界面所需变量
    private Button Edit;
    private Button Edit_Scale;
    private Button Edit_Rotate;
    private Button Edit_Saturation;
    private Button Edit_Contrast;
    private Button Edit_Brightness;
    private TextView Edit_text;


    //点击AI按钮弹出界面所需的4个变量
    private Button highfraction;
    private Button Stylemigration;
    private Button AI;
    private TextView AI_text;

    //点击添加按钮弹出界面所需的5个变量
    private Button addtext;
    private Button addaccessories;
    private Button addphotoframe;
    private Button add;
    private TextView add_text;

    //其余的home和done按钮
    private Button homeButton;
    private Button doneButton;

    //按钮选择状态
    enum ButtonSelectType {
        Edit,
        Ai,
        Add,
        None;
    }

    enum EditMethod {
        Scale,
        Rotate,
        Brightness,
        None;
    }


    private ButtonSelectType buttonSelect = ButtonSelectType.None;

    private EditMethod editMethod = EditMethod.None;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_process);

        BlidView();
        Intent intent = getIntent();
        imageUri = Uri.parse(intent.getStringExtra("extra_uri_origin"));
        fuzzyPhotoBmp = Photo.getFuzzyBitmapFromUri(getApplicationContext(), this.getContentResolver(), imageUri);
        imageView_origin.setImageURI(imageUri);
//        try {
//            testDownlaod();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        try {
//            ServerCommunication.Upload(getApplicationContext(),imageUri);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        Intent intent1 = getIntent();
//        if (intent1.getStringExtra("extra_uri_process") != null) {
//            imageUri = Uri.parse(intent1.getStringExtra("extra_uri_process"));
//            fuzzyPhotoBmp = Photo.getFuzzyBitmapFromUri(getApplicationContext(), this.getContentResolver(), imageUri);
//        }
//        RestoreOrigin();
        setIntent(intent);
        Intent intent1 = getIntent();
        imageUri = Uri.parse(intent1.getStringExtra("extra_uri_process"));
        fuzzyPhotoBmp = Photo.getFuzzyBitmapFromUri(getApplicationContext(), this.getContentResolver(), imageUri);
        RestoreOrigin();
    }

    @SuppressLint("HandlerLeak")
    public void testDownlaod() throws InterruptedException {
        ServerCommunication serverCommunication = new ServerCommunication();
        serverCommunication.setHandler(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ServerCommunication.CHANGE_UI) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    imageView_origin.setImageBitmap(bitmap);
                } else if (msg.what == ServerCommunication.ERROR) {
                    //throw new Exception("download error");
                }
            }
        });
        serverCommunication.Download("testtest","test");
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    imageUri = data.getData();
                    imageView_origin.setImageURI(imageUri);
                    fuzzyPhotoBmp = Photo.getFuzzyBitmapFromUri(getApplicationContext(), this.getContentResolver(), imageUri);
                    break;
                case UCrop.REQUEST_CROP:
                    imageUri = UCrop.getOutput(data);
                    imageView_origin.setImageURI(null);
                    imageView_origin.setImageURI(imageUri);
                    //准备模糊化
                    photoBmp = Photo.getBitmapFromUri(getApplicationContext(), this.getContentResolver(), imageUri);
                    fuzzyPhotoBmp = FastBlur.doBlur(photoBmp, 20, false);
                    break;
                case CHANGE_PICTURE:
                    if (Uri.parse(data.getStringExtra("finishChange")) != null) {
                        imageUri = Uri.parse(data.getStringExtra("finishChange"));
                    }
                    imageView_origin.setImageURI(null);
                    imageView_origin.setImageURI(imageUri);
            }
            RestoreOrigin();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void BlidView() {
        imageView_origin = (ImageView) findViewById(R.id.imageView_process);
        imageView_background = (ImageView) findViewById(R.id.background_process);

        backgroudforButton = (TextView) findViewById(R.id.backgroundforButton);

        Edit = (Button) findViewById(R.id.Editclick);
        Edit_Scale = (Button) findViewById(R.id.scale);
        Edit_Rotate = (Button) findViewById(R.id.rotate);
        Edit_Brightness = (Button) findViewById(R.id.brightness);
        Edit_Contrast = (Button) findViewById(R.id.contrast);
        Edit_Saturation = (Button) findViewById(R.id.saturation);
        Edit_text = (TextView) findViewById(R.id.edit_textview);


        highfraction = (Button) findViewById(R.id.highfraction);
        Stylemigration = (Button) findViewById(R.id.Stylemigration);
        AI = (Button) findViewById(R.id.AIclcik);
        AI_text = (TextView) findViewById(R.id.ai_textview);

        addtext = (Button) findViewById(R.id.addtext);
        addaccessories = (Button) findViewById(R.id.addaccessories);
        addphotoframe = (Button) findViewById(R.id.addphotoframe);
        add_text = (TextView) findViewById(R.id.add_textview);

        /**
         * addphotoframe 的跳转函数
         * 需要修改放置位置
         */
        addphotoframe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PictureProcessActivity.this, AddPhotoFrameActivity.class);
                intent.putExtra("extra_uri_process", imageUri.toString());
                startActivity(intent);
            }
        });

        homeButton = (Button) findViewById(R.id.homebutton);
        doneButton = (Button) findViewById(R.id.done_button);
    }

    /*
    编辑拆开
     */

    public void Edit1_scale(View view) {
        EditProcess(EditMethod.Scale);
    }

    public void Edit2_rotate(View view) {
        EditProcess(EditMethod.Rotate);
    }

    public void Edit3_brightness(View view) {
        Intent changeBrightness = new Intent(this, PicColorControlActivity.class);
        changeBrightness.putExtra("brightness_change_pic", imageUri.toString());
        startActivityForResult(changeBrightness, CHANGE_PICTURE);
    }

    public void Edit4_saturation(View view) {
        Intent changeSaturation = new Intent(this, PicColorControlActivity.class);
        changeSaturation.putExtra("saturation_change_pic", imageUri.toString());
        startActivityForResult(changeSaturation, CHANGE_PICTURE);
    }

    public void Edit5_contrasts(View view) {
        Intent changeSaturation = new Intent(this, PicColorControlActivity.class);
        changeSaturation.putExtra("contrast_change_pic", imageUri.toString());
        startActivityForResult(changeSaturation, CHANGE_PICTURE);
    }

    public void AiEdit1_StyleChange(View view) {
        Intent intent = new Intent(this, StyleChange.class);
        intent.putExtra("style_change_pic", imageUri.toString());
        startActivity(intent);
    }

    public void EditProcess(EditMethod editMethod) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "scale.jpg"));

        //////////////Uri destinationUri格式:file://*
        UCrop ucrop = UCrop.of(imageUri, destinationUri);
        ucrop = UcropConfig(ucrop, editMethod);

        ucrop.start(this);

    }

    private UCrop UcropConfig(UCrop uCrop, EditMethod mode) {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(true);

        switch (mode) {
            case Scale:
                options.setAllowedGestures(UCropActivity.NONE, UCropActivity.ALL, UCropActivity.ALL);
                options.setFreeStyleCropEnabled(true);
                break;
            case Rotate:
                options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);
                options.setFreeStyleCropEnabled(false);
                options.setCropGridColumnCount(0);
                options.setCropGridRowCount(0);
                options.setShowCropFrame(false);
                options.setToolbarTitle("旋转");

                break;

        }
        return uCrop.withOptions(options);
    }

    public void Add_Text(View view) {
        Intent intent = new Intent(PictureProcessActivity.this, Drawer.class);
        intent.putExtra("extra_photoadd", imageUri.toString());
        //  intent.putExtra("Height",imageView.getHeight());
        //   intent.putExtra("Height",imageView.getWidth());
        startActivity(intent);
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

    public void RestoreOrigin() {
        AiGroupSetVisibility(View.GONE);
        AddGroupSetVisibility(View.GONE);
        EditGroupSetVisibility(View.GONE);
        OtherButtonGroupSetVisibility(View.VISIBLE);
        backgroudforButton.setVisibility(View.GONE);
        buttonSelect = ButtonSelectType.None;
        SelectPhotoAppear("imageView_origin");
        SetButtonColor(buttonSelect);
    }

    private void SetButtonColor(ButtonSelectType button) {
        Button edit = findViewById(R.id.Editclick);
        Button ai = findViewById(R.id.AIclcik);
        Button add = findViewById(R.id.addMain);

        ColorStateList color = ColorStateList.valueOf(Color.parseColor("#008577"));
        edit.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        ai.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
        add.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));

        switch (button) {
            case Edit:
                edit.setCompoundDrawableTintList(color);
                break;
            case Ai:
                ai.setCompoundDrawableTintList(color);
                break;
            case Add:
                add.setCompoundDrawableTintList(color);
                break;
            case None:
                break;
        }
    }

    public void EditClick(View view) {

        switch (buttonSelect) {
            case Ai:
            case Add:
                SetButtonColor(ButtonSelectType.Edit);
                AiGroupSetVisibility(View.GONE);
                AddGroupSetVisibility(View.GONE);
                EditGroupSetVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Edit;
                SelectPhotoAppear("imageView_background");
                break;
            case Edit:
                SetButtonColor(ButtonSelectType.None);

                EditGroupSetVisibility(View.GONE);
                OtherButtonGroupSetVisibility(View.VISIBLE);
                backgroudforButton.setVisibility(View.GONE);
                buttonSelect = ButtonSelectType.None;
                SelectPhotoAppear("imageView_origin");
                break;
            case None:
                SetButtonColor(ButtonSelectType.Edit);
                EditGroupSetVisibility(View.VISIBLE);
                OtherButtonGroupSetVisibility(View.GONE);
                backgroudforButton.setVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Edit;
                SelectPhotoAppear("imageView_background");
                break;
        }
    }


    public void AiClick(View view) {
        switch (buttonSelect) {
            case Edit:
            case Add:
                SetButtonColor(ButtonSelectType.Ai);
                AddGroupSetVisibility(View.GONE);
                EditGroupSetVisibility(View.GONE);
                AiGroupSetVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Ai;
                SelectPhotoAppear("imageView_background");
                break;
            case Ai:
                AiGroupSetVisibility(View.GONE);
                SetButtonColor(ButtonSelectType.None);
                OtherButtonGroupSetVisibility(View.VISIBLE);
                backgroudforButton.setVisibility(View.GONE);
                buttonSelect = ButtonSelectType.None;
                SelectPhotoAppear("imageView_origin");
                break;
            case None:
                SetButtonColor(ButtonSelectType.Ai);
                AiGroupSetVisibility(View.VISIBLE);
                OtherButtonGroupSetVisibility(View.GONE);
                backgroudforButton.setVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Ai;
                SelectPhotoAppear("imageView_background");
                break;
        }
    }

    public void AddClick(View view) {
        switch (buttonSelect) {
            case Edit:
            case Ai:
                SetButtonColor(ButtonSelectType.Add);

                AiGroupSetVisibility(View.GONE);
                EditGroupSetVisibility(View.GONE);
                AddGroupSetVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Add;
                SelectPhotoAppear("imageView_background");
                break;
            case Add:
                SetButtonColor(ButtonSelectType.None);

                AddGroupSetVisibility(View.GONE);
                OtherButtonGroupSetVisibility(View.VISIBLE);
                backgroudforButton.setVisibility(View.GONE);
                buttonSelect = ButtonSelectType.None;
                SelectPhotoAppear("imageView_origin");
                break;
            case None:
                SetButtonColor(ButtonSelectType.Add);
                AddGroupSetVisibility(View.VISIBLE);
                OtherButtonGroupSetVisibility(View.GONE);
                backgroudforButton.setVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Add;
                SelectPhotoAppear("imageView_background");
                break;
        }
    }

    private void EditGroupSetVisibility(int visibility) {

        AnimationView.fade(Edit_Scale, visibility);
        AnimationView.fade(Edit_Rotate, visibility);
        AnimationView.fade(Edit_Saturation, visibility);
        AnimationView.fade(Edit_Contrast, visibility);
        AnimationView.fade(Edit_Brightness, visibility);
        AnimationView.fade(Edit_text, visibility);
//        Edit_Scale.setVisibility(visibility);
//        Edit_Rotate.setVisibility(visibility);
//        Edit_Saturation.setVisibility(visibility);
//        Edit_Contrast.setVisibility(visibility);
//        Edit_Brightness.setVisibility(visibility);
    }

    private void AddGroupSetVisibility(int visibility) {
        AnimationView.fade(addaccessories, visibility);
        AnimationView.fade(addphotoframe, visibility);
        AnimationView.fade(addtext, visibility);
        AnimationView.fade(add_text, visibility);
//        addaccessories.setVisibility(visibility);
//        addphotoframe.setVisibility(visibility);
//        addtext.setVisibility(visibility);
    }

    private void AiGroupSetVisibility(int visibility) {
//        highfraction.setVisibility(visibility);
//        Stylemigration.setVisibility(visibility);
        AnimationView.fade(highfraction, visibility);
        AnimationView.fade(Stylemigration, visibility);
        AnimationView.fade(AI_text, visibility);
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//            }
//        };
//        timer.schedule(timerTask, 600);

    }

    private void OtherButtonGroupSetVisibility(int visibility) {
        AnimationView.fade(homeButton, visibility);
        AnimationView.fade(doneButton, visibility);
//        homeButton.setVisibility(visibility);
//        doneButton.setVisibility(visibility);
    }

    private void SelectPhotoAppear(String SelectPic) {
        if (SelectPic == "imageView_background") {
            imageView_background.setVisibility(View.VISIBLE);
            imageView_background.setImageBitmap(fuzzyPhotoBmp);
            imageView_origin.setVisibility(View.GONE);
        } else if (SelectPic == "imageView_origin") {
            imageView_origin.setVisibility(View.VISIBLE);
            imageView_origin.setImageURI(imageUri);
            imageView_background.setVisibility(View.GONE);
        }
    }

    public void Add_Decorate(View view) {
        Intent intent = new Intent(PictureProcessActivity.this, AddDecorate.class);
        intent.putExtra("extra_decoradd", imageUri.toString());
        //  intent.putExtra("Height",imageView.getHeight());
        //   intent.putExtra("Height",imageView.getWidth());
        startActivity(intent);
    }

}
