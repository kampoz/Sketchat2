package com.kampoz.sketchat.realm;

import com.kampoz.sketchat.realm.SubjectRealm;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-04-16.
 */

public class GroupRealm extends RealmObject{

    @PrimaryKey
    private int id;
    private int ownersId;
    private String groupName;
    private RealmList<SubjectRealm> usersArrayList;

    public GroupRealm(){
    }

    public GroupRealm(String groupName){
        this.groupName = groupName;
    }

    public GroupRealm(String groupName, ArrayList<UserRealm> usersArrayList){
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(int ownersId) {
        this.ownersId = ownersId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public RealmList<SubjectRealm> getUsersArrayList() {
        return usersArrayList;
    }

    public void setUsersArrayList(RealmList<SubjectRealm> usersArrayList) {
        this.usersArrayList = usersArrayList;
    }
}
