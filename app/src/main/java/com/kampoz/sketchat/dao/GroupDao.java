package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.realm.ConversationRealm;
import com.kampoz.sketchat.realm.DrawingRealm;
import com.kampoz.sketchat.realm.GroupRealm;
import com.kampoz.sketchat.realm.SubjectRealm;
import com.kampoz.sketchat.realm.UserRealmSync;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wasili on 2017-04-25.
 */

public class GroupDao {

  private Realm realm;
  private String tag1 = "realm instance";
  private String tagOpen = "+ in GroupDao open";
  private String tagClose = "- in GroupDao close";
  private String tagCount = "= Realm instances opened in GroupDao: ";
  private String tagGlobal = "== globalRealmInstancesCount: ";
  static int count = 0;

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
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        GroupRealm groupRealm = realm.where(GroupRealm.class).equalTo("id", groupId).findFirst();
        groupRealm.setGroupName(newName);
        realm.copyToRealmOrUpdate(groupRealm);
      }
    });
  }

  /** Adding subject to group and creates Conversation object in Subject realm object */
  public void addSubjectToGroup(final long groupId, final SubjectRealm subjectRealm) {
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        SubjectDao subjectDao = new SubjectDao();
        subjectRealm.setId(subjectDao.generateSubjectId());
        subjectDao.closeRealmInstance();

        subjectRealm.setGroupId(groupId);
        realm.copyToRealm(subjectRealm);
        realm.where(GroupRealm.class).equalTo("id", groupId).findFirst().getSubjectsList().add(subjectRealm);

        DrawingDao drawingDao = new DrawingDao();
        DrawingRealm drawingRealm = realm.createObject(DrawingRealm.class, drawingDao.generateDrawingId());
        drawingDao.closeRealmInstance();

        realm.copyToRealmOrUpdate(drawingRealm);
        realm.where(SubjectRealm.class).equalTo("id", subjectRealm.getId()).findFirst().setDrawing(drawingRealm);

        ConversationDao conversationDao = new ConversationDao();
        ConversationRealm conversationRealm = realm.createObject(ConversationRealm.class, conversationDao.generateConversationId());
        conversationDao.closeRealmInstance();

        realm.copyToRealmOrUpdate(conversationRealm);
        realm.where(SubjectRealm.class).equalTo("id", subjectRealm.getId()).findFirst().setConversationRealm(conversationRealm);

        realm.close();
      }
    });
  }

  public void addUserToGroup(final long groupId, final UserRealmSync userRealm) {
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        UserRealmSyncDao userDao = new UserRealmSyncDao();
        userRealm.setId(userDao.generateUserId());
        userDao.closeRealmInstance();
        realm.copyToRealmOrUpdate(userRealm);
        realm.where(GroupRealm.class).equalTo("id", groupId).findFirst().getUsersList()
            .add(userRealm);
        realm.close();
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
    int newId = 0;
    Number oldMaxId = realm.where(GroupRealm.class).max("id");
    if (oldMaxId == null) {
      return newId;
    } else {
      return oldMaxId.intValue() + 1;
    }
    //Realm.getDefaultInstance().where(GroupRealm.class).max("id").intValue() + 1;
  }

  public List<SubjectRealm> getSubjectsFromGroupSorted(long groupId) {
    List<SubjectRealm> subjects = new ArrayList<>();
    //RealmResults<SubjectRealm> all = Realm.getDefaultInstance()
    // .where(SubjectRealm.class).findAllSorted("subject");
    RealmResults<SubjectRealm> subjectsFromGroup = realm.where(GroupRealm.class)
        .equalTo("id", groupId).findFirst().getSubjectsList().sort("subject");
    for (SubjectRealm subjectRealm : subjectsFromGroup) {
      subjects.add(subjectRealm);
    }
    return subjects;
  }

  public int getSubjectsCount(long groupId) {
    return realm.where(GroupRealm.class).equalTo("id", groupId).findFirst().getSubjectsList()
        .size();
  }

  public void closeRealmInstance() {
    //if(!realm.isClosed()) realm.close();
    realm.close();
  }
}
