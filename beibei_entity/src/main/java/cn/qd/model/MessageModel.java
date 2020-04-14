package cn.qd.model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private int code;
    private String message;

    public MessageModel(){
        this.code = 200;
        this.message = "操作成功";
    }

    public MessageModel(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
