package com.example.Intellibus.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class MainController {
    @GetMapping("/")
    public String index() {
        return "Hello, World!";
    }

    @PostMapping("path")
    public String postMethodName(@RequestBody String entity) {
        System.out.println(entity);
        return "Hello, " +entity;
    }
    
    @PutMapping("path/{id}")
    public String putMethodName(@PathVariable String id, @RequestBody String entity) {
        return "you entered: "+entity;
    }
}
