package com.example.demo.controllers;

import com.example.demo.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/message")
    public Message sayMessage() {
        return new Message("Hello from Spring Boot!");
    }

}
