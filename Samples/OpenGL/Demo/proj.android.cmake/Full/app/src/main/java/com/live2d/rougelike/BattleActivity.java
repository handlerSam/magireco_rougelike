package com.live2d.rougelike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.View.generateViewId;
import static com.live2d.rougelike.CharacterPlateView.ACCELE;
import static com.live2d.rougelike.CharacterPlateView.BLAST_HORIZONTAL;
import static com.live2d.rougelike.CharacterPlateView.BLAST_VERTICAL;
import static com.live2d.rougelike.CharacterPlateView.CHARGE;
import static com.live2d.rougelike.CharacterPlateView.DOPPEL;
import static com.live2d.rougelike.CharacterPlateView.MAGIA;

public class BattleActivity extends AppCompatActivity {

    final public static int CHARACTER_NORMAL_SIZE = 1200;
    final public static int CHARACTER_MAGNIFIED_SIZE = 1200;

    final public static int DOPPEL_NEED_MP = 2000;

    final public static int PLATE_SHOW = 0;
    final public static int SKILL_BAR_SHOW = 1;
    final public static int MAGIA_BAR_SHOW = 2;

    final public static int TEXT_RED = 0;
    final public static int TEXT_BLUE = 1;
    final public static int TEXT_GREEN = 2;

    final public static int ACCELECOMBO = 0;
    final public static int CHARGECOMBO = 1;
    final public static int BLASTCOMBO = 2;
    final public static int PUELLACOMBO = 3;

    public boolean isBossBattle = true;

    final public static float[][] multiChargeTable = new float[][]{
            /*ACCELE伤害*/{1.0f,1.1f,1.2f,1.3f,1.4f,1.5f,1.6f,1.7f,1.8f,1.9f,2.0f,2.1f,2.2f,2.3f,2.4f,2.5f,2.6f,2.7f,2.8f,2.9f,3.0f},
            /*BLAST伤害*/ {1.0f,1.4f,1.7f,2.0f,2.3f,2.5f,2.7f,2.9f,3.1f,3.3f,3.5f,3.7f,3.9f,4.1f,4.3f,4.5f,4.6f,4.7f,4.8f,4.9f,5.0f},
            /*ACCELE MP*/ {1.0f,1.3f,1.6f,1.9f,2.2f,2.5f,2.7f,2.9f,3.1f,3.3f,3.5f,3.9f,4.3f,4.7f,5.1f,5.5f,6.0f,6.5f,7.0f,7.5f,8.0f}
    };

    public static int DELTA_BETWEEN_ATTACK_AND_DAMAGE = 700;
    public static int DELTA_BETWEEN_EFFECT_SHOW = 750;


    public Character[][] monsterFormation = new Character[3][3];

    public StateBar[][] leftStateBarList = new StateBar[3][3];
    public StateBar[][] rightStateBarList = new StateBar[3][3];

    public ArrayList<Effect>[][] leftEffectList = new ArrayList[3][3];
    public ArrayList<Effect>[][] rightEffectList = new ArrayList[3][3];

    boolean clickable = true; // 决定全局button是否可以click

    int smallPlateNumber = 0;

    int turn = 1; // 回合数

    int isWin = 0; // 1为我方胜利,-1为敌方胜利

    public int totalSpriteNumber = 0;

    public int loadedSpriteNumber = 0;

    int barState = PLATE_SHOW;

    int chooseMonsterX = -1;// 棋盘上选择了哪个monster
    int chooseMonsterY = -1;

    int dragPlateId = -1;//0-5,仅当拖拽时有效

    int monsterPlate = -1;
    int monsterAttackerX = -1;
    int monsterAttackerY = -1;

    int chargeNumber = 0;
    int enemyChargeNumber = 0;


    ColorMatrixColorFilter grayColorFilter;//用于灰度设置
    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 1:
                    //我方进攻in结束
                    recoverCharacterSize(true);
                    magnifyCharacter(false);

                    //判断该敌人是否已经被打死,若打死则找新的目标
                    if(leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c.realHP <= 0){
                        int[] tempP = new int[]{-1, -1};
                        findAliveCharacter(tempP,false);
                        if(tempP[0] == -1){
                            //说明敌人全部被干掉了
                            win();
                        }else{
                            smallPlateXList[smallPlateNumber] = tempP[0];
                            smallPlateYList[smallPlateNumber] = tempP[1];
                        }
                    }

                    if(isWin == 0){
                        if(smallPlateList[smallPlateNumber] <= 4){
                            //是普通攻击
                            final Plate temp = plateList[smallPlateList[smallPlateNumber]];
                            final Character c = smallPlateConnectToList[smallPlateNumber] == null ? temp.c:smallPlateConnectToList[smallPlateNumber];

                            //挑拨判定
                            Character newTarget = judgeProvocation(c,leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c,true);
                            if(newTarget != null){
                                smallPlateXList[smallPlateNumber] = newTarget.formationX;
                                smallPlateYList[smallPlateNumber] = newTarget.formationY;
                            }

                            //回避无效判定
                            final boolean isMissNullified = isTriggerEffect(rightEffectList[c.formationX][c.formationY],"回避无效");

                            if(temp.plate == CharacterPlateView.ACCELE || temp.plate == CharacterPlateView.CHARGE){
                                rightCharList[c.formationX][c.formationY].spriteName = "attack_out";
                                changeSprite(c.formationX,c.formationY,true);
                                setCharacterAttackPosition(c.formationX,c.formationY,smallPlateXList[smallPlateNumber],smallPlateYList[smallPlateNumber],true);
                                rightCharList[c.formationX][c.formationY].setVisibility(VISIBLE);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int mp = getAttackMP(c,true,temp.plate);
                                        setMpOnCharacter(c,c.realMP+mp,true);
                                        //攻击者是否触发异常状态miss效果
                                        String isAbnormalDebuffMiss = isTriggerEffect(rightEffectList[c.formationX][c.formationY],new String[]{"雾","黑暗","幻惑"});

                                        //被攻击者是否触发回避效果
                                        boolean isMiss = isTriggerEffect(leftEffectList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]],"回避");
                                        if(hasAbnormalState(leftEffectList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]])){
                                            isMiss = false;
                                        }


                                        if(isMissNullified || (isAbnormalDebuffMiss.equals("") && !isMiss)){
                                            //伤害计算
                                            int damage = getDamage(c,leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c,true);
                                            setDamageOnCharacter(leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c,damage,false,true);
                                            sendDamageNumber(damage,
                                                    smallPlateXList[smallPlateNumber],smallPlateYList[smallPlateNumber],
                                                    (isRestrained(c,leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c)>=0)? TEXT_RED:TEXT_BLUE,false,true);

                                            //攻击时概率附带异常效果计算
                                            ArrayList<Effect> efList = rightEffectList[c.formationX][c.formationY];
                                            for(int i = 0; i < efList.size(); i++){
                                                Effect e = efList.get(i);
                                                switch(e.name){
                                                    case "攻击时给予状态毒": case "攻击时给予状态烧伤": case "攻击时给予状态诅咒":
                                                    case "攻击时给予状态魅惑": case "攻击时给予状态眩晕": case "攻击时给予状态拘束":
                                                    case "攻击时给予状态雾": case "攻击时给予状态黑暗": case "攻击时给予状态幻惑":
                                                    case "攻击时给予状态技能封印": case "攻击时给予状态Magia封印":
                                                    case "攻击时给予状态HP回复禁止": case "攻击时给予状态MP回复禁止":
                                                        //检查对方是否有该异常无效
                                                        boolean isEffectImmune = isTriggerEffect(leftEffectList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]],e.name.substring(7)+"无效");

                                                        //获取对方异常耐性
                                                        int abnormalStateResistance = calculateAbnormalStateResistance(leftEffectList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]]);
                                                        if(colorToss(e.probability-abnormalStateResistance)){
                                                            Effect e2 = new Effect();
                                                            if(!isEffectImmune){
                                                                e2.name = e.name.substring(7);
                                                                e2.time = e.valueTime;
                                                                e2.value = e.value;
                                                                leftEffectList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].add(e2);
                                                            }else{
                                                                e2.name = e.name.substring(7)+"无效";
                                                            }
                                                            sendEffect(e2,smallPlateXList[smallPlateNumber],smallPlateYList[smallPlateNumber],false,true);
                                                        }
                                                        break;
                                                    default:
                                                }
                                            }
                                        }

                                        //效果字幕显示
                                        if(isMiss || !isAbnormalDebuffMiss.equals("")){
                                            Effect e = new Effect();
                                            if(isMissNullified){
                                                e.name = "回避无效";
                                            }else{
                                                e.name = "回避";
                                            }
                                            sendEffect(e,smallPlateXList[smallPlateNumber],smallPlateYList[smallPlateNumber],false,true);
                                        }

                                        //计算受击MP和受击动画
                                        if(leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c.realHP > 0){
                                            int defendMP = getDefendMP(c,leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c,false);
                                            setMpOnCharacter(leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c,leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c.realMP+defendMP,false);
                                            if((isMissNullified || (isAbnormalDebuffMiss.equals("") && !isMiss))){
                                                //没有回避
                                                if(!isBossBattle){
                                                    leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].spriteName = "damage";
                                                    changeSprite(smallPlateXList[smallPlateNumber], smallPlateYList[smallPlateNumber],false);
                                                    leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                                }else{
                                                    leftCharList[1][1].spriteName = "damage";
                                                    changeSprite(1, 1,false);
                                                    leftCharList[1][1].webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                                }
                                            }
                                        }
                                        if(temp.plate == ACCELE){
                                            chargeNumber = 0;
                                            updateChargeView();
                                        }
                                    }
                                },DELTA_BETWEEN_ATTACK_AND_DAMAGE);
                            }else if(temp.plate == CharacterPlateView.BLAST_VERTICAL || temp.plate == CharacterPlateView.BLAST_HORIZONTAL){
                                rightCharList[c.formationX][c.formationY].spriteName = (temp.plate == CharacterPlateView.BLAST_VERTICAL)? "attackv_out":"attackh_out";
                                changeSprite(c.formationX,c.formationY,true);
                                if(temp.plate == CharacterPlateView.BLAST_VERTICAL){
                                    setCharacterAttackPosition(c.formationX,c.formationY,1,smallPlateYList[smallPlateNumber],true);
                                }else{
                                    setCharacterAttackPosition(c.formationX,c.formationY,smallPlateXList[smallPlateNumber],1,true);
                                }
                                rightCharList[c.formationX][c.formationY].setVisibility(VISIBLE);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //攻击MP
                                        int mp = getAttackMP(c,true,temp.plate);
                                        c.realMP += mp;
                                        //Blast攻击时MP获得
                                        int blastAttackMp = 0;
                                        ArrayList<Effect> efList = rightEffectList[c.formationX][c.formationY];
                                        for(int i = 0; i < efList.size(); i++){
                                            Effect e = efList.get(i);
                                            if(e.name.equals("Blast攻击时MP获得")){
                                                blastAttackMp += e.value;
                                            }
                                        }

                                        for(int i = 0; i < 3; i++){
                                            SpriteViewer sv = (temp.plate == CharacterPlateView.BLAST_VERTICAL)? leftCharList[i][smallPlateYList[smallPlateNumber]] : leftCharList[smallPlateXList[smallPlateNumber]][i];
                                            //攻击者是否触发异常状态miss效果
                                            String isAbnormalDebuffMiss = isTriggerEffect(rightEffectList[c.formationX][c.formationY],new String[]{"雾","黑暗","幻惑"});
                                            if(sv != null && sv.c.realHP > 0){
                                                //被攻击者是否触发回避效果
                                                boolean isMiss = false;
                                                if(temp.plate == CharacterPlateView.BLAST_VERTICAL){
                                                    isMiss = isTriggerEffect(leftEffectList[i][smallPlateYList[smallPlateNumber]],"回避");
                                                    if(hasAbnormalState(leftEffectList[i][smallPlateYList[smallPlateNumber]])){
                                                        isMiss = false;
                                                    }
                                                }else{
                                                    isMiss = isTriggerEffect(leftEffectList[smallPlateXList[smallPlateNumber]][i],"回避");
                                                    if(hasAbnormalState(leftEffectList[smallPlateXList[smallPlateNumber]][i])){
                                                        isMiss = false;
                                                    }
                                                }
                                                if(isMissNullified || (isAbnormalDebuffMiss.equals("") && !isMiss)){

                                                    //BlastMp结算
                                                    c.realMP += blastAttackMp;
                                                    //计算伤害
                                                    int damage = getDamage(c,sv.c,true);
                                                    setDamageOnCharacter(sv.c,damage,false,true);
                                                    if(temp.plate == CharacterPlateView.BLAST_VERTICAL){
                                                        sendDamageNumber(damage,
                                                                i,smallPlateYList[smallPlateNumber],
                                                                (isRestrained(c,sv.c)>=0)?TEXT_RED:TEXT_BLUE,false,true);
                                                    }else{
                                                        sendDamageNumber(damage,
                                                                smallPlateXList[smallPlateNumber],i,
                                                                (isRestrained(c,sv.c)>=0)?TEXT_RED:TEXT_BLUE,false,true);
                                                    }

                                                    //攻击时概率附带异常效果计算
                                                    for(int j = 0; j < efList.size(); j++){
                                                        Effect e = efList.get(j);
                                                        switch(e.name){
                                                            case "攻击时给予状态毒": case "攻击时给予状态烧伤": case "攻击时给予状态诅咒":
                                                            case "攻击时给予状态魅惑": case "攻击时给予状态眩晕": case "攻击时给予状态拘束":
                                                            case "攻击时给予状态雾": case "攻击时给予状态黑暗": case "攻击时给予状态幻惑":
                                                            case "攻击时给予状态技能封印": case "攻击时给予状态Magia封印":
                                                            case "攻击时给予状态HP回复禁止": case "攻击时给予状态MP回复禁止":
                                                                if(!isBossBattle || (isBossBattle && (i == 1))){
                                                                    //检查对方是否有该异常无效
                                                                    boolean isEffectImmune;
                                                                    //获取对方异常耐性
                                                                    int abnormalStateResistance;
                                                                    if(temp.plate == CharacterPlateView.BLAST_VERTICAL){
                                                                        abnormalStateResistance = calculateAbnormalStateResistance(leftEffectList[i][smallPlateYList[smallPlateNumber]]);
                                                                        isEffectImmune = isTriggerEffect(leftEffectList[i][smallPlateYList[smallPlateNumber]],e.name.substring(7)+"无效");
                                                                    }else{
                                                                        abnormalStateResistance = calculateAbnormalStateResistance(leftEffectList[smallPlateXList[smallPlateNumber]][i]);
                                                                        isEffectImmune = isTriggerEffect(leftEffectList[smallPlateXList[smallPlateNumber]][i],e.name.substring(7)+"无效");
                                                                    }
                                                                    if(colorToss(e.probability - abnormalStateResistance)){
                                                                        Effect e2 = new Effect();
                                                                        if(!isEffectImmune){
                                                                            e2.name = e.name.substring(7);
                                                                            e2.time = e.valueTime;
                                                                            e2.value = e.value;
                                                                            if(temp.plate == CharacterPlateView.BLAST_VERTICAL){
                                                                                leftEffectList[i][smallPlateYList[smallPlateNumber]].add(e2);
                                                                                sendEffect(e2,i,smallPlateYList[smallPlateNumber],false,true);
                                                                            }else{
                                                                                leftEffectList[smallPlateXList[smallPlateNumber]][i].add(e2);
                                                                                sendEffect(e2,smallPlateXList[smallPlateNumber],i,false,true);
                                                                            }
                                                                        }else{
                                                                            e2.name = e.name.substring(7)+"无效";
                                                                            if(temp.plate == CharacterPlateView.BLAST_VERTICAL){
                                                                                sendEffect(e2,i,smallPlateYList[smallPlateNumber],false,true);
                                                                            }else{
                                                                                sendEffect(e2,smallPlateXList[smallPlateNumber],i,false,true);
                                                                            }
                                                                        }
                                                                }
                                                            }
                                                            break;
                                                            default:
                                                        }
                                                    }
                                                }

                                                //效果字幕显示
                                                if(isMiss || !isAbnormalDebuffMiss.equals("")){
                                                    Effect e = new Effect();
                                                    if(isMissNullified){
                                                        //显示"回避无效"
                                                        e.name = "回避无效";
                                                    }else{
                                                        //显示回避
                                                        e.name = "回避";
                                                    }
                                                    if(temp.plate == CharacterPlateView.BLAST_VERTICAL){
                                                        sendEffect(e,i,smallPlateYList[smallPlateNumber],false,true);
                                                    }else{
                                                        sendEffect(e,smallPlateXList[smallPlateNumber],i,false,true);
                                                    }
                                                }

                                                if(sv.c.realHP > 0){
                                                    int defendMP = getDefendMP(c,sv.c,false);
                                                    setMpOnCharacter(sv.c,sv.c.realMP+defendMP,false);
                                                    if((isMissNullified || (isAbnormalDebuffMiss.equals("") && !isMiss))){
                                                        //没有回避
                                                        if(!isBossBattle){
                                                            sv.spriteName = "damage";
                                                            if(temp.plate == CharacterPlateView.BLAST_VERTICAL){
                                                                changeSprite(i, smallPlateYList[smallPlateNumber],false);
                                                            }else{
                                                                changeSprite(smallPlateXList[smallPlateNumber],i,false);
                                                            }
                                                            sv.webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                                        }else{
                                                            leftCharList[1][1].spriteName = "damage";
                                                            changeSprite(1, 1,false);
                                                            leftCharList[1][1].webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                        setMpOnCharacter(c,c.realMP,true);
                                        chargeNumber = 0;
                                        updateChargeView();
                                    }
                                },DELTA_BETWEEN_ATTACK_AND_DAMAGE);
                            }
                            rightCharList[c.formationX][c.formationY].webView.loadUrl("javascript:setAnimationIndex(" + 2 + ")");
                        }else{
                            //是magia或者doppel
                            final Character c = StartActivity.characters[smallPlateList[smallPlateNumber]-5];
                            int waitTime = 0;
                            final ArrayList<SkillEffect> effectList = (c.realMP >= DOPPEL_NEED_MP)? c.doppelEffectList:((c.star == 5)?c.magiaAfterEffectList:c.magiaOriginEffectList);
                            if(effectList.size() > 0){
                                for(int i = 0; i < effectList.size(); i++){
                                    final int tempI = i;
                                    if(effectList.get(tempI).target.equals("敌单") || effectList.get(tempI).target.equals("敌全")){
                                        waitTime += DELTA_BETWEEN_EFFECT_SHOW;
                                    }
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(effectList.get(tempI).target.equals("敌单")){
                                                int abnormalStateResistance = calculateAbnormalStateResistance(leftEffectList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]]);
                                                final Effect e = convertSkillEffectToEffect(effectList.get(tempI), abnormalStateResistance);
                                                if(e != null){
                                                    handleEffectiveness(e,smallPlateXList[smallPlateNumber],smallPlateYList[smallPlateNumber],false,true);
                                                }
                                            }else if(effectList.get(tempI).target.equals("敌全")){
                                                for(int j = 0; j < 3; j++){
                                                    for(int k = 0; k < 3; k++){
                                                        if(leftCharList[j][k] != null && leftCharList[j][k].c.realHP > 0){
                                                            if(!isBossBattle || (isBossBattle && (j == 1) && (k == 1))){
                                                                int abnormalStateResistance = calculateAbnormalStateResistance(leftEffectList[j][k]);
                                                                final Effect e = convertSkillEffectToEffect(effectList.get(tempI), abnormalStateResistance);
                                                                if(e != null){
                                                                    handleEffectiveness(e,j,k,false,true);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    },waitTime);
                                }
                            }
                            //播放受众动画
                            if(c.magiaTarget.equals("敌单")){
                                int damage = getDamage(c,leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c,true);
                                setDamageOnCharacter(leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c,damage,false,true);
                                sendDamageNumber(damage,
                                        smallPlateXList[smallPlateNumber],smallPlateYList[smallPlateNumber],
                                        (isRestrained(c,leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c)>=0)?TEXT_RED:TEXT_BLUE,false,true);
                                if(leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c.realHP > 0){
                                    int defendMP = getDefendMP(c,leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c,false);
                                    setMpOnCharacter(leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c,leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].c.realMP+defendMP,false);
                                    if(!isBossBattle){
                                        leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].spriteName = "damage";
                                        changeSprite(smallPlateXList[smallPlateNumber], smallPlateYList[smallPlateNumber],false);
                                        leftCharList[smallPlateXList[smallPlateNumber]][smallPlateYList[smallPlateNumber]].webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                    }else{
                                        leftCharList[1][1].spriteName = "damage";
                                        changeSprite(1, 1,false);
                                        leftCharList[1][1].webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                    }
                                }
                            }else if(c.magiaTarget.equals("敌全")){
                                for(int i = 0; i < 3; i++){
                                    for(int j = 0; j < 3; j++){
                                        if(leftCharList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                                            int damage = getDamage(c,leftCharList[i][j].c,true);
                                            setDamageOnCharacter(leftCharList[i][j].c,damage,false,true);
                                            sendDamageNumber(damage,
                                                    i,j,
                                                    (isRestrained(c,leftCharList[i][j].c)>=0)?TEXT_RED:TEXT_BLUE,false,true);
                                            if(leftCharList[i][j].c.realHP > 0){
                                                int defendMP = getDefendMP(c,leftCharList[i][j].c,false);
                                                setMpOnCharacter(leftCharList[i][j].c,leftCharList[i][j].c.realMP+defendMP,false);
                                                if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                                                    leftCharList[i][j].spriteName = "damage";
                                                    changeSprite(i, j,false);
                                                    leftCharList[i][j].webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            if(StartActivity.characters[smallPlateList[smallPlateNumber]-5].realMP >= DOPPEL_NEED_MP){
                                //说明是Dp
                                StartActivity.characters[smallPlateList[smallPlateNumber]-5].realMP = 0;
                            }else{
                                StartActivity.characters[smallPlateList[smallPlateNumber]-5].realMP -= 1000;
                            }
                            setMpOnCharacter(c,c.realMP,true);
                            //镜头转向右方，播放我方效果
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    recoverCharacterSize(false);
                                    magnifyCharacter(true);
                                    if(effectList.size() > 0){
                                        int waitTime = 0;
                                        for(int i = 0; i < effectList.size(); i++){
                                            final int tempI = i;
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(effectList.get(tempI).target.equals("自")){
                                                        int abnormalStateResistance = calculateAbnormalStateResistance(rightEffectList[c.formationX][c.formationY]);
                                                        final Effect e = convertSkillEffectToEffect(effectList.get(tempI), abnormalStateResistance);
                                                        if(e != null){
                                                            handleEffectiveness(e,c.formationX,c.formationY,true,true);
                                                        }
                                                    }else if(effectList.get(tempI).target.equals("己全")){
                                                        for(int j = 0; j < 3; j++){
                                                            for(int k = 0; k < 3; k++){
                                                                if(rightCharList[j][k] != null && rightCharList[j][k].c.realHP > 0){
                                                                    int abnormalStateResistance = calculateAbnormalStateResistance(rightEffectList[j][k]);
                                                                    final Effect e = convertSkillEffectToEffect(effectList.get(tempI), abnormalStateResistance);
                                                                    if(e != null){
                                                                        handleEffectiveness(e,j,k,true,true);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if(tempI == effectList.size() - 1){
                                                        //d/m的效果放完了
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Message m = new Message();
                                                                m.what = 2;
                                                                handler.sendMessage(m);
                                                            }
                                                        },1000);
                                                    }
                                                }
                                            },waitTime+200);
                                            if(effectList.get(tempI).target.equals("自") || effectList.get(tempI).target.equals("己全")){
                                                waitTime += DELTA_BETWEEN_EFFECT_SHOW;
                                            }
                                        }
                                    }
                                }
                            },waitTime+DELTA_BETWEEN_EFFECT_SHOW);
                        }
                    }
                    break;
                case 2:
                    //我方进攻out结束
                    small_plate[smallPlateNumber].setVisibility(View.INVISIBLE);
                    if(smallPlateNumber < 2){
                        small_plate_right_arrow[smallPlateNumber].setVisibility(View.INVISIBLE);
                    }
                    if(smallPlateList[smallPlateNumber] <= 4){
                        Character c2 = smallPlateConnectToList[smallPlateNumber] == null? plateList[smallPlateList[smallPlateNumber]].c:smallPlateConnectToList[smallPlateNumber];
                        rightCharList[c2.formationX][c2.formationY].spriteName = getRecoverOriginSpriteName(c2.formationX, c2.formationY, true, "stance",true);
                        changeSprite(c2.formationX,c2.formationY,true);
                    }
                    smallPlateNumber++;
                    if(smallPlateNumber < 3){
                        Character c = (smallPlateList[smallPlateNumber] > 4)? StartActivity.characters[smallPlateList[smallPlateNumber]-5]:plateList[smallPlateList[smallPlateNumber]].c;
                        boolean tempIsCharacterAlive = c.realHP > 0;
                        while(smallPlateNumber < 3 && !tempIsCharacterAlive){
                            smallPlateNumber++;
                        }
                    }

                    //判断是否杀死所有敌人
                    int[] tempP = new int[]{-1,-1};
                    findAliveCharacter(tempP,false);
                    if(tempP[0] == -1){
                        win();
                    }else{
                        if(smallPlateNumber < 3){
                            recoverCharacterSize(false);
                            magnifyCharacter(true);
                            startRightAttack();
                        }else{
                            //三个盘都打完了
                            startLeftAttack();
                        }
                    }
                    break;
                case 3:
                    //敌方进攻in结束
                    recoverCharacterSize(false);
                    magnifyCharacter(true);
                    int characterNumber = 0;
                    for(int i = 0; i < 5; i++){
                        if(StartActivity.characters[i] != null && StartActivity.characters[i].realHP > 0){
                            characterNumber++;
                        }
                    }
                    int chooseCharacter = (int)(Math.random()*characterNumber);
                    int tempCount = 0;
                    for(int i = 0; i < 5; i++){
                        if(StartActivity.characters[i] != null && StartActivity.characters[i].realHP > 0){
                            if(tempCount == chooseCharacter){
                                int tempX = StartActivity.characters[i].formationX;
                                int tempY = StartActivity.characters[i].formationY;
                                //挑拨判定
                                Character newTarget = judgeProvocation(leftCharList[monsterAttackerX][monsterAttackerY].c,rightCharList[tempX][tempY].c,false);
                                if(newTarget != null){
                                    tempX = newTarget.formationX;
                                    tempY = newTarget.formationY;
                                }
                                final int x = tempX;
                                final int y = tempY;

                                //回避无效判定
                                final boolean isMissNullified = isTriggerEffect(leftEffectList[monsterAttackerX][monsterAttackerY],"回避无效");

                                if(monsterPlate == CharacterPlateView.ACCELE || monsterPlate == CharacterPlateView.CHARGE){
                                    if(!isBossBattle){
                                        leftCharList[monsterAttackerX][monsterAttackerY].spriteName = "attack_out";
                                        changeSprite(monsterAttackerX,monsterAttackerY,false);
                                        setCharacterAttackPosition(x,y,monsterAttackerX,monsterAttackerY,false);
                                        leftCharList[monsterAttackerX][monsterAttackerY].setVisibility(VISIBLE);
                                    }
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            int mp = getAttackMP(leftCharList[monsterAttackerX][monsterAttackerY].c,false,monsterPlate);
                                            setMpOnCharacter(leftCharList[monsterAttackerX][monsterAttackerY].c,leftCharList[monsterAttackerX][monsterAttackerY].c.realMP+mp,false);
                                            //攻击者是否触发异常状态miss效果
                                            String isAbnormalDebuffMiss = isTriggerEffect(leftEffectList[monsterAttackerX][monsterAttackerY],new String[]{"雾","黑暗","幻惑"});
                                            //被攻击者是否触发回避效果
                                            boolean isMiss = isTriggerEffect(rightEffectList[x][y],"回避");
                                            if(hasAbnormalState(rightEffectList[x][y])){
                                                isMiss = false;
                                            }
                                            if(isMissNullified || (isAbnormalDebuffMiss.equals("") && !isMiss)){
                                                //计算伤害
                                                int damage = getDamage(leftCharList[monsterAttackerX][monsterAttackerY].c,rightCharList[x][y].c,false);
                                                setDamageOnCharacter(rightCharList[x][y].c,damage,true,true);
                                                sendDamageNumber(damage,
                                                        x,y,
                                                        (isRestrained(leftCharList[monsterAttackerX][monsterAttackerY].c,rightCharList[x][y].c)>=0)?TEXT_RED:TEXT_BLUE,true,true);

                                                //攻击时概率附带异常效果计算
                                                ArrayList<Effect> efList = leftEffectList[monsterAttackerX][monsterAttackerY];
                                                for(int j = 0; j < efList.size(); j++){
                                                    Effect e = efList.get(j);
                                                    switch(e.name){
                                                        case "攻击时给予状态毒": case "攻击时给予状态烧伤": case "攻击时给予状态诅咒":
                                                        case "攻击时给予状态魅惑": case "攻击时给予状态眩晕": case "攻击时给予状态拘束":
                                                        case "攻击时给予状态雾": case "攻击时给予状态黑暗": case "攻击时给予状态幻惑":
                                                        case "攻击时给予状态技能封印": case "攻击时给予状态Magia封印":
                                                        case "攻击时给予状态HP回复禁止": case "攻击时给予状态MP回复禁止":
                                                            //检查对方是否有该异常无效
                                                            boolean isEffectImmune = isTriggerEffect(rightEffectList[x][y],e.name.substring(7)+"无效");

                                                            //获取对方异常耐性
                                                            int abnormalStateResistance = calculateAbnormalStateResistance(rightEffectList[x][y]);
                                                            if(colorToss(e.probability - abnormalStateResistance)){
                                                                Effect e2 = new Effect();
                                                                if(!isEffectImmune){
                                                                    e2.name = e.name.substring(7);
                                                                    e2.time = e.valueTime;
                                                                    e2.value = e.value;
                                                                    rightEffectList[x][y].add(e2);
                                                                }else{
                                                                    e2.name = e.name.substring(7)+"无效";
                                                                }
                                                                sendEffect(e2,x,y,true,true);
                                                            }
                                                            break;
                                                        default:
                                                    }
                                                }
                                            }

                                            //效果字幕显示
                                            if(isMiss || !isAbnormalDebuffMiss.equals("")){
                                                if(isMissNullified){
                                                    //显示"回避无效"
                                                    Effect e = new Effect();
                                                    e.name = "回避无效";
                                                    sendEffect(e,x,y,true,true);
                                                }else{
                                                    //显示回避
                                                    Effect e = new Effect();
                                                    e.name = "回避";
                                                    sendEffect(e,x,y,true,true);
                                                }
                                            }

                                            if(monsterPlate == CharacterPlateView.CHARGE){
                                                enemyChargeNumber++;
                                            }else{
                                                enemyChargeNumber = 0;
                                            }
                                            if(rightCharList[x][y].c.realHP > 0){
                                                int defendMP = getDefendMP(leftCharList[monsterAttackerX][monsterAttackerY].c,rightCharList[x][y].c,true);
                                                setMpOnCharacter(rightCharList[x][y].c,rightCharList[x][y].c.realMP+defendMP,true);
                                                if((isMissNullified || (isAbnormalDebuffMiss.equals("") && !isMiss))){
                                                    rightCharList[x][y].spriteName = "damage";
                                                    changeSprite(x, y,true);
                                                    rightCharList[x][y].webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                                }
                                            }
                                        }
                                    },DELTA_BETWEEN_ATTACK_AND_DAMAGE);
                                }else if(monsterPlate == CharacterPlateView.BLAST_VERTICAL || monsterPlate == CharacterPlateView.BLAST_HORIZONTAL){
                                    if(!isBossBattle){
                                        leftCharList[monsterAttackerX][monsterAttackerY].spriteName = (monsterPlate == CharacterPlateView.BLAST_VERTICAL)?"attackv_out":"attackh_out";
                                        changeSprite(monsterAttackerX,monsterAttackerY,false);
                                        if(monsterPlate == CharacterPlateView.BLAST_VERTICAL){
                                            setCharacterAttackPosition(1,y,monsterAttackerX,monsterAttackerY,false);
                                        }else{
                                            setCharacterAttackPosition(x,1,monsterAttackerX,monsterAttackerY,false);
                                        }
                                        leftCharList[monsterAttackerX][monsterAttackerY].setVisibility(VISIBLE);
                                    }

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //攻击MP
                                            int mp = getAttackMP(leftCharList[monsterAttackerX][monsterAttackerY].c,false,monsterPlate);
                                            leftCharList[monsterAttackerX][monsterAttackerY].c.realMP += mp;

                                            //Blast攻击时MP获得
                                            int blastAttackMp = 0;
                                            ArrayList<Effect> efList = leftEffectList[monsterAttackerX][monsterAttackerY];
                                            for(int i = 0; i < efList.size(); i++){
                                                Effect e = efList.get(i);
                                                if(e.name.equals("Blast攻击时MP获得")){
                                                    blastAttackMp += e.value;
                                                }
                                            }

                                            for(int i = 0; i < 3; i++){
                                                SpriteViewer sv = (monsterPlate == CharacterPlateView.BLAST_VERTICAL)? rightCharList[i][y]:rightCharList[x][i];
                                                if(sv != null && sv.c.realHP > 0) {
                                                    //攻击者是否触发异常状态miss效果
                                                    String isAbnormalDebuffMiss = isTriggerEffect(leftEffectList[monsterAttackerX][monsterAttackerY],new String[]{"雾","黑暗","幻惑"});
                                                    //被攻击者是否触发回避效果
                                                    boolean isMiss = false;
                                                    if(monsterPlate == CharacterPlateView.BLAST_VERTICAL){
                                                        isMiss = isTriggerEffect(rightEffectList[i][y],"回避");
                                                        if(hasAbnormalState(rightEffectList[i][y])){
                                                            isMiss = false;
                                                        }
                                                    }else{
                                                        isMiss = isTriggerEffect(rightEffectList[x][i],"回避");
                                                        if(hasAbnormalState(rightEffectList[x][i])){
                                                            isMiss = false;
                                                        }
                                                    }
                                                    if(isMissNullified || (isAbnormalDebuffMiss.equals("") && !isMiss)){
                                                        //BlastMp
                                                        leftCharList[monsterAttackerX][monsterAttackerY].c.realMP += blastAttackMp;
                                                        //计算伤害
                                                        int damage = getDamage(leftCharList[monsterAttackerX][monsterAttackerY].c, sv.c, false);
                                                        setDamageOnCharacter(sv.c, damage, true,true);
                                                        if ((monsterPlate == CharacterPlateView.BLAST_VERTICAL)) {
                                                            sendDamageNumber(damage, i, y, (isRestrained(leftCharList[monsterAttackerX][monsterAttackerY].c, sv.c) >= 0)?TEXT_RED:TEXT_BLUE, true, true);
                                                        } else {
                                                            sendDamageNumber(damage, x, i, (isRestrained(leftCharList[monsterAttackerX][monsterAttackerY].c, sv.c) >= 0)?TEXT_RED:TEXT_BLUE, true, true);
                                                        }

                                                        //攻击时概率附带异常效果计算
                                                        for(int j = 0; j < efList.size(); j++){
                                                            Effect e = efList.get(j);
                                                            switch(e.name){
                                                                case "攻击时给予状态毒": case "攻击时给予状态烧伤": case "攻击时给予状态诅咒":
                                                                case "攻击时给予状态魅惑": case "攻击时给予状态眩晕": case "攻击时给予状态拘束":
                                                                case "攻击时给予状态雾": case "攻击时给予状态黑暗": case "攻击时给予状态幻惑":
                                                                case "攻击时给予状态技能封印": case "攻击时给予状态Magia封印":
                                                                case "攻击时给予状态HP回复禁止": case "攻击时给予状态MP回复禁止":
                                                                    //检查对方是否有该异常无效
                                                                    boolean isEffectImmune;
                                                                    //获取对方异常耐性
                                                                    int abnormalStateResistance;
                                                                    if(monsterPlate == CharacterPlateView.BLAST_VERTICAL){
                                                                        abnormalStateResistance = calculateAbnormalStateResistance(rightEffectList[i][y]);
                                                                        isEffectImmune = isTriggerEffect(rightEffectList[i][y],e.name.substring(7)+"无效");
                                                                    }else{
                                                                        abnormalStateResistance = calculateAbnormalStateResistance(rightEffectList[x][i]);
                                                                        isEffectImmune = isTriggerEffect(rightEffectList[x][i],e.name.substring(7)+"无效");
                                                                    }
                                                                    if(colorToss(e.probability - abnormalStateResistance)){
                                                                        Effect e2 = new Effect();
                                                                        if(!isEffectImmune){
                                                                            e2.name = e.name.substring(7);
                                                                            e2.time = e.valueTime;
                                                                            e2.value = e.value;
                                                                            if (monsterPlate == CharacterPlateView.BLAST_VERTICAL) {
                                                                                rightEffectList[i][y].add(e2);
                                                                                sendEffect(e2, i, y, true, true);
                                                                            }else{
                                                                                rightEffectList[x][i].add(e2);
                                                                                sendEffect(e2, x, i, true, true);
                                                                            }
                                                                        }else{
                                                                            e2.name = e.name.substring(7)+"无效";
                                                                            if (monsterPlate == CharacterPlateView.BLAST_VERTICAL) {
                                                                                sendEffect(e2, i, y, true, true);
                                                                            }else{
                                                                                sendEffect(e2, x, i, true, true);
                                                                            }
                                                                        }
                                                                    }
                                                                    break;
                                                                default:
                                                            }
                                                        }
                                                    }

                                                    //效果字幕显示
                                                    if(isMiss || !isAbnormalDebuffMiss.equals("")){
                                                        Effect e = new Effect();
                                                        if(isMissNullified){
                                                            //显示"回避无效"
                                                            e.name = "回避无效";
                                                        }else{
                                                            //显示回避
                                                            e.name = "回避";
                                                        }
                                                        if(monsterPlate == CharacterPlateView.BLAST_VERTICAL){
                                                            sendEffect(e,i,y,true,true);
                                                        }else{
                                                            sendEffect(e,x,i,true,true);
                                                        }
                                                    }

                                                    enemyChargeNumber = 0;
                                                    if(sv.c.realHP > 0){
                                                        int defendMP = getDefendMP(leftCharList[monsterAttackerX][monsterAttackerY].c,sv.c,true);
                                                        setMpOnCharacter(sv.c,sv.c.realMP+defendMP,true);
                                                        if((isMissNullified || (isAbnormalDebuffMiss.equals("") && !isMiss))){
                                                            sv.spriteName = "damage";
                                                            if((monsterPlate == CharacterPlateView.BLAST_VERTICAL)){
                                                                changeSprite(i, y,true);
                                                            }else{
                                                                changeSprite(x, i,true);
                                                            }
                                                            sv.webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                                        }
                                                    }
                                                }
                                            }
                                            setMpOnCharacter(leftCharList[monsterAttackerX][monsterAttackerY].c,leftCharList[monsterAttackerX][monsterAttackerY].c.realMP,false);
                                        }
                                    },DELTA_BETWEEN_ATTACK_AND_DAMAGE);
                                }
                                if(!isBossBattle){
                                    leftCharList[monsterAttackerX][monsterAttackerY].webView.loadUrl("javascript:setAnimationIndex(" + 4 + ")");
                                }else{
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Message m = new Message();
                                            m.what = 4;
                                            handler.sendMessage(m);
                                        }
                                    },2500);
                                }
                                break;
                            }else{
                                tempCount++;
                            }
                        }
                    }
                    break;
                case 4:
                    //敌方进攻out结束
                    leftCharList[monsterAttackerX][monsterAttackerY].spriteName = "stance";
                    changeSprite(monsterAttackerX,monsterAttackerY,false);
                    smallPlateNumber++;

                    //判读是否我方全部死亡
                    tempP = new int[]{-1,-1};
                    findAliveCharacter(tempP,true);
                    if(tempP[0] == -1) {
                        lose();
                    }else{
                        if(smallPlateNumber < 3){
                            recoverCharacterSize(true);
                            magnifyCharacter(false);
                            magnifyCharacter(false);
                            int monsterNumber = 0;
                            for(int i = 0; i < 3; i++){
                                for(int j = 0; j < 3; j++){
                                    if(monsterFormation[i][j] != null && monsterFormation[i][j].realHP > 0 && !hasAbnormalState(leftEffectList[i][j],new String[]{"魅惑","眩晕","拘束"})){
                                        monsterNumber++;
                                    }
                                }
                            }
                            if(monsterNumber == 0){
                                //没有可行动的敌人, 提前结束回合
                                //回合结束前结算异常效果和回血效果，并结束回合
                                calculateStateAndEndTurn();
                            }
                            int chooseMonster = (int)(Math.random()*monsterNumber);
                            int temp2Count = 0;
                            outerLoop:for(int i = 0; i < 3; i++){
                                for(int j = 0; j < 3; j++){
                                    if(monsterFormation[i][j] != null && monsterFormation[i][j].realHP > 0 && !hasAbnormalState(leftEffectList[i][j],new String[]{"魅惑","眩晕","拘束"})){
                                        if(temp2Count == chooseMonster){
                                            int plate = monsterFormation[i][j].plateList[(int)(Math.random()*5)];
                                            monsterPlate = plate;
                                            if(isBossBattle){
                                                monsterAttackerX = 1;
                                                monsterAttackerY = 1;
                                            }else{
                                                monsterAttackerX = i;
                                                monsterAttackerY = j;
                                            }
                                            final int tempi = monsterAttackerX;
                                            final int tempj = monsterAttackerY;
                                            if(plate == CharacterPlateView.ACCELE || plate == CharacterPlateView.CHARGE){
                                                leftCharList[monsterAttackerX][monsterAttackerY].spriteName = "attack_in";
                                            }else if(plate == CharacterPlateView.BLAST_HORIZONTAL){
                                                leftCharList[monsterAttackerX][monsterAttackerY].spriteName = "attackh_in";
                                            }else if(plate == CharacterPlateView.BLAST_VERTICAL){
                                                leftCharList[monsterAttackerX][monsterAttackerY].spriteName = "attackv_in";
                                            }
                                            leftCharList[monsterAttackerX][monsterAttackerY].webView.loadUrl("javascript:setAnimationIndex(" + 3 + ")");
                                            changeSprite(monsterAttackerX,monsterAttackerY, false);
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    leftStateBarList[tempi][tempj].setVisibility(GONE);
                                                }
                                            },500);
                                            break outerLoop;
                                        }else{
                                            temp2Count++;
                                        }

                                    }
                                }
                            }
                        }else{
                            //回合结束前结算异常效果和回血效果，并结束回合
                            calculateStateAndEndTurn();
                        }
                    }
                    break;
                default:
            }
            return true;
        }
    });

//    SpriteViewer background_effect;
    ImageView underlay;
    ImageView background;
    ImageView magiaPlate;
    ImageView skillPlate;
    ImageView backgroundLeft;
    ImageView backgroundRight;
    LinearLayout characterPlatesLayout;
    ConstraintLayout platesLayout;
    ConstraintLayout characterTouchLayout;
    ConstraintLayout characterStateLayout;
    ConstraintLayout skillEffectLayout;
    LinearLayout tipLayout;
    TextView tipTitle;
    TextView tipContent;
    CharacterPlateView[] charPlateViewList = new CharacterPlateView[5];
    Memoria[][] characterMemoria = new Memoria[5][2];
    ImageView[][][] skill__time = new ImageView[5][2][2];
    ImageView[] connectArrowView = new ImageView[5];
    LinearLayout skillBar;
    StrokeTextView charge_number_view;
    ImageView charge_frame;
    Plate[] plateList = new Plate[]{null,null,null,null,null};
    int[] smallPlateList = new int[]{-1,-1,-1}; // 对应plateList的下标,当值>4时说明是m/d盘，是StartActivity.characters[值-4]个角色的M/D盘
    int[] smallPlateXList = new int[]{-1,-1,-1}; // 被攻击者的位置X
    int[] smallPlateYList = new int[]{-1,-1,-1}; // 被攻击者的位置Y
    Character[] smallPlateConnectToList = new Character[]{null, null, null}; // 记录小盘子的连携情况

    ConstraintLayout effectDetailLayout;
    ImageView effectDetailCharAttr;
    TextView effectDetailCharName;
    TextView effectDetailHP;
    ImageView effectDetailHPBar;
    LinearLayout effectDetailEffectList;

    ImageView acceleCombo;
    ImageView chargeCombo;
    ImageView blastCombo;
    LinearLayout comboFrame;
    TextView comboText;
    StrokeTextView puellaCombo;

    ConstraintLayout backgroundLayout;
    ConstraintLayout skillDetailLayout;
    ImageView skillDetailIcon;
    TextView skillDetailTitle;
    TextView skillDetailCD;
    TextView skillDetailDescription;
    ImageView skillDetailInitialButton;
    ImageView skillDetailCancelButton;

    ConstraintLayout connectConstraintLayout;
    TextView connectDetailText;


    ConstraintLayout skill_launch_layout;
    TextView skill_launch_title;



    ConstraintLayout characterLayout;
    SpriteViewer[][] rightCharList = new SpriteViewer[3][3];
    SpriteViewer[][] leftCharList = new SpriteViewer[3][3];
    ImageView[][] leftTouchListLayout = new ImageView[3][3];
    ImageView[][] rightTouchListLayout = new ImageView[3][3];
    ImageView[][] touchImageListLayout = new ImageView[3][3];

    ImageView[] small_plate_background = new ImageView[3];
    ImageView[] small_plate_character = new ImageView[3];
    ImageView[] small_plate_attribute = new ImageView[3];
    ImageView[] small_plate_text = new ImageView[3];
    ImageView[] small_plate_arrow = new ImageView[3];
    ConstraintLayout[] small_plate = new ConstraintLayout[3];
    ImageView[] small_plate_right_arrow = new ImageView[3];
    ImageView[] small_plate_small_background = new ImageView[3];
    ImageView[] small_plate_small_character = new ImageView[3];

    ConstraintLayout[] skillLayout = new ConstraintLayout[5];
    ImageView[] skillCharacter = new ImageView[5];
    ImageView[][] skill_ = new ImageView[5][2];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        findView();
        initView();
    }

    public void findView(){
//        background_effect = findViewById(R.id.background_effect);
        underlay = findViewById(R.id.underlay);
        background = findViewById(R.id.background);
        magiaPlate = findViewById(R.id.magiaPlate);
        skillPlate = findViewById(R.id.skillPlate);
        backgroundLeft = findViewById(R.id.backgroundLeft);
        backgroundRight = findViewById(R.id.backgroundRight);
        characterPlatesLayout = findViewById(R.id.characterPlatesLayout);
        platesLayout = findViewById(R.id.platesLayout);
        characterTouchLayout = findViewById(R.id.characterTouchLayout);
        characterStateLayout = findViewById(R.id.characterStateLayout);
        skillEffectLayout = findViewById(R.id.skillEffectLayout);
        tipLayout = findViewById(R.id.tipLayout);
        tipTitle = findViewById(R.id.tipTitle);
        tipContent = findViewById(R.id.tipContent);
        skillBar = findViewById(R.id.skillBar);
        backgroundLayout = findViewById(R.id.backgroundLayout);
        skillDetailLayout = findViewById(R.id.skillDetailLayout);
        skillDetailIcon = findViewById(R.id.skillDetailIcon);
        skillDetailTitle = findViewById(R.id.skillDetailTitle);
        skillDetailCD = findViewById(R.id.skillDetailCD);
        skillDetailDescription = findViewById(R.id.skillDetailDescription);
        skillDetailInitialButton = findViewById(R.id.skillDetailInitialButton);
        skillDetailCancelButton = findViewById(R.id.skillDetailCancelButton);
        skill_launch_layout = findViewById(R.id.skill_launch_layout);
        skill_launch_title = findViewById(R.id.skill_launch_title);
        effectDetailLayout = findViewById(R.id.effectDetailLayout);
        effectDetailCharAttr = findViewById(R.id.effectDetailCharAttr);
        effectDetailCharName = findViewById(R.id.effectDetailCharName);
        effectDetailHP = findViewById(R.id.effectDetailHP);
        effectDetailHPBar = findViewById(R.id.effectDetailHPBar);
        effectDetailEffectList = findViewById(R.id.effectDetailEffectList);
        acceleCombo = findViewById(R.id.acceleCombo);
        chargeCombo = findViewById(R.id.chargeCombo);
        blastCombo = findViewById(R.id.blastCombo);
        comboFrame = findViewById(R.id.comboFrame);
        comboText = findViewById(R.id.comboText);
        puellaCombo = findViewById(R.id.puellaCombo);
        connectConstraintLayout = findViewById(R.id.connectConstraintLayout);
        connectDetailText = findViewById(R.id.connectDetailText);
        charge_number_view = findViewById(R.id.charge_number_view);
        charge_frame = findViewById(R.id.charge_frame);
        charPlateViewList = new CharacterPlateView[5];
        for(int i = 0; i < 5; i++){
            charPlateViewList[i] = findViewById(getIdByString("charPlateView"+i));
            skillLayout[i] = findViewById(getIdByString("skill"+i));
            skillCharacter[i] = findViewById(getIdByString("skillCharacter"+i));
            connectArrowView[i] = findViewById(getIdByString("connectArrowView"+i));
            for(int j = 0; j < 2; j++) {
                skill_[i][j] = findViewById(getIdByString("skill" + i + "_" + j));
                for(int k = 0; k < 2; k++){
                    skill__time[i][j][k] = findViewById(getIdByString("skill"+i+"_"+j+"_time"+k));
                }
            }
        }
        characterLayout = findViewById(R.id.characterLayout);
        for(int i = 0; i < 3; i++){
            small_plate[i] = findViewById(getIdByString("small_plate"+i));
            small_plate_background[i] = findViewById(getIdByString("small_plate"+i+"_background"));
            small_plate_character[i] = findViewById(getIdByString("small_plate"+i+"_character"));
            small_plate_attribute[i] = findViewById(getIdByString("small_plate"+i+"_attribute"));
            small_plate_text[i] = findViewById(getIdByString("small_plate"+i+"_text"));
            small_plate_arrow[i] = findViewById(getIdByString("small_plate"+i+"_arrow"));
            small_plate_small_background[i] = findViewById(getIdByString("small_plate"+i+"_small_background"));
            small_plate_small_character[i] = findViewById(getIdByString("small_plate"+i+"_small_character"));
        }
        for(int i = 0; i < 2; i++){
            small_plate_right_arrow[i] = findViewById(getIdByString("small_plate_right_arrow"+i));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initView(){
//        background_effect.charName = "street_day";
//        background_effect.spriteName = "all_f_f";
//        background_effect.prefix = "bg_quest_"; //对于背景:bg_quest_  对于人物:mini_
//        background_effect.canvasWidth = 1200;

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        grayColorFilter = new ColorMatrixColorFilter(cm);

        for(int i = 0; i < 5; i++){
            plateList[i] = null;
        }

        //初始化人物和敌人spriteView及相关view
        loadCharacter();

        //初始化详细效果界面取消按钮
        effectDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                effectDetailLayout.setVisibility(GONE);
            }
        });

        //初始化上方小盘
        for(int i = 0; i < 3; i++){
            small_plate[i].setVisibility(View.INVISIBLE);
        }
        small_plate_right_arrow[0].setVisibility(View.INVISIBLE);
        small_plate_right_arrow[1].setVisibility(View.INVISIBLE);



        //添加下方盘子点击事件
        for(int i = 0; i < 5; i++){
            final int temp = i;
            charPlateViewList[i].setOnTouchListener(new View.OnTouchListener() {
                int touchX;
                int touchY;
                boolean isDrag = false;
                boolean isChosed = false;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(clickable){
                        if(barState == PLATE_SHOW){
                            switch(event.getAction()){
                                case MotionEvent.ACTION_UP:
                                    if(!isDrag){
                                        if(smallPlateNumber < 3){
                                            if(isChosed){
                                                cancelSmallPlate(temp);
                                            }else{
                                                smallPlateXList[smallPlateNumber] = chooseMonsterX;
                                                smallPlateYList[smallPlateNumber] = chooseMonsterY;
                                                setSmallPlate(smallPlateNumber,temp,null);
                                                smallPlateNumber++;
                                                if(smallPlateNumber == 3){//说明三个盘选择完毕
                                                    prepareRightAttack();
                                                    clickable = false;
                                                }
                                            }
                                        }
                                        updateConnectArrow();
                                    }
                                    break;
                                case MotionEvent.ACTION_DOWN:
                                    isDrag = false;
                                    touchX = (int)event.getX();
                                    touchY = (int)event.getY();
                                    dragPlateId = -1;
                                    isChosed = false;
                                    for(int j = 0; j < 3; j++){
                                        if(smallPlateList[j] == temp){
                                            isChosed = true;
                                            break;
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if(!isDrag){
                                        if(!isChosed && (Math.abs(touchX - event.getX()) > 100 || Math.abs(touchY - event.getY()) > 100)){
                                            if(plateList[temp].c.diamondNumber == 3){
                                                boolean flag = false; // 是否该角色在前两个盘连携过了
                                                if(smallPlateNumber > 0){
                                                    for(int i = 0; i < smallPlateNumber; i++){
                                                        if(smallPlateList[i] <= 4){
                                                            if((plateList[smallPlateList[i]].c == plateList[temp].c) && smallPlateConnectToList != null){
                                                                flag = true;
                                                            }
                                                        }
                                                    }
                                                }
                                                if(!flag){
                                                    // 确定为连携
                                                    String connectText = "";
                                                    ArrayList<SkillEffect> tempArraylist = (plateList[temp].c.star == 4)? plateList[temp].c.connectOriginEffectList:plateList[temp].c.connectAfterEffectList;
                                                    for(int i = 0; i < tempArraylist.size(); i++){
                                                        if(i != 0){
                                                            connectText += " & ";
                                                        }
                                                        connectText += tempArraylist.get(i).getEffectDescription();
                                                    }
                                                    connectDetailText.setText(connectText);
                                                    connectConstraintLayout.setVisibility(VISIBLE);
                                                    isDrag = true;
                                                    dragPlateId = temp;
                                                    v.startDrag(null,new View.DragShadowBuilder(v),v, 0);
                                                    v.setVisibility(INVISIBLE);
                                                    setAllRightCharacterShader(0.5f);
                                                }
                                            }
                                        }
                                    }
                                    break;
                            }
                        }else if(barState == MAGIA_BAR_SHOW){
                            switch(event.getAction()){
                                case MotionEvent.ACTION_UP:
                                    if(smallPlateNumber < 3){
                                        if(isChosed){
                                            cancelSmallPlate(temp+5);
                                        }else{
                                            smallPlateXList[smallPlateNumber] = chooseMonsterX;
                                            smallPlateYList[smallPlateNumber] = chooseMonsterY;
                                            setSmallPlate(smallPlateNumber,temp+5,null);
                                            smallPlateNumber++;
                                            if(smallPlateNumber == 3){//说明三个盘选择完毕
                                                prepareRightAttack();
                                                clickable = false;
                                            }
                                        }
                                    }
                                    break;
                                case MotionEvent.ACTION_DOWN:
                                    isChosed = false;
                                    for(int j = 0; j < 3; j++){
                                        if(smallPlateList[j] == (temp+5)){
                                            isChosed = true;
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }

                    }
                    return true;
                }
            });
        }

        //添加右侧人物连携事件
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(rightTouchListLayout[i][j] != null && rightCharList[i][j].c.realHP > 0){
                    final int tempI = i;
                    final int tempJ = j;
                    rightTouchListLayout[i][j].setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View v, DragEvent event) {
                            if(rightCharList[tempI][tempJ].c.realHP > 0){
                                switch(event.getAction()){
                                    case DragEvent.ACTION_DRAG_ENTERED:
//                                    Log.i("Sam", "拖拽的view进入"+v.getClass().getSimpleName());
                                        rightCharList[tempI][tempJ].webView.setAlpha(1f);
                                        rightCharList[plateList[dragPlateId].c.formationX][plateList[dragPlateId].c.formationY].spriteName = "stance_con";
                                        changeSprite(plateList[dragPlateId].c.formationX,plateList[dragPlateId].c.formationY,true);
                                        break;
                                    case DragEvent.ACTION_DRAG_EXITED:
//                                    Log.i("Sam", "拖拽的view离开"+v.getClass().getSimpleName());
                                        rightCharList[plateList[dragPlateId].c.formationX][plateList[dragPlateId].c.formationY].spriteName = getRecoverOriginSpriteName(plateList[dragPlateId].c.formationX,plateList[dragPlateId].c.formationY,true, "wait", false);
                                        changeSprite(plateList[dragPlateId].c.formationX,plateList[dragPlateId].c.formationY,true);
                                        rightCharList[tempI][tempJ].webView.setAlpha(0.5f);
                                        break;
                                    case DragEvent.ACTION_DRAG_LOCATION:
                                        //Log.d("Sam","dragOn "+v.getClass().getSimpleName()+", x:"+event.getX()+", y:"+event.getY());
                                        break;
                                    case DragEvent.ACTION_DROP:
                                        if(smallPlateNumber < 3 && plateList[dragPlateId].c != rightCharList[tempI][tempJ].c){
                                            smallPlateXList[smallPlateNumber] = chooseMonsterX;
                                            smallPlateYList[smallPlateNumber] = chooseMonsterY;
                                            setSmallPlate(smallPlateNumber,dragPlateId,rightCharList[tempI][tempJ].c);
                                            smallPlateNumber++;
                                            if(smallPlateNumber == 3){//说明三个盘选择完毕
                                                prepareRightAttack();
                                            }
                                            updateConnectArrow();
                                        }
                                        break;
                                    case DragEvent.ACTION_DRAG_ENDED:
                                        ((View)event.getLocalState()).setVisibility(VISIBLE);
                                        setAllRightCharacterShader(1f);
                                        connectConstraintLayout.setVisibility(GONE);
                                        rightCharList[plateList[dragPlateId].c.formationX][plateList[dragPlateId].c.formationY].spriteName = getRecoverOriginSpriteName(plateList[dragPlateId].c.formationX,plateList[dragPlateId].c.formationY,true, "wait", false);
                                        changeSprite(plateList[dragPlateId].c.formationX,plateList[dragPlateId].c.formationY,true);
                                        break;
                                }
                            }
                            return true;
                        }
                    });
                }
            }
        }



        //添加左侧敌人点击逻辑
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(touchImageListLayout[i][j] != null && leftCharList[i][j].c.realHP > 0){
                    final int tempX = i;
                    final int tempY = j;
                    if(chooseMonsterX == -1){
                        chooseMonsterX = i;
                        chooseMonsterY = j;
                        touchImageListLayout[i][j].setVisibility(VISIBLE);
                    }
                    leftTouchListLayout[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(clickable && platesLayout.getVisibility() == VISIBLE){
                                if(leftCharList[tempX][tempY].c.realHP > 0){
                                    touchImageListLayout[chooseMonsterX][chooseMonsterY].setVisibility(GONE);
                                    chooseMonsterX = tempX;
                                    chooseMonsterY = tempY;
                                    touchImageListLayout[chooseMonsterX][chooseMonsterY].setVisibility(VISIBLE);
                                }
                            }
                        }
                    });
                }
            }
        }


        magiaPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(barState == PLATE_SHOW){
                    barState = MAGIA_BAR_SHOW;
                    showPlate();
                    magiaPlate.setBackgroundResource(R.drawable.magia_plate_back);
                    skillPlate.setVisibility(INVISIBLE);
                }else{
                    //收起magiaPlate栏
                    barState = PLATE_SHOW;
                    magiaPlate.setBackgroundResource(R.drawable.magia_plate);
                    skillPlate.setVisibility(VISIBLE);
                    showPlate();
                }
            }
        });



        StartActivity.clearCharBattleInfo();//重置行动盘积累数和顺序

        //技能栏设置
        skillPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickable){
                    if(barState == PLATE_SHOW){
                        //展开技能栏
                        barState = SKILL_BAR_SHOW;
                        showPlate();
                        skillPlate.setBackgroundResource(R.drawable.skill_plate_back);
                        characterPlatesLayout.setVisibility(GONE);
                        skillBar.setVisibility(VISIBLE);
                        magiaPlate.setVisibility(INVISIBLE);
                        for(int i = 0; i < 5; i++){
                            if(StartActivity.characters[i] != null && StartActivity.characters[i].realHP > 0){
                                skillLayout[i].setVisibility(VISIBLE);
                                Character c = StartActivity.characters[i];
                                skillCharacter[i].setBackgroundResource(getImageByString(c.charIconImage+"s"));
                                int memoriaNumber = 0;
                                Memoria m1 = null;
                                Memoria m2 = null;
                                for(int j = 0; j < 4; j++){
                                    if(c.memoriaList[j] != null){
                                        if(c.memoriaList[j].DEFOrigin != 0){
                                            //说明是一张技能型记忆
                                            if(m1 == null){
                                                m1 = c.memoriaList[j];
                                            }else{
                                                m2 = c.memoriaList[j];
                                            }
                                        }
                                        memoriaNumber++;
                                    }
                                }
                                characterMemoria[i][0] = m1;
                                characterMemoria[i][1] = m2;
                                if(characterMemoria[i][0] != null){
                                    skill_[i][0].setImageResource(getImageByString(characterMemoria[i][0].icon));
                                    setCharacterSkillTime(i,0,characterMemoria[i][0].CDNow);
                                    final int temp = i;
                                    skill_[i][0].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(clickable){
                                                setSkillDetail(temp,0);
                                            }
                                        }
                                    });
                                }else if(c.breakThrough - memoriaNumber >= 1){
                                    skill_[i][0].setImageResource(R.drawable.skill_not_equipped);
                                    setCharacterSkillTime(i,0,0);
                                }else{
                                    skill_[i][0].setImageResource(R.drawable.skill_locked);
                                    setCharacterSkillTime(i,0,0);
                                }
                                if(characterMemoria[i][1] != null){
                                    skill_[i][1].setImageResource(getImageByString(characterMemoria[i][1].icon));
                                    setCharacterSkillTime(i,1,characterMemoria[i][1].CDNow);
                                    final int temp = i;
                                    skill_[i][1].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(clickable){
                                                setSkillDetail(temp,1);
                                            }
                                        }
                                    });
                                }else if(c.breakThrough - memoriaNumber + (characterMemoria[i][0] == null? 0:1)>= 2){
                                    skill_[i][1].setImageResource(R.drawable.skill_not_equipped);
                                    setCharacterSkillTime(i,1,0);
                                }else{
                                    skill_[i][1].setImageResource(R.drawable.skill_locked);
                                    setCharacterSkillTime(i,1,0);
                                }
                            }else{
                                skillLayout[i].setVisibility(INVISIBLE);
                            }
                        }
                    }else if(barState == SKILL_BAR_SHOW){
                        //收起技能栏
                        barState = PLATE_SHOW;
                        showPlate();
                        skillPlate.setBackgroundResource(R.drawable.skill_plate);
                        characterPlatesLayout.setVisibility(VISIBLE);
                        skillBar.setVisibility(INVISIBLE);
                        magiaPlate.setVisibility(VISIBLE);

                    }
                }

            }
        });
        skillDetailCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickable){
                    skillDetailLayout.setVisibility(GONE);
                }
            }
        });


        //初始化下方技能黑幕
        skill_launch_layout.setVisibility(GONE);

        //角色被动记忆效果添加
        for(int i = 0; i < 5; i++){
            if(StartActivity.characters[i] != null && StartActivity.characters[i].realHP > 0) {
                for(int j = 0; j < 4; j++){
                    Memoria m = StartActivity.characters[i].memoriaList[j];
                    if(m != null && m.DEFOrigin == 0){
                        //说明是被动记忆
                        for(SkillEffect se : (StartActivity.characters[i].breakThrough == 4? m.effectAfterList:m.effectOriginList)){
                            Effect e = convertSkillEffectToEffect(se, 0);
                            if (e != null){
                                switch(e.name){
                                        case "以MP积累状态开始战斗":
                                            setMpOnCharacter(StartActivity.characters[i],(int)(1.0f*e.value*StartActivity.characters[i].getMaxMp()/100),true);
                                            break;
                                    default:
                                        rightEffectList[StartActivity.characters[i].formationX][StartActivity.characters[i].formationY].add(e);
                                }
                            }
                        }
                    }
                }
            }
        }

        //阵形效果添加
        Formation f = StartActivity.formationList.get(TeamChooseActivity.usingFormationId);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(f.gridAllEffectList[i][j].size() > 0 && rightEffectList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                    for(int k = 0; k < f.gridAllEffectList[i][j].size(); k++){
                        SkillEffect se = f.gridAllEffectList[i][j].get(k);
                        if(!se.name.equals("攻击力UP") && !se.name.equals("防御力UP") && !se.name.equals("攻击力DOWN") && !se.name.equals("防御力DOWN")){
                            rightEffectList[i][j].add(convertSkillEffectToEffect(se, 0));
                        }
                    }
                }
            }
        }

        //初始化ChargeView
        updateChargeView();

    }

    public void setSkillDetail(int c, int skill){// skill:0或1
        skillDetailIcon.setBackgroundResource(getImageByString(characterMemoria[c][skill].icon));
        skillDetailTitle.setText(characterMemoria[c][skill].name);
        skillDetailCD.setText(""+(characterMemoria[c][skill].breakthrough == 4? characterMemoria[c][skill].CDAfter:characterMemoria[c][skill].CDOrigin));
        skillDetailDescription.setText(characterMemoria[c][skill].getEffectDescription());
        skillDetailLayout.setVisibility(VISIBLE);
        if(characterMemoria[c][skill].CDNow == 0 && !hasAbnormalState(rightEffectList[StartActivity.characters[c].formationX][StartActivity.characters[c].formationY],new String[]{"技能封印"})){
            final Memoria m = characterMemoria[c][skill];
            final int tempC = c;
            final int tempSkill = skill;
            skillDetailInitialButton.getBackground().setColorFilter(null);
            skillDetailInitialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickable){
                        //释放技能
                        clickable = false;
                        skill_launch_title.setText(m.name);
                        skill_launch_layout.setVisibility(VISIBLE);

                        skillDetailLayout.setVisibility(GONE);
                        m.CDNow = m.breakthrough == 4? m.CDAfter:m.CDOrigin;

                        skill_[tempC][tempSkill].setImageResource(getImageByString(characterMemoria[tempC][tempSkill].icon));
                        setCharacterSkillTime(tempC,tempSkill,characterMemoria[tempC][tempSkill].CDNow);

                        final Character c = StartActivity.characters[tempC];
                        rightCharList[c.formationX][c.formationY].spriteName = "activate";
                        changeSprite(c.formationX,c.formationY,true);
                        for(int i = 0; i < (m.breakthrough == 4? m.effectAfterList.size():m.effectOriginList.size()); i++){
                            final int tempI = i;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //播放受众对应动画
                                    if((m.breakthrough == 4? m.effectAfterList:m.effectOriginList).get(tempI).target.equals("敌单")){
                                        if(!isBossBattle){
                                            leftCharList[chooseMonsterX][chooseMonsterY].spriteName = "damage";
                                            changeSprite(chooseMonsterX,chooseMonsterY,false);
                                        }else{
                                            leftCharList[1][1].spriteName = "damage";
                                            changeSprite(1,1,false);
                                        }

                                        int abnormalStateResistance = calculateAbnormalStateResistance(leftEffectList[chooseMonsterX][chooseMonsterY]);
                                        Effect e = convertSkillEffectToEffect((m.breakthrough == 4? m.effectAfterList:m.effectOriginList).get(tempI), abnormalStateResistance);
                                        if(e != null){
                                            handleEffectiveness(e,chooseMonsterX,chooseMonsterY,false,false);
                                        }
                                    }else if((m.breakthrough == 4? m.effectAfterList:m.effectOriginList).get(tempI).target.equals("敌全")){
                                        for(int i = 0; i < 3; i++){
                                            for(int j = 0; j < 3; j++){
                                                if(leftCharList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                                                    if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                                                        leftCharList[i][j].spriteName = "damage";
                                                        changeSprite(i,j,false);
                                                        int abnormalStateResistance = calculateAbnormalStateResistance(leftEffectList[i][j]);
                                                        Effect e = convertSkillEffectToEffect((m.breakthrough == 4? m.effectAfterList:m.effectOriginList).get(tempI),abnormalStateResistance);
                                                        if(e != null){
                                                            handleEffectiveness(e,i,j,false,false);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }else if((m.breakthrough == 4? m.effectAfterList:m.effectOriginList).get(tempI).target.equals("己全")){
                                        for(int i = 0; i < 3; i++){
                                            for(int j = 0; j < 3; j++){
                                                if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                                                    if(i != c.formationX || j != c.formationY){
                                                        rightCharList[i][j].spriteName = "reaction";
                                                        changeSprite(i,j,true);
                                                    }
                                                    int abnormalStateResistance = calculateAbnormalStateResistance(rightEffectList[i][j]);
                                                    Effect e = convertSkillEffectToEffect((m.breakthrough == 4? m.effectAfterList:m.effectOriginList).get(tempI),abnormalStateResistance);
                                                    if(e != null){
                                                        handleEffectiveness(e,i,j,true,false);
                                                    }
                                                }
                                            }
                                        }
                                    }else{
                                        //自
                                        int abnormalStateResistance = calculateAbnormalStateResistance(rightEffectList[c.formationX][c.formationY]);
                                        Effect e = convertSkillEffectToEffect((m.breakthrough == 4? m.effectAfterList:m.effectOriginList).get(tempI),abnormalStateResistance);
                                        if(e != null){
                                            handleEffectiveness(e,c.formationX,c.formationY,true,false);
                                        }
                                    }

                                    //如果是最后一个，则计划结束技能释放
                                    if(tempI == ((m.breakthrough == 4? m.effectAfterList:m.effectOriginList).size() - 1)){
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                clickable = true;
                                                skill_launch_layout.setVisibility(GONE);
                                                //恢复小人动画
                                                for(int i = 0; i < 3; i++){
                                                    for(int j = 0; j < 3; j++){
                                                        if(leftCharList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                                                            if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                                                                leftCharList[i][j].spriteName = getRecoverOriginSpriteName(i,j,false,"wait",false);
                                                                changeSprite(i, j, false);
                                                            }
                                                        }
                                                        if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                                                            rightCharList[i][j].spriteName = getRecoverOriginSpriteName(i,j,true,"wait",false);
                                                            changeSprite(i, j, true);
                                                        }
                                                    }
                                                }
                                                //
                                            }
                                        }, 1500);
                                    }
                                }
                            }, DELTA_BETWEEN_EFFECT_SHOW*(i+1));
                        }
                    }
                }
            });
        }else{
            skillDetailInitialButton.getBackground().setColorFilter(grayColorFilter);
            skillDetailInitialButton.setOnClickListener(null);
        }

    }

    public void loadCharacter(){
        Formation formation = StartActivity.formationList.get(TeamChooseActivity.usingFormationId);

        Intent receivedIntent = getIntent();
        BattleInfo bi = StartActivity.battleInfoList.get(receivedIntent.getIntExtra("battleInfo",0));

        monsterFormation = bi.monsterFormation;
        isBossBattle = bi.isBossBattle;


        int count = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(formation.grid[i][j] >= 1){
                    if(StartActivity.characters[count] != null && StartActivity.characters[count].realHP > 0){
                        StartActivity.characters[count].formationX = i;
                        StartActivity.characters[count].formationY = j;
                        createNewSprite(i,j, true,false);
                        rightCharList[i][j].charName = StartActivity.characters[count].spriteName;
                        rightCharList[i][j].spriteName = "wait";
                        rightCharList[i][j].prefix = "mini_";
                        rightCharList[i][j].c = StartActivity.characters[count];
                        rightStateBarList[i][j].updateHp(rightCharList[i][j].c.getRealMaxHP(),rightCharList[i][j].c.realHP);
                        rightStateBarList[i][j].updateMp(rightCharList[i][j].c.realMP);
                        rightStateBarList[i][j].setAttr(StartActivity.characters[count].element);
//                        rightEffectList[i][j] = new ArrayList<>();
                        rightEffectList[i][j] = StartActivity.characters[count].initialEffectList;
                        final int tempCount = count;
                        rightTouchListLayout[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                if(clickable){
                                    effectDetailCharAttr.setBackgroundResource(getImageByString(StartActivity.characters[tempCount].element));
                                    effectDetailCharName.setText(StartActivity.characters[tempCount].name);
                                    effectDetailHP.setText(StartActivity.characters[tempCount].realHP + "/" + StartActivity.characters[tempCount].getRealMaxHP());
                                    int realHp = StartActivity.characters[tempCount].realHP;
                                    if(((int)(1.0f*realHp/StartActivity.characters[tempCount].getRealMaxHP()*240)) <= 0){
                                        effectDetailHPBar.setVisibility(INVISIBLE);
                                    }else{
                                        effectDetailHPBar.setVisibility(VISIBLE);
                                        ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) effectDetailHPBar.getLayoutParams();
                                        p.width = (int)(1.0f*realHp/StartActivity.characters[tempCount].getRealMaxHP()*240);
                                        effectDetailHPBar.setLayoutParams(p);
                                    }
                                    effectDetailEffectList.removeAllViews();
                                    if(rightEffectList[StartActivity.characters[tempCount].formationX][StartActivity.characters[tempCount].formationY].size() > 0){
                                        for(int i = 0; i < rightEffectList[StartActivity.characters[tempCount].formationX][StartActivity.characters[tempCount].formationY].size(); i++){
                                            Effect e = rightEffectList[StartActivity.characters[tempCount].formationX][StartActivity.characters[tempCount].formationY].get(i);
                                            EffectDetailLayout edl = new EffectDetailLayout(BattleActivity.this,e);
                                            effectDetailEffectList.addView(edl);
                                        }
                                    }
                                    effectDetailLayout.setVisibility(VISIBLE);
                                }
                                return true;
                            }
                        });
                    }
                    count++;
                }
                if(monsterFormation[i][j] != null && monsterFormation[i][j].realHP > 0){

                    createNewSprite(i,j, false, !monsterFormation[i][j].spriteName.startsWith("monster"));
                    leftCharList[i][j].charName = monsterFormation[i][j].spriteName;
                    leftCharList[i][j].spriteName = "wait";
                    leftCharList[i][j].prefix = "mini_";
                    leftCharList[i][j].c = monsterFormation[i][j];
                    if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                        monsterFormation[i][j].formationX = i;
                        monsterFormation[i][j].formationY = j;
                        leftStateBarList[i][j].updateHp(leftCharList[i][j].c.getRealMaxHP(),leftCharList[i][j].c.realHP);
                        leftStateBarList[i][j].updateMp(leftCharList[i][j].c.realMP);
                        leftStateBarList[i][j].setAttr(monsterFormation[i][j].element);
                    }
                    leftEffectList[i][j] = monsterFormation[i][j].initialEffectList;
                    final int tempCount = count;
                    final Character mon = monsterFormation[i][j];
                    leftTouchListLayout[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if(clickable){
                                effectDetailCharAttr.setBackgroundResource(getImageByString(mon.element));
                                effectDetailCharName.setText(mon.name);
                                effectDetailHP.setText(mon.realHP + "/" + mon.HP);
                                int realHp = mon.realHP;
                                if(((int)(1.0f*realHp/mon.HP*240))<=0){
                                    effectDetailHPBar.setVisibility(INVISIBLE);
                                }else{
                                    effectDetailHPBar.setVisibility(VISIBLE);
                                    ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) effectDetailHPBar.getLayoutParams();
                                    p.width = (int)(1.0f*realHp/mon.HP*240);
                                    effectDetailHPBar.setLayoutParams(p);
                                }
                                effectDetailEffectList.removeAllViews();
                                if(isBossBattle){
                                    if(leftEffectList[1][1].size() > 0){
                                        for(int i = 0; i < leftEffectList[1][1].size(); i++){
                                            Effect e = leftEffectList[1][1].get(i);
                                            EffectDetailLayout edl = new EffectDetailLayout(BattleActivity.this,e);
                                            effectDetailEffectList.addView(edl);
                                        }
                                    }
                                }else{
                                    if(leftEffectList[mon.formationX][mon.formationY].size() > 0){
                                        for(int i = 0; i < leftEffectList[mon.formationX][mon.formationY].size(); i++){
                                            Effect e = leftEffectList[mon.formationX][mon.formationY].get(i);
                                            EffectDetailLayout edl = new EffectDetailLayout(BattleActivity.this,e);
                                            effectDetailEffectList.addView(edl);
                                        }
                                    }
                                }

                                effectDetailLayout.setVisibility(VISIBLE);
                            }
                            return true;
                        }
                    });
                }
            }
        }
    }


    public boolean randomChoosePlates(){
        //返回false时说明没有可行动角色
        ArrayList<Character> chosenCharacterList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            Character c = StartActivity.characters[i];
            if(c != null && c.realHP > 0 && !hasAbnormalState(rightEffectList[c.formationX][c.formationY],new String[]{"魅惑","眩晕","拘束"})){
                chosenCharacterList.add(StartActivity.characters[i]);
            }
        }
        if(chosenCharacterList.size() == 0){
            //说明没有可行动角色
            return false;
        }
        if(chosenCharacterList.size() > 3){
            Collections.sort(chosenCharacterList, new ActionOrderComparator());
            while(chosenCharacterList.size() > 3){
                chosenCharacterList.remove(chosenCharacterList.size()-1);
            }
        }
        int[] plateIdList = new int[]{-1,-1,-1,-1,-1};
        for(int i = 0; i < 5; i++){
            int choosePlate = (int)(Math.random()*chosenCharacterList.size()*5);
            while(isInList(plateIdList,choosePlate)){
                choosePlate = (int)(Math.random()*chosenCharacterList.size()*5);
            }
            plateIdList[i] = choosePlate;
            plateList[i] = new Plate();
            plateList[i].c = chosenCharacterList.get(choosePlate/5);
            plateList[i].plate = plateList[i].c.plateList[choosePlate % 5];
        }
        return true;
    }

    private boolean isInList(int[] list, int number){
        for(int i = 0; i < list.length; i++){
            if(list[i] == number){
                return true;
            }
        }
        return false;
    }

    public void showPlate(){
        if(barState == MAGIA_BAR_SHOW){
            for(int i = 0; i < charPlateViewList.length; i++){
                charPlateViewList[i].cancelShader();
                if(StartActivity.characters[i] != null && StartActivity.characters[i].realHP > 0){
                    if(StartActivity.characters[i].realMP >= 1000 && !hasAbnormalState(rightEffectList[StartActivity.characters[i].formationX][StartActivity.characters[i].formationY],new String[]{"magia封印"})){
                        charPlateViewList[i].setVisibility(VISIBLE);
                        charPlateViewList[i].setPlate(StartActivity.characters[i],(StartActivity.characters[i].realMP >= DOPPEL_NEED_MP)? DOPPEL:MAGIA);
                    }else{
                        charPlateViewList[i].setVisibility(INVISIBLE);
                    }
                }else{
                    charPlateViewList[i].setVisibility(INVISIBLE);
                }
            }
        }else{
            for(int i = 0; i < charPlateViewList.length; i++){
                charPlateViewList[i].cancelShader();
                charPlateViewList[i].setVisibility(VISIBLE);
                charPlateViewList[i].setPlate(plateList[i].c,plateList[i].plate);
            }
        }

        for(int i = 0; i < smallPlateList.length; i++){
            if(barState == PLATE_SHOW){
                if(smallPlateList[i] <= 4 && smallPlateList[i] != -1){
                    charPlateViewList[smallPlateList[i]].setShader();
                }
            }else if(barState == MAGIA_BAR_SHOW){
                if(smallPlateList[i] > 4){
                    charPlateViewList[smallPlateList[i] - 5].setShader();
                }
            }

        }
        updateConnectArrow();
    }

    public void updateConnectArrow(){
        outerFor:for(int i = 0; i < charPlateViewList.length; i++){
            if(connectArrowView[i].getAnimation() != null){
                connectArrowView[i].getAnimation().setAnimationListener(null);
                connectArrowView[i].clearAnimation();
                if(barState == MAGIA_BAR_SHOW || barState == SKILL_BAR_SHOW){
                    connectArrowView[i].setVisibility(INVISIBLE);
                }
            }
            if(barState == PLATE_SHOW){
                if(plateList[i].c.diamondNumber == 3){
                    for(int j = 0; j < smallPlateList.length; j++){
                        if(smallPlateList[j] != -1 && smallPlateConnectToList[j] != null && smallPlateList[j] <= 4 && smallPlateConnectToList[j].realHP > 0){
                            if(plateList[smallPlateList[j]].c == plateList[i].c){
                                //已选择相同角色的连携
                                connectArrowView[i].setVisibility(INVISIBLE);
                                continue outerFor;
                            }
                        }
                    }
                    connectArrowView[i].setVisibility(VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(this, R.anim.connect_arrow);
                    final int tempI = i;
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            connectArrowView[tempI].startAnimation(animation);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    connectArrowView[i].startAnimation(animation);
                }else{
                    connectArrowView[i].setVisibility(INVISIBLE);
                }
            }
        }
    }

    public int getIdByString(String name){
        Resources res = getResources();
        return res.getIdentifier(name,"id",getPackageName());
    }
 
    public void createNewSprite(int x, int y, boolean isRight, boolean needScaleX){
        if(isRight || !isBossBattle || (x == 1 && y == 1)){
            totalSpriteNumber++;
            if(isRight && (rightCharList[x][y] != null) && rightCharList[x][y].c.realHP > 0){
                characterLayout.removeView(rightCharList[x][y]);
                rightCharList[x][y].webView.loadUrl("javascript:endGame()");
                rightCharList[x][y] = null;
            }
            if(!isRight && (leftCharList[x][y] != null) && leftCharList[x][y].c.realHP > 0){
                characterLayout.removeView(leftCharList[x][y]);
                leftCharList[x][y].webView.loadUrl("javascript:endGame()");
                leftCharList[x][y] = null;
            }
            SpriteViewer sp = new SpriteViewer(this, false);
            sp.setZ(x);
            ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(CHARACTER_NORMAL_SIZE,CHARACTER_NORMAL_SIZE);
            sp.setLayoutParams(p);
            sp.setId(View.generateViewId());
            characterLayout.addView(sp);
            ConstraintSet sampleSet = new ConstraintSet();
            sampleSet.clone(characterLayout);
            if(isRight){
                sampleSet.connect(sp.getId(),ConstraintSet.START,characterLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1));
            }else{
                sampleSet.connect(sp.getId(),ConstraintSet.END,characterLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1));
            }
            sampleSet.connect(sp.getId(),ConstraintSet.BOTTOM,characterLayout.getId(),ConstraintSet.BOTTOM,130*(2-x));
            sampleSet.applyTo(characterLayout);
            sp.setVisibility(View.VISIBLE);
            if(isRight){
                rightCharList[x][y] = sp;
            }else{
                leftCharList[x][y] = sp;
//            sp.setPivotX(0);
                if(needScaleX){
                    leftCharList[x][y].setScaleX(-1);
                }
            }

            if(isRight){
                StateBar sb = new StateBar(this);
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                sb.setLayoutParams(lp);
                sb.setId(View.generateViewId());
                characterTouchLayout.addView(sb);
                ConstraintSet set = new ConstraintSet();
                set.clone(characterTouchLayout);
                set.connect(sb.getId(),ConstraintSet.START,characterTouchLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1)+CHARACTER_NORMAL_SIZE*3/7);
                set.connect(sb.getId(),ConstraintSet.BOTTOM,characterTouchLayout.getId(),ConstraintSet.BOTTOM,130*(2-x)+CHARACTER_NORMAL_SIZE*2/9+270);
                set.applyTo(characterTouchLayout);
//            imageView.setBackgroundResource(R.color.colorPurple);
                rightStateBarList[x][y] = sb;
                sb.setVisibility(View.VISIBLE);
            }else{
                StateBar sb = new StateBar(this);

                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                sb.setLayoutParams(lp);
                sb.setId(View.generateViewId());
                characterTouchLayout.addView(sb);
                ConstraintSet set = new ConstraintSet();
                set.clone(characterTouchLayout);
                set.connect(sb.getId(),ConstraintSet.END,characterTouchLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1)+(CHARACTER_NORMAL_SIZE-170)/2);
                set.connect(sb.getId(),ConstraintSet.BOTTOM,characterTouchLayout.getId(),ConstraintSet.BOTTOM,130*(2-x)+CHARACTER_NORMAL_SIZE*2/9+270);
                set.applyTo(characterTouchLayout);
//            imageView.setBackgroundResource(R.color.colorPurple);
                leftStateBarList[x][y] = sb;
                sb.setVisibility(View.VISIBLE);
            }
        }else{
            leftCharList[x][y] = new SpriteViewer(this,true);
        }



        if(!isRight){
            //添加触控面积
            ImageView imageView = new ImageView(this);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(200,200);
            imageView.setLayoutParams(lp);
            imageView.setId(View.generateViewId());
            characterTouchLayout.addView(imageView);
            ConstraintSet set = new ConstraintSet();
            set.clone(characterTouchLayout);
            set.connect(imageView.getId(),ConstraintSet.END,characterTouchLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1)+(CHARACTER_NORMAL_SIZE-170)/2);
            set.connect(imageView.getId(),ConstraintSet.BOTTOM,characterTouchLayout.getId(),ConstraintSet.BOTTOM,130*(2-x)+CHARACTER_NORMAL_SIZE*2/9);
            set.applyTo(characterTouchLayout);
//            imageView.setBackgroundResource(R.color.colorPurple);
            leftTouchListLayout[x][y] = imageView;
            imageView.setVisibility(View.VISIBLE);

            //添加触控箭头
            ImageView imageView2 = new ImageView(this);
            ConstraintLayout.LayoutParams lp2 = new ConstraintLayout.LayoutParams(105,77);
            imageView2.setLayoutParams(lp2);
            imageView2.setId(View.generateViewId());
            characterTouchLayout.addView(imageView2);
            ConstraintSet set2 = new ConstraintSet();
            set2.clone(characterTouchLayout);
            set2.connect(imageView2.getId(),ConstraintSet.END,characterTouchLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1)+(CHARACTER_NORMAL_SIZE-105)/2);
            set2.connect(imageView2.getId(),ConstraintSet.BOTTOM,characterTouchLayout.getId(),ConstraintSet.BOTTOM,130*(2-x)+CHARACTER_NORMAL_SIZE*2/9+340);
            set2.applyTo(characterTouchLayout);
            imageView2.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.character_chosen_plate));
            touchImageListLayout[x][y] = imageView2;
            imageView2.setVisibility(View.GONE);
        }else{
            //添加触控面积
            ImageView imageView = new ImageView(this);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(200,200);
            imageView.setLayoutParams(lp);
            imageView.setId(View.generateViewId());
            characterTouchLayout.addView(imageView);
            ConstraintSet set = new ConstraintSet();
            set.clone(characterTouchLayout);
            set.connect(imageView.getId(),ConstraintSet.START,characterTouchLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1)+(CHARACTER_NORMAL_SIZE-170)/2);
            set.connect(imageView.getId(),ConstraintSet.BOTTOM,characterTouchLayout.getId(),ConstraintSet.BOTTOM,130*(2-x)+CHARACTER_NORMAL_SIZE*2/9);
            set.applyTo(characterTouchLayout);
//            imageView.setBackgroundResource(R.color.colorPurple);
            rightTouchListLayout[x][y] = imageView;
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void magnifyCharacter(boolean isRight){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(isRight){
                    if(leftCharList[i][j] != null){
                        if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                            hideSprite(i,j,false);
                            leftStateBarList[i][j].setVisibility(GONE);
                        }
                    }
                    if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                        rightStateBarList[i][j].setVisibility(VISIBLE);
                        rightCharList[i][j].setVisibility(View.INVISIBLE);
                        setCharacterSize(i,j,CHARACTER_MAGNIFIED_SIZE,true);
                        changeCharacterPosition(i,j, true,true);
                    }
                }else{
                    if(rightCharList[i][j] != null){
                        hideSprite(i,j,true);
                        rightStateBarList[i][j].setVisibility(GONE);
                    }
                    if(leftCharList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                        if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                            leftStateBarList[i][j].setVisibility(VISIBLE);
                            leftCharList[i][j].setVisibility(View.INVISIBLE);
                            setCharacterSize(i,j,CHARACTER_MAGNIFIED_SIZE,false);
                            changeCharacterPosition(i,j,false,true);
                        }
                    }
                }

            }
        }
        //移动背景
        ConstraintSet sampleSet = new ConstraintSet();
        sampleSet.clone((ConstraintLayout)(backgroundLeft.getParent()));
        sampleSet.connect(backgroundLeft.getId(),ConstraintSet.END,((ConstraintLayout)(backgroundLeft.getParent())).getId(),ConstraintSet.END,isRight? StartActivity.SCREEN_WIDTH/4:StartActivity.SCREEN_WIDTH*3/4);
        sampleSet.applyTo((ConstraintLayout)(backgroundLeft.getParent()));
    }

    public void recoverCharacterSize(boolean isRight){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(isRight){
                    if(leftCharList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                        cancelHideSprite(i,j,false);
                    }
                    if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                        rightStateBarList[i][j].setVisibility(VISIBLE);
                        //rightCharList[i][j].setVisibility(View.INVISIBLE);
                        setCharacterSize(i,j,CHARACTER_NORMAL_SIZE,true);
                        changeCharacterPosition(i,j, true,false);
                    }
                }else{
                    if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                        cancelHideSprite(i,j,true);
                    }
                    if(leftCharList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                        if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                            leftStateBarList[i][j].setVisibility(VISIBLE);
                            //leftCharList[i][j].setVisibility(View.INVISIBLE);
                            setCharacterSize(i,j,CHARACTER_NORMAL_SIZE,false);
                            changeCharacterPosition(i,j,false,false);
                        }
                    }
                }

            }
        }
        //恢复背景
        ConstraintSet sampleSet = new ConstraintSet();
        sampleSet.clone((ConstraintLayout)(backgroundLeft.getParent()));
        sampleSet.connect(backgroundLeft.getId(),ConstraintSet.END,((ConstraintLayout)(backgroundLeft.getParent())).getId(),ConstraintSet.END,0);
        sampleSet.applyTo((ConstraintLayout)(backgroundLeft.getParent()));
    }

    public void setCharacterSize(int x, int y, int size, boolean isRight){
        if(isRight){
            ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) rightCharList[x][y].getLayoutParams();
            p.width = size;
            p.height = size;
            rightCharList[x][y].setLayoutParams(p);
        }else{
            ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) leftCharList[x][y].getLayoutParams();
            p.width = size;
            p.height = size;
            leftCharList[x][y].setLayoutParams(p);
        }

    }

    public void changeCharacter(int x, int y){
        rightCharList[x][y].resetCharacter();
        rightCharList[x][y].webView.loadUrl("javascript:changeCharacter()");
    }



    public void changeCharacterPosition(int x, int y, boolean isRight, boolean isMagnify){
        final SpriteViewer sp = isRight? rightCharList[x][y]:leftCharList[x][y];
        sp.setZ(x);
        ConstraintSet sampleSet = new ConstraintSet();
        sampleSet.clone(characterLayout);
        ConstraintSet set = new ConstraintSet();
        if(isMagnify){
            if(isRight){
                sampleSet.clear(sp.getId(),ConstraintSet.END);
                sampleSet.connect(sp.getId(),ConstraintSet.START,characterLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/2-CHARACTER_MAGNIFIED_SIZE/2+70*(x-1)+250*(y-1));
                set.clone(characterTouchLayout);
                set.clear(rightStateBarList[x][y].getId(),ConstraintSet.END);
                set.connect(rightStateBarList[x][y].getId(),ConstraintSet.START,characterTouchLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/2-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1)+CHARACTER_NORMAL_SIZE*3/7);
                set.applyTo(characterTouchLayout);
            }else{
                sampleSet.clear(sp.getId(),ConstraintSet.START);
                sampleSet.connect(sp.getId(),ConstraintSet.END,characterLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/2-CHARACTER_MAGNIFIED_SIZE/2+70*(x-1)-250*(y-1));
                set.clone(characterTouchLayout);
                set.clear(leftStateBarList[x][y].getId(),ConstraintSet.START);
                set.connect(leftStateBarList[x][y].getId(),ConstraintSet.END,characterTouchLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/2-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1)+CHARACTER_NORMAL_SIZE*3/7);
                set.applyTo(characterTouchLayout);
            }
            sampleSet.connect(sp.getId(),ConstraintSet.BOTTOM,characterLayout.getId(),ConstraintSet.BOTTOM,130*(2-x));
        }else{
            if(isRight){
                sampleSet.clear(sp.getId(),ConstraintSet.END);
                sampleSet.connect(sp.getId(),ConstraintSet.START,characterLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1));
                set.clone(characterTouchLayout);
                set.clear(rightStateBarList[x][y].getId(),ConstraintSet.END);
                set.connect(rightStateBarList[x][y].getId(),ConstraintSet.START,characterTouchLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1)+CHARACTER_NORMAL_SIZE*3/7);
                set.applyTo(characterTouchLayout);
            }else{
                sampleSet.clear(sp.getId(),ConstraintSet.START);
                sampleSet.connect(sp.getId(),ConstraintSet.END,characterLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1));
                set.clone(characterTouchLayout);
                set.clear(leftStateBarList[x][y].getId(),ConstraintSet.START);
                set.connect(leftStateBarList[x][y].getId(),ConstraintSet.END,characterTouchLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1)+CHARACTER_NORMAL_SIZE*3/7);
                set.applyTo(characterTouchLayout);
            }
            sampleSet.connect(sp.getId(),ConstraintSet.BOTTOM,characterLayout.getId(),ConstraintSet.BOTTOM,130*(2-x));
        }
        sampleSet.applyTo(characterLayout);

        sp.postDelayed(new Runnable() {
            @Override
            public void run() {
                sp.setVisibility(VISIBLE);
            }
        }, 200);
    }

    public void hideSprite(int x, int y, boolean isRight){
        final SpriteViewer sp = isRight? rightCharList[x][y]:leftCharList[x][y];
        ConstraintSet sampleSet = new ConstraintSet();
        sampleSet.clone(characterLayout);
        sampleSet.connect(sp.getId(),ConstraintSet.TOP,characterLayout.getId(),ConstraintSet.BOTTOM,0);
        sampleSet.clear(sp.getId(),ConstraintSet.BOTTOM);
        sampleSet.applyTo(characterLayout);

    }

    public void cancelHideSprite(int x, int y, boolean isRight){
        SpriteViewer sp = isRight? rightCharList[x][y]:leftCharList[x][y];
        ConstraintSet sampleSet = new ConstraintSet();
        sampleSet.clone(characterLayout);
        sampleSet.connect(sp.getId(),ConstraintSet.BOTTOM,characterLayout.getId(),ConstraintSet.BOTTOM,130*(2-x));
        sampleSet.clear(sp.getId(),ConstraintSet.TOP);
        sampleSet.applyTo(characterLayout);
    }

    public void changeSprite(int x, int y, boolean isRight){
        if(isRight){
            rightCharList[x][y].resetCharacter();
            rightCharList[x][y].webView.loadUrl("javascript:changeSprite()");
        }else{
            leftCharList[x][y].resetCharacter();
            leftCharList[x][y].webView.loadUrl("javascript:changeSprite()");
        }
    }

    public void setSmallPlate(int smallPlateId, int largePlateId, Character connectTo){
        if(smallPlateId > 0){
            small_plate_right_arrow[smallPlateId-1].setVisibility(VISIBLE);
        }
        if(largePlateId <= 4){
            if(smallPlateList[smallPlateId] != -1 && smallPlateList[smallPlateId] != largePlateId){
                charPlateViewList[smallPlateList[smallPlateId]].cancelShader();
            }
        }

        smallPlateList[smallPlateId] = largePlateId;
        smallPlateConnectToList[smallPlateId] = connectTo;
        Character c;
        if(largePlateId >= 5){
            c = StartActivity.characters[largePlateId-5];
        }else{
            c = plateList[largePlateId].c;
        }
        small_plate_attribute[smallPlateId].setBackgroundResource(getImageByString(c.element));
        if(largePlateId >= 5 && c.realMP >= DOPPEL_NEED_MP){
            small_plate_character[smallPlateId].setBackgroundResource(getImageByString(c.doppelImageName));
        }else{
            small_plate_character[smallPlateId].setBackgroundResource(getImageByString(c.charIconImage+"d"));
        }
        if(connectTo != null){
            small_plate_small_character[smallPlateId].setVisibility(VISIBLE);
            small_plate_small_character[smallPlateId].setBackgroundResource(getImageByString(connectTo.charIconImage+"d"));
        }else{
            small_plate_small_character[smallPlateId].setVisibility(GONE);
        }
        switch((largePlateId >= 5)? (c.realMP >= DOPPEL_NEED_MP? DOPPEL:MAGIA):plateList[largePlateId].plate){
            case ACCELE:
                small_plate_arrow[smallPlateId].setVisibility(GONE);
                small_plate_background[smallPlateId].setBackgroundResource(R.drawable.accele_plate);
                if(connectTo != null){
                    small_plate_small_background[smallPlateId].setVisibility(VISIBLE);
                    small_plate_small_background[smallPlateId].setBackgroundResource(R.drawable.accele_plate);
                }else{
                    small_plate_small_background[smallPlateId].setVisibility(GONE);
                }
                small_plate_text[smallPlateId].setImageResource(R.drawable.accele);
                break;
            case BLAST_HORIZONTAL:case BLAST_VERTICAL:
                small_plate_arrow[smallPlateId].setVisibility(VISIBLE);
                small_plate_background[smallPlateId].setBackgroundResource(R.drawable.blast_plate);
                if(connectTo != null){
                    small_plate_small_background[smallPlateId].setVisibility(VISIBLE);
                    small_plate_small_background[smallPlateId].setBackgroundResource(R.drawable.blast_plate);
                }else{
                    small_plate_small_background[smallPlateId].setVisibility(GONE);
                }
                small_plate_text[smallPlateId].setImageResource(R.drawable.blast);
                small_plate_arrow[smallPlateId].setImageResource(plateList[largePlateId].plate == BLAST_VERTICAL? R.drawable.blast_vertical:R.drawable.blast_horizontal);
                break;
            case CHARGE:
                small_plate_arrow[smallPlateId].setVisibility(GONE);
                small_plate_background[smallPlateId].setBackgroundResource(R.drawable.charge_plate);
                if(connectTo != null){
                    small_plate_small_background[smallPlateId].setVisibility(VISIBLE);
                    small_plate_small_background[smallPlateId].setBackgroundResource(R.drawable.charge_plate);
                }else{
                    small_plate_small_background[smallPlateId].setVisibility(GONE);
                }
                small_plate_text[smallPlateId].setImageResource(R.drawable.charge);
                break;
            case MAGIA:
                small_plate_arrow[smallPlateId].setVisibility(VISIBLE);
                small_plate_arrow[smallPlateId].setImageResource(getImageByString(c.magiaSkillIconName));
                small_plate_background[smallPlateId].setBackgroundResource(R.drawable.magia_plate_background);
                small_plate_small_background[smallPlateId].setVisibility(GONE);
                small_plate_text[smallPlateId].setImageResource(R.drawable.magia);
                break;
            case DOPPEL:
                small_plate_arrow[smallPlateId].setVisibility(VISIBLE);
                small_plate_arrow[smallPlateId].setImageResource(getImageByString(c.magiaSkillIconName));
                small_plate_background[smallPlateId].setBackgroundResource(R.drawable.magia_plate_background);
                small_plate_small_background[smallPlateId].setVisibility(GONE);
                small_plate_text[smallPlateId].setImageResource(R.drawable.doppel);
                break;
            default:
        }
        if(barState == MAGIA_BAR_SHOW){
            if(largePlateId > 4){
                charPlateViewList[largePlateId - 5].setShader();
            }
        }else{
            if(largePlateId <= 4){
                charPlateViewList[largePlateId].setShader();
            }
        }

        small_plate[smallPlateId].setVisibility(VISIBLE);
    }

    public void cancelSmallPlate(int largePlateId){
        int smallPlateId = -1;
        for(int i = 0; i < 3; i++){
            if(smallPlateList[i] == largePlateId){
                smallPlateId = i;
                break;
            }
        }
        for(int i = smallPlateId; i < 3; i++){
            if(i < 2){
                smallPlateList[i] = smallPlateList[i+1];
                smallPlateXList[i] = smallPlateXList[i+1];
                smallPlateYList[i] = smallPlateYList[i+1];
                smallPlateConnectToList[i] = smallPlateConnectToList[i+1];
            }else{
                smallPlateList[i] = -1;
                smallPlateXList[i] = -1;
                smallPlateYList[i] = -1;
                smallPlateConnectToList[i] = null;
            }
        }
        for(int i = 0; i < 3; i++){
            if(smallPlateList[i] != -1){
                setSmallPlate(i,smallPlateList[i],smallPlateConnectToList[i]);
            }else{
                small_plate[i].setVisibility(View.INVISIBLE);
            }
        }
        for(int i = 0; i < 2; i++){
            if(smallPlateList[i+1] == -1){
                small_plate_right_arrow[i].setVisibility(View.INVISIBLE);
            }else{
                small_plate_right_arrow[i].setVisibility(View.VISIBLE);
            }
        }
        charPlateViewList[largePlateId % 5].cancelShader();
        smallPlateNumber--;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                    rightCharList[i][j].spriteName = getRecoverOriginSpriteName(i,j,true, "wait", false);
                    changeSprite(i,j,true);
                }
            }
        }
    }

    public void setCharacterAttackPosition(int rx, int ry, int lx, int ly, boolean isRightAttack){
        if(isRightAttack){
            SpriteViewer sp = rightCharList[rx][ry];
            sp.setZ(lx);
            ConstraintSet sampleSet = new ConstraintSet();
            sampleSet.clone(characterLayout);
            sampleSet.clear(sp.getId(),ConstraintSet.START);
            sampleSet.clear(sp.getId(),ConstraintSet.TOP);
            sampleSet.connect(sp.getId(),ConstraintSet.END,characterLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/2-CHARACTER_MAGNIFIED_SIZE*2/3+70*(lx-1)-250*(ly-1));
            sampleSet.connect(sp.getId(),ConstraintSet.BOTTOM,characterLayout.getId(),ConstraintSet.BOTTOM,130*(2-lx));
            sampleSet.applyTo(characterLayout);
        }else{
            SpriteViewer sp = leftCharList[lx][ly];
            sp.setZ(rx);
            ConstraintSet sampleSet = new ConstraintSet();
            sampleSet.clone(characterLayout);
            sampleSet.clear(sp.getId(),ConstraintSet.END);
            sampleSet.clear(sp.getId(),ConstraintSet.TOP);
            sampleSet.connect(sp.getId(),ConstraintSet.START,characterLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/2-CHARACTER_MAGNIFIED_SIZE*2/3+70*(rx-1)+250*(ry-1));
            sampleSet.connect(sp.getId(),ConstraintSet.BOTTOM,characterLayout.getId(),ConstraintSet.BOTTOM,130*(2-rx));
            sampleSet.applyTo(characterLayout);
        }
    }

    public void setCharacterSkillTime(int c, int skill, int time){
        if(time == 0){
            skill__time[c][skill][0].setVisibility(GONE);
            skill__time[c][skill][1].setVisibility(GONE);
            skill_[c][skill].setColorFilter(null); // 如果想恢复彩色显示，设置为null即可
        }else{
            skill__time[c][skill][0].setVisibility(VISIBLE);
            skill__time[c][skill][0].setBackgroundResource(getImageByString("red_"+(time % 10)));
            if(time >= 10){
                skill__time[c][skill][1].setVisibility(VISIBLE);
                skill__time[c][skill][1].setBackgroundResource(getImageByString("red_"+((time - (time % 10) )/10)));
            }else{
                skill__time[c][skill][1].setVisibility(GONE);
            }
            skill_[c][skill].setColorFilter(grayColorFilter); // 如果想恢复彩色显示，设置为null即可
        }
    }

    public void setAllRightCharacterShader(float alpha){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                    rightCharList[i][j].webView.setAlpha(alpha);
                }
            }
        }
    }

    public String getRecoverOriginSpriteName(int x, int y, boolean isRight, String defaultSprite, boolean isInBattle){
        //用于恢复sprite的动作至本来状态
        if(isRight){
            for(int i = isInBattle? (smallPlateNumber+1):0; i < 3; i++){
                if(smallPlateList[i] != -1 && smallPlateConnectToList[i] != null){
                    Character c;
                    if(smallPlateList[i] <= 4){
                        c = plateList[smallPlateList[i]].c;
                    }else{
                        c = StartActivity.characters[i-5];
                    }
                    if(c.formationX == x && c.formationY == y){
                        return "stance_con";
                    }
                }
            }
        }
        return defaultSprite;
    }

    public void prepareRightAttack(){
        platesLayout.setVisibility(View.INVISIBLE);
        touchImageListLayout[chooseMonsterX][chooseMonsterY].setVisibility(GONE);
        smallPlateNumber = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(leftCharList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                    if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                        leftCharList[i][j].spriteName = "stance";
                        changeSprite(i, j, false);
                    }
                }
                if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                    rightCharList[i][j].spriteName = getRecoverOriginSpriteName(i,j,true,"stance", false);
                    changeSprite(i, j, true);
                }
            }
        }
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                startRightAttack();
            }
        }, 1500);

        //检查是否是charge\charge三连
        boolean isChargeCombo = true;
        boolean isAcceleCombo = true;
        boolean isBlastCombo = true;
        boolean isPuella = true;
        Character firstAttacker = null;
        for(int i = 0; i < 3; i++){
            if(smallPlateList[i] <= 4){
                if(plateList[smallPlateList[i]].plate != CHARGE){
                    isChargeCombo = false;
                }
                if(plateList[smallPlateList[i]].plate != ACCELE){
                    isAcceleCombo = false;
                }
                if(plateList[smallPlateList[i]].plate != BLAST_VERTICAL && plateList[smallPlateList[i]].plate != BLAST_HORIZONTAL){
                    isBlastCombo = false;
                }
                if(smallPlateConnectToList[i] == null){
                    //没有连携, 读取盘本来的人
                    if(firstAttacker == null){
                        firstAttacker = plateList[smallPlateList[i]].c;
                    }else{
                        if(firstAttacker != plateList[smallPlateList[i]].c){
                            isPuella = false;
                        }
                    }
                }else{
                    //有连携, 读取连携的人
                    if(firstAttacker == null){
                        firstAttacker = smallPlateConnectToList[i];
                    }else{
                        if(firstAttacker != smallPlateConnectToList[i]){
                            isPuella = false;
                        }
                    }
                }

            }else{
                isChargeCombo = false;
                isAcceleCombo = false;
                isBlastCombo = false;
                isPuella = false;
                break;
            }
        }
        if(isChargeCombo){
            chargeNumber += 2;
            if(chargeNumber > 20){
                chargeNumber = 20;
            }
            updateChargeView();
            showCombo(CHARGECOMBO);
        }else if(isAcceleCombo){
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                        rightCharList[i][j].c.realMP += 200;
                        setMpOnCharacter(rightCharList[i][j].c,rightCharList[i][j].c.realMP,true);
                    }
                }
            }
            showCombo(ACCELE);
        }else if(isBlastCombo){
            showCombo(BLASTCOMBO);
        }else if(isPuella){
            showCombo(PUELLACOMBO);
        }
    }

    public void showCombo(int comboType){
        View comboView = null;
        switch(comboType){
            case ACCELECOMBO:
                comboView = acceleCombo;
                comboText.setText("magia蓄能条增加");
                break;
            case CHARGECOMBO:
                comboView = chargeCombo;
                comboText.setText("charge数+2");
                break;
            case BLASTCOMBO:
                comboView = blastCombo;
                comboText.setText("伤害大幅提升");
                break;
            case PUELLACOMBO:
                comboView = puellaCombo;
                comboText.setText("伤害大幅提升");
                break;
        }
        final View tempComboView = comboView;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.combo_move);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //connectArrowView[tempI].startAnimation(animation);
                tempComboView.setVisibility(INVISIBLE);
                comboFrame.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        comboView.setVisibility(VISIBLE);
        comboFrame.setVisibility(VISIBLE);
        comboView.startAnimation(animation);
        comboFrame.startAnimation(animation);
    }

    public void startRightAttack(){
        //判断是否是Charge盘
        if(smallPlateList[smallPlateNumber] <= 4){
            if(plateList[smallPlateList[smallPlateNumber]].plate == CHARGE){
                chargeNumber++;
                if(chargeNumber > 20)chargeNumber = 20;
            }
            updateChargeView();
        }

        if(smallPlateConnectToList[smallPlateNumber] == null){
            if(smallPlateNumber == 0) {
                magnifyCharacter(true);
            }
            if(smallPlateList[smallPlateNumber] <= 4){
                //说明是正常攻击
                int plate = plateList[smallPlateList[smallPlateNumber]].plate;
                Character c = plateList[smallPlateList[smallPlateNumber]].c;
                final int fx = c.formationX;
                final int fy = c.formationY;
                if(plate == CharacterPlateView.ACCELE || plate == CharacterPlateView.CHARGE){
                    rightCharList[fx][fy].spriteName = "attack_in";
                }else if(plate == CharacterPlateView.BLAST_HORIZONTAL){
                    rightCharList[fx][fy].spriteName = "attackh_in";
                }else if(plate == CharacterPlateView.BLAST_VERTICAL){
                    rightCharList[fx][fy].spriteName = "attackv_in";
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rightStateBarList[fx][fy].setVisibility(GONE);
                    }},500);
                rightCharList[c.formationX][c.formationY].webView.loadUrl("javascript:setAnimationIndex(" + 1 + ")");
                changeSprite(c.formationX, c.formationY, true);
            }else{
                //说明是magia或者doppel
                Message m = new Message();
                m.what = 1;
                handler.sendMessage(m);
            }
        }else{
            // 说明有连携
            if(smallPlateNumber == 0) {
                magnifyCharacter(true);
            }
            int plate = plateList[smallPlateList[smallPlateNumber]].plate;
            Character c = plateList[smallPlateList[smallPlateNumber]].c;
            final int fx = c.formationX;
            final int fy = c.formationY;
            rightCharList[fx][fy].spriteName = "activate";
            changeSprite(fx,fy, true);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int connectToX = smallPlateConnectToList[smallPlateNumber].formationX;
                    final int connectToY = smallPlateConnectToList[smallPlateNumber].formationY;
                    rightCharList[connectToX][connectToY].spriteName = "reaction";
                    changeSprite(connectToX,connectToY, true);
                    final ArrayList<SkillEffect> tempArrayList = (rightCharList[fx][fy].c.star == 4)?rightCharList[fx][fy].c.connectOriginEffectList:rightCharList[fx][fy].c.connectAfterEffectList;

                    for(int i = 0; i < tempArrayList.size(); i++){
                        final int tempI = i;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //播放受众对应动画
                                int abnormalStateResistance = calculateAbnormalStateResistance(rightEffectList[connectToX][connectToY]);
                                Effect e = convertSkillEffectToEffect(tempArrayList.get(tempI), abnormalStateResistance);
                                if(e != null){
                                    handleEffectiveness(e,connectToX,connectToY,true,true);
                                }
                                //如果是最后一个，则计划结束技能释放
                                if(tempI == (tempArrayList.size() - 1)){
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            clickable = true;
                                            skill_launch_layout.setVisibility(GONE);
                                            // 正式开始进攻
                                            rightCharList[plateList[smallPlateList[smallPlateNumber]].c.formationX][plateList[smallPlateList[smallPlateNumber]].c.formationY].spriteName = getRecoverOriginSpriteName(plateList[smallPlateList[smallPlateNumber]].c.formationX,plateList[smallPlateList[smallPlateNumber]].c.formationY,true,"stance",true);
                                            changeSprite(plateList[smallPlateList[smallPlateNumber]].c.formationX,plateList[smallPlateList[smallPlateNumber]].c.formationY,true);
                                            int plate = plateList[smallPlateList[smallPlateNumber]].plate;
                                            Character c = smallPlateConnectToList[smallPlateNumber];
                                            final int fx = c.formationX;
                                            final int fy = c.formationY;
                                            if(plate == CharacterPlateView.ACCELE || plate == CharacterPlateView.CHARGE){
                                                rightCharList[fx][fy].spriteName = "attack_in";
                                            }else if(plate == CharacterPlateView.BLAST_HORIZONTAL){
                                                rightCharList[fx][fy].spriteName = "attackh_in";
                                            }else if(plate == CharacterPlateView.BLAST_VERTICAL){
                                                rightCharList[fx][fy].spriteName = "attackv_in";
                                            }
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    rightStateBarList[fx][fy].setVisibility(GONE);
                                                }},500);
                                            rightCharList[c.formationX][c.formationY].webView.loadUrl("javascript:setAnimationIndex(" + 1 + ")");
                                            changeSprite(c.formationX, c.formationY, true);
                                            //
                                        }
                                    }, 1500);
                                }
                            }
                        }, DELTA_BETWEEN_EFFECT_SHOW*(i+1));
                    }
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {

//                        }}, 1500);
                }},1000);
        }
    }

    public void sendEffect(Effect e, int x, int y, boolean isRight, boolean isMagnified){
        SkillEffectView ef;
        switch(e.name){
            case"攻击力UP": case"防御力UP":
                ef = new SkillEffectView(BattleActivity.this, skillEffectLayout, " "+e.name.substring(0,e.name.length()-2)+" "+e.name.substring(e.name.length()-2)+" ", R.anim.skill_positive_effect, R.color.white, R.color.skillTextPink, false);
                break;
            case"造成伤害UP":case"伤害削减":case "Magia伤害削减":case "HP最大时防御力UP":
            case "异常状态耐性UP": case "Blast攻击时MP获得": case "AcceleMPUP": case "HP最大时攻击力UP":
            case "Charge盘伤害UP": case "Charge后伤害UP":case "敌方状态异常时伤害UP":
            case "战斗不能时获得防御力UP": case "战斗不能时获得攻击力UP":case "Magia伤害UP":
            case "对魔女伤害上升":case "火属性攻击力UP": case "Blast伤害UP": case "Blast伤害削减":
            case "被弱点属性攻击时MPUP": case "MP获得量UP": case "濒死时防御力UP":case "濒死时攻击力UP": case "Doppel伤害UP":case "状态异常1回无效": case "Magia封印无效":
            case "回避无效": case "反击无效": case "毒无效":case "挑拨无效": case "诅咒无效":
            case "雾无效":  case "烧伤无效":case "黑暗无效":case "技能封印无效":
            case "魅惑无效": case "DEBUFF无效": case "无视防御力": case "伤害削减无效":
            case "眩晕无效":case "技能冷却加速": case "拘束无效": case "幻惑无效":case "挑拨":case "追击":case "反击":case "回避":
            case "暴击": case "保护同伴": case "忍耐":
            case "水属性攻击力UP":case "木属性攻击力UP":
            case "光属性攻击力UP":case "暗属性攻击力UP":
            case "攻击时给予状态毒":case "攻击时给予状态烧伤":case "攻击时给予状态诅咒":
            case "攻击时给予状态魅惑":case "攻击时给予状态眩晕":case "攻击时给予状态拘束":
            case "攻击时给予状态雾":case "攻击时给予状态黑暗":case "攻击时给予状态幻惑":
            case "攻击时给予状态技能封印":case "攻击时给予状态Magia封印":
            case "攻击时给予状态HP回复禁止":case "攻击时给予状态MP回复禁止":
                ef = new SkillEffectView(BattleActivity.this, skillEffectLayout, " "+e.name+" ", R.anim.skill_positive_effect, R.color.white, R.color.skillTextPink, false);
                break;
            case"攻击力DOWN": case"防御力DOWN":
                ef = new SkillEffectView(BattleActivity.this, skillEffectLayout, " "+e.name.substring(0,e.name.length()-4)+" "+e.name.substring(e.name.length()-4)+" ", R.anim.skill_negative_effect, R.color.white, R.color.skillTextBlue, false);
                break;
            case"造成伤害DOWN": case "AcceleMPDOWN": default:
            case "异常状态耐性DOWN":case "MP获得量DOWN":case "Blast伤害DOWN":case "Magia伤害DOWN":
            case "MP伤害":case "幻惑":case "诅咒":case "毒": case "技能封印":case "魅惑":case "给予状态眩晕":
            case "拘束":case "雾": case "黑暗":case "烧伤": case "Magia封印":case "BUFF解除":case "MP回复禁止":case "HP回复禁止":
                ef = new SkillEffectView(BattleActivity.this, skillEffectLayout, " "+e.name+" ", R.anim.skill_negative_effect, R.color.white, R.color.skillTextBlue, false);
                break;
            case "MP回复":case "HP回复":  case "DEBUFF解除":case "异常状态解除":case "HP自动回复":
                ef = new SkillEffectView(BattleActivity.this, skillEffectLayout, " "+e.name+" ", R.anim.skill_positive_effect, R.color.white, R.color.skillTextGreen, false);
                break;
        }
        ef.setId(generateViewId());
        skillEffectLayout.addView(ef);
        ConstraintSet sampleSet = new ConstraintSet();
        sampleSet.clone(skillEffectLayout);
        if(isMagnified){
            if(isRight){
                sampleSet.connect(ef.getId(),ConstraintSet.START,skillEffectLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/2-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1)+(CHARACTER_NORMAL_SIZE-500)/2);
            }else{
                sampleSet.connect(ef.getId(),ConstraintSet.END,skillEffectLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/2-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1)+(CHARACTER_NORMAL_SIZE-500)/2);
            }
        }else{
            if(isRight){
                sampleSet.connect(ef.getId(),ConstraintSet.START,skillEffectLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1)+(CHARACTER_NORMAL_SIZE-500)/2);
            }else{
                sampleSet.connect(ef.getId(),ConstraintSet.END,skillEffectLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1)+(CHARACTER_NORMAL_SIZE-500)/2);
            }
        }

        sampleSet.connect(ef.getId(),ConstraintSet.BOTTOM,skillEffectLayout.getId(),ConstraintSet.BOTTOM,130*(2-x)+CHARACTER_NORMAL_SIZE*1/9);
        sampleSet.applyTo(skillEffectLayout);

        //SkillEffectView ef2 = new SkillEffectView(BattleActivity.this, skillEffectLayout, " 攻击力UP! ", R.anim.skill_positive_effect, R.color.white, R.color.skillTextPink);
    }

    public void sendDamageNumber(int number, int x, int y, int textColor, boolean isRight, boolean isMagnified){
        SkillEffectView ef;
        if(textColor == TEXT_RED){
            ef = new SkillEffectView(BattleActivity.this, skillEffectLayout, " "+number+" ", R.anim.damage_number, R.color.skillTextPink, R.color.white, true);
        }else if(textColor == TEXT_BLUE){
            ef = new SkillEffectView(BattleActivity.this, skillEffectLayout, " "+number+" ", R.anim.damage_number, R.color.lightBlue, R.color.white, true);
        }else{
            ef = new SkillEffectView(BattleActivity.this, skillEffectLayout, " "+number+" ", R.anim.damage_number, R.color.skillTextGreen, R.color.white, true);
        }
        ef.setId(generateViewId());
        skillEffectLayout.addView(ef);
        ConstraintSet sampleSet = new ConstraintSet();
        sampleSet.clone(skillEffectLayout);
        if(isMagnified){
            if(isRight){
                sampleSet.connect(ef.getId(),ConstraintSet.START,skillEffectLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/2-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1)+(CHARACTER_NORMAL_SIZE-500)/2);
            }else{
                sampleSet.connect(ef.getId(),ConstraintSet.END,skillEffectLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/2-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1)+(CHARACTER_NORMAL_SIZE-500)/2);
            }
        }else{
            if(isRight){
                sampleSet.connect(ef.getId(),ConstraintSet.START,skillEffectLayout.getId(),ConstraintSet.START,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)+250*(y-1)+(CHARACTER_NORMAL_SIZE-500)/2);
            }else{
                sampleSet.connect(ef.getId(),ConstraintSet.END,skillEffectLayout.getId(),ConstraintSet.END,StartActivity.SCREEN_WIDTH/4*3-CHARACTER_NORMAL_SIZE/2+70*(x-1)-250*(y-1)+(CHARACTER_NORMAL_SIZE-500)/2);
            }
        }

        sampleSet.connect(ef.getId(),ConstraintSet.BOTTOM,skillEffectLayout.getId(),ConstraintSet.BOTTOM,130*(2-x)+CHARACTER_NORMAL_SIZE*2/9);
        sampleSet.applyTo(skillEffectLayout);

        //SkillEffectView ef2 = new SkillEffectView(BattleActivity.this, skillEffectLayout, " 攻击力UP! ", R.anim.skill_positive_effect, R.color.white, R.color.skillTextPink);
    }

    public Effect convertSkillEffectToEffect(SkillEffect se, int abnormalStateResistance){
        Effect e = new Effect();
        switch(se.name){
            case "攻击力UP": case "攻击力DOWN":
            case "防御力DOWN": case "防御力UP":
            case "造成伤害UP": case "造成伤害DOWN":
            case "伤害削减": case "Magia伤害削减":
            case "HP最大时防御力UP": case "AcceleMPDOWN":
            case "异常状态耐性UP": case "异常状态耐性DOWN":
            case "Blast攻击时MP获得": case "AcceleMPUP":
            case "HP最大时攻击力UP": case "MP获得量DOWN":
            case "Charge盘伤害UP": case "Charge后伤害UP":
            case "Blast伤害DOWN":case "敌方状态异常时伤害UP":
            case "战斗不能时获得防御力UP": case "战斗不能时获得攻击力UP":
            case "Magia伤害UP": case "Magia伤害DOWN":
            case "对魔女伤害上升":case "火属性攻击力UP":
            case "Blast伤害UP": case "Blast伤害削减":
            case "被弱点属性攻击时MPUP": case "MP获得量UP":
            case "濒死时防御力UP":case "濒死时攻击力UP":
            case "HP自动回复": case "Doppel伤害UP":
            case "水属性攻击力UP":case "木属性攻击力UP":
            case "光属性攻击力UP":case "暗属性攻击力UP":
            case "攻击时给予状态雾": case "攻击时给予状态黑暗": case "攻击时给予状态幻惑":
            case "攻击时给予状态毒":case "攻击时给予状态烧伤": case "攻击时给予状态诅咒":
            case "挑拨":case "追击":case "反击":case "回避":
            case "暴击":case "保护同伴":
                e.name = se.name;
                e.value = se.value;
                e.time = (se.time == 0)? 999:se.time;
                e.probability = se.probability;
                if(se.valueTime > 0){
                    e.valueTime = se.valueTime;
                }
                break;
            case "状态异常1回无效": case "Magia封印无效":
            case "回避无效": case "反击无效":
            case "毒无效":case "挑拨无效": case "诅咒无效":
            case "雾无效": case "忍耐":
            case "烧伤无效":case "黑暗无效":case "技能封印无效":
            case "魅惑无效": case "DEBUFF无效":
            case "无视防御力": case "伤害削减无效":
            case "眩晕无效":case "技能冷却加速":
            case "拘束无效": case "幻惑无效":
            case "攻击时给予状态魅惑":case "攻击时给予状态眩晕": case "攻击时给予状态拘束":
            case "攻击时给予状态Magia封印":case "攻击时给予状态技能封印":
            case "攻击时给予状态HP回复禁止":case "攻击时给予状态MP回复禁止":
                e.name = se.name;
                e.time = (se.time == 0)? 999:se.time;
                e.probability = se.probability;
                if(se.valueTime > 0){
                    e.valueTime = se.valueTime;
                }
                break;
            case "伤害上升":
                e.name = "造成伤害UP";
                e.value = se.value;
                e.time = (se.time == 0)? 999:se.time;
                break;
            case "Charge伤害UP":
                e.name = "Charge盘伤害UP";
                e.value = se.value;
                e.time = (se.time == 0)? 999:se.time;
                break;
            case "Blast伤害削减状态":
                e.name = "Blast伤害削减";
                e.value = se.value;
                e.time = (se.time == 0)? 999:se.time;
                break;
            case "Blast攻击获得MP":
                e.name = "Blast攻击时MP获得";
                e.value = se.value;
                e.time = (se.time == 0)? 999:se.time;
                break;
            case "Magia伤害削减状态":
                e.name = "Magia伤害削减";
                e.value = se.value;
                e.time = (se.time == 0)? 999:se.time;
                break;
            case "重抽为Accele的Disc":
            case "重抽为Blast的Disc":case "重抽Disc":
            case "重抽为同属性的Disc":case "重抽为Charge的Disc":
            case "BUFF解除":  case "DEBUFF解除": case "异常状态解除":
            case "重抽为自己的Disc":
                if(colorToss(se.probability)){
                    e.name = se.name;
                }else{
                    //没中异常
                    return null;
                }
                break;
            case "MP伤害":case "MP回复":case "以MP积累状态开始战斗":case "HP回复":
                if(colorToss(se.probability)){
                    e.name = se.name;
                    e.value = se.value;
                }else{
                    //没中异常
                    return null;
                }
            break;
            case "给予状态黑暗":case "给予状态雾":case "给予状态幻惑":
                if(colorToss(se.probability - abnormalStateResistance)){
                    e.name = se.name.substring(4);
                    e.probability = se.value;
                    e.time = se.time;
                }else{
                    //没中异常
                    return null;
                }
                break;
            case "给予状态毒": case "给予状态烧伤":case "给予状态诅咒":
                if(colorToss(se.probability - abnormalStateResistance)){
                    e.name = se.name.substring(4);
                    e.value = se.value;
                    e.time = se.time;
                }else{
                    //没中异常
                    return null;
                }
                break;
            case "给予状态技能封印":case "给予状态魅惑":case "给予状态眩晕":
            case "给予状态拘束":case "给予状态MP回复禁止":case "给予状态HP回复禁止":
                if(colorToss(se.probability - abnormalStateResistance)){
                    e.name = se.name.substring(4);
                    e.time = se.time;
                }else{
                    //没中异常
                    return null;
                }
                break;
            case "Magia封印": case "技能封印":
                if(colorToss(se.probability - abnormalStateResistance)){
                    e.name = se.name;
                    e.value = se.value;
                    e.time = se.time;
                }else{
                    //没中异常
                    return null;
                }
            default:
                return null;
        }
        return e;
    }

    public int getDamage(Character attacker, Character defender, boolean isPlayerAttack){
        int plateType = -1;
        if(isPlayerAttack){
            if(smallPlateList[smallPlateNumber] <= 4){
                plateType = plateList[smallPlateList[smallPlateNumber]].plate;
            }else{
                plateType = StartActivity.characters[smallPlateList[smallPlateNumber]-5].realMP >= DOPPEL_NEED_MP ? DOPPEL:MAGIA;
            }
        }else{
            plateType = CHARGE;
        }

        //白值ATK
        int panelATK = attacker.getRealATK();


        //阵形ATK
        int formationATK = 0;
        if(isPlayerAttack){
            Formation f = StartActivity.formationList.get(TeamChooseActivity.usingFormationId);
            if(f.gridAllEffectList[attacker.formationX][attacker.formationY].size() > 0){
                for(int i = 0; i < f.gridAllEffectList[attacker.formationX][attacker.formationY].size(); i++){
                    SkillEffect se = f.gridAllEffectList[attacker.formationX][attacker.formationY].get(i);
                    if(se.name.equals("攻击力UP")){
                        formationATK += se.value;
                    }else if(se.name.equals("攻击力DOWN")){
                        formationATK -= se.value;
                    }else if(se.name.endsWith("属性攻击力UP")){
                        if(se.name.startsWith("木") && attacker.element.equals("tree")){
                            formationATK += se.value;
                        }else if (se.name.startsWith("火") && attacker.element.equals("fire")){
                            formationATK += se.value;
                        }else if(se.name.startsWith("水") && attacker.element.equals("water")){
                            formationATK += se.value;
                        }else if(se.name.startsWith("光") && attacker.element.equals("light")){
                            formationATK += se.value;
                        }else if(se.name.startsWith("暗") && attacker.element.equals("dark")){
                            formationATK += se.value;
                        }
                    }
                }
            }
        }

        //攻击力Buff
        int atkBuff = 0;
        ArrayList<Effect> efList = isPlayerAttack? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            if(e.name.equals("攻击力UP")){
                atkBuff += e.value;
            }else if(e.name.equals("攻击力DOWN")){
                atkBuff -= e.value;
            }else if(e.name.equals("濒死时攻击力UP") && (1.0f*attacker.realHP/attacker.getRealMaxHP() < 0.2f)){
                atkBuff += e.value;
            }else if(e.name.equals("HP最大时攻击力UP") && (attacker.realHP == attacker.getRealMaxHP())){
                atkBuff += e.value;
            }else if(e.name.endsWith("属性攻击力UP")){
                if(e.name.startsWith("木") && attacker.element.equals("tree")){
                    atkBuff += e.value;
                }else if (e.name.startsWith("火") && attacker.element.equals("fire")){
                    atkBuff += e.value;
                }else if(e.name.startsWith("水") && attacker.element.equals("water")){
                    atkBuff += e.value;
                }else if(e.name.startsWith("光") && attacker.element.equals("light")){
                    atkBuff += e.value;
                }else if(e.name.startsWith("暗") && attacker.element.equals("dark")){
                    atkBuff += e.value;
                }
            }
        }
        if(atkBuff > 100) atkBuff = 100;
        if(atkBuff < -95) atkBuff = -95;

        //白值DEF
        int panelDEF = defender.getRealDEF()/3;

        //阵形DEF
        int formationDEF = 0;
        if(!isPlayerAttack){
            Formation f = StartActivity.formationList.get(TeamChooseActivity.usingFormationId);
            if(f.gridAllEffectList[defender.formationX][defender.formationY].size() > 0){
                for(int i = 0; i < f.gridAllEffectList[defender.formationX][defender.formationY].size(); i++){
                    SkillEffect se = f.gridAllEffectList[defender.formationX][defender.formationY].get(i);
                    if(se.name.equals("防御力UP")){
                        formationDEF += se.value;
                    }else if(se.name.equals("防御力DOWN")){
                        formationDEF -= se.value;
                    }
                }
            }
        }

        //防御力Buff
        int DEFBuff = 0;
        efList = isPlayerAttack? leftEffectList[defender.formationX][defender.formationY]:rightEffectList[defender.formationX][defender.formationY];
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            if(e.name.equals("防御力UP")){
                DEFBuff += e.value;
            }else if(e.name.equals("防御力DOWN")){
                DEFBuff -= e.value;
            }else if(e.name.equals("濒死时防御力UP") && (1.0f*defender.realHP/defender.getRealMaxHP() < 0.2f)){
                DEFBuff += e.value;
            }else if(e.name.equals("HP最大时防御力UP") && (defender.realHP == defender.getRealMaxHP())){
                DEFBuff += e.value;
            }
        }
        if(DEFBuff > 100) DEFBuff = 100;
        if(DEFBuff < -95) DEFBuff = -95;

        //是否无视防御力
        boolean isIgnoreDEF = false;
        if(!(plateType == MAGIA || plateType == DOPPEL)){
            efList = isPlayerAttack? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
            for(int i = 0; i < efList.size(); i++){
                Effect e = efList.get(i);
                if(e.name.equals("无视防御力")){
                    if(colorToss(e.probability)){
                        isIgnoreDEF = true;
                        Effect e2 = new Effect();
                        e2.name = "无视防御力";
                        sendEffect(e2,defender.formationX,defender.formationY,!isPlayerAttack,true);
                        break;
                    }
                }
            }
        }



        //角色基础M/d系数
        float fundamentalMDCoefficient = 1.0f;
        if(plateType == MAGIA){
            fundamentalMDCoefficient = 1.0f*(attacker.star == 5? attacker.magiaAfterMagnification: attacker.magiaOriginMagnification)/100;
        }else if(plateType == DOPPEL){
            fundamentalMDCoefficient = 1.0f*attacker.doppelMagnification/100;
        }

        //Magia伤害UP
        int magiaDamageUp = 0;
        if((plateType == MAGIA || plateType == DOPPEL)){
            efList = isPlayerAttack? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
            for(int i = 0; i < efList.size(); i++){
                Effect e = efList.get(i);
                if(e.name.equals("Magia伤害UP") && (plateType == MAGIA || plateType == DOPPEL)){
                    magiaDamageUp += e.value;
                }else if(e.name.equals("Magia伤害DOWN") && (plateType == MAGIA || plateType == DOPPEL)){
                    magiaDamageUp -= e.value;
                }
            }
            if(magiaDamageUp > 100)magiaDamageUp = 100;
            if(magiaDamageUp < -95)magiaDamageUp = -95;
        }

        //M/D连位加成
        float concatenationBonus = 1.0f;
        if(plateType == MAGIA || plateType == DOPPEL){
            if(smallPlateNumber > 0){
                for(int i = smallPlateNumber-1; i >= 0; i--){
                    if(smallPlateList[i] >= 4){
                        //这个位置是M/D
                        concatenationBonus += 0.1f;
                    }else{
                        break;
                    }
                }
            }
        }

        int fundamentalDamage = 0;
        //基础伤害
        float tempATK = panelATK * 1.0f * (100 + formationATK) / 100 * 1.0f * (100 + atkBuff);
        float tempDEF = panelDEF * 1.0f * (100 + formationDEF) / 100 * 1.0f * (100 + DEFBuff);
        if(plateType == ACCELE || plateType == BLAST_HORIZONTAL || plateType == BLAST_VERTICAL || plateType == CHARGE){
            fundamentalDamage = (int)(tempATK/100 - (isIgnoreDEF? 0:(tempDEF/100)));
        }else if(plateType == MAGIA || plateType == DOPPEL){
            fundamentalDamage = (int)((tempATK/100 - (tempDEF/100))*fundamentalMDCoefficient
                    *(1.0f*(100+magiaDamageUp)/100)*concatenationBonus);
        }
        fundamentalDamage = Math.max(fundamentalDamage,500);


        //盘型基础系数
        float fundamentalPlateCoefficient = 1.0f;

        if(isPlayerAttack){
            boolean isPuella = true;//是否为单人三连
            for(int i = 0; i < 3; i++){
                if(smallPlateList[i] <= 4){
                    if(plateList[smallPlateList[i]].c != attacker){
                        isPuella = false;
                        break;
                    }
                }else{
                    if(StartActivity.characters[smallPlateList[i]-5] != attacker){
                        isPuella = false;
                        break;
                    }
                }
            }
            boolean isCombo = true;//是否为ABC盘三连
            if(smallPlateList[smallPlateNumber] <= 4){
                //是普通的盘型
                for(int i = 0; i < 3; i++){
                    if(smallPlateList[i] <= 4){
                        if(plateList[smallPlateList[i]].plate != plateType){
                            isCombo = false;
                            break;
                        }
                    }else{
                        //有m/d盘
                        isCombo = false;
                        break;
                    }
                }
            }else{
                isCombo = false;
            }


            switch(plateType){
                case ACCELE:
                    fundamentalPlateCoefficient = isPuella? (isCombo? 1.2f:1.2f):(isCombo? 1.0f:1.0f);
                    break;
                case BLAST_HORIZONTAL: case BLAST_VERTICAL:
                    fundamentalPlateCoefficient = isPuella? (isCombo? 1.2f:0.9f):(isCombo? 0.9f:0.6f);
                    break;
                case CHARGE:
                    fundamentalPlateCoefficient = isPuella? (isCombo? 1.2f:1.2f):(isCombo? 1.0f:1.0f);
                    break;
                default:
            }
        }

        //B盘位置系数
        float BlastPositionCoefficient = 1.0f;
        if(plateType == BLAST_HORIZONTAL || plateType == BLAST_VERTICAL){
            //为b盘
            BlastPositionCoefficient = (smallPlateNumber == 0)? 1.0f:((smallPlateNumber == 1)? 1.1f:1.2f);
        }

        //叠C原始倍率
        float chargeCoefficient = 1.0f;
        if(plateType == ACCELE || plateType == BLAST_VERTICAL || plateType == BLAST_HORIZONTAL){
            if(plateType == ACCELE){
                chargeCoefficient = multiChargeTable[0][isPlayerAttack? chargeNumber:enemyChargeNumber];
            }else if(plateType == BLAST_VERTICAL || plateType == BLAST_HORIZONTAL){
                chargeCoefficient = multiChargeTable[1][isPlayerAttack? chargeNumber:enemyChargeNumber];
            }
            //charge后伤害提升buff
            int chargeDamageUp = 0;
            efList = isPlayerAttack? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
            for(int i = 0; i < efList.size(); i++){
                Effect e = efList.get(i);
                if(e.name.equals("Charge后伤害UP")){
                    chargeDamageUp += e.value;
                }
            }
            chargeCoefficient *= 1.0f*(100+chargeDamageUp)/100;
        }
        if(chargeCoefficient >= 5.5f) chargeCoefficient = 5.5f;



        //基础属性克制系数
        float elementRestrainedCoefficient = 1.0f;
        switch(attacker.element){
            case "tree":
                elementRestrainedCoefficient = defender.element.equals("fire") ? 0.5f:(defender.element.equals("water")? 1.5f:1.0f);
                break;
            case "water":
                elementRestrainedCoefficient = defender.element.equals("tree") ? 0.5f:(defender.element.equals("fire")? 1.5f:1.0f);
                break;
            case "fire":
                elementRestrainedCoefficient = defender.element.equals("water") ? 0.5f:(defender.element.equals("tree")? 1.5f:1.0f);
                break;
            case "light":
                elementRestrainedCoefficient = defender.element.equals("dark") ? 1.5f:1.0f;
                break;
            case "dark":
                elementRestrainedCoefficient = defender.element.equals("light") ? 1.5f:1.0f;
                break;
            default:
        }

        //状态异常固有加成
        float StateAbnormalCoefficient = 1.0f;
        if(judgeFloatEqual(elementRestrainedCoefficient,1.5f)){
            //处于属性克制下,检查defender是否处于特定异常状态中
            efList = isPlayerAttack? leftEffectList[defender.formationX][defender.formationY]:rightEffectList[defender.formationX][defender.formationY];
            for(int i = 0; i < efList.size(); i++){
                Effect e = efList.get(i);
                if(e.name.equals("烧伤") || e.name.equals("诅咒")){
                    StateAbnormalCoefficient = 1.2f;
                    break;
                }else if(e.name.equals("眩晕") || e.name.equals("拘束")){
                    StateAbnormalCoefficient = 1.2f;
                    break;
                }else if(e.name.equals("黑暗") || e.name.equals("幻惑")){
                    StateAbnormalCoefficient = 1.2f;
                    break;
                }
            }
        }

        //敌方是否处于异常状态
        boolean isDefenderStateAbnormal = false;
        efList = isPlayerAttack? leftEffectList[defender.formationX][defender.formationY]:rightEffectList[defender.formationX][defender.formationY];
        isDefenderStateAbnormal = hasAbnormalState(efList);


        //各种伤害UP buff
        int damageBuff = 0;
        efList = isPlayerAttack? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            if(e.name.equals("造成伤害UP")){
                damageBuff += e.value;
            }else if(e.name.equals("造成伤害DOWN")){
                damageBuff -= e.value;
            }else if(e.name.equals("Charge盘伤害UP") && (plateType == CHARGE)){
                damageBuff += e.value;
            }else if(e.name.equals("Blast伤害DOWN") && (plateType == BLAST_HORIZONTAL || plateType == BLAST_VERTICAL)){
                damageBuff -= e.value;
            }else if(e.name.equals("Blast伤害UP") && (plateType == BLAST_HORIZONTAL || plateType == BLAST_VERTICAL)){
                damageBuff += e.value;
            }else if(e.name.equals("Doppel伤害UP") && (plateType == DOPPEL)){
                damageBuff += e.value;
            }else if(e.name.equals("敌方状态异常时伤害UP") && isDefenderStateAbnormal){
                damageBuff += e.value;
            }
        }

        //各种伤害削减
        boolean isIgnoreDamageReduction = false;
        efList = isPlayerAttack? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            if(e.name.equals("伤害削减无效")){
                if(colorToss(e.probability)){
                    isIgnoreDamageReduction = true;
                    Effect e2 = new Effect();
                    e2.name = "伤害削减无效";
                    sendEffect(e2, defender.formationX, defender.formationY,!isPlayerAttack,true);
                    break;
                }
            }
        }
        if(!isIgnoreDamageReduction){
            efList = isPlayerAttack? leftEffectList[defender.formationX][defender.formationY]:rightEffectList[defender.formationX][defender.formationY];
            for(int i = 0; i < efList.size(); i++){
                Effect e = efList.get(i);
                if(e.name.equals("Magia伤害削减") || (plateType == MAGIA || plateType == DOPPEL)){
                    damageBuff -= e.value;
                    break;
                }else if(e.name.equals("伤害削减")){
                    damageBuff -= e.value;
                    break;
                }else if(e.name.equals("Blast伤害削减") || (plateType == BLAST_VERTICAL || plateType == BLAST_HORIZONTAL)){
                    damageBuff -= e.value;
                    break;
                }
            }
        }
        if(damageBuff > 200)damageBuff = 200;
        if(damageBuff < -70)damageBuff = -70;

        //是否暴击
        boolean isCriticalAttack = false;
        if(!(plateType == MAGIA || plateType == DOPPEL)){
            int maxProbability = 0;
            efList = isPlayerAttack? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
            for(int i = 0; i < efList.size(); i++){
                Effect e = efList.get(i);
                if(e.name.equals("暴击")){
                    if(e.probability > maxProbability){
                        maxProbability = e.probability;
                    }
                }
            }
            isCriticalAttack = colorToss(maxProbability);
            if(isCriticalAttack){
                Effect e = new Effect();
                e.name = "暴击";
                sendEffect(e,defender.formationX, defender.formationY,!isPlayerAttack,true);
                if(damageBuff > 0)damageBuff += 100;
                if(damageBuff <= 0){
                    damageBuff += 100;
                    damageBuff *= 2;
                    damageBuff -= 100;
                }
            }
        }
        Log.d("damage", "白值ATK"+panelATK+" 阵形ATK"+formationATK+" 攻击力Buff"+atkBuff+" 白值防御力"+panelDEF+" 阵形防御力"+formationDEF+" 防御力Buff"+DEFBuff);

        Log.d("damage","基础M/d系数"+fundamentalMDCoefficient+" M伤害Up"+magiaDamageUp+" MD连位加成"+concatenationBonus);

        Log.d("damage","基础伤害"+fundamentalDamage+" 盘型基础系数"+fundamentalPlateCoefficient+" B盘位置系数"+BlastPositionCoefficient+" 叠C原始倍率"+chargeCoefficient+" 属性克制系数"+elementRestrainedCoefficient+" 异常状态固有加成"+StateAbnormalCoefficient);

        switch(plateType){
            case ACCELE: case BLAST_HORIZONTAL: case BLAST_VERTICAL: case CHARGE:
                int finalDamage = (int)Math.max(250, fundamentalDamage * fundamentalPlateCoefficient * BlastPositionCoefficient
                        * chargeCoefficient * elementRestrainedCoefficient * StateAbnormalCoefficient
                        * 1.0f * (100 + damageBuff) / 100);
                Log.d("damage","伤害buff"+damageBuff+" 最终伤害"+finalDamage);
                return (int)((0.95f + Math.random()*0.1)*finalDamage);
            case MAGIA:case DOPPEL:
                finalDamage = (int)Math.max(250, fundamentalDamage
                        * elementRestrainedCoefficient * StateAbnormalCoefficient
                        * 1.0f*(100+damageBuff)/100);
                Log.d("damage","伤害buff"+damageBuff+" 最终伤害"+(int) finalDamage);
                return (int)((0.95f + Math.random()*0.1)*finalDamage);
            default:
        }
        return -1;
    }

    public void updateChargeView(){
        if(chargeNumber > 0){
            charge_number_view.setVisibility(VISIBLE);
            charge_frame.setVisibility(VISIBLE);
            charge_number_view.setText(""+chargeNumber);
        }else{
            charge_number_view.setVisibility(GONE);
            charge_frame.setVisibility(GONE);
        }
    }

    public void findAliveCharacter(int[] positionList, boolean isRight){
        SpriteViewer[][] svList = isRight? rightCharList:leftCharList;
        if(isRight){
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < 3; j++){
                    if(svList[j][i] != null && svList[j][i].c.realHP > 0){
                        positionList[0] = j;
                        positionList[1] = i;
                        return;
                    }
                }
            }
        }else{
            for(int i = 2; i >= 0; i--){
                for(int j = 0; j < 3; j++){
                    if(svList[j][i] != null && svList[j][i].c.realHP > 0){
                        positionList[0] = j;
                        positionList[1] = i;
                        return;
                    }
                }
            }
        }
        positionList[0] = -1;
        positionList[1] = -1;
    }

    public void setDamageOnCharacter(Character c, int damage, boolean isRight, boolean isMagnified){
        //不负责发送伤害数字
        c.realHP -= damage;

        if(c.realHP <= 0){
            //是否触发忍耐
            ArrayList<Effect> efList = isRight? rightEffectList[c.formationX][c.formationY]:leftEffectList[c.formationX][c.formationY];
            for(int i = 0; i < efList.size(); i++){
                Effect e = efList.get(i);
                if(e.name.equals("忍耐")){
                    c.realHP = 1;
                    sendEffect(e,c.formationX,c.formationY,isRight,isMagnified);
                    efList.remove(e);
                    break;
                }
            }
        }
        if(c.realHP <= 0){
            c.realHP = 0;
        }
        if(c.realHP >= c.getRealMaxHP()){
            c.realHP = c.getRealMaxHP();
        }
//        Log.d("Sam","剩余HP:" + c.realHP);

        StateBar sb = isRight? rightStateBarList[c.formationX][c.formationY]:leftStateBarList[c.formationX][c.formationY];
        if(sb != null){
            sb.updateHp(c.getRealMaxHP(),c.realHP);
            if(c.realHP == 0){
//            Log.d("Sam","characterDead");
                SpriteViewer sv = isRight? rightCharList[c.formationX][c.formationY]:leftCharList[c.formationX][c.formationY];
                sv.spriteName = "dead";
                changeSprite(c.formationX, c.formationY, isRight);
            }
        }

    }

    public void setMpOnCharacter(Character c, int mp, boolean isRight){
        //不负责发送数字
        c.realMP = mp;
        if(c.realMP >= c.getMaxMp()){
            c.realMP = c.getMaxMp();
        }
        if(c.realMP <= 0){
            c.realMP = 0;
        }
        StateBar sb = isRight? rightStateBarList[c.formationX][c.formationY]:leftStateBarList[c.formationX][c.formationY];
        sb.updateMp(c.realMP);
    }

    public int getAttackMP(Character attacker, boolean isPlayerAttack, int plateType){
        if(isPlayerAttack){
            if(smallPlateList[smallPlateNumber] >= 5){
                //是m/d盘
                return 0;
            }
        }

        //是否有首A奖励
        boolean isFirstPlateA = false;
        if(isPlayerAttack){
            isFirstPlateA = (smallPlateList[0] <= 4) && (plateList[smallPlateList[0]].plate == ACCELE);
        }

        //单盘原始MP
        int simplePlateOriginMp = 0;
        switch(isPlayerAttack ? plateList[smallPlateList[smallPlateNumber]].plate : plateType){
            case ACCELE:
                if(isFirstPlateA){
                    simplePlateOriginMp = 100 + smallPlateNumber * 35;
                }else{
                    simplePlateOriginMp = 70 + smallPlateNumber * 35;
                }
                break;
            case BLAST_HORIZONTAL: case BLAST_VERTICAL:
                if(isFirstPlateA){
                    return 30;
                }
                return 0;
            case CHARGE:
                if(isFirstPlateA){
                    simplePlateOriginMp = 50 + smallPlateNumber * 10;
                }else{
                    simplePlateOriginMp = 20 + smallPlateNumber * 10;
                }
                break;
        }

        //AcceleMP Buff
        int AcceleMpBuff = 0;
        ArrayList<Effect> efList = isPlayerAttack ? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
        if((isPlayerAttack ? plateList[smallPlateList[smallPlateNumber]].plate : plateType) == ACCELE){
            for(int i = 0; i < efList.size(); i++){
                Effect e = efList.get(i);
                if(e.name.equals("AcceleMPDOWN")){
                    AcceleMpBuff -= e.value;
                }else if(e.name.equals("AcceleMPUP")){
                    AcceleMpBuff += e.value;
                }
            }
            if(AcceleMpBuff >= 100)AcceleMpBuff = 100;
            if(AcceleMpBuff <= -95)AcceleMpBuff = -95;
        }

        //叠C倍率
        float multiChargeCoefficient = 1.0f;
        if(isPlayerAttack){
            multiChargeCoefficient = multiChargeTable[2][chargeNumber];
        }

        //角色攻击MP率
        float attackMpRatio = attacker.mpAttackRatio;

        //MP获得量 Buff
        int MpGetBuff = 0;
        efList = isPlayerAttack ? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            if(e.name.equals("MP获得量DOWN")){
                MpGetBuff -= e.value;
            }else if(e.name.equals("MP获得量UP")){
                MpGetBuff += e.value;
            }else if(e.name.equals("MP回复禁止")){
                return 0;
            }
        }
        if(MpGetBuff >= 100)MpGetBuff = 100;
        if(MpGetBuff <= -95)MpGetBuff = -95;

        return (int)Math.floor(Math.floor(Math.round(simplePlateOriginMp* 1.0f*(100+AcceleMpBuff)/100 )* multiChargeCoefficient)*attackMpRatio* 1.0f*(100+MpGetBuff)/100);
    }

    public int getDefendMP(Character attacker, Character defender, boolean isRight){
        //基础受击MP
        int fundamentalDefendMp = 40;

        //角色受击Mp率
        float defendMpRatio = defender.mpDefendRatio;

        //MP获得量 Buff
        int MpGetBuff = 0;
        ArrayList<Effect> efList = isRight ? rightEffectList[defender.formationX][defender.formationY]:leftEffectList[defender.formationX][defender.formationY];
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            if(e.name.equals("MP获得量DOWN")){
                MpGetBuff -= e.value;
            }else if(e.name.equals("MP获得量UP")){
                MpGetBuff += e.value;
            }else if(e.name.equals("MP回复禁止")){
                return 0;
            }
        }
        if(MpGetBuff >= 100)MpGetBuff = 100;
        if(MpGetBuff <= -95)MpGetBuff = -95;

        //被弱点属性攻击时MPUP
        int MpGetFromRestraintAttacker = 0;
        if(isRestrained(attacker,defender) == 1){
            efList = isRight ? rightEffectList[defender.formationX][defender.formationY]:leftEffectList[defender.formationX][defender.formationY];
            for(int i = 0; i < efList.size(); i++){
                Effect e = efList.get(i);
                if(e.name.equals("被弱点属性攻击时MPUP")){
                    MpGetFromRestraintAttacker += e.value;
                }
            }
        }


        return (int)Math.floor(fundamentalDefendMp * defendMpRatio * 1.0f * (100 + MpGetBuff)/100)
                + (int)Math.floor(MpGetFromRestraintAttacker * defendMpRatio * 1.0f * (100 + MpGetBuff)/100);
    }

    public Character judgeProvocation(Character attacker, Character target, boolean isPlayerAttack){
        //是否触发挑拨无效
        boolean isProvocationNullified = false;
        ArrayList<Effect> efList = isPlayerAttack? rightEffectList[attacker.formationX][attacker.formationY]:leftEffectList[attacker.formationX][attacker.formationY];
        isProvocationNullified = isTriggerEffect(efList,"挑拨无效");

        //查看受击一方挑拨
        SpriteViewer[][] svList = isPlayerAttack? leftCharList:rightCharList;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(svList[i][j] != null && svList[i][j].c.realHP > 0){
                    //查看是否有挑拨效果
                    efList = isPlayerAttack? leftEffectList[i][j]:rightEffectList[i][j];
                    for(int k = 0; k < efList.size(); k++){
                        Effect e = efList.get(k);
                        if(e.name.equals("挑拨")){
                            if(colorToss(e.probability)){
                                if(isProvocationNullified){
                                    Effect e2 = new Effect();
                                    e2.name = "挑拨无效";
                                    sendEffect(e2,i,j,!isPlayerAttack,true);
                                    return null;
                                }
                                sendEffect(e,i,j,!isPlayerAttack,true);
                                return svList[i][j].c;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void startLeftAttack(){
        recoverCharacterSize(false);
        recoverCharacterSize(true);
        smallPlateNumber = 0;
        clickable = false;
        platesLayout.setVisibility(View.INVISIBLE);
        touchImageListLayout[chooseMonsterX][chooseMonsterY].setVisibility(GONE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                magnifyCharacter(false);
                int monsterNumber = 0;
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        if(monsterFormation[i][j] != null && monsterFormation[i][j].realHP > 0 && !hasAbnormalState(leftEffectList[i][j],new String[]{"魅惑","眩晕","拘束"})){
                            monsterNumber++;
                        }
                    }
                }
                if(monsterNumber == 0){
                    //没有可行动的敌人, 提前结束回合
                    //回合结束前结算异常效果和回血效果，并结束回合
                    calculateStateAndEndTurn();
                }
                int chooseMonster = (int)(Math.random()*monsterNumber);
                int tempCount = 0;
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        if(monsterFormation[i][j] != null && monsterFormation[i][j].realHP > 0 && !hasAbnormalState(leftEffectList[i][j],new String[]{"魅惑","眩晕","拘束"})){
                            if(tempCount == chooseMonster){
                                int plate = monsterFormation[i][j].plateList[(int)(Math.random()*5)];
                                monsterPlate = plate;
                                if(isBossBattle){
                                    monsterAttackerX = 1;
                                    monsterAttackerY = 1;
                                }else{
                                    monsterAttackerX = i;
                                    monsterAttackerY = j;
                                }

                                if(plate == CharacterPlateView.ACCELE || plate == CharacterPlateView.CHARGE){
                                    leftCharList[monsterAttackerX][monsterAttackerY].spriteName = "attack_in";
                                }else if(plate == CharacterPlateView.BLAST_HORIZONTAL){
                                    leftCharList[monsterAttackerX][monsterAttackerY].spriteName = "attackh_in";
                                }else if(plate == CharacterPlateView.BLAST_VERTICAL){
                                    leftCharList[monsterAttackerX][monsterAttackerY].spriteName = "attackv_in";
                                }
                                leftCharList[monsterAttackerX][monsterAttackerY].webView.loadUrl("javascript:setAnimationIndex(" + 3 + ")");
                                changeSprite(monsterAttackerX,monsterAttackerY, false);
                                final int tempi = monsterAttackerX;
                                final int tempj = monsterAttackerY;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        leftStateBarList[tempi][tempj].setVisibility(GONE);
                                    }
                                },500);
                                return;
                            }else{
                                tempCount++;
                            }

                        }
                    }
                }

            }
        }, 1000);
    }

    public void calculateStateAndEndTurn(){
        //敌人三个盘都打完了
        recoverCharacterSize(false);
        recoverCharacterSize(true);
        // 计算回合结束后状态
        int maxWaitTime = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                final int tempI = i;
                final int tempJ = j;
                if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                    int tempWaitTime = 100;
                    ArrayList<Effect> efList = rightEffectList[i][j];
                    for(int k = 0; k < efList.size(); k++){
                        final Effect e = efList.get(k);
                        if(e.name.equals("毒") || e.name.equals("烧伤") || e.name.equals("诅咒")){
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    int damage = (int)(1.0f*(rightCharList[tempI][tempJ].c.getRealMaxHP())*e.value/100);
                                    setDamageOnCharacter(rightCharList[tempI][tempJ].c,damage,true,false);
                                    sendDamageNumber(damage,tempI,tempJ,TEXT_RED,true,false);
                                    if(rightCharList[tempI][tempJ].c.realHP > 0){
                                        rightCharList[tempI][tempJ].spriteName = "damage";
                                        changeSprite(tempI, tempJ,true);
                                        rightCharList[tempI][tempJ].webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                    }
                                    sendEffect(e,tempI,tempJ,true,false);
                                }
                            }, tempWaitTime);
                            tempWaitTime += DELTA_BETWEEN_EFFECT_SHOW;
                            if(maxWaitTime < tempWaitTime){
                                maxWaitTime = tempWaitTime;
                            }
                        }else if(e.name.equals("HP自动回复")){
                            //是否有HP回复禁止
                            if(!isTriggerEffect(efList,"HP回复禁止")){
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int recoverHP = (int)(1.0f*(rightCharList[tempI][tempJ].c.getRealMaxHP())*e.value/100);
                                        setDamageOnCharacter(rightCharList[tempI][tempJ].c,-recoverHP,true,false);
                                        sendDamageNumber(recoverHP,tempI,tempJ,TEXT_GREEN,true,false);
                                        sendEffect(e,tempI,tempJ,true,false);
                                    }
                                }, tempWaitTime);
                                tempWaitTime += DELTA_BETWEEN_EFFECT_SHOW;
                                if(maxWaitTime < tempWaitTime){
                                    maxWaitTime = tempWaitTime;
                                }
                            }
                        }
                    }
                }
                if(leftCharList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                    if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                        int tempWaitTime = 100;
                        ArrayList<Effect> efList = leftEffectList[i][j];
                        for(int k = 0; k < efList.size(); k++){
                            final Effect e = efList.get(k);
                            if(e.name.equals("毒") || e.name.equals("烧伤") || e.name.equals("诅咒")){
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        int damage = (int)(1.0f*(leftCharList[tempI][tempJ].c.getRealMaxHP())*e.value/100);
                                        setDamageOnCharacter(leftCharList[tempI][tempJ].c,damage,false,false);
                                        sendDamageNumber(damage,tempI,tempJ,TEXT_RED,false,false);
                                        if(leftCharList[tempI][tempJ].c.realHP > 0){
                                            leftCharList[tempI][tempJ].spriteName = "damage";
                                            changeSprite(tempI, tempJ,false);
                                            leftCharList[tempI][tempJ].webView.loadUrl("javascript:setAnimationIndex(" + 6 + ")");
                                        }
                                        sendEffect(e,tempI,tempJ,false,false);
                                    }
                                }, tempWaitTime);
                                tempWaitTime += DELTA_BETWEEN_EFFECT_SHOW;
                                if(maxWaitTime < tempWaitTime){
                                    maxWaitTime = tempWaitTime;
                                }
                            }else if(e.name.equals("HP自动回复")){
                                if(!isTriggerEffect(efList,"HP回复禁止")){
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            int recoverHP = (int)(1.0f*(leftCharList[tempI][tempJ].c.getRealMaxHP())*e.value/100);
                                            setDamageOnCharacter(leftCharList[tempI][tempJ].c,-recoverHP,false,false);
                                            sendDamageNumber(recoverHP,tempI,tempJ,TEXT_GREEN,false,false);
                                            sendEffect(e,tempI,tempJ,false,false);
                                        }
                                    }, tempWaitTime);
                                    tempWaitTime += DELTA_BETWEEN_EFFECT_SHOW;
                                    if(maxWaitTime < tempWaitTime){
                                        maxWaitTime = tempWaitTime;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if(maxWaitTime > 0){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    endTurn();
                }
            }, maxWaitTime += 1500);
        }else{
            endTurn();
        }

    }

    public void endTurn(){
        //更新怪物选择箭头
        int[] tempP = new int[]{-1, -1};
        if(leftCharList[chooseMonsterX][chooseMonsterY].c.realHP <= 0){
            touchImageListLayout[chooseMonsterX][chooseMonsterY].setVisibility(GONE);
            findAliveCharacter(tempP,false);
            if(tempP[0] == -1){
                //说明敌人全部被干掉了
                win();
                return;
            }else{
                chooseMonsterX = tempP[0];
                chooseMonsterY = tempP[1];
            }
            touchImageListLayout[chooseMonsterX][chooseMonsterY].setVisibility(VISIBLE);
        }

        //判断我方是否全部死亡
        tempP = new int[]{-1,-1};
        findAliveCharacter(tempP,true);
        if(tempP[0] == -1){
            //说明我方全部被干掉了
            lose();
            return;
        }

        //更新角色diamond数量
        for(int i = 0; i < 3; i++){
            if(smallPlateList[i] != -1){
                if(smallPlateConnectToList[i] == null){
                    //该盘没有连携
                    if(smallPlateList[i] <= 4 && plateList[smallPlateList[i]].c.diamondNumber < 3){
                        plateList[smallPlateList[i]].c.diamondNumber++;
                    }
                }else{
                    //该盘有连携
                    if(smallPlateConnectToList[i].diamondNumber < 3){
                        smallPlateConnectToList[i].diamondNumber++;
                    }
                    plateList[smallPlateList[i]].c.diamondNumber = 0;
                }
            }
        }
        for(int i = 0; i < 3; i++){
            smallPlateList[i] = -1;
            smallPlateXList[i] = -1;
            smallPlateYList[i] = -1;
            smallPlateConnectToList[i] = null;
        }


        //更新回合信息
        turn++;
        //更新主动技能cd
        for(int i = 0; i < 5; i++){
            if(StartActivity.characters[i] != null && StartActivity.characters[i].realHP > 0){
                boolean isSkillCDAccelerated = false;
                if(isTriggerEffect(rightEffectList[StartActivity.characters[i].formationX][StartActivity.characters[i].formationY],"技能冷却加速")){
                    isSkillCDAccelerated = true;
                }
                for(int j = 0; j < 4; j++){
                    if(StartActivity.characters[i].memoriaList[j] != null){
                        if(StartActivity.characters[i].memoriaList[j].CDNow > 0){
                            StartActivity.characters[i].memoriaList[j].CDNow--;
                        }
                        if(isSkillCDAccelerated && StartActivity.characters[i].memoriaList[j].CDNow > 0){
                            StartActivity.characters[i].memoriaList[j].CDNow--;
                        }
                    }
                }
            }
        }

        //更新角色effect时效
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(rightEffectList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                    if(rightEffectList[i][j].size()>0){
                        for(int k = 0; k < rightEffectList[i][j].size(); k++){
                            Effect e = rightEffectList[i][j].get(k);
                            if(e.time < 999){
                                e.time--;
                            }
                            if(e.time <= 0){
                                rightEffectList[i][j].remove(e);
                                k--;
                            }
                        }
                    }
                }
                if(leftEffectList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                    if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                        if(leftEffectList[i][j].size()>0){
                            for(int k = 0; k < leftEffectList[i][j].size(); k++){
                                Effect e = leftEffectList[i][j].get(k);
                                if(e.time < 999){
                                    e.time--;
                                }
                                if(e.time <= 0){
                                    leftEffectList[i][j].remove(e);
                                    k--;
                                }
                            }
                        }
                    }
                }
            }
        }

        if(!randomChoosePlates()){
            //说明我方没有可行动角色
            startLeftAttack();
            return;
        }else{
            showPlate();
        }

        platesLayout.setVisibility(View.VISIBLE);
        //收回magia盘
        if(barState == MAGIA_BAR_SHOW){
            barState = PLATE_SHOW;
            magiaPlate.setBackgroundResource(R.drawable.magia_plate);
            skillPlate.setVisibility(VISIBLE);
            showPlate();
        }
        touchImageListLayout[chooseMonsterX][chooseMonsterY].setVisibility(VISIBLE);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(leftCharList[i][j] != null && leftCharList[i][j].c.realHP > 0){
                    if(!isBossBattle || (isBossBattle && (i == 1) && (j == 1))){
                        leftCharList[i][j].spriteName = getRecoverOriginSpriteName(i,j,false,"wait",false);
                        changeSprite(i, j, false);
                    }
                }
                if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                    rightCharList[i][j].spriteName = getRecoverOriginSpriteName(i,j,true,"wait",false);
                    changeSprite(i, j, true);
                }
            }
        }


        smallPlateNumber = 0;
        clickable = true;


    }

    public void handleEffectiveness(Effect e, int x, int y, boolean isRight, boolean isMagnified){
        Character c = isRight? rightCharList[x][y].c:leftCharList[x][y].c;
        ArrayList<Effect> efList = isRight? rightEffectList[x][y]:leftEffectList[x][y];
        switch(e.name){
            // case "重抽为Accele的Disc":// case "重抽为Blast的Disc":
            // case "重抽为同属性的Disc":case "重抽为Charge的Disc":// case "重抽为自己的Disc":
            case "重抽Disc":
                randomChoosePlates();
                break;
            case "BUFF解除":
                for(int i = 0; i < efList.size(); i++){
                    Effect e2 = efList.get(i);
                    if(e2.time < 999 && judgeBuffAndDebuff(e2) == 1){
                        efList.remove(e2);
                        i--;
                    }
                }
                break;
            case "DEBUFF解除":
                for(int i = 0; i < efList.size(); i++){
                    Effect e2 = efList.get(i);
                    if(e2.time < 999 && judgeBuffAndDebuff(e2) == -1){
                        efList.remove(e2);
                        i--;
                    }
                }
                break;
            case "异常状态解除":
                for(int i = 0; i < efList.size(); i++){
                    Effect e2 = efList.get(i);
                    if(e2.time < 999){
                        switch(e2.name){
                            case "毒": case "烧伤": case "诅咒":
                            case "魅惑": case "眩晕": case "拘束":
                            case "雾": case "黑暗": case "幻惑":
                            case "技能封印": case "Magia封印":
                            case "MP回复禁止": case "HP回复禁止":
                                efList.remove(e2);
                                i--;
                            default:
                        }
                    }
                }
                break;
            case "HP回复":
                int recoverHP = (int)(1.0f * e.value * c.getRealMaxHP() / 100);
                setDamageOnCharacter(c,-recoverHP,isRight,isMagnified);
                sendDamageNumber(recoverHP,x,y,TEXT_GREEN,isRight,isMagnified);
                break;
            case "MP回复":
                //MP获得量 Buff
                int MpGetBuff = 0;
                for(int i = 0; i < efList.size(); i++){
                    Effect e3 = efList.get(i);
                    if(e3.name.equals("MP获得量DOWN")){
                        MpGetBuff -= e3.value;
                    }else if(e.name.equals("MP获得量UP")){
                        MpGetBuff += e3.value;
                    }
                }
                int recoverMP = (int)Math.floor(e.value * 10 * (1.0f * (100 + MpGetBuff)/100));
                boolean isMpRecoverForbidden = isTriggerEffect(efList, "MP回复禁止");
                if(!isMpRecoverForbidden){
                    setMpOnCharacter(c, c.realMP + recoverMP,isRight);
                    sendDamageNumber(recoverMP/10,x,y,TEXT_BLUE,isRight,isMagnified);
                    sendEffect(e,x,y,isRight,isMagnified);
                }else{
                    Effect e2 = new Effect();
                    e2.name = "MP回复禁止";
                    sendEffect(e2,x,y,isRight,isMagnified);
                }
                break;
            case "MP伤害":
                int reduceMP = e.value * 10;
                setMpOnCharacter(c,c.realMP - reduceMP,isRight);
                sendDamageNumber(e.value,x,y,TEXT_BLUE,isRight,isMagnified);
                sendEffect(e,x,y,isRight,isMagnified);
                break;
            default:

                if(judgeBuffAndDebuff(e) == -1){
                    //检查DEBUFF无效
                    for(int i = 0; i < efList.size(); i++){
                        Effect e2 = efList.get(i);
                        if(e2.name.equals("DEBUFF无效")){
                            efList.remove(e2);
                            sendEffect(e2,x,y,isRight,isMagnified);
                            return;
                        }
                    }
                }

                sendEffect(e,x,y,isRight,isMagnified);
                if(isRight){
                    rightEffectList[x][y].add(e);
                }else{
                    leftEffectList[x][y].add(e);
                }
        }


    }

    public int judgeBuffAndDebuff(Effect e){
        //1为正面buff, -1为负面, 0为其他
        switch(e.name){
            case"攻击力UP": case"防御力UP":
            case"造成伤害UP":case"伤害削减":case "Magia伤害削减":case "HP最大时防御力UP":
            case "异常状态耐性UP": case "Blast攻击时MP获得": case "AcceleMPUP": case "HP最大时攻击力UP":
            case "Charge盘伤害UP": case "Charge后伤害UP":case "敌方状态异常时伤害UP":
            case "战斗不能时获得防御力UP": case "战斗不能时获得攻击力UP":case "Magia伤害UP":
            case "对魔女伤害上升":case "火属性攻击力UP": case "Blast伤害UP": case "Blast伤害削减":
            case "被弱点属性攻击时MPUP": case "MP获得量UP": case "濒死时防御力UP":case "濒死时攻击力UP": case "Doppel伤害UP":case "状态异常1回无效": case "Magia封印无效":
            case "回避无效": case "反击无效": case "毒无效":case "挑拨无效": case "诅咒无效":
            case "雾无效": case "忍耐": case "烧伤无效":case "黑暗无效":case "技能封印无效":
            case "魅惑无效": case "DEBUFF无效": case "无视防御力": case "伤害削减无效":
            case "眩晕无效":case "技能冷却加速": case "拘束无效": case "幻惑无效":case "挑拨":case "追击":case "反击":case "回避":
            case "暴击":case "保护同伴":
            case "水属性攻击力UP":case "木属性攻击力UP":
            case "光属性攻击力UP":case "暗属性攻击力UP":
            case "攻击时给予状态毒":case "攻击时给予状态烧伤":case "攻击时给予状态诅咒":
            case "攻击时给予状态魅惑":case "攻击时给予状态眩晕":case "攻击时给予状态拘束":
            case "攻击时给予状态雾":case "攻击时给予状态黑暗":case "攻击时给予状态幻惑":
            case "攻击时给予状态技能封印":case "攻击时给予状态Magia封印":
            case "攻击时给予状态HP回复禁止":case "攻击时给予状态MP回复禁止":
                return 1;
            case"攻击力DOWN": case"防御力DOWN":
            case"造成伤害DOWN": case "AcceleMPDOWN":
            case "异常状态耐性DOWN":case "MP获得量DOWN":case "Blast伤害DOWN":case "Magia伤害DOWN":
            case "MP伤害":case "幻惑":case "诅咒":case "毒": case "技能封印":case "魅惑":case "给予状态眩晕":
            case "拘束":case "雾": case "黑暗":case "烧伤": case "Magia封印":case "BUFF解除":case "MP回复禁止":case "HP回复禁止":
                return -1;
            default:
                return 0;
        }
    }

    public boolean isTriggerEffect(ArrayList<Effect> efList, String effectName){
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            if(e.name.equals(effectName)){
                //Log.d("Sam","colorToss:"+e.name+", 概率:"+e.probability);
                if(colorToss(e.probability)){
                    return true;
                }
            }
        }
        return false;
    }

    public String isTriggerEffect(ArrayList<Effect> efList, String[] effectNameList){
        //只要有一个触发就结束判断，返回触发了哪一个
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            for(int j = 0; j < effectNameList.length; j++){
                if(e.name.equals(effectNameList[j])){
                    if(colorToss(e.probability)){
                        return effectNameList[j];
                    }
                }
            }
        }
        return "";
    }

    public boolean hasAbnormalState(ArrayList<Effect> efList, String[] abnormalList){
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            for(int j = 0; j < abnormalList.length; j++){
                if(e.name.equals(abnormalList[j])){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAbnormalState(ArrayList<Effect> efList){
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            if(e.name.equals("毒") || e.name.equals("烧伤") || e.name.equals("诅咒")){
                return true;
            }else if(e.name.equals("魅惑") || e.name.equals("眩晕") || e.name.equals("拘束")){
                return true;
            }else if(e.name.equals("雾") || e.name.equals("黑暗") || e.name.equals("幻惑")){
                return true;
            }else if(e.name.equals("技能封印") || e.name.equals("Magia封印")){
                return true;
            }else if(e.name.equals("MP回复禁止") || e.name.equals("HP回复禁止")){
                return true;
            }
        }
        return false;
    }

    public int calculateAbnormalStateResistance(ArrayList<Effect> efList){
        int resistance = 0;
        for(int i = 0; i < efList.size(); i++){
            Effect e = efList.get(i);
            if(e.name.equals("异常状态耐性UP")){
                resistance += e.value;
            }else if(e.name.equals("异常状态耐性DOWN")){
                resistance -= e.value;
            }
        }
        return resistance;
    }

    public void win(){
        isWin = 1;
        recoverCharacterSize(true);
        recoverCharacterSize(false);
        magnifyCharacter(true);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(rightCharList[i][j] != null && rightCharList[i][j].c.realHP > 0){
                    rightCharList[i][j].spriteName = "reaction";
                    changeSprite(i,j,true);
                }
            }
        }
        Toast.makeText(this,"胜利",Toast.LENGTH_LONG).show();
    }

    public void lose(){
        isWin = -1;
        Toast.makeText(this,"失败",Toast.LENGTH_LONG).show();
    }

    public int isRestrained(Character attacker, Character defender){
        switch(attacker.element){
            case "tree":
                return defender.element.equals("fire") ? -1:(defender.element.equals("water")? 1:0);
            case "water":
                return defender.element.equals("tree") ? -1:(defender.element.equals("fire")? 1:0);
            case "fire":
                return defender.element.equals("water") ? -1:(defender.element.equals("tree")? 1:0);
            case "light":
                return defender.element.equals("dark") ? 1:0;
            case "dark":
                return defender.element.equals("light") ? 1:0;
            default:
        }
        return 0;
    }

    public int getImageByString(String name){
        Resources res = getResources();
        return res.getIdentifier(name,"drawable",getPackageName());
    }

    public boolean judgeFloatEqual(float a, float b){
        return Math.abs(a-b) < 0.001f;
    }

    public boolean colorToss(int probabilityOfTrue){
        if(probabilityOfTrue >= 100){
            return true;
        }else if(probabilityOfTrue <= 0){
            return false;
        }
        return (Math.random()*100 < probabilityOfTrue);
    }
}

class Plate{
    Character c;
    int plate; // 说明是ABC哪种盘
    int chooseCharacterX;
    int chooseCharacterY;
}

class Effect{
    String name;
    int value;
    int time;
    int valueTime = 0; // 仅对"攻击时给予状态异常效果"有效, 为异常效果持续的时间
    int probability = 100;
    Effect(){}
    Effect(String name, int value, int time, int probability, int valueTime){
        this.name = name;
        this.value = value;
        this.time = time;
        this.probability = probability;
        this.valueTime = valueTime;
    }

    public String getDescription(){
        String allEffect = name;
        if(valueTime != 0){
            allEffect += "(" + valueTime + "T)";
        }
        if(probability == 100){
            allEffect += "[" + value + "%]";
        }else{
            allEffect += "(" + value + "%)[发动率: "+ probability + "%]";
        }
        return allEffect;
    }

    public String getTime(){
        if(time == 999){
            return "∞";
        }
        return ""+time;
    }
}