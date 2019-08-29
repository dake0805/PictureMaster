package com.example.myjavaapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
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

    private ImageView imageView;
    private Uri imageUri;
    private Button shareButton;
    protected void onCreate(Bundle savaInstanceState){
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_photo_result);
        imageView = findViewById(R.id.imageResult);
        Intent intent = getIntent();
        shareButton = (Button)findViewById(R.id.share);

        if(intent.getStringExtra("extra_uri")!=null)
        {
        imageUri = Uri.parse(intent.getStringExtra("extra_uri"));
            imageView.setImageURI(null);
            imageView.setImageURI(imageUri);
        }

        Button.OnClickListener control = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.share:
                        SharePhoto();
                        break;
                }
            }
        };
    }

    public void SavePhoto(View view) {
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

    public void SharePhoto(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent,"分享到"));
    }

    public void SetWallpaper(View view){

    }
}
