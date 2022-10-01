package com.live2d.rougelike;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


//import org.mozilla.geckoview.GeckoRuntime;
//import org.mozilla.geckoview.GeckoSession;
//import org.mozilla.geckoview.GeckoView;

public class SpriteViewer extends ConstraintLayout {
//    private static GeckoRuntime sRuntime;
    WebView webView;

    Context context;

    public Character c; //此character为只读！

    public boolean isBattle = false;

    public String charName = "street_day"; // resources.js中角色的name字段
    public String spriteName = "all_f_f"; //example: wait,留空就可以在log看到所有的动作名字
                                    // all_f_f all_n_f all_g_f
    public String prefix = "bg_quest_"; //对于背景:bg_quest_  对于人物:mini_

    public int canvasWidth = 1200;//恒定为1200，不改变

    Global global;

    TypedArray ta;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(isBattle){
                switch (message.what){
                    case 0:
                        webView.setVisibility(View.VISIBLE);
                        ((BattleActivity)context).loadedSpriteNumber++;
                        if(((BattleActivity)context).loadedSpriteNumber == ((BattleActivity)context).totalSpriteNumber){

                            //设置战斗bgm
                            global.setNewBGM(global.battleMusicList[(int)(Math.random()*global.battleMusicList.length)], 0.7f);

                            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(((BattleActivity)context).tipLayout, "alpha", 1f, 0);
                            fadeOut.setDuration(500);
                            fadeOut.start();

                            handler.postDelayed(new Runnable(){
                                @Override
                                public void run(){
                                    Character c = global.characters[(int)(Math.random()*global.characters.length)];
                                    while(c == null){
                                        c = global.characters[(int)(Math.random()*global.characters.length)];
                                    }
                                    global.playSound(c.characterId, "battleStart");
                                }
                            }, 500);

                            if(!((BattleActivity)context).randomChoosePlates()){
                                //说明没有可行动角色
                                ((BattleActivity)context).startLeftAttack();
                            }else{
                                ((BattleActivity)context).showPlate();
                            }
                            if(global.collectionDict.get("等等力徽章").isOwn){
                                ((BattleActivity)context).clickable = false;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((BattleActivity)context).smallPlateXList[((BattleActivity)context).smallPlateNumber] = ((BattleActivity)context).chooseMonsterX;
                                        ((BattleActivity)context).smallPlateYList[((BattleActivity)context).smallPlateNumber] = ((BattleActivity)context).chooseMonsterY;
                                        ((BattleActivity)context).setSmallPlate(((BattleActivity)context).smallPlateNumber,0,null);
                                        ((BattleActivity)context).smallPlateNumber++;
                                        ((BattleActivity)context).smallPlateXList[((BattleActivity)context).smallPlateNumber] = ((BattleActivity)context).chooseMonsterX;
                                        ((BattleActivity)context).smallPlateYList[((BattleActivity)context).smallPlateNumber] = ((BattleActivity)context).chooseMonsterY;
                                        ((BattleActivity)context).setSmallPlate(((BattleActivity)context).smallPlateNumber,2,null);
                                        ((BattleActivity)context).smallPlateNumber++;
                                        ((BattleActivity)context).smallPlateXList[((BattleActivity)context).smallPlateNumber] = ((BattleActivity)context).chooseMonsterX;
                                        ((BattleActivity)context).smallPlateYList[((BattleActivity)context).smallPlateNumber] = ((BattleActivity)context).chooseMonsterY;
                                        ((BattleActivity)context).setSmallPlate(((BattleActivity)context).smallPlateNumber,4,null);
                                        ((BattleActivity)context).smallPlateNumber++;
                                        ((BattleActivity)context).prepareRightAttack();
//                                        ((BattleActivity)context).charPlateViewList[0].performClick();
//                                        ((BattleActivity)context).charPlateViewList[2].performClick();
//                                        ((BattleActivity)context).charPlateViewList[4].performClick();
                                        Log.d("Sam","startClick");
                                    }
                                }, 1500);
                            }

                        }else{
                            ((BattleActivity)context).tipLayout.setVisibility(VISIBLE);
                        }
                        break;
                    case 1:
                        webView.setVisibility(View.GONE);
                        break;
                    default:
                }
            }else{
                //map activity
                switch (message.what){
                    case 0:
                        webView.setVisibility(View.VISIBLE);
                        Message m = new Message();
                        m.what = 0;
                        ((MapActivity)context).handler.sendMessage(m);
                        break;
                    default:
                }
            }
            return true;
        }
    });

    @SuppressLint("JavascriptInterface")
    public SpriteViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        isBattle = false;
        this.context = context;
        this.global = (Global)context.getApplicationContext();

        //load canvasWide from attrs
        ta = context.obtainStyledAttributes(attrs,R.styleable.SpriteViewer);
        canvasWidth = ta.getInt(R.styleable.SpriteViewer_canvasWide,1200);
        final float scale = ta.getFloat(R.styleable.SpriteViewer_scale,1f);
        final int offset = ta.getInteger(R.styleable.SpriteViewer_offset,150);
        View v = LayoutInflater.from(context).inflate(R.layout.sprite_viewer_layout, SpriteViewer.this);
        webView = v.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        webView.loadUrl("file:///android_asset/html/sprite_viewer.htm");

//        webView.loadUrl("https://kyu.gay/");
        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
        //webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100){
                    Log.d("HTMLAndroid","loadProgress:"+newProgress+"%");
                    webView.loadUrl("javascript:setCharacterNameAndSprite(\""+ charName +"\",\""+ spriteName +"\")");
                    webView.loadUrl("javascript:setCanvasWidth(" + canvasWidth + ")");
                    webView.loadUrl("javascript:setPrefix(\"" + prefix + "\")");
                    webView.loadUrl("javascript:setScaleAndOffset(" + scale + "," + offset +")");
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("HTMLView", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                return super.shouldInterceptRequest(view, request);
//            }
//            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed() ;
//            }
        });
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed() ;
            }
        });
        webView.setBackgroundColor(Color.parseColor("#00000000"));

        webView.addJavascriptInterface(new JsInterface(this,(MapActivity)(context)),"AndroidMethod");
    }

    @SuppressLint("JavascriptInterface")
    public SpriteViewer(@NonNull Context context, boolean isEmpty) {
        super(context);
        isBattle = true;
        this.global = (Global)context.getApplicationContext();
        if(!isEmpty){
            this.context = context;
            View v = LayoutInflater.from(context).inflate(R.layout.sprite_viewer_layout, SpriteViewer.this);
            webView = v.findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowFileAccessFromFileURLs(true);
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAllowContentAccess(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setHorizontalScrollBarEnabled(false);//水平不显示
            webView.setVerticalScrollBarEnabled(false); //垂直不显示
            webView.loadUrl("file:///android_asset/html/sprite_viewer.htm");
//        webView.loadUrl("https://kyu.gay/");
            WebView.setWebContentsDebuggingEnabled(true);
            webView.setWebChromeClient(new WebChromeClient(){
                //webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if(newProgress == 100){
                        Log.d("HTMLAndroid",newProgress+"");
                        webView.loadUrl("javascript:setCharacterNameAndSprite(\""+ charName +"\",\""+ spriteName +"\")");
                        webView.loadUrl("javascript:setCanvasWidth(" + canvasWidth + ")");
                        webView.loadUrl("javascript:setPrefix(\"" + prefix + "\")");
                        webView.loadUrl("javascript:setScaleAndOffset(" + 1 + "," + 150 +")");
                    }
                    super.onProgressChanged(view, newProgress);
                }

                @Override
                public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                    Log.d("HTMLView", consoleMessage.message() + " -- From line "
                            + consoleMessage.lineNumber() + " of "
                            + consoleMessage.sourceId());
                    return super.onConsoleMessage(consoleMessage);
                }
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                return super.shouldInterceptRequest(view, request);
//            }
//            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed() ;
//            }
            });
            webView.setWebViewClient(new WebViewClient() {
                public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed() ;
                }
            });
            webView.setBackgroundColor(Color.parseColor("#00000000"));

            webView.addJavascriptInterface(new JsInterface(this, (BattleActivity)context),"AndroidMethod");
            webView.setVisibility(GONE);
        }
    }

    public void resetCharacter(){
        webView.loadUrl("javascript:setCharacterNameAndSprite(\""+ charName +"\",\""+ spriteName +"\")");
        webView.loadUrl("javascript:setCanvasWidth(" + canvasWidth + ")");
        webView.loadUrl("javascript:setPrefix(\"" + prefix + "\")");
    }
}

