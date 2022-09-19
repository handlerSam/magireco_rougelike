package com.live2d.rougelike;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Comparator;

public class CharacterPlateView extends ConstraintLayout {
    final public static int ACCELE = 0;
    final public static int BLAST_VERTICAL = 1;
    final public static int BLAST_HORIZONTAL = 2;
    final public static int CHARGE = 3;
    final public static int MAGIA = 4;
    final public static int DOPPEL = 5;

    View v;
    ImageView background;
    ImageView character;
    ImageView text;
    ImageView[] diamond;
    ImageView attribute;
    ImageView arrow;
    Context context;


    public CharacterPlateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        v = LayoutInflater.from(context).inflate(R.layout.characer_plate_layout,CharacterPlateView.this);
        findView(v);
    }

    public void findView(View v){
        background = v.findViewById(R.id.background);
        character = v.findViewById(R.id.character);
        text = v.findViewById(R.id.text);
        diamond = new ImageView[]{v.findViewById(R.id.diamond0),v.findViewById(R.id.diamond1),v.findViewById(R.id.diamond2)};
        attribute = v.findViewById(R.id.attribute);
        arrow = v.findViewById(R.id.arrow);
    }

    public void setPlate(Character c, int plate){
        attribute.setBackgroundResource(getImageByString(c.element));
        setDiamond(c.diamondNumber);
        character.setBackgroundResource(getImageByString(c.charIconImage+"d"));
        switch(plate){
            case 0:
                for(int i = 0; i < diamond.length; i++){
                    diamond[i].setVisibility(VISIBLE);
                }
                arrow.setVisibility(GONE);
                background.setBackgroundResource(R.drawable.accele_plate);
                text.setImageResource(R.drawable.accele);
                break;
            case 1:case 2:
                for(int i = 0; i < diamond.length; i++){
                    diamond[i].setVisibility(VISIBLE);
                }
                arrow.setVisibility(VISIBLE);
                background.setBackgroundResource(R.drawable.blast_plate);
                text.setImageResource(R.drawable.blast);
                arrow.setImageResource(plate == 1? R.drawable.blast_vertical:R.drawable.blast_horizontal);
                break;
            case 3:
                for(int i = 0; i < diamond.length; i++){
                    diamond[i].setVisibility(VISIBLE);
                }
                arrow.setVisibility(GONE);
                background.setBackgroundResource(R.drawable.charge_plate);
                text.setImageResource(R.drawable.charge);
                break;
            case 4:
                for(int i = 0; i < diamond.length; i++){
                    diamond[i].setVisibility(GONE);
                }
                arrow.setVisibility(VISIBLE);
                arrow.setImageResource(getImageByString(c.magiaSkillIconName));
                background.setBackgroundResource(R.drawable.magia_plate_background);
                text.setImageResource(R.drawable.magia);
                break;
            case 5:
                character.setBackgroundResource(getImageByString(c.doppelImageName));
                for(int i = 0; i < diamond.length; i++){
                    diamond[i].setVisibility(GONE);
                }
                arrow.setVisibility(VISIBLE);
                arrow.setImageResource(getImageByString(c.magiaSkillIconName));
                background.setBackgroundResource(R.drawable.magia_plate_background);
                text.setImageResource(R.drawable.doppel);
                break;
            default:
        }

    }

    public void setDiamond(int diamondNumber){
        for(int i = 0; i < 3; i++){
            diamond[i].setBackgroundResource(i < diamondNumber? R.drawable.diamond_pink:R.drawable.diamond_grey);
        }
    }

    public void setShader(){
        v.setAlpha(0.5f);
    }

    public void cancelShader(){
        v.setAlpha(1);
    }

    public int getImageByString(String name){
        Resources res = context.getResources();
        return res.getIdentifier(name,"drawable",context.getPackageName());
    }
}

class ActionOrderComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Character c1 = (Character)o1;
        Character c2 = (Character)o2;
        return c2.actionOrder - c1.actionOrder;
    }
}
