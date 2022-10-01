package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeamChooseActivity extends AppCompatActivity {
    
    CharacterIconAdapter adapter;
    
    boolean isCheckMemoria = false;
    boolean isChangingToMemoria = false;
    
    ConstraintLayout[] charFormation = new ConstraintLayout[5];
    ImageView[][][] formation = new ImageView[5][3][3];
    ImageView[] charAll = new ImageView[5];
    ConstraintLayout[] showCharacterLayout = new ConstraintLayout[5];
    ImageView[] isLeader = new ImageView[5];
    ImageView[] charAttribute = new ImageView[5];
    StrokeTextView[] lvView = new StrokeTextView[5];
    LinearLayout[] starsLayout = new LinearLayout[5];
    ImageView[][] char_star = new ImageView[5][5];
    TextView[] charHP = new TextView[5];
    TextView[] charATK = new TextView[5];
    TextView[] charDEF = new TextView[5];
    TextView[] charMP = new TextView[5];
    ImageView[][] skillIcon_ = new ImageView[5][4];
    ImageView[] recover_HP_ = new ImageView[5];
    LinearLayout[] breakThoughLayout = new LinearLayout[5];
    ImageView[][] breakThough_ = new ImageView[5][3];
    ConstraintLayout[] showMemoriaLayout = new ConstraintLayout[5];
    CharCardView[][] charCard = new CharCardView[5][4];
    ImageView changeState;
    ImageView openFormation;
    ImageView startBattle;
    LinearLayout charRecyclerLayout;
    RecyclerView charRecyclerView;
    ConstraintLayout[] char_largestFrame = new ConstraintLayout[5];
    ImageView[] chooseChar = new ImageView[5];
    ImageView back;
    TextView cc_number;
    TextView grief_seed_number;

    boolean isIntentSend = false;
    
    Global global;

    ColorMatrixColorFilter grayColorFilter;//用于灰度设置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_choose);
        global = (Global)getApplicationContext(); 
        findView();
        init();
    }

    public void findView(){
        for(int i = 0; i < 5; i++){
            charFormation[i] = findViewById(getIDByStr("charFormation"+i));
            for(int j = 0; j < 3; j++){
                for(int k = 0; k < 3; k++){
                    formation[i][j][k] = findViewById(getIDByStr("formation"+i+"_"+j+"_"+k));
                }
            }
            charAll[i] = findViewById(getIDByStr("charAll"+i));
            showCharacterLayout[i] = findViewById(getIDByStr("showCharacterLayout"+i));
            isLeader[i] = findViewById(getIDByStr("isLeader"+i));
            charAttribute[i] = findViewById(getIDByStr("charAttribute"+i));
            lvView[i] = findViewById(getIDByStr("lvView"+i));
            starsLayout[i] = findViewById(getIDByStr("starsLayout"+i));
            char_largestFrame[i] = findViewById(getIDByStr("char"+i+"_largestFrame"));
            chooseChar[i] = findViewById(getIDByStr("chooseChar"+i));
            for(int j = 0; j < 5; j++){
                char_star[i][j] = findViewById(getIDByStr("char"+i+"_star"+j));
            }
            charHP[i] = findViewById(getIDByStr("charHP"+i));
            charATK[i] = findViewById(getIDByStr("charATK"+i));
            charDEF[i] = findViewById(getIDByStr("charDEF"+i));
            charMP[i] = findViewById(getIDByStr("charMP"+i));
            for(int j = 0; j < 4; j++){
                skillIcon_[i][j] = findViewById(getIDByStr("skillIcon"+i+"_"+j));
            }
            breakThoughLayout[i] = findViewById(getIDByStr("breakThoughLayout"+i));
            recover_HP_[i] = findViewById(getIDByStr("recover_HP_"+i));
            for(int j = 0; j < 3; j++){
                breakThough_[i][j] = findViewById(getIDByStr("breakThough"+i+"_"+j));
            }
            showMemoriaLayout[i] = findViewById(getIDByStr("showMemoriaLayout"+i));
            for(int j = 0; j < 4; j++){
                charCard[i][j] = findViewById(getIDByStr("char"+i+"Card"+j));
            }
        }
        changeState = findViewById(R.id.changeState);
        openFormation = findViewById(R.id.openFormation);
        startBattle = findViewById(R.id.startBattle);
        charRecyclerLayout = findViewById(R.id.charRecyclerLayout);
        charRecyclerView = findViewById(R.id.charRecyclerView);
        back = findViewById(R.id.back);
        cc_number = findViewById(R.id.cc_number);
        grief_seed_number = findViewById(R.id.grief_seed_number);
    }

    public void init(){
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0); // 设置饱和度
        grayColorFilter = new ColorMatrixColorFilter(cm);

        updateCCAndGriefSeedView();
        changeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCheckMemoria){
                    changeState.setImageResource(R.drawable.change_to_memoria);
                }else{
                    changeState.setImageResource(R.drawable.change_to_char);
                }
                isCheckMemoria = !isCheckMemoria;
                if(global.choseCharacter != -1){
                    chooseChar[global.choseCharacter].setVisibility(View.INVISIBLE);
                    global.choseCharacter = -1;
                    charRecyclerLayout.setVisibility(View.GONE);
                }
                updateCheckingViews();
            }
        });
        for(int i = 0; i < 5; i++){
            final int temp = i;
            for(int j = 0; j < 4; j++){
                final int tempJ = j;
                charCard[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isChangingToMemoria && global.characters[temp].breakThrough >= tempJ + 1){
                            isChangingToMemoria = true;
                            global.chooseCharacter = getCharacterIdInCharacterList(global.characters[temp]);
                            Intent receivedIntent = getIntent();
                            int battleId = receivedIntent.getIntExtra("battleInfo",-1);
                            boolean isRandomBattle = receivedIntent.getBooleanExtra("isRandomBattle",true);
                            if(!isIntentSend){
                                Intent intent1 = new Intent(TeamChooseActivity.this,MemoriaActivity.class);
                                intent1.putExtra("battleInfo",battleId);
                                intent1.putExtra("isRandomBattle", isRandomBattle);
                                intent1.putExtra("touchMemoriaId",tempJ);
                                intent1.putExtra("eventX", getIntent().getIntExtra("eventX",-1));
                                intent1.putExtra("eventY", getIntent().getIntExtra("eventY",-1));
                                for(int i = 0; i < global.characters.length; i++){
                                    Character c = global.characters[i];
                                    if(c != null){
                                        intent1.putExtra("HPRatio"+i,1.0f*global.characters[i].realHP/global.characters[i].getRealMaxHP());
                                    }
                                }


                                startActivity(intent1);
                                finish();
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                isIntentSend = true;
                            }

                        }
                    }
                });
            }
            char_largestFrame[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isCheckMemoria){
                        if(global.choseCharacter == -1){
                            global.choseCharacter = temp;
                            chooseChar[temp].setVisibility(View.VISIBLE);
                            adapter.notifyItemRangeChanged(0,global.characterList.size());
                            charRecyclerLayout.setVisibility(View.VISIBLE);
                        }else{
                            if(global.choseCharacter == temp){
                                global.choseCharacter = -1;
                                chooseChar[temp].setVisibility(View.INVISIBLE);
                                charRecyclerLayout.setVisibility(View.GONE);
                            }else{
                                Character c = global.characters[temp];
                                global.characters[temp] = global.characters[global.choseCharacter];
                                global.characters[global.choseCharacter] = c;
                                chooseChar[global.choseCharacter].setVisibility(View.INVISIBLE);
                                global.choseCharacter = -1;
                                charRecyclerLayout.setVisibility(View.GONE);
                            }
                        }
                        updateCheckingViews();
                    }
                }
            });
            isLeader[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(global.characters[temp] != null && !global.characters[temp].isLeader){
                        for(int j = 0; j < 5; j++){
                            if(global.characters[j] != null){
                                global.characters[j].isLeader = false;
                            }
                        }
                        global.characters[temp].isLeader = true;
                    }
                    updateCheckingViews();
                }
            });

            //开始战斗按钮设置
            final Intent receivedIntent = getIntent();
            final int battleId = receivedIntent.getIntExtra("battleInfo",-1);
            if(battleId == -1){
                startBattle.setVisibility(View.GONE);
            }else{
                startBattle.setVisibility(View.VISIBLE);
                startBattle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        global.cancelBGM();
                        int characterInTeamNumber = 0;
                        for(int i = 0; i < global.characters.length; i++){
                            if(global.characters[i] != null){
                                characterInTeamNumber++;
                            }
                        }
                        if(characterInTeamNumber > 0){
                            if(receivedIntent.getIntExtra("eventX", -1) != -1){
                                global.PLAYER_ON_MAP_X = receivedIntent.getIntExtra("eventX", -1);
                                global.PLAYER_ON_MAP_Y = receivedIntent.getIntExtra("eventY", -1);
                            }
                            Log.d("Sam","PlayerX:"+global.PLAYER_ON_MAP_X+", PlayerY:"+global.PLAYER_ON_MAP_Y);
                            if(!isIntentSend){
                                boolean isRandomBattle = receivedIntent.getBooleanExtra("isRandomBattle",true);
                                Intent intent1 = new Intent(TeamChooseActivity.this, BattleActivity.class);
                                intent1.putExtra("isRandomBattle", isRandomBattle);
                                intent1.putExtra("battleInfo", battleId);
                                intent1.putExtra("eventX",getIntent().getIntExtra("eventX",-1));
                                intent1.putExtra("eventY",getIntent().getIntExtra("eventY",-1));
                                intent1.putExtra("extraMissionId", receivedIntent.getIntExtra("extraMissionId",0));
                                startActivity(intent1);
                                finish();
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                isIntentSend = true;
                            }
                        }
                    }
                });
            }

        }

        //初始化最下方的角色栏
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        charRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CharacterIconAdapter(TeamChooseActivity.this);
        charRecyclerView.setAdapter(adapter);
        charRecyclerView.getItemAnimator().setAddDuration(0);
        charRecyclerView.getItemAnimator().setChangeDuration(0);
        charRecyclerView.getItemAnimator().setMoveDuration(0);
        charRecyclerView.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator)charRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        openFormation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isIntentSend){
                    Intent receivedIntent = getIntent();
                    int battleId = receivedIntent.getIntExtra("battleInfo",-1);
                    boolean isRandomBattle = receivedIntent.getBooleanExtra("isRandomBattle",true);
                    Intent intent1 = new Intent(TeamChooseActivity.this, FormationActivity.class);
                    intent1.putExtra("battleInfo",battleId);
                    intent1.putExtra("eventX",getIntent().getIntExtra("eventX",-1));
                    intent1.putExtra("eventY",getIntent().getIntExtra("eventY",-1));
                    intent1.putExtra("isRandomBattle", isRandomBattle);
                    startActivity(intent1);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    isIntentSend = true;
                }

            }
        });

        //初始化返回键
        if(getIntent().getBooleanExtra("isRandomBattle",true)){
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int characterNumberOnTeam = 0;
                    for(int i = 0; i < global.characters.length; i++){
                        if(global.characters[i] != null){
                            characterNumberOnTeam++;
                        }
                    }
                    if(characterNumberOnTeam > 0){
                        if(!isIntentSend){
                            Intent intent1 = new Intent(TeamChooseActivity.this, MapActivity.class);
                            startActivity(intent1);
                            finish();
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                            isIntentSend = true;
                        }
                    }
                }
            });
        }else{
            back.setVisibility(View.GONE);
        }


        updateCheckingViews();
    }

    public void updateCheckingViews(){
        for(int i = 0; i < 5; i++){
            if(global.characters[i] != null){
                charFormation[i].setVisibility(isCheckMemoria? View.INVISIBLE:View.VISIBLE);
                global.formationList.get(global.usingFormationId).setFormation(formation[i],charAll[i],i);
                showCharacterLayout[i].setVisibility(isCheckMemoria? View.INVISIBLE:View.VISIBLE);
                showCharacterLayout[i].setBackgroundResource(getResource(global.characters[i].choosingActivityImage));
                isLeader[i].setImageResource(global.characters[i].isLeader? R.drawable.leader:R.drawable.empty_leader);
                if(!isCheckMemoria){
                    isLeader[i].setImageResource(global.characters[i].isLeader? R.drawable.leader:R.drawable.empty_leader);
                    charAttribute[i].setImageResource(getResource(global.characters[i].element));
                    lvView[i].setText("Lv "+global.characters[i].lv);
                    for(int j = 0; j < 5; j++){
                        if(j < global.characters[i].star){
                            char_star[i][j].setVisibility(View.VISIBLE);
                        }else{
                            char_star[i][j].setVisibility(View.GONE);
                        }
                    }
                    charHP[i].setText(global.characters[i].realHP+"/"+global.characters[i].getRealMaxHP());
                    charATK[i].setText(""+global.characters[i].getRealATK());
                    charDEF[i].setText(""+global.characters[i].getRealDEF());
                    charMP[i].setText(""+(global.characters[i].realMP/10)+"/"+(global.characters[i].getMaxMp()/10));
                    if(global.characters[i].getRealMaxHP() == global.characters[i].HP){
                        charHP[i].setTextColor(getColor(R.color.shader));
                    }else{
                        charHP[i].setTextColor(getColor(R.color.textPink));
                    }
                    if(global.characters[i].getRealATK() == global.characters[i].ATK){
                        charATK[i].setTextColor(getColor(R.color.shader));
                    }else{
                        charATK[i].setTextColor(getColor(R.color.textPink));
                    }
                    if(global.characters[i].getRealDEF() == global.characters[i].DEF){
                        charDEF[i].setTextColor(getColor(R.color.shader));
                    }else{
                        charDEF[i].setTextColor(getColor(R.color.textPink));
                    }
                    recover_HP_[i].setVisibility(View.VISIBLE);
                    if(global.griefSeedNumber == 0){
                        recover_HP_[i].setColorFilter(grayColorFilter);
                        recover_HP_[i].setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){

                            }
                        });
                    }else{
                        final int tempI = i;
                        recover_HP_[i].setColorFilter(null);
                        recover_HP_[i].setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                AlertDialog.Builder builder = new AlertDialog.Builder(TeamChooseActivity.this);
                                final AlertDialog dialog = builder.create();
                                View dialog_frame = LayoutInflater.from(TeamChooseActivity.this).inflate(R.layout.alert_dialog_frame, null);
                                ((TextView) dialog_frame.findViewById(R.id.alert_dialog_title_name)).setText("使用悲叹之种");
                                ((TextView) dialog_frame.findViewById(R.id.alert_dialog_content_text)).setText(
                                        "是否花费 1 悲叹之种为"+ global.characters[tempI].name +"恢复 "
                                        + Math.min(global.characters[tempI].getRealMaxHP() - global.characters[tempI].realHP, 5000) + "HP 与 "
                                        + Math.min(global.characters[tempI].getMaxMp() - global.characters[tempI].realMP, 300)/10 + "MP ？");
                                ((FrameLayout)dialog_frame.findViewById(R.id.alert_dialog_extra_layout)).removeAllViews();
                                ((ImageView) dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setColorFilter(null);
                                (dialog_frame.findViewById(R.id.alert_dialog_ok_button)).setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        global.characters[tempI].realHP += Math.min(global.characters[tempI].getRealMaxHP() - global.characters[tempI].realHP, 5000);
                                        global.characters[tempI].realMP += Math.min(global.characters[tempI].getMaxMp() - global.characters[tempI].realMP, 300);
                                        global.griefSeedNumber--;
                                        updateCheckingViews();
                                        updateCCAndGriefSeedView();
                                        dialog.cancel();
                                        dialog.dismiss();
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
                            }
                        });
                    }
                    for(int j = 0; j < 4; j++){
                        if(global.characters[i].memoriaList[j] != null){
                            skillIcon_[i][j].setVisibility(View.VISIBLE);
                            skillIcon_[i][j].setImageResource(getResource(global.characters[i].memoriaList[j].icon));
                        }else if(j < global.characters[i].breakThrough){
                            skillIcon_[i][j].setVisibility(View.VISIBLE);
                            skillIcon_[i][j].setImageResource(R.drawable.add_memoria);
                        }else{
                            skillIcon_[i][j].setVisibility(View.INVISIBLE);
                        }
                    }
                }else{
                    recover_HP_[i].setVisibility(View.INVISIBLE);
                    charAll[i].setVisibility(View.INVISIBLE);
                }

                breakThoughLayout[i].setVisibility(isCheckMemoria? View.VISIBLE:View.INVISIBLE);
                if(isCheckMemoria){
                    for(int j = 0; j < 3; j++){
                        if(j+1 < global.characters[i].breakThrough){
                            breakThough_[i][j].setImageResource(R.drawable.filled_slot);
                        }else{
                            breakThough_[i][j].setImageResource(R.drawable.empty_slot);
                        }
                    }
                }

                showMemoriaLayout[i].setVisibility(isCheckMemoria? View.VISIBLE:View.INVISIBLE);
                showMemoriaLayout[i].setBackgroundResource(getResource(global.characters[i].choosingActivityImage));
                if(isCheckMemoria){
                    for(int j = 0; j < 4; j++){
                        if(global.characters[i].memoriaList[j] != null){
                            charCard[i][j].setMemoria(global.characters[i].memoriaList[j]);
                        }else if(j < global.characters[i].breakThrough){
                            charCard[i][j].setEmpty();
                        }else{
                            charCard[i][j].setUnusable();
                        }
                    }
                }
            }else{
                //该位置没人
                charFormation[i].setVisibility(isCheckMemoria? View.INVISIBLE:View.VISIBLE);
                global.formationList.get(global.usingFormationId).setFormation(formation[i],charAll[i],i);
                showCharacterLayout[i].setVisibility(View.INVISIBLE);
                breakThoughLayout[i].setVisibility(View.INVISIBLE);
                showMemoriaLayout[i].setVisibility(View.INVISIBLE);
                recover_HP_[i].setVisibility(View.INVISIBLE);
            }

        }
    }

    void updateCCAndGriefSeedView(){
        cc_number.setText(" " + global.ccNumber + " ");
        grief_seed_number.setText(" " + global.griefSeedNumber + " ");
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public int getCharacterIdInCharacterList(Character c){
        for(int i = 0; i < global.characterList.size(); i++){
            if(global.characterList.get(i) == c){
                return i;
            }
        }
        return -1;
    }


    public int getIDByStr(String idName){
        return getResources().getIdentifier(idName,"id", getPackageName());
    }

    public int getResource(String idName){
        return getResources().getIdentifier(idName,"drawable", getPackageName());
    }
}

