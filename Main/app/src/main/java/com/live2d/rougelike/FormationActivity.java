package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FormationActivity extends AppCompatActivity {
    ImageView[][] formation_ = new ImageView[3][3];
    ImageView[][] all_ = new ImageView[3][3];
    TextView[][] skillDescription_ = new TextView[3][3];
    ImageView confirm;
    RecyclerView formationRecyclerView;
    public Formation choseFormation = StartActivity.formationList.get(TeamChooseActivity.usingFormationId);

    boolean isIntentSend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation);
        findView();
        init();

    }

    void findView(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                formation_[i][j] = findViewById(getIDByStr("formation"+i+"_"+j));
                all_[i][j] = findViewById(getIDByStr("all"+i+"_"+j));
                skillDescription_[i][j] = findViewById(getIDByStr("skillDescription"+i+"_"+j));
            }
        }
        confirm = findViewById(R.id.confirm);
        formationRecyclerView = findViewById(R.id.formationRecyclerView);
    }

    void init(){
        choseFormation.isChose = true;
        formationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        formationRecyclerView.setAdapter(new FormationAdapter(FormationActivity.this));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receivedIntent = getIntent();
                int battleId = receivedIntent.getIntExtra("battleInfo",-1);
                boolean isRandomBattle = receivedIntent.getBooleanExtra("isRandomBattle",true);
                if(!isIntentSend){
                    Intent intent1 = new Intent(FormationActivity.this, TeamChooseActivity.class);
                    intent1.putExtra("battleInfo",battleId);
                    intent1.putExtra("isRandomBattle", isRandomBattle);
                    intent1.putExtra("eventX",getIntent().getIntExtra("eventX",-1));
                    intent1.putExtra("eventY",getIntent().getIntExtra("eventY",-1));
                    startActivity(intent1);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    isIntentSend = true;
                }

            }
        });

        setBigFormation();
    }

    void setBigFormation(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(choseFormation.grid[i][j] == 0){
                    //说明该位置为空
                    formation_[i][j].setVisibility(View.INVISIBLE);
                    all_[i][j].setVisibility(View.INVISIBLE);
                    skillDescription_[i][j].setVisibility(View.INVISIBLE);
                }else if(choseFormation.grid[i][j] == 1){
                    //说明该位置有人
                    formation_[i][j].setVisibility(View.VISIBLE);
                    all_[i][j].setVisibility(View.INVISIBLE);
                    formation_[i][j].setBackgroundResource(R.drawable.common_formation_block);
                    skillDescription_[i][j].setVisibility(View.INVISIBLE);
                }else{
                    //说明该位置为all
                    formation_[i][j].setVisibility(View.VISIBLE);
                    all_[i][j].setVisibility(View.VISIBLE);
                    formation_[i][j].setBackgroundResource(R.drawable.all_formation_block);
                    skillDescription_[i][j].setVisibility(View.VISIBLE);
                    String skillDescription = "";
                    for(int k = 0; k < choseFormation.gridAllEffectList[i][j].size(); k++){
                        if(k > 0){
                            skillDescription += "\n";
                        }
                        skillDescription += choseFormation.gridAllEffectList[i][j].get(k).getEffectDescription().substring(0,(choseFormation.gridAllEffectList[i][j].get(k).getEffectDescription().length()-3));
                    }
                    skillDescription_[i][j].setText(skillDescription);
                }
            }
        }
    }

    public int getIDByStr(String idName){
        return getResources().getIdentifier(idName,"id", getPackageName());
    }
}