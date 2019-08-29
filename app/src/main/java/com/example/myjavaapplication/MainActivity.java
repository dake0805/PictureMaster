package com.example.myjavaapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.roger.gifloadinglibrary.GifLoadingView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * to do
 * 存储空间权限申请
 * 保存图片 done
 * 明天带学生证   done
 */


public class MainActivity extends AppCompatActivity {

    private static final int CHOOSE_PICTURE = 1;
    //    private static final int CROP_PICTURE = 2;
    private static final int CAMERA_PICTURE = 3;

    private File photoFile;

    //private ImageView cropImage;

    private Uri imageCurrent;

    private ImageView imageView;
    private ImageView cropImage ;
    private Uri originImageUri;
    private Uri destinationUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //cropImage = findViewById(R.id.imageView);
        //GifLoadingView mGifLoadingView = new GifLoadingView();
        imageView = findViewById(R.id.imageView);
        cropImage = findViewById(R.id.imageView2);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    //分享函数
    public void SharePic(){
        Intent shareImageIntent = new Intent(Intent.ACTION_SEND);
        shareImageIntent.putExtra(Intent.EXTRA_STREAM,originImageUri);
        shareImageIntent.setType("image/*");
        startActivity(Intent.createChooser(shareImageIntent,"分享到"));
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.share:
                SharePic();
            default:break;
        }
        return true;
    }

    //TODO 按钮的图片设置与排版
    //Button SELECT
    public void selectPhoto(View view) {
        Intent selectPhoto = new Intent();
        selectPhoto.setAction(Intent.ACTION_PICK);
        selectPhoto.setType("image/*");
        startActivityForResult(selectPhoto, CHOOSE_PICTURE);
    }

    //Button CAMERA

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    originImageUri = data.getData();
//                    //分享
//                    Intent shareImageIntent = new Intent(Intent.ACTION_SEND);
//                    shareImageIntent.putExtra(Intent.EXTRA_STREAM,originImageUri);
//                    shareImageIntent.setType("image/*");
//                    startActivity(Intent.createChooser(shareImageIntent,"分享到"));
                    CropPhoto(originImageUri);
                    break;
                case CAMERA_PICTURE:
                    PhotoCropInCamera();
                    break;
                case UCrop.REQUEST_CROP:
                    Uri cropPhoto = UCrop.getOutput(data);          //得到的裁剪结果URI
                    cropImage.setImageURI(cropPhoto);
                    break;

            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    //处理照片
    private void CropPhoto(Uri originPhoto) {
        destinationUri = Uri.fromFile(new File(getCacheDir(), "test.jpg"));

        UCrop.of(originPhoto, destinationUri)
                .withMaxResultSize(1920, 1080)
                .start(this);
    }

    public void PhotoCropInCamera() {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "test.jpg"));

        Uri originPhoto = Uri.fromFile(photoFile);
        CropPhoto(originPhoto);
    }

    //CAMERA button 绑定函数
    public void cameraShotPhoto(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, CAMERA_PICTURE);
//        }
        Uri cameraPhoto;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraPhoto = FileProvider.getUriForFile(this,
                        "com.example.myjavaapplication",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhoto);
                startActivityForResult(takePictureIntent, CAMERA_PICTURE);
            }
        }
    }

    public void jointPhoto(View view) {
        Intent intent = new Intent(MainActivity.this, JointPhotoActivity.class);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //获得的Uri

            Uri ImageUri;
            Intent sendPicUriIntent = new Intent(MainActivity.this, PictureProcessActivity.class);
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    ImageUri = data.getData();
                    sendPicUriIntent.putExtra("extra_uri", ImageUri.toString());
                    startActivity(sendPicUriIntent);
                    //CropPhoto(originImageUri);
                    break;
                case CAMERA_PICTURE:
                    ImageUri = PhotoCropInCamera();
                    sendPicUriIntent.putExtra("extra_uri", ImageUri.toString());
                    startActivity(sendPicUriIntent);
                    break;
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }


    private void CropPhoto(Uri originPhoto) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "test.jpg"));

        UCrop.of(originPhoto, destinationUri)
                .withMaxResultSize(1920, 1080)
                .start(this);
    }

    public Uri PhotoCropInCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        /**
         * 注意修改文件名
         */
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), timeStamp + "test.jpg"));

        //获得Uri
        Uri originPhoto = Uri.fromFile(photoFile);
        //CropPhoto(originPhoto);
        return originPhoto;
    }


    //拍照前创建photo文件，在/mnt/sdcard/DCIM下
    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera/Test");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoFile = image;
        return image;
    }

    //CLEAR button绑定函数
    public void ClearPhoto(View view) {
        ImageView imageView = findViewById(R.id.imageView);
        ImageView imageView2 = findViewById(R.id.imageView2);
        imageView.setImageURI(null);
        imageView2.setImageURI(null);
    }


}
