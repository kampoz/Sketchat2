package com.kampoz.sketchat.helper;

import com.kampoz.sketchat.model.MessageObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wasili on 2017-04-15.
 */

public class MyRandomValuesGenerator {

    public String getRandomText(){
        Random random = new Random();
        String[] messages = {
                "orem ipsum dolor sit amet, consectetur adipiscing elit. Praesent" +
                " ut mi non purus scelerisque faucibus. Nunc id laoreet ante. Interdum et malesuada",
                "In eget accumsan leo. Fusce ornare mauris eget porttitor congue.",
                "das sddas sda",
                "Cras aliquet augue nibh, at bibendum mauris imperdiet id. Vestibulum sollicitudin maximus" +
                        " orci et iaculis. Morbi vulputate mollis rutrum. Fusce eleifend, magna vitae placerat " +
                        "eleifend, neque erat tincidunt orci, quis feugiat orci ex a tortor. Praesent tincidunt" +
                        " purus vel leo fermentum tempus malesuada vitae sapien. Aliquam ac tortor augue. " +
                        "Suspendisse massa dui, rhoncus in mi sed, dictum fringilla nibh. ",
                "Morbi malesuada, erat vitae suscipit posuere, sapien ipsum fermentum dolor, sed sollicitudin ipsum lectus eget risus. Praesent placerat tempus ligula,",
                "Pellentesque vitae pretium lectus.",
                "Tak",
                "Nie",
                "Fusce ornare mauris eget porttitor congue. Pellentesque gravida risus massa, in posuere metus finibus eget.",
                "Pellentesque consectetur ex eget orci consequat viverra. Etiam pretium pellentesque lobortis.",
                "Morbi sit amet imperdiet felis."
        };
        return messages[random.nextInt(messages.length)];
    }

    public boolean getrandomBoolean(){
        Random random = new Random();
        return random.nextBoolean();
    }

    public ArrayList<MessageObject> setMessagesArrayList(int elementNumber){
        ArrayList<MessageObject> messagesList = new ArrayList<>();
        for(int i=0; i<elementNumber; i++){
            MessageObject messageObject = new MessageObject(getrandomBoolean(), getRandomText());
        }
        return messagesList;
    }
}
