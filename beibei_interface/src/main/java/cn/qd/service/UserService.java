package cn.qd.service;

import cn.qd.entity.User;

public interface UserService {

    public void sendCode(String phone);

    public void register(User user, String code);
}
