package com.kampoz.sketchat.model;

import java.util.Date;
import io.realm.RealmObject;

/**
 * Created by wasili on 2017-04-15.
 */

public class ConversationModel extends RealmObject{

    private String textMessage;
    private Date timeOfMessage;


}
