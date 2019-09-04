package com.example.myjavaapplication;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CSOKDialog extends Dialog{
    private Context context = null;

    private TextView msgText;
    private TextView titleText;
    private Button okBtn;

    public static CSOKDialog createBuilder(Context context) {
        return new CSOKDialog(context);
    }

    public CSOKDialog(Context context) {
        this(context, R.style.Theme_AppCompat_Dialog);
        this.context = context;
    }

    public CSOKDialog(Context context, boolean cancelable,
                      OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public CSOKDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        setDialogContentView();
    }

    private void setDialogContentView() {
        View view = LayoutInflater.from(context).inflate(R.layout.showdialog_ok, null);
        titleText = (TextView) view.findViewById(R.id.title_text);
        msgText = (TextView) view.findViewById(R.id.msg_text);
        okBtn = (Button) view.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.e("TAG","okBtn21");
                CSOKDialog.this.dismiss();
            }
        });
        setContentView(view);
    }

    public CSOKDialog setMsg(String msg) {
        if (null != msgText) {
            msgText.setText(msg);
        }
        return this;
    }

    public CSOKDialog setMsg(int resId) {
        if (null != msgText) {
            msgText.setText(context.getString(resId));
        }
        return this;
    }


    public CSOKDialog setAlertTitle(String t) {
        if (null != titleText) {
            titleText.setText(t);
        }
        return this;
    }


    public CSOKDialog setAlertTitle(int resId) {
        if (null != titleText) {
            titleText.setText(context.getString(resId));
        }
        return this;
    }

}
