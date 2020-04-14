package cn.qd.controller.config;

import cn.qd.model.MessageModel;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public MessageModel error(Exception e){
        System.out.println("进入 全局异常 ");
        e.printStackTrace();
        return new MessageModel(500, "操作失败." + e.getMessage());
    }

}
