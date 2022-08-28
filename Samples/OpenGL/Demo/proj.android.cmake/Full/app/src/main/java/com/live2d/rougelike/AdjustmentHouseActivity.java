package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.Image;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import static com.live2d.rougelike.CharacterPlateView.ACCELE;
import static com.live2d.rougelike.CharacterPlateView.BLAST_HORIZONTAL;
import static com.live2d.rougelike.CharacterPlateView.BLAST_VERTICAL;
import static com.live2d.rougelike.CharacterPlateView.CHARGE;

public class AdjustmentHouseActivity extends AppCompatActivity {

    int changePlateTo = -1;

    GLSurfaceView _glSurfaceView;

    LinearLayout live2dContainer;

    ImageView character_breakthrough;
    ImageView character_plate_change;
    ImageView character_star_up;
    ImageView memoria_breakthrough;
    LinearLayout right_item_container;
    TextView cardNumber;
    ImageView order;
    ImageView orderBy;
    RecyclerView cardsRecyclerView;
    ImageView back;
    LinearLayout memoria_list;

    ColorMatrixColorFilter grayColorFilter;//用于灰度设置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjustment_house);
        findView();
        initLive2d();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Sam","onPause");
//        _glSurfaceView.onPause();
//        JniBridgeJava.nativeOnPause();
//        JniBridgeJava.nativeOnStop();
//        JniBridgeJava.nativeOnStart();
//        _glSurfaceView.onResume();
//        final Timer live2dTimer = new Timer();
//        TimerTask live2dTimerTask = new TimerTask() {
//            @Override
//            public void run() {
//                JniBridgeJava.nativeSetCharacter(
//                        "101700","001","011",
//                        "","","",
////                        "101700","100","010",
//                        "","",""
//                );
//                live2dTimer.cancel();
//            }
//        };
//        live2dTimer.schedule(live2dTimerTask,500,50);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        changeBackground("11061");
    }

    public void initView(){
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        grayColorFilter = new ColorMatrixColorFilter(cm);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AdjustmentHouseActivity.this, TeamChooseActivity.class);
                intent1.putExtra("battleInfo",0);
                startActivity(intent1);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
        character_breakthrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_item_container.removeAllViews();
                right_item_container.setVisibility(View.VISIBLE);
                memoria_list.setVisibility(View.GONE);
                for(int i = 0; i < StartActivity.characterList.size(); i++){
                    final Character c = StartActivity.characterList.get(i);
                    View item = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.character_break_through_item, right_item_container, false);
                    right_item_container.addView(item);
                    ImageView charImage = item.findViewById(R.id.left_char_image);
                    ConstraintLayout charFrame = item.findViewById(R.id.left_char_frame);
                    ImageView charAttr = item.findViewById(R.id.left_char_attr);
                    TextView lvView = item.findViewById(R.id.left_lv_view);
                    LinearLayout starsLayout = item.findViewById(R.id.left_stars_layout);
                    ImageView[] char_star = new ImageView[]{item.findViewById(R.id.left_char_star0),item.findViewById(R.id.left_char_star1),
                            item.findViewById(R.id.left_char_star2),item.findViewById(R.id.left_char_star3),item.findViewById(R.id.left_char_star4)};
                    ImageView[] slot_list = new ImageView[]{item.findViewById(R.id.left_slot_1),item.findViewById(R.id.left_slot_2),item.findViewById(R.id.left_slot_3),
                            item.findViewById(R.id.right_slot_1),item.findViewById(R.id.right_slot_2),item.findViewById(R.id.right_slot_3)};
                    ConstraintLayout purchase_button = item.findViewById(R.id.purchase_button);
                    TextView item_price = item.findViewById(R.id.item_price);
                    ImageView purchase_button_background = item.findViewById(R.id.purchase_button_background);
                    ImageView changeToArrow = item.findViewById(R.id.changeToArrow);

                    //init
                    charImage.setImageResource(getResourceByString(c.charIconImage+"d"));
                    charImage.setBackgroundResource(getResourceByString("bg_"+c.element));
                    charFrame.setBackgroundResource(getResourceByString("frame_rank_"+c.star));
                    charAttr.setImageResource(getResourceByString(c.element));
                    lvView.setText("Lv "+c.lv+" ");

                    for(int j = 0; j < 3; j++){
                        if(c.breakThrough > j + 1){
                            slot_list[j].setBackgroundResource(R.drawable.character_slot);
                        }else{
                            slot_list[j].setBackgroundResource(R.drawable.character_empty_slot);
                        }
                    }

                    if(c.breakThrough < 4){
                        for(int j = 0; j < 3; j++){
                            if(c.breakThrough > j){
                                slot_list[j+3].setBackgroundResource(R.drawable.character_slot);
                            }else{
                                slot_list[j+3].setBackgroundResource(R.drawable.character_empty_slot);
                            }
                        }
                        item_price.setText("-"+StartActivity.CHARACTER_BREAK_THROUGH_PRICE[c.breakThrough - 1]);
                        starsLayout.setVisibility(View.VISIBLE);
                        for(int j = 0; j < 5; j++){
                            char_star[j].setVisibility(j < c.star? View.VISIBLE:View.GONE);
                        }

                        if(StartActivity.CHARACTER_BREAK_THROUGH_PRICE[c.breakThrough - 1] > StartActivity.griefSeedNumber){
                            //买不起
                            purchase_button_background.setColorFilter(grayColorFilter);
                            purchase_button.setOnClickListener(null);
                        }else{
                            //买得起
                            purchase_button_background.setColorFilter(null);
                            purchase_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(AdjustmentHouseActivity.this);
                                    dialog.setTitle("魔力解放:"+c.name);//标题
                                    dialog.setMessage("将消耗"+StartActivity.CHARACTER_BREAK_THROUGH_PRICE[c.breakThrough - 1]+"个悲叹之种将魔力解放至"+(c.breakThrough+1)+"级.");//正文
                                    dialog.setCancelable(true);//是否能点击屏幕取消该弹窗
                                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //正确逻辑
                                            StartActivity.griefSeedNumber -= StartActivity.CHARACTER_BREAK_THROUGH_PRICE[c.breakThrough - 1];
                                            c.breakThrough++;
                                            character_breakthrough.performClick();
                                        }});
                                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //错误逻辑
                                        }});
                                    dialog.show();
                                }
                            });
                        }
                    }else{
                        //已将魔力解放至最大
                        for(int j = 0; j < 3; j++){
                            slot_list[j+3].setVisibility(View.GONE);
                        }
                        changeToArrow.setVisibility(View.GONE);

                        item_price.setText("--");
                        starsLayout.setVisibility(View.VISIBLE);
                        for(int j = 0; j < 5; j++){
                            char_star[j].setVisibility(j < c.star? View.VISIBLE:View.GONE);
                        }
                        purchase_button_background.setColorFilter(grayColorFilter);
                        purchase_button.setOnClickListener(null);
                    }

                }
            }
        });

        character_plate_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_item_container.removeAllViews();
                right_item_container.setVisibility(View.VISIBLE);
                memoria_list.setVisibility(View.GONE);
                for(int i = 0; i < StartActivity.characterList.size(); i++){
                    final Character c = StartActivity.characterList.get(i);
                    View item = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.character_plate_change_item, right_item_container, false);
                    right_item_container.addView(item);
                    ImageView charImage = item.findViewById(R.id.left_char_image);
                    ConstraintLayout charFrame = item.findViewById(R.id.left_char_frame);
                    ImageView charAttr = item.findViewById(R.id.left_char_attr);
                    TextView lvView = item.findViewById(R.id.left_lv_view);
                    LinearLayout starsLayout = item.findViewById(R.id.left_stars_layout);
                    ImageView[] char_star = new ImageView[]{item.findViewById(R.id.left_char_star0),item.findViewById(R.id.left_char_star1),
                            item.findViewById(R.id.left_char_star2),item.findViewById(R.id.left_char_star3),item.findViewById(R.id.left_char_star4)};

                    //init
                    final int price = StartActivity.CHARACTER_CHANGE_PLATE_PRICE[StartActivity.plate_change_time < StartActivity.CHARACTER_CHANGE_PLATE_PRICE.length? StartActivity.plate_change_time:(StartActivity.CHARACTER_CHANGE_PLATE_PRICE.length-1)];
                    charImage.setImageResource(getResourceByString(c.charIconImage+"d"));
                    charImage.setBackgroundResource(getResourceByString("bg_"+c.element));
                    charFrame.setBackgroundResource(getResourceByString("frame_rank_"+c.star));
                    charAttr.setImageResource(getResourceByString(c.element));
                    lvView.setText("Lv "+c.lv+" ");

                    for(int j = 0; j < 5; j++){
                        ImageView plate = item.findViewById(getIdByString("plate"+(j+1)));
                        switch(c.plateList[j]){
                            case ACCELE:
                                plate.setImageResource(R.drawable.accele_small_plate);
                                break;
                            case CHARGE:
                                plate.setImageResource(R.drawable.charge_small_plate);
                                break;
                            case BLAST_HORIZONTAL:
                                plate.setImageResource(R.drawable.blast_horizontal_small_plate);
                                break;
                            case BLAST_VERTICAL:
                                plate.setImageResource(R.drawable.blast_vertical_small_plate);
                                break;
                        }

                        final int tempJ = j;
                        plate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final View dialog_layout = getLayoutInflater().inflate(R.layout.plate_change_alert_dialog,null,false);
                                final ImageView origin_plate = dialog_layout.findViewById(R.id.origin_plate);
                                final ImageView accele_plate_choose_arrow = dialog_layout.findViewById(R.id.accele_plate_choose_arrow);
                                final ImageView charge_plate_choose_arrow = dialog_layout.findViewById(R.id.charge_plate_choose_arrow);
                                final ImageView blast_vertical_plate_choose_arrow = dialog_layout.findViewById(R.id.blast_vertical_plate_choose_arrow);
                                final ImageView blast_horizontal_plate_choose_arrow = dialog_layout.findViewById(R.id.blast_horizontal_plate_choose_arrow);
                                ImageView accele_plate = dialog_layout.findViewById(R.id.accele_plate);
                                ImageView charge_plate = dialog_layout.findViewById(R.id.charge_plate);
                                ImageView blast_vertical_plate = dialog_layout.findViewById(R.id.blast_vertical_plate);
                                ImageView blast_horizontal_plate = dialog_layout.findViewById(R.id.blast_horizontal_plate);
                                if(c.plateList[tempJ] == ACCELE){
                                    charge_plate_choose_arrow.setVisibility(View.VISIBLE);
                                    changePlateTo = CHARGE;
                                }else{
                                    accele_plate_choose_arrow.setVisibility(View.VISIBLE);
                                    changePlateTo = ACCELE;
                                }
                                switch(c.plateList[tempJ]){
                                    case ACCELE:
                                        origin_plate.setImageResource(R.drawable.accele_small_plate);
                                        LinearLayout accele_plate_layout = dialog_layout.findViewById(R.id.accele_plate_layout);
                                        accele_plate_layout.setVisibility(View.GONE);
                                        break;
                                    case CHARGE:
                                        origin_plate.setImageResource(R.drawable.charge_small_plate);
                                        LinearLayout charge_plate_layout = dialog_layout.findViewById(R.id.charge_plate_layout);
                                        charge_plate_layout.setVisibility(View.GONE);
                                        break;
                                    case BLAST_HORIZONTAL:
                                        origin_plate.setImageResource(R.drawable.blast_horizontal_small_plate);
                                        LinearLayout blast_horizontal_plate_layout = dialog_layout.findViewById(R.id.blast_horizontal_plate_layout);
                                        blast_horizontal_plate_layout.setVisibility(View.GONE);
                                        break;
                                    case BLAST_VERTICAL:
                                        origin_plate.setImageResource(R.drawable.blast_vertical_small_plate);
                                        LinearLayout blast_vertical_plate_layout = dialog_layout.findViewById(R.id.blast_vertical_plate_layout);
                                        blast_vertical_plate_layout.setVisibility(View.GONE);
                                        break;
                                }
                                accele_plate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clearPlateChosen(dialog_layout);
                                        accele_plate_choose_arrow.setVisibility(View.VISIBLE);
                                        changePlateTo = ACCELE;
                                    }
                                });
                                charge_plate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clearPlateChosen(dialog_layout);
                                        charge_plate_choose_arrow.setVisibility(View.VISIBLE);
                                        changePlateTo = CHARGE;
                                    }
                                });
                                blast_horizontal_plate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clearPlateChosen(dialog_layout);
                                        blast_horizontal_plate_choose_arrow.setVisibility(View.VISIBLE);
                                        changePlateTo = BLAST_HORIZONTAL;
                                    }
                                });
                                blast_vertical_plate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        clearPlateChosen(dialog_layout);
                                        blast_vertical_plate_choose_arrow.setVisibility(View.VISIBLE);
                                        changePlateTo = BLAST_VERTICAL;
                                    }
                                });


                                AlertDialog.Builder dialog = new AlertDialog.Builder(AdjustmentHouseActivity.this);
                                dialog.setTitle("行动盘替换: "+c.name);//标题
                                dialog.setCancelable(true);//是否能点击屏幕取消该弹窗
                                dialog.setView(dialog_layout);
                                if(price <= StartActivity.griefSeedNumber){
                                    dialog.setMessage("将消耗 "+ price +" 个悲叹之种对行动盘进行如下替换：");//正文
                                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //正确逻辑
                                            StartActivity.griefSeedNumber -= price;
                                            StartActivity.plate_change_time++;
                                            c.plateList[tempJ] = changePlateTo;
                                            character_plate_change.performClick();
                                        }});
                                }else{
                                    dialog.setMessage("需要消耗 "+ price +" 个悲叹之种，悲叹之种不足.");//正文
                                }

                                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //错误逻辑
                                    }});
                                dialog.show();
                            }
                        });
                    }

                }
            }
        });

        character_star_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                right_item_container.removeAllViews();
                right_item_container.setVisibility(View.VISIBLE);
                memoria_list.setVisibility(View.GONE);
                for(int i = 0; i < StartActivity.characterList.size(); i++){
                    final Character c = StartActivity.characterList.get(i);
                    View item = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.character_star_up_item, right_item_container, false);
                    right_item_container.addView(item);

                    //findLeft
                    ImageView charImage = item.findViewById(R.id.left_char_image);
                    ConstraintLayout charFrame = item.findViewById(R.id.left_char_frame);
                    ImageView charAttr = item.findViewById(R.id.left_char_attr);
                    TextView lvView = item.findViewById(R.id.left_lv_view);
                    LinearLayout starsLayout = item.findViewById(R.id.left_stars_layout);
                    ImageView[] char_star = new ImageView[]{item.findViewById(R.id.left_char_star0),item.findViewById(R.id.left_char_star1),
                            item.findViewById(R.id.left_char_star2),item.findViewById(R.id.left_char_star3),item.findViewById(R.id.left_char_star4)};
                    ImageView changeToArrow = item.findViewById(R.id.changeToArrow);


                    //initLeft
                    charImage.setImageResource(getResourceByString(c.charIconImage+"d"));
                    charImage.setBackgroundResource(getResourceByString("bg_"+c.element));
                    charFrame.setBackgroundResource(getResourceByString("frame_rank_"+c.star));
                    charAttr.setImageResource(getResourceByString(c.element));
                    lvView.setText("Lv "+c.lv);
                    starsLayout.setVisibility(View.VISIBLE);
                    for(int j = 0; j < 5; j++){
                        char_star[j].setVisibility(j < c.star? View.VISIBLE:View.GONE);
                    }

                    //findRight
                    ConstraintLayout right_char_Layout = item.findViewById(R.id.right_char_Layout);
                    charImage = item.findViewById(R.id.right_char_image);
                    charFrame = item.findViewById(R.id.right_char_frame);
                    charAttr = item.findViewById(R.id.right_char_attr);
                    lvView = item.findViewById(R.id.right_lv_view);
                    starsLayout = item.findViewById(R.id.right_stars_layout);
                    char_star = new ImageView[]{item.findViewById(R.id.right_char_star0),item.findViewById(R.id.right_char_star1),
                            item.findViewById(R.id.right_char_star2),item.findViewById(R.id.right_char_star3),item.findViewById(R.id.right_char_star4)};

                    //findButton
                    ConstraintLayout purchase_button = item.findViewById(R.id.purchase_button);
                    TextView item_price = item.findViewById(R.id.item_price);
                    ImageView purchase_button_background = item.findViewById(R.id.purchase_button_background);


                    if(c.star != 5){
                        //initRight
                        charImage.setImageResource(getResourceByString(c.charIconImage+"d"));
                        charImage.setBackgroundResource(getResourceByString("bg_"+c.element));
                        charFrame.setBackgroundResource(getResourceByString("frame_rank_"+c.star));
                        charAttr.setImageResource(getResourceByString(c.element));
                        lvView.setText("Lv "+c.lv);
                        for(int j = 0; j < 5; j++){
                            char_star[j].setVisibility(j <= c.star? View.VISIBLE:View.GONE);
                        }

                        item_price.setText("-"+StartActivity.CHARACTER_STAR_UP_PRICE);
                        starsLayout.setVisibility(View.VISIBLE);

                        if(StartActivity.CHARACTER_STAR_UP_PRICE > StartActivity.griefSeedNumber){
                            //买不起
                            purchase_button_background.setColorFilter(grayColorFilter);
                            purchase_button.setOnClickListener(null);
                        }else{
                            //买得起
                            purchase_button_background.setColorFilter(null);
                            purchase_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(AdjustmentHouseActivity.this);
                                    dialog.setTitle("星级提升: "+c.name);//标题
                                    dialog.setMessage("将消耗 "+StartActivity.CHARACTER_STAR_UP_PRICE+" 个悲叹之种将星级提升至 "+(c.star+1)+" 星.");//正文
                                    dialog.setCancelable(true);//是否能点击屏幕取消该弹窗
                                    dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //正确逻辑
                                            StartActivity.griefSeedNumber -= StartActivity.CHARACTER_STAR_UP_PRICE;
                                            c.star++;
                                            character_star_up.performClick();
                                        }});
                                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //错误逻辑
                                        }});
                                    dialog.show();
                                }
                            });
                        }
                    }else{
                        right_char_Layout.setVisibility(View.GONE);
                        changeToArrow.setVisibility(View.GONE);
                        purchase_button_background.setColorFilter(grayColorFilter);
                        purchase_button.setOnClickListener(null);
                        item_price.setText("--");
                    }
                }
            }
        });

        ImageView memoria_breakthrough;
        LinearLayout right_item_container;
        TextView cardNumber;
        ImageView order;
        ImageView orderBy;
        RecyclerView cardsRecyclerView;
        LinearLayout memoria_list;

        character_breakthrough.performClick();
    }

    public void initLive2d(){
        JniBridgeJava.SetActivityInstance(this);
        JniBridgeJava.SetContext(this);

        GLRenderer _glRenderer = new GLRenderer();
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(2);
        _glSurfaceView.setRenderer(_glRenderer);
        _glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        live2dContainer.addView(_glSurfaceView);
        final Timer live2dTimer = new Timer();
        TimerTask live2dTimerTask = new TimerTask() {
            @Override
            public void run() {
                JniBridgeJava.nativeSetCharacter(
                        "101700","001","011",
                        "","","",
//                        "101700","100","010",
                        "","",""
                );
                changeBackground("11061");
                live2dTimer.cancel();
            }
        };
        live2dTimer.schedule(live2dTimerTask,100,50);
        JniBridgeJava.nativeOnStart();
    }

    public void findView(){
        live2dContainer = findViewById(R.id.live2dContainer);
        character_breakthrough = findViewById(R.id.character_breakthrough);
        character_plate_change = findViewById(R.id.character_plate_change);
        character_star_up = findViewById(R.id.character_star_up);
        memoria_breakthrough = findViewById(R.id.memoria_breakthrough);
        right_item_container = findViewById(R.id.right_item_container);
        cardNumber = findViewById(R.id.cardNumber);
        order = findViewById(R.id.order);
        orderBy = findViewById(R.id.orderBy);
        cardsRecyclerView = findViewById(R.id.cardsRecyclerView);
        back = findViewById(R.id.back);
        memoria_list = findViewById(R.id.memoria_list);
    }

    public void clearPlateChosen(View dialog_layout){
        ImageView accele_plate_choose_arrow = dialog_layout.findViewById(R.id.accele_plate_choose_arrow);
        accele_plate_choose_arrow.setVisibility(View.INVISIBLE);
        ImageView charge_plate_choose_arrow = dialog_layout.findViewById(R.id.charge_plate_choose_arrow);
        charge_plate_choose_arrow.setVisibility(View.INVISIBLE);
        ImageView blast_vertical_plate_choose_arrow = dialog_layout.findViewById(R.id.blast_vertical_plate_choose_arrow);
        blast_vertical_plate_choose_arrow.setVisibility(View.INVISIBLE);
        ImageView blast_horizontal_plate_choose_arrow = dialog_layout.findViewById(R.id.blast_horizontal_plate_choose_arrow);
        blast_horizontal_plate_choose_arrow.setVisibility(View.INVISIBLE);
    }

    void changeBackground(String backgroundNumber){
        JniBridgeJava.nativeChangeBackground("bg_adv_"+backgroundNumber + ".png");
    }

    public int getResourceByString(String idName){
        return getResources().getIdentifier(idName,"drawable", getPackageName());
    }

    public int getIdByString(String idName){
        return getResources().getIdentifier(idName,"id", getPackageName());
    }
}