package com.nwpu.paim;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitmapToUri extends AppCompatActivity {


    public Uri BitmapToUri(Bitmap bitmap, Context context) throws IOException {


        Uri finishUri;

        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmpName = df.format(day);
        File finishFile = new File(context.getCacheDir(), tmpName+".jpg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();

        FileOutputStream fileOutputStream = new FileOutputStream(finishFile);
        fileOutputStream.write(bitmapData);
        fileOutputStream.flush();
        fileOutputStream.close();

        finishUri = Uri.fromFile(finishFile);
        return finishUri;

//        Uri finishUri;
//
//        File finishFile = new File(context.getCacheDir(), "tmp_bitmap.jpg");
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        byte[] bitmapData = bytes.toByteArray();
//
//        FileOutputStream fileOutputStream = new FileOutputStream(finishFile);
//        fileOutputStream.write(bitmapData);
//        fileOutputStream.flush();
//        fileOutputStream.close();
//
//        finishUri = Uri.fromFile(finishFile);
//        return finishUri;
    }
}