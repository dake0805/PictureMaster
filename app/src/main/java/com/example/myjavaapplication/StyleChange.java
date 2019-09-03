package com.example.myjavaapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class StyleChange extends AppCompatActivity {

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style_change);
//        setCustomActionBar();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });


    }

//
//    @Override
//    public boolean onNavigateUp() {
//        finish();
//        return super.onNavigateUp();
//    }
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.style_change_menu, menu);
//        return true;
//    }
//
//
//    private void setCustomActionBar() {
//        ArrayList<View> textViews = new ArrayList<>();
//
//        getWindow().getDecorView().findViewsWithText(textViews, getTitle(), View.FIND_VIEWS_WITH_TEXT);
//
//        if (textViews.size() > 0) {
//            AppCompatTextView appCompatTextView = null;
//            if (textViews.size() == 1) {
//                appCompatTextView = (AppCompatTextView) textViews.get(0);
//            } else {
//                for (View v : textViews) {
//                    if (v.getParent() instanceof Toolbar) {
//                        appCompatTextView = (AppCompatTextView) v;
//                        break;
//                    }
//                }
//            }
//
//            if (appCompatTextView != null) {
//                ViewGroup.LayoutParams params = appCompatTextView.getLayoutParams();
//                params.width = 1080;
//                params.
//                appCompatTextView.setLayoutParams(params);
//                appCompatTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//            }
//        }
//    }


}