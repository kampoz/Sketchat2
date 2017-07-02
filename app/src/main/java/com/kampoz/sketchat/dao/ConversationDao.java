package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.activity.SplashActivity;
import com.kampoz.sketchat.realm.ConversationRealm;
import com.kampoz.sketchat.realm.MessageRealm;
import com.kampoz.sketchat.realm.SubjectRealm;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.SyncConfiguration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Kamil on 29.06.2017.
 */

public class ConversationDao {

  Realm realm;

  public ConversationDao() {
    this.realm = Realm.getInstance(SplashActivity.publicSyncConfiguration);
  }

  /** This constructor using when local realm instance with RealmConfiguration is needed */
  /*public ConversationDao(RealmConfiguration realmConfiguration){
    this.realm = Realm.getInstance(realmConfiguration);
  }*/

  /** This constructor can be used when realm with Syncconfigiration is needed */
  public ConversationDao(SyncConfiguration syncConfiguration){
    this.realm = Realm.getInstance(syncConfiguration);
  }

  public long generateConversationId() {
    long newId = 0;
    Number oldMaxId = realm.where(ConversationRealm.class).max("id");
    if (oldMaxId == null) {
      return newId;
    } else {
      return oldMaxId.intValue() + 1;
    }
  }

  public ArrayList<MessageRealm> getMessages(long subjectId){
    RealmList<MessageRealm> messagesRealmList = realm.where(SubjectRealm.class).equalTo("id", subjectId).findFirst().getConversationRealm().getMessagesRealmList();
    ArrayList<MessageRealm> messagesList = new ArrayList<>();
    messagesList.addAll(messagesRealmList);
    return messagesList;
  }

  public ArrayList<MessageRealm> generteMessagesSeedList(int elementsNumber){
    ArrayList<MessageRealm> messagesList = new ArrayList<>();
    Random random = new Random();
    int userRandomId;
    for(long i=0; i< elementsNumber; i++){
      userRandomId = random.nextInt(3);
      messagesList.add(new MessageRealm(i,userRandomId , "test text; "+"userId = "+userRandomId, new Date()));
    }
    return messagesList;
  }

  public void closeRealmInstance() {
    realm.close();
  }
}
