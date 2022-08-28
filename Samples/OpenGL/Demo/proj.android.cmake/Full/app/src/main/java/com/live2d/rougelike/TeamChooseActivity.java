package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.live2d.rougelike.StartActivity.characters;

public class TeamChooseActivity extends AppCompatActivity {

    public static int choseCharacter = -1;
    public static int usingFormationId = 0;

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
    ImageView[][] skillIcon_ = new ImageView[5][4];
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_choose);
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
            for(int j = 0; j < 4; j++){
                skillIcon_[i][j] = findViewById(getIDByStr("skillIcon"+i+"_"+j));
            }
            breakThoughLayout[i] = findViewById(getIDByStr("breakThoughLayout"+i));
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

    }

    public void init(){
        changeState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCheckMemoria){
                    changeState.setImageResource(R.drawable.change_to_memoria);
                }else{
                    changeState.setImageResource(R.drawable.change_to_char);
                }
                isCheckMemoria = !isCheckMemoria;
                if(choseCharacter != -1){
                    chooseChar[choseCharacter].setVisibility(View.INVISIBLE);
                    choseCharacter = -1;
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
                        if(!isChangingToMemoria){
                            isChangingToMemoria = true;
                            MemoriaActivity.chooseCharacter = getCharacterIdInCharacterList(characters[temp]);
                            Intent receivedIntent = getIntent();
                            int battleId = receivedIntent.getIntExtra("battleInfo",-1);
                            Intent intent1 = new Intent(TeamChooseActivity.this,MemoriaActivity.class);
                            intent1.putExtra("battleInfo",battleId);
                            intent1.putExtra("touchMemoriaId",tempJ);
                            startActivity(intent1);
                            finish();
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        }
                    }
                });
            }
            char_largestFrame[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isCheckMemoria){
                        if(choseCharacter == -1){
                            choseCharacter = temp;
                            chooseChar[temp].setVisibility(View.VISIBLE);
                            adapter.notifyItemRangeChanged(0,StartActivity.characterList.size());
                            charRecyclerLayout.setVisibility(View.VISIBLE);
                        }else{
                            if(choseCharacter == temp){
                                choseCharacter = -1;
                                chooseChar[temp].setVisibility(View.INVISIBLE);
                                charRecyclerLayout.setVisibility(View.GONE);
                            }else{
                                Character c = characters[temp];
                                characters[temp] = characters[choseCharacter];
                                characters[choseCharacter] = c;
                                chooseChar[choseCharacter].setVisibility(View.INVISIBLE);
                                choseCharacter = -1;
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
                    if(characters[temp] != null && !characters[temp].isLeader){
                        for(int j = 0; j < 5; j++){
                            if(characters[j] != null){
                                characters[j].isLeader = false;
                            }
                        }
                        characters[temp].isLeader = true;
                    }
                    updateCheckingViews();
                }
            });
            startBattle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 后续需要更改HP提升时候的realHP更新逻辑
                    for(int i = 0; i < 5; i++){
                        if(StartActivity.characters[i] != null){
                            StartActivity.characters[i].realHP = characters[i].getRealMaxHP();
                        }
                    }
                    Intent receivedIntent = getIntent();
                    int battleId = receivedIntent.getIntExtra("battleInfo",-1);
                    Intent intent1 = new Intent(TeamChooseActivity.this, BattleActivity.class);
                    intent1.putExtra("battleInfo",battleId);
                    startActivity(intent1);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                }
            });
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
                Intent receivedIntent = getIntent();
                int battleId = receivedIntent.getIntExtra("battleInfo",-1);
                Intent intent1 = new Intent(TeamChooseActivity.this, FormationActivity.class);
                intent1.putExtra("battleInfo",battleId);
                startActivity(intent1);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
        updateCheckingViews();
    }

    public void updateCheckingViews(){
        for(int i = 0; i < 5; i++){
            if(characters[i] != null){
                charFormation[i].setVisibility(isCheckMemoria? View.INVISIBLE:View.VISIBLE);
                StartActivity.formationList.get(usingFormationId).setFormation(formation[i],charAll[i],i);
                showCharacterLayout[i].setVisibility(isCheckMemoria? View.INVISIBLE:View.VISIBLE);
                showCharacterLayout[i].setBackgroundResource(getResource(characters[i].choosingActivityImage));
                isLeader[i].setImageResource(characters[i].isLeader? R.drawable.leader:R.drawable.empty_leader);
                if(!isCheckMemoria){
                    isLeader[i].setImageResource(characters[i].isLeader? R.drawable.leader:R.drawable.empty_leader);
                    charAttribute[i].setImageResource(getResource(characters[i].element));
                    lvView[i].setText("Lv "+characters[i].lv);
                    for(int j = 0; j < 5; j++){
                        if(j < characters[i].star){
                            char_star[i][j].setVisibility(View.VISIBLE);
                        }else{
                            char_star[i][j].setVisibility(View.GONE);
                        }
                    }
                    charHP[i].setText(""+characters[i].getRealMaxHP());
                    charATK[i].setText(""+characters[i].getRealATK());
                    charDEF[i].setText(""+characters[i].getRealDEF());
                    if(characters[i].getRealMaxHP() == characters[i].HP){
                        charHP[i].setTextColor(getResources().getColor(R.color.shader));
                    }else{
                        charHP[i].setTextColor(getResources().getColor(R.color.textPink));
                    }
                    if(characters[i].getRealATK() == characters[i].ATK){
                        charATK[i].setTextColor(getResources().getColor(R.color.shader));
                    }else{
                        charATK[i].setTextColor(getResources().getColor(R.color.textPink));
                    }
                    if(characters[i].getRealDEF() == characters[i].DEF){
                        charDEF[i].setTextColor(getResources().getColor(R.color.shader));
                    }else{
                        charDEF[i].setTextColor(getResources().getColor(R.color.textPink));
                    }
                    for(int j = 0; j < 4; j++){
                        if(characters[i].memoriaList[j] != null){
                            skillIcon_[i][j].setVisibility(View.VISIBLE);
                            skillIcon_[i][j].setImageResource(getResource(characters[i].memoriaList[j].icon));
                        }else if(j < characters[i].breakThrough){
                            skillIcon_[i][j].setVisibility(View.VISIBLE);
                            skillIcon_[i][j].setImageResource(R.drawable.add_memoria);
                        }else{
                            skillIcon_[i][j].setVisibility(View.INVISIBLE);
                        }
                    }
                }else{
                    charAll[i].setVisibility(View.INVISIBLE);
                }

                breakThoughLayout[i].setVisibility(isCheckMemoria? View.VISIBLE:View.INVISIBLE);
                if(isCheckMemoria){
                    for(int j = 0; j < 3; j++){
                        if(j+1 < characters[i].breakThrough){
                            breakThough_[i][j].setImageResource(R.drawable.filled_slot);
                        }else{
                            breakThough_[i][j].setImageResource(R.drawable.empty_slot);
                        }
                    }
                }

                showMemoriaLayout[i].setVisibility(isCheckMemoria? View.VISIBLE:View.INVISIBLE);
                showMemoriaLayout[i].setBackgroundResource(getResource(characters[i].choosingActivityImage));
                if(isCheckMemoria){
                    for(int j = 0; j < 4; j++){
                        if(characters[i].memoriaList[j] != null){
                            charCard[i][j].setMemoria(characters[i].memoriaList[j]);
                        }else if(j < characters[i].breakThrough){
                            charCard[i][j].setEmpty();
                        }else{
                            charCard[i][j].setUnusable();
                        }
                    }
                }
            }else{
                //该位置没人
                charFormation[i].setVisibility(isCheckMemoria? View.INVISIBLE:View.VISIBLE);
                StartActivity.formationList.get(usingFormationId).setFormation(formation[i],charAll[i],i);
                showCharacterLayout[i].setVisibility(View.INVISIBLE);
                breakThoughLayout[i].setVisibility(View.INVISIBLE);
                showMemoriaLayout[i].setVisibility(View.INVISIBLE);
            }

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public int getCharacterIdInCharacterList(Character c){
        for(int i = 0; i < StartActivity.characterList.size(); i++){
            if(StartActivity.characterList.get(i) == c){
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

