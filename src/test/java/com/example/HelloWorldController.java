package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/")
    public String hello(@RequestParam(required = false) String name) {
        return "Hello, welcome to iQuant YouTube Channel!";
    }

    @GetMapping("/redirect")
    public String redirect(@RequestParam String url) {
        // ⚠️ Open redirect vulnerability
        return "Redirecting to: " + url;
    }

    @GetMapping("/internal-api")
    public String internalApi() {
        return "Internal API response";
    }
}
