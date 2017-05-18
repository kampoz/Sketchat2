package com.kampoz.sketchat.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.activity.DrawActivity;
import com.kampoz.sketchat.button.ColorButton;
import com.kampoz.sketchat.helper.MyColorRGB;

/**
 * Created by wasili on 2017-05-12.
 */

public class PaletteFragment extends Fragment implements ColorButton.PaintColorListener{

  public interface PaletteCallback{
    void wipeCanvas();
    void onColorChange(int color);
    void undo();
    void showDialog();
  }

  private PaletteCallback paletteCallback;
  SharedPreferences preferences;
  private Button bWipeCanvas;
  private Button bUndo;
  private ColorButton ibColor;
  private int currentColor = -16777216;
  View view;
  MyColorRGB colorRGB = new MyColorRGB(120,12,12);

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_palette, container, false);

    preferences = getActivity().getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
    final SharedPreferences.Editor editor = preferences.edit();
    bWipeCanvas = (Button)view.findViewById(R.id.bWipeCanvas);
    bUndo = (Button)view.findViewById(R.id.bUndo);
    ibColor = (ColorButton) view.findViewById(R.id.ibColor);
    ibColor.setListener(this);
    ibColor.setBackgroundColor(Color.rgb(colorRGB.getRed(), colorRGB.getGreen(), colorRGB.getBlue()));
    bWipeCanvas.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        paletteCallback.wipeCanvas();
      }
    });

    bUndo.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        paletteCallback.undo();
      }
    });

    ibColor.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        paletteCallback.showDialog();
      }
    });
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setHasOptionsMenu(true);
  }


  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    paletteCallback = (DrawActivity)context;
  }


  /*** interface ColorButton.PaintColorListener **/
  @Override
  public void onColorButtonClick(int color) {
    String strColor = String.format("#%06X", 0xFFFFFF & color);
    Log.d("onColorButtonClick", strColor);
    paletteCallback.onColorChange(color);
  }
  /************/

  public void setColorIbColor(int color){
    currentColor = color;
  }

  public void setColorRGB(MyColorRGB colorRGB) {
    this.colorRGB = colorRGB;
  }
}
