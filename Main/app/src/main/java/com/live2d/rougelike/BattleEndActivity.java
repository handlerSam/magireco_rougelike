package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class BattleEndActivity extends AppCompatActivity {

    ImageView underlay;
    ImageView backgroundLeft;
    ImageView backgroundRight;
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
    ConstraintLayout main_frame;
    ConstraintLayout extra_mission_frame;
    ImageView extra_mission_background;
    TextView extra_mission_text;
    TextView extra_mission_add_number;
    ImageView extra_mission_cc_icon;
    ImageView extra_mission_grief_seed_icon;
    TextView add_cc_number;
    TextView add_grief_seed_number;
    LinearLayout character_list;
    LinearLayout item_list;
    ImageView memoria_cc_icon;
    StrokeTextView touch_screen;
    TextView cc_number;
    TextView grief_seed_number;

    CardView chooseCardView = null;

    ColorMatrixColorFilter grayColorFilter;//用于灰度设置

    boolean isIntentSend = false;

    boolean isOpenMemoriaChooseFrame = false;

    boolean isRandomBattle;

    BattleInfo bi;

    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_end);
        global = (Global)getApplicationContext();
        findView();
        initView();
    }

    public void findView(){
        underlay = findViewById(R.id.underlay);
        backgroundLeft = findViewById(R.id.backgroundLeft);
        backgroundRight = findViewById(R.id.backgroundRight);
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
        main_frame = findViewById(R.id.main_frame);
        extra_mission_frame = findViewById(R.id.extra_mission_frame);
        extra_mission_background = findViewById(R.id.extra_mission_background);
        extra_mission_text = findViewById(R.id.extra_mission_text);
        extra_mission_add_number = findViewById(R.id.extra_mission_add_number);
        extra_mission_cc_icon = findViewById(R.id.extra_mission_cc_icon);
        extra_mission_grief_seed_icon = findViewById(R.id.extra_mission_grief_seed_icon);
        add_cc_number = findViewById(R.id.add_cc_number);
        add_grief_seed_number = findViewById(R.id.add_grief_seed_number);
        character_list = findViewById(R.id.character_list);
        item_list = findViewById(R.id.item_list);
        memoria_cc_icon = findViewById(R.id.memoria_cc_icon);
        touch_screen = findViewById(R.id.touch_screen);
        cc_number = findViewById(R.id.cc_number);
        grief_seed_number = findViewById(R.id.grief_seed_number);
    }

    public void initView(){

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        grayColorFilter = new ColorMatrixColorFilter(cm);
        updateCCAndGriefSeedView();

        //设置背景
        isRandomBattle = getIntent().getBooleanExtra("isRandomBattle",false);
        if(isRandomBattle){
            bi = global.mpEvent.get(getIntent().getIntExtra("battleInfo",-1)).bi;
        }else{
            bi = global.battleInfoList.get(getIntent().getIntExtra("battleInfo",-1));
        }
        if(bi.backgroundType == BattleInfo.JUNCTION){
            underlay.setImageResource(global.JUNCTION_BACKGROUND_IMAGE_LIST.get(bi.backgroundId).upImageId);
            backgroundLeft.setImageResource(global.JUNCTION_BACKGROUND_IMAGE_LIST.get(bi.backgroundId).downImageId);
            backgroundRight.setImageResource(global.JUNCTION_BACKGROUND_IMAGE_LIST.get(bi.backgroundId).downImageId);
        }else if(bi.backgroundType == BattleInfo.DAY){
            underlay.setImageResource(global.DAY_BACKGROUND_IMAGE_LIST.get(bi.backgroundId).upImageId);
            backgroundLeft.setImageResource(global.DAY_BACKGROUND_IMAGE_LIST.get(bi.backgroundId).downImageId);
            backgroundRight.setImageResource(global.DAY_BACKGROUND_IMAGE_LIST.get(bi.backgroundId).downImageId);
        }

        //额外任务
        Intent receivedIntent = getIntent();
        ExtraMission em = global.extraMissionList.get(receivedIntent.getIntExtra("extraMissionId",0));
        boolean isExtraMissionAchieve = receivedIntent.getBooleanExtra("achieveExtraMission", false);
        extra_mission_text.setText(em.name);
        if(em.bonus.cc > 0){
            extra_mission_add_number.setText("+"+em.bonus.cc);
            extra_mission_cc_icon.setVisibility(View.VISIBLE);
            extra_mission_grief_seed_icon.setVisibility(View.GONE);
            if(isExtraMissionAchieve){
                global.ccNumber += em.bonus.cc;
            }
        }else{
            extra_mission_add_number.setText("+"+em.bonus.griefSeed);
            extra_mission_cc_icon.setVisibility(View.GONE);
            extra_mission_grief_seed_icon.setVisibility(View.VISIBLE);
            if(isExtraMissionAchieve){
                global.griefSeedNumber += em.bonus.griefSeed;
            }
        }
        if(!isExtraMissionAchieve){
            //额外任务没成功，变成灰色
            extra_mission_background.setColorFilter(grayColorFilter);
            extra_mission_cc_icon.setColorFilter(grayColorFilter);
            extra_mission_grief_seed_icon.setColorFilter(grayColorFilter);
        }else{
            extra_mission_background.setColorFilter(null);
            extra_mission_cc_icon.setColorFilter(null);
            extra_mission_grief_seed_icon.setColorFilter(null);
        }

        //通关报酬设置
        Bonus bb = getBattleBonus();
        int getGriefSeed = bb.griefSeed;
        int getCC = bb.cc;
        if(global.collectionDict.get("\"死神的黑卡\"").isOwn){
            if(colorToss(50)){
                getGriefSeed += 1;
            }
        }
        if(global.collectionDict.get("特制的年糕汤").isOwn){
            if(colorToss(50)){
                getCC += 1000;
            }
        }
        add_cc_number.setText("+"+getCC);
        add_grief_seed_number.setText("+"+getGriefSeed);

        global.ccNumber += getCC;
        global.griefSeedNumber += getGriefSeed;

        //人物升级
        for(int i = 0; i < global.characters.length; i++){
            if(global.characters[i] != null){
                Character c = global.characters[i];
                View view = LayoutInflater.from(BattleEndActivity.this).inflate(R.layout.battle_end_character_item, character_list,false);
                ImageView charImage = view.findViewById(R.id.charImage);
                ConstraintLayout charFrame = view.findViewById(R.id.charFrame);
                ImageView charAttr = view.findViewById(R.id.charAttr);
                LinearLayout starsLayout = view.findViewById(R.id.starsLayout);
                ImageView[] char_star = new ImageView[]{view.findViewById(R.id.char_star0),view.findViewById(R.id.char_star1),
                        view.findViewById(R.id.char_star2),view.findViewById(R.id.char_star3),view.findViewById(R.id.char_star4)};
                TextView lv_origin = view.findViewById(R.id.lv_origin);
                ImageView right_arrow = view.findViewById(R.id.right_arrow);
                TextView lv_after = view.findViewById(R.id.lv_after);

                charImage.setImageResource(getResourceByString(c.charIconImage+"d"));
                charImage.setBackgroundResource(getResourceByString("bg_"+c.element));
                charFrame.setBackgroundResource(getResourceByString("frame_rank_"+c.star));
                charAttr.setImageResource(getResourceByString(c.element));
                starsLayout.setVisibility(View.VISIBLE);
                for(int j = 0; j < 5; j++){
                    char_star[j].setVisibility(j < c.star? View.VISIBLE:View.GONE);
                }
                lv_origin.setText(""+c.lv);

                int upgradeLV = 10;
                try {
                    if(c.lv >= Integer.parseInt(bi.recommendLV) + 20){
                        upgradeLV = 0;
                    }else if(c.lv >= Integer.parseInt(bi.recommendLV) + 10){
                        upgradeLV = 5;
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(c.lv == 1){
                    upgradeLV--;
                    lv_after.setText(""+(c.lv+upgradeLV));
                    c.lv += upgradeLV;
                }else if(c.lv >= c.getMaxLv() || upgradeLV == 0){
                    right_arrow.setVisibility(View.INVISIBLE);
                    lv_after.setVisibility(View.INVISIBLE);
                }else{
                    if(c.lv + upgradeLV > c.getMaxLv()){
                        upgradeLV = c.getMaxLv() - c.lv;
                    }
                    lv_after.setText(""+(c.lv+upgradeLV));
                    c.lv += upgradeLV;
                }

                c.updateAttributionBasedOnLv();
                character_list.addView(view);
            }
        }


        //是否收到收藏品
        if(isReceivedCollection()){
            int collectionId = -1;
            Collections.shuffle(global.collectionList);
            for(int i = 0; i < global.collectionList.size(); i++){
                if(!global.collectionList.get(i).isOwn){
                    collectionId = i;
                    break;
                }
            }
            if(collectionId != -1){
                global.collectionList.get(collectionId).isOwn = true;

                Collection c = global.collectionList.get(collectionId);
                View view = LayoutInflater.from(BattleEndActivity.this).inflate(R.layout.battle_end_item_item, item_list,false);
                LinearLayout item_background = view.findViewById(R.id.item_background);
                ImageView item_image = view.findViewById(R.id.item_image);
                TextView item_name = view.findViewById(R.id.item_name);
                TextView item_effect = view.findViewById(R.id.item_effect);

                item_background.setBackgroundResource(getResourceByString(c.background));
                item_image.setImageResource(getResourceByString(c.icon));
                item_name.setText(c.name);
                item_effect.setText(c.effectDescription);
                item_list.addView(view);
            }
        }

        main_frame.setVisibility(View.VISIBLE);
        memoria_choose_frame.setVisibility(View.GONE);
        //点击屏幕切换到记忆选择界面
        main_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            main_frame.setVisibility(View.GONE);
                            touch_screen.getAnimation().setAnimationListener(null);
                            touch_screen.clearAnimation();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    main_frame.startAnimation(mainAnimation);

                    Animation memoriaChooseAnimation = new AlphaAnimation(0f,1f);
                    memoriaChooseAnimation.setDuration(1000);
                    memoria_choose_frame.startAnimation(memoriaChooseAnimation);

                    //boolean isChooseSkillMemoria = Math.random() > 0.5f;
                    cardNumber.setText("请选择一张记忆结晶: ");
                    for(int i = 0; i < 6; i++){
                        for(int j = 0; j < 3; j++){
                            //随机记忆
                            int randomId = (int)(Math.random()*global.USEDMEMORIA[j].size());
                            Memoria m = new Memoria(""+global.USEDMEMORIA[j].get(randomId),BattleEndActivity.this);
                            final CardView cd = new CardView(BattleEndActivity.this);
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
                                        updateChooseMemoria();
                                    }else{
                                        if(chooseCardView != null){
                                            chooseCardView.setChoose(false);
                                        }
                                        chooseCardView = cd;
                                        chooseCardView.setChoose(true);
                                        updateChooseMemoria();
                                    }
                                }
                            });
                        }
                    }
                    updateChooseMemoria();
                    isOpenMemoriaChooseFrame = true;
                }

            }
        });

        // touch screen 动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.touch_screen);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                touch_screen.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        touch_screen.startAnimation(animation);
    }

    public void updateChooseMemoria(){
        if(chooseCardView == null){
            purchase_button_text.setText("离开");
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
                    jumpToNextActivity();
                }
            });
        }else{
            final Memoria m = new Memoria(chooseCardView.memoriaId,BattleEndActivity.this);
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
            skillIcon.setImageResource(getResourceByString(m.icon));
            skillDescription.setText(m.getEffectDescription());
            if(global.ccNumber >= price){
                purchase_button_text.setText("购买并离开");
                purchase_button_background.setColorFilter(null);
                purchase_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BattleEndActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
                        global.memoriaBag.add(new Memoria(chooseCardView.memoriaId,BattleEndActivity.this));
                        global.ccNumber -= price;
                        jumpToNextActivity();
                    }
                });
            }else{
                purchase_button_background.setColorFilter(grayColorFilter);
                purchase_button_text.setText("CC不足");
                purchase_button.setOnClickListener(null);
            }
        }
    }

    public Bonus getBattleBonus(){
        if(!bi.isBossBattle){
            if(global.gameTime < 14.01f){
                //说明是游戏前期, 1-2悲叹之种，2000-3000CC
                int randomGF = (int)(Math.random()*2)+0;
                int randomCC = ((int)(Math.random()*3))*500+2000;
                return new Bonus(randomCC,randomGF);
            }else{
                //说明是游戏后期, 1-2悲叹之种，3000-4000CC
                int randomGF = (int)(Math.random()*2)+1;
                int randomCC = ((int)(Math.random()*3))*500+3000;
                return new Bonus(randomCC,randomGF);
            }
        }else{
            if(global.gameTime < 14.01f){
                //说明是游戏前期, 2-3悲叹之种，2000-4000CC
                int randomGF = (int)(Math.random()*2)+1;
                int randomCC = ((int)(Math.random()*5))*500+2000;
                return new Bonus(randomCC,randomGF);
            }else{
                //说明是游戏后期, 2-3悲叹之种，3000-5000CC
                int randomGF = (int)(Math.random()*2)+2;
                int randomCC = ((int)(Math.random()*5))*500+3000;
                return new Bonus(randomCC,randomGF);
            }
        }
    }

    public boolean isReceivedCollection(){
        int probability = 0;
        if(!bi.isBossBattle){
            if(global.gameTime < 14.01f){
                //说明是游戏前期, 普通战斗
                probability = 0;
            }else{
                //说明是游戏后期, 普通战斗
                probability = 20;
            }
        }else{
            if(global.gameTime < 14.01f){
                //说明是游戏前期, 魔女战斗
                probability = 30;
            }else{
                //说明是游戏后期, 魔女战斗
                probability = 50;
            }
        }
        return colorToss(probability);
    }

    void updateCCAndGriefSeedView(){
        cc_number.setText(" "+global.ccNumber+" ");
        grief_seed_number.setText(" "+global.griefSeedNumber+" ");
    }

    public void jumpToNextActivity(){
        if(!isIntentSend){
            Intent intent1 = null;
            if(!isRandomBattle){
                if(getIntent().getIntExtra("battleInfo",-1) == 0){
                    //robbery battle
                    intent1 = new Intent(BattleEndActivity.this, DialogActivity.class);
                    intent1.putExtra("storyResourceId",R.raw.robbery_after);
                }else if(getIntent().getIntExtra("battleInfo",-1) == 1){
                    //八千代相遇后第一次战斗
                    intent1 = new Intent(BattleEndActivity.this, DialogActivity.class);
                    intent1.putExtra("storyResourceId",R.raw.first_meet_yachiyo_after_battle);
                }
            }else{
                //随机战斗
                //若使用了Doppel，则触发发现doppel事件
                String useDoppelCharName = getIntent().getStringExtra("useDoppelCharName");
                if(useDoppelCharName != null && !useDoppelCharName.equals("")){
                    intent1 = new Intent(BattleEndActivity.this, DialogActivity.class);
                    if(useDoppelCharName.equals("Hiiragi Nemu")){
                        intent1.putExtra("storyResourceId", R.raw.after_first_doppel_remu);
                    }else if(useDoppelCharName.equals("Satomi Touka")){
                        intent1.putExtra("storyResourceId", R.raw.after_first_doppel_toca);
                    }else if(useDoppelCharName.equals("Tamaki Ui")){
                        intent1.putExtra("storyResourceId", R.raw.after_first_doppel_ui);
                    }
                }else{
                    global.mpEvent.clear();
                    global.gameTime += 0.5f;
                    intent1 = new Intent(BattleEndActivity.this, MapActivity.class);
                }
            }
            startActivity(intent1);
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            isIntentSend = true;
        }
    }

    public boolean colorToss(int probabilityOfTrue){
        if(probabilityOfTrue >= 100){
            return true;
        }else if(probabilityOfTrue <= 0){
            return false;
        }
        return (Math.random()*100 < probabilityOfTrue);
    }

    public int getResourceByString(String idName){
        return getResources().getIdentifier(idName,"drawable", getPackageName());
    }


}