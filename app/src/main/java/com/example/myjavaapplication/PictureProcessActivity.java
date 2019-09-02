package com.example.myjavaapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

public class PictureProcessActivity extends AppCompatActivity {

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
    private Button Edit1;
    private Button Edit2;

    //点击AI按钮弹出界面所需的4个变量
    private Button highfraction;
    private Button Stylemigration;
    private Button AI;

    //点击添加按钮弹出界面所需的5个变量
    private Button addtext;
    private Button addaccessories;
    private Button addphotoframe;
    private Button add;

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
        if (intent.getStringExtra("extra_uri") != null) {
            imageUri = Uri.parse(intent.getStringExtra("extra_uri"));
            //准备模糊化
            photoBmp = Photo.getBitmapFromUri(getApplicationContext(), this.getContentResolver(), imageUri);
            fuzzyPhotoBmp = FastBlur.doBlur(photoBmp, 20, false);
            imageView_origin.setImageURI(imageUri);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    imageUri = data.getData();
                    imageView_origin.setImageURI(imageUri);
                    //准备模糊化
                    photoBmp = Photo.getBitmapFromUri(getApplicationContext(), this.getContentResolver(), imageUri);
                    fuzzyPhotoBmp = FastBlur.doBlur(photoBmp, 20, false);
                    break;
                case UCrop.REQUEST_CROP:
                    imageUri = UCrop.getOutput(data);
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
        Edit1 = (Button) findViewById(R.id.scale);
        Edit2 = (Button) findViewById(R.id.rotate);

        highfraction = (Button) findViewById(R.id.highfraction);
        Stylemigration = (Button) findViewById(R.id.Stylemigration);
        AI = (Button) findViewById(R.id.AIclcik);

        addtext = (Button) findViewById(R.id.addtext);
        addaccessories = (Button) findViewById(R.id.addaccessories);
        addphotoframe = (Button) findViewById(R.id.addphotoframe);

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

    public void EditProcess(EditMethod editMethod) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "scale.jpg"));

        //////////////Uri destinationUri格式:file://*


        UCrop ucrop = UCrop.of(imageUri, destinationUri);
        ucrop = UcropConfig(ucrop, editMethod);

        ucrop.start(this);


    }

    private UCrop UcropConfig(UCrop uCrop, EditMethod mode) {
        UCrop.Options options = new UCrop.Options();
//        options.setFreeStyleCropEnabled(true);
        options.setHideBottomControls(true);

//        options.setToolbarColor(Color.GREEN);
//        options.setActiveWidgetColor(Color.GREEN);
//        options.setCropFrameColor(Color.GREEN);
//        options.setStatusBarColor(Color.GREEN);
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

    public void DoneClick(View view) {
        Intent intent = new Intent(PictureProcessActivity.this, PhotoResultActivity.class);
        intent.putExtra("extra_resultUri", imageUri.toString());
        startActivity(intent);
    }

    public void HomeClick(View view) {
        Intent intent = new Intent(PictureProcessActivity.this, MainActivity.class);
        startActivity(intent);
    }

//    private UCrop UcropConfig(UCrop uCrop) {
////        //select button 绑定
////        public void SelectPhoto_Pre (View view){
////            Intent selectPhoto = new Intent();
////            selectPhoto.setAction(Intent.ACTION_PICK);
////            selectPhoto.setType("image/*");
////            startActivityForResult(selectPhoto, CHOOSE_PICTURE);
//        }
//    }

    public void RestoreOrigin() {
        AiGroupSetVisibility(View.GONE);
        AddGroupSetVisibility(View.GONE);
        EditGroupSetVisibility(View.GONE);
        OtherButtonGroupSetVisibility(View.VISIBLE);
        backgroudforButton.setVisibility(View.GONE);
        buttonSelect = ButtonSelectType.None;
        SelectPhotoAppear("imageView_origin");
    }

    public void EditClick(View view) {
        switch (buttonSelect) {
            case Ai:
            case Add:
                AiGroupSetVisibility(View.GONE);
                AddGroupSetVisibility(View.GONE);
                EditGroupSetVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Edit;
                SelectPhotoAppear("imageView_background");
                break;
            case Edit:
                EditGroupSetVisibility(View.GONE);
                OtherButtonGroupSetVisibility(View.VISIBLE);
                backgroudforButton.setVisibility(View.GONE);
                buttonSelect = ButtonSelectType.None;
                SelectPhotoAppear("imageView_origin");
                break;
            case None:
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
                AddGroupSetVisibility(View.GONE);
                EditGroupSetVisibility(View.GONE);
                AiGroupSetVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Ai;
                SelectPhotoAppear("imageView_background");
                break;
            case Ai:
                AiGroupSetVisibility(View.GONE);
                OtherButtonGroupSetVisibility(View.VISIBLE);
                backgroudforButton.setVisibility(View.GONE);
                buttonSelect = ButtonSelectType.None;
                SelectPhotoAppear("imageView_origin");
                break;
            case None:
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
                AiGroupSetVisibility(View.GONE);
                EditGroupSetVisibility(View.GONE);
                AddGroupSetVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Add;
                SelectPhotoAppear("imageView_background");
                break;
            case Add:
                AddGroupSetVisibility(View.GONE);
                OtherButtonGroupSetVisibility(View.VISIBLE);
                backgroudforButton.setVisibility(View.GONE);
                buttonSelect = ButtonSelectType.None;
                SelectPhotoAppear("imageView_origin");
                break;
            case None:
                AddGroupSetVisibility(View.VISIBLE);
                OtherButtonGroupSetVisibility(View.GONE);
                backgroudforButton.setVisibility(View.VISIBLE);
                buttonSelect = ButtonSelectType.Add;
                SelectPhotoAppear("imageView_background");
                break;
        }
    }

    private void EditGroupSetVisibility(int visibility) {
        Edit1.setVisibility(visibility);
        Edit2.setVisibility(visibility);
    }

    private void AddGroupSetVisibility(int visibility) {
        addaccessories.setVisibility(visibility);
        addphotoframe.setVisibility(visibility);
        addtext.setVisibility(visibility);
    }

    private void AiGroupSetVisibility(int visibility) {
        highfraction.setVisibility(visibility);
        Stylemigration.setVisibility(visibility);
    }

    private void OtherButtonGroupSetVisibility(int visibility) {
        homeButton.setVisibility(visibility);
        doneButton.setVisibility(visibility);
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

//
//    /**
//     * 当点击beauty按钮时，
//     * 如果isbeautybutton=true，
//     * add相关的控件消失在屏幕上，之后置isaddbutton为true
//     * beauty相关的控件显示在屏幕上，之后置isbeautybutton为false
//     * 其余两个按钮消失，之后置isthetwoon为true
//     *
//     * 如果isbeautybutton=false，beauty相关的控件不显示在屏幕上，之后置isbeautybutton为true
//     * 其余两个按钮出现，之后置isthetwoon为false
//     */
//    public void btn_onclickofbeauty(View view){
//        backgroudforbrauty=(TextView)findViewById(R.id.backgroundforbeauty);
//        highfraction=(Button)findViewById(R.id.highfraction);
//        Stylemigration=(Button)findViewById(R.id.Stylemigration);
//        backgroudforadd=(TextView)findViewById(R.id.backgroundforadd);
//        addtext=(Button)findViewById(R.id.addtext);
//        addaccessories=(Button)findViewById(R.id.addaccessories);
//        addphotoframe=(Button)findViewById(R.id.addphotoframe);
//        Home=(Button)findViewById(R.id.homebutton);
//
//        if(isbeautybutton){
//            backgroudforadd.setVisibility(View.GONE);
//            addtext.setVisibility(View.GONE);
//            addaccessories.setVisibility(View.GONE);
//            addphotoframe.setVisibility(View.GONE);
//            isaddbutton=true;
//            backgroudforbrauty.setVisibility(View.VISIBLE);
//            highfraction.setVisibility(View.VISIBLE);
//            Stylemigration.setVisibility(View.VISIBLE);
//            isbeautybutton=false;
//            Home.setVisibility(View.GONE);
//            isthetwoon=true;
//        }
//        else{
//            backgroudforbrauty.setVisibility(View.GONE);
//            highfraction.setVisibility(View.GONE);
//            Stylemigration.setVisibility(View.GONE);
//            isbeautybutton=true;
//            Home.setVisibility(View.VISIBLE);
//            isthetwoon=false;
//            imageView_background.setImageBitmap(fuzzyPhotoBmp);
//            imageView.setImageURI(null);
//
//        }
//
//    }
//
//    /**
//     * 当点击add按钮时，
//     * 如果isaddbutton=true，
//     * beauty相关的控件消失在屏幕上，之后置isbeautybutton为true
//     * add相关的控件显示在屏幕上，之后置isaddbutton为false
//     *
//     * 如果isaddbutton=false，add相关的控件不显示在屏幕上，之后置isaddbutton为true
//     */
//
//    public void btn_onclickofadd(View view){
//        backgroudforadd=(TextView)findViewById(R.id.backgroundforadd);
//        addtext=(Button)findViewById(R.id.addtext);
//        addaccessories=(Button)findViewById(R.id.addaccessories);
//        addphotoframe=(Button)findViewById(R.id.addphotoframe);
//        backgroudforbrauty=(TextView)findViewById(R.id.backgroundforbeauty);
//        highfraction=(Button)findViewById(R.id.highfraction);
//        Stylemigration=(Button)findViewById(R.id.Stylemigration);
//        Home=(Button)findViewById(R.id.homebutton);
//
//        if(isaddbutton){
//            backgroudforbrauty.setVisibility(View.GONE);
//            highfraction.setVisibility(View.GONE);
//            Stylemigration.setVisibility(View.GONE);
//            isbeautybutton=true;
//            backgroudforadd.setVisibility(View.VISIBLE);
//            addtext.setVisibility(View.VISIBLE);
//            addaccessories.setVisibility(View.VISIBLE);
//            addphotoframe.setVisibility(View.VISIBLE);
//            isaddbutton=false;
//            Home.setVisibility(View.GONE);
//            isthetwoon=true;
//
//        }
//        else{
//            backgroudforadd.setVisibility(View.GONE);
//            addtext.setVisibility(View.GONE);
//            addaccessories.setVisibility(View.GONE);
//            addphotoframe.setVisibility(View.GONE);
//            isaddbutton=true;
//            Home.setVisibility(View.VISIBLE);
//            isthetwoon=false;
//        }
//    }
}
