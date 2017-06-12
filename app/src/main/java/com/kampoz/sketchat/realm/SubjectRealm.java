package com.kampoz.sketchat.realm;

import io.realm.Case;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-04-16.
 */

public class SubjectRealm extends RealmObject{

    @PrimaryKey
    private long id;
    private long groupId;
    private String subject;
    private int interlocutorsNumber;
    private DrawingRealm drawing;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public long getGroupId() {
        return groupId;
    }
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    public int getInterlocutorsNumber() {
        return interlocutorsNumber;
    }
    public void setInterlocutorsNumber(int interlocutorsNumber) {
        this.interlocutorsNumber = interlocutorsNumber;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public DrawingRealm getDrawing() {
        return drawing;
    }
    public void setDrawing(DrawingRealm drawing) {
        this.drawing = drawing;
    }


    public static int generateSubjectId() {
        Realm realm = Realm.getDefaultInstance();
        int newId = 0;
        Number oldMaxId = realm.where(SubjectRealm.class).max("id");
        if(oldMaxId==null){
            realm.close();
            return newId;
        }else{
            realm.close();
            return oldMaxId.intValue()+1;
        }
        //Realm.getDefaultInstance().where(GroupRealm.class).max("id").intValue() + 1;
    }


}
