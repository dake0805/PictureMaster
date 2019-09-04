package com.example.myjavaapplication;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AnimationView {
    public static void fadeIn(View view, float startAlpha, float endAlpha, long duration) {
        if (view.getVisibility() == View.VISIBLE) return;

        view.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(startAlpha, endAlpha);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static void fadeIn(View view) {
        fadeIn(view, 0F, 1F, 600);

        // We disabled the button in fadeOut(), so enable it here.
        view.setEnabled(true);
    }

    public static void fadeOut(View view,float startAlpha,float endAlpha,long duration){
        if (view.getVisibility() != View.VISIBLE) return;

        // Since the button is still clickable before fade-out animation
        // ends, we disable the button first to block click.
        view.setEnabled(false);
        Animation animation = new AlphaAnimation(startAlpha, endAlpha);
        animation.setDuration(duration);
        view.startAnimation(animation);
        view.setVisibility(View.GONE);
    }

    public static void fadeOut(View view) {
        fadeOut(view,1F,0F,600);
    }

    public static void fade(View view,int visibility){
        if(visibility == View.GONE || visibility == View.INVISIBLE){
            AnimationView.fadeOut(view);
        }
        else {
            AnimationView.fadeIn(view);
        }
    }

}
