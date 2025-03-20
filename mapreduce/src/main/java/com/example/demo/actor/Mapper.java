package com.example.demo.actor;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.example.demo.message.LineMessage;
import com.example.demo.message.WordMessage;
import com.example.demo.service.AkkaService;

public class Mapper extends UntypedActor{

    private final AkkaService akkaService; // Référence au service

    private String[] words;

    public Mapper(AkkaService akkaService) {
        this.akkaService = akkaService;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof LineMessage m) {
            System.out.println("Ligne : " + m.line());
            words= m.line().split("\\s+");
            for(int i =0; i< words.length;i++){
                ActorRef targetReducer = akkaService.partition(words[i]);
                targetReducer.tell(new WordMessage(words[i]), getSelf());
            }
        }
    }
}
