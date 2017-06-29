package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.activity.SplashActivity;
import com.kampoz.sketchat.realm.MessageRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.SyncConfiguration;

/**
 * Created by Kamil on 29.06.2017.
 */

public class MessageDao {
  private Realm realm;

  /** Constructors */
  public MessageDao() {
    this.realm = Realm.getDefaultInstance();
  }

  /** This constructor using when local realm instance with RealmConfiguration is needed */
  public MessageDao(RealmConfiguration realmConfiguration){
    this.realm = Realm.getInstance(realmConfiguration);
  }

  /** This constructor can be used when realm with Syncconfigiration is needed */
  public MessageDao(SyncConfiguration syncConfiguration){
    this.realm = Realm.getInstance(syncConfiguration);
  }

  /** Public methods */
  public void saveMessageGlobally(long userId, String mesageText){
    final MessageRealm messageRealm = new MessageRealm(userId, mesageText);
    MessageDao messageDao = new MessageDao(SplashActivity.publicSyncConfiguration);
    messageRealm.setId(messageDao.generateMessageId());
    messageDao.closeRealmInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(messageRealm);
      }
    });
  }



  public long generateMessageId() {
    long newId = 0;
    Number oldMaxId = realm.where(MessageRealm.class).max("id");
    if (oldMaxId == null) {
      return newId;
    } else {
      return oldMaxId.intValue() + 1;
    }
  }

  /** this method has to be called always if Dao ended its task */
  public void closeRealmInstance() {
    realm.close();
  }

}
