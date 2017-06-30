package com.kampoz.sketchat.dao;

import android.util.Log;
import com.kampoz.sketchat.realm.GroupRealm;
import com.kampoz.sketchat.realm.UserRealm;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;
import io.realm.exceptions.RealmException;

/**
 * Created by Kamil on 15.06.2017.
 */

public class UserDao {

  private Realm realm;
  private String tagUserDao = "UserDao tag";


  public UserDao() {
    this.realm = Realm.getDefaultInstance();
  }

  /** This constructor using when local realm instance with RealmConfiguration is needed */
  public UserDao(RealmConfiguration realmConfiguration){
    this.realm = Realm.getInstance(realmConfiguration);
  }

  /** This constructor can be used when realm with Syncconfigiration is needed */
  public UserDao(SyncConfiguration syncConfiguration){
    this.realm = Realm.getInstance(syncConfiguration);
  }

  public void registerNewUser(String userName) {
    final UserRealm userRealm = new UserRealm();
    userRealm.setId(generateUserId());
    userRealm.setName(userName);
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(userRealm);
      }
    });
  }

  /** Saves login userRealm locally with id 0 **/
  public void saveLoginUserLocally(String userName){
    final UserRealm userRealm = new UserRealm();
    userRealm.setId(0);
    userRealm.setName(userName);
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(userRealm);
      }
    });
  }

  /** Saves login userRealm locally with name and id like in global Realm **/
  public void saveLoginUserLocally(final UserRealm userRealm){
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.deleteAll();
        realm.copyToRealmOrUpdate(userRealm);
      }
    });
  }

  public void saveLoginUserLocally(final UserRealm userRealm, RealmConfiguration realmConfiguration){
    Realm realm = Realm.getInstance(realmConfiguration);
    realm.executeTransaction(new Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(userRealm);
      }
    });
  }

  public void saveLoginUserLocally(String userName, RealmConfiguration realmConfiguration){
    Realm realm = Realm.getInstance(realmConfiguration);
    final UserRealm userRealm = new UserRealm();
    userRealm.setId(0);
    userRealm.setName(userName);
    realm.executeTransaction(new Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(userRealm);
      }
    });
  }

  /** Works only with realm local RealmConfiguration */
  public UserRealm getCurrentLoginUser(){
    UserRealm userRealmLocally = new UserRealm();
    if(realm.where(UserRealm.class).count()==1)
    {
      userRealmLocally = realm.where(UserRealm.class).findFirst();
    }else{
      throw new RealmException("Wrong RealmUser objects amount in local realm database, not equals 1");
    }
    return userRealmLocally;
  }

  /** Used with SyncConfiguration */
  public void addingUserToGroupAndGroupToUser(final long userId, final long groupId){
    realm.executeTransaction(new Transaction() {
      @Override
      public void execute(Realm realm) {
        GroupRealm groupRealm = realm.where(GroupRealm.class).equalTo("id", groupId).findFirst();
        UserRealm userRealm  = realm.where(UserRealm.class).equalTo("id", userId).findFirst();
        groupRealm.getUsersList().add(userRealm);
        userRealm.getUsersGroups().add(groupRealm);
        realm.copyToRealmOrUpdate(userRealm);
        realm.copyToRealmOrUpdate(groupRealm);
      }
    });
  }

  public boolean ifUserNameExist(String userName){
    UserRealm user = realm.where(UserRealm.class).equalTo("name", userName).findFirst();
    if(user != null) return true;
    else return false;
  }

  public UserRealm getUserByName(String userName){
    UserRealm userRealm = realm.where(UserRealm.class).equalTo("name", userName).findFirst();
    return userRealm;
  }

  public UserRealm getUserById(long userId){
    UserRealm userRealm = realm.where(UserRealm.class).equalTo("id", userId).findFirst();
    return userRealm;
  }

  public long generateUserId() {
    long newId = 0;
    Number oldMaxId = realm.where(UserRealm.class).max("id");
    if (oldMaxId == null) {
      return newId;
    } else {
      return oldMaxId.intValue() + 1;
    }
  }

  /** Works when constructor of UserDao is used with RealmConfiguration */
  public boolean isUserLogin(){
    UserRealm userRealm = realm.where(UserRealm.class).findFirst();
    if(userRealm!=null){
      Log.d(tagUserDao, "lokalnie zapisany jest user: "+userRealm.getName());
      return true;
    }else{
      Log.d(tagUserDao, "lokalnie brak zalogowanego usera.");
      return false;
    }
  }

  /** Works when constructor of UserDao is used with SyncConfiguration */
  public boolean ifUserExistInDataBase(String userName){
    UserRealm userRealm = realm.where(UserRealm.class).equalTo("name", userName).findFirst();
    if(userRealm!=null)
      return true;
    else
      return false;
  }

  /** Works when constructor of UserDao is used with RealmConfiguration */
  public void cleanLocalRealm(){
    realm.executeTransaction(new Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.deleteAll();
      }
    });
  }

  public void closeRealmInstance() {
    realm.close();
  }
}
