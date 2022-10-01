package com.live2d.rougelike;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.view.View.VISIBLE;
import static com.live2d.rougelike.CharacterPlateView.ACCELE;
import static com.live2d.rougelike.CharacterPlateView.BLAST_HORIZONTAL;
import static com.live2d.rougelike.CharacterPlateView.BLAST_VERTICAL;
import static com.live2d.rougelike.CharacterPlateView.CHARGE;

public class MapActivity extends AppCompatActivity implements View.OnTouchListener{
    
    public boolean isMaskStrip = false;
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
    ImageView leader_replace_image_view;
    ConstraintLayout map_event_detail_frame;
    ConstraintLayout map_event_detail_title_frame;
    TextView map_event_detail_title;
    TextView event_description;
    TextView recommend_lv;
    TextView enemy_number;
    TextView carry_buff;
    ConstraintLayout go_button;
    ConstraintLayout extra_mission_frame;
    TextView extra_mission_text;
    TextView extra_mission_add_number;
    ImageView extra_mission_cc_icon;
    ImageView extra_mission_grief_seed_icon;
    ImageView change_map_button;
    ImageView[] clock_number = new ImageView[4];
    ImageView clock_colon;

    boolean isIntentSend = false;
    ColorMatrixColorFilter grayColorFilter;//用于灰度设置
    private int screenHeight;
    private int screenWidth;
    //private ViewDragHelper mDragHelper;
//    private float scale = 1;
    private boolean isInit = false;
    private ConstraintLayout kamihamaMap;
    private ConstraintLayout events_layout;
    private SpriteViewer leader;
    
    Global global;

    Runnable clockColonControl = new Runnable(){
        @Override
        public void run(){
            clock_colon.setVisibility(clock_colon.getVisibility() == VISIBLE? View.INVISIBLE:VISIBLE);
            handler.postDelayed(this,1000);
        }
    };

    Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(@NonNull Message message){
            //map activity
            if(!isMaskStrip){
                switch(message.what){
                    case 1:
                        leader.setVisibility(View.GONE);
                        leader_replace_image_view.setVisibility(View.VISIBLE);
                    case 0:
                        isMaskStrip = true;
                        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(black_mask, "alpha", 1f, 0);
                        fadeOut.setDuration(500);
                        fadeOut.start();
                        break;
                    default:
                }
            }
            return true;
        }
    });
    private List<String> commonBossNameList = Arrays.asList("monster_钟摆的魔女", "monster_立耳的魔女","monster_Flower Speaker之谣", "monster_沙地的魔女", "monster_羊之魔女", "monster_屋顶的魔女",
//            "monster_生神的魔女", "monster_班长的魔女",
            "monster_保护孩子的魔女",   "monster_玫瑰园的魔女");

    private List<String> commonMonsterNameList = Arrays.asList("monster_立耳的魔女的手下",
            "monster_沙地的魔女的手下", "monster_羊之魔女的手下", "monster_屋顶的魔女的手下",
            "monster_保护孩子的魔女的手下", "monster_生神的魔女的手下", "monster_班长的魔女的手下",
            "monster_玫瑰园的魔女的手下", "monster_幸福的魔女的手下(绿)", "monster_橡胶的魔女的手下",
            "monster_流浪的魔女的手下", "monster_象征的魔女的手下Ⅰ", "monster_象征的魔女的手下Ⅲ(粉)",
            "monster_象征的魔女的手下Ⅳ(黄)", "monster_零食的魔女的手下");
    private String[] specialBossList = new String[]{"monster_绝交阶梯之谣", "monster_待人马之谣", "monster_不幸角杯之谣",
            "monster_无名人工智能之谣", "monster_记忆馆长之谣", "monster_螯合大摩天轮之谣", "monster_熊后之谣", "monster_兵熊之谣"};
    private String[] specialMonsterList = new String[]{"monster_绝交挂锁之谣", "monster_通灵绘马之谣", "monster_不幸猫头鹰之谣",
            "monster_无名邮件之谣(白)", "monster_记忆职员之谣", "monster_螯合吉祥物之谣", "monster_工熊之谣(橘)", "monster_工熊之谣(红)"};
    private Pair<Integer, Integer>[] specialPoint = new Pair[]{new Pair(536, 1054), new Pair(1587, 1198),
            new Pair(2793, 656), new Pair(2194, 958), new Pair(1345, 1368), new Pair(3539, 334), new Pair(2266, 149), new Pair(2266, 149)};
    private int MODE;//当前状态
    private PointF startPointF = new PointF();//初始坐标
    private float distance;//初始距离
    private int leaderX = 0;
    private int leaderY = 0;

    //获取距离
    private static float getDistance(MotionEvent event){//获取两点间距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        global = (Global)getApplicationContext();
        findView();
        if(global.effectPool.size() == 0){
            initEffectPool();
        }
        //mDragHelper = ViewDragHelper.create((ViewGroup)(findViewById(R.id.rootActivity)),1.0f, callback);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        screenHeight = kamihamaMap.getMeasuredHeight();
        screenWidth = kamihamaMap.getMeasuredWidth();
        Log.d("Sam", "mapHeight:" + screenHeight + ", mapWidth:" + screenWidth);

        if(!isInit){
            initView();
            isInit = true;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        //Log.d("Sam", "pivotX:"+ViewHelper.getPivotX(kamihamaMap) + "pivotY:"+ViewHelper.getPivotY(kamihamaMap));

        switch(event.getAction() & event.getActionMasked()){
            case MotionEvent.ACTION_DOWN://单指触碰
                //起始矩阵先获取ImageView的当前状态
                //获取起始坐标
                startPointF.set(event.getX(), event.getY());
                global.mapX = ViewHelper.getX(kamihamaMap);
                global.mapY = ViewHelper.getY(kamihamaMap);
                //此时状态是单指操作
                MODE = global.MODE_DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN://双指触碰
                //最后的状态传给起始状态
                //获取距离
                distance = getDistance(event);
                global.mapX = ViewHelper.getX(kamihamaMap);
                global.mapY = ViewHelper.getY(kamihamaMap);
                //状态改为双指操作
                MODE = global.MODE_SCALE;
                break;
            case MotionEvent.ACTION_MOVE://滑动（单+双）
                map_event_detail_frame.setVisibility(View.GONE);
                if(MODE == global.MODE_DRAG){//单指滑动时
                    global.mapX += (int) (event.getX() - startPointF.x);
                    global.mapY += (int) (event.getY() - startPointF.y);
                    ViewHelper.setX(kamihamaMap, global.mapX);
                    ViewHelper.setY(kamihamaMap, global.mapY);
                }else if(MODE == global.MODE_SCALE){//双指滑动时
                    //计算缩放倍数
                    global.scaleMultiple = global.scaleMultiple * getDistance(event) / distance;
                    ViewHelper.setScaleX(kamihamaMap, global.scaleMultiple);// x方向上缩放
                    ViewHelper.setScaleY(kamihamaMap, global.scaleMultiple);// y方向上缩放
                }
                break;
            case MotionEvent.ACTION_UP://单指离开
            case MotionEvent.ACTION_POINTER_UP://双指离开
                //防止缩放过度
                if(global.scaleMultiple > 3){
                    global.scaleMultiple = 3;
                    ViewHelper.setScaleX(kamihamaMap, global.scaleMultiple);// x方向上缩放
                    ViewHelper.setScaleY(kamihamaMap, global.scaleMultiple);// y方向上缩放
                }else if(global.scaleMultiple < 1){
                    global.scaleMultiple = 1;
                    ViewHelper.setScaleX(kamihamaMap, global.scaleMultiple);// x方向上缩放
                    ViewHelper.setScaleY(kamihamaMap, global.scaleMultiple);// y方向上缩放
                    global.mapX = 0;
                    global.mapY = 0;
                    ViewHelper.setX(kamihamaMap, global.mapX);
                    ViewHelper.setY(kamihamaMap, global.mapY);
                }

                //防止过度移动, 图片不能移出屏幕的1/4
                if(global.mapX > ((screenWidth * global.scaleMultiple) / 2 - screenWidth / 4.0f)){
                    global.mapX = (screenWidth * global.scaleMultiple) / 2 - screenWidth / 4.0f;
                }else if(global.mapX < -screenWidth * global.scaleMultiple / 2 + screenWidth / 4.0f){
                    global.mapX = -(screenWidth * global.scaleMultiple) / 2 + screenWidth / 4.0f;
                }
                if(global.mapY > (screenHeight * global.scaleMultiple) / 2 - screenHeight / 4.0f){
                    global.mapY = (screenHeight * global.scaleMultiple) / 2 - screenHeight / 4.0f;
                }else if(global.mapY < -screenHeight * global.scaleMultiple / 2 + screenHeight / 4.0f){
                    global.mapY = -(screenHeight * global.scaleMultiple) / 2 + screenHeight / 4.0f;
                }
                ViewHelper.setX(kamihamaMap, global.mapX);
                ViewHelper.setY(kamihamaMap, global.mapY);
                //手指离开后，重置状态
                MODE = global.MODE_NONE;
                break;
        }
        Log.d("Sam","MapX:" + global.mapX + ", MapY:" + global.mapY + ", Scale:" + global.scaleMultiple);
        return true;
    }

    @Override
    public void onBackPressed(){
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
        leader_replace_image_view = findViewById(R.id.leader_replace_image_view);
        map_event_detail_frame = findViewById(R.id.map_event_detail_frame);
        map_event_detail_title_frame = findViewById(R.id.map_event_detail_title_frame);
        map_event_detail_title = findViewById(R.id.map_event_detail_title);
        event_description = findViewById(R.id.event_description);
        recommend_lv = findViewById(R.id.recommend_lv);
        enemy_number = findViewById(R.id.enemy_number);
        carry_buff = findViewById(R.id.carry_buff);
        go_button = findViewById(R.id.go_button);
        extra_mission_frame = findViewById(R.id.extra_mission_frame);
        extra_mission_text = findViewById(R.id.extra_mission_text);
        extra_mission_add_number = findViewById(R.id.extra_mission_add_number);
        extra_mission_cc_icon = findViewById(R.id.extra_mission_cc_icon);
        extra_mission_grief_seed_icon = findViewById(R.id.extra_mission_grief_seed_icon);
        change_map_button = findViewById(R.id.change_map_button);
        for(int i = 0; i < 4; i++){
            clock_number[i] = findViewById(getIdByString("clock_number"+i));
        }
        clock_colon = findViewById(R.id.clock_colon);
    }

    public void initView(){
        kamihamaMap.setOnTouchListener(this);

        updateCCAndGriefSeedView();

        setClock();

        black_mask.setVisibility(View.VISIBLE);

        map_event_detail_frame.setVisibility(View.GONE);

        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        grayColorFilter = new ColorMatrixColorFilter(cm);

        //设置BGM
        if(Math.abs(global.gameTime - 17.0f) <= 0.1f){
            global.setNewBGM(R.raw.bgm24_story07_hca, 1.0f);
        }else{
            global.setNewBGM(R.raw.bgm24_story08_hca, 1.0f);
        }


        //恢复上次地图
        if(global.isSimpleMap){
            kamihamaMap.setBackgroundResource(R.drawable.kamihama_map);
        }else{
            kamihamaMap.setBackgroundResource(R.drawable.kamihama_map_mark);
        }

        if(!global.isMapSizeTransferred){
            for(int i = 0; i < global.mapRandomPoint.length; i++){
                global.mapRandomPoint[i][0] = (int) (1.0f * global.mapRandomPoint[i][0] / 4096.0f * screenWidth);
                global.mapRandomPoint[i][1] = (int) (1.0f * global.mapRandomPoint[i][1] / 2048.0f * screenHeight);
            }
            global.PLAYER_ON_MAP_X = (int) (1.0f * global.PLAYER_ON_MAP_X / 4096.0f * screenWidth);
            global.PLAYER_ON_MAP_Y = (int) (1.0f * global.PLAYER_ON_MAP_Y / 2048.0f * screenHeight);
            global.isMapSizeTransferred = true;
        }

        //初始地点
        //int temp = (int)(Math.random()*global.mapRandomPoint.length);
        leaderX = global.PLAYER_ON_MAP_X;
        leaderY = global.PLAYER_ON_MAP_Y;
        Log.d("Sam", "leaderX:" + leaderX + ", leaderY:" + leaderY);

        //初始化角色小人
        initLeader();

        //为角色小人一定范围内选择事件
        initSurrendingEvents();

        //切换地图到上次地点
        ViewHelper.setX(kamihamaMap, global.mapX);
        ViewHelper.setY(kamihamaMap, global.mapY);
        ViewHelper.setScaleX(kamihamaMap, global.scaleMultiple);// x方向上缩放
        ViewHelper.setScaleY(kamihamaMap, global.scaleMultiple);// y方向上缩放

        //查看队伍
        team_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isIntentSend){
                    Intent intent1 = new Intent(MapActivity.this, TeamChooseActivity.class);
                    intent1.putExtra("battleInfo", -1);
                    startActivity(intent1);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    isIntentSend = true;
                }
            }
        });

        //进入商店
        shop_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(global.ccNumber >= global.COST_FOR_SUMMON_ADJUSTMENT_HOUSE){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                    final AlertDialog dialog = builder.create();

                    View dialog_frame = LayoutInflater.from(MapActivity.this).inflate(R.layout.alert_dialog_frame, null);
                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_title_name)).setText("购买服务");
                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("是否花费 " + global.COST_FOR_SUMMON_ADJUSTMENT_HOUSE + "cc 购买调整屋的上门服务？\n(当前地图事件会更新)");
                    ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).removeAllViews();
                    //((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).addView();
                    ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(null);
                    (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            if(!isIntentSend){
                                global.ccNumber -= global.COST_FOR_SUMMON_ADJUSTMENT_HOUSE;
                                global.mpEvent.clear();
                                global.gameTime += global.ADD_GAME_TIME;
                                if(global.collectionDict.get("幽灵执照").isOwn){
                                    if(colorToss(25)){
                                        global.gameTime -= global.ADD_GAME_TIME;
                                    }
                                }
                                Intent intent1 = new Intent(MapActivity.this, AdjustmentHouseActivity.class);
                                startActivity(intent1);
                                finish();
                                overridePendingTransition(0, android.R.anim.fade_out);
                                isIntentSend = true;
                            }
                        }
                    });
                    (dialog_frame.findViewById(R.id.alert_dialog_cancel_button)).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setContentView(dialog_frame);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                    final AlertDialog dialog = builder.create();

                    View dialog_frame = LayoutInflater.from(MapActivity.this).inflate(R.layout.alert_dialog_frame, null);
                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_title_name)).setText("购买服务");
                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("购买调整屋的上门服务需要 " + global.COST_FOR_SUMMON_ADJUSTMENT_HOUSE + "cc, cc不足");
                    ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(grayColorFilter);
                    ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).removeAllViews();
                    (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){

                        }
                    });
                    (dialog_frame.findViewById(R.id.alert_dialog_cancel_button)).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    dialog.getWindow().setContentView(dialog_frame);
                }
            }
        });

        //查看道具
        MapCollectionAdapter mapCollectionAdapter = new MapCollectionAdapter(MapActivity.this, global.collectionList);
        collection_recycler_view.setAdapter(mapCollectionAdapter);
        StaggeredGridLayoutManager m = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        collection_recycler_view.setLayoutManager(m);
        collection_list_background.setVisibility(View.GONE);
        item_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                collection_list_background.setVisibility(View.VISIBLE);
            }
        });
        collection_list_background.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                collection_list_background.setVisibility(View.GONE);
            }
        });

        //切换地图
        change_map_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(global.isSimpleMap){
                    kamihamaMap.setBackgroundResource(R.drawable.kamihama_map_mark);
                }else{
                    kamihamaMap.setBackgroundResource(R.drawable.kamihama_map);
                }
                global.isSimpleMap = !global.isSimpleMap;
            }
        });

        //若spriteViewer没有正常启动，则显示备用小人
        Message tempM = new Message();
        tempM.what = 1;
        handler.sendMessageDelayed(tempM, 3000);
    }

    void updateCCAndGriefSeedView(){
        cc_number.setText(" " + global.ccNumber + " ");
        grief_seed_number.setText(" " + global.griefSeedNumber + " ");
    }

    void setClock(){
        float t = global.gameTime;
        for(int i = 0; i < 24; i++){
            if(i - 0.1f < t && t < i + 0.6f){
                clock_number[0].setBackgroundResource(getImageByString("blue_"+(i / 10)));
                clock_number[1].setBackgroundResource(getImageByString("blue_"+(i % 10)));
                if(i - 0.1f < t && t < i + 0.1f){
                    clock_number[2].setBackgroundResource(R.drawable.blue_0);
                    clock_number[3].setBackgroundResource(R.drawable.blue_0);
                }else{
                    clock_number[2].setBackgroundResource(R.drawable.blue_3);
                    clock_number[3].setBackgroundResource(R.drawable.blue_0);
                }
            }
        }
        handler.postDelayed(clockColonControl,1000);
    }


    public void initLeader(){
        Character temp = null;
        for(int i = 0; i < global.characters.length; i++){
            if(global.characters[i] != null && global.characters[i].isLeader){
                temp = global.characters[i];
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
        sampleSet.connect(leader.getId(), ConstraintSet.START, kamihamaMap.getId(), ConstraintSet.START, leaderX - 40);//人物view宽80
        sampleSet.connect(leader.getId(), ConstraintSet.TOP, kamihamaMap.getId(), ConstraintSet.TOP, leaderY - 110);//人物view长110
        leader.setVisibility(View.VISIBLE);

        //设置好一份备份，当spriteViewer无法正常启动时则显示备份
        leader_replace_image_view.setImageResource(getImageByString(temp.miniImage));
        sampleSet.connect(leader_replace_image_view.getId(), ConstraintSet.START, kamihamaMap.getId(), ConstraintSet.START, leaderX - 40);//人物view宽80
        sampleSet.connect(leader_replace_image_view.getId(), ConstraintSet.TOP, kamihamaMap.getId(), ConstraintSet.TOP, leaderY - 110);//人物view长110
        sampleSet.applyTo(kamihamaMap);
        leader_replace_image_view.setVisibility(View.GONE);
        //leader.resetCharacter();
        //leader.webView.loadUrl("javascript:changeSprite()");

    }

    public void initSurrendingEvents(){
        if(Math.abs(global.gameTime - 16.0f) <= 0.1f){
            //进入万年樱事件
            global.mpEvent.clear();
            for(int i = 0; i < global.mapRandomPoint.length; i++){
                //1.在leader的一定距离范围内
                int tempX = global.mapRandomPoint[i][0];
                int tempY = global.mapRandomPoint[i][1];
                if(Math.pow(tempX - leaderX, 2) + Math.pow(tempY - leaderY, 2) <= global.EXPLORE_RADIUS * global.EXPLORE_RADIUS){
                    //2.不能在leader的图像范围内 80*110 与 25*32
                    if(!(Math.pow(tempX - leaderX, 2) + Math.pow(tempY - 16 - leaderY + 55, 2) <= Math.pow(80, 2))){
                        //if(!(Math.abs(tempX-12-leaderX) <= 40 && Math.abs(tempY-16-leaderY+55) <= 55)){
                        //3.不能和已经触发的点图像重叠 25*32
                        boolean isOverlap = false;
                        for(int j = 0; j < global.mpEvent.size(); j++){
                            int tempJx = global.mpEvent.get(j).x;
                            int tempJy = global.mpEvent.get(j).y;
                            if(Math.pow(tempX - tempJx, 2) + Math.pow(tempY - tempJy, 2) <= Math.pow(32, 2)){
                                isOverlap = true;
                                break;
                            }
                        }
                        if(!isOverlap){
                            MapEvent mpe = new MapEvent();
                            mpe.x = tempX;
                            mpe.y = tempY;
                            Log.d("Sam", "randomMpeX:" + tempX + ", mpeY:" + tempY);
                            global.mpEvent.add(mpe);
                            Log.d("Sam", "addEventPoint:" + tempX + "," + tempY);
                            break;
                        }
                    }
                }
            }

            MapEvent mpe = global.mpEvent.get(0);
            mpe.eventType = global.EVENT;
            mpe.bi = null;
            mpe.randomEvent = R.raw.time_16_event;
        }else if(Math.abs(global.gameTime - 17.0f) <= 0.1f){
            //进入最终事件
            global.mpEvent.clear();
            MapEvent mpe = new MapEvent();
            mpe.x = (int) (1.0f * 2573 / 4096.0f * screenWidth);
            mpe.y = (int) (1.0f * 150 / 2048.0f * screenHeight);
            mpe.eventType = global.EVENT;
            mpe.bi = null;
            mpe.randomEvent = R.raw.final_battle_after_meet_yachiyo_before;
            global.mpEvent.add(mpe);
        }else{
            //不是16,17点，进入普通的事件判断
            int finalEventNumber = global.EVENT_NUMBER;
            if(global.collectionDict.get("红蓝蜡烛").isOwn){
                finalEventNumber += 2;
            }
            if(global.mpEvent.size() < finalEventNumber){
                //选择所有可能的点
                for(int i = 0; i < global.mapRandomPoint.length; i++){
                    //1.在leader的一定距离范围内
                    int tempX = global.mapRandomPoint[i][0];
                    int tempY = global.mapRandomPoint[i][1];
                    if(Math.pow(tempX - leaderX, 2) + Math.pow(tempY - leaderY, 2) <= global.EXPLORE_RADIUS * global.EXPLORE_RADIUS){
                        //2.不能在leader的图像范围内 80*110 与 25*32
                        if(!(Math.pow(tempX - leaderX, 2) + Math.pow(tempY - 16 - leaderY + 55, 2) <= Math.pow(80, 2))){
                            //if(!(Math.abs(tempX-12-leaderX) <= 40 && Math.abs(tempY-16-leaderY+55) <= 55)){
                            //3.不能和已经触发的点图像重叠 25*32
                            boolean isOverlap = false;
                            for(int j = 0; j < global.mpEvent.size(); j++){
                                int tempJx = global.mpEvent.get(j).x;
                                int tempJy = global.mpEvent.get(j).y;
                                if(Math.pow(tempX - tempJx, 2) + Math.pow(tempY - tempJy, 2) <= Math.pow(32, 2)){
                                    isOverlap = true;
                                    break;
                                }
                            }
                            if(!isOverlap){
                                MapEvent mpe = new MapEvent();
                                mpe.x = tempX;
                                mpe.y = tempY;
                                Log.d("Sam", "randomMpeX:" + tempX + ", mpeY:" + tempY);
                                global.mpEvent.add(mpe);
                                Log.d("Sam", "addEventPoint:" + tempX + "," + tempY);
                            }
                        }
                    }
                }

                //筛选点
                Collections.shuffle(global.mpEvent);
                while(global.mpEvent.size() > finalEventNumber){
                    int markPoint = -1;
                    double maxD = -1d;
                    for(int i = 0; i < global.mpEvent.size(); i++){
                        //计算除去某个点后所有点之间的距离，最终remove掉距离总和最大的点
                        double tempD = calculateSumDistance(global.mpEvent, i);
                        if(tempD > maxD){
                            maxD = tempD;
                            markPoint = i;
                        }
                    }
                    global.mpEvent.remove(markPoint);
                    Log.d("Sam","removePoint:" + markPoint);
                }

                while(global.mpEvent.size() > finalEventNumber){
                    int randomId = (int) (Math.random() * global.mpEvent.size());
                    global.mpEvent.remove(randomId);
                }

                boolean hasShop = false;
                //为留下来的点添加事件
                for(int i = 0; i < global.mpEvent.size(); i++){
                    MapEvent mpe = global.mpEvent.get(i);
                    double tempRandom = Math.random();
                    if(Math.abs(global.gameTime - 7.0f) <= 0.1f){
                        //第一次进入，全都是战斗
                        if(tempRandom < 0.3d){
                            //魔女战斗
                            mpe.eventType = global.BOSS_BATTLE;
                            mpe.bi = generateRandomBattle(mpe.x, mpe.y, true);
                        }else{
                            //普通战斗
                            mpe.eventType = global.NORMAL_BATTLE;
                            mpe.bi = generateRandomBattle(mpe.x, mpe.y, false);
                        }
                    }else{
                        if(tempRandom < 0.3d && global.randomEventList.size() > 0){
                            //事件
                            mpe.eventType = global.EVENT;
                            mpe.bi = null;
                            ArrayList<Integer> availableEventList = new ArrayList<>();
                            for(int j = 0; j < global.randomEventList.size(); j++){
                                int temp = global.randomEventList.get(j);
                                if(temp == R.raw.promotion_of_bangbangzai){
                                    if(global.ccNumber > 500){
                                        availableEventList.add(temp);
                                    }
                                }else{
                                    availableEventList.add(temp);
                                }
                            }
                            mpe.randomEvent = availableEventList.get((int)(Math.random()*availableEventList.size()));
                        }else if(tempRandom < 0.5d){
                            //魔女战斗
                            mpe.eventType = global.BOSS_BATTLE;
                            mpe.bi = generateRandomBattle(mpe.x, mpe.y, true);
                        }else if(tempRandom < 0.6d && !hasShop){
                            //商店
                            mpe.eventType = global.SHOP;
                            hasShop = true;
                        }else{
                            //普通战斗
                            mpe.eventType = global.NORMAL_BATTLE;
                            mpe.bi = generateRandomBattle(mpe.x, mpe.y, false);
                        }
                    }
                }

            }
        }




        //在地图上生成筛选后的点
        for(int i = 0; i < global.mpEvent.size(); i++){
            final MapEvent mpe = global.mpEvent.get(i);
            final ImageView ep = new ImageView(this);
            final int tempI = i;
            Log.d("Sam", "mpeX:" + mpe.x + ", mpeY:" + mpe.y);
            ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(25, 32);
            ep.setLayoutParams(p);
            ep.setId(View.generateViewId());
            events_layout.addView(ep);
            ConstraintSet sampleSet = new ConstraintSet();
            sampleSet.clone(events_layout);
            sampleSet.connect(ep.getId(), ConstraintSet.START, events_layout.getId(), ConstraintSet.START, mpe.x - 12);
            sampleSet.connect(ep.getId(), ConstraintSet.TOP, events_layout.getId(), ConstraintSet.TOP, mpe.y - 32);
            sampleSet.applyTo(events_layout);

            if(mpe.eventType == global.NORMAL_BATTLE){
                //普通战斗
                ep.setBackgroundResource(R.drawable.map_mark_battle);
                ep.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        map_event_detail_title_frame.setBackgroundResource(R.drawable.map_event_detail_pink_title);
                        map_event_detail_title.setText(mpe.bi.battleName);
                        event_description.setText(mpe.bi.battleDescription);
                        recommend_lv.setText(mpe.bi.recommendLV);
                        enemy_number.setText("" + mpe.bi.monsterNumber);
                        String tempEffectDescription = "";
                        for(int j = 0; j < mpe.bi.useEffect.size(); j++){
                            tempEffectDescription += mpe.bi.useEffect.get(j).first.getDescription() + "\n";
                        }
                        carry_buff.setText(tempEffectDescription);
                        map_event_detail_frame.setVisibility(View.VISIBLE);

                        ExtraMission em = global.extraMissionList.get(mpe.bi.extraMissionId);
                        extra_mission_frame.setVisibility(View.VISIBLE);
                        extra_mission_text.setText(em.name);
                        if(em.bonus.cc > 0){
                            extra_mission_add_number.setText("+" + em.bonus.cc);
                            extra_mission_cc_icon.setVisibility(View.VISIBLE);
                            extra_mission_grief_seed_icon.setVisibility(View.GONE);
                        }else{
                            extra_mission_add_number.setText("+" + em.bonus.griefSeed);
                            extra_mission_cc_icon.setVisibility(View.GONE);
                            extra_mission_grief_seed_icon.setVisibility(View.VISIBLE);
                        }


                        go_button.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                if(!isIntentSend){
                                    Intent intent1 = new Intent(MapActivity.this, TeamChooseActivity.class);
                                    intent1.putExtra("battleInfo", tempI);
                                    intent1.putExtra("isRandomBattle", true);
                                    intent1.putExtra("eventX", mpe.x);
                                    intent1.putExtra("eventY", mpe.y);
                                    startActivity(intent1);
                                    finish();
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    isIntentSend = true;
                                }
                            }
                        });
                    }
                });
            }else if(mpe.eventType == global.BOSS_BATTLE){
                //boss战斗
                ep.setBackgroundResource(R.drawable.map_mark_emergent_battle);
                ep.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        map_event_detail_title_frame.setBackgroundResource(R.drawable.map_event_detail_red_title);
                        map_event_detail_title.setText(mpe.bi.battleName);
                        event_description.setText(mpe.bi.battleDescription);
                        recommend_lv.setText(mpe.bi.recommendLV);
                        enemy_number.setText("" + mpe.bi.monsterNumber);
                        String tempEffectDescription = "";
                        for(int j = 0; j < mpe.bi.useEffect.size(); j++){
                            tempEffectDescription += mpe.bi.useEffect.get(j).first.getDescription() + "\n";
                        }
                        carry_buff.setText(tempEffectDescription);
                        map_event_detail_frame.setVisibility(View.VISIBLE);

                        ExtraMission em = global.extraMissionList.get(mpe.bi.extraMissionId);
                        extra_mission_frame.setVisibility(View.VISIBLE);
                        extra_mission_text.setText(em.name);
                        if(em.bonus.cc > 0){
                            extra_mission_add_number.setText("+" + em.bonus.cc);
                            extra_mission_cc_icon.setVisibility(View.VISIBLE);
                            extra_mission_grief_seed_icon.setVisibility(View.GONE);
                        }else{
                            extra_mission_add_number.setText("+" + em.bonus.griefSeed);
                            extra_mission_cc_icon.setVisibility(View.GONE);
                            extra_mission_grief_seed_icon.setVisibility(View.VISIBLE);
                        }

                        go_button.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                if(!isIntentSend){
                                    Intent intent1 = new Intent(MapActivity.this, TeamChooseActivity.class);
                                    intent1.putExtra("battleInfo", tempI);
                                    intent1.putExtra("isRandomBattle", true);
                                    intent1.putExtra("eventX", mpe.x);
                                    intent1.putExtra("eventY", mpe.y);
                                    startActivity(intent1);
                                    finish();
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    isIntentSend = true;
                                }
                            }
                        });
                    }
                });
            }else if(mpe.eventType == global.EVENT){
                //对话
                ep.setBackgroundResource(R.drawable.map_mark_event);
                ep.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        map_event_detail_title_frame.setBackgroundResource(R.drawable.map_event_detail_pink_title);
                        map_event_detail_title.setText("随机事件");
                        event_description.setText("那里似乎有些什么，但是不过去看看显然不知道那里具体会发生什么。");
                        recommend_lv.setText("--");
                        enemy_number.setText("--");
                        carry_buff.setText("");
                        map_event_detail_frame.setVisibility(View.VISIBLE);
                        extra_mission_frame.setVisibility(View.GONE);
                        go_button.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                global.cancelBGM();
                                global.PLAYER_ON_MAP_X = mpe.x;
                                global.PLAYER_ON_MAP_Y = mpe.y;
                                if(!isIntentSend){
                                    if(global.collectionDict.get("便携式照相机").isOwn){
                                        global.ccNumber += 500;
                                    }
                                    global.gameTime += global.ADD_GAME_TIME;
                                    if(global.collectionDict.get("幽灵执照").isOwn){
                                        if(colorToss(25)){
                                            global.gameTime -= global.ADD_GAME_TIME;
                                        }
                                    }
                                    Intent intent1 = new Intent(MapActivity.this, DialogActivity.class);
                                    intent1.putExtra("mpEventId", tempI);
                                    startActivity(intent1);
                                    finish();
                                    overridePendingTransition(0, android.R.anim.fade_out);
                                    isIntentSend = true;
                                }
                            }
                        });
                    }
                });
            }else{
                //商店
                ep.setBackgroundResource(R.drawable.map_mark_shop);
                ep.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        map_event_detail_title_frame.setBackgroundResource(R.drawable.map_event_detail_pink_title);
                        map_event_detail_title.setText("调整屋");
                        event_description.setText("调整屋似乎正好在此地出外勤。是个补充物资的好机会。");
                        recommend_lv.setText("--");
                        enemy_number.setText("--");
                        carry_buff.setText("");
                        extra_mission_frame.setVisibility(View.GONE);
                        map_event_detail_frame.setVisibility(View.VISIBLE);
                        go_button.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                global.PLAYER_ON_MAP_X = mpe.x;
                                global.PLAYER_ON_MAP_Y = mpe.y;
                                if(!isIntentSend){
                                    global.mpEvent.clear();
                                    global.gameTime += global.ADD_GAME_TIME;
                                    if(global.collectionDict.get("幽灵执照").isOwn){
                                        if(colorToss(25)){
                                            global.gameTime -= global.ADD_GAME_TIME;
                                        }
                                    }
                                    Intent intent1 = new Intent(MapActivity.this, AdjustmentHouseActivity.class);
                                    startActivity(intent1);
                                    finish();
                                    overridePendingTransition(0, android.R.anim.fade_out);
                                    isIntentSend = true;
                                }
                            }
                        });

                    }
                });
            }
        }
    }

    public void initEffectPool(){
        global.effectPool = new ArrayList<>();
        global.effectPool.add(new Pair<>(new Effect("攻击力UP", 15, 999, 100, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击力UP", 35, 999, 100, 0), 4));
        global.effectPool.add(new Pair<>(new Effect("攻击力UP", 65, 999, 100, 0), 8));
        global.effectPool.add(new Pair<>(new Effect("攻击力DOWN", 20, 999, 100, 0), -3));
        global.effectPool.add(new Pair<>(new Effect("攻击力DOWN", 40, 999, 100, 0), -5));
        global.effectPool.add(new Pair<>(new Effect("防御力UP", 15, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("防御力UP", 35, 999, 100, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("防御力UP", 65, 999, 100, 0), 4));
        global.effectPool.add(new Pair<>(new Effect("防御力DOWN", 20, 999, 100, 0), -2));
        global.effectPool.add(new Pair<>(new Effect("防御力DOWN", 40, 999, 100, 0), -4));
        global.effectPool.add(new Pair<>(new Effect("造成伤害UP", 20, 999, 100, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("造成伤害UP", 40, 999, 100, 0), 4));
        global.effectPool.add(new Pair<>(new Effect("造成伤害UP", 70, 999, 100, 0), 8));
        global.effectPool.add(new Pair<>(new Effect("造成伤害DOWN", 25, 999, 100, 0), -3));
        global.effectPool.add(new Pair<>(new Effect("造成伤害DOWN", 45, 999, 100, 0), -5));
        global.effectPool.add(new Pair<>(new Effect("伤害削减", 20, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("伤害削减", 40, 999, 100, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("伤害削减", 70, 999, 100, 0), 4));
        global.effectPool.add(new Pair<>(new Effect("Magia伤害削减", 40, 999, 100, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("Magia伤害削减", 70, 999, 100, 0), 4));
        global.effectPool.add(new Pair<>(new Effect("异常状态耐性UP", 20, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("异常状态耐性UP", 40, 999, 100, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("异常状态耐性UP", 90, 999, 100, 0), 3));
        global.effectPool.add(new Pair<>(new Effect("异常状态耐性DOWN", 15, 999, 100, 0), -1));
        global.effectPool.add(new Pair<>(new Effect("异常状态耐性DOWN", 35, 999, 100, 0), -2));
        global.effectPool.add(new Pair<>(new Effect("濒死时防御力UP", 20, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("濒死时防御力UP", 40, 999, 100, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("濒死时防御力UP", 90, 999, 100, 0), 4));
        global.effectPool.add(new Pair<>(new Effect("濒死时攻击力UP", 20, 999, 100, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("濒死时攻击力UP", 40, 999, 100, 0), 3));
        global.effectPool.add(new Pair<>(new Effect("濒死时攻击力UP", 90, 999, 100, 0), 6));
        global.effectPool.add(new Pair<>(new Effect("HP自动回复", 3, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("HP自动回复", 5, 999, 100, 0), 3));
        global.effectPool.add(new Pair<>(new Effect("HP自动回复", 10, 999, 100, 0), 5));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态雾", 25, 999, 15, 2), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态雾", 25, 999, 35, 3), 5));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态雾", 25, 999, 65, 4), 7));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态黑暗", 35, 999, 15, 2), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态黑暗", 35, 999, 35, 3), 5));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态黑暗", 35, 999, 85, 4), 8));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态幻惑", 50, 999, 15, 2), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态幻惑", 50, 999, 35, 3), 5));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态幻惑", 50, 999, 75, 4), 8));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态毒", 5, 999, 25, 3), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态毒", 5, 999, 35, 4), 4));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态毒", 5, 999, 60, 5), 6));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态烧伤", 10, 999, 20, 2), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态烧伤", 10, 999, 35, 3), 4));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态烧伤", 10, 999, 60, 4), 7));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态诅咒", 15, 999, 15, 2), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态诅咒", 15, 999, 40, 4), 5));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态诅咒", 15, 999, 60, 5), 8));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态魅惑", 0, 999, 15, 2), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态魅惑", 0, 999, 40, 3), 3));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态魅惑", 0, 999, 60, 3), 5));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态眩晕", 0, 999, 15, 2), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态眩晕", 0, 999, 40, 3), 3));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态眩晕", 0, 999, 60, 4), 5));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态拘束", 0, 999, 15, 2), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态拘束", 0, 999, 40, 3), 3));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态拘束", 0, 999, 60, 3), 5));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态Magia封印", 0, 999, 15, 2), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态Magia封印", 0, 999, 40, 3), 3));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态技能封印", 0, 999, 25, 2), 3));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态技能封印", 0, 999, 50, 3), 4));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态HP回复禁止", 0, 999, 30, 2), 1));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态HP回复禁止", 0, 999, 60, 3), 2));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态MP回复禁止", 0, 999, 25, 2), 3));
        global.effectPool.add(new Pair<>(new Effect("攻击时给予状态MP回复禁止", 0, 999, 50, 3), 4));
        global.effectPool.add(new Pair<>(new Effect("挑拨", 0, 999, 25, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("挑拨", 0, 999, 50, 0), 3));
        global.effectPool.add(new Pair<>(new Effect("回避", 0, 999, 25, 0), 3));
        global.effectPool.add(new Pair<>(new Effect("回避", 0, 999, 50, 0), 5));
        global.effectPool.add(new Pair<>(new Effect("回避", 0, 999, 80, 0), 8));
        global.effectPool.add(new Pair<>(new Effect("暴击", 200, 999, 10, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("暴击", 200, 999, 40, 0), 4));
        global.effectPool.add(new Pair<>(new Effect("暴击", 200, 999, 80, 0), 8));
        global.effectPool.add(new Pair<>(new Effect("回避无效", 0, 999, 40, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("回避无效", 0, 999, 100, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("毒无效", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("挑拨无效", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("诅咒无效", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("雾无效", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("忍耐", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("烧伤无效", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("黑暗无效", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("拘束无效", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("眩晕无效", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("幻惑无效", 0, 999, 100, 0), 1));
        global.effectPool.add(new Pair<>(new Effect("无视防御力", 0, 999, 40, 0), 4));
        global.effectPool.add(new Pair<>(new Effect("无视防御力", 0, 999, 80, 0), 8));
        global.effectPool.add(new Pair<>(new Effect("伤害削减无效", 0, 999, 50, 0), 2));
        global.effectPool.add(new Pair<>(new Effect("伤害削减无效", 0, 999, 100, 0), 4));
    }

    public BattleInfo generateRandomBattle(int x, int y, boolean isBossBattle){
        Log.d("Sam", "generateRandomBattle");
        BattleInfo bi = new BattleInfo();

        bi.backgroundType = BattleInfo.JUNCTION;
        bi.backgroundId = (int) (Math.random() * global.JUNCTION_BACKGROUND_IMAGE_LIST.size());
        bi.isBossBattle = isBossBattle;
        bi.battleName = isBossBattle ? "魔女的气息..." : "有使魔在活动！";

        int sumPoint = (global.gameTime <= 11.01f) ? 2 : (global.gameTime <= 15.01f ? 8 : 16);

        Log.d("Sam", "generateTotalBuff");
        bi.useEffect = new ArrayList<>();
        //随机战斗的总buff池
        ArrayList<String> randomBuffChoice = global.ENEMY_RANDOM_BUFF_DICT.get(sumPoint);
        String tempChoice = randomBuffChoice.get((int) (Math.random() * randomBuffChoice.size()));
        Log.d("Sam", "useBuffChoice:" + tempChoice);
        for(int j = 0; j < global.effectPool.size(); j++){
            if(tempChoice.substring(j, j + 1).equals("1")){
                bi.useEffect.add(new Pair<>(new Effect(global.effectPool.get(j).first), global.effectPool.get(j).second));
            }
        }

        Log.d("Sam", "generateEnemyName");
        //得到敌人名字
        ArrayList<String> monsterNameList;
        if(isBossBattle){
            monsterNameList = new ArrayList<>(commonBossNameList);
            for(int i = 0; i < specialBossList.length; i++){
                if(Math.pow(x - 1.0f * specialPoint[i].first / screenWidth, 2) + Math.pow(y - 1.0f * specialPoint[i].second / screenHeight, 2) <= 200){
                    monsterNameList.add(specialBossList[i]);
                    monsterNameList.add(specialBossList[i]);
                    monsterNameList.add(specialBossList[i]);
                    monsterNameList.add(specialBossList[i]);
                    monsterNameList.add(specialBossList[i]);
                }
            }
        }else{
            monsterNameList = new ArrayList<>(commonMonsterNameList);
            for(int i = 0; i < specialMonsterList.length; i++){
                if(Math.pow(x - 1.0f * specialPoint[i].first / screenWidth, 2) + Math.pow(y - 1.0f * specialPoint[i].second / screenHeight, 2) <= 200){
                    monsterNameList.add(specialMonsterList[i]);
                    monsterNameList.add(specialMonsterList[i]);
                    monsterNameList.add(specialMonsterList[i]);
                    monsterNameList.add(specialMonsterList[i]);
                    monsterNameList.add(specialMonsterList[i]);
                }
            }
        }
        String monsterSprite = monsterNameList.get((int) (Math.random() * monsterNameList.size()));

        bi.battleDescription = isBossBattle ? "似乎有魔女在此活动。请小心。" : "似乎有使魔的气息，为了人们的安全最好尽快击败他们。";

        if(isBossBattle){
            Log.d("Sam", "generateBoss");
            bi.monsterNumber = 1;
            bi.monsterList.add(generateMonster(x, y, bi.useEffect, monsterSprite, sumPoint, true));
            bi.monsterFormation[1][1] = bi.monsterList.get(0);
            int p = 7;
            if(Math.random() * 10 < p){
                bi.monsterFormation[1][0] = bi.monsterList.get(0);
                p -= 2;
            }
            if(Math.random() * 10 < p){
                bi.monsterFormation[0][0] = bi.monsterList.get(0);
                bi.monsterFormation[2][0] = bi.monsterList.get(0);
                p -= 2;
            }
            if(Math.random() * 10 < p){
                bi.monsterFormation[1][2] = bi.monsterList.get(0);
                p -= 2;
            }
            if(Math.random() * 10 < p){
                bi.monsterFormation[0][1] = bi.monsterList.get(0);
                bi.monsterFormation[2][1] = bi.monsterList.get(0);
                p -= 2;
            }
            if(Math.random() * 10 < p){
                bi.monsterFormation[0][2] = bi.monsterList.get(0);
                bi.monsterFormation[2][2] = bi.monsterList.get(0);
                p -= 2;
            }
        }else{
            //确定怪物数量, 添加相应怪物
            Log.d("Sam", "generateMonsters");
            bi.monsterNumber = (int) (Math.random() * 3) + 3;
            for(int i = 0; i < bi.monsterNumber; i++){
                bi.monsterList.add(generateMonster(x, y, bi.useEffect, monsterSprite, sumPoint * 3 / 4, false));
                while(true){
                    int tempX = (int) (Math.random() * 3);
                    int tempY = (int) (Math.random() * 3);
                    if(bi.monsterFormation[tempX][tempY] == null){
                        bi.monsterFormation[tempX][tempY] = bi.monsterList.get(i);
                        break;
                    }
                }
            }

        }

        if(global.gameTime < 8.01f){
            bi.recommendLV = "1";
        }else if(global.gameTime < 10.01f){
            bi.recommendLV = "20";
        }else if(global.gameTime < 12.01f){
            bi.recommendLV = "40";
        }else if(global.gameTime < 14.01f){
            bi.recommendLV = "60";
        }else if(global.gameTime < 16.01f){
            bi.recommendLV = "80";
        }else if(global.gameTime < 18.01f){
            bi.recommendLV = "100";
        }
        bi.extraMissionId = (int) (Math.random() * global.extraMissionList.size());
        return bi;
    }

    public Character generateMonster(int x, int y, ArrayList<Pair<Effect, Integer>> useEffectPool, String spriteName, int buffPoint, boolean isBossBattle){
        Log.d("Sam", "generateMonster:buffLength:" + useEffectPool.size() + ", name:" + spriteName);
        Character c = new Character();

        //属性
        String[] elementList = new String[]{"tree", "water", "fire", "light", "dark"};
        c.element = elementList[(int) (Math.random() * (elementList.length))];

        //名字
        c.spriteName = spriteName;
        if(c.spriteName.startsWith("monster_")){
            c.name = c.spriteName.substring(8);
        }

        //数值
        float difficultCoefficient = 0.6f;
        if(!isBossBattle){
            if(global.gameTime < 8.01f){
                c.lv = 1;
                c.HP = 3000;
                c.realHP = c.HP;
                c.ATK = (int)(750 * difficultCoefficient);
                c.DEF = (int)(1000 * difficultCoefficient);
            }else if(global.gameTime < 10.01f){
                c.lv = 20;
                c.HP = (int)(9800 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(1500 * difficultCoefficient);
                c.DEF = (int)(4500 * difficultCoefficient);
            }else if(global.gameTime < 12.01f){
                c.lv = 40;
                c.HP = (int)(15000 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(2250 * difficultCoefficient);
                c.DEF = (int)(6500 * difficultCoefficient);
            }else if(global.gameTime < 14.01f){
                c.lv = 60;
                c.HP = (int)(35000 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(4800 * difficultCoefficient);
                c.DEF = (int)(7000 * difficultCoefficient);
            }else if(global.gameTime < 16.01f){
                c.lv = 80;
                c.HP = (int)(80000 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(9000 * difficultCoefficient);
                c.DEF = (int)(10500 * difficultCoefficient);
            }else if(global.gameTime < 18.01f){
                c.lv = 100;
                c.HP = (int)(150000 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(14400 * difficultCoefficient);
                c.DEF = (int)(13000 * difficultCoefficient);
            }
        }else{
            if(global.gameTime < 8.01f){
                c.lv = 1;
                c.HP = (int)(16000 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(1500 * difficultCoefficient);
                c.DEF = (int)(1300 * difficultCoefficient);
            }else if(global.gameTime < 10.01f){
                c.lv = 20;
                c.HP = (int)(34300 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(2875 * difficultCoefficient);
                c.DEF = (int)(4075 * difficultCoefficient);
            }else if(global.gameTime < 12.01f){
                c.lv = 40;
                c.HP = (int)(52500 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(4700 * difficultCoefficient);
                c.DEF = (int)(7000 * difficultCoefficient);
            }else if(global.gameTime < 14.01f){
                c.lv = 60;
                c.HP = (int)(122500 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(8700 * difficultCoefficient);
                c.DEF = (int)(9000 * difficultCoefficient);
            }else if(global.gameTime < 16.01f){
                c.lv = 80;
                c.HP = (int)(280000 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(14000 * difficultCoefficient);
                c.DEF = (int)(15500 * difficultCoefficient);
            }else if(global.gameTime < 18.01f){
                c.lv = 100;
                c.HP = (int)(552500 * difficultCoefficient);
                c.realHP = c.HP;
                c.ATK = (int)(18000 * difficultCoefficient);
                c.DEF = (int)(20000 * difficultCoefficient);
            }
        }

        c.mpAttackRatio = 0f;
        c.mpDefendRatio = 0f;

        //盘型
        int[] plateList = new int[]{ACCELE, CHARGE, CHARGE, BLAST_HORIZONTAL, BLAST_VERTICAL};
        c.plateList = new int[5];
        for(int i = 0; i < c.plateList.length; i++){
            c.plateList[i] = plateList[(int) (Math.random() * plateList.length)];
        }

        //添加effect
        if(isBossBattle){
            for(int i = 0; i < useEffectPool.size(); i++){
                c.initialEffectList.add(new Effect(useEffectPool.get(i).first));
            }
        }else{
            //洗牌后逐个开始载入，直到总和大于buffPoint
            Collections.shuffle(useEffectPool);
            int tempSum = 0;
            for(int i = 0; i < useEffectPool.size(); i++){
                if(tempSum < buffPoint){
                    c.initialEffectList.add(new Effect(useEffectPool.get(i).first));
                    tempSum += useEffectPool.get(i).second;
                }else{
                    break;
                }
            }
        }


        return c;
    }

    public double calculateSumDistance(ArrayList<MapEvent> mpEvent, int ignorePoint){
        // ignorePoint == -1代表不忽略点
        double d = 0d;
        for(int i = 0; i < mpEvent.size()-1; i++){
            if(i != ignorePoint){
                int x = mpEvent.get(i).x;
                int y = mpEvent.get(i).y;
                for(int j = i+1; j < mpEvent.size(); j++){
                    if(j != ignorePoint){
                        d += Math.sqrt((Math.pow(x-mpEvent.get(j).x,2)+Math.pow(y-mpEvent.get(j).y,2)));
                    }
                }
            }
        }
        return d * (Math.random()+0.5);
    }

    public boolean colorToss(int probabilityOfTrue){
        if(probabilityOfTrue >= 100){
            return true;
        }else if(probabilityOfTrue <= 0){
            return false;
        }
        double temp = Math.random() * 100;
        Log.d("Sam", "colorTossRandomNumber:" + temp);
        return (temp < probabilityOfTrue);
    }

    public int getImageByString(String name){
        Resources res = getResources();
        return res.getIdentifier(name, "drawable", getPackageName());
    }

    public int getIdByString(String name){
        Resources res = getResources();
        return res.getIdentifier(name, "id", getPackageName());
    }

    @Override
    protected void onDestroy(){
        handler.removeCallbacks(clockColonControl);
        super.onDestroy();
    }
}

class MapEvent{
    int x;
    int y;
    int eventType;// global.SHOP global.NORMAL_BATTLE global.BOSS_BATTLE global.EVENT
    BattleInfo bi;
    int randomEvent = -1;

    public MapEvent(){
    }

    public MapEvent(int x, int y, int eventType, BattleInfo bi){
        this.x = x;
        this.y = y;
        this.eventType = eventType;
        this.bi = bi;
    }

    public MapEvent(int x, int y, int eventType, int randomEvent){
        this.x = x;
        this.y = y;
        this.eventType = eventType;
        this.randomEvent = randomEvent;
    }
}