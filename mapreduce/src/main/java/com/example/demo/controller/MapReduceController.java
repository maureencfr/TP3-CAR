package com.example.demo.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.example.demo.service.AkkaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.concurrent.TimeoutException;

@Controller
public class MapReduceController {
    @Autowired
    private AkkaService service;


    @GetMapping("/home")
    public String displayHomePage() {
        return "mapreduce";
    }

    @PostMapping("/init")
    public RedirectView initActors() {
        service.init();
        return new RedirectView("/home");
    }

    @PostMapping("/process")
    public RedirectView processFile(@RequestBody List<String> lines) throws TimeoutException {
        service.distributeLines(lines);
        return new RedirectView("/home");
    }

    @GetMapping("/count/{word}")
    public RedirectView getWordOccurences(@PathVariable String word) {
        //service.getWordOccurences(word);
        return new RedirectView("/home");
    }
}
