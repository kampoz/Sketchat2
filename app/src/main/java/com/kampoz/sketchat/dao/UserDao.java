package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.realm.UserRealm;

import io.realm.Realm;

/**
 * Created by Kamil on 15.06.2017.
 */

public class UserDao {

    private Realm realm;

    public UserDao() {
        this.realm = Realm.getDefaultInstance();
    }

    public void addNewUser(){
        final UserRealm userRealm = new UserRealm();
        userRealm.setId(generateUserId());
    }

    public void closeRealmInstance(){
        realm.close();
    }

    public long generateUserId() {
        long newId = 0;
        Number oldMaxId = realm.where(UserRealm.class).max("id");
        if (oldMaxId == null) {
            return newId;
        } else {
            return oldMaxId.intValue() + 1;
        }
    }
}
