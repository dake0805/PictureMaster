package com.example.myjavaapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ServerCommunication {

    public static void Upload(Context context, Uri Uri) {
        Uri uri = Uri;
        String Url = "http://http://127.0.0.1:8000/upload/servlet/upload";

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setTimeout(60 * 60 * 1000);

        String path = Photo.getPath(context,uri);

//        String path = getFilePathFromContentUri(uri,contentResolver);


        File testFile = new File(path);

        if(testFile.exists()){
            Log.d("test","exit");
        }else {
            Log.d("test","not exit");
        }

        RequestParams param = new RequestParams();
        try {
            param.put("file", testFile);
            httpClient.post(Url, param, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(String arg0) {
                    super.onSuccess(arg0);
                    Log.i("ck", "success>" + arg0);
                    if (arg0.equals("success")) {
                    }
                }

                @Override
                public void onFailure(Throwable arg0, String arg1) {
                    super.onFailure(arg0, arg1);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



}

