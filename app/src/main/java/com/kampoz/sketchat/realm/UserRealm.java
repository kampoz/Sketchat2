package com.kampoz.sketchat.realm;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by wasili on 2017-04-13.
 */

public class UserRealm extends RealmObject{

    private String name;

    @Ignore
    private int             sessionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}
