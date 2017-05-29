package com.kampoz.sketchat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.dialog.ColorPickerDialogFragment;
import com.kampoz.sketchat.fragments.PaletteFragment;
import com.kampoz.sketchat.helper.MyColorRGB;
import com.kampoz.sketchat.helper.MyLinearLayout;
import com.kampoz.sketchat.realm.DrawPathRealm;
import com.kampoz.sketchat.realm.DrawPointRealm;
import com.kampoz.sketchat.model.PencilView;
import com.kampoz.sketchat.realm.SubjectRealm;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmList;
import io.realm.RealmResults;
import java.util.HashMap;
import java.util.Iterator;
import android.view.MenuItem;

public class DrawActivity extends AppCompatActivity
    implements SurfaceHolder.Callback, PaletteFragment.PaletteCallback,
    ColorPickerDialogFragment.ColorListener {

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
  private DrawPathRealm currentPath = new DrawPathRealm();
  private long idOfLastDrawPath;
  private int currentColor;
  private MyColorRGB currentRGBColor = new MyColorRGB(0, 0, 0);
  private PencilView currentPencil;
  private HashMap<String, Integer> nameToColorMap = new HashMap<>();
  private HashMap<Integer, String> colorIdToName = new HashMap<>();
  private PaletteFragment paletteFragment;
  private final FragmentManager fragmentManager = getSupportFragmentManager();
  private ColorPickerDialogFragment dialog;
  SharedPreferences preferences;
  private Long currentSubjectId;
  private String currentSubjectTitle;
  //private MyLinearLayout llDrawingContainer;
  //private DrawerLayout drawerLayout;
  private Context context;
  private DrawerLayout drawer;
  private ActionBarDrawerToggle mDrawerToggle;
  private Toolbar toolbar;
  private ImageButton bChat;
  private TextView tvSubjectTitle;
  Canvas canvas = null;
  ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_draw);
    Intent intent = getIntent();
    realm = Realm.getDefaultInstance();
    toolbar = (Toolbar) findViewById(R.id.app_bar);
    //Log.d("DA czas", "1");
    setSupportActionBar(toolbar);
    //Log.d("DA czas", "2");
    getSupportActionBar().setHomeButtonEnabled(false);
    //Log.d("DA czas", "3");
    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    //Log.d("DA czas","4");
    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    //Log.d("DA czas","5");
    tvSubjectTitle = (TextView) findViewById(R.id.tvSubjectTitle);
    //Log.d("DA czas","6");
    bChat = (ImageButton) findViewById(R.id.bChat);
    //Log.d("DA czas","7");
    bChat.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!drawer.isDrawerOpen(Gravity.LEFT)) {
          drawer.openDrawer(Gravity.LEFT);
        }
        if (drawer.isDrawerOpen(Gravity.LEFT)) {
          drawer.closeDrawer(Gravity.LEFT);
        }
      }
    });
    //Log.d("DA czas","8");

    currentSubjectId = intent.getLongExtra("currentSubjectid", 0);
    //Log.d("DA czas","9");
    tvSubjectTitle.setText(getCurrentSubjectTitle(currentSubjectId));
    //Log.d("DA czas","10");
    currentColor = -16777216;
    dialog = new ColorPickerDialogFragment();
    //Log.d("DA czas","11");
    dialog.setColorListener(this);
    Log.d("DA czas", "12");
    dialog.setCurrentColor(currentColor);
    Log.d("DA czas", "13");
    preferences = getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
    Log.d("DA czas", "14");
    final SharedPreferences.Editor editor = preferences.edit();
    Log.d("DA czas", "15");
    paletteFragment = new PaletteFragment();
    Log.d("DA czas", "16");
    setPaletteFragment();
    Log.d("DA czas", "17");
    surfaceView = (SurfaceView) findViewById(R.id.surface_view);
    Log.d("DA czas", "18");
    surfaceView.getHolder().addCallback(DrawActivity.this);
    Log.d("DA czas", "19");

    surfaceView.setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        //Log.d("SurfaceView", "Kliknięto SurfaceView "+event.getRawX()+" "+event.getRawY());
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
            realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getDrawing()
                .getPaths().add(currentPath);
            realm.commitTransaction();
          } else if (action == MotionEvent.ACTION_MOVE) {
            realm.beginTransaction();
            DrawPointRealm point = realm.createObject(DrawPointRealm.class);
            point.setX(pointX);
            point.setY(pointY);
            currentPath.getPoints().add(point);
            realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getDrawing()
                .getPaths().add(currentPath);
            realm.commitTransaction();
          } else if (action == MotionEvent.ACTION_UP) {
            realm.beginTransaction();
            currentPath.setCompleted(true);
            DrawPointRealm point = realm.createObject(DrawPointRealm.class);
            point.setX(pointX);
            point.setY(pointY);
            currentPath.getPoints().add(point);
            realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getDrawing()
                .getPaths().add(currentPath);
            realm.commitTransaction();
            idOfLastDrawPath = currentPath.getId();
            currentPath = null;
          } else {
            realm.beginTransaction();
            currentPath.setCompleted(true);
            realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getDrawing()
                .getPaths().add(currentPath);
            realm.commitTransaction();
            idOfLastDrawPath = currentPath.getId();
            currentPath = null;
          }
          return true;
        }
        return false;
      }
    });
    Log.d("DA czas", "20");

    drawer.getParent().requestDisallowInterceptTouchEvent(true);
    Log.d("DA czas", "21");
    Log.d("DA czas", "=========================================================");

  }

  public String getCurrentSubjectTitle(Long currentSubjectId) {
    return realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getSubject();
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
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
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
    return false;
  }

  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder) {
    Log.d("DA czas", "22");
    ////Todo: Tu Asynctaska dac startujacego wczytanie pierwszy raz rysunku

    /*if (drawThread == null) {
      drawThread = new DrawThread();
      drawThread.start();
    }*/
    new firstSketchDownloadAsyncTask().execute();

    Log.d("DA czas", "23");
    Log.d("DA czas", "=============");
  }

  @Override
  public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    Log.d("DA czas", "24");
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
    Log.d("DA czas", "25");
    Log.d("DA czas", "================");
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
        Log.d("DA czas", "26");
        if (bgRealm != null) {
          bgRealm.stopWaitForChange();
        }
      }
      interrupt();
      Log.d("DA czas", "27");
      Log.d("DA czas", "===============");
    }

    @Override
    public void run() {
      Log.d("DA czas", "28");
      while (ratio < 0 && !isInterrupted()) {
      }
      if (isInterrupted()) {
        return;
      }
      canvas = null;
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
      //final RealmResults<DrawPathRealm> results = bgRealm.where(DrawPathRealm.class).findAll();
      final RealmList<DrawPathRealm> results = bgRealm.where(SubjectRealm.class)
          .equalTo("id", currentSubjectId).
              findFirst().getDrawing().getPaths();
      while (!isInterrupted()) {
        try {
          final SurfaceHolder holder = surfaceView.getHolder();
          canvas = holder.lockCanvas();

          synchronized (holder) {
            Log.d("DA czas", "29");
            if(canvas!=null) {
              canvas.drawColor(Color.WHITE);
            }
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
              if(canvas!=null){
              canvas.drawPath(path, paint);
              }
            }
            Log.d("DA czas", "30");
            if(progressDialog!=null && progressDialog.isShowing()){
              progressDialog.dismiss();
              progressDialog=null;
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
      Log.d("DA czas", "31");
      Log.d("DA czas", "===================");
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_drawer_chat) {
      drawer.openDrawer(Gravity.LEFT);
      return true;
    } else {
      // Handle your other action bar items...
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    //getMenuInflater().inflate(R.menu.menu_drawing, menu);
    //menu =  mActionMenuView.getMenu();
    //getMenuInflater().inflate(R.menu.menu_drawing, menu);
    //mActionMenuView.getMenu().
    return true;
  }

  private class firstSketchDownloadAsyncTask extends AsyncTask<Void, Void, Void> {

    SurfaceHolder holder;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      holder = surfaceView.getHolder();
      canvas = holder.lockCanvas();
      progressDialog = new ProgressDialog(DrawActivity.this);
      progressDialog.setMessage("Loading sketch. Please wait...");
      progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progressDialog.setCancelable(false);
      //progressDialog.setProgressStyle(R.style.MyProgressDialogTheme);
      progressDialog.show();
    }
    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      if (canvas != null) {
        surfaceView.getHolder().unlockCanvasAndPost(canvas);
        if (drawThread == null) {
          drawThread = new DrawThread();
          drawThread.start();
        }
      }
    }
    @Override
    protected Void doInBackground(Void... arg0) {
      /*Realm bgRealm = Realm.getDefaultInstance();
      final RealmList<DrawPathRealm> results = bgRealm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getDrawing().getPaths();
      synchronized (holder) {
        Log.d("DA czas", "32");
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
        Log.d("DA czas", "33");
      }*/
      return null;
    }


  }

  /*** Interfaces methods:**/
  /***
   * Interface PaletteFragment.PaletteCallback
   **/
  @Override
  public void wipeCanvas() {
    if (realm != null) {
      realm.executeTransactionAsync(new Realm.Transaction() {
        @Override
        public void execute(Realm realm) {
          realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getDrawing()
              .getPaths().deleteAllFromRealm();
          //realm.deleteAll();
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
        DrawPathRealm lastDrawPath = realm.where(DrawPathRealm.class)
            .equalTo("id", idOfLastDrawPath).findFirst();
        if (lastDrawPath != null) {
          lastDrawPath.deleteFromRealm();
        }
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

  /***
   * interface ColorPickerDialogFragment.ColorListener
   **/
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

  public int getIntFromColor(int Red, int Green, int Blue) {
    Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
    Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
    Blue = Blue & 0x000000FF; //Mask out anything not blue.
    return 0xFF000000 | Red | Green
        | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
  }

  public long getCurrentSubjectId() {
    return currentSubjectId;
  }

  public void setCurrentSubjectId(long currentSubjectId) {
    this.currentSubjectId = currentSubjectId;
  }
}

