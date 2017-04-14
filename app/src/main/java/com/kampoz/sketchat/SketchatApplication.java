package com.kampoz.sketchat;

import android.app.Application;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.kampoz.sketchat.activity.RegisterActivity;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

/**
 * Created by wasili on 2017-04-13.
 */

public class SketchatApplication extends Application {

    private static final String REALM_URL = "realm://" + "100.0.0.21" + ":9080/Test";
    private static final String AUTH_URL = "http://" + "100.0.0.21" + ":9080/auth";
    private static final String ID = "kampoz@kaseka.net";
    private static final String PASSWORD = "Murzyn1!";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)
                                .withFolder(new File("/data/data/com.kampoz.sketchat/files/df91fa1834600814a5f76708cacf98df/df91fa1834600814a5f76708cacf98df"))
                                .databaseNamePattern(Pattern.compile(".+\\.realm"))
                                .build())
                        .build());

        final File file =  new File(Realm.getDefaultInstance().getPath()).getParentFile();

        Log.d("SketchatApplication", file.toString());
//        RealmLog.setLevel(Log.VERBOSE);

            final SyncCredentials syncCredentials = SyncCredentials.usernamePassword(ID, PASSWORD, false);
            SyncUser.loginAsync(syncCredentials, AUTH_URL, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(user, REALM_URL)
                                .name("default.realm")
                                .directory(file).build();
                    Realm.setDefaultConfiguration(syncConfiguration);

                    try {
                        Log.d("SyncConfiguration", syncConfiguration.getRealmDirectory().getCanonicalPath());
                        Log.d("SyncConfiguration", syncConfiguration.getRealmDirectory().getAbsolutePath());
                        Log.d("SyncConfiguration", syncConfiguration.getRealmDirectory().getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onError(ObjectServerError error) {
                    Log.d("Error", "To wina wasyla");
                }
            });

    }

}
