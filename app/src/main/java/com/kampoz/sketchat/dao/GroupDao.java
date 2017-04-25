package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.realm.GroupRealm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by wasili on 2017-04-25.
 */

public class GroupDao {

    public List<GroupRealm> getAllfromGroupRealm() {
        List<GroupRealm> groups = new ArrayList<>();
        RealmResults<GroupRealm> all = Realm.getDefaultInstance().where(GroupRealm.class).findAll();
        for (GroupRealm groupRealm : all) {
            groups.add(groupRealm);
        }
        return groups;
    }
}
