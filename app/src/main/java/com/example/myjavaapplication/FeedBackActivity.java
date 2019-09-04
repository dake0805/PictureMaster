package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FeedBackActivity extends AppCompatActivity {

    private Button btn1;

    private TextView textView;
    protected void onCreate(Bundle savaInstanceState){
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_feedback);
        btn1 = (Button)findViewById(R.id.feedback_accept);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CSOKDialog okCancelDialog = CSOKDialog.createBuilder(FeedBackActivity.this);
                textView = (TextView)findViewById(R.id.editText2) ;
                okCancelDialog.setMsg("Your message has been successfully sent!");
                //返回键是否关闭对话框  true 表示可以关闭
                okCancelDialog.setCancelable(false);
                //设置在窗口外空白处触摸时是否取消此对话框。 如果设置为true，则对话框设置为可取消
                okCancelDialog.setCanceledOnTouchOutside(false);
                okCancelDialog.show();
                textView.setVisibility(View.INVISIBLE);
            }
        });
    }
}
