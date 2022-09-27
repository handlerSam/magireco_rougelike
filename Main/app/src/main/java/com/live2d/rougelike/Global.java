package com.live2d.rougelike;

import android.animation.ValueAnimator;
import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.util.Pair;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Global extends Application{
    final public int[] CHARACTER_BREAK_THROUGH_PRICE = new int[]{1, 1, 2};
    //选中的formation：StartActivity.formationList.get(TeamChooseActivity.usingFormationId)
    final public int CHARACTER_STAR_UP_PRICE = 5;

    //    public  ArrayList<Character> monsterList = new ArrayList<>();
    final public int[] CHARACTER_CHANGE_PLATE_PRICE = new int[]{1, 1, 2, 3, 5, 8};
    final public int[] MEMORIA_LV_UP_PRICE = new int[]{1000, 2000, 4000};
    final public int[] MEMORIA_PURCHASE_PRICE = new int[]{0, 3000, 6000};
    final public int CHARACTER_NORMAL_SIZE = 1200;
    final public int CHARACTER_MAGNIFIED_SIZE = 1200;
    final public int DOPPEL_NEED_MP = 1500;
    final public int PLATE_SHOW = 0;
    final public int SKILL_BAR_SHOW = 1;
    final public int MAGIA_BAR_SHOW = 2;
    final public int TEXT_RED = 0;
    final public int TEXT_BLUE = 1;
    final public int TEXT_GREEN = 2;
    final public int ACCELECOMBO = 0;
    final public int CHARGECOMBO = 1;
    final public int BLASTCOMBO = 2;
    final public int PUELLACOMBO = 3;
    final public float[][] multiChargeTable = new float[][]{
            /*ACCELE伤害*/{1.0f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2.0f, 2.1f, 2.2f, 2.3f, 2.4f, 2.5f, 2.6f, 2.7f, 2.8f, 2.9f, 3.0f},
            /*BLAST伤害*/ {1.0f, 1.4f, 1.7f, 2.0f, 2.3f, 2.5f, 2.7f, 2.9f, 3.1f, 3.3f, 3.5f, 3.7f, 3.9f, 4.1f, 4.3f, 4.5f, 4.6f, 4.7f, 4.8f, 4.9f, 5.0f},
            /*ACCELE MP*/ {1.0f, 1.3f, 1.6f, 1.9f, 2.2f, 2.5f, 2.7f, 2.9f, 3.1f, 3.3f, 3.5f, 3.9f, 4.3f, 4.7f, 5.1f, 5.5f, 6.0f, 6.5f, 7.0f, 7.5f, 8.0f}
    };
    public ArrayList<Formation> formationList = new ArrayList<>();
    public Character[] characters = {null, null, null, null, null};
    public Dictionary<String, Collection> collectionDict = new Hashtable<>();
    public ArrayList<Collection> collectionList = new ArrayList<>();
    public ArrayList<Integer>[] USEDMEMORIA = new ArrayList[3];
    public ArrayList<Character> characterList = new ArrayList<>();
    public ArrayList<Memoria> memoriaBag = new ArrayList<>();
    public ArrayList<BattleInfo> battleInfoList = new ArrayList<>();
    public ArrayList<ExtraMission> extraMissionList = new ArrayList<>();
    public ArrayList<BackgroundImage> DAY_BACKGROUND_IMAGE_LIST = new ArrayList<>();
    public ArrayList<BackgroundImage> DUSK_BACKGROUND_IMAGE_LIST = new ArrayList<>();
    public ArrayList<BackgroundImage> JUNCTION_BACKGROUND_IMAGE_LIST = new ArrayList<>();
    public int[][] mapRandomPoint;
    public int SCREEN_WIDTH = 0;
    public int SCREEN_HEIGHT = 0;
    public int plate_change_time = 0;
    public int griefSeedNumber = 3;
    public int ccNumber = 4000;
    public float gameTime = 7.0f;
    public int PLAYER_ON_MAP_X = 760;// 改动后要调用MapActivity.eventX.clear();MapActivity.eventY.clear();，地图才会更新
    public int PLAYER_ON_MAP_Y = 471;
    public int COST_FOR_SUMMON_ADJUSTMENT_HOUSE = 2000;
    public Dictionary<Integer, ArrayList<String>> ENEMY_RANDOM_BUFF_DICT = new Hashtable();
    public int[] preloadMemoria = {1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1037, 1038, 1039, 1041, 1042, 1043, 1044, 1045, 1046, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1059, 1061, 1062, 1063, 1064, 1065, 1066, 1067, 1068, 1069, 1070, 1071, 1072, 1103, 1105, 1106, 1107, 1112, 1113, 1115, 1117, 1119, 1120, 1121, 1122, 1124, 1126, 1127, 1130, 1131, 1132, 1133, 1134, 1136, 1137, 1138, 1140, 1142, 1144, 1145, 1146, 1151, 1152, 1154, 1155, 1156, 1160, 1161, 1162, 1163, 1164, 1166, 1167, 1168, 1169, 1171, 1174, 1176, 1177, 1179, 1180, 1182, 1186, 1187, 1188, 1189, 1192, 1193, 1195, 1196, 1197, 1199, 1200, 1202, 1207, 1209, 1210, 1214, 1215, 1216, 1218, 1219, 1221, 1222, 1224, 1225, 1226, 1227, 1229, 1230, 1231, 1232, 1234, 1235, 1236, 1237, 1239, 1240, 1241, 1243, 1244, 1246, 1247, 1250, 1251, 1252, 1253, 1255, 1259, 1260, 1261, 1262, 1264, 1265, 1266, 1267, 1268, 1270, 1271, 1272, 1273, 1274, 1277, 1278, 1280, 1283, 1284, 1285, 1286, 1288, 1289, 1290, 1291, 1293, 1294, 1295, 1300, 1301, 1302, 1303, 1304, 1305, 1306, 1307, 1309, 1310, 1311, 1313, 1316, 1317, 1318, 1320, 1321, 1322, 1324, 1326, 1327, 1329, 1330, 1331, 1332, 1334, 1336, 1337, 1339, 1340, 1341, 1342, 1345, 1346, 1347, 1348, 1349, 1350, 1351, 1353, 1354, 1355, 1359, 1360, 1361, 1362, 1363, 1364, 1365, 1367, 1368, 1369, 1370, 1371, 1374, 1376, 1377, 1378, 1379, 1381, 1382, 1383, 1384, 1386, 1387, 1388, 1390, 1391, 1392, 1393, 1394, 1395, 1396, 1397, 1398, 1400, 1401, 1403, 1404, 1405, 1406, 1407, 1409, 1411, 1412, 1417, 1418, 1419, 1420, 1421, 1423, 1424, 1428, 1429, 1430, 1431, 1432, 1434, 1435, 1437, 1438, 1439, 1440, 1441, 1443, 1444, 1445, 1446, 1449, 1450, 1453, 1454, 1456, 1457, 1458, 1460, 1461, 1465, 1466, 1469, 1472, 1473, 1474, 1475, 1476, 1477, 1478, 1479, 1481, 1482, 1483, 1486, 1487, 1489, 1490, 1491, 1492, 1494, 1495, 1496, 1497, 1501, 1502, 1503, 1504, 1505, 1509, 1510, 1511, 1512, 1515, 1516, 1519, 1520, 1521, 1522, 1524, 1525, 1526, 1529, 1530, 1531, 1532, 1534, 1535, 1536, 1537, 1541, 1542, 1543, 1545, 1546, 1547, 1548, 1550, 1551, 1552, 1553, 1555, 1557, 1558, 1559, 1561, 1562, 1564, 1565, 1566, 1568, 1569, 1570, 1571, 1572, 1574, 1575, 1576, 1577, 1578, 1579, 1580, 1582, 1583, 1584, 1585, 1587, 1588, 1590, 1591, 1592, 1593, 1594, 1595, 1597, 1599, 1600, 1601, 1602, 1604, 1605, 1606, 1607, 1609, 1610, 1611, 1612, 1613, 1614, 1616, 1617, 1619, 1620, 1621, 1624, 1625, 1626, 1627, 1629, 1630, 1631, 1632, 1634, 1635, 1636, 1637, 1639, 1640, 1641, 1642, 1643, 1645, 1646, 1647, 1648, 1650, 1651, 1652, 1653, 1655, 1656, 1657, 1659, 1660, 1661, 1663, 1664, 1665, 1666, 1668, 1669, 1670, 1671, 1673, 1674, 1675, 1676, 1677, 1678, 1679, 1681, 1682, 1683, 1684, 1686, 1687, 1688, 1689, 1691, 1692, 1693, 1696, 1697, 1699, 1702, 1703, 1704, 1706, 1707, 1708, 1709, 1712, 1713, 1714, 1715, 1719, 1720, 1723, 1724, 1726, 1728, 1729, 1730, 1731, 1732};
    public ArrayList<Integer> randomEventList = new ArrayList<>();

    // battle activity:
    //用于统计获得某收藏品后胜利的战斗次数
    public int winBattleCount1 = 0;
    public int winBattleCount2 = 0;
    public int DELTA_BETWEEN_ATTACK_AND_DAMAGE = 700;
    public int DELTA_BETWEEN_EFFECT_SHOW = 750;
    public boolean hasTriggerFirstDoppelEvent = false;
    public int[] battleMusicList = new int[]{R.raw.bgm01_anime12_hca, R.raw.bgm01_anime15_hca,
    R.raw.bgm01_battle02_hca, R.raw.bgm01_battle03_hca, R.raw.bgm01_battle04_hca,
    R.raw.bgm01_battle04_hca, R.raw.bgm01_battle06_hca, R.raw.bgm01_battle07_hca};
    int chargeNumber = 0;

    //dialog acticity
    public float ALPHASPEED = 0.15f;
    public float ALPHABACKGROUNDSPEED = 0.05f;
    public int plotFlag = 0; // 只读取flag标志和该标志相同的对话，对话默认flag=0

    //map activity
    public final int EVENT = 1;
    public final int NORMAL_BATTLE = 2;
    public final int BOSS_BATTLE = 3;
    public final int SHOP = 4;
    public final int MODE_NONE = 0;//无操作
    public final int MODE_DRAG = 1;//单指操作
    public final int MODE_SCALE = 2;//双指操作
    public final int EXPLORE_RADIUS = 200;//相对于4096*2048的地图而言
    public final int EVENT_NUMBER = 2;
    public float mapX = 1720;
    public float mapY = 855;
    public boolean isMapSizeTransferred = false;
    public float scaleMultiple = 2.464692f;//缩放倍数
    //医疗中心出口 MapX:1720.0, MapY:855.0, Scale:2.464692
    public boolean isSimpleMap = true;
    //用来记录地图上的事件点，每次移动角色时清空，在加载本activity时会依据其长度而决定是否补充event
    public ArrayList<MapEvent> mpEvent = new ArrayList<>();
    public ArrayList<Pair<Effect, Integer>> effectPool = new ArrayList<>();


    // memoria activity
    public int chooseCharacter = 0;

    public int chooseEquipSlot = -1;

    public boolean isOrderByLV = false;
    public boolean isDesc = true;

    public boolean canSetMemoria = false;
    
    public MediaPlayer mainBGM = null;
    public int musicResourceId = -1;// -1为不在播放音乐，若在播放则为musicResourceId
    public int nextMusicResourceId = -1;//下一个要播放的音乐，是音乐设置唯一的对外接口，当音乐fade完之后会读取这个
    public boolean isInFade = false;


    // teamChoose activity
    public int choseCharacter = -1;
    public int usingFormationId = 0;

    public void clearCharBattleInfo(){
        for(int i = 0; i < characterList.size(); i++){
//            characterList.get(i).diamondNumber = 0;
            characterList.get(i).actionOrder = 2;
        }
    }

    public void setNewBGM(final int fileResourceId){
        nextMusicResourceId = fileResourceId;
        Log.d("Sam","musicId:"+fileResourceId);
        if(!isInFade){
            if(fileResourceId != musicResourceId){
                if(musicResourceId != -1){
                    //说明之前有bgm在放
                    setFade(1.0f,0,1500);
                }else{
                    //说明没有
                    if((mainBGM != null) && (mainBGM.isPlaying())){
                        mainBGM.release();
                        mainBGM = null;
                        musicResourceId = -1;
                    }
                    if(nextMusicResourceId != -1){
                        musicResourceId = nextMusicResourceId;
                        mainBGM = MediaPlayer.create(Global.this, musicResourceId);//用create方法会自动调用prepare不要再自己调用了
                        mainBGM.setVolume(1.0f,1.0f);
                        mainBGM.setLooping(true);
                        mainBGM.start();
                        mainBGM.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                setNewBGM(musicResourceId);
                            }
                        });
                    }

                }
            }
        }
    }

    private void setFade(float from, int to, int duration){
        isInFade = true;
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(((float)animation.getAnimatedValue()) <= 0.1f){
                    if((mainBGM != null) && (mainBGM.isPlaying())){
                        mainBGM.release();
                        mainBGM = null;
                        musicResourceId = -1;
                    }
                    if(nextMusicResourceId != -1){
                        musicResourceId = nextMusicResourceId;
                        mainBGM = MediaPlayer.create(Global.this, musicResourceId);//用create方法会自动调用prepare不要再自己调用了
                        mainBGM.setVolume(1.0f,1.0f);
                        mainBGM.setLooping(true);
                        mainBGM.start();
                        mainBGM.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                setNewBGM(musicResourceId);
                            }
                        });
                    }
                    isInFade = false;
                    animation.cancel();
                }else{
                    try{//mediaOnplay记录是哪个mediaplayer
                        mainBGM.setVolume((float)animation.getAnimatedValue(),(float)animation.getAnimatedValue());
                    }catch(Exception e){
                        animation.cancel();
                    }
                }
            }
        });
        animator.start();
    }

    public void cancelBGM(){
//        if((mainBGM != null) && (mainBGM.isPlaying())){
//            mainBGM.release();
//            mainBGM = null;
//            musicResourceId = -1;
//        }
        if(musicResourceId != -1){
            //说明之前有bgm在放
            nextMusicResourceId = -1;
            Log.d("Sam","musicId:"+nextMusicResourceId);
            if(!isInFade){
                setFade(1.0f,0,1500);
            }
        }
    }
}
