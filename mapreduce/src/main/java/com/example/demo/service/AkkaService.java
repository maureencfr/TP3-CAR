package com.example.demo.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.example.demo.actor.Reducer;
import com.example.demo.actor.ResponseActor;
import com.example.demo.message.LineMessage;
import org.springframework.stereotype.Service;
import com.example.demo.actor.Mapper;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import akka.pattern.Patterns;

@Service
public class AkkaService {

    private ActorSystem system;
    private ActorRef[] mappers;
    private ActorRef[] reducers;
    private Inbox inbox;

    public void init(){
        system = ActorSystem.create("Mapreduce");
        inbox = Inbox.create(system);
        System.out.println("Initialisation des acteurs...");
        var mapper1 = system.actorOf( Props.create(Mapper.class,()-> new Mapper(this)), "mapper1" );
        var mapper2 = system.actorOf( Props.create(Mapper.class,()-> new Mapper(this)), "mapper2" );
        var mapper3 = system.actorOf( Props.create(Mapper.class,()-> new Mapper(this)), "mapper3" );
        var reduce1 = system.actorOf( Props.create(Reducer.class), "reducer1" );
        var reduce2 = system.actorOf( Props.create(Reducer.class), "reducer2" );
        mappers= new ActorRef[]{mapper1, mapper2, mapper3};
        reducers= new ActorRef[]{reduce1, reduce2};
        System.out.println("Liste des mappers : " + Arrays.toString(mappers));
        System.out.println("Liste des reducers : " + Arrays.toString(reducers));
    }

    public void distributeLines(List<String> lines) throws TimeoutException {
        for (int i = 0; i < lines.size(); i++) {
            ActorRef targetMapper = mappers[i % mappers.length];
            targetMapper.tell(new LineMessage(lines.get(i)), ActorRef.noSender());
        }
    }

    public ActorRef partition(String word) {
        int hash = word.hashCode(); //code unique pour chaque mot
        int reducerIndex = Math.abs(hash) % reducers.length; //index en faisant la valeur absolue du code modulo le nombre de reducers
        System.out.println("Le mot : " + word + " : va au reducer numéro : " + (reducerIndex));
        return reducers[reducerIndex];
    }

    public int getWordOccurences(String word) throws Exception {
        ActorRef wordReducer = partition(word);

        Future<Object> future = Patterns.ask(wordReducer, word, 1000);

        try {
            Long occurrences = (Long) Await.result(future, Duration.create(1, TimeUnit.SECONDS));
            return occurrences.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
