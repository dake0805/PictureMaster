package com.nwpu.paim;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    private Button setting_about;

    private Button setting_contact;

    private Button setting_feedback;

    protected void onCreate(Bundle savaInstanceState) {
        super.onCreate(savaInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

        setting_about = (Button) findViewById(R.id.setting_about);
        setting_contact = (Button) findViewById(R.id.setting_contact);
        setting_feedback = (Button) findViewById(R.id.setting_feedback);

//        setting_about.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                builder.setTitle("保存照片");
//                builder.setMessage("是否确定要存储照片");
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
//
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                    }
//                });
//                builder.setNegativeButton("", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                    }
//                });
//                AlertDialog b = builder.create();
//                b.show();
//            }
//        });


//        setting_contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                final CSOKDialog okCancelDialog = CSOKDialog.createBuilder(Settings.this);
//                okCancelDialog.setMsg("Email:nebula225@protonmail.com\nQQ:645745123\nTel:13679285813");
//                //返回键是否关闭对话框  true 表示可以关闭
//                okCancelDialog.setCancelable(false);
//                //设置在窗口外空白处触摸时是否取消此对话框。 如果设置为true，则对话框设置为可取消
//                okCancelDialog.setCanceledOnTouchOutside(false);
//                okCancelDialog.show();
//            }
//        });

        setting_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedback = new Intent(Settings.this, FeedBackActivity.class);
                startActivity(feedback);
            }
        });
    }

    public void contact_Click(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("联系我们");
        String message = "https://github.com/dake0805/PictureMaster";
        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog b = builder.create();
        b.show();
    }

    public void about_Clcik(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("关于我们");
        String message = "PAIM® for android®  Trademarks Apple, App Store, Mac App Store, iBookstore, iPhone, and iPad are registered trademarks or service marks of Apple Inc. All other brand names, product names or trademarks belong to their respective holders.  Third-Party Software Credits and Attributions  AFNetworking, 2.5.3 Copyright 2013-2015, AFNetworking https://github.com/AFNetworking/AFNetworking/blob/master/LICENSE  UICKeyChainStore, 1.1.0 Copyright 2011 kishikawa katsumi https://github.com/kishikawakatsumi/UICKeyChainStore/blob/master/LICENSE  ReactiveCocoa, 2.4.2 Copyright 2012 - 2014, GitHub, Inc. https://github.com/ReactiveCocoa/ReactiveCocoa/blob/master/LICENSE.md  SMPageControl, 1.2 Copyright (C) 2012 by Spaceman Labs (http://www.spacemanlabs.com) https://github.com/Spaceman-Labs/SMPageControl/blob/master/LICENSE  DB5 Copyright (c) 2013 Q Branch LLC (http://qbranch.co/) https://github.com/quartermaster/DB5/blob/master/LICENSE  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.";

        builder.setMessage(message);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        AlertDialog b = builder.create();
        b.show();
    }

    public void language_Click(View view) {
        Intent intent = new Intent(Settings.this, language_setting.class);
        startActivity(intent);

    }

}
