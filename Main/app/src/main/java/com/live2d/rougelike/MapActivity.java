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

import static com.live2d.rougelike.CharacterPlateView.ACCELE;
import static com.live2d.rougelike.CharacterPlateView.BLAST_HORIZONTAL;
import static com.live2d.rougelike.CharacterPlateView.BLAST_VERTICAL;
import static com.live2d.rougelike.CharacterPlateView.CHARGE;

public class MapActivity extends AppCompatActivity implements View.OnTouchListener{

    public static final int EVENT = 1;
    public static final int NORMAL_BATTLE = 2;
    public static final int BOSS_BATTLE = 3;
    public static final int SHOP = 4;
    public static final int MODE_NONE = 0;//无操作
    public static final int MODE_DRAG = 1;//单指操作
    public static final int MODE_SCALE = 2;//双指操作
    public static final int EXPLORE_RADIUS = 200;//相对于4096*2048的地图而言
    public static final int EVENT_NUMBER = 2;
    public static float mapX = 1720;
    public static float mapY = 855;
    public static boolean isMapSizeTransferred = false;
    public static float scaleMultiple = 2.464692f;//缩放倍数

    //医疗中心出口 MapX:1720.0, MapY:855.0, Scale:2.464692

    public static boolean isSimpleMap = true;

    //用来记录地图上的事件点，每次移动角色时清空，在加载本activity时会依据其长度而决定是否补充event
    public static ArrayList<MapEvent> mpEvent = new ArrayList<>();
    public static ArrayList<Pair<Effect, Integer>> effectPool = new ArrayList<>();
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
    private List<String> commonBossNameList = Arrays.asList("monster_钟摆的魔女", "monster_立耳的魔女",
            "monster_Flower Speaker之谣", "monster_沙地的魔女", "monster_羊之魔女", "monster_屋顶的魔女",
            "monster_保护孩子的魔女", "monster_生神的魔女", "monster_班长的魔女", "monster_玫瑰园的魔女");
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
        findView();
        if(effectPool.size() == 0){
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
                map_event_detail_frame.setVisibility(View.GONE);
                if(MODE == MODE_DRAG){//单指滑动时
                    mapX += (int) (event.getX() - startPointF.x);
                    mapY += (int) (event.getY() - startPointF.y);
                    ViewHelper.setX(kamihamaMap, mapX);
                    ViewHelper.setY(kamihamaMap, mapY);
                }else if(MODE == MODE_SCALE){//双指滑动时
                    //计算缩放倍数
                    scaleMultiple = scaleMultiple * getDistance(event) / distance;
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
                if(mapX > ((screenWidth * scaleMultiple) / 2 - screenWidth / 4.0f)){
                    mapX = (screenWidth * scaleMultiple) / 2 - screenWidth / 4.0f;
                }else if(mapX < -screenWidth * scaleMultiple / 2 + screenWidth / 4.0f){
                    mapX = -(screenWidth * scaleMultiple) / 2 + screenWidth / 4.0f;
                }
                if(mapY > (screenHeight * scaleMultiple) / 2 - screenHeight / 4.0f){
                    mapY = (screenHeight * scaleMultiple) / 2 - screenHeight / 4.0f;
                }else if(mapY < -screenHeight * scaleMultiple / 2 + screenHeight / 4.0f){
                    mapY = -(screenHeight * scaleMultiple) / 2 + screenHeight / 4.0f;
                }
                ViewHelper.setX(kamihamaMap, mapX);
                ViewHelper.setY(kamihamaMap, mapY);
                //手指离开后，重置状态
                MODE = MODE_NONE;
                break;
        }
        Log.d("Sam","MapX:" + mapX + ", MapY:" + mapY + ", Scale:" + scaleMultiple);
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

        //恢复上次地图
        if(isSimpleMap){
            kamihamaMap.setBackgroundResource(R.drawable.kamihama_map);
        }else{
            kamihamaMap.setBackgroundResource(R.drawable.kamihama_map_mark);
        }

        if(!isMapSizeTransferred){
            for(int i = 0; i < StartActivity.mapRandomPoint.length; i++){
                StartActivity.mapRandomPoint[i][0] = (int) (1.0f * StartActivity.mapRandomPoint[i][0] / 4096.0f * screenWidth);
                StartActivity.mapRandomPoint[i][1] = (int) (1.0f * StartActivity.mapRandomPoint[i][1] / 2048.0f * screenHeight);
            }
            StartActivity.PLAYER_ON_MAP_X = (int) (1.0f * StartActivity.PLAYER_ON_MAP_X / 4096.0f * screenWidth);
            StartActivity.PLAYER_ON_MAP_Y = (int) (1.0f * StartActivity.PLAYER_ON_MAP_Y / 2048.0f * screenHeight);
            isMapSizeTransferred = true;
        }

        //初始地点
        //int temp = (int)(Math.random()*StartActivity.mapRandomPoint.length);
        leaderX = StartActivity.PLAYER_ON_MAP_X;
        leaderY = StartActivity.PLAYER_ON_MAP_Y;
        Log.d("Sam", "leaderX:" + leaderX + ", leaderY:" + leaderY);

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
                if(StartActivity.ccNumber >= StartActivity.COST_FOR_SUMMON_ADJUSTMENT_HOUSE){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                    final AlertDialog dialog = builder.create();

                    View dialog_frame = LayoutInflater.from(MapActivity.this).inflate(R.layout.alert_dialog_frame, null);
                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_title_name)).setText("购买服务");
                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("是否花费 " + StartActivity.COST_FOR_SUMMON_ADJUSTMENT_HOUSE + "cc 购买调整屋的上门服务？\n(当前地图事件会更新)");
                    ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).removeAllViews();
                    //((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).addView();
                    ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(null);
                    (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            if(!isIntentSend){
                                StartActivity.ccNumber -= StartActivity.COST_FOR_SUMMON_ADJUSTMENT_HOUSE;
                                mpEvent.clear();
                                StartActivity.gameTime += 0.5f;
                                if(StartActivity.collectionDict.get("幽灵执照").isOwn){
                                    if(colorToss(25)){
                                        StartActivity.gameTime -= 0.5f;
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
                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("购买调整屋的上门服务需要 " + StartActivity.COST_FOR_SUMMON_ADJUSTMENT_HOUSE + "cc, cc不足");
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
        MapCollectionAdapter mapCollectionAdapter = new MapCollectionAdapter(MapActivity.this, StartActivity.collectionList);
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
                if(isSimpleMap){
                    kamihamaMap.setBackgroundResource(R.drawable.kamihama_map_mark);
                }else{
                    kamihamaMap.setBackgroundResource(R.drawable.kamihama_map);
                }
                isSimpleMap = !isSimpleMap;
            }
        });

        //若spriteViewer没有正常启动，则显示备用小人
        Message tempM = new Message();
        tempM.what = 1;
        handler.sendMessageDelayed(tempM, 3000);
    }

    void updateCCAndGriefSeedView(){
        cc_number.setText(" " + StartActivity.ccNumber + " ");
        grief_seed_number.setText(" " + StartActivity.griefSeedNumber + " ");
    }

    void setClock(){
        float t = StartActivity.gameTime;
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
        int finalEventNumber = EVENT_NUMBER;
        if(StartActivity.collectionDict.get("红蓝蜡烛").isOwn){
            finalEventNumber += 2;
        }
        if(mpEvent.size() < finalEventNumber){
            //选择所有可能的点
            for(int i = 0; i < StartActivity.mapRandomPoint.length; i++){
                //1.在leader的一定距离范围内
                int tempX = StartActivity.mapRandomPoint[i][0];
                int tempY = StartActivity.mapRandomPoint[i][1];
                if(Math.pow(tempX - leaderX, 2) + Math.pow(tempY - leaderY, 2) <= EXPLORE_RADIUS * EXPLORE_RADIUS){
                    //2.不能在leader的图像范围内 80*110 与 25*32
                    if(!(Math.pow(tempX - leaderX, 2) + Math.pow(tempY - 16 - leaderY + 55, 2) <= Math.pow(80, 2))){
                        //if(!(Math.abs(tempX-12-leaderX) <= 40 && Math.abs(tempY-16-leaderY+55) <= 55)){
                        //3.不能和已经触发的点图像重叠 25*32
                        boolean isOverlap = false;
                        for(int j = 0; j < mpEvent.size(); j++){
                            int tempJx = mpEvent.get(j).x;
                            int tempJy = mpEvent.get(j).y;
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
                            mpEvent.add(mpe);
                            Log.d("Sam", "addEventPoint:" + tempX + "," + tempY);
                        }
                    }
                }
            }

            //筛选点
            Collections.shuffle(mpEvent);
            while(mpEvent.size() > finalEventNumber){
                int markPoint = -1;
                double maxD = -1d;
                for(int i = 0; i < mpEvent.size(); i++){
                    //计算除去某个点后所有点之间的距离，最终remove掉距离总和最大的点
                    double tempD = calculateSumDistance(mpEvent, i);
                    if(tempD > maxD){
                        maxD = tempD;
                        markPoint = i;
                    }
                }
                mpEvent.remove(markPoint);
                Log.d("Sam","removePoint:" + markPoint);
            }



            while(mpEvent.size() > finalEventNumber){
                int randomId = (int) (Math.random() * mpEvent.size());
                mpEvent.remove(randomId);
            }

            //为剩下的点添加事件
            for(int i = 0; i < mpEvent.size(); i++){
                MapEvent mpe = mpEvent.get(i);
                double tempRandom = Math.random();
                if(tempRandom < 0.4d){
                    //普通战斗
                    mpe.eventType = NORMAL_BATTLE;
                    mpe.bi = generateRandomBattle(mpe.x, mpe.y, false);
                }else if(tempRandom < 0.6d){
                    //魔女战斗
                    mpe.eventType = BOSS_BATTLE;
                    mpe.bi = generateRandomBattle(mpe.x, mpe.y, true);
                }else if(tempRandom < 0.9d){
                    //事件
                    mpe.eventType = EVENT;
                    mpe.bi = null;
                }else{
                    //商店
                    mpe.eventType = SHOP;
                }
            }

        }


        //在地图上生成筛选后的点
        for(int i = 0; i < mpEvent.size(); i++){
            final MapEvent mpe = mpEvent.get(i);
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

            if(mpe.eventType == NORMAL_BATTLE){
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

                        ExtraMission em = StartActivity.extraMissionList.get(mpe.bi.extraMissionId);
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
            }else if(mpe.eventType == BOSS_BATTLE){
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

                        ExtraMission em = StartActivity.extraMissionList.get(mpe.bi.extraMissionId);
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
            }else if(mpe.eventType == EVENT){
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
                                StartActivity.PLAYER_ON_MAP_X = mpe.x;
                                StartActivity.PLAYER_ON_MAP_Y = mpe.y;
                                if(!isIntentSend){
                                    if(StartActivity.collectionDict.get("便携式照相机").isOwn){
                                        StartActivity.ccNumber += 500;
                                    }
                                    MapActivity.mpEvent.clear();
                                    StartActivity.gameTime += 0.5f;
                                    if(StartActivity.collectionDict.get("幽灵执照").isOwn){
                                        if(colorToss(25)){
                                            StartActivity.gameTime -= 0.5f;
                                        }
                                    }
                                    Intent intent1 = new Intent(MapActivity.this, DialogActivity.class);
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
                                StartActivity.PLAYER_ON_MAP_X = mpe.x;
                                StartActivity.PLAYER_ON_MAP_Y = mpe.y;
                                if(!isIntentSend){
                                    MapActivity.mpEvent.clear();
                                    StartActivity.gameTime += 0.5f;
                                    if(StartActivity.collectionDict.get("幽灵执照").isOwn){
                                        if(colorToss(25)){
                                            StartActivity.gameTime -= 0.5f;
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
        effectPool = new ArrayList<>();
        effectPool.add(new Pair<>(new Effect("攻击力UP", 15, 999, 100, 0), 2));
        effectPool.add(new Pair<>(new Effect("攻击力UP", 35, 999, 100, 0), 4));
        effectPool.add(new Pair<>(new Effect("攻击力UP", 65, 999, 100, 0), 8));
        effectPool.add(new Pair<>(new Effect("攻击力DOWN", 20, 999, 100, 0), -3));
        effectPool.add(new Pair<>(new Effect("攻击力DOWN", 40, 999, 100, 0), -5));
        effectPool.add(new Pair<>(new Effect("防御力UP", 15, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("防御力UP", 35, 999, 100, 0), 2));
        effectPool.add(new Pair<>(new Effect("防御力UP", 65, 999, 100, 0), 4));
        effectPool.add(new Pair<>(new Effect("防御力DOWN", 20, 999, 100, 0), -2));
        effectPool.add(new Pair<>(new Effect("防御力DOWN", 40, 999, 100, 0), -4));
        effectPool.add(new Pair<>(new Effect("造成伤害UP", 20, 999, 100, 0), 2));
        effectPool.add(new Pair<>(new Effect("造成伤害UP", 40, 999, 100, 0), 4));
        effectPool.add(new Pair<>(new Effect("造成伤害UP", 70, 999, 100, 0), 8));
        effectPool.add(new Pair<>(new Effect("造成伤害DOWN", 25, 999, 100, 0), -3));
        effectPool.add(new Pair<>(new Effect("造成伤害DOWN", 45, 999, 100, 0), -5));
        effectPool.add(new Pair<>(new Effect("伤害削减", 20, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("伤害削减", 40, 999, 100, 0), 2));
        effectPool.add(new Pair<>(new Effect("伤害削减", 70, 999, 100, 0), 4));
        effectPool.add(new Pair<>(new Effect("Magia伤害削减", 40, 999, 100, 0), 2));
        effectPool.add(new Pair<>(new Effect("Magia伤害削减", 70, 999, 100, 0), 4));
        effectPool.add(new Pair<>(new Effect("异常状态耐性UP", 20, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("异常状态耐性UP", 40, 999, 100, 0), 2));
        effectPool.add(new Pair<>(new Effect("异常状态耐性UP", 90, 999, 100, 0), 3));
        effectPool.add(new Pair<>(new Effect("异常状态耐性DOWN", 15, 999, 100, 0), -1));
        effectPool.add(new Pair<>(new Effect("异常状态耐性DOWN", 35, 999, 100, 0), -2));
        effectPool.add(new Pair<>(new Effect("濒死时防御力UP", 20, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("濒死时防御力UP", 40, 999, 100, 0), 2));
        effectPool.add(new Pair<>(new Effect("濒死时防御力UP", 90, 999, 100, 0), 4));
        effectPool.add(new Pair<>(new Effect("濒死时攻击力UP", 20, 999, 100, 0), 2));
        effectPool.add(new Pair<>(new Effect("濒死时攻击力UP", 40, 999, 100, 0), 3));
        effectPool.add(new Pair<>(new Effect("濒死时攻击力UP", 90, 999, 100, 0), 6));
        effectPool.add(new Pair<>(new Effect("HP自动回复", 3, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("HP自动回复", 5, 999, 100, 0), 3));
        effectPool.add(new Pair<>(new Effect("HP自动回复", 10, 999, 100, 0), 5));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态雾", 25, 999, 15, 2), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态雾", 25, 999, 35, 3), 5));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态雾", 25, 999, 65, 4), 7));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态黑暗", 35, 999, 15, 2), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态黑暗", 35, 999, 35, 3), 5));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态黑暗", 35, 999, 85, 4), 8));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态幻惑", 50, 999, 15, 2), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态幻惑", 50, 999, 35, 3), 5));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态幻惑", 50, 999, 75, 4), 8));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态毒", 5, 999, 25, 3), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态毒", 5, 999, 35, 4), 4));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态毒", 5, 999, 60, 5), 6));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态烧伤", 10, 999, 20, 2), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态烧伤", 10, 999, 35, 3), 4));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态烧伤", 10, 999, 60, 4), 7));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态诅咒", 15, 999, 15, 2), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态诅咒", 15, 999, 40, 4), 5));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态诅咒", 15, 999, 60, 5), 8));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态魅惑", 0, 999, 15, 2), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态魅惑", 0, 999, 40, 3), 3));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态魅惑", 0, 999, 60, 3), 5));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态眩晕", 0, 999, 15, 2), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态眩晕", 0, 999, 40, 3), 3));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态眩晕", 0, 999, 60, 4), 5));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态拘束", 0, 999, 15, 2), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态拘束", 0, 999, 40, 3), 3));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态拘束", 0, 999, 60, 3), 5));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态Magia封印", 0, 999, 15, 2), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态Magia封印", 0, 999, 40, 3), 3));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态技能封印", 0, 999, 25, 2), 3));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态技能封印", 0, 999, 50, 3), 4));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态HP回复禁止", 0, 999, 30, 2), 1));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态HP回复禁止", 0, 999, 60, 3), 2));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态MP回复禁止", 0, 999, 25, 2), 3));
        effectPool.add(new Pair<>(new Effect("攻击时给予状态MP回复禁止", 0, 999, 50, 3), 4));
        effectPool.add(new Pair<>(new Effect("挑拨", 0, 999, 25, 0), 2));
        effectPool.add(new Pair<>(new Effect("挑拨", 0, 999, 50, 0), 3));
        effectPool.add(new Pair<>(new Effect("回避", 0, 999, 25, 0), 3));
        effectPool.add(new Pair<>(new Effect("回避", 0, 999, 50, 0), 5));
        effectPool.add(new Pair<>(new Effect("回避", 0, 999, 80, 0), 8));
        effectPool.add(new Pair<>(new Effect("暴击", 200, 999, 10, 0), 2));
        effectPool.add(new Pair<>(new Effect("暴击", 200, 999, 40, 0), 4));
        effectPool.add(new Pair<>(new Effect("暴击", 200, 999, 80, 0), 8));
        effectPool.add(new Pair<>(new Effect("回避无效", 0, 999, 40, 0), 1));
        effectPool.add(new Pair<>(new Effect("回避无效", 0, 999, 100, 0), 2));
        effectPool.add(new Pair<>(new Effect("毒无效", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("挑拨无效", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("诅咒无效", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("雾无效", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("忍耐", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("烧伤无效", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("黑暗无效", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("拘束无效", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("眩晕无效", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("幻惑无效", 0, 999, 100, 0), 1));
        effectPool.add(new Pair<>(new Effect("无视防御力", 0, 999, 40, 0), 4));
        effectPool.add(new Pair<>(new Effect("无视防御力", 0, 999, 80, 0), 8));
        effectPool.add(new Pair<>(new Effect("伤害削减无效", 0, 999, 50, 0), 2));
        effectPool.add(new Pair<>(new Effect("伤害削减无效", 0, 999, 100, 0), 4));
    }

    public BattleInfo generateRandomBattle(int x, int y, boolean isBossBattle){
        Log.d("Sam", "generateRandomBattle");
        BattleInfo bi = new BattleInfo();
        bi.backgroundId = (int) (Math.random() * StartActivity.JUNCTION_BACKGROUND_IMAGE_LIST.size());
        bi.isBossBattle = isBossBattle;
        bi.battleName = isBossBattle ? "魔女的气息..." : "有使魔在活动！";

        int sumPoint = (StartActivity.gameTime <= 11.01f) ? 2 : (StartActivity.gameTime <= 15.01f ? 8 : 16);

        Log.d("Sam", "generateTotalBuff");
        bi.useEffect = new ArrayList<>();
        //随机战斗的总buff池
        ArrayList<String> randomBuffChoice = StartActivity.ENEMY_RANDOM_BUFF_DICT.get(sumPoint);
        String tempChoice = randomBuffChoice.get((int) (Math.random() * randomBuffChoice.size()));
        Log.d("Sam", "useBuffChoice:" + tempChoice);
        for(int j = 0; j < effectPool.size(); j++){
            if(tempChoice.substring(j, j + 1).equals("1")){
                bi.useEffect.add(new Pair<>(new Effect(effectPool.get(j).first), effectPool.get(j).second));
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

        if(StartActivity.gameTime < 8.01f){
            bi.recommendLV = "1";
        }else if(StartActivity.gameTime < 10.01f){
            bi.recommendLV = "20";
        }else if(StartActivity.gameTime < 12.01f){
            bi.recommendLV = "40";
        }else if(StartActivity.gameTime < 14.01f){
            bi.recommendLV = "60";
        }else if(StartActivity.gameTime < 16.01f){
            bi.recommendLV = "80";
        }else if(StartActivity.gameTime < 18.01f){
            bi.recommendLV = "100";
        }
        bi.extraMissionId = (int) (Math.random() * StartActivity.extraMissionList.size());
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
        if(!isBossBattle){
            if(StartActivity.gameTime < 8.01f){
                c.lv = 1;
                c.HP = 4500;
                c.realHP = c.HP;
                c.ATK = 750;
                c.DEF = 1000;
            }else if(StartActivity.gameTime < 10.01f){
                c.lv = 20;
                c.HP = 9800;
                c.realHP = c.HP;
                c.ATK = 1500;
                c.DEF = 4500;
            }else if(StartActivity.gameTime < 12.01f){
                c.lv = 40;
                c.HP = 15000;
                c.realHP = c.HP;
                c.ATK = 2250;
                c.DEF = 6500;
            }else if(StartActivity.gameTime < 14.01f){
                c.lv = 60;
                c.HP = 35000;
                c.realHP = c.HP;
                c.ATK = 4800;
                c.DEF = 7000;
            }else if(StartActivity.gameTime < 16.01f){
                c.lv = 80;
                c.HP = 80000;
                c.realHP = c.HP;
                c.ATK = 9000;
                c.DEF = 10500;
            }else if(StartActivity.gameTime < 18.01f){
                c.lv = 100;
                c.HP = 150000;
                c.realHP = c.HP;
                c.ATK = 14400;
                c.DEF = 13000;
            }
        }else{
            if(StartActivity.gameTime < 8.01f){
                c.lv = 1;
                c.HP = 16000;
                c.realHP = c.HP;
                c.ATK = 1500;
                c.DEF = 1300;
            }else if(StartActivity.gameTime < 10.01f){
                c.lv = 20;
                c.HP = 34300;
                c.realHP = c.HP;
                c.ATK = 2875;
                c.DEF = 4750;
            }else if(StartActivity.gameTime < 12.01f){
                c.lv = 40;
                c.HP = 52500;
                c.realHP = c.HP;
                c.ATK = 4700;
                c.DEF = 7000;
            }else if(StartActivity.gameTime < 14.01f){
                c.lv = 60;
                c.HP = 122500;
                c.realHP = c.HP;
                c.ATK = 8700;
                c.DEF = 9000;
            }else if(StartActivity.gameTime < 16.01f){
                c.lv = 80;
                c.HP = 280000;
                c.realHP = c.HP;
                c.ATK = 14000;
                c.DEF = 15500;
            }else if(StartActivity.gameTime < 18.01f){
                c.lv = 100;
                c.HP = 525000;
                c.realHP = c.HP;
                c.ATK = 18000;
                c.DEF = 20000;
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
}

class MapEvent{
    int x;
    int y;
    int eventType;// SHOP NORMAL_BATTLE BOSS_BATTLE EVENT
    BattleInfo bi;

    public MapEvent(){
    }

    public MapEvent(int x, int y, int eventType, BattleInfo bi){
        this.x = x;
        this.y = y;
        this.eventType = eventType;
        this.bi = bi;
    }
}