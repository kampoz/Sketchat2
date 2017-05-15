package com.kampoz.sketchat.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.kampoz.sketchat.R;
import android.content.SharedPreferences;
import com.kampoz.sketchat.activity.DrawActivity;
import com.kampoz.sketchat.button.ColorButton;
import com.kampoz.sketchat.model.DrawPath;
import io.realm.Realm;

/**
 * Created by wasili on 2017-05-12.
 */

public class PaletteFragment extends Fragment implements ColorButton.PaintColorListener{
  SharedPreferences preferences;

  public interface PaletteCallback{
    void wipeCanvas();
    void onColorChange(int color);
    void undo();
  }

  private PaletteCallback paletteCallback;

  private ColorButton ibColor1;
  private ColorButton ibColor2;
  private ColorButton ibColor3;
  private ColorButton ibColor4;
  private ColorButton ibColor5;
  private ColorButton ibColor6;
  private Button bWipeCanvas;
  private Button bUndo;
  View view;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_palette, container, false);

    preferences = getActivity().getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
    final SharedPreferences.Editor editor = preferences.edit();

    ibColor1 = (ColorButton)view.findViewById(R.id.bColor1);
    ibColor2 = (ColorButton)view.findViewById(R.id.bColor2);
    ibColor3 = (ColorButton)view.findViewById(R.id.bColor3);
    ibColor4 = (ColorButton)view.findViewById(R.id.bColor4);
    ibColor5 = (ColorButton)view.findViewById(R.id.bColor5);
    ibColor6 = (ColorButton)view.findViewById(R.id.bColor6);
    bWipeCanvas = (Button)view.findViewById(R.id.bWipeCanvas);
    bUndo = (Button)view.findViewById(R.id.bUndo);
    ibColor1.setUpColor(R.color.colorBlack);
    ibColor2.setUpColor(R.color.colorMyRedDark);
    ibColor3.setUpColor(R.color.colorBallYellowDark);
    ibColor4.setUpColor(R.color.colorMyRed);
    ibColor5.setUpColor(R.color.colorMyGreen);
    ibColor6.setUpColor(R.color.colorMyBlue);

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

    bindButtons();
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setHasOptionsMenu(true);
  }

  private void bindButtons() {
    int[] buttonIds = {
        R.id.bColor1,
        R.id.bColor2,
        R.id.bColor3,
        R.id.bColor4,
        R.id.bColor5,
        R.id.bColor6
    };

    for (int id : buttonIds) {
      ColorButton colorButton = (ColorButton) view.findViewById(id);
      colorButton.setListener(this);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    paletteCallback = (DrawActivity)context;
  }

  @Override
  public void onClick(int color) {
    String strColor = String.format("#%06X", 0xFFFFFF & color);
    Log.d("onClick", strColor);
    paletteCallback.onColorChange(color);
  }

  public void setPaletteCallback(
      PaletteCallback paletteCallback) {
    this.paletteCallback = paletteCallback;
  }
}
