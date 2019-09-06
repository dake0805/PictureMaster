package com.example.myjavaapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class FeedBackActivity extends AppCompatActivity {

    public Button feedback_accept_btn;

    public TextView textView;

    protected void onCreate(Bundle savaInstanceState) {
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.feedBack_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });


        feedback_accept_btn = (Button) findViewById(R.id.feedback_accept);
//        feedback_accept_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final CSOKDialog okCancelDialog = CSOKDialog.createBuilder(FeedBackActivity.this);
//                textView = (TextView)findViewById(R.id.editText2) ;
//                okCancelDialog.setMsg("Your message has been successfully sent!");
//                //返回键是否关闭对话框  true 表示可以关闭
//                okCancelDialog.setCancelable(false);
//                //设置在窗口外空白处触摸时是否取消此对话框。 如果设置为true，则对话框设置为可取消
//                okCancelDialog.setCanceledOnTouchOutside(false);
//                okCancelDialog.show();
//                textView.setVisibility(View.INVISIBLE);
//            }
//        });
    }

    public void FeedbackClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("联系我们");
        builder.setMessage("你已经成功发送");
        final TextView textView = (TextView) findViewById(R.id.editText2);
        textView.setVisibility(View.GONE);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                textView.setVisibility(View.VISIBLE);
            }
        });
        AlertDialog b = builder.create();
        b.show();
        String text = textView.getText().toString();
        textView.setText("");
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:1072505283@qq.com"));
        data.putExtra(Intent.EXTRA_SUBJECT, "反馈");
        data.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(data);
    }
}