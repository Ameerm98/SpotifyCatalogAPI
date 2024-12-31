package com.example.catalog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello from API! There is no UI here...";
    }

    @GetMapping("/internal")
    public void internalRequests(){
        return;
    }

}