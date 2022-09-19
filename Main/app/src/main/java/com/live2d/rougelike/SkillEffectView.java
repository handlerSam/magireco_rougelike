package com.live2d.rougelike;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SkillEffectView extends ConstraintLayout {
    ConstraintLayout parentLayout;
    StrokeTextView strokeTextView;
    View v;
    Context context;

    public SkillEffectView(@NonNull Context context, final ConstraintLayout externalParentLayout, String text, int animationFile, int foregroundColor, int backgroundColor, boolean isDamageNumber) {
        super(context);
        this.context = context;
        if(isDamageNumber){
            v = LayoutInflater.from(context).inflate(R.layout.damage_number,SkillEffectView.this);
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.skill_effect,SkillEffectView.this);
        }

        parentLayout = findViewById(R.id.parentLayout);
        strokeTextView = findViewById(R.id.strokeTextView);
        strokeTextView.setText(text);
        strokeTextView.setBackGroundColor(getResources().getColor(backgroundColor));
        strokeTextView.setTextColor(getResources().getColor(foregroundColor));
        Animation animation = AnimationUtils.loadAnimation(context, animationFile);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        externalParentLayout.removeView(v);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        strokeTextView.startAnimation(animation);
    }
}
