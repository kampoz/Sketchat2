package com.kampoz.sketchat.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.helper.MyColorRGB;

public class ColorPickerDialogFragment extends DialogFragment {
  public interface ColorListener {
    void setColor(MyColorRGB colorRGB);
    //MyColorRGB getCurrentColor();
    int getCurrentColor();
  };

  private ColorListener colorListener;
  private ImageButton[][] buttons = new ImageButton[8][10];
  private MyColorRGB[][] colors = new MyColorRGB[8][10];
  private View ivCurrentColor;
  private int currentColor = -16777216;
  private MyColorRGB currentColorRGB = new MyColorRGB(0,0,0);

  @Override
  public Dialog onCreateDialog(Bundle ssvadInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_color_picker, null);
    ivCurrentColor = view.findViewById(R.id.ivCurrentColor);
    ivCurrentColor.setBackgroundColor(Color.rgb(currentColorRGB.getRed(), currentColorRGB.getGreen(), currentColorRGB.getBlue()));
    createColorsTable();
    createColorButtonsBoard(view);
    builder.setView(view);
    Dialog dialog = builder.create();
    return dialog;
  }

  @Override
  public int show(FragmentTransaction transaction, String tag) {

    Log.d("color picker show()", "currentColorRGB: "+currentColorRGB.getRed()+" "+ currentColorRGB.getGreen()+" "+currentColorRGB.getBlue());

    return super.show(transaction, tag);
  }

  public void createColorButtonsBoard(View view) {
    LinearLayout layoutVertical = (LinearLayout) view.findViewById(R.id.llColorsButtonsContainer);
    LinearLayout rowLayout = null;
    int count = 101;
    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        50, 1);

    for (Integer i = 0; i < 7; i++) {
      if (count % 10 == 1) {
        rowLayout = new LinearLayout(getActivity());
        rowLayout.setWeightSum(10);
        layoutVertical.addView(rowLayout, param);
        count = count - 10;
      }
      for (Integer j = 0; j < 10; j++) {
        buttons[i][j] = new ImageButton(getActivity());
        final MyColorRGB color = colors[i][j];
        //buttons[i][j].setBackgroundResource(R.drawable.fieldblue);
        buttons[i][j]
            .setBackgroundColor(Color.rgb(color.getRed(),color.getGreen(),color.getBlue()));

        rowLayout.addView(buttons[i][j], param);

        final Integer x = i;
        final Integer y = j;
        buttons[x][y].setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                             ivCurrentColor.setBackgroundColor(Color.rgb(color.getRed(),color.getGreen(),color.getBlue()));
                                             colorListener.setColor(color);
                                             ColorPickerDialogFragment.this.dismiss();
                                           }
                                         }
        );
      }
    }
  }

  public void createColorsTable(){
    int count = 0;
    int red = 254;
    int green = 254;
    int blue = 254;
    for (Integer i = 0; i < 7; i++) {
      for (Integer j = 0; j < 10; j++) {
        colors[i][j] = new MyColorRGB(red, green, blue);
        //Log.d("colors[][] "+i+" "+j, colors[i][j].getRed()+" "+colors[i][j].getGreen()+" "+colors[i][j].getBlue());
        if(i==0)blue-=25;
        if(i==1)green-=25;
        if(i==2)blue+=25;
        if(i==3)red-=25;
        if(i==4)green+=25;
        if(i==5)blue-=25;
        if(i==6)green-=25;
      }
    }
    Log.d("colors lenght.....", String.valueOf(colors.length));
  }

  public void setColorListener(ColorListener colorListener) {
    this.colorListener = colorListener;
  }

  public int getCurrentColor() {
    return currentColor;
  }

  public void setCurrentColorRGB(MyColorRGB currentColorRGB) {
    this.currentColorRGB = currentColorRGB;
  }

  public void setCurrentColor(int currentColor) {
    this.currentColor = currentColor;
  }
}
