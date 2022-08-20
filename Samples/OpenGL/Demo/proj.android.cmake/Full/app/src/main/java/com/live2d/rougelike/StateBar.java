package com.live2d.rougelike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class StateBar extends ConstraintLayout {
    ImageView attribute;
    ImageView hpBar;
    ImageView mpBar;
    ImageView dpBar;
    ImageView smallSkill1;
    ImageView smallSkill2;
    ImageView number0;
    ImageView number1;
    ImageView number2;

    Context context;

    int realHp = 1000;
    int realMp = 0;

    int fullHp = 1000;

    public StateBar(@NonNull Context context) {
        super(context);
        this.context = context;
        View v = LayoutInflater.from(context).inflate(R.layout.status_bar, StateBar.this);
        findView(v);
        updateHp();
        updateMp();
    }

    public void findView(View v){
        attribute = v.findViewById(R.id.attribute);
        hpBar = v.findViewById(R.id.hpBar);
        mpBar = v.findViewById(R.id.mpBar);
        dpBar = v.findViewById(R.id.dpBar);
        smallSkill1 = v.findViewById(R.id.smallSkill1);
        smallSkill2 = v.findViewById(R.id.smallSkill2);
        number0 = v.findViewById(R.id.number0);
        number1 = v.findViewById(R.id.number1);
        number2 = v.findViewById(R.id.number2);
    }

    public void updateHp(){
        if(realHp<=0){
            hpBar.setVisibility(INVISIBLE);
            this.setVisibility(INVISIBLE);
        }else{
            hpBar.setVisibility(VISIBLE);
            ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) hpBar.getLayoutParams();
            p.width = (int)(1.0f*realHp/fullHp*100);
            hpBar.setLayoutParams(p);
        }
    }

    public void updateHp(int fullHp, int realHp){
        this.fullHp = fullHp;
        this.realHp = realHp;
        updateHp();
    }

    public void updateMp(){
        if(realMp <= 0){
            mpBar.setVisibility(INVISIBLE);
            dpBar.setVisibility(INVISIBLE);
            number0.setVisibility(INVISIBLE);
            number1.setVisibility(INVISIBLE);
            number2.setVisibility(INVISIBLE);
        }else{
            //设置MP数字
            if(realMp >= 1000){
                number0.setVisibility(VISIBLE);
                int number = (realMp - (realMp % 1000))/1000;
                number0.setBackgroundResource(getResourceByString("blue_"+number));
            }else{
                number0.setVisibility(INVISIBLE);
            }
            if(realMp >= 100){
                number1.setVisibility(VISIBLE);
                number1.setBackgroundResource(getResourceByString("blue_"+((realMp % 1000) - (realMp % 100))/100));
            }else{
                number1.setVisibility(INVISIBLE);
            }
            if(realMp >= 10){
                number2.setVisibility(VISIBLE);
                number2.setBackgroundResource(getResourceByString("blue_"+((realMp % 100) - (realMp % 10))/10));
            }else{
                number2.setVisibility(INVISIBLE);
            }


            mpBar.setVisibility(VISIBLE);
            ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) mpBar.getLayoutParams();
            p.width = (int)((Math.min(realMp,1000)) * 0.08);
            mpBar.setLayoutParams(p);
            if(realMp > 1000){
                dpBar.setVisibility(VISIBLE);
                ConstraintLayout.LayoutParams p2 = (ConstraintLayout.LayoutParams) dpBar.getLayoutParams();
                p2.width = (int)((realMp - 1000) * 0.08);
                dpBar.setLayoutParams(p2);
            }else{
                dpBar.setVisibility(INVISIBLE);
            }
        }
    }

    public void updateMp(int realMp){
        this.realMp = realMp;
        updateMp();
    }

    public void setAttr(String element){
        attribute.setBackgroundResource(getResourceByString(element));
    }

    public int getResourceByString(String idName){
        return getResources().getIdentifier(idName,"drawable", context.getPackageName());
    }
}
