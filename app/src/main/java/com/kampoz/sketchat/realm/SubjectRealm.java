package com.kampoz.sketchat.realm;

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

    public void addNewSubject(final SubjectRealm subjectRealm1){
        Realm realm = Realm.getDefaultInstance();
        final SubjectRealm subjectRealm = new SubjectRealm();
        subjectRealm.setId(generateSubjectId());
        subjectRealm.setSubject(subjectRealm1.getSubject());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(subjectRealm);
            }
        });
        realm.close();
    }

    private int generateSubjectId() {
        Realm defaultInstance = Realm.getDefaultInstance();
        int newId = 0;
        Number oldMaxId = defaultInstance.where(SubjectRealm.class).max("id");
        if(oldMaxId==null){
            return newId;
        }else
            return oldMaxId.intValue()+1;
        //Realm.getDefaultInstance().where(GroupRealm.class).max("id").intValue() + 1;
    }

    public List<SubjectRealm> getAllfromSubjectRealmSorted() {
        List<SubjectRealm> subjects = new ArrayList<>();
        RealmResults<SubjectRealm> all = Realm.getDefaultInstance().where(SubjectRealm.class).findAllSorted("subject");
        for (SubjectRealm subjectRealm : all) {
            subjects.add(subjectRealm);
        }
        return subjects;
    }
}
