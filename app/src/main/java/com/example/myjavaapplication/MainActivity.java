package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * to do
 * 存储空间权限申请 done
 * 保存图片 done
 * 明天带学生证   done
 * onNewIntent是什么
 */


public class MainActivity extends AppCompatActivity {

    public static final int CHOOSE_PICTURE = 1;
    //    private static final int CROP_PICTURE = 2;
    private static final int CAMERA_PICTURE = 3;


    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 7;

    private File photoFile;

    //private ImageView cropImage;

    private Uri imageCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckPermission();
        //cropImage = findViewById(R.id.imageView);
        //GifLoadingView mGifLoadingView = new GifLoadingView();
    }

    //检查权限
    private void CheckPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        }
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
    public void cameraShotPhoto(View view) {
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


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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
                    ImageUri = getCameraPhotoUri();
                    sendPicUriIntent.putExtra("extra_uri", ImageUri.toString());
                    startActivity(sendPicUriIntent);
                    break;
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public Uri getCameraPhotoUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        /**
         * 注意修改文件名
         */
//        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), timeStamp + "test.jpg"));

        //获得Uri
        Uri originPhoto = null;
        //CropPhoto(originPhoto);
        if (photoFile != null) {
            originPhoto = FileProvider.getUriForFile(this,
                    "com.example.myjavaapplication",
                    photoFile);
        }

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
        photoFile = image;
        return image;
    }


}

