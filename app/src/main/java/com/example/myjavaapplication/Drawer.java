package com.example.myjavaapplication;


/*
* 添加功能的实现
* 名字起的有问题
* @hrncool
* */


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;



public class Drawer extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);


        Button Button_AddText = (Button)findViewById(R.id.Edit_button);
        final EditText EditText_1 = (EditText)findViewById(R.id.Edit_Text1) ;
        Button_AddText.setOnClickListener(this);
        }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Edit_button:
                Intent intent = new Intent(Drawer.this,TextEdit.class);
                startActivity(intent);

                //EditText EditText_1 = (EditText)findViewById(R.id.Edit_Text1) ;

                //EditText_1.requestFocus();
                //InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
               //imm.showSoftInput(EditText_1, InputMethodManager.SHOW_IMPLICIT);

                break;


            case R.id.Decorate_Button:

                break;

            default:
        }

        /*

        edit.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);*/

    }





}

