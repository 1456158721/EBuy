package cn.qd.service;

import cn.qd.dao.UserDao;
import cn.qd.entity.User;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService{

    @Value("${beibei.mq.queue}")
    private String queueName;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendCode(String phone) {
        //1 产生验证码
        Random random = new Random();
        int code = random.nextInt(999999);
        if(code < 100000){
            code += 100000;
        }
        //2 添加到缓存
        //redisTemplate.boundValueOps("code_"+phone).set(code+"");
        //设置过期时间
        //redisTemplate.boundValueOps("code_"+phone).expire(5, TimeUnit.MINUTES);

        Map<String, String> message = new HashMap<>();
        message.put("phone", phone);
        message.put("code", code+"");

        String json = JSON.toJSONString(message);

        //3 发送消息
        rabbitTemplate.convertAndSend("", queueName, json);

    }


    public void register(User user, String code) {

        String phonecode = (String)redisTemplate.boundValueOps("code_"+user.getPhone()).get();
        if(phonecode == null){
            throw new RuntimeException("验证码 失效了");
        }
        if(!phonecode.equals(code)){
            throw new RuntimeException("验证码 错误");
        }

        if(user.getUsername() == null){
            user.setUsername(user.getPhone());
        }

        //校验用户名 是否重复
        User u = new User();
        u.setUsername(user.getUsername());
        long c = userDao.selectCount(u);
        if(c > 0){
            throw new RuntimeException("用户名已经存在");
        }

        //补充数据
        user.setCreated(new Date());
        user.setUpdated(new Date());
        user.setStatus("1");
        user.setPoints(0);

        userDao.insert(user);
    }
}
