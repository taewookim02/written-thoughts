package com.written.app.hello;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/api/test")
    @CrossOrigin(origins = "http://localhost:3000")
    public Map<String, String> hello() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "hello world!!!!");
        return response;
    }
}
