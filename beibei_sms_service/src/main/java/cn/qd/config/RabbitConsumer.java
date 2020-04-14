package cn.qd.config;


import cn.qd.service.SMSCodeService;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitConsumer {

    @Autowired
    private SMSCodeService smsCodeService;

    @RabbitListener(queues = {"${beibei.mq.queue}"})
    public void  smsCode(String msg){
        Map<String, String>  message = JSON.parseObject(msg, Map.class);
        String phone = message.get("phone");
        String code = message.get("code");

        smsCodeService.sendCode(phone, code);
    }
}
