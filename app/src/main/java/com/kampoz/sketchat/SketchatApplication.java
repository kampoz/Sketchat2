package com.kampoz.sketchat;

import android.app.Application;
import io.realm.Realm;
/**
 * Created by wasili on 2017-04-13.
 */

public class SketchatApplication extends Application

    {

        @Override
        public void onCreate() {
        super.onCreate();
        Realm.init(this);
//        RealmLog.setLevel(Log.VERBOSE);
    }

}
