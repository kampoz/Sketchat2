package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.realm.DrawingRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;

/**
 * Created by Kamil on 29.06.2017.
 */

public class DrawingDao {

  Realm realm;


  public DrawingDao() {
    this.realm = Realm.getDefaultInstance();
  }

  /** This constructor using when local realm instance with RealmConfiguration is needed */
  public DrawingDao(RealmConfiguration realmConfiguration){
    this.realm = Realm.getInstance(realmConfiguration);
  }

  /** This constructor can be used when realm with Syncconfigiration is needed */
  public DrawingDao(SyncConfiguration syncConfiguration){
    this.realm = Realm.getInstance(syncConfiguration);
  }


  public long generateDrawingId() {
    long newId = 0;
    Number oldMaxId = realm.where(DrawingRealm.class).max("id");
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
