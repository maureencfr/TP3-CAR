package com.example.demo.actor;
import akka.actor.UntypedActor;
import com.example.demo.message.WordMessage;

import java.util.Map;

public class Reducer extends UntypedActor{

    private Map<String, Long> occurences;

    public Map<String, Long> getOccurences() {
        return occurences;
    }

    public void setOccurences(Map<String, Long> occurences) {
        this.occurences = occurences;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof WordMessage m) {
            System.out.println("Mot : " + m.word());
            if(occurences.containsKey(m.word())){
                occurences.put(m.word(),occurences.get(m.word())+1);
            }
            System.out.println("Compte : " + occurences.get(m.word()));
        }
    }
}
