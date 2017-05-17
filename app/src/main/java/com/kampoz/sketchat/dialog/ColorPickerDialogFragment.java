package com.kampoz.sketchat.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.shape.ColorCircleView;

public class ColorPickerDialogFragment extends DialogFragment {
  Paint paint = new Paint();
  //ImageView drawingImageView;

  @Override
  public Dialog onCreateDialog(Bundle ssvadInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_color_picker, null);

    ColorCircleView  colorCircleView = new ColorCircleView(getActivity());

    final ImageView drawingImageView = (ImageView) view.findViewById(R.id.ivColorsContainet);
    ViewTreeObserver vto = drawingImageView.getViewTreeObserver();
    vto.addOnGlobalLayoutListener (new OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        drawingImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        int parentWidth = ((LinearLayout)drawingImageView.getParent()).getWidth();

        int drawingImageViewWidth  = parentWidth-50;
        int drawingImageViewHeight  = parentWidth-50;
        int singleSquareWidth = drawingImageViewWidth/10;

        //int drawingImageViewWidth  = drawingImageView.getLayoutParams().height;
        //int drawingImageViewHeight = drawingImageView.getLayoutParams().width;

        Bitmap bitmap = Bitmap.createBitmap(drawingImageViewWidth, drawingImageViewHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawingImageView.setImageBitmap(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        float x = 250;
        float y = 250;
        float radius = 100;
        canvas.drawCircle(x, y, radius, paint);

        int red, green, blue;
        float left, top, right, bottom;

        left = 0;
        top = 0;
        right = 0;
        bottom = 0;

        red = 255;
        green = 0;
        blue = 0;

        int counter = 0;

        for(int i = 0; i< drawingImageViewWidth; i+=singleSquareWidth){
          for(int j = 0; j< drawingImageViewWidth; j+=singleSquareWidth) {
            paint.setStyle(Style.FILL);
            paint.setColor(Color.rgb(red, green, blue));
            canvas.drawRect(j, i, j+singleSquareWidth, i+singleSquareWidth, paint);
            //red =+ 3;
            green+=25;
            if(counter>33){
              blue+=25;
            }
            if(counter>66) {
              red -= 25;
            }
            counter +=1;
          }
        }

//        paint.setStyle(Style.FILL);
//        canvas.drawRect(200,200,250,250, paint);
//        paint.setColor(Color.rgb(0,0,100));
//        canvas.drawRect(250,200,300,250, paint);
      }
    });

    builder.setView(view);
    Dialog dialog = builder.create();
    return dialog;
  }

}
