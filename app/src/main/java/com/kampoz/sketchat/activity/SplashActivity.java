package com.kampoz.sketchat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.kampoz.sketchat.R;
import com.kampoz.sketchat.dao.UserDao;
import com.kampoz.sketchat.realm.UserRealm;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class SplashActivity extends AppCompatActivity {

  private static final String REALM_URL = "realm://" + "100.0.0.234" + ":9080/Draw888";
  private static final String AUTH_URL = "http://" + "100.0.0.234" + ":9080/auth";
    private static final String ID = "kampoz@kaseka.net";
  private static final String PASSWORD = "Murzyn1!";
  private volatile Realm realm;
  SharedPreferences preferences;
  Context context;
  private String tag = "cz SA";
  private String tagGlobalInstances = " Realm global inst. SA";
  public static int globalRealmInstancesCount = 0;
  boolean isFirst = true;
  public static SyncConfiguration publicSyncConfiguration;
  public static RealmConfiguration publicRealmConfiguration;  /** local realm configuration for holding login data*/
  private Realm localRealm;
  private boolean isOfLine = true;
  private LinearLayout llInternetConnetion;
  private String SAThreadTag = "SA thread check";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    Log.d(tagGlobalInstances, "--------------- Start app --------------");

    /**
     * Configuration for local Realm
     * */
    publicRealmConfiguration = Realm.getDefaultInstance().getConfiguration();
    localRealm = Realm.getInstance(publicRealmConfiguration);

    llInternetConnetion = (LinearLayout)findViewById(R.id.llInternetConnection);

    preferences = getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
    //final SharedPreferences.Editor editor = preferences.edit();

    if(SyncUser.currentUser()!=null && SyncUser.currentUser().isValid()) {
      final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), REALM_URL).build();
      publicSyncConfiguration = syncConfiguration;
      Realm.setDefaultConfiguration(syncConfiguration);
      startGroupAndSubjectsActivity();
      //addUserSeedLocal("User 2");
      Log.d(tagGlobalInstances, "onCreate() <SyncUser exist> "+String.valueOf(Realm.getGlobalInstanceCount(syncConfiguration)));

    } else {
      final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);
      SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
        @Override
        public void onSuccess(SyncUser user) {
          final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL).build();

          Log.d("SplashActivity", "   onSucces");
          /*Log.d("SyncConfiguration",
                  "..1)getRealmFileName() " + syncConfiguration.getRealmFileName());
          Log.d("SyncConfiguration",
                  "..2)getRealmDirectory() " + syncConfiguration.getRealmDirectory().toString());
          Log.d("SyncConfiguration", "..3)getPath() " + syncConfiguration.getPath());
          Log.d("SyncConfiguration", "..4)getUser() " + syncConfiguration.getUser());
          Log.d("SyncConfiguration", "..5)getServerUrl() " + syncConfiguration.getServerUrl());
          Log.d("SyncConfiguration",
                  "..6)getRealmObjectClasses() " + syncConfiguration.getRealmObjectClasses());*/
          //Log.d(tagGlobalInstances, "onCreate() z else <SyncUser don't exist or isnt valid> "+String.valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));

          /*if (realm == null) {
            Realm.removeDefaultConfiguration();
            Realm.setDefaultConfiguration(syncConfiguration);
            //realm = Realm.getDefaultInstance();
          } else {
            Realm.removeDefaultConfiguration();
            Realm.setDefaultConfiguration(syncConfiguration);
          }*/

          //editor.putString("dbLocalPath", syncConfiguration.getRealmDirectory().toString());
          //editor.apply();
          //Log.d("SyncConfiguration", preferences.getString("dbLocalPath", "default value"));
          //Log.d(tagGlobalInstances, "onCreate() z else "+String.valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));

          //addUserSeed();


          startGroupAndSubjectsActivity();
        }

        @Override
        public void onError(ObjectServerError error) {


          Log.d("Connection error", "................1) Brak połaczenia");
          SyncUser user = SyncUser.currentUser();

          CheckingInternetConnectionThead checkThread = new CheckingInternetConnectionThead();
          checkThread.start();

          if(user == null){
            Toast.makeText(SplashActivity.this, "Connection error. Check internet connection", Toast.LENGTH_LONG).show();
          }
          final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL).directory(SplashActivity.this.getFilesDir()).build();
          Realm.setDefaultConfiguration(syncConfiguration);



          startGroupAndSubjectsActivity();
        }
      });
    }
  }

  /** Method checking internet connection ***/
  public boolean isOnline() {
    ConnectivityManager cm =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return netInfo != null && netInfo.isConnectedOrConnecting();
  }

  /**
   * Checking if userRealm exist in local realm database.
   * In argument local there is a local Realm with RealmConfiguration instead od SyncConfiguration
   * **/

  private boolean userIsLogin(Realm realm){
    UserRealm userRealm = realm.where(UserRealm.class).findFirst();
    if(userRealm!=null){
      return true;
    }else{
      return false;
    }
  }

  private void startGroupAndSubjectsActivity(){
    Intent startGroupsAndSubjectsActivity = new Intent(SplashActivity.this, GroupsAndSubjectsActivity.class);
    SplashActivity.this.startActivity(startGroupsAndSubjectsActivity);
    SplashActivity.this.finish();
  }

  private void startLoginAndRegisterActivity(){
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
      Log.d(tagGlobalInstances, "onDestroy(); Realm.getGlobalInstanceCount() z else "+String.valueOf(Realm.getGlobalInstanceCount(SplashActivity.publicSyncConfiguration)));
    }
    if(localRealm != null){
      localRealm.close();
      localRealm = null;
    }
    Log.d(tag, "...onDestroy()...");


  }

  public void addUserSeed(){
    String userName = "Testowy";
    UserDao userDao = new UserDao();
    userDao.addNewUser(userName);
    UserRealm user = userDao.getUserByName(userName);
    /** Dodanie usera do bazy lokalnej*/
    userDao.saveLoginUserLocally(user, publicRealmConfiguration);
    userDao.closeRealmInstance();
  }

  public  void addUserSeedLocal(String userName){
    UserDao userDao = new UserDao();
    userDao.saveLoginUserLocally(userName, publicRealmConfiguration);
    userDao.closeRealmInstance();
  }

  class CheckingInternetConnectionThead extends Thread{
    public void run(){
      while(isOfLine){
        Log.d(SAThreadTag, ".....thread is checking connection....");
        if(isOnline()){
          Log.d(SAThreadTag, ">>> thread detected connection <<<");
          isOfLine = false;
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              findViewById(R.id.llInternetConnection).setVisibility(View.VISIBLE);
            }
          });
        }else{

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
