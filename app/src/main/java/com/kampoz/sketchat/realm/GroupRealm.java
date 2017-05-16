package com.kampoz.sketchat.realm;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-04-16.
 */

public class GroupRealm extends RealmObject {

  @PrimaryKey
  private int id;
  private int ownersId;
  private String groupName;
  private RealmList<SubjectRealm> subjectsList;

  public GroupRealm() {
  }

  public GroupRealm(int id, String groupName) {
    this.groupName = groupName;
    this.id = id;
  }

  public GroupRealm(String groupName, ArrayList<UserRealm> usersArrayList) {
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

  public RealmList<SubjectRealm> getSubjectsList() {
    return subjectsList;
  }

  public void setSubjectsList(RealmList<SubjectRealm> subjectsList) {
    this.subjectsList = subjectsList;
  }

  public List<GroupRealm> getAllfromGroupRealm() {
    List<GroupRealm> groups = new ArrayList<>();
    RealmResults<GroupRealm> all = Realm.getDefaultInstance().where(GroupRealm.class).findAll();
    for (GroupRealm groupRealm : all) {
      groups.add(groupRealm);
    }
    return groups;
  }

  //zwraca wszystkie obiekty GroupRealm posortowane
  public List<GroupRealm> getAllfromGroupRealmSorted() {
//    List<GroupRealm> groups = new ArrayList<>();
//    RealmResults<GroupRealm> all = Realm.getDefaultInstance().where(GroupRealm.class)
//        .findAllSorted("groupName");
//    for (GroupRealm groupRealm : all) {
//      groups.add(groupRealm);
//    }
    return Realm.getDefaultInstance().where(GroupRealm.class).findAllSorted("groupName");
  }

  public List<GroupRealm> searchELementsByName(String newText) {
    List<GroupRealm> groups = new ArrayList<>();
    RealmResults<GroupRealm> all = Realm.getDefaultInstance().where(GroupRealm.class)
        .contains("groupName", newText, Case.INSENSITIVE).findAllSorted("groupName");
    for (GroupRealm groupRealm : all) {
      groups.add(groupRealm);
    }
    return groups;
  }

  public void deleteGroup(final int id) {
    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.where(GroupRealm.class).equalTo("id", id).findFirst().deleteFromRealm();
      }
    });
  }

  public static String getGroupNameForId(final int id) {
    return Realm.getDefaultInstance().where(GroupRealm.class).equalTo("id", id).findFirst().getGroupName();
  }

  public void changeName(final String newName) {
    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        setGroupName(newName);
      }
    });
  }

  public void addSubjectToGroup(final int groupId, final SubjectRealm subjectRealm){
    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        subjectRealm.setId(subjectRealm.generateSubjectId());
        subjectRealm.setGroupId(groupId);
        realm.copyToRealm(subjectRealm);
        realm.where(GroupRealm.class).equalTo("id", groupId).findFirst().getSubjectsList().add(subjectRealm);
      }
    });
  }

  public void addNewGroup(final GroupRealm groupRealm) {
//        Realm.getDefaultInstance().beginTransaction();
//        GroupRealm groupRealm1 = Realm.getDefaultInstance().createObject(GroupRealm.class);
//        groupRealm.generateId(generateGroupId());
//        Realm.getDefaultInstance().commitTransaction();
    ///////////////////////
    Realm realm = Realm.getDefaultInstance();
    final GroupRealm groupRealm2 = new GroupRealm();
    groupRealm2.setId(generateGroupId());
    groupRealm2.setGroupName(groupRealm.getGroupName());
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(groupRealm2);
      }
    });
    realm.close();
  }

  private int generateGroupId() {
    Realm defaultInstance = Realm.getDefaultInstance();
    int newId = 0;
    Number oldMaxId = defaultInstance.where(GroupRealm.class).max("id");
    if (oldMaxId == null) {
      return newId;
    } else {
      return oldMaxId.intValue() + 1;
    }
    //Realm.getDefaultInstance().where(GroupRealm.class).max("id").intValue() + 1;
  }

  public static List<SubjectRealm> getSubjectsFromGroupSorted(int groupId) {
    List<SubjectRealm> subjects = new ArrayList<>();
    //RealmResults<SubjectRealm> all = Realm.getDefaultInstance().where(SubjectRealm.class).findAllSorted("subject");
    RealmResults<SubjectRealm> subjectsFromGroup = Realm.getDefaultInstance().where(GroupRealm.class).equalTo("id", groupId).findFirst().getSubjectsList().sort("subject");
    for (SubjectRealm subjectRealm : subjectsFromGroup) {
      subjects.add(subjectRealm);
    }
    return subjects;
  }

}
