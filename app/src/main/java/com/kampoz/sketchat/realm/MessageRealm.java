package com.kampoz.sketchat.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-04-15.
 */

public class MessageRealm extends RealmObject{

    @PrimaryKey
    private int id;
    private boolean left;
    private String userName;
    private String stringMessageText;

    public MessageRealm(){
    }

    public MessageRealm(boolean left, String message) {
        super();
        this.left = left;
        this.stringMessageText = message;
    }

    public MessageRealm(boolean left, String userName, String message) {
        super();
        this.left = left;
        this.userName = userName;
        this.stringMessageText = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public String getStringMessageText() {
        return stringMessageText;
    }

    public void setStringMessageText(String messageText) {
        this.stringMessageText = messageText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
