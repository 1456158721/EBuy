package cn.qd.user.controller;

import cn.qd.model.MessageModel;
import cn.qd.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;


    //发送验证码
    @RequestMapping("/sendCode")
    @ResponseBody
    public MessageModel sendCode(@RequestParam  String phone){
        userService.sendCode(phone);
        return new MessageModel();
    }


    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    //注册方法
    @RequestMapping("/addUser")
    public String registerUser(){

        return "register";
    }


    @GetMapping("/center")
    public String userCenter(){

        //得到用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return "center-index";
    }


}
