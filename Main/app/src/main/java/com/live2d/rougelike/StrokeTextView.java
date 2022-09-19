package com.live2d.rougelike;

import android.content.Context;

import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextPaint;

import android.util.AttributeSet;

import android.util.Log;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;


public class StrokeTextView extends AppCompatTextView {

    private TextView backGroundText = null;//用于描边的TextView

    TypedArray ta;

    int backgroundColor;
    public StrokeTextView(Context context) {

        this(context, null);
    }

    public StrokeTextView(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
        ta = context.obtainStyledAttributes(attrs,R.styleable.StrokeTextView);
        backgroundColor = ta.getColor(R.styleable.StrokeTextView_backgroundColor,getResources().getColor(R.color.colorShader));

    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        ta = context.obtainStyledAttributes(attrs,R.styleable.StrokeTextView);
        backGroundText =new TextView(context, attrs, defStyle);

    }

    @Override

    public void setLayoutParams(ViewGroup.LayoutParams params) {

//同步布局参数

        backGroundText.setLayoutParams(params);

        super.setLayoutParams(params);

    }

    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        CharSequence tt =backGroundText.getText();

        //两个TextView上的文字必须一致

        if (tt ==null || !tt.equals(this.getText())) {

            backGroundText.setText(getText());

            this.postInvalidate();

        }

        backGroundText.measure(widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        backGroundText.layout(left, top, right, bottom);

        super.onLayout(changed, left, top, right, bottom);

    }

    @Override

    protected void onDraw(Canvas canvas) {

        init();

        backGroundText.draw(canvas);

        super.onDraw(canvas);

    }

    public void setBackGroundColor(int backgroundColor){
        this.backgroundColor = backgroundColor;
        backGroundText.setTextColor(backgroundColor);
    }

    public void init() {

        TextPaint tp1 =backGroundText.getPaint();

        //设置描边宽度
        int wide = ta.getInt(R.styleable.StrokeTextView_wide,4);
        tp1.setStrokeWidth(wide);

        //背景描边并填充全部

        tp1.setStyle(Paint.Style.FILL_AND_STROKE);

        //设置描边颜色

        Log.d("Sam","backgroundColor:"+backgroundColor);
        backGroundText.setTextColor(backgroundColor);


        //将背景的文字对齐方式做同步

        backGroundText.setGravity(getGravity());

    }

}
