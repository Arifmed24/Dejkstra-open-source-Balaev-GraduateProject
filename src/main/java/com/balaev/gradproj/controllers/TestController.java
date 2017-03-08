package com.balaev.gradproj.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        String message = "Hello World";
        model.put("message", message);
        return "welcome";
    }
}
