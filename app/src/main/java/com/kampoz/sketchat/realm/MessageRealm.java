package com.kampoz.sketchat.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by wasili on 2017-04-15.
 */

public class MessageRealm extends RealmObject{

    @PrimaryKey
    private long id;
    private long userId;
    private String messageText;
    private Date messageTime;

    public MessageRealm(){
    }

    public MessageRealm(String message) {
        super();
        this.messageText = message;
    }

    public MessageRealm(long userId, String messageText) {
        super();
        this.userId = userId;
        this.messageText = messageText;
    }

    public MessageRealm(long userId, String message, Date messageTime){
        super();
        this.userId = userId;
        this.messageText = message;
        this.messageTime = messageTime;
    }

    /** constructor fr seed*/
    public MessageRealm(long id, long userId, String message, Date messageTime){
        super();
        this.id = id;
        this.userId = userId;
        this.messageText = message;
        this.messageTime = messageTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }
}
