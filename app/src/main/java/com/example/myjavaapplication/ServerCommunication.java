package com.example.myjavaapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.commons.httpclient.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ServerCommunication {

    protected static final int CHANGE_UI = 1;//下载成功
    protected static final int ERROR = 2;//下载出错

    public ServerCommunication() {

    }


    public static void Upload(Context context, Uri Uri, String type) throws Exception {
        Uri uri = Uri;
        String Url = "http://192.168.188.106:8080/PictureMasterServer_war/PictureMasterServlet";

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.setTimeout(60 * 60 * 1000);

        String path = Photo.getPath(context, uri);

//        String path = getFilePathFromContentUri(uri,contentResolver);


        File testFile = new File(path);
        final Map<String, Object> paramMap = new HashMap<String, Object>(); //文本数据全部添加到Map里
//        paramMap.put("file", testFile);
//        paramMap.put("text", type);
//        if(testFile!=null) {
//            HttpConnectionUtil.doPostPicture(Url, paramMap, testFile);
//        }
        //
        if (testFile.exists()) {
            Log.d("test", "exit");
        } else {
            Log.d("test", "not exit");
        }

        RequestParams param = new RequestParams();
        try {
//            param.put("Content-Type","multipart/form-data");
            param.put("file", testFile);
            param.put("convert_type", type);
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

                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    if (statusCode == 200) {
                        Log.d("test", "ok");
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

    //主线程创建消息处理器
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void Download(String picName, String type) {
//        final  String path ="http://192.168.188.106:8080/PictureMasterServer_war/output_imgs/" + picName +"_" + type +".jpg";
        String needAdd = null;
        String path = null;
        http:
//192.168.188.106:8080/PictureMasterServer_war/output_imgs/style_cezanne_pretrained/test_latest/images/Death-Valley-Sunset-Dunes_fake.png
        if (type.equals("ESRGAN")) {
            needAdd = "esrgan";
            path = "http://192.168.188.106:8080/PictureMasterServer_war/output_imgs/";
        } else if (type.equals("CartoonGAN_Hayao")) {
            needAdd = "Hayao";
            path = "http://192.168.188.106:8080/PictureMasterServer_war/output_imgs/";
        } else if (type.equals("CartoonGAN_Hosoda")) {
            needAdd = "Hosoda";
            path = "http://192.168.188.106:8080/PictureMasterServer_war/output_imgs/";
        } else if (type.equals("CycleGAN_Cezanne")) {
            needAdd = "fake";
            path = "http://192.168.188.106:8080/PictureMasterServer_war/output_imgs/style_cezanne_pretrained/test_latest/images/";
        } else if (type.equals("CycleGAN_Monet")) {
            needAdd = "fake";
            path = "http://192.168.188.106:8080/PictureMasterServer_war/output_imgs/style_monet_pretrained/test_latest/images/";
        } else if (type.equals("CycleGAN_Ukiyoe")) {
            needAdd = "fake";
            path = "http://192.168.188.106:8080/PictureMasterServer_war/output_imgs/style_ukiyoe_pretrained/test_latest/images/";
        } else if (type.equals("CycleGAN_Vangogh")) {
            needAdd = "fake";
            path = "http://192.168.188.106:8080/PictureMasterServer_war/output_imgs/style_vangogh_pretrained/test_latest/images/";
        }
        picName = picName.replaceFirst(".png", "_" + needAdd + ".png");
        picName = picName.replaceFirst(".jpg", "_" + needAdd + ".jpg");

        path = path + picName;
        final String processPath = path;
        System.out.println("test1y");
        if (TextUtils.isEmpty(path)) {

        } else {
            new Thread() {  //创建新线程下载图片
                private HttpURLConnection connection;
                private Bitmap bitmap;

                public void run() {
                    System.out.println("testest");
                    try {
                        URL url = new URL(processPath);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        int code = connection.getResponseCode();
                        if (code == 200) {
                            InputStream inputStream = connection.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            Message msg = new Message();    //利用消息机制告知主线程下载结果，下同
                            msg.what = CHANGE_UI;
                            msg.obj = bitmap;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = ERROR;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = ERROR;
                        handler.sendMessage(msg);
                    }
                }
            }.start();
        }
    }


}

