package com.live2d.rougelike;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class CardView extends ConstraintLayout {
    ImageView chooseView;
    ImageView memoriaView;
    ImageView equippingView;
    StrokeTextView lvView;
    LinearLayout unableToEquipView;
    View v;
    Context context;
    int nowLv = 1;
    int maxLv = 50;

    public CardView(@NonNull Context context) {
        super(context);
        v = LayoutInflater.from(context).inflate(R.layout.card_view,CardView.this);
        this.context = context;
        findView(v);
    }

    public CardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        v = LayoutInflater.from(context).inflate(R.layout.card_view,CardView.this);
        this.context = context;
        findView(v);
    }

    public CardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        v = LayoutInflater.from(context).inflate(R.layout.card_view,CardView.this);
        this.context = context;
        findView(v);
    }

    public CardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        v = LayoutInflater.from(context).inflate(R.layout.card_view,CardView.this);
        this.context = context;
        findView(v);
    }

    public CardView(@NonNull Context context, ViewGroup parent) {
        super(context);
        v = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        this.context = context;
        findView(v);
    }


    void findView(View v){
        chooseView  = v.findViewById(R.id.chooseView);
        memoriaView = v.findViewById(R.id.memoriaView);
        equippingView = v.findViewById(R.id.equippingView);
        lvView = v.findViewById(R.id.lvView);
        unableToEquipView = v.findViewById(R.id.unableToEquip);
    }

    public void setMemoria(String id){
        memoriaView.setImageResource(getResource("memoria_" + id + "_s"));
        setLv(-1,-1);
    }

    public void setChoose(boolean isToChoose){
        chooseView.setVisibility(isToChoose? VISIBLE:INVISIBLE);
    }

    public void setLv(int nowLv, int maxLv){
        this.nowLv = (nowLv == -1)? this.nowLv : nowLv;
        this.maxLv = (maxLv == -1)? this.maxLv : maxLv;
        lvView.setVisibility(VISIBLE);
        lvView.setText("Lv " + nowLv + "/" + maxLv);
    }

    public void setUnableToEquip(boolean isToUnable){
        unableToEquipView.setVisibility(isToUnable? VISIBLE:INVISIBLE);
    }

    public void setUnusable(){//未开孔
        setChoose(false);
        setUnableToEquip(false);
        lvView.setVisibility(INVISIBLE);
        equippingView.setVisibility(INVISIBLE);
        memoriaView.setImageResource(R.drawable.no_card);
    }

    public void setEmpty(){//开孔但未装备
        setChoose(false);
        setUnableToEquip(false);
        lvView.setVisibility(INVISIBLE);
        equippingView.setVisibility(INVISIBLE);
        memoriaView.setImageResource(R.drawable.empty_card);
    }

    public void setEquippingResource(boolean isEquippedByOthers){
        equippingView.setImageResource(isEquippedByOthers? R.drawable.equiping3:R.drawable.equiping2);
    }

    public int getResource(String idName){
        return context.getResources().getIdentifier(idName,"drawable", context.getPackageName());
    }
}
