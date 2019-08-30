package com.example.myjavaapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//实现下载图片的功能
public class DownloadActivity extends AppCompatActivity {
    protected static final int CHANGE_UI = 1;//下载成功
    protected static final int ERROR = 2;//下载出错

    private EditText et_path;//用于获取用户输入的url
    private ImageView iv_pic;//用于显示图片

    //主线程创建消息处理器
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==CHANGE_UI){
                Bitmap bitmap = (Bitmap)msg.obj;
                iv_pic.setImageBitmap(bitmap);
            }else if(msg.what==ERROR){
                Toast.makeText(DownloadActivity.this, "图片显示错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        //初始化控件
        et_path = (EditText) findViewById(R.id.et_path);
        iv_pic = (ImageView)findViewById(R.id.iv_pic);
    }

    //处理按钮事件
    public  void OnClick(View view){
        final String path = et_path.getText().toString();
        if(TextUtils.isEmpty(path)){
            Toast.makeText(this, "图片路径不能为空", Toast.LENGTH_SHORT).show();
        }else{
            new Thread() {  //创建新线程下载图片
                private HttpURLConnection connection;
                private Bitmap bitmap;
                public void run() {
                    try {
                        URL url = new URL(path);
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
                    } catch(Exception e){
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
