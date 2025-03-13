package com.example.demo.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import com.example.demo.actor.Reducer;
import com.example.demo.message.LineMessage;
import com.example.demo.message.WordMessage;
import org.springframework.stereotype.Service;
import com.example.demo.actor.Mapper;
import scala.concurrent.duration.FiniteDuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class AkkaService {

    private ActorRef[] mappers;
    private ActorRef[] reducers;
    private Inbox inbox;

    public void init(){
        ActorSystem system = ActorSystem.create("Mapreduce");
        inbox = Inbox.create(system);
        System.out.println("Initialisation des acteurs...");
        var mapper1 = system.actorOf( Props.create(Mapper.class), "mapper1" );
        var mapper2 = system.actorOf( Props.create(Mapper.class), "mapper2" );
        var mapper3 = system.actorOf( Props.create(Mapper.class), "mapper3" );
        var reduce1 = system.actorOf( Props.create(Reducer.class), "reducer1" );
        var reduce2 = system.actorOf( Props.create(Reducer.class), "reducer2" );
        mappers= new ActorRef[]{mapper1, mapper2, mapper3};
        reducers= new ActorRef[]{reduce1, reduce2};
        System.out.println("Liste des mappers : " + Arrays.toString(mappers));
        System.out.println("Liste des reducers : " + Arrays.toString(reducers));
    }

    public void distributeLines(List<String> lines) throws TimeoutException {
        for(int i=0; i<lines.size(); i+=3){
            inbox.send( mappers[0], new LineMessage(lines.get(i)));
            Object reply = inbox.receive(FiniteDuration.create(5, TimeUnit.SECONDS));
            if (reply instanceof WordMessage rm) {
                System.out.println("Mot : " + rm.word());
            }
        }
        for(int i=1; i<lines.size(); i+=3){
            inbox.send( mappers[1], new LineMessage(lines.get(i)));
            Object reply = inbox.receive(FiniteDuration.create(5, TimeUnit.SECONDS));
            if (reply instanceof WordMessage rm) {
                System.out.println("Mot : " + rm.word());
            }
        }
        for(int i=2; i<lines.size(); i+=3){
            inbox.send( mappers[2], new LineMessage(lines.get(i)));
            Object reply = inbox.receive(FiniteDuration.create(5, TimeUnit.SECONDS));
            if (reply instanceof WordMessage rm) {
                System.out.println("Mot : " + rm.word());
            }
        }
    }

}
