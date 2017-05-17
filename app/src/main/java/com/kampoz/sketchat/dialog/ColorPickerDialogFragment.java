package com.kampoz.sketchat.dialog;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.shape.ColorCircleView;

/**
 * Created by wasili on 2017-05-17.
 */

public class ColorPickerDialogFragment extends DialogFragment {
  Paint paint = new Paint();
  ImageView drawingImageView;

  @Override
  public Dialog onCreateDialog(Bundle ssvadInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.dialog_color_picker, null);

    ColorCircleView  colorCircleView = new ColorCircleView(getActivity());

    drawingImageView = (ImageView) view.findViewById(R.id.vColorCircle);
    Bitmap bitmap = Bitmap.createBitmap(500
        ,500, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawingImageView.setImageBitmap(bitmap);

    Paint paint = new Paint();
    paint.setColor(Color.RED);
    paint.setStyle(Paint.Style.STROKE);
    float x = 250;
    float y = 250;
    float radius = 200;
    canvas.drawCircle(x, y, radius, paint);

    builder.setView(view);
    Dialog dialog = builder.create();
    return dialog;
  }

}
