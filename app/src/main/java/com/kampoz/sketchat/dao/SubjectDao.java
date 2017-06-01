package com.kampoz.sketchat.dao;

import android.util.Log;
import com.kampoz.sketchat.activity.SplashActivity;
import com.kampoz.sketchat.realm.GroupRealm;
import com.kampoz.sketchat.realm.SubjectRealm;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wasili on 2017-05-31.
 */

public class SubjectDao {

  private Realm realm;
  private String tag1 = "realm instance";
  private String tagOpen = "+ in SubjectDao open";
  private String tagClose = "- in SubjectDao close";
  private String tagCount = "= Realm instances opened in SubjectDao: ";
  private String tagGlobal = "== globalRealmInstancesCount: ";
  private int count = 0;

  public SubjectDao() {
    this.realm = Realm.getDefaultInstance();
    count++;
    SplashActivity.globalRealmInstancesCount++;
    Log.d(tag1,"-------------------------");
    Log.d(tag1,tagOpen);
    Log.d(tag1,tagCount + count);
    Log.d(tag1,tagGlobal + SplashActivity.globalRealmInstancesCount);
  }

  public Realm getRealm() {
    return realm;
  }

  /***
   * Zapytania do bazy danych Realm, zwiazane z obiektem SubjectRealm.class
   */

  public void changeName(final String newName, final long subjectId){
    getRealm().executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        SubjectRealm subjectRealm = realm.where(SubjectRealm.class).equalTo("id", subjectId).findFirst();
        subjectRealm.setSubject(newName);
        realm.copyToRealmOrUpdate(subjectRealm);
      }
    });
  }

  public void deleteSubject(final long id){
    getRealm().executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.where(SubjectRealm.class).equalTo("id", id).findFirst().deleteFromRealm();
      }
    });
  }

  public  void addNewSubject(final SubjectRealm subjectRealm1){
    final SubjectRealm subjectRealm = new SubjectRealm();
    subjectRealm.setId(generateSubjectId());
    subjectRealm.setSubject(subjectRealm1.getSubject());
    getRealm().executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(subjectRealm);
      }
    });
  }

  public int generateSubjectId() {
    int newId = 0;
    Number oldMaxId = getRealm().where(SubjectRealm.class).max("id");
    if(oldMaxId==null){
      return newId;
    }else{
      return oldMaxId.intValue()+1;
    }
    //Realm.getDefaultInstance().where(GroupRealm.class).max("id").intValue() + 1;
  }

  public List<SubjectRealm> getAllfromSubjectRealmSorted() {
    List<SubjectRealm> subjects = new ArrayList<>();
    RealmResults<SubjectRealm> all = getRealm().where(SubjectRealm.class).findAllSorted("subject");
    for (SubjectRealm subjectRealm : all) {
      subjects.add(subjectRealm);
    }
    return subjects;
  }

  public List<SubjectRealm> searchElementsByName(String newText, long groupId) {
    RealmResults<SubjectRealm> all = getRealm().where(GroupRealm.class).equalTo("id", groupId).
        findFirst().getSubjectsList().where().contains("subject", newText, Case.INSENSITIVE).findAllSorted("subject");
    /*** ta metoda tez działa, ale prawd. jest wolniejsza: **/
    //RealmResults<SubjectRealm> all = Realm.getDefaultInstance().where(SubjectRealm.class).equalTo("groupId", groupId).contains("subject", newText, Case.INSENSITIVE).findAllSorted("subject");

    return new ArrayList<>(all);
  }

  public void closeRealmInstance(){
    realm.close();
    count--;
    SplashActivity.globalRealmInstancesCount--;
    Log.d(tag1,tagClose);
    Log.d(tag1,tagCount + count);
    Log.d(tag1,tagGlobal + SplashActivity.globalRealmInstancesCount);
  }

}
