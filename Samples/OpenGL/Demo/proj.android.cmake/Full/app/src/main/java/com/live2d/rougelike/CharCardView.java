package com.live2d.rougelike;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CharCardView extends ConstraintLayout {
    Context context;
    View v;
    ImageView charMemoria;
    ImageView skillIcon;
    ImageView max;
    TextView  lvView;
    int lv = 1;
    int maxLv = 30;

    public CharCardView(@NonNull Context context) {
        super(context);
        this.context = context;
        v = LayoutInflater.from(context).inflate(R.layout.char_card_view,CharCardView.this);
        find();
    }

    public CharCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        v = LayoutInflater.from(context).inflate(R.layout.char_card_view,CharCardView.this);
        find();
    }

    public CharCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        v = LayoutInflater.from(context).inflate(R.layout.char_card_view,CharCardView.this);
        find();
    }

    public CharCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        v = LayoutInflater.from(context).inflate(R.layout.char_card_view,CharCardView.this);
        find();
    }

    public void find(){
        charMemoria = v.findViewById(R.id.charMemoria);
        skillIcon = v.findViewById(R.id.skillIcon);
        max = v.findViewById(R.id.max);
        lvView = v.findViewById(R.id.lvView);
    }

    public void setLv(int lv, int maxlv){
        this.lv = (lv == -1? this.lv:lv);
        this.maxLv = (maxlv == -1? this.maxLv:maxlv);
        lvView.setText("Lv"+this.lv+"/"+this.maxLv);
        if(this.lv == this.maxLv){
            max.setVisibility(VISIBLE);
        }else{
            max.setVisibility(INVISIBLE);
        }
    }

    public void setMemoria(Memoria m){
        charMemoria.setImageResource(getResource("memoria_" + m.id + "_s"));
        lvView.setVisibility(VISIBLE);
        setLv(m.lvNow,m.lvMax);
        skillIcon.setVisibility(VISIBLE);
        skillIcon.setImageResource(getResource(m.icon));
    }

    public void setUnusable(){//未开孔
        charMemoria.setImageResource(R.drawable.no_card);
        lvView.setVisibility(INVISIBLE);
        skillIcon.setVisibility(INVISIBLE);
        max.setVisibility(INVISIBLE);
    }

    public void setEmpty(){//开孔但未装备
        charMemoria.setImageResource(R.drawable.empty_card);
        lvView.setVisibility(INVISIBLE);
        skillIcon.setVisibility(INVISIBLE);
        max.setVisibility(INVISIBLE);
    }

    public int getResource(String idName){
        return context.getResources().getIdentifier(idName,"drawable", context.getPackageName());
    }


}
