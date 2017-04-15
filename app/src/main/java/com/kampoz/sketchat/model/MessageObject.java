package com.kampoz.sketchat.model;

/**
 * Created by wasili on 2017-04-15.
 */

public class MessageObject {
    public boolean left;
    public String userName;
    public String message;

    public MessageObject(){
    }
    public MessageObject(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
    }

    public MessageObject(boolean left, String userName, String message) {
        super();
        this.left = left;
        this.userName = userName;
        this.message = message;
    }
}
