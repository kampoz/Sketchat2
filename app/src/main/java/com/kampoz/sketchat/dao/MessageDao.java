package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.activity.SplashActivity;
import com.kampoz.sketchat.realm.MessageRealm;
import com.kampoz.sketchat.realm.SubjectRealm;
import io.realm.Realm;
import java.util.Date;

/**
 * Created by Kamil on 29.06.2017.
 */

public class MessageDao {
  private Realm realm;

  /** Constructors */
  public MessageDao() {
    this.realm = Realm.getInstance(SplashActivity.publicSyncConfiguration);
  }
  /** Public methods */
  public void saveMessageGlobally(final long subjectId, long userId, String mesageText){
    final MessageRealm messageRealm = new MessageRealm(userId, mesageText);
    messageRealm.setId(generateMessageId());
    messageRealm.setMessageTime(new Date());
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(messageRealm);
        realm.where(SubjectRealm.class).equalTo("id", subjectId).findFirst().getConversationRealm().getMessagesRealmList().add(messageRealm);
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
