package com.example.myjavaapplication;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyDialog extends Dialog {
    private Context context = null;

    private TextView msgText;
    private TextView titleText;
    private Button okBtn;

    public static CSOKDialog createBuilder(Context context) {
        return new CSOKDialog(context);
    }

    public MyDialog(Context context) {
        this(context, R.style.Theme_AppCompat_Dialog);
        this.context = context;
    }

    public MyDialog(Context context, boolean cancelable,
                      OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        setDialogContentView();
    }

    private void setDialogContentView() {
        View view = LayoutInflater.from(context).inflate(R.layout.showdialog_feed, null);
        titleText = (TextView) view.findViewById(R.id.feed_title);
        msgText = (TextView) view.findViewById(R.id.feed_back);
        okBtn = (Button) view.findViewById(R.id.feed_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("TAG","okBtn21");
                MyDialog.this.dismiss();
            }
        });
        setContentView(view);
    }

    public MyDialog setMsg(String msg) {
        if (null != msgText) {
            msgText.setText(msg);
        }
        return this;
    }

    public MyDialog setMsg(int resId) {
        if (null != msgText) {
            msgText.setText(context.getString(resId));
        }
        return this;
    }


    public MyDialog setAlertTitle(String t) {
        if (null != titleText) {
            titleText.setText(t);
        }
        return this;
    }


    public MyDialog setAlertTitle(int resId) {
        if (null != titleText) {
            titleText.setText(context.getString(resId));
        }
        return this;
    }

}
