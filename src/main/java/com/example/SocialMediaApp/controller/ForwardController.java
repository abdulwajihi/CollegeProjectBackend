package com.example.SocialMediaApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {

    @RequestMapping(value = { "/", "/login", "/feed", "/{path:[^\\.]*}" })
    public String forward() {
        return "forward:/index.html";
    }
}
