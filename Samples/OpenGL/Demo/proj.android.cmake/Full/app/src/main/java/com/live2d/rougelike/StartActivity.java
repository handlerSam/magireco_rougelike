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


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

//    public static ArrayList<Character> monsterList = new ArrayList<>();

    public static ArrayList<Collection> collectionList = new ArrayList<>();

    public int[] preloadMemoria = {1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1037, 1038, 1039, 1041, 1042, 1043, 1044, 1045, 1046, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1059, 1061, 1062, 1063, 1064, 1065, 1066, 1067, 1068, 1069, 1070, 1071, 1072, 1103, 1105, 1106, 1107, 1112, 1113, 1115, 1117, 1119, 1120, 1121, 1122, 1124, 1126, 1127, 1130, 1131, 1132, 1133, 1134, 1136, 1137, 1138, 1140, 1142, 1144, 1145, 1146, 1151, 1152, 1154, 1155, 1156, 1160, 1161, 1162, 1163, 1164, 1166, 1167, 1168, 1169, 1171, 1174, 1176, 1177, 1179, 1180, 1182, 1186, 1187, 1188, 1189, 1192, 1193, 1195, 1196, 1197, 1199, 1200, 1202, 1207, 1209, 1210, 1214, 1215, 1216, 1218, 1219, 1221, 1222, 1224, 1225, 1226, 1227, 1229, 1230, 1231, 1232, 1234, 1235, 1236, 1237, 1239, 1240, 1241, 1243, 1244, 1246, 1247, 1250, 1251, 1252, 1253, 1255, 1259, 1260, 1261, 1262, 1264, 1265, 1266, 1267, 1268, 1270, 1271, 1272, 1273, 1274, 1277, 1278, 1280, 1283, 1284, 1285, 1286, 1288, 1289, 1290, 1291, 1293, 1294, 1295, 1300, 1301, 1302, 1303, 1304, 1305, 1306, 1307, 1309, 1310, 1311, 1313, 1316, 1317, 1318, 1320, 1321, 1322, 1324, 1326, 1327, 1329, 1330, 1331, 1332, 1334, 1336, 1337, 1339, 1340, 1341, 1342, 1345, 1346, 1347, 1348, 1349, 1350, 1351, 1353, 1354, 1355, 1359, 1360, 1361, 1362, 1363, 1364, 1365, 1367, 1368, 1369, 1370, 1371, 1374, 1376, 1377, 1378, 1379, 1381, 1382, 1383, 1384, 1386, 1387, 1388, 1390, 1391, 1392, 1393, 1394, 1395, 1396, 1397, 1398, 1400, 1401, 1403, 1404, 1405, 1406, 1407, 1409, 1411, 1412, 1417, 1418, 1419, 1420, 1421, 1423, 1424, 1428, 1429, 1430, 1431, 1432, 1434, 1435, 1437, 1438, 1439, 1440, 1441, 1443, 1444, 1445, 1446, 1449, 1450, 1453, 1454, 1456, 1457, 1458, 1460, 1461, 1465, 1466, 1469, 1472, 1473, 1474, 1475, 1476, 1477, 1478, 1479, 1481, 1482, 1483, 1486, 1487, 1489, 1490, 1491, 1492, 1494, 1495, 1496, 1497, 1501, 1502, 1503, 1504, 1505, 1509, 1510, 1511, 1512, 1515, 1516, 1519, 1520, 1521, 1522, 1524, 1525, 1526, 1529, 1530, 1531, 1532, 1534, 1535, 1536, 1537, 1541, 1542, 1543, 1545, 1546, 1547, 1548, 1550, 1551, 1552, 1553, 1555, 1557, 1558, 1559, 1561, 1562, 1564, 1565, 1566, 1568, 1569, 1570, 1571, 1572, 1574, 1575, 1576, 1577, 1578, 1579, 1580, 1582, 1583, 1584, 1585, 1587, 1588, 1590, 1591, 1592, 1593, 1594, 1595, 1597, 1599, 1600, 1601, 1602, 1604, 1605, 1606, 1607, 1609, 1610, 1611, 1612, 1613, 1614, 1616, 1617, 1619, 1620, 1621, 1624, 1625, 1626, 1627, 1629, 1630, 1631, 1632, 1634, 1635, 1636, 1637, 1639, 1640, 1641, 1642, 1643, 1645, 1646, 1647, 1648, 1650, 1651, 1652, 1653, 1655, 1656, 1657, 1659, 1660, 1661, 1663, 1664, 1665, 1666, 1668, 1669, 1670, 1671, 1673, 1674, 1675, 1676, 1677, 1678, 1679, 1681, 1682, 1683, 1684, 1686, 1687, 1688, 1689, 1691, 1692, 1693, 1696, 1697, 1699, 1702, 1703, 1704, 1706, 1707, 1708, 1709, 1712, 1713, 1714, 1715, 1719, 1720, 1723, 1724, 1726, 1728, 1729, 1730, 1731, 1732};

    public static ArrayList<Integer>[] USEDMEMORIA = new ArrayList[3];

    public static ArrayList<Character> characterList = new ArrayList<>();

    public static ArrayList<Memoria> memoriaBag = new ArrayList<>();

    public static ArrayList<BattleInfo> battleInfoList = new ArrayList<>();

    public static int[][] mapRandomPoint;

    public static int SCREEN_WIDTH = 0;

    public static int SCREEN_HEIGHT = 0;

    final public static int[] CHARACTER_BREAK_THROUGH_PRICE = new int[]{1,1,2};

    final public static int CHARACTER_STAR_UP_PRICE = 5;

    final public static int[] CHARACTER_CHANGE_PLATE_PRICE = new int[]{1,1,2,3,5,8};

    final public static int[] MEMORIA_LV_UP_PRICE = new int[]{1000,2000,4000};

    public static int plate_change_time = 0;

    public static int griefSeedNumber = 15;

    public static int ccNumber = 40000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        SCREEN_HEIGHT = Math.min(metric.widthPixels,metric.heightPixels);  // 屏幕宽度（像素）
        SCREEN_WIDTH = Math.max(metric.widthPixels,metric.heightPixels);  // 屏幕高度（像素）
        JniBridgeJava.setScreenSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        Log.d("sam","screenWidth:"+SCREEN_WIDTH+", Height:"+SCREEN_HEIGHT);

        initMemoria();
        initCollection();
        initMapRandomPoint();
        initCharacterList();

        initBattleInfoList();

        initFormation();

        //Intent intent1 = new Intent(StartActivity.this, TeamChooseActivity.class);
        //intent1.putExtra("battleInfo",0);
        //startActivity(intent1);
        //finish();
        //overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

        Intent intent1 = new Intent(StartActivity.this, MapActivity.class);
        startActivity(intent1);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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
        toca.lv = 100;
        toca.star = 5;
        toca.HP = 21019;
        toca.realHP = toca.HP;
        toca.ATK = 9803;
        toca.DEF = 6072;
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

        characterList.get(1).isLeader = true;

//        characterList.get(0).setMemoria(0,memoriaBag.get(0));
//        characterList.get(0).setMemoria(1,memoriaBag.get(1));
//        characterList.get(1).setMemoria(0,memoriaBag.get(2));

//        memoriaBag.get(0).setCarrier(0);
//        memoriaBag.get(1).setCarrier(0);
//        memoriaBag.get(2).setCarrier(1);

        characters[1] = characterList.get(0);
        characters[2] = characterList.get(2);
        characters[3] = characterList.get(1);
    }

    public void initBattleInfoList(){
        initMonsterList();

        initBossList();
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
        monster1.plateList = new int[]{BLAST_HORIZONTAL,BLAST_VERTICAL,BLAST_HORIZONTAL,BLAST_VERTICAL,CHARGE};
        monster1.initialEffectList.add(new Effect("攻击时给予状态雾",25,999,50,1));
        monster1.initialEffectList.add(new Effect("攻击时给予状态诅咒",15,999,40,1));
        monster1.initialEffectList.add(new Effect("回避",0,999,20,0));
        bi.monsterList.add(monster1);

        bi.monsterFormation[1][1] = monster1;
        bi.monsterFormation[0][1] = monster1;
        bi.monsterFormation[2][1] = monster1;
        bi.monsterFormation[1][0] = monster1;
        bi.monsterFormation[1][2] = monster1;
        battleInfoList.add(bi);
    }

    public void initMonsterList(){
        BattleInfo bi = new BattleInfo();
        bi.isBossBattle = false;
        Character monster1 = new Character();
        monster1.element = "tree";
        monster1.spriteName = "monster_不幸猫头鹰之谣";
        monster1.name = "不幸猫头鹰之谣";
        monster1.lv = 80;
        monster1.HP = 90000;
        monster1.realHP = monster1.HP;
        monster1.ATK = 10000;
        monster1.DEF = 3000;
        monster1.mpAttackRatio = 0f;
        monster1.mpDefendRatio = 0f;
        monster1.plateList = new int[]{CHARGE,CHARGE,BLAST_HORIZONTAL,BLAST_VERTICAL,CHARGE};
        //monster1.initialEffectList.add(new Effect("攻击时给予状态雾",25,999,100,1));
        //monster1.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        monster1.initialEffectList.add(new Effect("回避",0,999,20,0));
        bi.monsterList.add(monster1);
        bi.monsterFormation[0][0] = monster1;

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
        monster2.initialEffectList.add(new Effect("攻击时给予状态烧伤",5,999,20,1));
        //monster2.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        bi.monsterList.add(monster2);
        bi.monsterFormation[1][0] = monster2;

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
        monster3.initialEffectList.add(new Effect("攻击时给予状态眩晕",0,999,20,2));
        //monster3.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        bi.monsterList.add(monster3);
        bi.monsterFormation[1][2] = monster3;

        Character monster4 = new Character();
        monster4.element = "light";
        monster4.spriteName = "monster_螯合吉祥物之谣";
        monster4.name = "螯合吉祥物之谣";
        monster4.lv = 80;
        monster4.HP = 150000;
        monster4.realHP = monster4.HP;
        monster4.ATK = 10000;
        monster4.DEF = 3000;
        monster4.mpAttackRatio = 0f;
        monster4.mpDefendRatio = 0f;
        monster4.plateList = new int[]{CHARGE,CHARGE,BLAST_HORIZONTAL,BLAST_VERTICAL,CHARGE};
        monster4.initialEffectList.add(new Effect("攻击时给予状态幻惑",50,999,20,1));
        //monster4.initialEffectList.add(new Effect("攻击时给予状态诅咒",0,999,100,1));
        bi.monsterList.add(monster4);
        bi.monsterFormation[2][0] = monster4;
        battleInfoList.add(bi);
    }

    public void initFormation(){
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

    }

    public void initMemoria(){
        for(int i = 0; i < 3; i++){
            USEDMEMORIA[i] = new ArrayList<>();
        }
        //把preloadMemoria中的记忆按照星级分到USEDMEMORIA中
        for(int i = 0; i < preloadMemoria.length; i++){
            int id = preloadMemoria[i];
            Memoria m = new Memoria(""+id,StartActivity.this);
            USEDMEMORIA[m.star-2].add(id);
        }

        //把所有记忆都加载到背包中
        for(int i = 0; i < USEDMEMORIA.length; i++){
            for(int j = 0; j < USEDMEMORIA[i].size(); j++){
                Memoria m = new Memoria(""+USEDMEMORIA[i].get(j),StartActivity.this);
                m.setBreakthrough(0);
                m.setLv(m.lvNow);
                memoriaBag.add(m);
            }
        }

        //随机删除记忆
        while(memoriaBag.size() >= 16){
            int tempId = (int)(Math.random()*memoriaBag.size());
            memoriaBag.remove(tempId);
        }
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
                collectionList.add(c);
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
            mapRandomPoint = new int[efList.length()][2];
            for(int i = 0; i < efList.length(); i++){
                mapRandomPoint[i][0] = efList.getJSONArray(i).getInt(0);
                mapRandomPoint[i][1] = efList.getJSONArray(i).getInt(1);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
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

class BattleInfo{
    ArrayList<Character> monsterList = new ArrayList<>();
    Character[][] monsterFormation = new Character[3][3];
    boolean isBossBattle;
    public BattleInfo(){}
}

class Collection{
    public String name;
    public String description;
    public String effectDescription;
    public String icon;
    public int price;
    public boolean isOwn = false;
    public Collection(String name, String description, String effectDescription, String icon, int price){
        this.name = name;
        this.description = description;
        this.effectDescription = effectDescription;
        this.icon = icon;
        this.price = price;
    }
    public Collection(){

    }
}