package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.realm.DrawingRealm;
import com.kampoz.sketchat.realm.GroupRealm;

import com.kampoz.sketchat.realm.SubjectRealm;
import io.realm.Case;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by wasili on 2017-04-25.
 */

public class GroupDao {

  private Realm realm;

  public GroupDao() {
    this.realm = Realm.getDefaultInstance();
  }

  public Realm getRealm() {
    return realm;
  }

  /***
   * Zapytania do bazy danych Realm, zwiazane z obiektem GroupRealm.class
   */

  public List<GroupRealm> getAllfromGroupRealm() {
    List<GroupRealm> groups = new ArrayList<>();
    RealmResults<GroupRealm> all = realm.where(GroupRealm.class).findAll();
    for (GroupRealm groupRealm : all) {
      groups.add(groupRealm);
    }
    return groups;
  }

  //zwraca wszystkie obiekty GroupRealm posortowane
  public List<GroupRealm> getAllfromGroupRealmSorted() {
    List<GroupRealm> groupList = realm.where(GroupRealm.class).findAllSorted("groupName");
    return groupList;
  }

  public List<GroupRealm> searchELementsByName(String newText) {
    List<GroupRealm> groups = new ArrayList<>();
    RealmResults<GroupRealm> all = realm.where(GroupRealm.class)
        .contains("groupName", newText, Case.INSENSITIVE).findAllSorted("groupName");
    for (GroupRealm groupRealm : all) {
      groups.add(groupRealm);
    }
    return groups;
  }

  public void deleteGroup(final long id) {
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.where(GroupRealm.class).equalTo("id", id).findFirst().deleteFromRealm();
      }
    });
  }

  public String getGroupNameForId(final long id) {
    String groupName = realm.where(GroupRealm.class).equalTo("id", id).findFirst().getGroupName();
    return groupName;
  }

  public void changeName(final String newName, final long groupId) {
    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        GroupRealm groupRealm = realm.where(GroupRealm.class).equalTo("id", groupId).findFirst();
        groupRealm.setGroupName(newName);
        realm.copyToRealmOrUpdate(groupRealm);
      }
    });
  }

  public void addSubjectToGroup(final long groupId, final SubjectRealm subjectRealm) {
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        //// TODO: 2017-05-31 zamienic subjectRealm tu na daoSubjectRealm jak juz bedzie
        subjectRealm.setId(subjectRealm.generateSubjectId());
        subjectRealm.setGroupId(groupId);
        realm.copyToRealm(subjectRealm);
        realm.where(GroupRealm.class).equalTo("id", groupId).findFirst().getSubjectsList()
            .add(subjectRealm);
        DrawingRealm drawingRealm = realm
            .createObject(DrawingRealm.class, DrawingRealm.generateId());
        ;
        realm.copyToRealmOrUpdate(drawingRealm);
        realm.where(SubjectRealm.class).equalTo("id", subjectRealm.getId()).findFirst()
            .setDrawing(drawingRealm);
      }
    });
  }

  public void addNewGroup(final GroupRealm groupRealm) {
    final GroupRealm groupRealm2 = new GroupRealm();
    groupRealm2.setId(generateGroupId());
    groupRealm2.setGroupName(groupRealm.getGroupName());
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(groupRealm2);
      }
    });
  }

  private int generateGroupId() {
    Realm realm = Realm.getDefaultInstance();
    int newId = 0;
    Number oldMaxId = realm.where(GroupRealm.class).max("id");
    if (oldMaxId == null) {
      realm.close();
      return newId;
    } else {
      realm.close();
      return oldMaxId.intValue() + 1;
    }
    //Realm.getDefaultInstance().where(GroupRealm.class).max("id").intValue() + 1;
  }

  public List<SubjectRealm> getSubjectsFromGroupSorted(long groupId) {
    List<SubjectRealm> subjects = new ArrayList<>();
    //RealmResults<SubjectRealm> all = Realm.getDefaultInstance()
    // .where(SubjectRealm.class).findAllSorted("subject");
    RealmResults<SubjectRealm> subjectsFromGroup = realm.where(GroupRealm.class).equalTo("id", groupId).findFirst().getSubjectsList().sort("subject");
    for (SubjectRealm subjectRealm : subjectsFromGroup) {
      subjects.add(subjectRealm);
    }
    return subjects;
  }
}
