package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

/*
 * 文字输入页面
 * */

public class TextEdit extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_edit);

        Button finsh_botton = (Button)findViewById(R.id.Finsh_Button);

        finsh_botton.setOnClickListener(this);




    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.Finsh_Button:
                EditText EditText_1 = (EditText)findViewById(R.id.Edit_Text1) ;

                EditText_1.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(EditText_1, InputMethodManager.SHOW_IMPLICIT);

                break;
            case R.id.Edit_Text1:

                break;


            default:
        }

    }
}
