package com.kampoz.sketchat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kampoz.sketchat.R;
import com.kampoz.sketchat.realm.UserRealm;

import io.realm.Realm;

public class RegisterActivity extends AppCompatActivity {

    private static final String REALM_URL = "realm://" + "100.0.0.21" + ":9080/~/Test";
    private static final String AUTH_URL = "http://" + "100.0.0.21" + ":9080/auth";
    private static final String ID = "kampoz@kaseka.net";
    private static final String PASSWORD = "Murzyn1!";

    private Button bOKinRegistration;
    private EditText etUsernameInRegistration;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsernameInRegistration = (EditText)findViewById(R.id.etUsernameInRegistration);
        bOKinRegistration = (Button) findViewById(R.id.bOKinRegistration);

//        final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);
//        SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
//            @Override
//            public void onSuccess(SyncUser user) {
//                final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL).build();
//                Log.d("SyncConfiguration", syncConfiguration.getPath());
//                Realm.setDefaultConfiguration(syncConfiguration);
//                realm = Realm.getDefaultInstance();
//
//                Stetho.initialize(
//                        Stetho.newInitializerBuilder(RegisterActivity.this)
//                                .enableDumpapp(Stetho.defaultDumperPluginsProvider(RegisterActivity.this))
//                                .enableWebKitInspector(RealmInspectorModulesProvider.builder(RegisterActivity.this).withFolder(syncConfiguration.getRealmDirectory()).databaseNamePattern(Pattern.compile(".+\\.realm")).build())
//                                .build());
//            }
//
//            @Override
//            public void onError(ObjectServerError error) {
//                Log.d("Error", "To wina wasyla");
//            }
//        });

        bOKinRegistration.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final String userName = etUsernameInRegistration.getText().toString();
                if(TextUtils.isEmpty(userName)) {
                    etUsernameInRegistration.setError("Username cannot be empty");
                }
                else{

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            UserRealm userRealm = realm.createObject(UserRealm.class, userName);

                            //realm.copyToRealmOrUpdate(userRealm);
                        }
                    });

                    Log.d("Users count", Integer.toString( Realm.getDefaultInstance().where(UserRealm.class).findAll().size()));
                    Log.d("Path",  Realm.getDefaultInstance().getPath());
                    Log.d("Conf filename",  Realm.getDefaultInstance().getConfiguration().getRealmFileName());
                    //intent
                }
            }
        });
    }


}
