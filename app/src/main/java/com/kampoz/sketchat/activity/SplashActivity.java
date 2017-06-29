package com.kampoz.sketchat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.dao.UserDao;
import com.kampoz.sketchat.helper.MyConnectionChecker;
import com.kampoz.sketchat.realm.UserRealm;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class SplashActivity extends AppCompatActivity {

  private static final String REALM_URL = "realm://" + "192.168.0.111" + ":9080/Draw555";
  private static final String AUTH_URL = "http://" + "192.168.0.111" + ":9080/auth";
  private static final String ID = "kampoz@kaseka.net";
  private static final String PASSWORD = "Murzyn1!";
  private volatile Realm realm;
  SharedPreferences preferences;
  private Context context;
  private String tag = "cz SA";
  private String tagGlobalInstances = " Realm global inst. SA";
  public static int globalRealmInstancesCount = 0;
  boolean isFirst = true;
  public static SyncConfiguration publicSyncConfiguration;
  public static RealmConfiguration publicRealmConfiguration;
  /**
   * local realm configuration for holding login data
   */
  private Realm localRealm;
  private boolean isOfLine = true;
  private boolean checkingSyncUser = false;
  private LinearLayout llInternetConnection;
  private String SAThreadTag = "SA thread check";
  private Button bConnect;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);



    /** Configuration for local Realm */
    publicRealmConfiguration = new RealmConfiguration.Builder().build();
    //cleanLocalRealm();

    llInternetConnection = (LinearLayout) findViewById(R.id.llInternetConnection);
    preferences = getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
    bConnect = (Button) findViewById(R.id.bConnect);
    bConnect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        connectionMethod();
        checkingSyncUser = true;
        CheckingIsSyncUserNotNullThread checkingSyncUserThread = new CheckingIsSyncUserNotNullThread();
        checkingSyncUserThread.start();
        /*SyncUser syncUser = SyncUser.currentUser();
        if (syncUser == null) {
          Toast.makeText(SplashActivity.this, "SyncUser error. Server offline", Toast.LENGTH_LONG)
              .show();
        }*/
      }
    });
    connectionMethod();
    //addUserSeedLocal("Zenek");
  }

  /**
   * Method connectionMethod() checks if:
   * 1) (if) Is there an active SyncUser
   * 2) (else) If syncUser is not valid creates a new SyncUser and:
   *  a) (onSuccess) If device hes connection to Realm database creates SyncConfiguration and connect to data base
   *  b) (onError) If device has no connection to data base runs a CheckingInternetConnectionThead instance
   */

  private void connectionMethod() {
    if (SyncUser.currentUser() != null && SyncUser.currentUser().isValid()) {
      final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(
          SyncUser.currentUser(), REALM_URL).build();
      publicSyncConfiguration = syncConfiguration;
      checkingSyncUser = false;
      Realm.setDefaultConfiguration(syncConfiguration);
      if (isUserLogin()) {
        startGroupAndSubjectsActivity();
      } else {
        startLoginAndRegisterActivity();
      }
      Log.d(tagGlobalInstances, "onCreate() <SyncUser exist> " + String
          .valueOf(Realm.getGlobalInstanceCount(syncConfiguration)));

    } else {
      final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);
      SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
        @Override
        public void onSuccess(SyncUser user) {
          final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL)
              .build();
          publicSyncConfiguration = syncConfiguration;
          checkingSyncUser = false;
          if (isUserLogin()) {
            startGroupAndSubjectsActivity();
          } else {
            startLoginAndRegisterActivity();
          }
        }

        @Override
        public void onError(ObjectServerError error) {
          llInternetConnection.setVisibility(View.VISIBLE);
          CheckingInternetConnectionThead checkThread = new CheckingInternetConnectionThead();
          checkThread.start();
        }
      });
    }
  }

  /**
   * Method checking internet connection
   ***/
  public boolean isOnline() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }

  /**
   * Checking if userRealm exist in local realm database.
   **/

  private boolean isUserLogin() {
    UserDao userDao = new UserDao(publicRealmConfiguration);
    boolean isLogin = userDao.isUserLogin();
    userDao.closeRealmInstance();
    return isLogin;
  }

  private void cleanLocalRealm() {
    UserDao userDao = new UserDao(publicRealmConfiguration);
    userDao.cleanLocalRealm();
    userDao.closeRealmInstance();
  }

  private void startGroupAndSubjectsActivity() {
    Intent startGroupsAndSubjectsActivity = new Intent(SplashActivity.this,
        GroupsAndSubjectsActivity.class);
    SplashActivity.this.startActivity(startGroupsAndSubjectsActivity);
    SplashActivity.this.finish();
  }

  private void startLoginAndRegisterActivity() {
    Intent startIntent = new Intent(SplashActivity.this, LoginAndRegisterActivity.class);
    SplashActivity.this.startActivity(startIntent);
    SplashActivity.this.finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (realm != null) {
      realm.close();
      realm = null;
      Log.d(tagGlobalInstances, "onDestroy(); Realm.getGlobalInstanceCount() z else " + String
          .valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
    }
    if (localRealm != null) {
      localRealm.close();
      localRealm = null;
    }
    Log.d(tag, "...onDestroy()...");
  }

  public void addUserSeed() {
    String userName = "Testowy";
    UserDao userDao = new UserDao();
    userDao.registerNewUser(userName);
    UserRealm user = userDao.getUserByName(userName);
    /** Dodanie usera do bazy lokalnej*/
    userDao.saveLoginUserLocally(user, publicRealmConfiguration);
    userDao.closeRealmInstance();
  }

  public void addUserSeedLocal(String userName) {
    UserDao userDao = new UserDao();
    userDao.saveLoginUserLocally(userName, publicRealmConfiguration);
    userDao.closeRealmInstance();
  }


  /**
   * Thread cheking if device has internet connection
   */
  class CheckingInternetConnectionThead extends Thread {

    MyConnectionChecker myConnectionChecker = new MyConnectionChecker();

    public void run() {
      if (!myConnectionChecker.isOnline(SplashActivity.this)) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Log.d("Connection error", "....NO INTERNET....");
            Toast.makeText(SplashActivity.this, "Check internet connection", Toast.LENGTH_LONG)
                .show();
          }
        });
      }
      while (isOfLine) {
        Log.d(SAThreadTag, ".....thread is checking connection....");
        if (myConnectionChecker.isOnline(SplashActivity.this)) {
          Log.d(SAThreadTag, ">>> Thread detect INTERNET CONNECTION <<<");
          isOfLine = false;
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              findViewById(R.id.bConnect).setVisibility(View.VISIBLE);
              findViewById(R.id.tvConnectionInfo).setVisibility(View.INVISIBLE);
            }
          });
        } else {

        }
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    }
  }

  /**
   * Thread cheking if server is online and there is a possibility to create SyncUser for the first
   * time. This is nessesery only when app is started for the first time and SyncUser must be
   * created.
   **/
  class CheckingIsSyncUserNotNullThread extends Thread {

    public void run() {
      while (checkingSyncUser) {
        SyncUser user = SyncUser.currentUser();
        if (user != null) {
          Toast.makeText(SplashActivity.this, "Server online. You can connect now", Toast.LENGTH_LONG).show();
        } else {
          checkingSyncUser = false;
          //startGroupAndSubjectsActivity();
        }
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
