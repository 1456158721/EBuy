package cn.qd.cas.client1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Client1Controller {


    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "Hello Cas";
    }

}
