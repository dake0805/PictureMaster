package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/*
 * 文本编辑的实现
 * @hrncool
 * */

public class TextEdit extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_edit);

        Button finshbotton = (Button)findViewById(R.id.Finsh_Button);
        finshbotton.setOnClickListener(this);
        /*TextView a = (TextView)findViewById(R.id.Edit_Text1);
        a.setText("Happpy");*/

    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.Finsh_Button:
                String text="HelloWorld";

                EditText EditText_1 = (EditText)findViewById(R.id.Edit_Text1) ;
                text = EditText_1.getText().toString();

                Intent intent = new Intent(TextEdit.this,Drawer.class);

                intent.putExtra("edit_text",text);
                startActivity(intent);

                break;

            default:
        }

    }







}


