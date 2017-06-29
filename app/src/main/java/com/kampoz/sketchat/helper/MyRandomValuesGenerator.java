package com.kampoz.sketchat.helper;

import com.kampoz.sketchat.realm.GroupRealm;
import com.kampoz.sketchat.realm.SubjectRealm;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by wasili on 2017-04-15.
 */

public class MyRandomValuesGenerator {

    ArrayList<GroupRealm> groupsList = new ArrayList<>();

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

    String[] groupsNames = {"Projektanci", "Robienie mokeup'ów", "Artyści od swieta",
            "Plakaty ręcznie malowane w jajka wielkanocne",
            "Brodacze", "Rysowanie w 10 minut i 24 sekundy",
            "Projektowanie skomplikowanych systemów nadzoru"};

    String[] subjects = {"Bardzo wazny temat", "Jeszcze wazniejszy temat",
            "Maecenas consectetur finibus finibus", "Ut id nisl convallis, pharetra libero ut, euismod mi. Mauris maximus nisi sit amet, suscipit justo. Vivamus cursus faucibus magna, ut tincidunt velit aliquam id.",
            "Donec iaculis tortor purus, sed pulvinar arcu scelerisque in", "Krótki temat", "Mauris malesuada augue sit amet varius gravida",
            "Curabitur mollis elit id sem tincidunt auctor. Aliquam venenatis justo sit amet magna pharetra ornare. Nam tincidunt, felis in blandit tristique, turpis sapien auctor diam, quis mollis est risus et metus."};


    public String getRandomMessageText(){
        Random random = new Random();
        return messages[random.nextInt(messages.length)];
    }

        /*  Nie działa, przyjmuje
        8tylko jedną wartośc zawsze.*/

    public boolean getrandomBoolean(){
        Random random = new Random();
        return random.nextBoolean();
    }

        //Generuje ArrayLIst z obiektami MessageRealm
    /*public ArrayList<MessageRealm> generateMessagesArrayList(int elementsNumber){
        ArrayList<MessageRealm> messagesList = new ArrayList<>();
        for(int i=0; i<elementsNumber; i++){
            boolean isLeft = true;
            if(i%3==1)isLeft=false;
            MessageRealm messageObject = new MessageRealm(isLeft, getRandomMessageText());
            messagesList.add(messageObject);
        }
        return messagesList;
    }*/

        //generuje losowe nazwy grup
    public void generateGroupsList(int groupsNumber){
        Realm realm = Realm.getDefaultInstance();

        for(int i=0; i<groupsNumber; i++){
            Random random = new Random();
            final GroupRealm groupRealm = new GroupRealm(i, groupsNames[random.nextInt(groupsNames.length)]);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(groupRealm);
                }
            });
        }
        realm.close();
    }

    public ArrayList<SubjectRealm> generateSubjectsList(int subjectsNumber){
        ArrayList<SubjectRealm> subjectsList = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<subjectsNumber; i++){
            SubjectRealm subject = new SubjectRealm();
            subject.setSubject("example subject");
            subjectsList.add(subject);
        }
        return subjectsList;
    }
}
