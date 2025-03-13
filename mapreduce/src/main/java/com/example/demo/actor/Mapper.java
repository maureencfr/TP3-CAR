package com.example.demo.actor;
import akka.actor.UntypedActor;
import com.example.demo.message.LineMessage;
import com.example.demo.message.WordMessage;

public class Mapper extends UntypedActor{

    private String[] words;

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
            words= m.line().split(" ");
            for(int i =0; i< words.length;i++){
                getSender().tell(new WordMessage(words[i]), getSelf());
            }
        }
    }
}
