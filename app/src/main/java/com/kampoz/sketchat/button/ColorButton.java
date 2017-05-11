package com.kampoz.sketchat.button;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by wasili on 2017-05-10.
 * ImegeButtton do wybierania kolorów
 */

public class ColorButton extends ImageButton {

  public interface PaintColorListener {
    void onClick(int color);
  };

  private int color;
  private PaintColorListener listener;

  //private ColorListener listener;

  public ColorButton(Context context){
    super(context);
    setup();
  };

  public ColorButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    setup();
  }

  public ColorButton(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    setup();
  }

  public void setUpColor(int color){
    Drawable background1 = this.getBackground();
    GradientDrawable gradientDrawable = (GradientDrawable) background1;
    gradientDrawable.setColor(ContextCompat.getColor(getContext(),color));
    this.color = ContextCompat.getColor(getContext(),color);
  }

  private void setup() {
    this.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onClick(color);
      }
    });
  }
//
//  public void setListener(ColorListener listener) {
//    this.listener = listener;
//  }


  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public void setListener(PaintColorListener listener) {
    this.listener = listener;
  }
}
