package com.huadin.assetstatistics.widget;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

/**
 * Created by 华电 on 2017/5/15.
 */

public class MyFab extends FloatingActionButton {
  private int startX;
  private int startY;
  private int screenWidth;
  private int screenHeight;

  private long startTime = 0;
  private long endTime = 0;


  public MyFab(Context context) {
    this(context,null);
  }

  public MyFab(Context context, AttributeSet attrs) {
    this(context, attrs,0);
  }

  public MyFab(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

    DisplayMetrics dm = getResources().getDisplayMetrics();
    screenWidth = dm.widthPixels;
    screenHeight = dm.heightPixels-230;//减去下边的高度
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {

    switch (ev.getAction()){
      case MotionEvent.ACTION_DOWN:

        startTime = System.currentTimeMillis();

        startX = (int)ev.getRawX();
         startY = (int)ev.getRawY();
        break;
      case MotionEvent.ACTION_MOVE:

        int endX = (int) ev.getRawX();
        int endY = (int) ev.getRawY();

        int dx = endX - startX;
        int dy = endY - startY;

        int left = this.getLeft() + dx;
        int top = this.getTop() + dy;
        int right = this.getRight() + dx;
        int bottom = this.getBottom() + dy;

        startX = endX;
        startY = endY;

        if (left < 0) { //最左边
          left = 0;
          right = left + this.getWidth();
        }
        if (right > screenWidth) { //最右边
          right = screenWidth;
          left = right - this.getWidth();
        }
        if (top < 0) {  //最上边
          top = 0;
          bottom = top + this.getHeight();
        }
        if (bottom > screenHeight) {//最下边
          bottom = screenHeight;
          top = bottom - this.getHeight();
        }

        this.layout(left, top, right, bottom);//设置控件的新位置

        break;
      case MotionEvent.ACTION_UP:
        endTime = System.currentTimeMillis();
        //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
        if ((endTime - startTime) > 0.1 * 1000L) {
          return true;
        }
        break;
    }

    return super.onTouchEvent(ev);
  }



}
