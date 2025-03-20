package com.example.demo.actor;
import akka.actor.UntypedActor;
import com.example.demo.message.WordMessage;

import java.util.HashMap;
import java.util.Map;

public class Reducer extends UntypedActor{

    private Map<String, Long> occurences = new HashMap<>();

    public Map<String, Long> getOccurences() {
        return occurences;
    }

    public void setOccurences(Map<String, Long> occurences) {
        this.occurences = occurences;
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof WordMessage m) {
            String word = m.word();
            occurences.put(word, occurences.getOrDefault( word, 0L) + 1);
            System.out.println("Reducer [" + getSelf().path().name() + "] - " +
                    "Mot: " + word + ", Compteur: " + occurences.get(word));

        }
        else if (message instanceof String w) {
            getSender().tell(occurences.getOrDefault(w, 0L), getSelf());
        }
    }
}
