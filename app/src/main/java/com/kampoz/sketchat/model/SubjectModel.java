package com.kampoz.sketchat.model;

import java.util.ArrayList;

/**
 * Created by wasili on 2017-04-16.
 */

public class SubjectModel {
    private String subject;
    private int interlocutorsNumber;

    public SubjectModel(String subject) {
        this.subject = subject;
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
}
