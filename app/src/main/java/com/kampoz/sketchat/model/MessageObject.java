package com.kampoz.sketchat.model;

/**
 * Created by wasili on 2017-04-15.
 */

public class MessageObject {
    private boolean left;
    private String userName;
    private String stringMessageText;

    public MessageObject(){
    }
    public MessageObject(boolean left, String message) {
        super();
        this.left = left;
        this.stringMessageText = message;
    }

    public MessageObject(boolean left, String userName, String message) {
        super();
        this.left = left;
        this.userName = userName;
        this.stringMessageText = message;
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
