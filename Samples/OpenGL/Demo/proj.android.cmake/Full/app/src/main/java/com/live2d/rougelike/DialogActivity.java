/**
 * Copyright(c) Live2D Inc. All rights reserved.
 * <p>
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.live2d.rougelike;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
/*
storyJson格式：
{story:[{char0id,char0motion,char0face,char1...,char2...,text,speakerName}]}

*/



public class DialogActivity extends Activity {
    Plot p;
    GLRenderer _glRenderer;
    GLSurfaceView _glSurfaceView;
    LinearLayout live2dContainer;

    TextView left_name;
    TextView right_name;
    TextView middle_name;
    TextView text;
    ConstraintLayout dialog;

    LinearLayout choiceLinearLayout;
    LinearLayout[] choiceLayout = {null,null,null};
    TextView[] choiceText = {null,null,null};

    LinearLayout blackMask;

    ImageView skip;

    boolean isOver = false;

    float modelAlpha = 0;
    boolean isModelAlphaInc = true;
    public static float ALPHASPEED = 0.15f;
    public static float ALPHABACKGROUNDSPEED = 0.05f;
    public static int plotFlag = 0; // 只读取flag标志和该标志相同的对话，对话默认flag=0
    public static boolean canTouchNext = true;
    boolean canTouchChoice = false;

    int stringCurrentLength = 0;

    Timer dialogTimer = null;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case 1:
                    String str = p.getText();
                    text.setText(str.substring(0,stringCurrentLength));
                    if(stringCurrentLength < str.length()){
                        stringCurrentLength++;
                    }else{
                        stringCurrentLength = 0;
                        if(dialogTimer != null){
                            dialogTimer.cancel();
                            dialogTimer = null;
                        }
                    }
                    break;
                case 2:
                    showPlot();
                    break;
                default:
            }
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        findView();
        p = new Plot();
        loadPlot(p);
        isOver = false;
        JniBridgeJava.SetActivityInstance(this);
        JniBridgeJava.SetContext(this);
        GLRenderer _glRenderer = new GLRenderer();
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(2);
        _glSurfaceView.setRenderer(_glRenderer);
        _glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        live2dContainer.addView(_glSurfaceView);

        canTouchNext = false;
        showPlot();
        final Timer live2dTimer = new Timer();
        TimerTask live2dTimerTask = new TimerTask() {
            @Override
            public void run() {
                setLive2d();
                setAlpha(modelAlpha);
                modelAlpha += ALPHASPEED;
                if(modelAlpha >= 1){
                    setAlpha(1.0f);
                    Message m = new Message();
                    m.what = 2;
                    handler.sendMessage(m);
                    live2dTimer.cancel();
                    canTouchNext = true;
                }
            }
        };
        live2dTimer.schedule(live2dTimerTask,500,50);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canTouchNext && !canTouchChoice){
                    canTouchNext = false;
                    if(p.skip()){
                        isModelAlphaInc = false;
                        stringCurrentLength = 0;
                        if(dialogTimer != null){
                            dialogTimer.cancel();
                            dialogTimer = null;
                        }
                        final Timer live2dTimer = new Timer();
                        TimerTask live2dTimerTask = new TimerTask() {
                            @Override
                            public void run() {
                                if(isModelAlphaInc){
                                    setAlpha(modelAlpha);
                                    modelAlpha += ALPHABACKGROUNDSPEED;
                                    //使屏幕变亮
                                    blackMask.setAlpha(1-modelAlpha);
                                    if(modelAlpha >= 1){
                                        setAlpha(1.0f);
                                        blackMask.setAlpha(0);
                                        Message m = new Message();
                                        m.what = 2;
                                        handler.sendMessage(m);
                                        live2dTimer.cancel();
                                        canTouchNext = true;
                                    }
                                }else{
                                    modelAlpha -= ALPHABACKGROUNDSPEED;
                                    setAlpha(modelAlpha);
                                    //使屏幕变暗
                                    blackMask.setAlpha(1-modelAlpha);
                                    if(modelAlpha <= 0){
                                        setLive2d();
                                        isModelAlphaInc = true;
                                        changeBackground(p.getLastBackgroundId());
                                    }
                                }
                            }
                        };
                        live2dTimer.schedule(live2dTimerTask,100,50);
                    }else{
                        //到末尾了
                        if(dialogTimer != null){
                            dialogTimer.cancel();
                            dialogTimer = null;
                        }
                        jumpToNextActivity();
                    }
                }
            }
        });


        JniBridgeJava.nativeOnStart();
        //getWindow().getDecorView().setSystemUiVisibility(
        //        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //                | View.SYSTEM_UI_FLAG_FULLSCREEN
        //                | (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT
        //                ? View.SYSTEM_UI_FLAG_LOW_PROFILE
        //                : View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        //);
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
    protected void onDestroy() {
        super.onDestroy();
        JniBridgeJava.nativeOnPause();
        JniBridgeJava.nativeOnStop();
        JniBridgeJava.nativeOnDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!p.getBackgroundId().equals("")){
            changeBackground(p.getBackgroundId());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //float pointX = event.getX();
        //float pointY = event.getY();
        //switch (event.getAction()) {
        //    case MotionEvent.ACTION_DOWN:
        //        JniBridgeJava.nativeOnTouchesBegan(pointX, pointY);
        //        break;
        //    case MotionEvent.ACTION_UP:
        //        JniBridgeJava.nativeOnTouchesEnded(pointX, pointY);
        //        break;
        //    case MotionEvent.ACTION_MOVE:
        //        JniBridgeJava.nativeOnTouchesMoved(pointX, pointY);
        //        break;
        //}
        if(canTouchNext){
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if(stringCurrentLength != 0){
                        // 上一句对话还没显示完
                        stringCurrentLength = p.getText().length();
                    }else{
                        if(!isOver){
                            if(dialogTimer != null){
                                dialogTimer.cancel();
                                dialogTimer = null;
                            }
                            stringCurrentLength = 0;
                            isOver = !p.next();
                            canTouchNext = false;
                            if(p.isModelChange() || !p.getBackgroundId().equals("")){
                                isModelAlphaInc = false;
                                final Timer live2dTimer = new Timer();
                                TimerTask live2dTimerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        if(isModelAlphaInc){
                                            modelAlpha += p.getBackgroundId().equals("")? ALPHASPEED:ALPHABACKGROUNDSPEED;
                                            setAlpha(modelAlpha);
                                            if(!p.getBackgroundId().equals("")){
                                                //使屏幕变亮
                                                blackMask.setAlpha(1-modelAlpha);
                                            }
                                            if(modelAlpha >= 1){
                                                setAlpha(1.0f);
                                                blackMask.setAlpha(0f);
                                                Message m = new Message();
                                                m.what = 2;
                                                handler.sendMessage(m);
                                                live2dTimer.cancel();
                                                canTouchNext = true;
                                            }
                                        }else{
                                            modelAlpha -= p.getBackgroundId().equals("")? ALPHASPEED:ALPHABACKGROUNDSPEED;
                                            setAlpha(modelAlpha);
                                            if(!p.getBackgroundId().equals("")){
                                                //使屏幕变暗
                                                blackMask.setAlpha(1-modelAlpha);
                                            }
                                            if(modelAlpha <= 0){
                                                setLive2d();
                                                if(!p.getBackgroundId().equals("")){
                                                    //切换背景
                                                    changeBackground(p.getBackgroundId());
                                                }
                                                isModelAlphaInc = true;
                                            }
                                        }
                                    }
                                };
                                live2dTimer.schedule(live2dTimerTask,100,50);
                            }else{
                                setLive2d();
                                showPlot();
                                canTouchNext = true;
                            }

                        }else{
                            //对话结束了
                            jumpToNextActivity();
                        }
                    }
                    break;
                default:
            }
        }

        return super.onTouchEvent(event);
    }


    private void findView(){
        live2dContainer = findViewById(R.id.live2dContainer);
        left_name = findViewById(R.id.left_name);
        right_name = findViewById(R.id.right_name);
        middle_name = findViewById(R.id.middle_name);
        text = findViewById(R.id.text);
        dialog = findViewById(R.id.dialog);
        choiceLinearLayout = findViewById(R.id.choiceLinearLayout);
        choiceLayout = new LinearLayout[]{findViewById(R.id.choice0Layout),findViewById(R.id.choice1Layout),findViewById(R.id.choice2Layout)};
        choiceText = new TextView[]{findViewById(R.id.choice0Text),findViewById(R.id.choice1Text),findViewById(R.id.choice2Text)};
        skip = findViewById(R.id.skip);
        blackMask = findViewById(R.id.blackMask);
    }


    public boolean loadPlot(Plot p){
        InputStream stream = getResources().openRawResource(R.raw.story1);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer sb = new StringBuffer();
        String line = "";
        try{
            while((line = reader.readLine())!=null){
                sb.append(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        try{
            JSONArray jsonArray = new JSONArray(sb.toString());
            p.setJsonArray(jsonArray);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    void showPlot(){
        if(p.isChoice()){
            dialog.setVisibility(View.GONE);
            choiceLinearLayout.setVisibility(View.VISIBLE);
            canTouchNext = false;
            canTouchChoice = true;
            for(int i = 1; i <= 3; i++){
                String text = p.getChoicesText(i);
                final int temp = i;
                if(!text.equals("")){
                    choiceLayout[i-1].setVisibility(View.VISIBLE);
                    choiceText[i-1].setText(text);
                    choiceLayout[i-1].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(canTouchChoice){
                                canTouchChoice = false;
                                plotFlag = p.getChoicesFlag(temp);
                                //触发下一次点击
                                if(!isOver){
                                    if(dialogTimer != null){
                                        dialogTimer.cancel();
                                        dialogTimer = null;
                                    }
                                    stringCurrentLength = 0;
                                    isOver = !p.next();
                                    canTouchNext = false;
                                    if(p.isModelChange()){
                                        isModelAlphaInc = false;
                                        final Timer live2dTimer = new Timer();
                                        TimerTask live2dTimerTask = new TimerTask() {
                                            @Override
                                            public void run() {
                                                if(isModelAlphaInc){
                                                    modelAlpha += p.getBackgroundId().equals("")? ALPHASPEED:ALPHABACKGROUNDSPEED;
                                                    setAlpha(modelAlpha);
                                                    if(modelAlpha >= 1){
                                                        setAlpha(1.0f);
                                                        Message m = new Message();
                                                        m.what = 2;
                                                        handler.sendMessage(m);
                                                        live2dTimer.cancel();
                                                        canTouchNext = true;
                                                    }
                                                }else{
                                                    modelAlpha -= p.getBackgroundId().equals("")? ALPHASPEED:ALPHABACKGROUNDSPEED;
                                                    setAlpha(modelAlpha);
                                                    if(modelAlpha <= 0){
                                                        setLive2d();
                                                        isModelAlphaInc = true;
                                                    }
                                                }
                                            }
                                        };
                                        live2dTimer.schedule(live2dTimerTask,100,50);
                                    }else{
                                        setLive2d();
                                        showPlot();
                                        canTouchNext = true;
                                    }

                                }
                            }
                        }
                    });
                }else{
                    choiceLayout[i-1].setVisibility(View.GONE);
                }
            }
        }else{
            //是对话
            dialog.setVisibility(View.VISIBLE);
            choiceLinearLayout.setVisibility(View.GONE);
            //speaker setting:
            boolean leftVisibility = false;
            boolean middleVisibility = false;
            boolean rightVisibility = false;
            switch(p.getSpeakerDirection()){
                case 0:
                    leftVisibility = true;
                    left_name.setText(p.getSpeakerName());
                    dialog.setBackground(getResources().getDrawable(R.drawable.dialogue_left));
                    break;
                case 1:
                    middleVisibility = true;
                    middle_name.setText(p.getSpeakerName());
                    dialog.setBackground(getResources().getDrawable(R.drawable.dialogue_middle));
                    break;
                case 2:
                    rightVisibility = true;
                    right_name.setText(p.getSpeakerName());
                    dialog.setBackground(getResources().getDrawable(R.drawable.dialogue_right));
                    break;
                default:
            }
            left_name.setVisibility(leftVisibility? View.VISIBLE:View.INVISIBLE);
            middle_name.setVisibility(middleVisibility? View.VISIBLE:View.INVISIBLE);
            right_name.setVisibility(rightVisibility? View.VISIBLE:View.INVISIBLE);

            //Dialog Setting:
            if(dialogTimer != null){
                dialogTimer.cancel();
                dialogTimer = null;
            }
            dialogTimer = new Timer();
            stringCurrentLength = 0;
            TimerTask dialogTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Message m = new Message();
                    m.what = 1;
                    handler.sendMessage(m);
                }
            };
            dialogTimer.schedule(dialogTimerTask,0,25);
        }
    }

    void setLive2d(){
        //live2d setting:
        JniBridgeJava.nativeSetCharacter(
                p.getModelId(0),p.getModelMotion(0),p.getModelFace(0),
                p.getModelId(1),p.getModelMotion(1),p.getModelFace(1),
                p.getModelId(2),p.getModelMotion(2),p.getModelFace(2)
        );
    }

    void setAlpha(float alpha){
        JniBridgeJava.nativeSetAlpha(alpha);
    }

    void jumpToNextActivity(){
        Intent intent1 = new Intent(DialogActivity.this, MapActivity.class);
        canTouchNext = false;
        startActivity(intent1);
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    void changeBackground(String backgroundNumber){
        JniBridgeJava.nativeChangeBackground("bg_adv_"+backgroundNumber + ".png");
    }
}

class Plot{
    JSONArray jsonArray;
    int id = 0;
    Plot(){}
    Plot(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }
    public void setJsonArray(JSONArray jsonArray){
        this.jsonArray = jsonArray;
    }

    String getModelId(int charId){
        try{
            return ((JSONObject)(jsonArray.get(id))).optString("char"+charId+"id");
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }

    String getModelMotion(int charId){
        try{
            return ((JSONObject)(jsonArray.get(id))).optString("char"+charId+"motion");
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    String getModelFace(int charId){
        try{
            return ((JSONObject)(jsonArray.get(id))).optString("char"+charId+"face");
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    String getText(){
        try{
            return ((JSONObject)(jsonArray.get(id))).optString("text");
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    String getBackgroundId(){
        try{
            return ((JSONObject)(jsonArray.get(id))).optString("background");
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    String getBackgroundId(int id){
        try{
            return ((JSONObject)(jsonArray.get(id))).optString("background");
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    String getLastBackgroundId(){
        try{
            for(int i = id; i >= 0; i--){
                if(!getBackgroundId(i).equals("")){
                    return getBackgroundId(i);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    String getSpeakerName(){
        try{
            return ((JSONObject)(jsonArray.get(id))).optString("speakerName");
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    int getSpeakerDirection(){
        try{
            return ((JSONObject)(jsonArray.get(id))).getInt("speakerDirection");
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    boolean isChoice(){
        try{
            return ((JSONObject)(jsonArray.get(id))).getInt("isChoice") == 1;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    boolean isChoice(int id){
        try{
            return ((JSONObject)(jsonArray.get(id))).getInt("isChoice") == 1;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    String getChoicesText(int i){
        try{
            return ((JSONObject)(jsonArray.get(id))).optString("choose"+i);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    int getChoicesFlag(int i){
        try{
            return ((JSONObject)(jsonArray.get(id))).getInt("choose"+i+"Flag");
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    int getPlotFlag(){
        try{
            return ((JSONObject)(jsonArray.get(id))).getInt("flag");
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    int getPlotFlag(int id){
        try{
            return ((JSONObject)(jsonArray.get(id))).getInt("flag");
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    boolean next(){
        while(id < (jsonArray.length()-1)) {
            id++;
            if(getPlotFlag() == DialogActivity.plotFlag){
                break;
            }
        }
        if(id >= (jsonArray.length()-1)){
            return false;
        }else if(isChoice()){
            return true;
        }else{
            //尝试寻找后面有没有是当前flag的对话
            int temp = id;
            while(temp < (jsonArray.length()-1)) {
                temp++;
                if(getPlotFlag(temp) == DialogActivity.plotFlag){
                    return true;
                }
            }
            return false;
        }
    }

    boolean skip(){
        //false: 到末尾了; true: 停在选择处
        while(id < (jsonArray.length()-1)) {
            id++;
            if(getPlotFlag() == DialogActivity.plotFlag && isChoice()){
                return true;
            }else if(id >= (jsonArray.length()-1)){
                return false;
            }
        }
        return false;
    }

    boolean isModelChange(){
        //现在的人物是否和上一句的人物一致
        if(id == 0){
            return true;
        }
        try{
            int nowFlag = getPlotFlag();
            for(int j = id-1; j >= 0; j--){
                if(getPlotFlag(j) == nowFlag){
                    for(int i = 0; i < 3; i++) {
                        if (!((JSONObject) (jsonArray.get(j))).optString("char" + i + "id").equals(((JSONObject) (jsonArray.get(id))).optString("char" + i + "id"))) {
                            return true;
                        }
                    }
                    return false;
                }else if(isChoice(j)){
                    return true;
                }
            }
            return false;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
