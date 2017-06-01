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

    public static void addNewSubject(final SubjectRealm subjectRealm1){
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

    public List<SubjectRealm> getAllfromSubjectRealmSorted() {
        List<SubjectRealm> subjects = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SubjectRealm> all = realm.where(SubjectRealm.class).findAllSorted("subject");
        for (SubjectRealm subjectRealm : all) {
            subjects.add(subjectRealm);
        }
        realm.close();
        return subjects;
    }

    public List<SubjectRealm> searchElementsByName(String newText, long groupId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SubjectRealm> all = realm.where(GroupRealm.class).equalTo("id", groupId).
            findFirst().getSubjectsList().where().contains("subject", newText, Case.INSENSITIVE).findAllSorted("subject");
        /*** ta metoda tez działa, ale prawd. jest wolniejsza: **/
        //RealmResults<SubjectRealm> all = Realm.getDefaultInstance().where(SubjectRealm.class).equalTo("groupId", groupId).contains("subject", newText, Case.INSENSITIVE).findAllSorted("subject");
        realm.close();
        return new ArrayList<>(all);
    }
}
