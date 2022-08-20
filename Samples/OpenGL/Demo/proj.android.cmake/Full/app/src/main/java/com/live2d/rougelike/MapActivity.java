package com.live2d.rougelike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.customview.widget.ViewDragHelper;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

public class MapActivity extends AppCompatActivity implements View.OnTouchListener {

    private int screenHeight;
    private int screenWidth;
    private float mapX = 0;
    private float mapY = 0;
    private ConstraintLayout kamihamaMap;
    //private ViewDragHelper mDragHelper;
//    private float scale = 1;

    private int MODE;//当前状态
    public static final int MODE_NONE = 0;//无操作
    public static final int MODE_DRAG = 1;//单指操作
    public static final int MODE_SCALE = 2;//双指操作

    private PointF startPointF = new PointF();//初始坐标
    private float distance;//初始距离
    private float scaleMultiple = 1;//缩放倍数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        kamihamaMap = findViewById(R.id.kamihamaMap);
        kamihamaMap.setOnTouchListener(this);
        //mDragHelper = ViewDragHelper.create((ViewGroup)(findViewById(R.id.rootActivity)),1.0f, callback);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        screenHeight = kamihamaMap.getMeasuredHeight();
        screenWidth = kamihamaMap.getMeasuredWidth();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //Log.d("Sam", "pivotX:"+ViewHelper.getPivotX(kamihamaMap) + "pivotY:"+ViewHelper.getPivotY(kamihamaMap));

        switch (event.getAction()&event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN://单指触碰
                //起始矩阵先获取ImageView的当前状态
                //获取起始坐标
                startPointF.set(event.getX(), event.getY());
                mapX = ViewHelper.getX(kamihamaMap);
                mapY = ViewHelper.getY(kamihamaMap);
                //此时状态是单指操作
                MODE = MODE_DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN://双指触碰
                //最后的状态传给起始状态
                //获取距离
                distance = getDistance(event);
                mapX = ViewHelper.getX(kamihamaMap);
                mapY = ViewHelper.getY(kamihamaMap);
                //状态改为双指操作
                MODE = MODE_SCALE;
                break;
            case MotionEvent.ACTION_MOVE://滑动（单+双）
                if (MODE == MODE_DRAG) {//单指滑动时
                    mapX += (int)(event.getX() - startPointF.x);
                    mapY += (int)(event.getY() - startPointF.y);
                    ViewHelper.setX(kamihamaMap, mapX);
                    ViewHelper.setY(kamihamaMap, mapY);
                }else if (MODE == MODE_SCALE) {//双指滑动时
                    //计算缩放倍数
                    scaleMultiple = scaleMultiple * getDistance(event)/distance;
                    ViewHelper.setScaleX(kamihamaMap, scaleMultiple);// x方向上缩放
                    ViewHelper.setScaleY(kamihamaMap, scaleMultiple);// y方向上缩放
                }
                break;
            case MotionEvent.ACTION_UP://单指离开
            case MotionEvent.ACTION_POINTER_UP://双指离开
                //防止缩放过度
                if(scaleMultiple > 3){
                    scaleMultiple = 3;
                    ViewHelper.setScaleX(kamihamaMap, scaleMultiple);// x方向上缩放
                    ViewHelper.setScaleY(kamihamaMap, scaleMultiple);// y方向上缩放
                }else if(scaleMultiple < 1){
                    scaleMultiple = 1;
                    ViewHelper.setScaleX(kamihamaMap, scaleMultiple);// x方向上缩放
                    ViewHelper.setScaleY(kamihamaMap, scaleMultiple);// y方向上缩放
                    mapX = 0;
                    mapY = 0;
                    ViewHelper.setX(kamihamaMap, mapX);
                    ViewHelper.setY(kamihamaMap, mapY);
                }

                //防止过度移动, 图片不能移出屏幕的1/4
                if(mapX > ((screenWidth*scaleMultiple)/2 - screenWidth/4.0f)){
                    mapX = (screenWidth*scaleMultiple)/2 - screenWidth/4.0f;
                }else if(mapX < -screenWidth*scaleMultiple/2 + screenWidth/4.0f){
                    mapX = -(screenWidth*scaleMultiple)/2 + screenWidth/4.0f;
                }
                if(mapY > (screenHeight*scaleMultiple)/2 - screenHeight/4.0f){
                    mapY = (screenHeight*scaleMultiple)/2 - screenHeight/4.0f;
                }else if(mapY < -screenHeight*scaleMultiple/2 + screenHeight/4.0f){
                    mapY = -(screenHeight*scaleMultiple)/2 + screenHeight/4.0f;
                }
                ViewHelper.setX(kamihamaMap, mapX);
                ViewHelper.setY(kamihamaMap, mapY);
                //手指离开后，重置状态
                MODE = MODE_NONE;
                break;
        }
//        Log.d("Sam","MapX:" + mapX + ", MapY:" + mapY);
        return true;
    }

    //获取距离
    private static float getDistance(MotionEvent event) {//获取两点间距离
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

//    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
//        /**
//         * 用于判断是否捕获当前child的触摸事件
//         *
//         * @param child
//         *            当前触摸的子view
//         * @param pointerId
//         * @return true就捕获并解析；false不捕获
//         */
//        @Override
//        public boolean tryCaptureView(View child, int pointerId) {
//            return true;
//        }
//
//        /**
//         * 控制水平方向上的位置
//         */
//        @Override
//        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            Log.d("Sam", "Horizontal Move");
////            if (left < (screenWidth - screenWidth * scaleMultiple) / 2){
////                left = (int) (screenWidth - screenWidth * scaleMultiple) / 2;// 限制mainView可向左移动到的位置
////            }
////            if (left > (screenWidth * scaleMultiple - screenWidth) / 2){
////                left = (int) (screenWidth * scaleMultiple - screenWidth) / 2;// 限制mainView可向右移动到的位置
////            }
//            return left;
//        }
//
//        public int clampViewPositionVertical(View child, int top, int dy) {
////            if (top < (screenHeight - screenHeight * scaleMultiple) / 2) {
////                top = (int) (screenHeight - screenHeight * scaleMultiple) / 2;// 限制mainView可向上移动到的位置
////            }
////            if (top > (screenHeight * scaleMultiple - screenHeight) / 2) {
////                top = (int) (screenHeight * scaleMultiple - screenHeight) / 2;// 限制mainView可向上移动到的位置
////            }
//            return top;
//        }
//
//    };

}