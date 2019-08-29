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
 * 明天带学生证
 */


public class MainActivity extends AppCompatActivity {

    private static final int CHOOSE_PICTURE = 1;
    //    private static final int CROP_PICTURE = 2;
    private static final int CAMERA_PICTURE = 3;

    private File photoFile;

    //private ImageView cropImage;

    private Uri imageCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //cropImage = findViewById(R.id.imageView);
        //GifLoadingView mGifLoadingView = new GifLoadingView();
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
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, CAMERA_PICTURE);
//        }
        //test hrn
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

    public void jointPhoto(View view){
        Intent intent = new Intent(MainActivity.this,JointPhotoActivity.class);
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //获得的Uri
            Uri ImageUri;
            Intent sendPicUriIntent = new Intent(MainActivity.this,PictureProcessActivity.class);
            switch (requestCode) {
                case CHOOSE_PICTURE:
                    ImageUri = data.getData();
                    sendPicUriIntent.putExtra("extra_uri",ImageUri.toString());
                    startActivity(sendPicUriIntent);
                    //CropPhoto(originImageUri);
                    break;
                case CAMERA_PICTURE:
                    ImageUri = PhotoCropInCamera();
                    sendPicUriIntent.putExtra("extra_uri",ImageUri.toString());
                    startActivity(sendPicUriIntent);
                    break;
//                case UCrop.REQUEST_CROP:
//                    Uri cropPhoto = UCrop.getOutput(data);          //得到的裁剪结果URI
//                    imageCurrent = cropPhoto;
//                    cropImage.setImageURI(null);                //刷新ImageView
//                    cropImage.setImageURI(cropPhoto);
//                    break;
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

//    public void ClearPhoto(View view) {
//        cropImage.setImageURI(null);
//    }

    public void SavePhoto(View view) {
//        ImageView imageView = findViewById(R.id.imageView);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Photo");
        builder.setMessage("test");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_SHORT).show();

                try {
                    SavePhotoInStorage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog b = builder.create();
        b.show();

    }


    /**
     * 保存图片到外部存储    /storage/0/Picture/Save
     * 使用文件输入输出流
     *
     * @throws IOException
     */
    private void SavePhotoInStorage() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Save");
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents

        InputStream in = getContentResolver().openInputStream(imageCurrent);
        OutputStream out = new FileOutputStream(new File(image.getPath()));
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.close();
        in.close();
    }

}

