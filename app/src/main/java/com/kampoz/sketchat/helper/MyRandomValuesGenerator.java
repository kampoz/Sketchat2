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
                "veni vidi vici",
                "Cras aliquet augue nibh, at bibendum mauris imperdiet id. Vestibulum sollicitudin maximus" +
                        " orci et iaculis. Morbi vulputate mollis rutrum. Fusce eleifend, magna vitae placerat " +
                        "eleifend, neque erat tincidunt orci, quis feugiat orci ex a tortor. Praesent tincidunt" +
                        " purus vel leo fermentum tempus malesuada vitae sapien. Aliquam ac tortor augue. " +
                        "Suspendisse massa dui, rhoncus in mi sed, dictum fringilla nibh. ",
                "Morbi malesuada, erat vitae suscipit posuere, sapien ipsum fermentum dolor, sed sollicitudin ipsum lectus eget risus. Praesent placerat tempus ligula,",
                "Pellentesque vitae pretium lectus?",
                "Tak",
                "Nie",
                "OK",
                "Dlaczego?",
                "Fusce ornare mauris eget porttitor congue. Pellentesque gravida risus massa, in posuere metus finibus eget.",
                "Pellentesque consectetur ex eget orci consequat viverra. Etiam pretium pellentesque lobortis.",
                "Morbi sit amet imperdiet felis.",
                "Arci consequat viverra",
                "Morbi egestas, nunc vel tincidunt in, volutpat enim sit amet, consectetuer viverra quis," +
                        " lacinia quis, faucibus orci ac dolor. Ut sit amet erat. Fusce gravida, quam tristique" +
                        " senectus et ultrices tortor vehicula sapien tristique dapibus, mauris enim, euismod nulla non enim nunc, fringilla orci. "
        };
        return messages[random.nextInt(messages.length)];
    }

    //Nie działa, przyjmuje tylko jedną wartośc zawsze.
    public boolean getrandomBoolean(){
        Random random = new Random();
        return random.nextBoolean();
    }

        //Generuje ArrayLIst z obiektami MessageObject
    public ArrayList<MessageObject> generateMessagesArrayList(int elementsNumber){
        ArrayList<MessageObject> messagesList = new ArrayList<>();
        for(int i=0; i<elementsNumber; i++){
            boolean isLeft = true;
            if(i%3==1)isLeft=false;
            MessageObject messageObject = new MessageObject(isLeft, getRandomText());
            messagesList.add(messageObject);
        }
        return messagesList;
    }
}
