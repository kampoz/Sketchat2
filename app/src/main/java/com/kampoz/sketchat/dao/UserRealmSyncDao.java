package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.activity.SplashActivity;
import com.kampoz.sketchat.realm.GroupRealm;
import com.kampoz.sketchat.realm.UserRealmSync;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.SyncConfiguration;

/**
 * Created by Kamil on 15.06.2017.
 */
/** DAO should works only with global SyncConfiguration
 * */
public class UserRealmSyncDao {

  private Realm realm;
  private String tagUserDao = "UserRealmSyncDao tag";


  public UserRealmSyncDao() {
    this.realm = Realm.getInstance(SplashActivity.publicSyncConfiguration);
  }

  public UserRealmSyncDao(SyncConfiguration syncConfiguration){
    this.realm = Realm.getInstance(syncConfiguration);
  }

  public void registerNewUser(String userName) {
    final UserRealmSync userRealm = new UserRealmSync();
    userRealm.setId(generateUserId());
    userRealm.setName(userName);
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(userRealm);
      }
    });
  }


  /** Used with SyncConfiguration */
  public void addingUserToGroupAndGroupToUser(final long userId, final long groupId){
    realm.executeTransaction(new Transaction() {
      @Override
      public void execute(Realm realm) {
        GroupRealm groupRealm = realm.where(GroupRealm.class).equalTo("id", groupId).findFirst();
        UserRealmSync userRealm  = realm.where(UserRealmSync.class).equalTo("id", userId).findFirst();
        groupRealm.getUsersList().add(userRealm);
        userRealm.getUsersGroups().add(groupRealm);
        realm.copyToRealmOrUpdate(userRealm);
        realm.copyToRealmOrUpdate(groupRealm);
      }
    });
  }

  public boolean ifUserNameExist(String userName){
    UserRealmSync user = realm.where(UserRealmSync.class).equalTo("name", userName).findFirst();
    if(user != null) return true;
    else return false;
  }

  public UserRealmSync getUserByName(String userName){
    UserRealmSync userRealm = realm.where(UserRealmSync.class).equalTo("name", userName).findFirst();
    return userRealm;
  }

  public UserRealmSync getUserById(long userId){
    UserRealmSync userRealm = realm.where(UserRealmSync.class).equalTo("id", userId).findFirst();
    return userRealm;
  }

  public long generateUserId() {
    long newId = 0;
    Number oldMaxId = realm.where(UserRealmSync.class).max("id");
    if (oldMaxId == null) {
      return newId;
    } else {
      return oldMaxId.longValue() + 1;
    }
  }

  /** Works when constructor of UserRealmSyncDao is used with SyncConfiguration */
  public boolean ifUserExistInDataBase(String userName){
    UserRealmSync userRealm = realm.where(UserRealmSync.class).equalTo("name", userName).findFirst();
    if(userRealm!=null)
      return true;
    else
      return false;
  }

  public void closeRealmInstance() {
    realm.close();
  }
}
