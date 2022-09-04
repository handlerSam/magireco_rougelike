package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.customview.widget.ViewDragHelper;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements View.OnTouchListener {

    private int screenHeight;
    private int screenWidth;
    public static float mapX = 0;
    public static float mapY = 0;
    private boolean isInit = false;
    public static boolean isMapSizeTransferred = false;

    private ConstraintLayout kamihamaMap;
    private ConstraintLayout events_layout;
    TextView cc_number;
    TextView grief_seed_number;

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
    }

    public void initView(){

        kamihamaMap.setOnTouchListener(this);

        updateCCAndGriefSeedView();



        if(!isMapSizeTransferred){
            for(int i = 0; i <StartActivity.mapRandomPoint.length; i++){
                StartActivity.mapRandomPoint[i][0] = (int)(1.0f*StartActivity.mapRandomPoint[i][0]/4096.0f*screenWidth);
                StartActivity.mapRandomPoint[i][1] = (int)(1.0f*StartActivity.mapRandomPoint[i][1]/2048.0f*screenHeight);
            }
            isMapSizeTransferred = true;
        }

        //随机选择初始地点
        int temp = (int)(Math.random()*StartActivity.mapRandomPoint.length);
        leaderX = StartActivity.mapRandomPoint[temp][0];
        leaderY = StartActivity.mapRandomPoint[temp][1];
        Log.d("Sam", "leaderX:"+leaderX + ", leaderY:"+leaderY);

        //初始化角色小人
        initLeader();

        //为角色小人一定范围内选择事件
        initSurrendingEvents();

        //切换地图到以人物为中心
        ViewHelper.setX(kamihamaMap, mapX);
        ViewHelper.setY(kamihamaMap, mapY);
        ViewHelper.setScaleX(kamihamaMap, scaleMultiple);// x方向上缩放
        ViewHelper.setScaleY(kamihamaMap, scaleMultiple);// y方向上缩放

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
            leader.spriteName = "mini";
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
        ArrayList<Integer> eventXList = new ArrayList<>();
        ArrayList<Integer> eventYList = new ArrayList<>();

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
                    for(int j = 0; j < eventXList.size(); j++){
                        int tempJx = eventXList.get(j);
                        int tempJy = eventYList.get(j);
                        if(Math.pow(tempX-tempJx,2)+Math.pow(tempY-tempJy,2) <= Math.pow(32,2)){
                            isOverlap = true;
                            break;
                        }
                    }
                    if(!isOverlap){
                        eventXList.add(tempX);
                        eventYList.add(tempY);
                        Log.d("Sam","addEventPoint:"+tempX+","+tempY);
                    }
                }
            }
        }

        //筛选点
        while(eventXList.size() > EVENT_NUMBER){
            int randomId = (int)(Math.random()*eventXList.size());
            eventXList.remove(randomId);
            eventYList.remove(randomId);
        }

        //生成筛选后的点
        for(int i = 0; i < eventXList.size(); i++){
            ImageView ep = new ImageView(this);
            ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(25,32);
            ep.setLayoutParams(p);
            ep.setId(View.generateViewId());
            events_layout.addView(ep);
            ConstraintSet sampleSet = new ConstraintSet();
            sampleSet.clone(events_layout);
            sampleSet.connect(ep.getId(),ConstraintSet.START,events_layout.getId(),ConstraintSet.START,eventXList.get(i)-12);
            sampleSet.connect(ep.getId(),ConstraintSet.TOP,events_layout.getId(),ConstraintSet.TOP,eventYList.get(i)-32);
            sampleSet.applyTo(events_layout);

            //设置event内容
            if(i == 0){
                //对话
                ep.setBackgroundResource(R.drawable.map_mark_event);
                ep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(MapActivity.this, DialogActivity.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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
                        Intent intent1 = new Intent(MapActivity.this, AdjustmentHouseActivity.class);
                        startActivity(intent1);
                        finish();
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });
            }
        }
    }
}