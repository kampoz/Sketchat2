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
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class SplashActivity extends AppCompatActivity {

  private static final String REALM_URL = "realm://" + "100.0.0.21" + ":9080/Draw13";
  private static final String AUTH_URL = "http://" + "100.0.0.21" + ":9080/auth";
  private static final String ID = "kampoz@kaseka.net";
  private static final String PASSWORD = "Murzyn1!";
  private volatile Realm realm;
  SharedPreferences preferences;
  Context context;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    preferences = getSharedPreferences("com.kampoz.sketchat", MODE_PRIVATE);
    final SharedPreferences.Editor editor = preferences.edit();

    final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);
    SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
      @Override
      public void onSuccess(SyncUser user) {
        final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user,
            REALM_URL).directory(SplashActivity.this.getFilesDir()).build();

        Log.d("SyncConfiguration",
            "..1)getRealmFileName() " + syncConfiguration.getRealmFileName());
        Log.d("SyncConfiguration",
            "..2)getRealmDirectory() " + syncConfiguration.getRealmDirectory().toString());
        Log.d("SyncConfiguration", "..3)getPath() " + syncConfiguration.getPath());
        Log.d("SyncConfiguration", "..4)getUser() " + syncConfiguration.getUser());
        Log.d("SyncConfiguration", "..5)getServerUrl() " + syncConfiguration.getServerUrl());
        Log.d("SyncConfiguration",
            "..6)getRealmObjectClasses() " + syncConfiguration.getRealmObjectClasses());

        Realm.setDefaultConfiguration(syncConfiguration);
        realm = Realm.getDefaultInstance();
        editor.putString("dbLocalPath", syncConfiguration.getRealmDirectory().toString());
        editor.apply();
        Log.d("SyncConfiguration", preferences.getString("dbLocalPath", "default value"));
        Log.d("Cykl życia DA", "...onCreate()...");

        Intent startGroupsAndSubjectsActivity = new Intent(SplashActivity.this, GroupsAndSubjectsActivity.class);
        SplashActivity.this.startActivity(startGroupsAndSubjectsActivity);
        SplashActivity.this.finish();
      }

      @Override
      public void onError(ObjectServerError error) {
        Toast.makeText(SplashActivity.this, "Connection error", Toast.LENGTH_LONG).show();
        Log.d("Connection error", "...1) Brak połaczenia");

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
