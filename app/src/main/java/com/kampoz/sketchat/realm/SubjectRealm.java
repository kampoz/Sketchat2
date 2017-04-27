package com.kampoz.sketchat.realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-04-16.
 */

public class SubjectRealm extends RealmObject{

    @PrimaryKey
    private int id;
    private String subject;
    private int interlocutorsNumber;

    public SubjectRealm() {
    }

    public SubjectRealm(String subject) {
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void changeName(final String newName){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                setSubject(newName);
            }
        });
    }

    public void deleteSubject(final int id){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(SubjectRealm.class).equalTo("id", id).findFirst().deleteFromRealm();
            }
        });
    }
}
