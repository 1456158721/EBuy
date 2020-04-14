package com.test.cas.client2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Client2Controller {


    @GetMapping("/test2")
    @ResponseBody
    public String test(){
        return "Hello2 Cas";
    }

}
