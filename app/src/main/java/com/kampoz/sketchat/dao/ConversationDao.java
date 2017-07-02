package com.kampoz.sketchat.dao;

import com.kampoz.sketchat.activity.SplashActivity;
import com.kampoz.sketchat.realm.ConversationRealm;
import com.kampoz.sketchat.realm.MessageRealm;
import com.kampoz.sketchat.realm.SubjectRealm;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncConfiguration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Created by Kamil on 29.06.2017.
 */

public class ConversationDao {

  public interface ConversationListener{
    void refreshAdapterView();
  }

  private Realm realm;
  private ConversationListener listener;

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

  /** gets messages for current subject sorted by date ascendencing */
  public ArrayList<MessageRealm> getMessages(long subjectId){
    RealmResults<MessageRealm> messagesRealmResults = realm.where(SubjectRealm.class).equalTo("id", subjectId).findFirst().getConversationRealm().getMessagesRealmList().sort("messageTime", Sort.ASCENDING);
    ArrayList<MessageRealm> messagesList = new ArrayList<>();
    messagesList.addAll(messagesRealmResults);

    messagesRealmResults.addChangeListener(
        new OrderedRealmCollectionChangeListener<RealmResults<MessageRealm>>() {
          @Override
          public void onChange(RealmResults<MessageRealm> collection, OrderedCollectionChangeSet changeSet) {
            changeSet.getInsertions();

            listener.refreshAdapterView();
          }
        });

    return messagesList;
  }

  public ArrayList<MessageRealm> getMessages2(long subjectId){
    RealmResults<MessageRealm> messagesRealmResults = realm.where(SubjectRealm.class).equalTo("id", subjectId).findFirst().getConversationRealm().getMessagesRealmList().sort("messageTime", Sort.ASCENDING);
    ArrayList<MessageRealm> messagesList = new ArrayList<>();
    messagesList.addAll(messagesRealmResults);

    return messagesList;
  }

  //Realm.getDefaultInstance().where(CGMeeting.class).equalTo("organisationId", Globals.mCurrentCompanyId).findAllSorted("date", Sort.DESCENDING);

  public ArrayList<MessageRealm> generteMessagesSeedList(int elementsNumber){
    ArrayList<MessageRealm> messagesList = new ArrayList<>();
    Random random = new Random();
    int userRandomId;
    for(long i=0; i< elementsNumber; i++){
      userRandomId = random.nextInt(3);
      Date date = new Date();
      messagesList.add(new MessageRealm(i,userRandomId , "test text; "+"userId = "+userRandomId+"; "+date.toString(), date));
    }
    return messagesList;
  }

  public void setListener(ConversationListener listener) {
    this.listener = listener;
  }

  public void closeRealmInstance() {
    realm.close();
  }
}
