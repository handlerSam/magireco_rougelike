package com.live2d.rougelike;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;

import static com.live2d.rougelike.CharacterPlateView.ACCELE;
import static com.live2d.rougelike.CharacterPlateView.BLAST_HORIZONTAL;
import static com.live2d.rougelike.CharacterPlateView.BLAST_VERTICAL;
import static com.live2d.rougelike.CharacterPlateView.CHARGE;

public class StartActivity extends AppCompatActivity{


    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        global = (Global)getApplicationContext();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        global.SCREEN_HEIGHT = Math.min(metric.widthPixels, metric.heightPixels);  // 屏幕宽度（像素）
        global.SCREEN_WIDTH = Math.max(metric.widthPixels, metric.heightPixels);  // 屏幕高度（像素）
        JniBridgeJava.setScreenSize(global.SCREEN_WIDTH, global.SCREEN_HEIGHT);
        Log.d("sam", "screenWidth:" + global.SCREEN_WIDTH + ", Height:" + global.SCREEN_HEIGHT);

        initRandomEvent();

        initMemoria();
        initCollection();
        initMapRandomPoint();
        initCharacterList();
        initBackground();
        initBattleInfoList();

        initFormation();

        initExtraMissionList();

        initRandomBuff();

        initSoundPool();

        //Intent intent1 = new Intent(StartActivity.this, TeamChooseActivity.class);
        //intent1.putExtra("battleInfo",0);
        //startActivity(intent1);
        //finish();
        //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        Intent intent1 = new Intent(StartActivity.this, DialogActivity.class);
        intent1.putExtra("storyResourceId",R.raw.story1);
        startActivity(intent1);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void initRandomEvent(){
        global.randomEventList.add(R.raw.promotion_of_bangbangzai);
        global.randomEventList.add(R.raw.robbery_before);
    }

    public void initCharacterList(){
        Character remu = new Character();
        remu.breakThrough = 1;
        remu.element = "tree";
        remu.name = "柊音梦";
        remu.choosingActivityImage = "team_choose_101400_1";
        remu.spriteName = "Hiiragi Nemu";
        remu.charIconImage = "card_10144_";
        remu.magiaSkillIconName = "icon_skill_1012";
        remu.doppelImageName = "mini_101400_dd";
        remu.miniImage = "mini_remu";
        remu.isLeader = false;
        remu.lv = 1;
        remu.star = 4;
        remu.realMP = 0;
        remu.plateList = new int[]{ACCELE, ACCELE, ACCELE, BLAST_VERTICAL, CHARGE};
        remu.mpAttackRatio = 1.2f;
        remu.mpDefendRatio = 1.0f;
        remu.characterId = "1014";

        remu.fourStarMinHP = 6300;
        remu.fourStarMaxHP = 23007;
        remu.fourStarMinATK = 1761;
        remu.fourStarMaxATK = 6385;
        remu.fourStarMinDEF = 1715;
        remu.fourStarMaxDEF = 6129;
        remu.fiveStarMinHP = 7147;
        remu.fiveStarMaxHP = 29016;
        remu.fiveStarMinATK = 1999;
        remu.fiveStarMaxATK = 8055;
        remu.fiveStarMinDEF = 1946;
        remu.fiveStarMaxDEF = 7725;

        remu.connectOriginEffectList.add(new SkillEffect("攻击力UP", 35, "自", 1, 100));
        remu.connectOriginEffectList.add(new SkillEffect("HP回复", 37, "自", 1, 100));
        remu.connectOriginEffectList.add(new SkillEffect("攻击时给予状态幻惑", 65, "自", 1, 50, 1));

        remu.connectAfterEffectList.add(new SkillEffect("攻击力UP", 40, "自", 1, 100));
        remu.connectAfterEffectList.add(new SkillEffect("HP回复", 42, "自", 1, 100));
        remu.connectAfterEffectList.add(new SkillEffect("攻击时给予状态幻惑", 50, "自", 1, 50, 1));

        remu.magiaTarget = "敌单";
        remu.magiaOriginMagnification = 1128;
        remu.magiaAfterMagnification = 1192;
        remu.doppelMagnification = 2763;
        remu.magiaOriginEffectList.add(new SkillEffect("给予状态魅惑", 0, "敌单", 1, 100));
        remu.magiaOriginEffectList.add(new SkillEffect("给予状态诅咒", 15, "敌单", 1, 100));
        remu.magiaOriginEffectList.add(new SkillEffect("给予状态黑暗", 35, "敌单", 1, 100));
        remu.magiaOriginEffectList.add(new SkillEffect("攻击力UP", 30, "己全", 3, 100));

        remu.magiaAfterEffectList.add(new SkillEffect("给予状态魅惑", 0, "敌单", 1, 100));
        remu.magiaAfterEffectList.add(new SkillEffect("给予状态诅咒", 15, "敌单", 1, 100));
        remu.magiaAfterEffectList.add(new SkillEffect("给予状态黑暗", 35, "敌单", 1, 100));
        remu.magiaAfterEffectList.add(new SkillEffect("BUFF解除", 0, "敌单", 1, 100));
        remu.magiaAfterEffectList.add(new SkillEffect("攻击力UP", 30, "己全", 3, 100));

        remu.doppelEffectList.add(new SkillEffect("给予状态魅惑", 0, "敌单", 1, 100));
        remu.doppelEffectList.add(new SkillEffect("给予状态诅咒", 15, "敌单", 1, 100));
        remu.doppelEffectList.add(new SkillEffect("给予状态黑暗", 35, "敌单", 1, 100));
        remu.doppelEffectList.add(new SkillEffect("BUFF解除", 0, "敌单", 1, 100));
        remu.doppelEffectList.add(new SkillEffect("攻击力UP", 30, "己全", 3, 100));

        remu.updateAttributionBasedOnLv();
        //remu.initialEffectList.add(new Effect("眩晕",0,1,100,0));

        Character toca = new Character();
        toca.breakThrough = 1;
        toca.element = "fire";
        toca.name = "里见灯花";
        toca.choosingActivityImage = "team_choose_100700_2";
        toca.charIconImage = "card_10074_";
        toca.magiaSkillIconName = "icon_skill_1014";
        toca.doppelImageName = "mini_100700_dd";
        toca.miniImage = "mini_toca";
        toca.isLeader = false;
        toca.spriteName = "Satomi Touka";
        toca.lv = 1;
        toca.star = 4;
        toca.realMP = 0;
        toca.plateList = new int[]{ACCELE, ACCELE, BLAST_HORIZONTAL, BLAST_VERTICAL, CHARGE};
        toca.mpAttackRatio = 1.2f;
        toca.mpDefendRatio = 1.2f;
        toca.characterId = "1007";

        toca.fourStarMinHP = 4704;
        toca.fourStarMaxHP = 16689;
        toca.fourStarMinATK = 2109;
        toca.fourStarMaxATK = 7756;
        toca.fourStarMinDEF = 1372;
        toca.fourStarMaxDEF = 4832;
        toca.fiveStarMinHP = 5335;
        toca.fiveStarMaxHP = 21019;
        toca.fiveStarMinATK = 2397;
        toca.fiveStarMaxATK = 9803;
        toca.fiveStarMinDEF = 1553;
        toca.fiveStarMaxDEF = 6072;

        toca.connectOriginEffectList.add(new SkillEffect("攻击力UP", 35, "自", 1, 100));
        toca.connectOriginEffectList.add(new SkillEffect("MP回复", 20, "自", 1, 100));
        toca.connectOriginEffectList.add(new SkillEffect("攻击时给予状态Magia封印", 0, "自", 1, 60, 1));

        toca.connectAfterEffectList.add(new SkillEffect("攻击力UP", 40, "自", 1, 100));
        toca.connectAfterEffectList.add(new SkillEffect("MP回复", 25, "自", 1, 100));
        toca.connectAfterEffectList.add(new SkillEffect("攻击时给予状态Magia封印", 0, "自", 1, 100, 1));

        toca.magiaTarget = "敌全";
        toca.magiaOriginMagnification = 380;
        toca.magiaAfterMagnification = 400;
        toca.doppelMagnification = 902;
        toca.magiaOriginEffectList.add(new SkillEffect("防御力DOWN", 25, "敌全", 3, 100));
        toca.magiaOriginEffectList.add(new SkillEffect("防御力UP", 32, "自", 3, 100));

        toca.magiaAfterEffectList.add(new SkillEffect("防御力DOWN", 27, "敌全", 3, 100));
        toca.magiaAfterEffectList.add(new SkillEffect("防御力UP", 47, "自", 3, 100));
        toca.magiaAfterEffectList.add(new SkillEffect("Magia伤害UP", 35, "自", 3, 100));

        toca.doppelEffectList.add(new SkillEffect("防御力DOWN", 35, "敌全", 3, 100));
        toca.doppelEffectList.add(new SkillEffect("防御力UP", 47, "自", 3, 100));
        toca.doppelEffectList.add(new SkillEffect("Magia伤害UP", 35, "自", 5, 100));

        toca.updateAttributionBasedOnLv();
        // toca.initialEffectList.add(new Effect("眩晕",0,1,100,0));

        Character ui = new Character();
        ui.breakThrough = 1;
        ui.element = "dark";
        ui.name = "环忧";
        ui.choosingActivityImage = "team_choose_101500_2";
        ui.charIconImage = "card_10154_";
        ui.magiaSkillIconName = "icon_skill_1014";
        ui.doppelImageName = "mini_101500_dd";
        ui.miniImage = "mini_ui";
        ui.isLeader = false;
        ui.spriteName = "Tamaki Ui";
        ui.lv = 1;
        ui.star = 4;
        ui.realMP = 0;
        ui.plateList = new int[]{ACCELE, ACCELE, BLAST_HORIZONTAL, CHARGE, CHARGE};
        ui.mpAttackRatio = 0.9f;
        ui.mpDefendRatio = 0.9f;
        ui.characterId = "1015";

        ui.fourStarMinHP = 5973;
        ui.fourStarMaxHP = 21658;
        ui.fourStarMinATK = 1883;
        ui.fourStarMaxATK = 6729;
        ui.fourStarMinDEF = 1930;
        ui.fourStarMaxDEF = 7048;
        ui.fiveStarMinHP = 6775;
        ui.fiveStarMaxHP = 27303;
        ui.fiveStarMinATK = 2136;
        ui.fiveStarMaxATK = 8479;
        ui.fiveStarMinDEF = 2189;
        ui.fiveStarMaxDEF = 8887;

        ui.connectOriginEffectList.add(new SkillEffect("攻击力UP", 35, "自", 1, 100));
        ui.connectOriginEffectList.add(new SkillEffect("伤害削减无效", 100, "自", 1, 100));
        ui.connectOriginEffectList.add(new SkillEffect("攻击时给予状态诅咒", 15, "自", 1, 65, 1));

        ui.connectAfterEffectList.add(new SkillEffect("攻击力UP", 40, "自", 1, 100));
        ui.connectAfterEffectList.add(new SkillEffect("伤害削减无效", 100, "自", 1, 100));
        ui.connectAfterEffectList.add(new SkillEffect("攻击时给予状态诅咒", 15, "自", 1, 100, 1));

        ui.magiaTarget = "敌全";
        ui.magiaOriginMagnification = 390;
        ui.magiaAfterMagnification = 410;
        ui.doppelMagnification = 902;
        ui.magiaOriginEffectList.add(new SkillEffect("MP伤害", 30, "敌全", 1, 100));
        ui.magiaOriginEffectList.add(new SkillEffect("给予状态诅咒", 15, "敌全", 1, 100));

        ui.magiaAfterEffectList.add(new SkillEffect("MP伤害", 35, "敌全", 1, 100));
        ui.magiaAfterEffectList.add(new SkillEffect("给予状态诅咒", 15, "敌全", 1, 100));
        ui.magiaAfterEffectList.add(new SkillEffect("MP回复", 29, "自", 1, 100));

        ui.doppelEffectList.add(new SkillEffect("MP伤害", 60, "敌全", 1, 100));
        ui.doppelEffectList.add(new SkillEffect("给予状态诅咒", 15, "敌全", 1, 100));
        ui.doppelEffectList.add(new SkillEffect("MP回复", 34, "自", 1, 100));

        ui.updateAttributionBasedOnLv();
        //ui.initialEffectList.add(new Effect("眩晕",0,1,100,0));

        global.characterList.add(remu);
        global.characterList.add(toca);
        global.characterList.add(ui);

        global.characterList.get(1).isLeader = true;

//        characterList.get(0).setMemoria(0,memoriaBag.get(0));
//        characterList.get(0).setMemoria(1,memoriaBag.get(1));
//        characterList.get(1).setMemoria(0,memoriaBag.get(2));

//        memoriaBag.get(0).setCarrier(0);
//        memoriaBag.get(1).setCarrier(0);
//        memoriaBag.get(2).setCarrier(1);

        global.characters[1] = global.characterList.get(0);
        global.characters[2] = global.characterList.get(2);
        global.characters[3] = global.characterList.get(1);
    }

    public void initBattleInfoList(){
        //initMonsterList();
        //initBossList();
        initPlotBattle();
    }

    public void initPlotBattle(){
        BattleInfo bi = new BattleInfo();
        bi.isBossBattle = false;

        bi.monsterNumber = 3;
        bi.recommendLV = "??";
        bi.backgroundId = 2;
        bi.backgroundType = BattleInfo.DAY;

        Character felicia = new Character();
        felicia.element = "dark";
        felicia.spriteName = "Mitsuki Felicia";
        felicia.name = "菲利希亚";
        felicia.star = 5;
        felicia.characterId = "1005";
        felicia.fiveStarMinHP = 5194;
        felicia.fiveStarMaxHP = 21087;
        felicia.fiveStarMinATK = (int)(2159*0.5f);
        felicia.fiveStarMaxATK = (int)(8700*0.5f);
        felicia.fiveStarMinDEF = 1645;
        felicia.fiveStarMaxDEF = 6530;
        felicia.lv = 1;
        felicia.realHP = felicia.HP;
        felicia.mpAttackRatio = 0f;
        felicia.mpDefendRatio = 0f;
        felicia.plateList = new int[]{ACCELE, BLAST_VERTICAL, BLAST_HORIZONTAL, CHARGE, CHARGE};
        felicia.initialEffectList.add(new Effect("攻击时给予状态雾", 25, 999, 50, 1));
        felicia.initialEffectList.add(new Effect("攻击力UP", 10, 999, 100, 0));
        felicia.updateAttributionBasedOnLv();
        bi.monsterList.add(felicia);

        Character kako = new Character();
        kako.element = "tree";
        kako.spriteName = "Natsume Kako";
        kako.name = "夏目佳子";
        kako.star = 5;
        kako.characterId = "3011";
        kako.fiveStarMinHP = 5404;
        kako.fiveStarMaxHP = 21291;
        kako.fiveStarMinATK = (int)(1616*0.5f);
        kako.fiveStarMaxATK = (int)(6609*0.5f);
        kako.fiveStarMinDEF = 2033;
        kako.fiveStarMaxDEF = 7949;
        kako.lv = 1;
        kako.mpAttackRatio = 0f;
        kako.mpDefendRatio = 0f;
        kako.plateList = new int[]{ACCELE, ACCELE, BLAST_VERTICAL, BLAST_HORIZONTAL, CHARGE};
        kako.initialEffectList.add(new Effect("HP自动回复", 5, 999, 100, 0));
        kako.initialEffectList.add(new Effect("回避", 0, 999, 20, 0));
        kako.updateAttributionBasedOnLv();
        bi.monsterList.add(kako);

        Character ayame = new Character();
        ayame.element = "fire";
        ayame.spriteName = "Mikuri Ayame";
        ayame.name = "三栗菖蒲";
        ayame.star = 5;
        ayame.characterId = "3028";
        ayame.fiveStarMinHP = 5195;
        ayame.fiveStarMaxHP = 20780;
        ayame.fiveStarMinATK = (int)(2139*0.5f);
        ayame.fiveStarMaxATK = (int)(8556*0.5f);
        ayame.fiveStarMinDEF = 1678;
        ayame.fiveStarMaxDEF = 6712;
        ayame.lv = 1;
        ayame.realHP = kako.HP;
        ayame.mpAttackRatio = 0f;
        ayame.mpDefendRatio = 0f;
        ayame.plateList = new int[]{ACCELE, BLAST_VERTICAL, CHARGE, CHARGE, CHARGE};
        ayame.initialEffectList.add(new Effect("暴击", 0, 999, 10, 1));
        ayame.initialEffectList.add(new Effect("攻击时给予状态诅咒", 15, 999, 40, 1));
        ayame.updateAttributionBasedOnLv();
        bi.monsterList.add(ayame);

        bi.monsterFormation[0][0] = felicia;
        bi.monsterFormation[1][1] = kako;
        bi.monsterFormation[2][2] = ayame;
        global.battleInfoList.add(bi);

        // 与八千代第一次相遇时的战斗
        bi = new BattleInfo();
        bi.isBossBattle = true;

        bi.monsterNumber = 1;
        bi.recommendLV = "??";
        bi.backgroundId = 2;
        bi.backgroundType = BattleInfo.JUNCTION;

        Character monster1 = new Character();
        monster1.element = "fire";
        monster1.spriteName = "monster_沙地的魔女";
        monster1.name = "沙地的魔女";
        monster1.lv = 80;
        monster1.HP = 180000;
        monster1.realHP = monster1.HP;
        monster1.ATK = 12000;
        monster1.DEF = 3000;
        monster1.mpAttackRatio = 0f;
        monster1.mpDefendRatio = 0f;
        monster1.plateList = new int[]{BLAST_HORIZONTAL, BLAST_VERTICAL, BLAST_HORIZONTAL, BLAST_VERTICAL, CHARGE};
        bi.monsterList.add(monster1);

        bi.monsterFormation[1][1] = monster1;
        bi.monsterFormation[1][0] = monster1;
        bi.monsterFormation[1][2] = monster1;
        global.battleInfoList.add(bi);
    }

    public void initBossList(){
        BattleInfo bi = new BattleInfo();
        bi.isBossBattle = true;

        Character monster1 = new Character();
        monster1.element = "dark";
        monster1.spriteName = "monster_无名人工智能之谣";
        monster1.name = "无名人工智能之谣";
        monster1.lv = 80;
        monster1.HP = 180000;
        monster1.realHP = monster1.HP;
        monster1.ATK = 12000;
        monster1.DEF = 3000;
        monster1.mpAttackRatio = 0f;
        monster1.mpDefendRatio = 0f;
        monster1.plateList = new int[]{BLAST_HORIZONTAL, BLAST_VERTICAL, BLAST_HORIZONTAL, BLAST_VERTICAL, CHARGE};
        monster1.initialEffectList.add(new Effect("攻击时给予状态雾", 25, 999, 50, 1));
        monster1.initialEffectList.add(new Effect("攻击时给予状态诅咒", 15, 999, 40, 1));
        monster1.initialEffectList.add(new Effect("回避", 0, 999, 20, 0));
        bi.monsterList.add(monster1);

        bi.monsterFormation[1][1] = monster1;
        bi.monsterFormation[0][1] = monster1;
        bi.monsterFormation[2][1] = monster1;
        bi.monsterFormation[1][0] = monster1;
        bi.monsterFormation[1][2] = monster1;
        global.battleInfoList.add(bi);
    }

    public void initMonsterList(){
        BattleInfo bi = new BattleInfo();
        bi.isBossBattle = false;
        Character monster1 = new Character();
        monster1.element = "tree";
        monster1.spriteName = "monster_不幸猫头鹰之谣";
        monster1.name = "不幸猫头鹰之谣";
        monster1.lv = 80;
        monster1.HP = 900;
        monster1.realHP = monster1.HP;
        monster1.ATK = 10000;
        monster1.DEF = 3000;
        monster1.mpAttackRatio = 0f;
        monster1.mpDefendRatio = 0f;
        monster1.plateList = new int[]{CHARGE, CHARGE, BLAST_HORIZONTAL, BLAST_VERTICAL, CHARGE};
        //monster1.initialEffectList.add(new Effect("攻击时给予状态雾",25,999,100,1));
        monster1.initialEffectList.add(new Effect("攻击时给予状态Magia封印", 0, 999, 100, 2));
        monster1.initialEffectList.add(new Effect("回避", 0, 999, 20, 0));
        bi.monsterList.add(monster1);
        bi.monsterFormation[0][0] = monster1;

        Character monster2 = new Character();
        monster2.element = "fire";
        monster2.spriteName = "monster_工熊之谣(蓝)";
        monster2.name = "工熊之谣";
        monster2.lv = 80;
        monster2.HP = 1000;
        monster2.realHP = monster2.HP;
        monster2.ATK = 9000;
        monster2.DEF = 4200;
        monster2.mpAttackRatio = 0f;
        monster2.mpDefendRatio = 0f;
        monster2.plateList = new int[]{CHARGE, CHARGE, BLAST_HORIZONTAL, BLAST_VERTICAL, CHARGE};
        monster2.initialEffectList.add(new Effect("攻击时给予状态Magia封印", 5, 999, 20, 2));
        //monster2.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        bi.monsterList.add(monster2);
        bi.monsterFormation[1][0] = monster2;

        Character monster3 = new Character();
        monster3.element = "water";
        monster3.spriteName = "monster_流浪的魔女的手下";
        monster3.name = "流浪的魔女的手下";
        monster3.lv = 80;
        monster3.HP = 800;
        monster3.realHP = monster3.HP;
        monster3.ATK = 9000;
        monster3.DEF = 4500;
        monster3.mpAttackRatio = 0f;
        monster3.mpDefendRatio = 0f;
        monster3.plateList = new int[]{CHARGE, CHARGE, BLAST_HORIZONTAL, BLAST_VERTICAL, CHARGE};
        monster3.initialEffectList.add(new Effect("攻击时给予状态Magia封印", 0, 999, 20, 2));
        //monster3.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        bi.monsterList.add(monster3);
        bi.monsterFormation[1][2] = monster3;

        Character monster4 = new Character();
        monster4.element = "light";
        monster4.spriteName = "monster_螯合吉祥物之谣";
        monster4.name = "螯合吉祥物之谣";
        monster4.lv = 80;
        monster4.HP = 1500;
        monster4.realHP = monster4.HP;
        monster4.ATK = 10000;
        monster4.DEF = 3000;
        monster4.mpAttackRatio = 0f;
        monster4.mpDefendRatio = 0f;
        monster4.plateList = new int[]{CHARGE, CHARGE, BLAST_HORIZONTAL, BLAST_VERTICAL, CHARGE};
        monster4.initialEffectList.add(new Effect("攻击时给予状态Magia封印", 50, 999, 20, 2));
        //monster4.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        bi.monsterList.add(monster4);
        bi.monsterFormation[2][0] = monster4;
        global.battleInfoList.add(bi);
    }

    public void initFormation(){
        global.formationList.add(new Formation(new int[][]{{1, 0, 1}, {0, 2, 0}, {1, 0, 1}}, "英勇梯队"));
        global.formationList.get(0).gridAllEffectList[1][1].add(new SkillEffect("攻击力UP", 10, "自", 0, 100));
        global.formationList.get(0).gridAllEffectList[1][1].add(new SkillEffect("防御力UP", 10, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{1, 0, 0}, {2, 1, 2}, {1, 0, 0}}, "光明方阵"));
        global.formationList.get(1).gridAllEffectList[1][0].add(new SkillEffect("防御力UP", 10, "自", 0, 100));
        global.formationList.get(1).gridAllEffectList[1][2].add(new SkillEffect("攻击力UP", 10, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{0, 1, 0}, {2, 1, 2}, {0, 1, 0}}, "强力十字"));
        global.formationList.get(2).gridAllEffectList[1][0].add(new SkillEffect("防御力UP", 10, "自", 0, 100));
        global.formationList.get(2).gridAllEffectList[1][0].add(new SkillEffect("攻击力DOWN", 10, "自", 0, 100));
        global.formationList.get(2).gridAllEffectList[1][2].add(new SkillEffect("攻击力UP", 10, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{0, 0, 2}, {2, 1, 1}, {0, 0, 2}}, "属性方阵·火"));
        global.formationList.get(3).gridAllEffectList[0][2].add(new SkillEffect("火属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(3).gridAllEffectList[1][0].add(new SkillEffect("火属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(3).gridAllEffectList[2][2].add(new SkillEffect("火属性攻击力UP", 15, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{0, 0, 2}, {2, 1, 1}, {0, 0, 2}}, "属性方阵·水"));
        global.formationList.get(4).gridAllEffectList[0][2].add(new SkillEffect("水属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(4).gridAllEffectList[1][0].add(new SkillEffect("水属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(4).gridAllEffectList[2][2].add(new SkillEffect("水属性攻击力UP", 15, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{0, 0, 2}, {2, 1, 1}, {0, 0, 2}}, "属性方阵·木"));
        global.formationList.get(5).gridAllEffectList[0][2].add(new SkillEffect("木属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(5).gridAllEffectList[1][0].add(new SkillEffect("木属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(5).gridAllEffectList[2][2].add(new SkillEffect("木属性攻击力UP", 15, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{0, 0, 2}, {2, 1, 1}, {0, 0, 2}}, "属性方阵·光"));
        global.formationList.get(6).gridAllEffectList[0][2].add(new SkillEffect("光属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(6).gridAllEffectList[1][0].add(new SkillEffect("光属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(6).gridAllEffectList[2][2].add(new SkillEffect("光属性攻击力UP", 15, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{0, 0, 2}, {2, 1, 1}, {0, 0, 2}}, "属性方阵·暗"));
        global.formationList.get(7).gridAllEffectList[0][2].add(new SkillEffect("暗属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(7).gridAllEffectList[1][0].add(new SkillEffect("暗属性攻击力UP", 15, "自", 0, 100));
        global.formationList.get(7).gridAllEffectList[2][2].add(new SkillEffect("暗属性攻击力UP", 15, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{0, 1, 0}, {1, 2, 1}, {0, 1, 0}}, "英勇十字"));
        global.formationList.get(8).gridAllEffectList[1][1].add(new SkillEffect("防御力UP", 15, "自", 0, 100));
        global.formationList.get(8).gridAllEffectList[1][1].add(new SkillEffect("异常状态耐性UP", 10, "自", 0, 100));
        global.formationList.get(8).gridAllEffectList[1][1].add(new SkillEffect("Blast伤害UP", 10, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{2, 0, 2}, {0, 1, 0}, {2, 0, 2}}, "守护之力"));
        global.formationList.get(9).gridAllEffectList[0][0].add(new SkillEffect("HP自动回复", 2, "自", 0, 100));
        global.formationList.get(9).gridAllEffectList[0][0].add(new SkillEffect("防御力UP", 10, "自", 0, 100));
        global.formationList.get(9).gridAllEffectList[0][2].add(new SkillEffect("HP自动回复", 2, "自", 0, 100));
        global.formationList.get(9).gridAllEffectList[0][2].add(new SkillEffect("防御力UP", 10, "自", 0, 100));
        global.formationList.get(9).gridAllEffectList[2][0].add(new SkillEffect("HP自动回复", 2, "自", 0, 100));
        global.formationList.get(9).gridAllEffectList[2][0].add(new SkillEffect("防御力UP", 10, "自", 0, 100));
        global.formationList.get(9).gridAllEffectList[2][2].add(new SkillEffect("HP自动回复", 2, "自", 0, 100));
        global.formationList.get(9).gridAllEffectList[2][2].add(new SkillEffect("防御力UP", 10, "自", 0, 100));

        global.formationList.add(new Formation(new int[][]{{1, 0, 1}, {0, 2, 0}, {1, 0, 1}}, "强力梯队"));
        global.formationList.get(10).gridAllEffectList[1][1].add(new SkillEffect("攻击力UP", 10, "自", 0, 100));
        global.formationList.get(10).gridAllEffectList[1][1].add(new SkillEffect("伤害削减", 5, "自", 0, 100));

    }

    public void initMemoria(){
        for(int i = 0; i < 3; i++){
            global.USEDMEMORIA[i] = new ArrayList<>();
        }
        //把preloadMemoria中的记忆按照星级分到USEDMEMORIA中
        for(int i = 0; i < global.preloadMemoria.length; i++){
            int id = global.preloadMemoria[i];
            Memoria m = new Memoria("" + id, StartActivity.this);
            global.USEDMEMORIA[m.star - 2].add(id);
        }

        ////把所有记忆都加载到背包中
        //for(int i = 0; i < USEDMEMORIA.length; i++){
        //    for(int j = 0; j < USEDMEMORIA[i].size(); j++){
        //        Memoria m = new Memoria(""+USEDMEMORIA[i].get(j),StartActivity.this);
        //        m.setBreakthrough(0);
        //        m.setLv(m.lvNow);
        //        memoriaBag.add(m);
        //    }
        //}
        ////随机删除记忆
        //while(memoriaBag.size() >= 16){
        //    int tempId = (int)(Math.random()*memoriaBag.size());
        //    memoriaBag.remove(tempId);
        //}
    }

    public void initCollection(){
        JSONObject textList = null;
        InputStream stream = getResources().openRawResource(R.raw.collections);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer sb = new StringBuffer();
        String line = "";
        try{
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            textList = new JSONObject(sb.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            JSONArray efList = textList.getJSONArray("collections");
            for(int i = 0; i < efList.length(); i++){
                Collection c = new Collection();
                JSONObject temp = efList.getJSONObject(i);
                c.name = temp.optString("name");
                c.description = temp.optString("description");
                c.effectDescription = temp.optString("effectDescription");
                c.icon = temp.optString("icon");
                c.price = temp.optInt("price");
                c.background = temp.optString("background");
                global.collectionList.add(c);
                global.collectionDict.put(c.name, c);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        Collections.shuffle(global.collectionList);

    }

    public void initBackground(){
        global.DAY_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_day_east_street_up, R.drawable.background_day_east_riverbank_down));
        global.DAY_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_day_east_street_up, R.drawable.background_day_east_street_down));
        global.DAY_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_day_riverbank_up, R.drawable.background_day_riverbank_down));
        global.DAY_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_day_east_street_up, R.drawable.background_day_shuide_street_down));
        global.DAY_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_day_riverbank_up, R.drawable.background_day_street2_down));
        global.DUSK_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_dusk_street_up, R.drawable.background_dusk_east_riverbank_down));
        global.DUSK_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_dusk_street_up, R.drawable.background_dusk_street2_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction1_up, R.drawable.background_junction1_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction1_up, R.drawable.background_junction2_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction1_up, R.drawable.background_junction3_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction1_up, R.drawable.background_junction4_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction1_up, R.drawable.background_junction5_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction6_up, R.drawable.background_junction6_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction7_up, R.drawable.background_junction7_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction8_up, R.drawable.background_junction8_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction9_up, R.drawable.background_junction9_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction10_up, R.drawable.background_junction10_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction1_up, R.drawable.background_junction11_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction12_up, R.drawable.background_junction12_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction1_up, R.drawable.background_junction13_down));
        global.JUNCTION_BACKGROUND_IMAGE_LIST.add(new BackgroundImage(R.drawable.background_junction1_up, R.drawable.background_junction14_down));
    }

    public void initRandomBuff(){
        JSONObject textList = null;
        InputStream stream = getResources().openRawResource(R.raw.random_buff);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer sb = new StringBuffer();
        String line = "";
        try{
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            textList = new JSONObject(sb.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            for(int i = 2; i <= 16; i++){
                JSONArray efList = textList.getJSONArray("" + i);
                ArrayList<String> buffIdList = new ArrayList<>();
                for(int j = 0; j < efList.length(); j++){
                    buffIdList.add(efList.optString(j));
                }
                global.ENEMY_RANDOM_BUFF_DICT.put(i, buffIdList);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void initMapRandomPoint(){
        JSONObject textList = null;
        InputStream stream = getResources().openRawResource(R.raw.map_random_points);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer sb = new StringBuffer();
        String line = "";
        try{
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            textList = new JSONObject(sb.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            JSONArray efList = textList.getJSONArray("mapPoints");
            global.mapRandomPoint = new int[efList.length()][2];
            for(int i = 0; i < efList.length(); i++){
                global.mapRandomPoint[i][0] = efList.getJSONArray(i).getInt(0);
                global.mapRandomPoint[i][1] = efList.getJSONArray(i).getInt(1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void initExtraMissionList(){
        ExtraMission em = new ExtraMission("3回合内通关", new Bonus(1000, 0));
        global.extraMissionList.add(em);

        em = new ExtraMission("3回合内通关", new Bonus(0, 1));
        global.extraMissionList.add(em);

        em = new ExtraMission("5回合内通关", new Bonus(500, 0));
        global.extraMissionList.add(em);

        //em = new ExtraMission("某次攻击对两名以上敌人造成伤害", new Bonus(1000,0));
        //extraMissionList.add(em);

        em = new ExtraMission("释放一次Magia", new Bonus(1000, 0));
        global.extraMissionList.add(em);

        em = new ExtraMission("释放一次Magia", new Bonus(0, 1));
        global.extraMissionList.add(em);

        em = new ExtraMission("发动一次3BCombo", new Bonus(1000, 0));
        global.extraMissionList.add(em);

        em = new ExtraMission("发动一次3BCombo", new Bonus(0, 1));
        global.extraMissionList.add(em);

        em = new ExtraMission("发动一次3CCombo", new Bonus(1000, 0));
        global.extraMissionList.add(em);

        em = new ExtraMission("发动一次3CCombo", new Bonus(0, 1));
        global.extraMissionList.add(em);

        em = new ExtraMission("消耗5个charge", new Bonus(1000, 0));
        global.extraMissionList.add(em);

        em = new ExtraMission("消耗5个charge", new Bonus(0, 1));
        global.extraMissionList.add(em);

        em = new ExtraMission("至少一人血量大于80%", new Bonus(1000, 0));
        global.extraMissionList.add(em);

        em = new ExtraMission("至少一人血量大于60%", new Bonus(500, 0));
        global.extraMissionList.add(em);

        em = new ExtraMission("全员血量大于60%", new Bonus(1000, 0));
        global.extraMissionList.add(em);
    }

    private void initSoundPool(){
        //effect sound:
        int[] soundList = new int[]{42,43,47,48,49,50,51,52,53,54,55,56,57,58,59,60,62,68,69,70,72,73,74};
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(soundList.length*4);
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
        builder.setAudioAttributes(attrBuilder.build());
        global.soundPool = builder.build();
    }


    @Override
    public void onBackPressed(){
//        super.onBackPressed();
    }

    public void saveCharacters(){
//        //写入
//        SharedPreferences.Editor editor = getSharedPreferences("characters",MODE_PRIVATE).edit();
//        for()
//        editor.putString("Name","Sam");
//        editor.apply();
//
////读取
//        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
//        String name = pref.getString("Name","tom");//后一个是如果没读到的默认值
    }

}

class Formation{
    public int[][] grid;//0空 1可以填角色 2all位

    public ArrayList<SkillEffect>[][] gridAllEffectList = new ArrayList[3][3];

    public String name = "";

    public boolean isChose = false; //在FormationActivity里是否被选中

    public Formation(int[][] grid, String name){
        this.grid = grid;
        this.name = name;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                gridAllEffectList[i][j] = new ArrayList<>();
            }
        }
    }

    public void setFormation(ImageView[][] formation, ImageView allView, int id){
        //id为第几个人，从0开始
        int count = 0; //记录这是第几个人的位置
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                switch(grid[i][j]){
                    case 0:
                        formation[i][j].setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        formation[i][j].setVisibility(View.VISIBLE);
                        if(count == id){
                            formation[i][j].setImageResource(R.drawable.red_block);
                            if(allView != null){
                                allView.setVisibility((grid[i][j] > 1) ? View.VISIBLE : View.INVISIBLE);
                            }
                        }else{
                            formation[i][j].setImageResource(R.drawable.empty_block);
                        }
                        count++;
                        break;
                    default:
                }
            }
        }
    }
}

class BattleInfo{
    public static final int JUNCTION = 1;
    public static final int DAY = 2;
    public static final int DUSK = 3;
    ArrayList<Character> monsterList = new ArrayList<>();
    Character[][] monsterFormation = new Character[3][3];
    boolean isBossBattle;
    String battleName = "";
    String battleDescription = "";
    int monsterNumber = 1;
    ArrayList<Pair<Effect, Integer>> useEffect = new ArrayList<>();
    int extraMissionId = -1;
    String recommendLV = "??";
    int backgroundId = -1;
    int backgroundType = JUNCTION;

    public BattleInfo(){
    }
}

class Collection{
    public String name;
    public String description;
    public String effectDescription;
    public String icon;
    public String background = "item_frame_3";
    public int price;
    public boolean isOwn = false;

    public Collection(String name, String description, String effectDescription, String icon, int price, String background){
        this.name = name;
        this.description = description;
        this.effectDescription = effectDescription;
        this.icon = icon;
        this.price = price;
        this.background = background;
    }

    public Collection(){

    }
}

class ExtraMission{
    String name;
    Bonus bonus;

    ExtraMission(String name, Bonus bonus){
        this.name = name;
        this.bonus = bonus;
    }
}

class Bonus{
    int cc;
    int griefSeed;

    Bonus(int cc, int griefSeed){
        this.cc = cc;
        this.griefSeed = griefSeed;
    }
}

class BackgroundImage{
    int upImageId;
    int downImageId;

    public BackgroundImage(int upImageId, int downImageId){
        this.upImageId = upImageId;
        this.downImageId = downImageId;
    }
}