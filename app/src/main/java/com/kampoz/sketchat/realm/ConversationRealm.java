package com.kampoz.sketchat.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Kamil on 29.06.2017.
 */

public class ConversationRealm extends RealmObject {

  @PrimaryKey
  private long id;
  private RealmList<MessageRealm> messagesRealmList = new RealmList<>();


  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }

  public RealmList<MessageRealm> getMessagesRealmList() {
    return messagesRealmList;
  }
  public void setMessagesRealmList(RealmList<MessageRealm> messagesRealmList) {
    this.messagesRealmList = messagesRealmList;
  }
}
