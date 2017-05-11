package com.kampoz.sketchat.activity;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.button.ColorButton;
import com.kampoz.sketchat.button.ColorButton.PaintColorListener;
import com.kampoz.sketchat.model.DrawPath;
import com.kampoz.sketchat.model.DrawPoint;
import com.kampoz.sketchat.model.PencilView;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import java.util.HashMap;
import java.util.Iterator;

public class DrawActivity extends AppCompatActivity implements SurfaceHolder.Callback,
     PaintColorListener {
    private static final String REALM_URL = "realm://" + "100.0.0.21" + ":9080/Draw3";
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
    private DrawPath currentPath;
    //private String currentColor = "Charcoal";
    private int currentColor;
    private PencilView currentPencil;
    private HashMap<String, Integer> nameToColorMap = new HashMap<>();
    private HashMap<Integer, String> colorIdToName = new HashMap<>();
    private Button bWipeCanvas;
    private ColorButton ibColor1;
    private ColorButton ibColor2;
    private ColorButton ibColor3;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        currentColor = 0x000000;
        preferences = getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);
        SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL).directory(DrawActivity.this.getFilesDir()).build();
                Log.d("SyncConfiguration", "..1)getRealmFileName() "+syncConfiguration.getRealmFileName());
                Log.d("SyncConfiguration", "..2)getRealmDirectory() "+syncConfiguration.getRealmDirectory().toString());
                Log.d("SyncConfiguration", "..3)getPath() "+syncConfiguration.getPath());
                Log.d("SyncConfiguration", "..4)getUser() "+syncConfiguration.getUser());
                Log.d("SyncConfiguration", "..5)getServerUrl() "+syncConfiguration.getServerUrl());
                Log.d("SyncConfiguration", "..6)getRealmObjectClasses() "+syncConfiguration.getRealmObjectClasses());

                Realm.setDefaultConfiguration(syncConfiguration);
                realm = Realm.getDefaultInstance();
                editor.putString("dbLocalPath",syncConfiguration.getRealmDirectory().toString());
                editor.apply();
                Log.d("SyncConfiguration", preferences.getString("dbLocalPath","default value"));
            }

            @Override
            public void onError(ObjectServerError error) {
//                File file = new File(preferences.getString("dbLocalPath","default value"));
//                RealmConfiguration conf = new RealmConfiguration.Builder().directory(file).name("Draw").build();
                //final SyncConfiguration conf = new SyncConfiguration.Builder(null, REALM_URL).build();
//                Realm.setDefaultConfiguration(conf);
//                realm = Realm.getDefaultInstance();
//                Log.d("RealmConfiguration", "...1) Brak połaczenia");
//                Log.d("RealmConfiguration", "...2) RealmConfiguration.getPath(): "+conf.getPath());
                Toast.makeText(DrawActivity.this, "No connection", Toast.LENGTH_LONG).show();
                Log.d("Connection error", "...1) Brak połaczenia");
                Log.d("Connection error", "...1) Brak połaczenia");
            }
        });

        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(DrawActivity.this);
        bWipeCanvas = (Button)findViewById(R.id.bWipeCanvas);

        ibColor1 = (ColorButton)findViewById(R.id.bColor1);
        ibColor2 = (ColorButton)findViewById(R.id.bColor2);
        ibColor3 = (ColorButton)findViewById(R.id.bColor3);
        ibColor1.setUpColor(R.color.colorBlack);
        ibColor2.setUpColor(R.color.colorMyRedDark);
        ibColor3.setUpColor(R.color.colorBallYellowDark);
//        ibColor1.setListener(this);
//        ibColor2.setListener(this);
//        ibColor3.setListener(this);

        bindButtons();
        //initializeShakeSensor();

        bWipeCanvas.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                wipeCanvas();
            }
        });

//        Drawable background1 = ibColor1.getBackground();
//        GradientDrawable gradientDrawable = (GradientDrawable) background1;
//        gradientDrawable.setColor(ContextCompat.getColor(this,R.color.colorBlack));
//
//        Drawable background2 = ibColor2.getBackground();
//        GradientDrawable gradientDrawable2 = (GradientDrawable) background2;
//        gradientDrawable2.setColor(ContextCompat.getColor(this,R.color.colorMyRedDark));
//
//        Drawable background3 = ibColor3.getBackground();
//        GradientDrawable gradientDrawable3 = (GradientDrawable) background3;
//        gradientDrawable3.setColor(ContextCompat.getColor(this,R.color.colorBallYellowDark));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //sensorManager.registerListener(shakeSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(shakeSensorEventListener);
    }

//    private void initializeShakeSensor() {
//
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        shakeSensorEventListener = new io.realm.draw.sensor.ShakeSensorEventListener();
//        shakeSensorEventListener.setOnShakeListener(new io.realm.draw.sensor.ShakeSensorEventListener.OnShakeListener() {
//
//            @Override
//            public void onShake(int count) {
//                wipeCanvas();
//            }
//        });
//    }

    private void bindButtons() {
        int[] buttonIds = {
            R.id.bColor1,
            R.id.bColor2,
            R.id.bColor3
        };

        for (int id : buttonIds) {
            ColorButton colorButton = (ColorButton) findViewById(id);
            colorButton.setListener(this);
        }

        //currentPencil = (PencilView) findViewById(R.id.charcoal);
        //currentPencil.setSelected(true);
    }

//    private void generateColorMap() {
//        nameToColorMap.put("Charcoal", 0xff1c283f);
//        nameToColorMap.put("Elephant", 0xff9a9ba5);
//        nameToColorMap.put("Dove", 0xffebebf2);
//        nameToColorMap.put("Ultramarine", 0xff39477f);
//        nameToColorMap.put("Indigo", 0xff59569e);
//        nameToColorMap.put("GrapeJelly", 0xff9a50a5);
//        nameToColorMap.put("Mulberry", 0xffd34ca3);
//        nameToColorMap.put("Flamingo", 0xfffe5192);
//        nameToColorMap.put("SexySalmon", 0xfff77c88);
//        nameToColorMap.put("Peach", 0xfffc9f95);
//        nameToColorMap.put("Melon", 0xfffcc397);
//        colorIdToName.put(R.id.bColor1, "Charcoal");
//        colorIdToName.put(R.id.bColor2,"Peach");
//        colorIdToName.put(R.id.bColor3, "Melon");
//
//    }

    private void wipeCanvas() {
        if(realm != null) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm r) {
                    r.deleteAll();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(realm == null) {
            return false; // if we are in the middle of a rotation, realm may be null.
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
                currentPath = realm.createObject(DrawPath.class);
                currentPath.setColor(currentColor);
                DrawPoint point = realm.createObject(DrawPoint.class);
                point.setX(pointX);
                point.setY(pointY);
                currentPath.getPoints().add(point);
                realm.commitTransaction();
            } else if (action == MotionEvent.ACTION_MOVE) {
                realm.beginTransaction();
                DrawPoint point = realm.createObject(DrawPoint.class);
                point.setX(pointX);
                point.setY(pointY);
                currentPath.getPoints().add(point);
                realm.commitTransaction();
            } else if (action == MotionEvent.ACTION_UP) {
                realm.beginTransaction();
                currentPath.setCompleted(true);
                DrawPoint point = realm.createObject(DrawPoint.class);
                point.setX(pointX);
                point.setY(pointY);
                currentPath.getPoints().add(point);
                realm.commitTransaction();
                currentPath = null;
            } else {
                realm.beginTransaction();
                currentPath.setCompleted(true);
                realm.commitTransaction();
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

//    @Override
//    public void onClick(View view) {
//        String colorName = colorIdToName.get(view.getId());
//        if (colorName == null) {
//            return;
//        }
//        //currentColor = colorName;
//        if (view instanceof PencilView) {
//            currentPencil.setSelected(false);
//            currentPencil.invalidate();
//            PencilView pencil = (PencilView) view;
//            pencil.setSelected(true);
//            pencil.invalidate();
//            currentPencil = pencil;
//        }
//    }

    @Override
    public void onClick(int color) {
        String strColor = String.format("#%06X", 0xFFFFFF & color);
        Log.d("onClick", strColor);
        currentColor = color;
    }

    class DrawThread extends Thread {
        private Realm bgRealm;

        public void shutdown() {
            synchronized(this) {
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

            final RealmResults<DrawPath> results = bgRealm.where(DrawPath.class).findAll();

            while (!isInterrupted()) {
                try {
                    final SurfaceHolder holder = surfaceView.getHolder();
                    canvas = holder.lockCanvas();

                    synchronized (holder) {
                        canvas.drawColor(Color.WHITE);
                        final Paint paint = new Paint();
                        for (DrawPath drawPath : results) {
                            final RealmList<DrawPoint> points = drawPath.getPoints();
                            final Integer color = drawPath.getColor();//nameToColorMap.get(drawPath.getColor());
                            if (color != null) {
                                paint.setColor(color);
                            } else {
                                paint.setColor(currentColor);
                            }
                            paint.setStyle(Paint.Style.STROKE);
                            paint.setStrokeWidth((float) (4 / ratio));
                            final Iterator<DrawPoint> iterator = points.iterator();
                            final DrawPoint firstPoint = iterator.next();
                            final Path path = new Path();
                            final float firstX = (float) ((firstPoint.getX() / ratio) + marginLeft);
                            final float firstY = (float) ((firstPoint.getY() / ratio) + marginTop);
                            path.moveTo(firstX, firstY);
                            while(iterator.hasNext()) {
                                DrawPoint point = iterator.next();
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

            synchronized(this) {
                bgRealm.close();
            }
        }
    }
}

