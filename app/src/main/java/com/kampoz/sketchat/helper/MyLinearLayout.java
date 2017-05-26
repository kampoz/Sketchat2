package com.kampoz.sketchat.helper;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * Created by wasili on 2017-05-25.
 */

public class MyLinearLayout extends LinearLayoutCompat {

  private View view;

  public MyLinearLayout(Context context) {
    super(context);
  }

  public MyLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (isHitToView(ev, view)) {
      return false;
    } else {
      return super.onInterceptTouchEvent(ev);
    }
  }

  public static boolean isHitToView(MotionEvent event, View view) {
    if (view == null || event == null || view.getVisibility() != View.VISIBLE)
      return false;

    Rect r = new Rect();
    view.getLocalVisibleRect(r);

    int[] coordinates = new int[2];
    view.getLocationOnScreen(coordinates);

    r.left += coordinates[0];
    r.right += coordinates[0];
    r.top += coordinates[1];
    r.bottom += coordinates[1];

    float x = event.getRawX();
    float y = event.getRawY();

    if (r.right >= x && r.top <= y && r.left <= x && r.bottom >= y)
      return true;
    return false;
  }

  public void setViewToClick(View view) {
    this.view = view;
  }

  @Override
  public void setOnClickListener(OnClickListener l) {
    super.setOnClickListener(l);
    Toast.makeText(getContext(), "Kliknięto MyLinearLayout", Toast.LENGTH_LONG).show();
  }
}
