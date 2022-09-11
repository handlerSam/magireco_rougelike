package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

public class MapActivity extends AppCompatActivity implements View.OnTouchListener {

    private int screenHeight;
    private int screenWidth;
    public static float mapX = 0;
    public static float mapY = 0;
    private boolean isInit = false;
    public static boolean isMapSizeTransferred = false;

    private ConstraintLayout kamihamaMap;
    private ConstraintLayout events_layout;

    ConstraintLayout black_mask;

    TextView cc_number;
    TextView grief_seed_number;
    ImageView shop_button;
    ImageView team_button;
    ImageView item_button;
    RecyclerView collection_recycler_view;
    ConstraintLayout collection_list_background;
    TextView no_item_text_view;
    ImageView cancel_button;


    private SpriteViewer leader;

    //private ViewDragHelper mDragHelper;
//    private float scale = 1;

    private int MODE;//当前状态
    public static final int MODE_NONE = 0;//无操作
    public static final int MODE_DRAG = 1;//单指操作
    public static final int MODE_SCALE = 2;//双指操作

    public static final int EXPLORE_RADIUS = 200;//相对于4096*2048的地图而言

    public static final int EVENT_NUMBER = 4;

    private PointF startPointF = new PointF();//初始坐标
    private float distance;//初始距离
    public static float scaleMultiple = 1;//缩放倍数

    private int leaderX = 0;
    private int leaderY = 0;

    //用来记录地图上的事件点，每次移动角色时清空，在加载本activity时会依据其长度而决定是否补充event
    public static ArrayList<Integer> eventX = new ArrayList<>();
    public static ArrayList<Integer> eventY = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        findView();
        //mDragHelper = ViewDragHelper.create((ViewGroup)(findViewById(R.id.rootActivity)),1.0f, callback);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        screenHeight = kamihamaMap.getMeasuredHeight();
        screenWidth = kamihamaMap.getMeasuredWidth();
        Log.d("Sam","mapHeight:"+screenHeight+", mapWidth:"+screenWidth);
        if(!isInit){
            initView();
            isInit = true;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Log.d("Sam", "pivotX:"+ViewHelper.getPivotX(kamihamaMap) + "pivotY:"+ViewHelper.getPivotY(kamihamaMap));

        switch (event.getAction()&event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN://单指触碰
                //起始矩阵先获取ImageView的当前状态
                //获取起始坐标
                startPointF.set(event.getX(), event.getY());
                mapX = ViewHelper.getX(kamihamaMap);
                mapY = ViewHelper.getY(kamihamaMap);
                //此时状态是单指操作
                MODE = MODE_DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN://双指触碰
                //最后的状态传给起始状态
                //获取距离
                distance = getDistance(event);
                mapX = ViewHelper.getX(kamihamaMap);
                mapY = ViewHelper.getY(kamihamaMap);
                //状态改为双指操作
                MODE = MODE_SCALE;
                break;
            case MotionEvent.ACTION_MOVE://滑动（单+双）
                if (MODE == MODE_DRAG) {//单指滑动时
                    mapX += (int)(event.getX() - startPointF.x);
                    mapY += (int)(event.getY() - startPointF.y);
                    ViewHelper.setX(kamihamaMap, mapX);
                    ViewHelper.setY(kamihamaMap, mapY);
                }else if (MODE == MODE_SCALE) {//双指滑动时
                    //计算缩放倍数
                    scaleMultiple = scaleMultiple * getDistance(event)/distance;
                    ViewHelper.setScaleX(kamihamaMap, scaleMultiple);// x方向上缩放
                    ViewHelper.setScaleY(kamihamaMap, scaleMultiple);// y方向上缩放
                }
                break;
            case MotionEvent.ACTION_UP://单指离开
            case MotionEvent.ACTION_POINTER_UP://双指离开
                //防止缩放过度
                if(scaleMultiple > 3){
                    scaleMultiple = 3;
                    ViewHelper.setScaleX(kamihamaMap, scaleMultiple);// x方向上缩放
                    ViewHelper.setScaleY(kamihamaMap, scaleMultiple);// y方向上缩放
                }else if(scaleMultiple < 1){
                    scaleMultiple = 1;
                    ViewHelper.setScaleX(kamihamaMap, scaleMultiple);// x方向上缩放
                    ViewHelper.setScaleY(kamihamaMap, scaleMultiple);// y方向上缩放
                    mapX = 0;
                    mapY = 0;
                    ViewHelper.setX(kamihamaMap, mapX);
                    ViewHelper.setY(kamihamaMap, mapY);
                }

                //防止过度移动, 图片不能移出屏幕的1/4
                if(mapX > ((screenWidth*scaleMultiple)/2 - screenWidth/4.0f)){
                    mapX = (screenWidth*scaleMultiple)/2 - screenWidth/4.0f;
                }else if(mapX < -screenWidth*scaleMultiple/2 + screenWidth/4.0f){
                    mapX = -(screenWidth*scaleMultiple)/2 + screenWidth/4.0f;
                }
                if(mapY > (screenHeight*scaleMultiple)/2 - screenHeight/4.0f){
                    mapY = (screenHeight*scaleMultiple)/2 - screenHeight/4.0f;
                }else if(mapY < -screenHeight*scaleMultiple/2 + screenHeight/4.0f){
                    mapY = -(screenHeight*scaleMultiple)/2 + screenHeight/4.0f;
                }
                ViewHelper.setX(kamihamaMap, mapX);
                ViewHelper.setY(kamihamaMap, mapY);
                //手指离开后，重置状态
                MODE = MODE_NONE;
                break;
        }
//        Log.d("Sam","MapX:" + mapX + ", MapY:" + mapY);
        return true;
    }

    //获取距离
    private static float getDistance(MotionEvent event) {//获取两点间距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public void findView(){
        kamihamaMap = findViewById(R.id.kamihamaMap);
        leader = findViewById(R.id.leader);
        events_layout = findViewById(R.id.events_layout);
        cc_number = findViewById(R.id.cc_number);
        grief_seed_number = findViewById(R.id.grief_seed_number);
        black_mask = findViewById(R.id.black_mask);
        shop_button = findViewById(R.id.shop_button);
        team_button = findViewById(R.id.team_button);
        item_button = findViewById(R.id.item_button);
        collection_recycler_view = findViewById(R.id.collection_recycler_view);
        collection_list_background = findViewById(R.id.collection_list_background);
        no_item_text_view = findViewById(R.id.no_item_text_view);
        cancel_button = findViewById(R.id.cancel_button);
    }

    public void initView(){
        kamihamaMap.setOnTouchListener(this);

        updateCCAndGriefSeedView();

        black_mask.setVisibility(View.VISIBLE);

        if(!isMapSizeTransferred){
            for(int i = 0; i <StartActivity.mapRandomPoint.length; i++){
                StartActivity.mapRandomPoint[i][0] = (int)(1.0f*StartActivity.mapRandomPoint[i][0]/4096.0f*screenWidth);
                StartActivity.mapRandomPoint[i][1] = (int)(1.0f*StartActivity.mapRandomPoint[i][1]/2048.0f*screenHeight);
            }
            StartActivity.PLAYER_ON_MAP_X = (int)(1.0f * StartActivity.PLAYER_ON_MAP_X / 4096.0f * screenWidth);
            StartActivity.PLAYER_ON_MAP_Y = (int)(1.0f * StartActivity.PLAYER_ON_MAP_Y / 2048.0f * screenHeight);
            isMapSizeTransferred = true;
        }

        //初始地点
        //int temp = (int)(Math.random()*StartActivity.mapRandomPoint.length);
        leaderX = StartActivity.PLAYER_ON_MAP_X;
        leaderY = StartActivity.PLAYER_ON_MAP_Y;
        Log.d("Sam", "leaderX:"+leaderX + ", leaderY:"+leaderY);

        //初始化角色小人
        initLeader();

        //为角色小人一定范围内选择事件
        initSurrendingEvents();

        //切换地图到上次地点
        ViewHelper.setX(kamihamaMap, mapX);
        ViewHelper.setY(kamihamaMap, mapY);
        ViewHelper.setScaleX(kamihamaMap, scaleMultiple);// x方向上缩放
        ViewHelper.setScaleY(kamihamaMap, scaleMultiple);// y方向上缩放

        //查看队伍
        team_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MapActivity.this, TeamChooseActivity.class);
                intent1.putExtra("battleInfo",-1);
                startActivity(intent1);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        //进入商店
        shop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MapActivity.this);
                dialog.setMessage("是否花费 "+ StartActivity.COST_FOR_SUMMON_ADJUSTMENT_HOUSE + "cc 购买调整屋的上门服务？\n(当前地图事件会更新)");//正文
                dialog.setCancelable(true);//是否能点击屏幕取消该弹窗
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //正确逻辑
                        StartActivity.ccNumber -= StartActivity.COST_FOR_SUMMON_ADJUSTMENT_HOUSE;
                        eventX.clear();
                        eventY.clear();

                        Intent intent1 = new Intent(MapActivity.this, AdjustmentHouseActivity.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(0,android.R.anim.fade_out);
                    }});
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //错误逻辑
                    }});
                dialog.show();

            }
        });

        //查看道具
        MapCollectionAdapter mapCollectionAdapter = new MapCollectionAdapter(MapActivity.this, StartActivity.collectionList);
        collection_recycler_view.setAdapter(mapCollectionAdapter);
        StaggeredGridLayoutManager m = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        collection_recycler_view.setLayoutManager(m);
        collection_list_background.setVisibility(View.GONE);
        item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collection_list_background.setVisibility(View.VISIBLE);
            }
        });
        collection_list_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collection_list_background.setVisibility(View.GONE);
            }
        });
    }

    void updateCCAndGriefSeedView(){
        cc_number.setText(" "+StartActivity.ccNumber+" ");
        grief_seed_number.setText(" "+StartActivity.griefSeedNumber+" ");
    }

    public void initLeader(){
        Character temp = null;
        for(int i = 0; i < StartActivity.characters.length; i++){
            if(StartActivity.characters[i] != null && StartActivity.characters[i].isLeader){
                temp = StartActivity.characters[i];
                break;
            }
        }
        leader.charName = temp.spriteName;
        if(temp.spriteName.equals("Satomi Touka")){
            leader.spriteName = "stance";// 灯花打伞好看
        }else{
            leader.spriteName = "wait";
        }

        leader.prefix = "mini_";
        //leader.canvasWidth = 150;
        //leader.webView.loadUrl("javascript:setCanvasWidth("+ leader.canvasWidth +")");
        //leader.webView.loadUrl("javascript:changeCharacter()");

        ConstraintSet sampleSet = new ConstraintSet();
        sampleSet.clone(kamihamaMap);
        sampleSet.connect(leader.getId(),ConstraintSet.START,kamihamaMap.getId(),ConstraintSet.START,leaderX-40);//人物view宽80
        sampleSet.connect(leader.getId(),ConstraintSet.TOP,kamihamaMap.getId(),ConstraintSet.TOP,leaderY-110);//人物view长110
        sampleSet.applyTo(kamihamaMap);
        leader.setVisibility(View.VISIBLE);
        //leader.resetCharacter();
        //leader.webView.loadUrl("javascript:changeSprite()");

    }

    public void initSurrendingEvents(){

        if(eventX.size() < EVENT_NUMBER){
            //选择所有可能的点
            for(int i = 0; i < StartActivity.mapRandomPoint.length; i++){
                //1.在leader的一定距离范围内
                int tempX = StartActivity.mapRandomPoint[i][0];
                int tempY = StartActivity.mapRandomPoint[i][1];
                if(Math.pow(tempX-leaderX,2)+Math.pow(tempY-leaderY,2) <= EXPLORE_RADIUS*EXPLORE_RADIUS){
                    //2.不能在leader的图像范围内 80*110 与 25*32
                    if(!(Math.pow(tempX-leaderX,2)+Math.pow(tempY-16-leaderY+55,2) <= Math.pow(80,2))){
                        //if(!(Math.abs(tempX-12-leaderX) <= 40 && Math.abs(tempY-16-leaderY+55) <= 55)){
                        //3.不能和已经触发的点图像重叠 25*32
                        boolean isOverlap = false;
                        for(int j = 0; j < eventX.size(); j++){
                            int tempJx = eventX.get(j);
                            int tempJy = eventY.get(j);
                            if(Math.pow(tempX-tempJx,2)+Math.pow(tempY-tempJy,2) <= Math.pow(32,2)){
                                isOverlap = true;
                                break;
                            }
                        }
                        if(!isOverlap){
                            eventX.add(tempX);
                            eventY.add(tempY);
                            Log.d("Sam","addEventPoint:"+tempX+","+tempY);
                        }
                    }
                }
            }

            //筛选点
            while(eventX.size() > EVENT_NUMBER){
                int randomId = (int)(Math.random()*eventX.size());
                eventX.remove(randomId);
                eventY.remove(randomId);
            }
        }


        //生成筛选后的点
        for(int i = 0; i < eventX.size(); i++){
            final ImageView ep = new ImageView(this);
            ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(25,32);
            ep.setLayoutParams(p);
            ep.setId(View.generateViewId());
            events_layout.addView(ep);
            ConstraintSet sampleSet = new ConstraintSet();
            sampleSet.clone(events_layout);
            sampleSet.connect(ep.getId(),ConstraintSet.START,events_layout.getId(),ConstraintSet.START,eventX.get(i)-12);
            sampleSet.connect(ep.getId(),ConstraintSet.TOP,events_layout.getId(),ConstraintSet.TOP,eventY.get(i)-32);
            sampleSet.applyTo(events_layout);

            //设置event内容
            final int tempI = i;
            if(i == 0){
                //对话
                ep.setBackgroundResource(R.drawable.map_mark_event);
                ep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartActivity.PLAYER_ON_MAP_X = eventX.get(tempI);
                        StartActivity.PLAYER_ON_MAP_Y = eventY.get(tempI);
                        MapActivity.eventX.clear();
                        MapActivity.eventY.clear();

                        Intent intent1 = new Intent(MapActivity.this, DialogActivity.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(0,android.R.anim.fade_out);
                    }
                });
            }else if(i == 1){
                //普通战斗
                ep.setBackgroundResource(R.drawable.map_mark_battle);
                ep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(MapActivity.this, TeamChooseActivity.class);
                        intent1.putExtra("battleInfo",0);
                        intent1.putExtra("extraMissionId", (int)(Math.random()*StartActivity.extraMissionList.size()));
                        intent1.putExtra("eventX",eventX.get(tempI));
                        intent1.putExtra("eventY",eventY.get(tempI));
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });
            }else if(i == 2){
                //boss战斗
                ep.setBackgroundResource(R.drawable.map_mark_emergent_battle);
                ep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(MapActivity.this, TeamChooseActivity.class);
                        intent1.putExtra("battleInfo",1);
                        intent1.putExtra("extraMission", (int)(Math.random()*StartActivity.extraMissionList.size()));
                        intent1.putExtra("eventX",eventX.get(tempI));
                        intent1.putExtra("eventY",eventY.get(tempI));
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });
            }else if(i == 3){
                //商店
                ep.setBackgroundResource(R.drawable.map_mark_shop);
                ep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StartActivity.PLAYER_ON_MAP_X = eventX.get(tempI);
                        StartActivity.PLAYER_ON_MAP_Y = eventY.get(tempI);
                        MapActivity.eventX.clear();
                        MapActivity.eventY.clear();

                        Intent intent1 = new Intent(MapActivity.this, AdjustmentHouseActivity.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(0,android.R.anim.fade_out);
                    }
                });
            }
        }
    }


    public void generateMonster(boolean isBossBattle){
        ArrayList<Effect> effectPool = new ArrayList<>();
        ArrayList<Integer> effectPoint = new ArrayList<>();

        effectPool.add(new Effect("攻击力UP",15,999,100,0));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击力UP",35,999,100,0));
        effectPoint.add(4);
        effectPool.add(new Effect("攻击力UP",65,999,100,0));
        effectPoint.add(8);

        effectPool.add(new Effect("攻击力DOWN",20,999,100,0));
        effectPoint.add(-3);
        effectPool.add(new Effect("攻击力DOWN",40,999,100,0));
        effectPoint.add(-5);

        effectPool.add(new Effect("防御力UP",15,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("防御力UP",35,999,100,0));
        effectPoint.add(2);
        effectPool.add(new Effect("防御力UP",65,999,100,0));
        effectPoint.add(4);

        effectPool.add(new Effect("防御力DOWN",20,999,100,0));
        effectPoint.add(-2);
        effectPool.add(new Effect("防御力DOWN",40,999,100,0));
        effectPoint.add(-4);

        effectPool.add(new Effect("造成伤害UP",20,999,100,0));
        effectPoint.add(2);
        effectPool.add(new Effect("造成伤害UP",40,999,100,0));
        effectPoint.add(4);
        effectPool.add(new Effect("造成伤害UP",70,999,100,0));
        effectPoint.add(8);

        effectPool.add(new Effect("造成伤害DOWN",25,999,100,0));
        effectPoint.add(-3);
        effectPool.add(new Effect("造成伤害DOWN",45,999,100,0));
        effectPoint.add(-5);

        effectPool.add(new Effect("伤害削减",20,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("伤害削减",40,999,100,0));
        effectPoint.add(2);
        effectPool.add(new Effect("伤害削减",70,999,100,0));
        effectPoint.add(4);

        effectPool.add(new Effect("Magia伤害削减",40,999,100,0));
        effectPoint.add(2);
        effectPool.add(new Effect("Magia伤害削减",70,999,100,0));
        effectPoint.add(4);

        effectPool.add(new Effect("异常状态耐性UP",20,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("异常状态耐性UP",40,999,100,0));
        effectPoint.add(2);
        effectPool.add(new Effect("异常状态耐性UP",90,999,100,0));
        effectPoint.add(3);

        effectPool.add(new Effect("异常状态耐性DOWN",15,999,100,0));
        effectPoint.add(-1);
        effectPool.add(new Effect("异常状态耐性DOWN",35,999,100,0));
        effectPoint.add(-2);

        effectPool.add(new Effect("濒死时防御力UP",20,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("濒死时防御力UP",40,999,100,0));
        effectPoint.add(2);
        effectPool.add(new Effect("濒死时防御力UP",90,999,100,0));
        effectPoint.add(4);

        effectPool.add(new Effect("濒死时攻击力UP",20,999,100,0));
        effectPoint.add(2);
        effectPool.add(new Effect("濒死时攻击力UP",40,999,100,0));
        effectPoint.add(3);
        effectPool.add(new Effect("濒死时攻击力UP",90,999,100,0));
        effectPoint.add(6);

        effectPool.add(new Effect("HP自动回复",3,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("HP自动回复",5,999,100,0));
        effectPoint.add(3);
        effectPool.add(new Effect("HP自动回复",10,999,100,0));
        effectPoint.add(5);

        effectPool.add(new Effect("攻击时给予状态雾",25,999,15,2));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态雾",25,999,35,3));
        effectPoint.add(5);
        effectPool.add(new Effect("攻击时给予状态雾",25,999,65,4));
        effectPoint.add(7);

        effectPool.add(new Effect("攻击时给予状态黑暗",35,999,15,2));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态黑暗",35,999,35,3));
        effectPoint.add(5);
        effectPool.add(new Effect("攻击时给予状态黑暗",35,999,85,4));
        effectPoint.add(8);

        effectPool.add(new Effect("攻击时给予状态幻惑",50,999,15,2));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态幻惑",50,999,35,3));
        effectPoint.add(5);
        effectPool.add(new Effect("攻击时给予状态幻惑",50,999,75,4));
        effectPoint.add(8);

        effectPool.add(new Effect("攻击时给予状态毒",5,999,25,3));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态毒",5,999,35,4));
        effectPoint.add(4);
        effectPool.add(new Effect("攻击时给予状态毒",5,999,60,5));
        effectPoint.add(6);

        effectPool.add(new Effect("攻击时给予状态烧伤",10,999,20,2));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态烧伤",10,999,35,3));
        effectPoint.add(4);
        effectPool.add(new Effect("攻击时给予状态烧伤",10,999,60,4));
        effectPoint.add(7);

        effectPool.add(new Effect("攻击时给予状态诅咒",15,999,15,2));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态诅咒",15,999,40,4));
        effectPoint.add(5);
        effectPool.add(new Effect("攻击时给予状态诅咒",15,999,60,5));
        effectPoint.add(8);

        effectPool.add(new Effect("攻击时给予状态魅惑",0,999,15,2));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态魅惑",0,999,40,3));
        effectPoint.add(3);
        effectPool.add(new Effect("攻击时给予状态魅惑",0,999,60,3));
        effectPoint.add(5);

        effectPool.add(new Effect("攻击时给予状态眩晕",0,999,15,2));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态眩晕",0,999,40,3));
        effectPoint.add(3);
        effectPool.add(new Effect("攻击时给予状态眩晕",0,999,60,4));
        effectPoint.add(5);

        effectPool.add(new Effect("攻击时给予状态拘束",0,999,15,2));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态拘束",0,999,40,3));
        effectPoint.add(3);
        effectPool.add(new Effect("攻击时给予状态拘束",0,999,60,3));
        effectPoint.add(5);

        effectPool.add(new Effect("攻击时给予状态Magia封印",0,999,15,2));
        effectPoint.add(2);
        effectPool.add(new Effect("攻击时给予状态Magia封印",0,999,40,3));
        effectPoint.add(3);

        effectPool.add(new Effect("攻击时给予状态技能封印",0,999,25,2));
        effectPoint.add(3);
        effectPool.add(new Effect("攻击时给予状态技能封印",0,999,50,3));
        effectPoint.add(4);

        effectPool.add(new Effect("攻击时给予状态HP回复禁止",0,999,30,2));
        effectPoint.add(1);
        effectPool.add(new Effect("攻击时给予状态HP回复禁止",0,999,60,3));
        effectPoint.add(2);

        effectPool.add(new Effect("攻击时给予状态MP回复禁止",0,999,25,2));
        effectPoint.add(3);
        effectPool.add(new Effect("攻击时给予状态MP回复禁止",0,999,50,3));
        effectPoint.add(4);

        effectPool.add(new Effect("挑拨",0,999,25,0));
        effectPoint.add(2);
        effectPool.add(new Effect("挑拨",0,999,50,0));
        effectPoint.add(3);

        effectPool.add(new Effect("回避",0,999,25,0));
        effectPoint.add(3);
        effectPool.add(new Effect("回避",0,999,50,0));
        effectPoint.add(5);
        effectPool.add(new Effect("回避",0,999,80,0));
        effectPoint.add(8);

        effectPool.add(new Effect("暴击",200,999,10,0));
        effectPoint.add(2);
        effectPool.add(new Effect("暴击",200,999,40,0));
        effectPoint.add(4);
        effectPool.add(new Effect("暴击",200,999,80,0));
        effectPoint.add(8);

        effectPool.add(new Effect("回避无效",0,999,40,0));
        effectPoint.add(1);
        effectPool.add(new Effect("回避无效",0,999,100,0));
        effectPoint.add(2);

        effectPool.add(new Effect("毒无效",0,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("挑拨无效",0,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("诅咒无效",0,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("雾无效",0,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("忍耐",0,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("烧伤无效",0,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("黑暗无效",0,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("拘束无效",0,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("眩晕无效",0,999,100,0));
        effectPoint.add(1);
        effectPool.add(new Effect("幻惑无效",0,999,100,0));
        effectPoint.add(1);

        effectPool.add(new Effect("无视防御力",0,999,40,0));
        effectPoint.add(4);
        effectPool.add(new Effect("无视防御力",0,999,80,0));
        effectPoint.add(8);

        effectPool.add(new Effect("伤害削减无效",0,999,50,0));
        effectPoint.add(2);
        effectPool.add(new Effect("伤害削减无效",0,999,100,0));
        effectPoint.add(4);

        
        if(StartActivity.gameTime < 10.01f){
            //阶段1
            if(isBossBattle){

            }
        }else if(StartActivity.gameTime < 14.01f){

        }else{

        }
    }
}