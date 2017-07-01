package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.activity.SplashActivity;
import com.kampoz.sketchat.realm.UserRealmLocal;
import io.realm.Realm;
import io.realm.Realm.Transaction;
import io.realm.RealmConfiguration;

/**
 * Created by Kamil on 01.07.2017.
 */

/**
 * DAO should works only with local RealmConfiguration
 */

public class UserRealmLocalDao {


  private Realm realm;
  private String tagUserDao = "UserRealmLocalDao tag";


  public UserRealmLocalDao() {
    this.realm = Realm.getInstance(SplashActivity.publicRealmConfiguration);
  }

  public UserRealmLocalDao(RealmConfiguration realmConfiguration) {
    this.realm = Realm.getInstance(realmConfiguration);
  }

  /** Saves login userRealm locally with id 0 **/
  public void saveLoginUserLocally(long id, String userName){
    final UserRealmLocal userRealmLocal = new UserRealmLocal();
    userRealmLocal.setId(id);
    userRealmLocal.setName(userName);
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(userRealmLocal);
      }
    });
  }

  /** Saves login userRealm locally with name and id like in global Realm **/
  public void saveLoginUserLocally(final UserRealmLocal userRealmLocal){
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.deleteAll();
        realm.copyToRealmOrUpdate(userRealmLocal);
      }
    });
  }

  /** Works only with realm local RealmConfiguration */
  public UserRealmLocal getCurrentLoginUser(){
    UserRealmLocal userRealmLocal = new UserRealmLocal();
    if(realm.where(UserRealmLocal.class).count()==1)
    {
      userRealmLocal = realm.where(UserRealmLocal.class).findFirst();
    }else{
      throw new ArrayIndexOutOfBoundsException("Wrong RealmUser objects amount in local realm database, not equals 1");
    }
    return userRealmLocal;
  }

  public boolean isUserLogin(){
    long count = realm.where(UserRealmLocal.class).count();
    if(count==1)
      return true;
    else if (count==0)
      return false;
    else{
      throw new ArrayIndexOutOfBoundsException("Wrong RealmUser objects amount in local realm database, not equals 1 or 0");
    }
  }

  /** Works when constructor of UserRealmSyncDao is used with RealmConfiguration */
  public void cleanLocalRealm(){
    realm.executeTransaction(new Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.deleteAll();
      }
    });
  }

  public void logoutCurrentUser(){
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
