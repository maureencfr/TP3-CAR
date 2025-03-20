package com.example.demo.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.example.demo.actor.ResponseActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.example.demo.service.AkkaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    public RedirectView processFile(@RequestParam("file") MultipartFile file) throws TimeoutException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        service.distributeLines(lines);
        return new RedirectView("/home");
    }

    @PostMapping("/count")
    public String getWordOccurences(@RequestParam String word, Model model) throws Exception {

        int res = service.getWordOccurences(word);

        model.addAttribute("word", word);
        model.addAttribute("occurrences", res);
        return "mapreduce";
    }
}
