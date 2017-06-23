package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.realm.UserRealm;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmConfiguration;

/**
 * Created by Kamil on 15.06.2017.
 */

public class UserDao {

  private Realm realm;

  public UserDao() {
    this.realm = Realm.getDefaultInstance();
  }

  public void addNewUser(String userName) {
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
    userRealm.setName(userName);
    realm.executeTransaction(new Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(userRealm);
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

  public void closeRealmInstance() {
    realm.close();
  }
}
