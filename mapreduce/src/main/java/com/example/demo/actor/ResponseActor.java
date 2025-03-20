package com.example.demo.actor;

import akka.actor.UntypedActor;

public class ResponseActor extends UntypedActor {

    private int occurrences;

    @Override
    public void onReceive(Object message) {
        if (message instanceof Long) {
            occurrences = ((Long) message).intValue();
            System.out.println("Réponse reçue : Nombre d'occurrences = " + occurrences);
            getSender().tell(occurrences, getSelf());
            getContext().stop(getSelf());
        }
    }

    public int getOccurrences() {
        return occurrences;
    }
}
