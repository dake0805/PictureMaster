package com.example.myjavaapplication;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoResultActivity extends AppCompatActivity {

    private ImageView imageView_background;
    private Uri originImageUri;
    private Bitmap photoBmp;
    private Bitmap fuzzyPhotoBmp;

    //private Button setBackground;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_result);

        imageView_background = (ImageView) findViewById(R.id.background_result);
        Intent intent = getIntent();

        if(intent.getStringExtra("extra_resultUri")!=null){
            originImageUri = Uri.parse(intent.getStringExtra("extra_resultUri"));
            photoBmp = Photo.getBitmapFromUri(getApplicationContext(),this.getContentResolver(),originImageUri);
            fuzzyPhotoBmp = FastBlur.doBlur(photoBmp,40,false);
            //fuzzyPhotoBmp = FastBlur.doBlur(Photo.scaleBitmap(photoBmp,1,1),20,false);
            imageView_background.setImageBitmap(fuzzyPhotoBmp);
        }
    }

    //uri转bitmap 可以用来避免旋转
    private Bitmap getBitmapFromUri(Uri uri) {
        //获取路径
        String  photoPath = FastBlur.getPath(getApplicationContext(),uri);
        //通过路径判断被旋转的角度
        int degrees = FastBlur.getBitmapDegree(photoPath);
        Bitmap bitmap;
        try {
            // 读取uri所在的图片
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
        //再旋转
        bitmap = FastBlur.rotateBitmap(bitmap,degrees);
        return bitmap;
    }

    public void close_click(View view) {
        finish();
    }

    //share button 绑定
    public void SharePhoto(View view) {
        if (originImageUri != null) {
            //   Log.d("uri",imageUri.toString());
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, originImageUri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "分享到"));
        } else {
            Log.d("test", "uri not exit");
        }

    }

    //set wallpaper button 绑定
    public void SetWallpaperClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置壁纸");
        builder.setMessage("是否确定要设置壁纸");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                SetWallpaper();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog b = builder.create();
        b.show();
    }

    public void SetWallpaper() {
        final WallpaperManager wpManager = WallpaperManager.getInstance(this);
        try {
            //wpManager.setResource(R.id.imageView); //墙纸
            InputStream in = getContentResolver().openInputStream(originImageUri);
            wpManager.setStream(in);
            in.close();
            Toast.makeText(PhotoResultActivity.this, "更换壁纸成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //save button 绑定
    public void SavePhoto(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("保存照片");
        builder.setMessage("是否确定要存储照片");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    SavePhotoInStorage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
        InputStream in = getContentResolver().openInputStream(originImageUri);
        OutputStream out = new FileOutputStream(new File(image.getPath()));
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        Toast.makeText(getApplicationContext(), "保存图片成功", Toast.LENGTH_SHORT).show();
        out.close();
        in.close();
    }
}
