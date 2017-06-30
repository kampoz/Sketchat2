package com.kampoz.sketchat.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-04-13.
 */

public class UserRealm extends RealmObject{

    @PrimaryKey
    private long id;
    private String name;
    private String password;
    private RealmList<GroupRealm> usersGroups;

    @Ignore
    private int sessionId;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RealmList<GroupRealm> getUsersGroups() {
        return usersGroups;
    }

    public void setUsersGroups(RealmList<GroupRealm> usersGroups) {
        this.usersGroups = usersGroups;
    }
}
