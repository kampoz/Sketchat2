package com.kampoz.sketchat.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by wasili on 2017-04-16.
 */

public class SubjectRealm extends RealmObject{

    @PrimaryKey
    private long id;
    private long groupId;
    private String subject;
    private int interlocutorsNumber;
    private DrawingRealm drawing;
    private ConversationRealm conversationRealm;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getGroupId() {
        return groupId;
    }
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    public int getInterlocutorsNumber() {
        return interlocutorsNumber;
    }
    public void setInterlocutorsNumber(int interlocutorsNumber) {
        this.interlocutorsNumber = interlocutorsNumber;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public DrawingRealm getDrawing() {
        return drawing;
    }
    public void setDrawing(DrawingRealm drawing) {
        this.drawing = drawing;
    }
    public ConversationRealm getConversationRealm() {
        return conversationRealm;
    }
    public void setConversationRealm(ConversationRealm conversationRealm) {
        this.conversationRealm = conversationRealm;
    }
}
