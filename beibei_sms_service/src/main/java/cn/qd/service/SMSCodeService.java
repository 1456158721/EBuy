package cn.qd.service;

import org.springframework.stereotype.Service;

@Service
public class SMSCodeService {

    public void sendCode(String phone, String code){
        System.out.println("发送校验码: " + phone + "\t" + code);

    }
}

