package com.kampoz.sketchat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.kampoz.sketchat.R;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class SplashActivity extends AppCompatActivity {

  private static final String REALM_URL = "realm://" + "100.0.0.21" + ":9080/Draw999";
  private static final String AUTH_URL = "http://" + "100.0.0.21" + ":9080/auth";
  private static final String ID = "kampoz@kaseka.net";
  private static final String PASSWORD = "Murzyn1!";
  private volatile Realm realm;
  SharedPreferences preferences;
  Context context;
  private String tag = "cz SA";
  public static int globalRealmInstancesCount = 0;
  boolean isFirst = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    preferences = getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
    final SharedPreferences.Editor editor = preferences.edit();

    if(SyncUser.currentUser()!=null && SyncUser.currentUser().isValid()) {

      final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(),
          REALM_URL).build();

      //Realm.setDefaultConfiguration(Realm.getDefaultInstance().getConfiguration());
//      Realm.getInstance(syncConfiguration).close();

      Realm.setDefaultConfiguration(syncConfiguration);

      Intent startGroupsAndSubjectsActivity = new Intent(SplashActivity.this,
          GroupsAndSubjectsActivity.class);
      SplashActivity.this.startActivity(startGroupsAndSubjectsActivity);
      SplashActivity.this.finish();
      Log.d(tag, "Realm.getGlobalInstanceCount() z if "+String.valueOf(Realm.getGlobalInstanceCount(syncConfiguration)));

    } else {
      final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);
      SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
        @Override
        public void onSuccess(SyncUser user) {
          final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user,
                  REALM_URL).build();

          Log.d("SyncConfiguration",
                  "..1)getRealmFileName() " + syncConfiguration.getRealmFileName());
          Log.d("SyncConfiguration",
                  "..2)getRealmDirectory() " + syncConfiguration.getRealmDirectory().toString());
          Log.d("SyncConfiguration", "..3)getPath() " + syncConfiguration.getPath());
          Log.d("SyncConfiguration", "..4)getUser() " + syncConfiguration.getUser());
          Log.d("SyncConfiguration", "..5)getServerUrl() " + syncConfiguration.getServerUrl());
          Log.d("SyncConfiguration",
                  "..6)getRealmObjectClasses() " + syncConfiguration.getRealmObjectClasses());
          Log.d(tag, "Realm.getGlobalInstanceCount() z else "+String.valueOf(Realm.getGlobalInstanceCount(syncConfiguration)));

          if (realm == null) {
            Realm.removeDefaultConfiguration();
            Realm.setDefaultConfiguration(syncConfiguration);
            //realm = Realm.getDefaultInstance();
          } else {
            Realm.removeDefaultConfiguration();
            Realm.setDefaultConfiguration(syncConfiguration);
          }

          editor.putString("dbLocalPath", syncConfiguration.getRealmDirectory().toString());
          editor.apply();
          Log.d("SyncConfiguration", preferences.getString("dbLocalPath", "default value"));
          Log.d(tag, "Realm.getGlobalInstanceCount() "+String.valueOf(Realm.getGlobalInstanceCount(syncConfiguration)));

          Intent startGroupsAndSubjectsActivity = new Intent(SplashActivity.this, GroupsAndSubjectsActivity.class);
          SplashActivity.this.startActivity(startGroupsAndSubjectsActivity);
          SplashActivity.this.finish();
        }

        @Override
        public void onError(ObjectServerError error) {
          Toast.makeText(SplashActivity.this, "Connection error", Toast.LENGTH_LONG).show();

          Log.d("Connection error", "................1) Brak połaczenia");
          SyncUser user = SyncUser.currentUser();
          final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user,
                  REALM_URL).directory(SplashActivity.this.getFilesDir()).build();
          Realm.setDefaultConfiguration(syncConfiguration);
          Intent startGroupsAndSubjectsActivity = new Intent(SplashActivity.this, GroupsAndSubjectsActivity.class);
          SplashActivity.this.startActivity(startGroupsAndSubjectsActivity);
          SplashActivity.this.finish();
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
    Log.d(tag, "...onDestroy()...");
  }
}
