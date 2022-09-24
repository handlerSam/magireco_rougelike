package com.live2d.rougelike;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.KeyEvent.ACTION_DOWN;
import static com.live2d.rougelike.CharacterPlateView.ACCELE;
import static com.live2d.rougelike.CharacterPlateView.BLAST_HORIZONTAL;
import static com.live2d.rougelike.CharacterPlateView.BLAST_VERTICAL;
import static com.live2d.rougelike.CharacterPlateView.CHARGE;

public class AdjustmentHouseActivity extends AppCompatActivity{

    Global global;
    
    public Collection DEFAULT_COLLECTION;
    int changePlateTo = -1;
    GLSurfaceView _glSurfaceView;
    GLRenderer _glRenderer;
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
    ImageView shop_trade;
    ConstraintLayout shop_frame;
    LinearLayout shop_list_first_row;
    LinearLayout shop_list_second_row;
    ColorMatrixColorFilter grayColorFilter;//用于灰度设置
    Collection[][] shopCollectionList = new Collection[2][3];
    TextView cc_number;
    TextView grief_seed_number;

    ShopMemoriaAdapter memoriaAdapter;

    ConstraintLayout black_mask_adjustment_house;

    float live2dAlpha = 0.0f;

    boolean isIntentSend = false;

    Bitmap memoriaBreakthroughBitmap;

    Bitmap characterPlateChangeBitmap;

    boolean isTouchMemoriaBreakthroughTransparent = false;

    boolean isTouchCharacterPlateChangeTransparent = false;

    Handler handler = new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(@NonNull Message message){
            switch(message.what){
                case 0:
                    ObjectAnimator fadeOut = ObjectAnimator.ofFloat(black_mask_adjustment_house, "alpha", 1f, 0);
                    fadeOut.setDuration(500);
                    fadeOut.start();
                    break;
                default:
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        Log.d("Sam","adjustmentHouseActivityCreate");
        setContentView(R.layout.activity_adjustment_house);
        findView();
        initLive2d();
        initView();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        changeBackground("11061");
    }

    public void initView(){
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        grayColorFilter = new ColorMatrixColorFilter(cm);

        global = (Global)getApplicationContext();
        
        DEFAULT_COLLECTION = new Collection("", "", "", "", 0, "item_frame_3");
        DEFAULT_COLLECTION.isOwn = true;

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isIntentSend){
                    Intent intent1 = new Intent(AdjustmentHouseActivity.this, MapActivity.class);
                    startActivity(intent1);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    isIntentSend = true;
                }
            }
        });
        character_breakthrough.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                right_item_container.removeAllViews();
                right_item_container.setVisibility(View.VISIBLE);
                memoria_list.setVisibility(View.GONE);
                shop_frame.setVisibility(View.GONE);
                for(int i = 0; i < global.characterList.size(); i++){
                    final Character c = global.characterList.get(i);
                    View item = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.character_break_through_item, right_item_container, false);
                    right_item_container.addView(item);
                    ImageView charImage = item.findViewById(R.id.left_char_image);
                    ConstraintLayout charFrame = item.findViewById(R.id.left_char_frame);
                    ImageView charAttr = item.findViewById(R.id.left_char_attr);
                    TextView lvView = item.findViewById(R.id.left_lv_view);
                    LinearLayout starsLayout = item.findViewById(R.id.left_stars_layout);
                    ImageView[] char_star = new ImageView[]{item.findViewById(R.id.left_char_star0), item.findViewById(R.id.left_char_star1),
                            item.findViewById(R.id.left_char_star2), item.findViewById(R.id.left_char_star3), item.findViewById(R.id.left_char_star4)};
                    ImageView[] slot_list = new ImageView[]{item.findViewById(R.id.left_slot_1), item.findViewById(R.id.left_slot_2), item.findViewById(R.id.left_slot_3),
                            item.findViewById(R.id.right_slot_1), item.findViewById(R.id.right_slot_2), item.findViewById(R.id.right_slot_3)};
                    ConstraintLayout purchase_button = item.findViewById(R.id.purchase_button);
                    TextView item_price = item.findViewById(R.id.item_price);
                    ImageView purchase_button_background = item.findViewById(R.id.purchase_button_background);
                    ImageView changeToArrow = item.findViewById(R.id.changeToArrow);

                    //init
                    charImage.setImageResource(getResourceByString(c.charIconImage + "d"));
                    charImage.setBackgroundResource(getResourceByString("bg_" + c.element));
                    charFrame.setBackgroundResource(getResourceByString("frame_rank_" + c.star));
                    charAttr.setImageResource(getResourceByString(c.element));
                    lvView.setText("Lv " + c.lv + " ");

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
                                slot_list[j + 3].setBackgroundResource(R.drawable.character_slot);
                            }else{
                                slot_list[j + 3].setBackgroundResource(R.drawable.character_empty_slot);
                            }
                        }
                        item_price.setText("-" + global.CHARACTER_BREAK_THROUGH_PRICE[c.breakThrough - 1]);
                        starsLayout.setVisibility(View.VISIBLE);
                        for(int j = 0; j < 5; j++){
                            char_star[j].setVisibility(j < c.star ? View.VISIBLE : View.GONE);
                        }

                        if(global.CHARACTER_BREAK_THROUGH_PRICE[c.breakThrough - 1] > global.griefSeedNumber){
                            //买不起
                            purchase_button_background.setColorFilter(grayColorFilter);
                            purchase_button.setOnClickListener(null);
                        }else{
                            //买得起
                            purchase_button_background.setColorFilter(null);
                            purchase_button.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v){

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AdjustmentHouseActivity.this);
                                    final AlertDialog dialog = builder.create();

                                    View dialog_frame = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.alert_dialog_frame, null);
                                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_title_name)).setText("魔力解放:" + c.name);
                                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("将消耗" + global.CHARACTER_BREAK_THROUGH_PRICE[c.breakThrough - 1] + "个悲叹之种将魔力解放至" + (c.breakThrough + 1) + "级.");
                                    ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).removeAllViews();
                                    //((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).addView();
                                    ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(null);
                                    (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            //正确逻辑
                                            global.griefSeedNumber -= global.CHARACTER_BREAK_THROUGH_PRICE[c.breakThrough - 1];
                                            c.breakThrough++;
                                            updateCCAndGriefSeedView();
                                            character_breakthrough.performClick();
                                            dialog.dismiss();
                                            dialog.cancel();
                                        }
                                    });
                                    (dialog_frame.findViewById(R.id.alert_dialog_cancel_button)).setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            dialog.dismiss();
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                    dialog.getWindow().setContentView(dialog_frame);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                }
                            });
                        }
                    }else{
                        //已将魔力解放至最大
                        for(int j = 0; j < 3; j++){
                            slot_list[j + 3].setVisibility(View.GONE);
                        }
                        changeToArrow.setVisibility(View.GONE);

                        item_price.setText("--");
                        starsLayout.setVisibility(View.VISIBLE);
                        for(int j = 0; j < 5; j++){
                            char_star[j].setVisibility(j < c.star ? View.VISIBLE : View.GONE);
                        }
                        purchase_button_background.setColorFilter(grayColorFilter);
                        purchase_button.setOnClickListener(null);
                    }

                }
            }
        });
        character_plate_change.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == ACTION_DOWN){
                    if(characterPlateChangeBitmap.getPixel((int) event.getX(), (int) event.getY()) == 0){
                        isTouchCharacterPlateChangeTransparent = true;
                        if(2 * event.getY() > characterPlateChangeBitmap.getHeight()){
                            character_star_up.dispatchTouchEvent(event);
                        }else{
                            character_breakthrough.dispatchTouchEvent(event);
                        }
                    }else{
                        isTouchCharacterPlateChangeTransparent = false;
                    }
                    Log.d("Sam", "is CharacterPlateChange Touch Transparent:" + isTouchCharacterPlateChangeTransparent);
                }
                return false;
            }
        });
        characterPlateChangeBitmap = ((BitmapDrawable) character_plate_change.getDrawable()).getBitmap();
        character_plate_change.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isTouchCharacterPlateChangeTransparent){
                    right_item_container.removeAllViews();
                    right_item_container.setVisibility(View.VISIBLE);
                    memoria_list.setVisibility(View.GONE);
                    shop_frame.setVisibility(View.GONE);
                    for(int i = 0; i < global.characterList.size(); i++){
                        final Character c = global.characterList.get(i);
                        View item = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.character_plate_change_item, right_item_container, false);
                        right_item_container.addView(item);
                        ImageView charImage = item.findViewById(R.id.left_char_image);
                        ConstraintLayout charFrame = item.findViewById(R.id.left_char_frame);
                        ImageView charAttr = item.findViewById(R.id.left_char_attr);
                        TextView lvView = item.findViewById(R.id.left_lv_view);
                        LinearLayout starsLayout = item.findViewById(R.id.left_stars_layout);
                        ImageView[] char_star = new ImageView[]{item.findViewById(R.id.left_char_star0), item.findViewById(R.id.left_char_star1),
                                item.findViewById(R.id.left_char_star2), item.findViewById(R.id.left_char_star3), item.findViewById(R.id.left_char_star4)};

                        //init
                        final int price = global.CHARACTER_CHANGE_PLATE_PRICE[global.plate_change_time < global.CHARACTER_CHANGE_PLATE_PRICE.length ? global.plate_change_time : (global.CHARACTER_CHANGE_PLATE_PRICE.length - 1)];
                        charImage.setImageResource(getResourceByString(c.charIconImage + "d"));
                        charImage.setBackgroundResource(getResourceByString("bg_" + c.element));
                        charFrame.setBackgroundResource(getResourceByString("frame_rank_" + c.star));
                        charAttr.setImageResource(getResourceByString(c.element));
                        lvView.setText("Lv " + c.lv + " ");

                        for(int j = 0; j < 5; j++){
                            ImageView plate = item.findViewById(getIdByString("plate" + (j + 1)));
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
                            plate.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v){
                                    final View dialog_layout = getLayoutInflater().inflate(R.layout.plate_change_alert_dialog, null, false);
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
                                    accele_plate.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            clearPlateChosen(dialog_layout);
                                            accele_plate_choose_arrow.setVisibility(View.VISIBLE);
                                            changePlateTo = ACCELE;
                                        }
                                    });
                                    charge_plate.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            clearPlateChosen(dialog_layout);
                                            charge_plate_choose_arrow.setVisibility(View.VISIBLE);
                                            changePlateTo = CHARGE;
                                        }
                                    });
                                    blast_horizontal_plate.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            clearPlateChosen(dialog_layout);
                                            blast_horizontal_plate_choose_arrow.setVisibility(View.VISIBLE);
                                            changePlateTo = BLAST_HORIZONTAL;
                                        }
                                    });
                                    blast_vertical_plate.setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            clearPlateChosen(dialog_layout);
                                            blast_vertical_plate_choose_arrow.setVisibility(View.VISIBLE);
                                            changePlateTo = BLAST_VERTICAL;
                                        }
                                    });

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AdjustmentHouseActivity.this);
                                    final AlertDialog dialog = builder.create();

                                    View dialog_frame = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.alert_dialog_frame, null);
                                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_title_name)).setText("行动盘替换: " + c.name);
                                    ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).removeAllViews();
                                    ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).addView(dialog_layout);

                                    if(price <= global.griefSeedNumber){
                                        ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("将消耗 " + price + " 个悲叹之种对行动盘进行如下替换：");
                                        ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(null);
                                        (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v){
                                                //正确逻辑
                                                global.griefSeedNumber -= price;
                                                global.plate_change_time++;
                                                c.plateList[tempJ] = changePlateTo;
                                                updateCCAndGriefSeedView();
                                                character_plate_change.performClick();
                                                dialog.dismiss();
                                                dialog.cancel();
                                            }
                                        });
                                    }else{
                                        ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("需要消耗 " + price + " 个悲叹之种，悲叹之种不足.");
                                        ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(grayColorFilter);
                                        (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v){
                                                dialog.dismiss();
                                                dialog.cancel();
                                            }
                                        });
                                    }
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
                                }
                            });
                        }

                    }
                }
            }
        });

        character_star_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                right_item_container.removeAllViews();
                right_item_container.setVisibility(View.VISIBLE);
                memoria_list.setVisibility(View.GONE);
                shop_frame.setVisibility(View.GONE);
                for(int i = 0; i < global.characterList.size(); i++){
                    final Character c = global.characterList.get(i);
                    View item = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.character_star_up_item, right_item_container, false);
                    right_item_container.addView(item);

                    //findLeft
                    ImageView charImage = item.findViewById(R.id.left_char_image);
                    ConstraintLayout charFrame = item.findViewById(R.id.left_char_frame);
                    ImageView charAttr = item.findViewById(R.id.left_char_attr);
                    TextView lvView = item.findViewById(R.id.left_lv_view);
                    LinearLayout starsLayout = item.findViewById(R.id.left_stars_layout);
                    ImageView[] char_star = new ImageView[]{item.findViewById(R.id.left_char_star0), item.findViewById(R.id.left_char_star1),
                            item.findViewById(R.id.left_char_star2), item.findViewById(R.id.left_char_star3), item.findViewById(R.id.left_char_star4)};
                    ImageView changeToArrow = item.findViewById(R.id.changeToArrow);


                    //initLeft
                    charImage.setImageResource(getResourceByString(c.charIconImage + "d"));
                    charImage.setBackgroundResource(getResourceByString("bg_" + c.element));
                    charFrame.setBackgroundResource(getResourceByString("frame_rank_" + c.star));
                    charAttr.setImageResource(getResourceByString(c.element));
                    lvView.setText("Lv " + c.lv);
                    starsLayout.setVisibility(View.VISIBLE);
                    for(int j = 0; j < 5; j++){
                        char_star[j].setVisibility(j < c.star ? View.VISIBLE : View.GONE);
                    }

                    //findRight
                    ConstraintLayout right_char_Layout = item.findViewById(R.id.right_char_Layout);
                    charImage = item.findViewById(R.id.right_char_image);
                    charFrame = item.findViewById(R.id.right_char_frame);
                    charAttr = item.findViewById(R.id.right_char_attr);
                    lvView = item.findViewById(R.id.right_lv_view);
                    starsLayout = item.findViewById(R.id.right_stars_layout);
                    char_star = new ImageView[]{item.findViewById(R.id.right_char_star0), item.findViewById(R.id.right_char_star1),
                            item.findViewById(R.id.right_char_star2), item.findViewById(R.id.right_char_star3), item.findViewById(R.id.right_char_star4)};

                    //findButton
                    ConstraintLayout purchase_button = item.findViewById(R.id.purchase_button);
                    TextView item_price = item.findViewById(R.id.item_price);
                    ImageView purchase_button_background = item.findViewById(R.id.purchase_button_background);


                    if(c.star != 5){
                        //initRight
                        charImage.setImageResource(getResourceByString(c.charIconImage + "d"));
                        charImage.setBackgroundResource(getResourceByString("bg_" + c.element));
                        charFrame.setBackgroundResource(getResourceByString("frame_rank_" + c.star));
                        charAttr.setImageResource(getResourceByString(c.element));
                        lvView.setText("Lv " + c.lv);
                        for(int j = 0; j < 5; j++){
                            char_star[j].setVisibility(j <= c.star ? View.VISIBLE : View.GONE);
                        }

                        item_price.setText("-" + global.CHARACTER_STAR_UP_PRICE);
                        starsLayout.setVisibility(View.VISIBLE);

                        if(global.CHARACTER_STAR_UP_PRICE > global.griefSeedNumber){
                            //买不起
                            purchase_button_background.setColorFilter(grayColorFilter);
                            purchase_button.setOnClickListener(null);
                        }else{
                            //买得起
                            purchase_button_background.setColorFilter(null);
                            purchase_button.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v){

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AdjustmentHouseActivity.this);
                                    final AlertDialog dialog = builder.create();

                                    View dialog_frame = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.alert_dialog_frame, null);
                                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_title_name)).setText("星级提升: " + c.name);
                                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("将消耗 " + global.CHARACTER_STAR_UP_PRICE + " 个悲叹之种将星级提升至 " + (c.star + 1) + " 星.");
                                    ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).removeAllViews();
                                    //((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).addView();
                                    ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(null);
                                    (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            //正确逻辑
                                            global.griefSeedNumber -= global.CHARACTER_STAR_UP_PRICE;
                                            c.star++;
                                            updateCCAndGriefSeedView();
                                            character_star_up.performClick();
                                            dialog.dismiss();
                                            dialog.cancel();
                                        }
                                    });
                                    (dialog_frame.findViewById(R.id.alert_dialog_cancel_button)).setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            dialog.dismiss();
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                    dialog.getWindow().setContentView(dialog_frame);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
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

        memoria_breakthrough.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if(memoriaBreakthroughBitmap.getPixel((int) event.getX(), (int) event.getY()) == 0){
                        isTouchMemoriaBreakthroughTransparent = true;
                        if(2 * event.getY() > characterPlateChangeBitmap.getHeight()){
                            shop_trade.dispatchTouchEvent(event);
                        }else{
                            character_star_up.dispatchTouchEvent(event);
                        }
                    }else{
                        isTouchMemoriaBreakthroughTransparent = false;
                    }
                    Log.d("Sam", "is MemoriaBreakthrough Touch Point Transparent:" + isTouchMemoriaBreakthroughTransparent);
                }
                return false;
            }
        });
        memoriaBreakthroughBitmap = ((BitmapDrawable) memoria_breakthrough.getDrawable()).getBitmap();
        memoria_breakthrough.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!isTouchMemoriaBreakthroughTransparent){
                    right_item_container.removeAllViews();
                    right_item_container.setVisibility(View.GONE);
                    memoria_list.setVisibility(View.VISIBLE);
                    shop_frame.setVisibility(View.GONE);

                    memoriaAdapter = new ShopMemoriaAdapter(global.memoriaBag, AdjustmentHouseActivity.this);
                    cardsRecyclerView.setAdapter(memoriaAdapter);
                    StaggeredGridLayoutManager m = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
                    cardsRecyclerView.setLayoutManager(m);

                    cardNumber.setText("" + global.memoriaBag.size() + "/400");

                    //初始化排序按钮及排序效果
                    orderBy.setImageResource(global.isOrderByLV ? R.drawable.order2 : R.drawable.order1);
                    updateSortedOutcome();
                    memoriaAdapter.notifyItemRangeChanged(0, global.memoriaBag.size());

                    orderBy.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            global.isOrderByLV = !global.isOrderByLV;
                            orderBy.setImageResource(global.isOrderByLV ? R.drawable.order2 : R.drawable.order1);
                            updateSortedOutcome();
                            memoriaAdapter.notifyItemRangeChanged(0, global.memoriaBag.size());
                        }
                    });
                    order.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            global.isDesc = !global.isDesc;
                            order.setImageResource(global.isDesc ? R.drawable.desc : R.drawable.incr);
                            updateSortedOutcome();
                            memoriaAdapter.notifyItemRangeChanged(0, global.memoriaBag.size());
                        }
                    });
                }
            }
        });

        //设置商店的商品
        //随机取六个商品
        ArrayList<Collection> tempShopItemList = new ArrayList<>();
        int cnt = 6;
        for(int i = 0; i < global.collectionList.size() && cnt > 0; i++){
            if(!global.collectionList.get(i).isOwn){
                tempShopItemList.add(global.collectionList.get(i));
                cnt--;
            }
        }
//        while(tempShopItemList.size() < 6){
//            int id = (int)(Math.random()*global.collectionList.size());
//            Collection c = global.collectionList.get(id);
//            if(!tempShopItemList.contains(c) && !c.isOwn){
//                tempShopItemList.add(c);
//            }
//        }
        for(int i = 0; i < tempShopItemList.size(); i++){
            Collection c = tempShopItemList.get(i);
            if(i < 3){
                shopCollectionList[0][i] = c;
            }else{
                shopCollectionList[1][i - 3] = c;
            }
        }
        for(int i = tempShopItemList.size(); i < 6; i++){
            if(i < 3){
                shopCollectionList[0][i] = DEFAULT_COLLECTION;
            }else{
                shopCollectionList[1][i - 3] = DEFAULT_COLLECTION;
            }
        }

        shop_trade.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                right_item_container.setVisibility(View.GONE);
                memoria_list.setVisibility(View.GONE);
                shop_frame.setVisibility(View.VISIBLE);
                shop_list_first_row.removeAllViews();
                shop_list_second_row.removeAllViews();
                for(int i = 0; i < 2; i++){
                    for(int j = 0; j < 3; j++){
                        final Collection c = shopCollectionList[i][j];
                        View item = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.shop_item, i == 0 ? shop_list_first_row : shop_list_second_row, false);
                        ImageView shop_item_image = item.findViewById(R.id.shop_item_image);
                        TextView shop_item_name = item.findViewById(R.id.shop_item_name);
                        TextView shop_item_price = item.findViewById(R.id.shop_item_price);
                        ImageView cc_icon = item.findViewById(R.id.cc_icon);
                        shop_item_image.setBackgroundResource(getResourceByString(c.icon));
                        shop_item_name.setText(c.name);
                        if(c.isOwn){
                            shop_item_price.setText("-售罄-");
                            cc_icon.setVisibility(View.GONE);
                            item.setAlpha(0.5f);
                        }else{
                            shop_item_price.setText("" + c.price);
                            cc_icon.setVisibility(View.VISIBLE);
                            item.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v){
                                    final View dialog_layout = getLayoutInflater().inflate(R.layout.shop_item_alert_dialog, null, false);
                                    ImageView shop_item_image = dialog_layout.findViewById(R.id.shop_item_image);
                                    TextView shop_item_name = dialog_layout.findViewById(R.id.shop_item_name);
                                    TextView shop_item_effect_description = dialog_layout.findViewById(R.id.shop_item_effect_description);
                                    TextView shop_item_description = dialog_layout.findViewById(R.id.shop_item_description);

                                    shop_item_image.setBackgroundResource(getResourceByString(c.icon));
                                    shop_item_name.setText(c.name);
                                    shop_item_effect_description.setText(c.effectDescription);
                                    shop_item_description.setText(c.description);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AdjustmentHouseActivity.this);
                                    final AlertDialog dialog = builder.create();

                                    View dialog_frame = LayoutInflater.from(AdjustmentHouseActivity.this).inflate(R.layout.alert_dialog_frame, null);
                                    ((TextView) dialog_frame.findViewById(R.id.alert_dialog_title_name)).setText("购买");
                                    if(global.ccNumber >= c.price){
                                        ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("将花费 " + c.price + "cc 购买以下商品:");
                                        ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).removeAllViews();
                                        ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).addView(dialog_layout);
                                        ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(null);
                                        (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v){
                                                //正确逻辑
                                                global.ccNumber -= c.price;
                                                c.isOwn = true;
                                                updateCCAndGriefSeedView();
                                                shop_trade.performClick();
                                                dialog.dismiss();
                                                dialog.cancel();
                                            }
                                        });
                                    }else{
                                        ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText("购买商品需要 " + c.price + "cc , cc不足.");
                                        ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).removeAllViews();
                                        ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).addView(dialog_layout);
                                        ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(grayColorFilter);
                                        (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v){
                                                //正确逻辑
                                            }
                                        });
                                    }
                                    (dialog_frame.findViewById(R.id.alert_dialog_cancel_button)).setOnClickListener(new View.OnClickListener(){
                                        @Override
                                        public void onClick(View v){
                                            dialog.dismiss();
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                    dialog.getWindow().setContentView(dialog_frame);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                                }
                            });

                        }
                        if(i == 0){
                            shop_list_first_row.addView(item);
                        }else{
                            shop_list_second_row.addView(item);
                        }
                    }
                }

            }
        });

        updateCCAndGriefSeedView();
        character_breakthrough.performClick();
    }

    public void initLive2d(){
        JniBridgeJava.SetActivityInstance(this);
        JniBridgeJava.SetContext(this);

        _glRenderer = new GLRenderer();
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(2);
        _glSurfaceView.setRenderer(_glRenderer);
        _glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        live2dContainer.addView(_glSurfaceView);
        final Timer live2dTimer = new Timer();
        TimerTask live2dTimerTask = new TimerTask(){
            @Override
            public void run(){
                JniBridgeJava.nativeSetCharacter(
                        "101700", "001", "011",
                        "", "", "",
//                        "101700","100","010",
                        "", "", ""
                );
                changeBackground("11061");
                Message m = new Message();
                m.what = 0;
                handler.sendMessage(m);
                live2dTimer.cancel();

            }
        };
        live2dTimer.schedule(live2dTimerTask, 500, 50);
        JniBridgeJava.nativeOnStart();
    }


    //@Override
    //protected void onPause() {
    //    super.onPause();
    //    Log.d("SGY","onPause");
    //    _glSurfaceView.onPause();
    //    JniBridgeJava.nativeOnPause();
    //    JniBridgeJava.nativeOnStop();
    //    JniBridgeJava.nativeOnStart();
    //    _glSurfaceView.onResume();
//
    //    //JniBridgeJava.nativeOnPause();
    //}

    @Override
    protected void onDestroy(){
        super.onDestroy();
        JniBridgeJava.nativeOnPause();
        JniBridgeJava.nativeOnStop();
        JniBridgeJava.nativeOnDestroy();
    }

    public void updateSortedOutcome(){
        Collections.sort(global.memoriaBag, new Comparator<Memoria>(){
            @Override
            public int compare(Memoria lhs, Memoria rhs){
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                int weight = 0;
                if(global.isOrderByLV){
                    return lhs.lvNow > rhs.lvNow ? (global.isDesc ? -1 : 1) : (lhs.lvNow < rhs.lvNow) ? (global.isDesc ? 1 : -1) : (Integer.parseInt(lhs.id) > Integer.parseInt(rhs.id) ? -1 : (Integer.parseInt(lhs.id) < Integer.parseInt(rhs.id) ? 1 : 0));
                }else{
                    return lhs.star > rhs.star ? (global.isDesc ? -1 : 1) : (lhs.star < rhs.star) ? (global.isDesc ? 1 : -1) : (Integer.parseInt(lhs.id) > Integer.parseInt(rhs.id) ? -1 : (Integer.parseInt(lhs.id) < Integer.parseInt(rhs.id) ? 1 : 0));
                }
            }
        });
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
        cc_number = findViewById(R.id.cc_number);
        grief_seed_number = findViewById(R.id.grief_seed_number);
        shop_trade = findViewById(R.id.shop_trade);
        shop_frame = findViewById(R.id.shop_frame);
        shop_list_first_row = findViewById(R.id.shop_list_first_row);
        shop_list_second_row = findViewById(R.id.shop_list_second_row);
        black_mask_adjustment_house = findViewById(R.id.black_mask_adjustment_house);
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


    void updateCCAndGriefSeedView(){
        cc_number.setText(" " + global.ccNumber + " ");
        grief_seed_number.setText(" " + global.griefSeedNumber + " ");
    }

    void changeBackground(String backgroundNumber){
        JniBridgeJava.nativeChangeBackground("bg_adv_" + backgroundNumber + ".png");
    }

    public int getResourceByString(String idName){
        return getResources().getIdentifier(idName, "drawable", getPackageName());
    }

    public int getIdByString(String idName){
        return getResources().getIdentifier(idName, "id", getPackageName());
    }
}