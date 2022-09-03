package com.live2d.rougelike;

import android.os.Message;
import android.webkit.JavascriptInterface;

class JsInterface{
    SpriteViewer spriteViewer;
    BattleActivity battleActivity;

    public JsInterface(SpriteViewer spriteViewer, BattleActivity battleActivity){
        this.spriteViewer = spriteViewer;
        this.battleActivity = battleActivity;
    }

    public JsInterface(SpriteViewer spriteViewer){
        this.spriteViewer = spriteViewer;
    }

    @JavascriptInterface
    public void hideView(){
        Message msg = Message.obtain();
        msg.what = 1;
        spriteViewer.handler.sendMessage(msg);
    }

    @JavascriptInterface
    public void showView(){
        Message msg = Message.obtain();
        msg.what = 0;
        spriteViewer.handler.sendMessage(msg);
    }

    @JavascriptInterface
    public void animationEndCallback(int index){
        Message msg = Message.obtain();
        msg.what = index;
        battleActivity.handler.sendMessage(msg);
    }
}
