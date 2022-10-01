package com.live2d.rougelike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.app.usage.ConfigurationStats;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BonusActivity extends AppCompatActivity{

    //第一次选择
    ConstraintLayout first_frame;
    ConstraintLayout bonus_title;
    LinearLayout first_choice_frame;
    ConstraintLayout[] bonus_choice_layout = new ConstraintLayout[3];
    TextView[] bonus_choice_text = new TextView[3];
    TextView[] bonus_choice_subtext = new TextView[3];


    ConstraintLayout memoria_choose_frame;
    CardView showingMemoria;
    TextView showingMemoriaName;
    TextView HPShowing;
    TextView ATKShowing;
    TextView DEFShowing;
    LinearLayout break_throughLinearLayout;
    TextView coolTimeView;
    TextView skillName;
    ImageView skillIcon;
    TextView skillDescription;
    ConstraintLayout purchase_button;
    ImageView purchase_button_background;
    TextView purchase_button_text;
    TextView memoria_price;
    TextView cardNumber;
    LinearLayout star_4_memoria_list;
    LinearLayout star_3_memoria_list;
    LinearLayout star_2_memoria_list;

    ImageView memoria_cc_icon;
    StrokeTextView touch_screen;
    TextView cc_number;
    TextView grief_seed_number;

    CardView chooseCardView = null;

    ColorMatrixColorFilter grayColorFilter;//用于灰度设置

    boolean isIntentSend = false;

    boolean isOpenMemoriaChooseFrame = false;

    boolean isInAnimation = false;


    Global global;

    ArrayList<Pair<String,String>> bonusList = new ArrayList<>();

    Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(@NonNull Message message){
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        global = (Global)getApplicationContext();
        findView();
        initBonus();
        initView();
    }

    public void findView(){
        first_frame = findViewById(R.id.first_frame);
        bonus_title = findViewById(R.id.bonus_title);
        first_choice_frame = findViewById(R.id.first_choice_frame);
        for(int i = 0; i < 3; i++){
            bonus_choice_layout[i] = findViewById(getIdByString("bonus_choice" + i + "_layout"));
            bonus_choice_text[i] = findViewById(getIdByString("bonus_choice" + i + "_text"));
            bonus_choice_subtext[i] = findViewById(getIdByString("bonus_choice" + i + "_subtext"));
        }
        memoria_choose_frame = findViewById(R.id.memoria_choose_frame);
        showingMemoria = findViewById(R.id.showingMemoria);
        showingMemoriaName = findViewById(R.id.showingMemoriaName);
        HPShowing = findViewById(R.id.HPShowing);
        ATKShowing = findViewById(R.id.ATKShowing);
        DEFShowing = findViewById(R.id.DEFShowing);
        break_throughLinearLayout = findViewById(R.id.break_throughLinearLayout);
        coolTimeView = findViewById(R.id.coolTimeView);
        skillName = findViewById(R.id.skillName);
        skillIcon = findViewById(R.id.skillIcon);
        skillDescription = findViewById(R.id.skillDescription);
        purchase_button = findViewById(R.id.purchase_button);
        purchase_button_background = findViewById(R.id.purchase_button_background);
        purchase_button_text = findViewById(R.id.purchase_button_text);
        memoria_price = findViewById(R.id.memoria_price);
        cardNumber = findViewById(R.id.cardNumber);
        star_4_memoria_list = findViewById(R.id.star_4_memoria_list);
        star_3_memoria_list = findViewById(R.id.star_3_memoria_list);
        star_2_memoria_list = findViewById(R.id.star_2_memoria_list);
        memoria_cc_icon = findViewById(R.id.memoria_cc_icon);
        touch_screen = findViewById(R.id.touch_screen);
        cc_number = findViewById(R.id.cc_number);
        grief_seed_number = findViewById(R.id.grief_seed_number);
    }

    public void initBonus(){
        bonusList.add(new Pair<>("似乎是一小袋CC?", "获得3000CC"));
        bonusList.add(new Pair<>("也许是一小袋悲叹之种?", "获得3个悲叹之种"));
        //bonusList.add(new Pair<>("好像是一大包东西，但是不知为何打了个很紧的死结...", "获得3000CC与3悲叹之种，所有角色损失50%生命"));
        bonusList.add(new Pair<>("可能是件特殊的礼物?", "所有角色获得50MP"));
    }

    public void initView(){
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        grayColorFilter = new ColorMatrixColorFilter(cm);
        updateCCAndGriefSeedView();

        updateCCAndGriefSeedView();
        //第一次选择
        first_choice_frame.setVisibility(View.VISIBLE);
        memoria_choose_frame.setVisibility(View.GONE);
        for(int i = 0; i < 3; i++){
            //从BonusList里取出一个，之后直接消去该对象
            final Pair<String, String> tempBonus = bonusList.get((int)(bonusList.size() * Math.random()));
            bonus_choice_text[i].setText(tempBonus.first);
            bonus_choice_subtext[i].setText(tempBonus.second);
            bonus_choice_layout[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(tempBonus.second.equals("获得3000CC")){
                        global.ccNumber += 3000;
                        updateCCAndGriefSeedView();
                    }else if(tempBonus.second.equals("获得3个悲叹之种")){
                        global.griefSeedNumber += 3;
                        updateCCAndGriefSeedView();
                    }else if(tempBonus.second.equals("获得3000CC与3悲叹之种，所有角色损失50%生命")){
                        global.ccNumber += 3000;
                        global.griefSeedNumber += 3;
                        updateCCAndGriefSeedView();
                        for(int i = 0; i < global.characterList.size(); i++){
                            Character tempC = global.characterList.get(i);
                            tempC.realHP = (int)(0.5f * tempC.getRealMaxHP());
                        }
                    }else if(tempBonus.second.equals("所有角色获得50MP")){
                        for(int i = 0; i < global.characterList.size(); i++){
                            Character tempC = global.characterList.get(i);
                            tempC.realMP = 500;
                        }
                    }

                    //进入记忆选择界面
                    if(!isOpenMemoriaChooseFrame){
                        Animation mainAnimation = new AlphaAnimation(1f,0f);
                        mainAnimation.setDuration(1000);
                        mainAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                memoria_choose_frame.setVisibility(View.VISIBLE);
                                updateCCAndGriefSeedView();
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                first_frame.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        first_frame.startAnimation(mainAnimation);

                        Animation memoriaChooseAnimation = new AlphaAnimation(0f,1f);
                        memoriaChooseAnimation.setDuration(1000);
                        memoria_choose_frame.startAnimation(memoriaChooseAnimation);

                        setMemoriaFrame(1);
                    }
                }
            });
            bonusList.remove(tempBonus);
        }
    }

    public void setMemoriaFrame(final int time){
        cardNumber.setText("请选择初始记忆结晶(" + time + "/3): ");
        ArrayList<Integer> loadedMemoriaList = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 3; j++){
                //随机记忆
                int randomId = (int)(Math.random()*global.USEDMEMORIA[j].size());
                while(loadedMemoriaList.contains(randomId)){
                    randomId = (int)(Math.random()*global.USEDMEMORIA[j].size());
                }
                loadedMemoriaList.add(randomId);
                Memoria m = new Memoria(""+global.USEDMEMORIA[j].get(randomId),BonusActivity.this);
                final CardView cd = new CardView(BonusActivity.this);
                cd.setMemoria(m.id);
                cd.setLv(m.lvNow,m.lvMax);
                cd.setChoose(false);
                if(j == 0){
                    star_2_memoria_list.addView(cd);
                }else if(j == 1){
                    star_3_memoria_list.addView(cd);
                }else if(j == 2){
                    star_4_memoria_list.addView(cd);
                }
                cd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(chooseCardView == cd){
                            chooseCardView.setChoose(false);
                            chooseCardView = null;
                            updateChooseMemoria(time);
                        }else{
                            if(chooseCardView != null){
                                chooseCardView.setChoose(false);
                            }
                            chooseCardView = cd;
                            chooseCardView.setChoose(true);
                            updateChooseMemoria(time);
                        }
                    }
                });
            }
        }
        updateChooseMemoria(time);
        isOpenMemoriaChooseFrame = true;
    }

    public void updateChooseMemoria(final int time){
        if(chooseCardView == null){
            purchase_button_text.setText("放弃" + (time == 3? "并出发":""));
            memoria_price.setVisibility(View.GONE);
            memoria_cc_icon.setVisibility(View.GONE);
            showingMemoria.setUnusable();
            showingMemoria.setUnableTextView("");
            showingMemoriaName.setText("");
            HPShowing.setText("");
            ATKShowing.setText("");
            DEFShowing.setText("");
            break_throughLinearLayout.setVisibility(View.INVISIBLE);
            coolTimeView.setText("");
            skillName.setText("");
            skillIcon.setVisibility(View.INVISIBLE);
            skillDescription.setText("");
            purchase_button_background.setColorFilter(null);
            purchase_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isInAnimation){
                        if(time == 3){
                            jumpToNextActivity();
                        }else{
                            chooseCardView = null;
                            star_2_memoria_list.removeAllViews();
                            star_3_memoria_list.removeAllViews();
                            star_4_memoria_list.removeAllViews();
                            isInAnimation = true;
                            handler.postDelayed(new Runnable(){
                                @Override
                                public void run(){
                                    setMemoriaFrame(time+1);
                                    isInAnimation = false;
                                }
                            }, 700);
                        }
                    }
                }
            });
        }else{
            final Memoria m = new Memoria(chooseCardView.memoriaId,BonusActivity.this);
            memoria_price.setVisibility(View.VISIBLE);
            int tempPrice = global.MEMORIA_PURCHASE_PRICE[m.star-2];
            if(global.collectionDict.get("5万日元商品券").isOwn){
                if(m.star == 3){
                    tempPrice -= 1500;
                }
            }
            if(global.collectionDict.get("东西协议").isOwn){
                if(m.star == 4){
                    tempPrice -= 2000;
                }
            }
            final int price = tempPrice;
            memoria_price.setText(""+price);
            memoria_cc_icon.setVisibility(View.VISIBLE);
            showingMemoria.setMemoria(chooseCardView.memoriaId);
            showingMemoria.setLv(m.lvNow,m.lvMax);
            showingMemoriaName.setText(m.name);
            HPShowing.setText(""+m.HPOrigin);
            ATKShowing.setText(""+m.ATKOrigin);
            DEFShowing.setText(""+m.DEFOrigin);
            break_throughLinearLayout.setVisibility(View.INVISIBLE);
            coolTimeView.setText(m.isSkill() ? "冷却时间 "+m.CDOrigin:"");
            skillName.setText("");
            skillIcon.setVisibility(View.VISIBLE);
            skillIcon.setImageResource(getResourceByString(m.icon));
            skillDescription.setText(m.getEffectDescription());
            if(global.ccNumber >= price){
                purchase_button_text.setText("购买");
                purchase_button_background.setColorFilter(null);
                purchase_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isInAnimation){
                            Toast.makeText(BonusActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
                            global.memoriaBag.add(new Memoria(chooseCardView.memoriaId,BonusActivity.this));
                            global.ccNumber -= price;
                            updateCCAndGriefSeedView();
                            if(time == 3){
                                jumpToNextActivity();
                            }else{
                                chooseCardView = null;
                                star_2_memoria_list.removeAllViews();
                                star_3_memoria_list.removeAllViews();
                                star_4_memoria_list.removeAllViews();
                                isInAnimation = true;
                                handler.postDelayed(new Runnable(){
                                    @Override
                                    public void run(){
                                        isInAnimation = false;
                                        setMemoriaFrame(time+1);
                                    }
                                }, 700);
                            }
                        }
                    }
                });
            }else{
                purchase_button_background.setColorFilter(grayColorFilter);
                purchase_button_text.setText("CC不足");
                purchase_button.setOnClickListener(null);
            }
        }
    }

    public void jumpToNextActivity(){
        if(!isIntentSend){
            global.cancelBGM();
            Intent intent1 = null;
            intent1 = new Intent(BonusActivity.this, MapActivity.class);
            startActivity(intent1);
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            isIntentSend = true;
        }
    }

    @Override
    public void onBackPressed(){

    }

    void updateCCAndGriefSeedView(){
        cc_number.setText(" " + global.ccNumber + " ");
        grief_seed_number.setText(" " + global.griefSeedNumber + " ");
    }

    public int getResourceByString(String idName){
        return getResources().getIdentifier(idName,"drawable", getPackageName());
    }

    public int getIdByString(String idName){
        return getResources().getIdentifier(idName,"id", getPackageName());
    }
}