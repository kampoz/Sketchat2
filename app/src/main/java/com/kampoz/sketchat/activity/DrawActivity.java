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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.dialog.ColorPickerDialogFragment;
import com.kampoz.sketchat.fragments.PaletteFragment;
import com.kampoz.sketchat.helper.MyColorRGB;
import com.kampoz.sketchat.realm.DrawPathRealm;
import com.kampoz.sketchat.realm.DrawPointRealm;
import com.kampoz.sketchat.model.PencilView;
import com.kampoz.sketchat.realm.SubjectRealm;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmList;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import java.util.HashMap;
import java.util.Iterator;
import android.view.MenuItem;

public class DrawActivity extends AppCompatActivity
    implements SurfaceHolder.Callback, PaletteFragment.PaletteCallback,
    ColorPickerDialogFragment.ColorListener {

  private static final String REALM_URL = "realm://" + "100.0.0.21" + ":9080/Draw999";
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
  private Context context;
  private DrawerLayout drawer;
  private ActionBarDrawerToggle mDrawerToggle;
  private Toolbar toolbar;
  private ImageButton bChat;
  private TextView tvSubjectTitle;
  Canvas canvas = null;
  ProgressDialog progressDialog;
  private String tag = "cz DA";
  private int countInThread = 0;
  private String tag1 = "realm instance th";
  private String tagOpen = "+ in Thread open";
  private String tagClose = "- in Thread close";
  private String tagCount = "= Realm instances opened in Thread: ";
  private String tagGlobal = "== globalRealmInstancesCount: ";

  private int countDA = 0;
  private String tag2 = "realm instance DA";
  private String tagOpenDA = "+ in DrawActivity open";
  private String tagCloseDA = "- in DrawActivity close";
  private String tagCountDA = "= Realm instances opened in DrawActivity: ";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_draw);
    Intent intent = getIntent();

    realm = Realm.getDefaultInstance();
    countDA++;
    SplashActivity.globalRealmInstancesCount++;
    Log.d(tag2,"---------DrawActivity OnCreate()------------");
    Log.d(tag2,"-------------------------");
    Log.d(tag2,tagOpenDA);
    Log.d(tag2,tagCountDA + countDA);
    Log.d(tag2,tagGlobal + SplashActivity.globalRealmInstancesCount);

    toolbar = (Toolbar) findViewById(R.id.app_bar);

    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(false);
    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    tvSubjectTitle = (TextView) findViewById(R.id.tvSubjectTitle);
    bChat = (ImageButton) findViewById(R.id.bChat);
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

    currentSubjectId = intent.getLongExtra("currentSubjectid", 0);
    tvSubjectTitle.setText(getCurrentSubjectTitle(currentSubjectId));
    currentColor = -16777216;
    dialog = new ColorPickerDialogFragment();
    dialog.setColorListener(this);
    dialog.setCurrentColor(currentColor);
    preferences = getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
    final SharedPreferences.Editor editor = preferences.edit();
    paletteFragment = new PaletteFragment();
    setPaletteFragment();
    surfaceView = (SurfaceView) findViewById(R.id.surface_view);
    surfaceView.getHolder().addCallback(DrawActivity.this);

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
          final double pointX = (x - marginLeft - viewLocation[0]) * ratio;
          final double pointY = (y - marginTop - viewLocation[1]) * ratio;

          if (action == MotionEvent.ACTION_DOWN) {
            realm.executeTransaction(new Transaction() {
              @Override
              public void execute(Realm realm) {
                currentPath = realm.createObject(DrawPathRealm.class);
                currentPath.setColor(currentColor);
                DrawPointRealm point = realm.createObject(DrawPointRealm.class);
                point.setX(pointX);
                point.setY(pointY);
                currentPath.getPoints().add(point);
                realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst()
                    .getDrawing()
                    .getPaths().add(currentPath);
              }
            });
          } else if (action == MotionEvent.ACTION_MOVE) {
            realm.executeTransaction(new Transaction() {
              @Override
              public void execute(Realm realm) {
                DrawPointRealm point = realm.createObject(DrawPointRealm.class);
                point.setX(pointX);
                point.setY(pointY);
                currentPath.getPoints().add(point);
                realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst()
                    .getDrawing()
                    .getPaths().add(currentPath);
              }
            });
          } else if (action == MotionEvent.ACTION_UP) {
            realm.executeTransaction(new Transaction() {
              @Override
              public void execute(Realm realm) {
                currentPath.setCompleted(true);
                DrawPointRealm point = realm.createObject(DrawPointRealm.class);
                point.setX(pointX);
                point.setY(pointY);
                currentPath.getPoints().add(point);
                realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst()
                    .getDrawing()
                    .getPaths().add(currentPath);
              }
            });
            idOfLastDrawPath = currentPath.getId();
            currentPath = null;
          } else {
            realm.executeTransaction(new Transaction() {
              @Override
              public void execute(Realm realm) {
                currentPath.setCompleted(true);
                realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst()
                    .getDrawing()
                    .getPaths().add(currentPath);
              }
            });
            idOfLastDrawPath = currentPath.getId();
            currentPath = null;
          }
          return true;
        }
        return false;
      }
    });
    drawer.getParent().requestDisallowInterceptTouchEvent(true);


  }

  public String getCurrentSubjectTitle(Long currentSubjectId) {
    return realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getSubject();
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.d(tag, "...onStop()...");
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(tag, "...onStart()...");
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(tag, "...onResume()...");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(tag, "...onPause()...");
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
    Log.d(tag, "...onRestart()...");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (realm != null) {
      realm.close();
      realm = null;
      countDA--;
      SplashActivity.globalRealmInstancesCount--;
      Log.d(tag1,tagCloseDA);
      Log.d(tag1,tagCountDA + countDA);
      Log.d(tag1,tagGlobal + SplashActivity.globalRealmInstancesCount);
      Log.d(tag1,"---------DrawActivity OnDestroy()------------");
    }
    ratio = 0;
  }

  // if we are in the middle of a rotation, realm may be null.
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return false;
  }

  @Override
  public void surfaceCreated(SurfaceHolder surfaceHolder) {

    ////Todo: Tu Asynctaska dac startujacego wczytanie pierwszy raz rysunku

    /*if (drawThread == null) {
      drawThread = new DrawThread();
      drawThread.start();
    }*/
    new firstSketchDownloadAsyncTask().execute();
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
      //drawThread.shutdown();
      //drawThread = null;
    }
    ratio = -1;
  }

  class DrawThread extends Thread {
    private Realm bgRealm;

    public void shutdown() {
      synchronized (this) {
        if (bgRealm != null) {
          bgRealm.stopWaitForChange();
          bgRealm.close();
          countInThread--;
          SplashActivity.globalRealmInstancesCount--;
          Log.d(tag1,tagClose);
          Log.d(tag1,tagCount + countInThread);
          Log.d(tag1,tagGlobal + SplashActivity.globalRealmInstancesCount);
          Log.d(tag1,"---------thread shutdown()------------");
        }
      }
      //interrupt();
    }

    @Override
    public void run() {
      Log.d(tag1, "---------thread run()----------------------");

      //final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);


      /*SyncUser user = SyncUser.currentUser();
      Log.d(tag1, "---------thread run() SyncUser.currentUser()--------------------");
      final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user,
          REALM_URL).build();
      Realm.setDefaultConfiguration(syncConfiguration);*/

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
      //moja sdynchronizacja, żeby sie nie rozjechało
      /*synchronized (this){
      bgRealm = Realm.getDefaultInstance();
      countInThread++;
      SplashActivity.globalRealmInstancesCount++;
      Log.d(tag1, "-------------------------");
      Log.d(tag1, tagOpen);
      Log.d(tag1, tagCount + countInThread);
      Log.d(tag1, tagGlobal + SplashActivity.globalRealmInstancesCount);
      SplashActivity.globalRealmInstancesCount++;
    }*/
      //final RealmResults<DrawPathRealm> results = bgRealm.where(DrawPathRealm.class).findAll();
      //synchronized (this)
      bgRealm = Realm.getDefaultInstance();
      final RealmList<DrawPathRealm> results = bgRealm.where(SubjectRealm.class)
          .equalTo("id", currentSubjectId).
              findFirst().getDrawing().getPaths();
      //bgRealm.close();

      while (!isInterrupted()) {
        Log.d(tag, "28a while (!isInterrupted()");
        try {
          final SurfaceHolder holder = surfaceView.getHolder();
          canvas = holder.lockCanvas();

          synchronized (holder) {
            if (canvas != null) {
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
              if (canvas != null) {
                canvas.drawPath(path, paint);
              }
            }
            if (progressDialog != null && progressDialog.isShowing()) {
              progressDialog.dismiss();
              progressDialog = null;
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
        shutdown();
        //bgRealm.close();
        Log.d(tag, "30a bgRealm.close()");
      }
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
      //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
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
          Log.d(tag, "AsyncTask.onPostExecute() ");
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
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        DrawPathRealm lastDrawPath = realm.where(DrawPathRealm.class)
            .equalTo("id", idOfLastDrawPath).findFirst();
        if (lastDrawPath != null) {
          lastDrawPath.deleteFromRealm();
        }
      }
    });
    realm.close();
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

