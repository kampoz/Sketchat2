package com.kampoz.sketchat.activity;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.dialog.ColorPickerDialogFragment;
import com.kampoz.sketchat.fragments.PaletteFragment;
import com.kampoz.sketchat.helper.MyColorRGB;
import com.kampoz.sketchat.realm.DrawPathRealm;
import com.kampoz.sketchat.realm.DrawPointRealm;
import com.kampoz.sketchat.model.PencilView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import java.util.HashMap;
import java.util.Iterator;

public class DrawActivity extends AppCompatActivity
    implements SurfaceHolder.Callback, PaletteFragment.PaletteCallback, ColorPickerDialogFragment.ColorListener {

  private static final String REALM_URL = "realm://" + "100.0.0.21" + ":9080/Draw5";
  private static final String AUTH_URL = "http://" + "100.0.0.21" + ":9080/auth";
  private static final String ID = "kampoz@kaseka.net";
  private static final String PASSWORD = "Murzyn1!";
  private static final int EDGE_WIDTH = 683;
  private volatile Realm realm;
  private SurfaceView surfaceView;
  private double ratio = -1;
  private double marginLeft;
  private double marginTop;
  private DrawThread drawThread;
  private DrawPathRealm currentPath;
  private long idOfLastDrawPath;
  private int currentColor;
  private MyColorRGB currentRGBColor = new MyColorRGB(0,0,0);
  private PencilView currentPencil;
  private HashMap<String, Integer> nameToColorMap = new HashMap<>();
  private HashMap<Integer, String> colorIdToName = new HashMap<>();
  private PaletteFragment paletteFragment;
  private final FragmentManager fragmentManager = getSupportFragmentManager();
  private ColorPickerDialogFragment dialog;
  SharedPreferences preferences;
  private long subjectId = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_draw);
    currentColor = -16777216;
    dialog = new ColorPickerDialogFragment();
    dialog.setColorListener(this);
    dialog.setCurrentColor(currentColor);
    preferences = getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
    final SharedPreferences.Editor editor = preferences.edit();
    paletteFragment = new PaletteFragment();
    setPaletteFragment();
    realm = Realm.getDefaultInstance();
    surfaceView = (SurfaceView) findViewById(R.id.surface_view);
    surfaceView.getHolder().addCallback(DrawActivity.this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.d("Cykl życia DA", "...onStop()...");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d("Cykl życia DA", "...onStart()...");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d("Cykl życia DA", "...onResume()...");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d("Cykl życia DA", "...onPause()...");
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Log.d("Cykl życia DA", "...onBackPressed()...");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.d("Cykl życia DA", "...onRestart()...");
  }
  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (realm != null) {
      realm.close();
      realm = null;
    }
    Log.d("Cykl życia DA", "...onDestroy()...koniec");
  }

  // if we are in the middle of a rotation, realm may be null.
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (realm == null) {
      return false;
    }
    int[] viewLocation = new int[2];
    surfaceView.getLocationInWindow(viewLocation);
    int action = event.getAction();
    if (action == MotionEvent.ACTION_DOWN
        || action == MotionEvent.ACTION_MOVE
        || action == MotionEvent.ACTION_UP
        || action == MotionEvent.ACTION_CANCEL) {
      float x = event.getRawX();
      float y = event.getRawY();
      double pointX = (x - marginLeft - viewLocation[0]) * ratio;
      double pointY = (y - marginTop - viewLocation[1]) * ratio;

      if (action == MotionEvent.ACTION_DOWN) {
        realm.beginTransaction();
        currentPath = realm.createObject(DrawPathRealm.class);
        currentPath.setColor(currentColor);
        DrawPointRealm point = realm.createObject(DrawPointRealm.class);
        point.setX(pointX);
        point.setY(pointY);
        currentPath.getPoints().add(point);
        realm.commitTransaction();
      } else if (action == MotionEvent.ACTION_MOVE) {
        realm.beginTransaction();
        DrawPointRealm point = realm.createObject(DrawPointRealm.class);
        point.setX(pointX);
        point.setY(pointY);
        currentPath.getPoints().add(point);
        realm.commitTransaction();
      } else if (action == MotionEvent.ACTION_UP) {
        realm.beginTransaction();
        currentPath.setCompleted(true);
        DrawPointRealm point = realm.createObject(DrawPointRealm.class);
        point.setX(pointX);
        point.setY(pointY);
        currentPath.getPoints().add(point);
        realm.commitTransaction();
        idOfLastDrawPath = currentPath.getId();
        currentPath = null;
      } else {
        realm.beginTransaction();
        currentPath.setCompleted(true);
        realm.commitTransaction();
        idOfLastDrawPath = currentPath.getId();
        currentPath = null;
      }
      return true;
    }
    return false;
  }

  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder) {
    if (drawThread == null) {
      drawThread = new DrawThread();
      drawThread.start();
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    boolean isPortrait = width < height;
    if (isPortrait) {
      ratio = (double) EDGE_WIDTH / height;
    } else {
      ratio = (double) EDGE_WIDTH / width;
    }
    if (isPortrait) {
      marginLeft = (width - height) / 2.0;
      marginTop = 0;
    } else {
      marginLeft = 0;
      marginTop = (height - width) / 2.0;
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    if (drawThread != null) {
      drawThread.shutdown();
      drawThread = null;
    }
    ratio = -1;
  }

  class DrawThread extends Thread {
    private Realm bgRealm;
    public void shutdown() {
      synchronized (this) {
        if (bgRealm != null) {
          bgRealm.stopWaitForChange();
        }
      }
      interrupt();
    }

    @Override
    public void run() {
      while (ratio < 0 && !isInterrupted()) {
      }
      if (isInterrupted()) {
        return;
      }
      Canvas canvas = null;
      try {
        final SurfaceHolder holder = surfaceView.getHolder();
        canvas = holder.lockCanvas();
        canvas.drawColor(Color.WHITE);
      } finally {
        if (canvas != null) {
          surfaceView.getHolder().unlockCanvasAndPost(canvas);
        }
      }
      while (realm == null && !isInterrupted()) {
      }
      if (isInterrupted()) {
        return;
      }
      bgRealm = Realm.getDefaultInstance();
      final RealmResults<DrawPathRealm> results = bgRealm.where(DrawPathRealm.class).findAll();
      while (!isInterrupted()) {
        try {
          final SurfaceHolder holder = surfaceView.getHolder();
          canvas = holder.lockCanvas();

          synchronized (holder) {
            canvas.drawColor(Color.WHITE);
            final Paint paint = new Paint();
            for (DrawPathRealm drawPath : results) {
              final RealmList<DrawPointRealm> points = drawPath.getPoints();
              final Integer color = drawPath.getColor();//nameToColorMap.get(drawPath.getColor());
              if (color != null) {
                paint.setColor(color);
              } else {
                paint.setColor(currentColor);
              }
              paint.setStyle(Style.STROKE);
              paint.setStrokeWidth((float) (4 / ratio));
              final Iterator<DrawPointRealm> iterator = points.iterator();
              final DrawPointRealm firstPoint = iterator.next();
              final Path path = new Path();
              final float firstX = (float) ((firstPoint.getX() / ratio) + marginLeft);
              final float firstY = (float) ((firstPoint.getY() / ratio) + marginTop);
              path.moveTo(firstX, firstY);
              while (iterator.hasNext()) {
                DrawPointRealm point = iterator.next();
                final float x = (float) ((point.getX() / ratio) + marginLeft);
                final float y = (float) ((point.getY() / ratio) + marginTop);
                path.lineTo(x, y);
              }
              canvas.drawPath(path, paint);
            }
          }
        } finally {
          if (canvas != null) {
            surfaceView.getHolder().unlockCanvasAndPost(canvas);
          }
        }
        bgRealm.waitForChange();
      }
      synchronized (this) {
        bgRealm.close();
      }
    }
  }

  /*** Interface PaletteFragment.PaletteCallback **/
  @Override
  public void wipeCanvas() {
    if (realm != null) {
      realm.executeTransactionAsync(new Realm.Transaction() {
        @Override
        public void execute(Realm r) {
          r.deleteAll();
        }
      });
    }
  }

  @Override
  public void onColorChange(int color) {
    currentColor = color;
  }
  @Override
  public void undo() {
    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        DrawPathRealm lastDrawPath = realm.where(DrawPathRealm.class).equalTo("id", idOfLastDrawPath).findFirst();
        if(lastDrawPath != null)
        lastDrawPath.deleteFromRealm();
      }
    });
  }

  @Override
  public void showDialog() {

    FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
    dialog.setCurrentColorRGB(currentRGBColor);
    dialog.show(fragmentTransaction, "ColorPickerDialogFragment");
  }
  /** End Interface PaletteFragment.PaletteCallback**/

  /*** interface ColorPickerDialogFragment.ColorListener **/
  @Override
  public void setColor(MyColorRGB colorRGB) {
    currentColor = getIntFromColor(colorRGB.getRed(), colorRGB.getGreen(), colorRGB.getBlue());
    currentRGBColor = colorRGB;
    paletteFragment.setColorRGB(currentRGBColor);
    //paletteFragment.setColorIbColor(currentColor);
  }

  @Override
  public int getCurrentColor() {
    return currentColor;
  }

  /*************************/

  private void setPaletteFragment() {
    FragmentTransaction fragmentTransaction = this.fragmentManager.beginTransaction();
    //paletteFragment.setColorIbColor(currentColor);
    //currentColor = getIntFromColor(colorRGB.getRed(), colorRGB.getGreen(), colorRGB.getBlue());
    fragmentTransaction.replace(R.id.fl_palette_fragment_container, paletteFragment);
    fragmentTransaction.commit();

    //paletteFragment.setColorRGB(currentRGBColor);
  }

  public int getIntFromColor(int Red, int Green, int Blue){
    Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
    Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
    Blue = Blue & 0x000000FF; //Mask out anything not blue.
    return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
  }

  public long getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(long subjectId) {
    this.subjectId = subjectId;
  }
}

