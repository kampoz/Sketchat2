package com.kampoz.sketchat.helper;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/**
 * Created by wasili on 2017-05-26.
 */

public class MyDrawerLayout extends DrawerLayout {

  public MyDrawerLayout(Context context) {
    super(context);
  }

  public MyDrawerLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }


  //disable scrim listener
  @Override
  public boolean onInterceptTouchEvent(MotionEvent event) {
    if(isDrawerOpen(GravityCompat.START)){
      if(event.getX() > getChildAt(1).getWidth()){
        return false;
      }
    }
    return super.onInterceptTouchEvent(event);
  }


}
