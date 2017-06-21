package com.kampoz.sketchat.realm;

import com.kampoz.sketchat.dao.GroupDao;
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
  private long id;
  private long ownersId;
  private String groupName;
  private RealmList<SubjectRealm> subjectsList;
  private RealmList<UserRealm> usersList;

  public GroupRealm() {
  }

  public GroupRealm(long id, String groupName) {
    this.groupName = groupName;
    this.id = id;
  }
  public GroupRealm(String groupName, ArrayList<UserRealm> usersArrayList) {
    this.groupName = groupName;
  }

  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }

  public long getOwnersId() {
    return ownersId;
  }
  public void setOwnersId(long ownersId) {
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

  public RealmList<UserRealm> getUsersList() {
    return usersList;
  }
  public void setUsersList(RealmList<UserRealm> usersList) {
    this.usersList = usersList;
  }

}
