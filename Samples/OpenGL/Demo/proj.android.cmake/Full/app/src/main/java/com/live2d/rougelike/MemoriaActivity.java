package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.live2d.rougelike.CharacterPlateView.ACCELE;
import static com.live2d.rougelike.CharacterPlateView.BLAST_VERTICAL;
import static com.live2d.rougelike.CharacterPlateView.BLAST_HORIZONTAL;
import static com.live2d.rougelike.CharacterPlateView.CHARGE;

public class MemoriaActivity extends AppCompatActivity {

    public ArrayList<Character> characterList;

    public ArrayList<Memoria> memoriaBag;

    public static int chooseCharacter = 0;

    public static int chooseEquipSlot = -1;

    public static boolean isOrderByLV = false;
    public static boolean isDesc = true;

    public static boolean canSetMemoria = false;

    ImageView charAttrView; // charAttribute
    TextView charNameView; // charName
    CardView equipMemoria1View; //equipMemoria1
    CardView equipMemoria2View; //equipMemoria2
    CardView equipMemoria3View; //equipMemoria3
    CardView equipMemoria4View; //equipMemoria4
    ArrayList<CardView> equipMemoriaViewList = new ArrayList<>();
    TextView addHPView;//HPAll
    TextView addATKView;//ATKAll
    TextView addDEFView;//DEFAll
    ImageView uninstallAll;
    ImageView confirm;
    CardView showingMemoriaView; // showingMemoria
    TextView showingMemoriaNameView; //showingMemoriaName
    TextView HPShowingView; // HPShowing
    TextView ATKShowingView; //ATKShowing
    TextView DEFShowingView; //DEFShowing
    LinearLayout break_throughLinearLayout;
    TextView coolTimeView;
    TextView skillNameView; //skillName
    ImageView skillIcon;
    TextView skillDescriptionView; // skillDescription
    ImageView setView;
    TextView cardNumberView; // cardNumber
    ImageView orderView; // order
    ImageView orderByView; //orderBy
    RecyclerView cardsRecyclerView;
    MemoriaAdapter memoriaAdapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memoria);

        characterList = StartActivity.characterList;

        memoriaBag = StartActivity.memoriaBag;

        findView();

        updateSortedOutcome();

        memoriaAdapter = new MemoriaAdapter(memoriaBag,MemoriaActivity.this);
        cardsRecyclerView.setAdapter(memoriaAdapter);
        StaggeredGridLayoutManager m = new StaggeredGridLayoutManager(6,StaggeredGridLayoutManager.VERTICAL);
        cardsRecyclerView.setLayoutManager(m);

        initView();
    }

    public void findView(){
        charAttrView = findViewById(R.id.charAttribute);
        charNameView = findViewById(R.id.charName);
        equipMemoria1View = findViewById(R.id.equipMemoria1);
        equipMemoria2View = findViewById(R.id.equipMemoria2);
        equipMemoria3View = findViewById(R.id.equipMemoria3);
        equipMemoria4View = findViewById(R.id.equipMemoria4);
        if(equipMemoriaViewList.size() == 0){
            equipMemoriaViewList.add(equipMemoria1View);
            equipMemoriaViewList.add(equipMemoria2View);
            equipMemoriaViewList.add(equipMemoria3View);
            equipMemoriaViewList.add(equipMemoria4View);
        }
        addHPView  = findViewById(R.id.HPAll);
        addATKView = findViewById(R.id.ATKAll);
        addDEFView = findViewById(R.id.DEFAll);
        uninstallAll = findViewById(R.id.uninstallAll);
        confirm = findViewById(R.id.confirm);
        showingMemoriaView = findViewById(R.id.showingMemoria);
        showingMemoriaNameView = findViewById(R.id.showingMemoriaName);
        HPShowingView = findViewById(R.id.HPShowing);
        ATKShowingView = findViewById(R.id.ATKShowing);
        DEFShowingView = findViewById(R.id.DEFShowing);
        break_throughLinearLayout = findViewById(R.id.break_throughLinearLayout);
        coolTimeView = findViewById(R.id.coolTimeView);
        skillNameView = findViewById(R.id.skillName);
        skillIcon = findViewById(R.id.skillIcon);
        skillDescriptionView = findViewById(R.id.skillDescription);
        setView = findViewById(R.id.setView);
        cardNumberView = findViewById(R.id.cardNumber);
        orderView = findViewById(R.id.order);
        orderByView = findViewById(R.id.orderBy);
        cardsRecyclerView = findViewById(R.id.cardsRecyclerView);
        back = findViewById(R.id.back);
    }

    public void initView(){
        cardNumberView.setText(""+memoriaBag.size()+"/400");
        charAttrView.setImageResource(getResourceByString(characterList.get(chooseCharacter).element));
        charNameView.setText(characterList.get(chooseCharacter).name);

        //初始化排序按钮及排序效果
        orderByView.setImageResource(isOrderByLV? R.drawable.order2:R.drawable.order1);
        updateSortedOutcome();
        memoriaAdapter.notifyItemRangeChanged(0,memoriaBag.size());

        orderByView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOrderByLV = !isOrderByLV;
                orderByView.setImageResource(isOrderByLV? R.drawable.order2:R.drawable.order1);
                updateSortedOutcome();
                memoriaAdapter.notifyItemRangeChanged(0,memoriaBag.size());
            }
        });
        orderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDesc = !isDesc;
                orderView.setImageResource(isDesc? R.drawable.desc:R.drawable.incr);
                updateSortedOutcome();
                memoriaAdapter.notifyItemRangeChanged(0,memoriaBag.size());
            }
        });

        for(int i = 0; i < 4; i++){
            final CardView cv = equipMemoriaViewList.get(i);
            final int id = i;
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(id < characterList.get(chooseCharacter).breakThrough){
                        if(chooseEquipSlot == -1){
                            chooseEquipSlot = id;
                            cv.setChoose(true);
                        }else if(chooseEquipSlot == id){
                            //取消对自己的选择
                            chooseEquipSlot = -1;
                            cv.setChoose(false);
                            showingMemoriaView.setUnusable();
                            HPShowingView.setText("");
                            ATKShowingView.setText("");
                            DEFShowingView.setText("");
                            break_throughLinearLayout.setVisibility(View.INVISIBLE);
                            coolTimeView.setVisibility(View.INVISIBLE);
                            showingMemoriaNameView.setVisibility(View.INVISIBLE);
                            skillIcon.setVisibility(View.INVISIBLE);
                            skillDescriptionView.setText("");
                            skillNameView.setVisibility(View.INVISIBLE);
                            setView.setVisibility(View.INVISIBLE);
                            clearBagChoose();
                            updateEquippingState();
                            return;
                        }else{
                            equipMemoriaViewList.get(chooseEquipSlot).setChoose(false);
                            chooseEquipSlot = id;
                            cv.setChoose(true);
                        }
                        Memoria m = characterList.get(chooseCharacter).memoriaList[id];
                        if(m != null){
                            showingMemoriaView.setMemoria(m.id);
                            showingMemoriaView.setLv(m.lvNow,m.lvMax);
                            showingMemoriaView.setChoose(false);
                            showingMemoriaNameView.setText(m.name);
                            HPShowingView.setText(m.breakthrough == 4?  ""+ m.HPAfter :  "" +m.HPOrigin);
                            ATKShowingView.setText(m.breakthrough == 4? ""+ m.ATKAfter: "" +m.ATKOrigin);
                            DEFShowingView.setText(m.breakthrough == 4? ""+ m.DEFAfter: "" +m.DEFOrigin);
                            break_throughLinearLayout.setVisibility(m.breakthrough == 4? View.VISIBLE:View.INVISIBLE);
                            if(m.DEFOrigin != 0){
                                coolTimeView.setVisibility(View.VISIBLE);
                                coolTimeView.setText("冷却回合数 "+(m.breakthrough == 4? m.CDAfter:m.CDOrigin));
                            }else{
                                coolTimeView.setVisibility(View.INVISIBLE);
                            }
                            showingMemoriaNameView.setVisibility(View.VISIBLE);
                            skillNameView.setVisibility(View.VISIBLE);
                            //skillNameView.setText();
                            skillIcon.setImageResource(getResourceByString(m.icon));
                            skillIcon.setVisibility(View.VISIBLE);
                            skillDescriptionView.setText(m.getEffectDescription());
                            setView.setImageResource(R.drawable.uninstall);
                            setView.setVisibility(View.VISIBLE);
                        }else{
                            clearShowing();
                        }
                        clearBagChoose();
                        updateEquippingState();
                    }
                }

            });
        }

        uninstallAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < 4; i++){
                    Character ch = characterList.get(chooseCharacter);
                    if(i < ch.breakThrough){
                        //说明已经开孔
                        if(ch.memoriaList[i] != null){
                            ch.memoriaList[i].characterId = -1;
                            ch.memoriaList[i] = null;
                        }
                    }
                }
                clearAllChoose();
                updateEquippingState();
            }
        });

        setView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((chooseEquipSlot != -1) && (memoriaAdapter.choosingMemoria != null)){
                    //set状态
                    Character ch = characterList.get(chooseCharacter);
                    if(ch.memoriaList[chooseEquipSlot] != null){
                        //把自己装备的这张记忆卸下来
                        ch.memoriaList[chooseEquipSlot].characterId = -1;
                        ch.memoriaList[chooseEquipSlot] = null;
                    }
                    if(memoriaAdapter.choosingMemoria.characterId != -1){
                        //把别人装备的这张记忆卸下来
                        Character ch2 = characterList.get(memoriaAdapter.choosingMemoria.characterId);
                        for(int i = 0; i < 4; i++){
                            if(ch2.memoriaList[i] == memoriaAdapter.choosingMemoria){
                                ch2.memoriaList[i] = null;
                            }
                        }
                        memoriaAdapter.choosingMemoria.characterId = -1;
                    }
                    ch.memoriaList[chooseEquipSlot] = memoriaAdapter.choosingMemoria;
                    memoriaAdapter.choosingMemoria.characterId = chooseCharacter;
                }else{
                    //取下状态
                    Character ch = characterList.get(chooseCharacter);
                    if(ch.memoriaList[chooseEquipSlot] != null){
                        //把自己装备的这张记忆卸下来
                        ch.memoriaList[chooseEquipSlot].characterId = -1;
                        ch.memoriaList[chooseEquipSlot] = null;
                    }
                }
                clearAllChoose();
                updateEquippingState();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllChoose();
                Intent receivedIntent = getIntent();
                int battleId = receivedIntent.getIntExtra("battleInfo",-1);
                // 按照百分比增加血量
                float HPRatio = receivedIntent.getFloatExtra("HPRatio",1.0f);
                characterList.get(chooseCharacter).realHP = (int)(1.0f*characterList.get(chooseCharacter).getRealMaxHP()*HPRatio);
                //Log.d("Sam","realMaxHp:"+characterList.get(chooseCharacter).getRealMaxHP());
                if(characterList.get(chooseCharacter).realHP <= 1){
                    characterList.get(chooseCharacter).realHP = 1;
                }
                Intent intent1 = new Intent(MemoriaActivity.this,TeamChooseActivity.class);
                intent1.putExtra("battleInfo",battleId);
                startActivity(intent1);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllChoose();
                Intent receivedIntent = getIntent();
                int battleId = receivedIntent.getIntExtra("battleInfo",-1);
                // 按照百分比增加血量
                float HPRatio = receivedIntent.getFloatExtra("HPRatio",1.0f);
                characterList.get(chooseCharacter).realHP = (int)(1.0f*characterList.get(chooseCharacter).getRealMaxHP()*HPRatio);
                //Log.d("Sam","realMaxHp:"+characterList.get(chooseCharacter).getRealMaxHP());
                if(characterList.get(chooseCharacter).realHP <= 1){
                    characterList.get(chooseCharacter).realHP = 1;
                }
                Intent intent1 = new Intent(MemoriaActivity.this,TeamChooseActivity.class);
                intent1.putExtra("battleInfo",battleId);
                startActivity(intent1);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        updateEquippingState();

        Intent launchIntent = getIntent();
        if(launchIntent != null){
            int touchMemoriaId = launchIntent.getIntExtra("touchMemoriaId",-1);
            if(touchMemoriaId >= 0){
                //默认点击该框
                equipMemoriaViewList.get(touchMemoriaId).callOnClick();
            }
        }
    }

    public void updateEquippingState(){
        int addHp = 0;
        int addATK = 0;
        int addDEF = 0;
        for(int i = 0; i < 4; i++){
            CardView cv = equipMemoriaViewList.get(i);
            Character ch = characterList.get(chooseCharacter);
            if(i < ch.breakThrough){
                //说明已经开孔
                if(ch.memoriaList[i] == null){
                    cv.setEmpty();
                }else{
                    cv.setMemoria(ch.memoriaList[i].id);
                    cv.setLv(ch.memoriaList[i].lvNow,ch.memoriaList[i].lvMax);
                    addHp += (ch.memoriaList[i]. breakthrough == 4)? ch.memoriaList[i].HPAfter:ch.memoriaList[i].HPOrigin;
                    addATK += (ch.memoriaList[i].breakthrough == 4)? ch.memoriaList[i].ATKAfter:ch.memoriaList[i].ATKOrigin;
                    addDEF += (ch.memoriaList[i].breakthrough == 4)? ch.memoriaList[i].DEFAfter:ch.memoriaList[i].DEFOrigin;
                }
                if(i == chooseEquipSlot){
                    cv.setChoose(true);
                }else{
                    cv.setChoose(false);
                }
            }else{
                cv.setUnusable();
            }
        }
        addHPView.setText("+"+addHp);
        addATKView.setText("+"+addATK);
        addDEFView.setText("+"+addDEF);

        updateSortedOutcome();
        memoriaAdapter.notifyItemRangeChanged(0,memoriaBag.size());
    }

    public void clearBagChoose(){
        if(memoriaAdapter.choosingMemoria != null){
            memoriaAdapter.choosingMemoria.isChoosed = false;
            memoriaAdapter.choosingMemoria = null;
            for(MemoriaAdapter.ViewHolder h: memoriaAdapter.holderList){
                h.cardView.setChoose(false);
            }
        }
    }

    public void clearAllChoose(){
        clearBagChoose();
        if(chooseEquipSlot != -1){
            equipMemoriaViewList.get(chooseEquipSlot).setChoose(false);
            chooseEquipSlot = -1;
        }
        clearShowing();
    }

    public void clearShowing(){
        showingMemoriaView.setUnusable();
        HPShowingView.setText("");
        ATKShowingView.setText("");
        DEFShowingView.setText("");
        break_throughLinearLayout.setVisibility(View.INVISIBLE);
        coolTimeView.setVisibility(View.INVISIBLE);
        showingMemoriaNameView.setVisibility(View.INVISIBLE);
        skillIcon.setVisibility(View.INVISIBLE);
        skillDescriptionView.setText("");
        skillNameView.setVisibility(View.INVISIBLE);
        setView.setVisibility(View.INVISIBLE);
    }
    public int getResourceByString(String idName){
        return getResources().getIdentifier(idName,"drawable", getPackageName());
    }

    public void updateSortedOutcome(){
        Collections.sort(memoriaBag, new Comparator<Memoria>() {
            @Override
            public int compare(Memoria lhs, Memoria rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                int weight = 0;
                if(lhs.characterId == MemoriaActivity.chooseCharacter){
                    weight -= 2;
                }else if(lhs.characterId != -1){
                    weight -= 1;
                }
                if(rhs.characterId == MemoriaActivity.chooseCharacter){
                    weight += 2;
                }else if(rhs.characterId != -1){
                    weight += 1;
                }
                if(weight != 0){
                    return weight;
                }
                if(isOrderByLV){
                    return lhs.lvNow > rhs.lvNow ? (isDesc? -1:1) : (lhs.lvNow < rhs.lvNow ) ? (isDesc? 1:-1):(Integer.parseInt(lhs.id) > Integer.parseInt(rhs.id)? -1:(Integer.parseInt(lhs.id) < Integer.parseInt(rhs.id) ? 1:0));
                }else{
                    return lhs.star > rhs.star ? (isDesc? -1:1) : (lhs.star < rhs.star ) ? (isDesc? 1:-1) :(Integer.parseInt(lhs.id) > Integer.parseInt(rhs.id)? -1:(Integer.parseInt(lhs.id) < Integer.parseInt(rhs.id) ? 1:0));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}

class Memoria{
    static JSONObject textList;

    Context context;
    String name;
    int breakthrough = 0;
    int star;
    int HPOrigin;
    int HPAfter;
    int ATKOrigin;
    int ATKAfter;
    int DEFOrigin;
    int DEFAfter;
    int CDOrigin;
    int CDAfter;
    int CDNow;
    String id; // 格式: "2210"
    String icon; // 格式: "icon_skill_1087"
    int characterId = -1; // 存的是characterlist中的id, -1代表没有人装备
    ArrayList<SkillEffect> effectOriginList = new ArrayList<>();
    ArrayList<SkillEffect> effectAfterList = new ArrayList<>();

    boolean isChoosed = false;
    boolean canSet = true;

    int lvNow = 1;
    int lvMax = 50;


    public Memoria(String id, Context c){
        this.context = c;
        this.id = id;
        if(textList == null){
            InputStream stream = context.getResources().openRawResource(R.raw.memoria_dic);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            try{
                while((line = reader.readLine()) != null){
                    sb.append(line);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            try{
                textList = new JSONObject(sb.toString());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        try{
            JSONObject m = textList.getJSONObject(id);
            name = m.optString("name");
            star = m.optInt("star");
            HPOrigin = m.optInt("HPOrigin");
            HPAfter = m.optInt("HPAfter");
            ATKOrigin = m.optInt("ATKOrigin");
            ATKAfter = m.optInt("ATKAfter");
            DEFOrigin = m.optInt("DEFOrigin");
            DEFAfter = m.optInt("DEFAfter");
            CDOrigin = m.optInt("CDOrigin");
            CDAfter = m.optInt("CDAfter");

            //Log.d("Sam", "getIcon: "+ m.getString("icon"));
            icon = m.optString("icon");
            icon = icon.substring(0,icon.length()-4);
            JSONArray efList = m.getJSONArray("effectOrigin");
            for(int i = 0; i < efList.length(); i++){
                SkillEffect s = new SkillEffect();
                s.name = efList.getJSONObject(i).optString("name");
                s.value = efList.getJSONObject(i).optInt("value");
                s.target = efList.getJSONObject(i).optString("target");
                s.time = efList.getJSONObject(i).optInt("time");
                s.probability = efList.getJSONObject(i).optInt("probability");
                s.valueTime = efList.getJSONObject(i).optInt("valueTime");
                if(s.probability == 0){
                    s.probability = 100;
                }
                effectOriginList.add(s);
            }
            efList = m.getJSONArray("effectAfter");
            for(int i = 0; i < efList.length(); i++){
                SkillEffect s = new SkillEffect();
                s.name = efList.getJSONObject(i).optString("name");
                s.value = efList.getJSONObject(i).optInt("value");
                s.target = efList.getJSONObject(i).optString("target");
                s.time = efList.getJSONObject(i).optInt("time");
                s.probability = efList.getJSONObject(i).optInt("probability");
                s.valueTime = efList.getJSONObject(i).optInt("valueTime");
                if(s.probability == 0){
                    s.probability = 100;
                }
                effectAfterList.add(s);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        lvMax = (star == 2? 15: (star == 3? 20:30)) + breakthrough*5;
        if(isSkill()){
            CDNow = 0;
        }else{
            CDNow = 0;
        }
    }

    public void setBreakthrough(int breakthrough){
        this.breakthrough = breakthrough;
        lvMax = (star == 2? 15: (star == 3? 20:30)) + breakthrough*5;
        if(isSkill()){
            CDNow = 0;
        }else{
            CDNow = 0;
        }
    }

    public void setLv(int lvNow){
        this.lvNow = lvNow;
    }

    public void setCarrier(int characterId){
        this.characterId = characterId;
    }

    public boolean isSkill(){
        return DEFOrigin > 0;
    }

    public String getEffectDescription(){
        String allEffect = "";
        for(int j = 0; j < (breakthrough == 4? effectAfterList.size():effectOriginList.size()); j++){
            if(j != 0){
                allEffect += " & ";
            }
            allEffect += (breakthrough == 4? effectAfterList:effectOriginList).get(j).getEffectDescription();
        }
        return allEffect;
    }

    public String getEffectDescription(boolean isAfter){
        String allEffect = "";
        for(int j = 0; j < (isAfter? effectAfterList.size():effectOriginList.size()); j++){
            if(j != 0){
                allEffect += "\n";
            }
            allEffect += (isAfter? effectAfterList:effectOriginList).get(j).getEffectDescription();
        }
        return allEffect;
    }

    public int getRealHP(){
        return (int)((HPAfter - HPOrigin) * 1.0f * (lvNow - 1) / lvMax) + HPOrigin;
    }
    public int getRealATK(){
        return (int)((ATKAfter - ATKOrigin) * 1.0f * (lvNow - 1) / lvMax) + ATKOrigin;
    }
    public int getRealDEF(){
        return (int)((DEFAfter - DEFOrigin) * 1.0f * (lvNow - 1) / lvMax) + DEFOrigin;
    }
}

class SkillEffect{
    String name;
    int value;
    String target; // target为""时默认效果对自己
    int valueTime; // 仅对"攻击时给予状态异常效果"有效, 为异常效果持续的时间
    int time;
    int probability;
    public SkillEffect(){};
    public SkillEffect(String name, int value, String target, int time, int probability){
        this.name = name;
        this.value = value;
        this.target = target;
        this.time = time;
        this.probability = probability;
        this.valueTime = 0;
    }
    public SkillEffect(String name, int value, String target, int time, int probability, int valueTime){
         this.name = name;
         this.value = value;
         this.target = target;
         this.time = time;
         this.probability = probability;
         this.valueTime = valueTime;
    }

    public String getEffectDescription(){
        String allEffect = name;
        if(valueTime != 0){
            allEffect += "(" + valueTime + "T)";
        }
        if(probability == 100 || !hasProbability(name)){
            if(value > 0 && hasValue(name)){
                allEffect += "[" + value + "%]";
            }
        }else{
            if(value > 0 && hasValue(name)){
                allEffect += "(" + value + "%)";
            }
            allEffect += "[发动率:"+ probability + "%]";
        }
        if(!target.equals("")){
            allEffect += "(" + target;
            if(time != 0 && hasTime(name)){
                allEffect += "/" + time + "T";
            }
            allEffect += ")";
        }
        return allEffect;
    }

    public boolean hasValue(String efName){
        switch(efName){
            case "状态异常1回无效": case "Magia封印无效":
            case "回避无效": case "反击无效":
            case "毒无效":case "挑拨无效": case "诅咒无效":
            case "雾无效": case "忍耐":
            case "挑拨":case "追击":case "反击":case "回避":
            case "暴击":case "保护同伴":
            case "烧伤无效":case "黑暗无效":case "技能封印无效":
            case "魅惑无效": case "DEBUFF无效":
            case "无视防御力": case "伤害削减无效":
            case "眩晕无效":case "技能冷却加速":
            case "拘束无效": case "幻惑无效":
            case "攻击时给予魅惑状态":case "攻击时给予眩晕状态": case "攻击时给予拘束状态":
            case "攻击时给予雾状态": case "攻击时给予黑暗状态": case "攻击时给予幻惑状态":
            case "攻击时给予Magia封印状态":case "攻击时给予技能封印状态":
            case "攻击时给予HP回复禁止状态":case "攻击时给予MP回复禁止状态":
            case "重抽为Accele的Disc":
            case "重抽为Blast的Disc":case "重抽Disc":
            case "重抽为同属性的Disc":case "重抽为Charge的Disc":
            case "BUFF解除":  case "DEBUFF解除": case "异常状态解除":
            case "重抽为自己的Disc":case "给予状态幻惑":
            case "给予状态技能封印":case "给予状态魅惑":case "给予状态眩晕":
            case "给予状态拘束":case "给予状态雾":case "给予状态MP回复禁止":
            case "给予状态黑暗":case "Magia封印": case "技能封印":
                return false;
        }
        return true;
    }

    public boolean hasProbability(String efName){
        switch(efName){
            case "攻击时给予状态雾": case "攻击时给予状态黑暗": case "攻击时给予状态幻惑":
            case "攻击时给予状态毒":case "攻击时给予状态烧伤": case "攻击时给予状态诅咒":
            case "挑拨":case "追击":case "反击":case "回避":
            case "暴击":case "保护同伴": case "Magia封印无效":
            case "回避无效": case "反击无效":
            case "毒无效":case "挑拨无效": case "诅咒无效":
            case "雾无效": case "忍耐":
            case "烧伤无效":case "黑暗无效":case "技能封印无效":
            case "魅惑无效": case "DEBUFF无效":
            case "无视防御力": case "伤害削减无效":
            case "眩晕无效": case "技能冷却加速":
            case "拘束无效": case "幻惑无效":
            case "攻击时给予状态魅惑":case "攻击时给予状态眩晕": case "攻击时给予状态拘束":
            case "攻击时给予状态Magia封印":case "攻击时给予状态技能封印":
            case "攻击时给予状态HP回复禁止":case "攻击时给予状态MP回复禁止":
            case "BUFF解除":  case "DEBUFF解除": case "异常状态解除":
            case "MP伤害":case "MP回复":
            case "给予状态黑暗":case "给予状态雾":case "给予状态幻惑":
            case "给予状态毒": case "给予状态烧伤":case "给予状态诅咒":
            case "给予状态技能封印":case "给予状态魅惑":case "给予状态眩晕":
            case "给予状态拘束":case "给予状态MP回复禁止":case "给予状态HP回复禁止":
            case "Magia封印": case "技能封印":
                return true;
        }
        return false;
    }

    public boolean hasTime(String efName){
        switch(efName){
            case "HP最大时防御力UP":
            case "Blast攻击时MP获得":
            case "HP最大时攻击力UP":
            case "战斗不能时获得防御力UP": case "战斗不能时获得攻击力UP":
            case "状态异常1回无效":
            case "重抽为Accele的Disc":
            case "重抽为Blast的Disc":case "重抽Disc":
            case "重抽为同属性的Disc":case "重抽为Charge的Disc":
            case "BUFF解除":  case "DEBUFF解除": case "异常状态解除":
            case "重抽为自己的Disc":
            case "MP伤害":case "MP回复": case "以MP积累状态开始战斗":case "HP回复":
            return false;
        }
        return true;
    }
}

class Character{
    Memoria[] memoriaList = {null,null,null,null};

    ArrayList<SkillEffect> connectOriginEffectList = new ArrayList<>();
    ArrayList<SkillEffect> connectAfterEffectList = new ArrayList<>();

    int breakThrough = 3;
    String element = "tree";
    String name = "柊音梦";
    String spriteName = "Hiiragi Nemu";
    String choosingActivityImage = "team_choose_101400_1";
    String charIconImage = "card_10144_";// 灯花10074 10075 音梦10144 10145 忧10154 10155
    int[] plateList = {ACCELE,ACCELE,ACCELE,BLAST_VERTICAL,CHARGE};
    boolean isLeader = false;

    String magiaTarget = "敌全"; // 敌全 敌单
    int magiaOriginMagnification = 1128;
    int magiaAfterMagnification = 1192;
    int doppelMagnification = 2763;

    ArrayList<SkillEffect> magiaOriginEffectList = new ArrayList<>();
    ArrayList<SkillEffect> magiaAfterEffectList = new ArrayList<>();
    ArrayList<SkillEffect> doppelEffectList = new ArrayList<>();
    String magiaSkillIconName = "icon_skill_1014";
    String doppelImageName = "mini_100700_dd";


    int formationX = 0; // 在战斗中有效，记录formation上的位置坐标
    int formationY = 0;
    int diamondNumber = 0; // 在战斗中有效，记录plate上diamond的数量，战斗结束和开始时重新清零
    int actionOrder = 0; // 在战斗中的行动盘优先级顺序，战斗结束和开始的时候重新清零


    float mpAttackRatio = 1.0f;
    float mpDefendRatio = 1.0f;
    int lv = 80;
    int star = 4;
    int HP = 35920;//基础HP，真实的HP上限用getRealMaxHP()
    int ATK = 11402;
    int DEF = 9014;
    int realHP;
    int realMP;

    ArrayList<Effect> initialEffectList = new ArrayList<>();
    public Character(){

    }

    public void setMemoria(int position, Memoria memoria){
        memoriaList[position] = memoria;
    }

    public boolean canEquipSkill(int chooseId){
        int skillNumber = 0;
        for(int i = 0; i < 4; i++){
            if(memoriaList[i] != null){
                if(i != chooseId && memoriaList[i].DEFOrigin > 0){
                    skillNumber++;
                }
            }
        }
        return skillNumber < 2;
    }

    public boolean canEquipNormalcy(int chooseId){
        int normalcyNumber = 0;
        for(int i = 0; i < 4; i++){
            if(memoriaList[i] != null){
                if(i != chooseId && memoriaList[i].DEFOrigin == 0){
                    normalcyNumber++;
                }
            }
        }
        return normalcyNumber < 2;
    }

    public int getMaxMp(){
        return star == 5? 2000:1000;
    }

    public int getRealMaxHP(){
        int output = this.HP;
        for(int i = 0; i < 4; i++){
            if(memoriaList[i] != null){
                output += memoriaList[i].getRealHP();
            }
        }
        return output;
    }

    public int getRealATK(){
        int output = this.ATK;
        for(int i = 0; i < 4; i++){
            if(memoriaList[i] != null){
                output += memoriaList[i].getRealATK();
            }
        }
        return output;
    }

    public int getRealDEF(){
        int output = this.DEF;
        for(int i = 0; i < 4; i++){
            if(memoriaList[i] != null){
                output += memoriaList[i].getRealDEF();
            }
        }
        return output;
    }
}

