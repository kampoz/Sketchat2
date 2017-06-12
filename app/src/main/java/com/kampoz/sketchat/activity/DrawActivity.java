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
import io.realm.RealmConfiguration;
import io.realm.RealmList;

import java.util.HashMap;
import java.util.Iterator;

import android.view.MenuItem;

public class DrawActivity extends AppCompatActivity
    implements SurfaceHolder.Callback, PaletteFragment.PaletteCallback,
    ColorPickerDialogFragment.ColorListener {

  private static final String REALM_URL = "realm2://" + "100.0.0.21" + ":9080/Draw999";
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
  private MyDrawThread myDrawThread;
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
  private String tag1 = "realm2 instance th";
  private String tagOpen = "+ in Thread open";
  private String tagClose = "- in Thread close";
  private String tagCount = "= Realm instances opened in Thread: ";
  private String tagGlobal = "== globalRealmInstancesCount: ";

  private int countDA = 0;
  private String tag2 = "realm2 instance DA";
  private String tagOpenDA = "+ in DrawActivity open";
  private String tagCloseDA = "- in DrawActivity close";
  private String tagCountDA = "= Realm instances opened in DrawActivity: ";
  private String tagBgRealm = "DA bgRealm instance";
  private String tagGlobalInstances = "Realm global inst. DA";
  private String tagMyThread = "DA myThread";
  private static String tagRealmThread = "DA inst realm2";
  private  boolean realmCloseFlag = false;
  private RealmThread realmThread;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_draw);
    Intent intent = getIntent();

    realm = Realm.getDefaultInstance();
    countDA++;
    SplashActivity.globalRealmInstancesCount++;
    Log.d(tag2,"---------DrawActivity OnCreate()------------");

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
    Log.d(tagGlobalInstances,"onCreate()  Realm.getGlobalInstanceCount: "+String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));


    realmThread = new RealmThread(SplashActivity.publicSyncConfiguration, new RealmThread.RealmRunnable() {
      @Override
      public void run(Realm realm) {
        final RealmList<DrawPathRealm> results = realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getDrawing().getPaths();
        do {
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

          Log.d("RealmThread", " wątek RealmThread działa ...");

        } while (realm.waitForChange());
      }
    });

  }

  public String getCurrentSubjectTitle(Long currentSubjectId) {
    return realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getSubject();
  }

  @Override
  protected void onStop() {
    super.onStop();
    realmThread.shutdown();
    Log.d(tag, "...onStop()...");
    Log.d(tagGlobalInstances,"onStop()  Realm.getGlobalInstanceCount: "+String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));

  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(tag, "...onStart()...");
    Log.d(tagGlobalInstances,"onStart() Realm.getGlobalInstanceCount: "+String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(tag, "...onResume()...");
    Log.d(tagGlobalInstances,"onResume() Realm.getGlobalInstanceCount: "+String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
  }

  @Override
  protected void onPause() {
    super.onPause();
    realmCloseFlag = true;
    Log.d(tag, "...onPause()...");
    Log.d(tagGlobalInstances,"onPause() Realm.getGlobalInstanceCount: "+String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
    Log.d(tagGlobalInstances,"onBackPressed() Realm.getGlobalInstanceCount: "+String.
            valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
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
      realmCloseFlag = true;
      Log.d(tag1,"---------DrawActivity OnDestroy()------------");
      Log.d(tagGlobalInstances,"onDestroy() Realm.getGlobalInstanceCount: "+String.
              valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));

      //realmThread.interrupt();
      //realmThread.shutdown();
    }
    ratio = 0;
  }

  // if we are in the middle of a rotation, realm2 may be null.
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
      drawThread.shutdown();
      drawThread = null;
    }
    ratio = -1;
  }


  public static final class RealmThread extends Thread {

    // Runnable interface
    public interface RealmRunnable {
      void run(final Realm realm);
    }

    private final RealmConfiguration realmConfig;
    private final RealmRunnable task;
    private Realm realm2;

    public RealmThread(RealmConfiguration realmConfig, RealmRunnable task) {
      super();
      this.realmConfig = realmConfig;
      this.task = task;
    }

    public RealmThread(RealmConfiguration realmConfig, RealmRunnable task, String threadName) {
      super(threadName);
      this.realmConfig = realmConfig;
      this.task = task;
    }

    /*@Override
    public void run() {
      realm2 = Realm.getInstance(realmConfig);
      Log.d(tagRealmThread, " >>> >>> >>> realm2 NEW INSTANCE OPEN >>> >>> >>>");
      if (task != null) {
        task.run(realm2);
      }
      synchronized(this) {
        if (!realm2.isClosed()) {
          realm2.close();
          Log.d(tagRealmThread, " >>> >>> >>> realm2 INSTANCE CLOSE ////////////");
        }
        realm2 = null;
      }
    }*/

    @Override
    public void run() {
      try{
      realm2 = Realm.getInstance(realmConfig);
      Log.d(tagRealmThread, " >>> >>> >>> realm2 NEW INSTANCE OPEN >>> INSTANCE OPEN >>> INSTANCE OPEN >>>>>>>>>>>>>>> INSTANCE OPEN ");
      if (task != null) {
        task.run(realm2);
      }} finally {
      synchronized(this) {
        if (!realm2.isClosed()) {
          realm2.close();
          Log.d(tagRealmThread, " >>> >>> >>> realm2 INSTANCE CLOSE INSTANCE CLOSE INSTANCE CLOSE INSTANCE CLOSE INSTANCE CLOSE <<<<<<<<<<");
        }
        realm2 = null;
      }}
    }

    /**
     * Abort the Realm thread as soon as possibly.
     */
    public void shutdown() {
      synchronized (this) {
        Log.d("RealmThread", " shutdown() ");
        if (!isAlive() || realm2 == null) {
          return;
        }
        Log.d("RealmThread", "stopWaitForChange()");
        realm2.stopWaitForChange();
      }
    }

  }


/**Mój ropoczety wątek*/
  class MyDrawThread extends Thread{
    private Realm realm;

    @Override
    public void run() {
      realm = Realm.getDefaultInstance();
      Log.d(tagMyThread, "realm NEW INSTANCE OPEN >>> >>> >>>");
      final RealmList<DrawPathRealm> results = realm.where(SubjectRealm.class).equalTo("id", currentSubjectId).findFirst().getDrawing().getPaths();
      canvas = null;
      try {
        final SurfaceHolder holder = surfaceView.getHolder();
        canvas = holder.lockCanvas();
        canvas.drawColor(Color.YELLOW);
      } finally {
        if (canvas != null) {
          surfaceView.getHolder().unlockCanvasAndPost(canvas);
        }
      }


      while(!isInterrupted()){
        final SurfaceHolder holder = surfaceView.getHolder();
        canvas = holder.lockCanvas();
        if (canvas != null) {
          canvas.drawColor(Color.WHITE);
        }

      }
    }
  }

  class DrawThread extends Thread {
    private Realm bgRealm;

    public void shutdown() {
      synchronized (this) {
        if (bgRealm != null) {
          bgRealm.stopWaitForChange();
          Log.d(tag1,"Realm.getGlobalInstanceCount(): "+String.
                  valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
        }
      }
      interrupt();
    }

    @Override
    public void run() {
      Log.d(tag1, "---------thread run()----------------------");
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

      if (isInterrupted()) {
        return;
      }
      //final RealmResults<DrawPathRealm> results = bgRealm.where(DrawPathRealm.class).findAll();
      //synchronized (this)
      Realm bgRealm = Realm.getDefaultInstance();
      Log.d(tagBgRealm, "bgRealm NEW INSTANCE OPEN >>> >>> >>>");

      final RealmList<DrawPathRealm> results = bgRealm.where(SubjectRealm.class)
          .equalTo("id", currentSubjectId).
              findFirst().getDrawing().getPaths();

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
        if(realmCloseFlag) {
          bgRealm.stopWaitForChange();
          if(!bgRealm.isClosed())bgRealm.close();
        }
        else bgRealm.waitForChange();
        Log.d(tagBgRealm, " bgRealm.waitForChange() ");
      }
      if(interrupted() && realmCloseFlag) {
        if(!bgRealm.isClosed())bgRealm.close();
        Log.d(tagBgRealm, "drawThread is interrupted() & bgRealm INSTANCE CLOSED <<<");
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
        //if (realmThread.isAlive()) {
          //drawThread = new DrawThread();
          //myDrawThread = new MyDrawThread();
          //drawThread.start();
          //myDrawThread.start();
//          }else{
//          realmThread.start();
//          }

        if (realmThread.getState() == Thread.State.NEW)
        {
          realmThread.start();
        } else{
          Intent startDrawActivityIntent = new Intent(DrawActivity.this, DrawActivity.class);
          startDrawActivityIntent.putExtra("currentSubjectid", currentSubjectId);
          startActivity(startDrawActivityIntent);
          DrawActivity.this.finish();
        }
          Log.d(tag, "AsyncTask.onPostExecute() ");
        }
    }


    @Override
    protected Void doInBackground(Void... arg0) {
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

