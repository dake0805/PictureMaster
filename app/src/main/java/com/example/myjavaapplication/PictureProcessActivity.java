package com.example.myjavaapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureProcessActivity extends AppCompatActivity {

    private ImageView imageView;
    private Uri imageUri;


    protected void onCreate(Bundle savaInstanceState) {
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_pic_process);
        imageView = findViewById(R.id.imageView);
        Intent intent = getIntent();
        imageUri = Uri.parse(intent.getStringExtra("extra_uri"));
        imageView.setImageURI(imageUri);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UCrop.REQUEST_CROP:
                    Uri cropPhoto = UCrop.getOutput(data);          //得到的裁剪结果URI
                    Intent intent = new Intent(PictureProcessActivity.this, PhotoResultActivity.class);
                    intent.putExtra("extra_uri", imageUri.toString());
                    startActivity(intent);
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    public void EditClick(View view) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "test.jpg"));
        UCrop.of(imageUri, destinationUri)
                .withMaxResultSize(1920, 1080)
                .start(this);
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
