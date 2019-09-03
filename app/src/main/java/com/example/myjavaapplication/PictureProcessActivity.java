package com.example.myjavaapplication;

import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.yalantis.ucrop.UCrop;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_process);
        //刚开始按钮隐藏
        doneButton = (Button) findViewById(R.id.done_button);
        doneButton.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.imageView_process);
        Intent intent = getIntent();
        imageUri =Uri.parse(intent.getStringExtra("extra_uri"));
        imageView.setImageURI(imageUri);


        //hrn测试用
        final Button button9=(Button) findViewById(R.id.add);
        button9.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent intent = new Intent(PictureProcessActivity.this,Drawer.class);
                intent.putExtra("extra_photoadd",imageUri.toString());
                intent.putExtra("Height",imageView.getHeight());
                intent.putExtra("Height",imageView.getWidth());
                startActivity(intent);
            }
        });


        if(intent.getStringExtra("extra_uri")!=null)
        {
            imageUri = Uri.parse(intent.getStringExtra("extra_uri"));
            imageView.setImageURI(imageUri);
        }
        else
        {
            //imageView.setImageResource(R.mipmap.joint_test);
        }
    }

    protected void OnResume() {
        imageView.setImageURI(null);
        imageView.setImageURI(imageUri);
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

                    try {
                        imageUri = Uri.parse(
                                android.provider.MediaStore.Images.Media.insertImage(
                                        getContentResolver(),
                                        tempFile.getAbsolutePath(), null, null));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageURI(null);
                    imageView.setImageURI(imageUri);
                    doneButton.setVisibility(View.VISIBLE);
                    break;
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void EditClick(View view) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "test.jpg"));

        //////////////Uri destinationUri格式:file://*

        UCrop.of(imageUri, destinationUri)
                .withMaxResultSize(1920, 1080)
                .start(this);
    }

    public void DoneClick(View view) {
        Intent intent = new Intent(PictureProcessActivity.this, PhotoResultActivity.class);
        intent.putExtra("extra_resultUri", imageUri);
        startActivity(intent);
    }

    //select button 绑定
    public void SelectPhoto_Pre(View view) {
        Intent selectPhoto = new Intent();
        selectPhoto.setAction(Intent.ACTION_PICK);
        selectPhoto.setType("image/*");
        startActivityForResult(selectPhoto, CHOOSE_PICTURE);
    }

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

    //share button 绑定
    public void SharePhoto(View view) {
        if (imageUri != null) {
            //   Log.d("uri",imageUri.toString());
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "分享到"));
        } else {
            Log.d("test", "uri not exit");
        }

    }


    //set wallpaper button 绑定
    public void SetWallpaper(View view) {
        final WallpaperManager wpManager = WallpaperManager.getInstance(this);
        try {
            //wpManager.setResource(R.id.imageView); //墙纸
            InputStream in = getContentResolver().openInputStream(imageUri);
            wpManager.setStream(in);
            in.close();
            Toast.makeText(PictureProcessActivity.this, "更换壁纸成功", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        InputStream in = getContentResolver().openInputStream(imageUri);
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
