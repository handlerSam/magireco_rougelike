package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.Map;

import static com.live2d.rougelike.CharacterPlateView.ACCELE;
import static com.live2d.rougelike.CharacterPlateView.BLAST_HORIZONTAL;
import static com.live2d.rougelike.CharacterPlateView.BLAST_VERTICAL;
import static com.live2d.rougelike.CharacterPlateView.CHARGE;

public class StartActivity extends AppCompatActivity {

    public static ArrayList<Formation> formationList = new ArrayList<>();
    //选中的formation：StartActivity.formationList.get(TeamChooseActivity.usingFormationId)

    public static Character[] characters = {null,null,null,null,null};

    public static ArrayList<Character> monsterList = new ArrayList<>();

    public static int[][] USEDMEMORIA = {
            {1016,1017,1018,1019,1020,1021,1022,1023,1024,1025,1026,1027,1028,1029,1030,1031,1032,1033,1034,1035,1037,1038,1039,1041,1042,1043,1044,1045,1046,1047,1048,1103,1112,1117,1119},
            {1049,1050,1051,1052,1053,1054,1055,1056,1057,1059,1061,1062,1063,1064,1065,1066,1164,1189,1193,1219,1268,1273,1289,1316,1322,1391,1438},
            {1067,1068,1069,1070,1071,1072,1107,1113,1121,1122,1130,1134,1138,1152,1239,1243,1161,1168,1176,1177,1188,1190,1192,1203,1209,1218,1225,1226,1232,1234,1246,1247,1250,1260,1267,1320,1321,1390,1449,1456,1437}
    };

    public static ArrayList<Character> characterList = new ArrayList<>();

    public static ArrayList<Memoria> memoriaBag = new ArrayList<>();

    public static int SCREEN_WIDTH = 0;

    public static int SCREEN_HEIGHT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        for(int i = 0; i < USEDMEMORIA.length; i++){
            for(int j = 0; j < USEDMEMORIA[i].length; j++){
                Memoria m = new Memoria(""+USEDMEMORIA[i][j],StartActivity.this);
                m.setBreakthrough(0);
                m.setLv(1);
                memoriaBag.add(m);
                m = new Memoria(""+USEDMEMORIA[i][j],StartActivity.this);
                m.setBreakthrough(4);
                m.setLv(m.lvMax);
                memoriaBag.add(m);
            }
        }
        initCharacterList();
        initMonsterList();

        formationList.add(new Formation(new int[][]{{1,0,1},{0,2,0},{1,0,1}}, "英勇梯队"));
        formationList.get(0).gridAllEffectList[1][1].add(new SkillEffect("攻击力UP",10,"自",0,100));
        formationList.get(0).gridAllEffectList[1][1].add(new SkillEffect("防御力UP",10,"自",0,100));

        formationList.add(new Formation(new int[][]{{1,0,0},{2,1,2},{1,0,0}}, "光明方阵"));
        formationList.get(1).gridAllEffectList[1][0].add(new SkillEffect("防御力UP",10,"自",0,100));
        formationList.get(1).gridAllEffectList[1][2].add(new SkillEffect("攻击力UP",10,"自",0,100));

        formationList.add(new Formation(new int[][]{{0,1,0},{2,1,2},{0,1,0}}, "强力十字"));
        formationList.get(2).gridAllEffectList[1][0].add(new SkillEffect("防御力UP",10,"自",0,100));
        formationList.get(2).gridAllEffectList[1][0].add(new SkillEffect("攻击力DOWN",10,"自",0,100));
        formationList.get(2).gridAllEffectList[1][2].add(new SkillEffect("攻击力UP",10,"自",0,100));

        formationList.add(new Formation(new int[][]{{0,0,2},{2,1,1},{0,0,2}}, "属性方阵·火"));
        formationList.get(3).gridAllEffectList[0][2].add(new SkillEffect("火属性攻击力UP",15,"自",0,100));
        formationList.get(3).gridAllEffectList[1][0].add(new SkillEffect("火属性攻击力UP",15,"自",0,100));
        formationList.get(3).gridAllEffectList[2][2].add(new SkillEffect("火属性攻击力UP",15,"自",0,100));

        formationList.add(new Formation(new int[][]{{0,0,2},{2,1,1},{0,0,2}}, "属性方阵·水"));
        formationList.get(4).gridAllEffectList[0][2].add(new SkillEffect("水属性攻击力UP",15,"自",0,100));
        formationList.get(4).gridAllEffectList[1][0].add(new SkillEffect("水属性攻击力UP",15,"自",0,100));
        formationList.get(4).gridAllEffectList[2][2].add(new SkillEffect("水属性攻击力UP",15,"自",0,100));

        formationList.add(new Formation(new int[][]{{0,0,2},{2,1,1},{0,0,2}}, "属性方阵·木"));
        formationList.get(5).gridAllEffectList[0][2].add(new SkillEffect("木属性攻击力UP",15,"自",0,100));
        formationList.get(5).gridAllEffectList[1][0].add(new SkillEffect("木属性攻击力UP",15,"自",0,100));
        formationList.get(5).gridAllEffectList[2][2].add(new SkillEffect("木属性攻击力UP",15,"自",0,100));

        formationList.add(new Formation(new int[][]{{0,0,2},{2,1,1},{0,0,2}}, "属性方阵·光"));
        formationList.get(6).gridAllEffectList[0][2].add(new SkillEffect("光属性攻击力UP",15,"自",0,100));
        formationList.get(6).gridAllEffectList[1][0].add(new SkillEffect("光属性攻击力UP",15,"自",0,100));
        formationList.get(6).gridAllEffectList[2][2].add(new SkillEffect("光属性攻击力UP",15,"自",0,100));

        formationList.add(new Formation(new int[][]{{0,0,2},{2,1,1},{0,0,2}}, "属性方阵·暗"));
        formationList.get(7).gridAllEffectList[0][2].add(new SkillEffect("暗属性攻击力UP",15,"自",0,100));
        formationList.get(7).gridAllEffectList[1][0].add(new SkillEffect("暗属性攻击力UP",15,"自",0,100));
        formationList.get(7).gridAllEffectList[2][2].add(new SkillEffect("暗属性攻击力UP",15,"自",0,100));

        formationList.add(new Formation(new int[][]{{0,1,0},{1,2,1},{0,1,0}}, "英勇十字"));
        formationList.get(8).gridAllEffectList[1][1].add(new SkillEffect("防御力UP",15,"自",0,100));
        formationList.get(8).gridAllEffectList[1][1].add(new SkillEffect("异常状态耐性UP",10,"自",0,100));
        formationList.get(8).gridAllEffectList[1][1].add(new SkillEffect("Blast伤害UP",10,"自",0,100));

        formationList.add(new Formation(new int[][]{{2,0,2},{0,1,0},{2,0,2}}, "守护之力"));
        formationList.get(9).gridAllEffectList[0][0].add(new SkillEffect("HP自动回复",2,"自",0,100));
        formationList.get(9).gridAllEffectList[0][0].add(new SkillEffect("防御力UP",10,"自",0,100));
        formationList.get(9).gridAllEffectList[0][2].add(new SkillEffect("HP自动回复",2,"自",0,100));
        formationList.get(9).gridAllEffectList[0][2].add(new SkillEffect("防御力UP",10,"自",0,100));
        formationList.get(9).gridAllEffectList[2][0].add(new SkillEffect("HP自动回复",2,"自",0,100));
        formationList.get(9).gridAllEffectList[2][0].add(new SkillEffect("防御力UP",10,"自",0,100));
        formationList.get(9).gridAllEffectList[2][2].add(new SkillEffect("HP自动回复",2,"自",0,100));
        formationList.get(9).gridAllEffectList[2][2].add(new SkillEffect("防御力UP",10,"自",0,100));

        formationList.add(new Formation(new int[][]{{1,0,1},{0,2,0},{1,0,1}}, "强力梯队"));
        formationList.get(10).gridAllEffectList[1][1].add(new SkillEffect("攻击力UP",10,"自",0,100));
        formationList.get(10).gridAllEffectList[1][1].add(new SkillEffect("伤害削减",5,"自",0,100));

//        GeckoView view = findViewById(R.id.geckoview);
//        GeckoSession session = new GeckoSession(new GeckoSessionSettings.Builder().usePrivateMode(true)
//                .useTrackingProtection(true)
//                .userAgentMode(USER_AGENT_MODE_MOBILE)
//                .userAgentOverride("")
//                .suspendMediaWhenInactive(true)
//                .allowJavascript(true).build());
//
//        session.setContentDelegate(new GeckoSession.ContentDelegate() {});
//
//        if (sRuntime == null) {
//            // GeckoRuntime can only be initialized once per process
//            sRuntime = GeckoRuntime.create(this);
//        }
//
//        session.open(sRuntime);
//        view.setSession(session);
//        session.loadUri("about:buildconfig"); // Or any other URL...

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        SCREEN_HEIGHT = Math.min(metric.widthPixels,metric.heightPixels);  // 屏幕宽度（像素）
        SCREEN_WIDTH = Math.max(metric.widthPixels,metric.heightPixels);  // 屏幕高度（像素）

        Log.d("sam","screenWidth:"+SCREEN_WIDTH+", Height:"+SCREEN_HEIGHT);

        Intent intent1 = new Intent(StartActivity.this, TeamChooseActivity.class);
        startActivity(intent1);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void initCharacterList(){
        Character remu = new Character();
        remu.breakThrough = 3;
        remu.element = "tree";
        remu.name = "柊音梦";
        remu.choosingActivityImage = "team_choose_101400_1";
        remu.spriteName = "Hiiragi Nemu";
        remu.charIconImage = "card_10144_";
        remu.magiaSkillIconName = "icon_skill_1012";
        remu.doppelImageName = "mini_101400_dd";
        remu.isLeader = false;
        remu.lv = 80;
        remu.star = 4;
        remu.HP = 23007;
        remu.realHP = remu.HP;
        remu.ATK = 6385;
        remu.DEF = 6129;
        remu.realMP = 0;
        remu.plateList = new int[]{ACCELE,ACCELE,ACCELE,BLAST_VERTICAL,CHARGE};
        remu.mpAttackRatio = 1.2f;
        remu.mpDefendRatio = 1.0f;
        remu.connectOriginEffectList.add(new SkillEffect("攻击力UP",35,"自",1,100));
        remu.connectOriginEffectList.add(new SkillEffect("HP回复",37,"自",1,100));
        remu.connectOriginEffectList.add(new SkillEffect("攻击时给予状态幻惑",65,"自",1,50,1));

        remu.connectAfterEffectList.add(new SkillEffect("攻击力UP",40,"自",1,100));
        remu.connectAfterEffectList.add(new SkillEffect("HP回复",42,"自",1,100));
        remu.connectAfterEffectList.add(new SkillEffect("攻击时给予状态幻惑",50,"自",1,50,1));

        remu.magiaTarget = "敌单";
        remu.magiaOriginMagnification = 1128;
        remu.magiaAfterMagnification = 1192;
        remu.doppelMagnification = 2763;
        remu.magiaOriginEffectList.add(new SkillEffect("给予状态魅惑",0,"敌单",1,100));
        remu.magiaOriginEffectList.add(new SkillEffect("给予状态诅咒",15,"敌单",1,100));
        remu.magiaOriginEffectList.add(new SkillEffect("给予状态黑暗",35,"敌单",1,100));
        remu.magiaOriginEffectList.add(new SkillEffect("攻击力UP",30,"己全",3,100));

        remu.magiaAfterEffectList.add(new SkillEffect("给予状态魅惑",0,"敌单",1,100));
        remu.magiaAfterEffectList.add(new SkillEffect("给予状态诅咒",15,"敌单",1,100));
        remu.magiaAfterEffectList.add(new SkillEffect("给予状态黑暗",35,"敌单",1,100));
        remu.magiaAfterEffectList.add(new SkillEffect("BUFF解除",0,"敌单",1,100));
        remu.magiaAfterEffectList.add(new SkillEffect("攻击力UP",30,"己全",3,100));

        remu.doppelEffectList.add(new SkillEffect("给予状态魅惑",0,"敌单",1,100));
        remu.doppelEffectList.add(new SkillEffect("给予状态诅咒",15,"敌单",1,100));
        remu.doppelEffectList.add(new SkillEffect("给予状态黑暗",35,"敌单",1,100));
        remu.doppelEffectList.add(new SkillEffect("BUFF解除",0,"敌单",1,100));
        remu.doppelEffectList.add(new SkillEffect("攻击力UP",30,"己全",3,100));
        
        //remu.initialEffectList.add(new Effect("眩晕",0,1,100,0));

        Character toca = new Character();
        toca.breakThrough = 4;
        toca.element = "fire";
        toca.name = "里见灯花";
        toca.choosingActivityImage = "team_choose_100700_2";
        toca.charIconImage = "card_10074_";
        toca.magiaSkillIconName = "icon_skill_1014";
        toca.doppelImageName = "mini_100700_dd";
        toca.isLeader = false;
        toca.spriteName = "Satomi Touka";
        toca.lv = 90;
        toca.star = 5;
        toca.HP = 16689;
        toca.realHP = toca.HP;
        toca.ATK = 7756;
        toca.DEF = 4832;
        toca.realMP = 0;
        toca.plateList = new int[]{ACCELE,ACCELE,BLAST_HORIZONTAL,BLAST_VERTICAL,CHARGE};
        toca.mpAttackRatio = 1.2f;
        toca.mpDefendRatio = 1.2f;
        toca.connectOriginEffectList.add(new SkillEffect("攻击力UP",35,"自",1,100));
        toca.connectOriginEffectList.add(new SkillEffect("MP回复",20,"自",1,100));
        toca.connectOriginEffectList.add(new SkillEffect("攻击时给予状态Magia封印",0,"自",1,60,1));

        toca.connectAfterEffectList.add(new SkillEffect("攻击力UP",40,"自",1,100));
        toca.connectAfterEffectList.add(new SkillEffect("MP回复",25,"自",1,100));
        toca.connectAfterEffectList.add(new SkillEffect("攻击时给予状态Magia封印",0,"自",1,100,1));

        toca.magiaTarget = "敌全";
        toca.magiaOriginMagnification = 380;
        toca.magiaAfterMagnification = 400;
        toca.doppelMagnification = 902;
        toca.magiaOriginEffectList.add(new SkillEffect("防御力DOWN",25,"敌全",3,100));
        toca.magiaOriginEffectList.add(new SkillEffect("防御力UP",32,"自",3,100));

        toca.magiaAfterEffectList.add(new SkillEffect("防御力DOWN",27,"敌全",3,100));
        toca.magiaAfterEffectList.add(new SkillEffect("防御力UP",47,"自",3,100));
        toca.magiaAfterEffectList.add(new SkillEffect("Magia伤害UP",35,"自",3,100));

        toca.doppelEffectList.add(new SkillEffect("防御力DOWN",35,"敌全",3,100));
        toca.doppelEffectList.add(new SkillEffect("防御力UP",47,"自",3,100));
        toca.doppelEffectList.add(new SkillEffect("Magia伤害UP",35,"自",5,100));

       // toca.initialEffectList.add(new Effect("眩晕",0,1,100,0));
        
        Character ui = new Character();
        ui.breakThrough = 3;
        ui.element = "dark";
        ui.name = "环忧";
        ui.choosingActivityImage = "team_choose_101500_2";
        ui.charIconImage = "card_10154_";
        ui.magiaSkillIconName = "icon_skill_1014";
        ui.doppelImageName = "mini_101500_dd";
        ui.isLeader = false;
        ui.spriteName = "Tamaki Ui";
        ui.lv = 80;
        ui.star = 5;
        ui.HP = 21658;
        ui.realHP = ui.HP;
        ui.ATK = 6729;
        ui.DEF = 7048;
        ui.realMP = 0;
        ui.plateList = new int[]{ACCELE,ACCELE,BLAST_HORIZONTAL,CHARGE,CHARGE};
        ui.mpAttackRatio = 0.9f;
        ui.mpDefendRatio = 0.9f;
        ui.connectOriginEffectList.add(new SkillEffect("攻击力UP",35,"自",1,100));
        ui.connectOriginEffectList.add(new SkillEffect("伤害削减无效",100,"自",1,100));
        ui.connectOriginEffectList.add(new SkillEffect("攻击时给予状态诅咒",15,"自",1,65,1));

        ui.connectAfterEffectList.add(new SkillEffect("攻击力UP",40,"自",1,100));
        ui.connectAfterEffectList.add(new SkillEffect("伤害削减无效",100,"自",1,100));
        ui.connectAfterEffectList.add(new SkillEffect("攻击时给予状态诅咒",15,"自",1,100,1));

        ui.magiaTarget = "敌全";
        ui.magiaOriginMagnification = 390;
        ui.magiaAfterMagnification = 410;
        ui.doppelMagnification = 902;
        ui.magiaOriginEffectList.add(new SkillEffect("MP伤害",30,"敌全",1,100));
        ui.magiaOriginEffectList.add(new SkillEffect("给予状态诅咒",15,"敌全",1,100));

        ui.magiaAfterEffectList.add(new SkillEffect("MP伤害",35,"敌全",1,100));
        ui.magiaAfterEffectList.add(new SkillEffect("给予状态诅咒",15,"敌全",1,100));
        ui.magiaAfterEffectList.add(new SkillEffect("MP回复",29,"自",1,100));

        ui.doppelEffectList.add(new SkillEffect("MP伤害",60,"敌全",1,100));
        ui.doppelEffectList.add(new SkillEffect("给予状态诅咒",15,"敌全",1,100));
        ui.doppelEffectList.add(new SkillEffect("MP回复",34,"自",1,100));

        //ui.initialEffectList.add(new Effect("眩晕",0,1,100,0));
        
        characterList.add(remu);
        characterList.add(toca);
        characterList.add(ui);

        characterList.get(2).isLeader = true;

        characterList.get(0).setMemoria(0,memoriaBag.get(0));
        characterList.get(0).setMemoria(1,memoriaBag.get(1));
        characterList.get(1).setMemoria(0,memoriaBag.get(2));

        memoriaBag.get(0).setCarrier(0);
        memoriaBag.get(1).setCarrier(0);
        memoriaBag.get(2).setCarrier(1);

        characters[1] = characterList.get(0);
        characters[2] = characterList.get(2);
        characters[3] = characterList.get(1);
    }

    public void initMonsterList(){
        Character monster1 = new Character();
        monster1.element = "tree";
        monster1.spriteName = "monster_不幸猫头鹰之谣";
        monster1.name = "不幸猫头鹰之谣";
        monster1.lv = 80;
        monster1.HP = 90000;
        monster1.realHP = monster1.HP;
        monster1.ATK = 12000;
        monster1.DEF = 3000;
        monster1.mpAttackRatio = 0f;
        monster1.mpDefendRatio = 0f;
        monster1.plateList = new int[]{CHARGE,CHARGE,BLAST_HORIZONTAL,BLAST_VERTICAL,CHARGE};
        monster1.initialEffectList.add(new Effect("攻击时给予状态雾",25,999,100,1));
        //monster1.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        monsterList.add(monster1);

        Character monster2 = new Character();
        monster2.element = "fire";
        monster2.spriteName = "monster_工熊之谣(蓝)";
        monster2.name = "工熊之谣";
        monster2.lv = 80;
        monster2.HP = 100000;
        monster2.realHP = monster2.HP;
        monster2.ATK = 9000;
        monster2.DEF = 4200;
        monster2.mpAttackRatio = 0f;
        monster2.mpDefendRatio = 0f;
        monster2.plateList = new int[]{CHARGE,CHARGE,BLAST_HORIZONTAL,BLAST_VERTICAL,CHARGE};
        monster2.initialEffectList.add(new Effect("攻击时给予状态烧伤",5,999,100,1));
        //monster2.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        monsterList.add(monster2);

        Character monster3 = new Character();
        monster3.element = "water";
        monster3.spriteName = "monster_流浪的魔女的手下";
        monster3.name = "流浪的魔女的手下";
        monster3.lv = 80;
        monster3.HP = 80000;
        monster3.realHP = monster3.HP;
        monster3.ATK = 9000;
        monster3.DEF = 4500;
        monster3.mpAttackRatio = 0f;
        monster3.mpDefendRatio = 0f;
        monster3.plateList = new int[]{CHARGE,CHARGE,BLAST_HORIZONTAL,BLAST_VERTICAL,CHARGE};
        monster3.initialEffectList.add(new Effect("攻击时给予状态眩晕",0,999,100,1));
        //monster3.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        monsterList.add(monster3);

        Character monster4 = new Character();
        monster4.element = "light";
        monster4.spriteName = "monster_螯合吉祥物之谣";
        monster4.name = "螯合吉祥物之谣";
        monster4.lv = 80;
        monster4.HP = 150000;
        monster4.realHP = monster4.HP;
        monster4.ATK = 12000;
        monster4.DEF = 3000;
        monster4.mpAttackRatio = 0f;
        monster4.mpDefendRatio = 0f;
        monster4.plateList = new int[]{CHARGE,CHARGE,BLAST_HORIZONTAL,BLAST_VERTICAL,CHARGE};
        monster4.initialEffectList.add(new Effect("攻击时给予状态幻惑",50,999,100,1));
        //monster4.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        monsterList.add(monster4);
    }

    public static void clearCharBattleInfo(){
        for(int i = 0; i < characterList.size(); i++){
//            characterList.get(i).diamondNumber = 0;
            characterList.get(i).actionOrder = 2;
        }
    }

    @Override
    public void onBackPressed() {
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
                    case 1: case 2: case 3: case 4:
                        formation[i][j].setVisibility(View.VISIBLE);
                        if(count == id){
                            formation[i][j].setImageResource(R.drawable.red_block);
                            if(allView != null){
                                allView.setVisibility((grid[i][j] > 1)? View.VISIBLE:View.INVISIBLE);
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


