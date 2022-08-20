package com.live2d.rougelike;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
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
    public static int SHOWVIEW = 0;
    public static int HIDEVIEW = 1;

    public Character c; //此character为只读！

    public String charName = "street_day"; // resources.js中角色的name字段
    public String spriteName = "all_f_f"; //example: wait,留空就可以在log看到所有的动作名字
                                    // all_f_f all_n_f all_g_f
    public String prefix = "bg_quest_"; //对于背景:bg_quest_  对于人物:mini_

    public int canvasWidth = 1200;//恒定为1200，不改变
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    webView.setVisibility(View.VISIBLE);
                    ((BattleActivity)context).loadedSpriteNumber++;
                    if(((BattleActivity)context).loadedSpriteNumber == ((BattleActivity)context).totalSpriteNumber){
                        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(((BattleActivity)context).tipLayout, "alpha", 1f, 0);
                        fadeOut.setDuration(1000);
                        fadeOut.start();
                        if(!((BattleActivity)context).randomChoosePlates()){
                            //说明没有可行动角色
                            ((BattleActivity)context).startLeftAttack();
                        }else{
                            ((BattleActivity)context).showPlate();
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
            return true;
        }
    });

    @SuppressLint("JavascriptInterface")
    public SpriteViewer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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

    @SuppressLint("JavascriptInterface")
    public SpriteViewer(@NonNull Context context) {
        super(context);
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

    public void resetCharacter(){
        webView.loadUrl("javascript:setCharacterNameAndSprite(\""+ charName +"\",\""+ spriteName +"\")");
        webView.loadUrl("javascript:setCanvasWidth(" + canvasWidth + ")");
        webView.loadUrl("javascript:setPrefix(\"" + prefix + "\")");
    }
}

